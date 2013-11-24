package org.fogbeam.quoddy.jaxrs

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response
import static org.grails.jaxrs.response.Responses.*

import org.fogbeam.foaf.api.FoafOnlineAccount
import org.fogbeam.foaf.api.FoafPerson
import org.fogbeam.foaf.model.FoafOnlineAccountImpl
import org.fogbeam.foaf.model.FoafPersonImpl
import org.fogbeam.quoddy.User
import org.fogbeam.quoddy.jaxrs.collection.FOAFPersonCollection


@Path('/api/foaf/user')
@Consumes(['application/xml','application/json'])
@Produces(['application/rdf+xml','application/xml', 'text/xml'])
class FOAFPersonCollectionResource
{
	
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
