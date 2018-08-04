package org.fogbeam.quoddy.jaxrs

// import static org.grails.jaxrs.response.Responses.*
import static org.grails.plugins.jaxrs.response.Responses.*;

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

import org.apache.commons.io.input.ReaderInputStream
import org.fogbeam.foaf.api.FoafOnlineAccount
import org.fogbeam.foaf.api.FoafPerson
import org.fogbeam.foaf.model.FoafOnlineAccountImpl
import org.fogbeam.foaf.model.FoafPersonImpl
import org.fogbeam.quoddy.AccountRole
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.jaxrs.collection.FOAFPersonCollection
import org.fogbeam.quoddy.profile.ContactAddress
import org.fogbeam.quoddy.profile.Profile

import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.rdf.model.Property
import com.hp.hpl.jena.rdf.model.ResIterator
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.sparql.vocabulary.FOAF
import com.hp.hpl.jena.vocabulary.DCTerms
import com.hp.hpl.jena.vocabulary.RDF


@Path('/api/foaf/user')
@Consumes(['application/rdf+xml', 'application/xml'])
@Produces(['application/rdf+xml','application/xml', 'text/xml'])
class FOAFPersonCollectionResource {

	def profileService;
	def userService;


	
	@GET
	Response readAll() {

		// println "readAll";
		List<User> allUsers = User.findAll();

		println "found ${allUsers.size()} users";
		FoafPersonTransformer trans = new FoafPersonTransformer();

		List<FoafPerson> foafPersons = new ArrayList<FoafPerson>();

		for( User user : allUsers )
		{
			FoafPerson person = trans.transformUserToFoafPerson( user )
			foafPersons.add( person );
		}


		FOAFPersonCollection foafCollection = new FOAFPersonCollection();

		foafCollection.addAll( foafPersons );

		ok( foafCollection );
	}


	// add user via FOAF
	@POST
	@Produces( "text/plain")
	Response addUser( String input )
	{
		log.info( "input to addUser: " + input );

		Model model = null;

		try
		{
			model = ModelFactory.createDefaultModel() ;

			
			AccountRole userRole = userService.findAccountRoleByName( "user" );
			AccountRole adminRole = userService.findAccountRoleByName( "admin" );
			
			
			Property hasEmailProperty = model.createProperty("http://www.w3.org/2006/vcard/ns#hasEmail");
			Property passwordProperty = model.createProperty( "http://fogbeam.org/quoddy/ns/password" );
			
			InputStream inputStream = new ReaderInputStream(new StringReader(input));

			model.read( inputStream, "" );


			ResIterator iterator = model.listResourcesWithProperty(RDF.type, FOAF.Person);

			while( iterator.hasNext() )
			{
				Resource foafPerson = iterator.nextResource();
				String firstName = foafPerson.getProperty(FOAF.firstName).getString();
				String lastName = foafPerson.getProperty(FOAF.family_name).getString();
				String password = foafPerson.getProperty( passwordProperty ).getString();
				String userId = foafPerson.getProperty(FOAF.accountName).getString();
				String identifier = foafPerson.getProperty(DCTerms.identifier).getString();

				identifier = identifier.split("\\:")[1];
				
				
				Resource email = foafPerson.getPropertyResourceValue(hasEmailProperty);

				String suppliedEmail = email?.toString();
				println "suppliedEmail: $suppliedEmail";
				
				// println "suppliedEmail: " + suppliedEmail;

				if( suppliedEmail != null )
				{
					if( suppliedEmail.startsWith("mailto"))
					{
						suppliedEmail = suppliedEmail.split( "\\:")[1];
					}
				}
				
				
				// create a user with these parameters

				User newUser = userService.findUserByUuid( identifier );

				if( newUser != null )
				{
					println "Found existing ${identifier} user!";
					continue;
					
				}
				else
				{
					println "Could not find user ${identifier}";
					println "Creating new ${identifier} user";
					newUser = new User();
					newUser.uuid = identifier;
					newUser.displayName = firstName + " " + lastName;
					newUser.firstName = firstName;
					newUser.lastName = lastName;
					newUser.email = suppliedEmail;
					newUser.userId = userId;
					newUser.password = password;
					newUser.dateCreated = new Date();
					newUser.bio = "bio";

					Profile newUserProfile = new Profile();
					newUserProfile.setOwner( newUser );
					newUserProfile.dateCreated = new Date();
					newUser.profile = newUserProfile;

					newUser.addToRoles( userRole );
					newUser.addToRoles( adminRole );

					userService.createUser( newUser );

					
					ContactAddress newEmail = new ContactAddress();
					newEmail.setAddress(suppliedEmail);
					newEmail.setServiceType(ContactAddress.EMAIL);
					newEmail.setPrimaryInType(true);
					newEmail.setDateCreated(new Date());
					newEmail.profile = newUserProfile;
					
					if( !newEmail.save())
					{
						newEmail.errors.allErrors.each { println it; }
					}

					newUser.profile.addToContactAddresses( newEmail );
					profileService.updateProfile( newUser.profile );					
					
				}

			}
		}
		finally
		{
			if( model != null )
			{
				model.close();
			}
		}


		ok("OK");
	}

	// update user via FOAF
	@PUT
	@Produces( "text/plain")
	Response updateUser( String input )
	{
		log.info( "input to addUser: " + input );

		Model model = null;

		try
		{
			model = ModelFactory.createDefaultModel() ;

			Property hasEmailProperty = model.createProperty("http://www.w3.org/2006/vcard/ns#hasEmail");

			InputStream inputStream = new ReaderInputStream(new StringReader(input));

			model.read( inputStream, "" );


			ResIterator iterator = model.listResourcesWithProperty(RDF.type, FOAF.Person);

			while( iterator.hasNext() )
			{
				Resource foafPerson = iterator.nextResource();
				String firstName = foafPerson.getProperty(FOAF.firstName).getString();
				String lastName = foafPerson.getProperty(FOAF.family_name).getString();
				String identifier = foafPerson.getProperty(DCTerms.identifier).getString();

				identifier = identifier.split("\\:")[1];
				log.info( "identifier: " + identifier);

				// lookup this user by their identifier and then update the record with the input data
				Resource email = foafPerson.getPropertyResourceValue(hasEmailProperty);
				String suppliedEmail = email?.toString();

				// println "suppliedEmail: " + suppliedEmail;

				if( suppliedEmail != null )
				{
					if( suppliedEmail.startsWith("mailto"))
					{
						suppliedEmail = suppliedEmail.split( "\\:")[1];
					}

					List<User> users = User.executeQuery( "select user from User as user where user.uuid = :id", ['id':identifier] );

					User user = null;

					if( !users.isEmpty() )
					{
						user = users.get(0);
					}

					if( user != null )
					{

						// delete any existing "primary in type" email addresses
						ContactAddress.executeUpdate("delete from ContactAddress as ca where ca.primaryInType = true and ca.profile.id = :profileId", ['profileId':user.profile.id]);

						// println "user = " + user;
						ContactAddress newEmail = new ContactAddress();
						newEmail.setAddress(suppliedEmail);
						newEmail.setServiceType(ContactAddress.EMAIL);
						newEmail.setPrimaryInType(true);
						newEmail.setDateCreated(new Date());

						if( !newEmail.save())
						{
							user.errors.allErrors.each { println it; }
						}

						user.profile.addToContactAddresses( newEmail );
						profileService.updateProfile( user.profile );
					}
				}
			}
		}
		finally
		{
			if( model != null )
			{
				model.close();
			}
		}


		ok("OK");
	}


}

class FoafPersonTransformer
{
	public List<FoafPerson> transformUserToFoafPerson( List<User> users )
	{
		List<FoafPerson> foafPersons = new ArrayList<FoafPerson>();

		for( User user : users )
		{
			FoafPerson foafPerson = this.transformUserToFoafPerson( user );
			foafPersons.add( foafPerson );
		}

		return foafPersons;
	}

	public FoafPerson transformUserToFoafPerson( final User user )
	{
		FoafPerson person = new FoafPersonImpl();

		person.name =  user.fullName;
		person.firstName = user.firstName;
		person.surname = user.lastName;
		person.mbox = user.email;

		FoafOnlineAccount account = new FoafOnlineAccountImpl();

		account.setType( "Quoddy" );
		account.setIdentifier( user.uuid );
		account.setAccountName( user.userId );
		account.setAccountServiceHomepage( "http://localhost:8080/quoddy" );

		person.addAccount( account );

		return person;

	}
}
