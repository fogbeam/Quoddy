package org.fogbeam.quoddy.stream

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement


import org.fogbeam.quoddy.User


/* TODO: map the fields of this class to the activitystrea.ms protocol */
/* SPEC: http://activitystrea.ms/specs/json/1.0/ */
/*
	In its simplest form, an activity consists of an actor, a verb, an an object, 
	and a target. It tells the story of a person performing an action on or with 
	an object -- "Geraldine posted a photo to her album" or "John shared a video". 
	In most cases these components will be explicit, but they may also be implied.

	It is a goal of this specification to provide sufficient metadata about an activity 
	such that a consumer of the data can present it to a user in a rich human-friendly 
	format. This may include constructing readable sentences about the activity that 
	occurred, visual representations of the activity, or combining similar activities 
	for display. 
*/


/*


Following is a simple, minimal example of a JSON serialized activity:

  {
    "published": "2011-02-10T15:04:55Z",
    "actor": {
      "url": "http://example.org/martin",
      "objectType" : "person",
      "id": "tag:example.org,2011:martin",
      "image": {
        "url": "http://example.org/martin/image",
        "width": 250,
        "height": 250
      },
      "displayName": "Martin Smith"
    },
    "verb": "post",
    "object" : {
      "url": "http://example.org/blog/2011/02/entry",
      "id": "tag:example.org,2011:abc123/xyz"
    },
    "target" : {
      "url": "http://example.org/blog/",
      "objectType": "blog",
      "id": "tag:example.org,2011:abc123",
      "displayName": "Martin's Blog"
    }
  }

*/


/*

  {
    "items" : [
      {
        "published": "2011-02-10T15:04:55Z",
        "foo": "some extension property",
        "generator": {
          "url": "http://example.org/activities-app"
        },
        "provider": {
          "url": "http://example.org/activity-stream"
        },
        "title": "Martin posted a new video to his album.",
        "actor": {
          "url": "http://example.org/martin",
          "objectType": "person",
          "id": "tag:example.org,2011:martin",
          "foo2": "some other extension property",
          "image": {
            "url": "http://example.org/martin/image",
            "width": 250,
            "height": 250
          },
          "displayName": "Martin Smith"
        },
        "verb": "post",
        "object" : {
          "url": "http://example.org/album/my_fluffy_cat.jpg",
          "objectType": "photo",
          "id": "tag:example.org,2011:my_fluffy_cat",
          "image": {
            "url": "http://example.org/album/my_fluffy_cat_thumb.jpg",
            "width": 250,
            "height": 250
          }
        },
        "target": {
          "url": "http://example.org/album/",
          "objectType": "photo-album",
          "id": "tag:example.org,2011:abc123",
          "displayName": "Martin's Photo Album",
          "image": {
            "url": "http://example.org/album/thumbnail.jpg",
            "width": 250,
            "height": 250
          }
        }
      }
    ]
  }
*/


/*

	3.2.  ActivityStreamItem Serialization

	Property				Value								Description
	
	actor 					Object								Describes the entity that performed the activity. An activity MUST contain one actor property whose value is a single Object.
	content 				JSON [RFC4627] String				Natural-language description of the activity encoded as a single JSON String containing HTML markup. Visual elements such as thumbnail images MAY be included. An activity MAY contain a content property.
	generator 				Object								Describes the application that generated the activity. An activity MAY contain a generator property whose value is a single Object.
	icon 					Media Link							Description of a resource providing a visual representation of the object, intended for human consumption. The image SHOULD have an aspect ratio of one (horizontal) to one (vertical) and SHOULD be suitable for presentation at a small size. An activity MAY have an icon property.
	id 						JSON [RFC4627] String				Provides a permanent, universally unique identifier for the activity in the form of an absolute IRI [RFC3987]. An activity SHOULD contain a single id property. If an activity does not contain an id property, consumers MAY use the value of the url property as a less-reliable, non-unique identifier.
	object 					Object								Describes the primary object of the activity. For instance, in the activity, "John saved a movie to his wishlist", the object of the activity is "movie". An activity SHOULD contain an object property whose value is a single Object. If the object property is not contained, the primary object of the activity MAY be implied by context.
	published 				[RFC3339] date-time					The date and time at which the activity was published. An activity MUST contain a published property.
	provider 				Object								Describes the application that published the activity. Note that this is not necessarily the same entity that generated the activity. An activity MAY contain a provider property whose value is a single Object.
	target 					Object								Describes the target of the activity. The precise meaning of the activity's target is dependent on the activities verb, but will often be the object the English preposition "to". For instance, in the activity, "John saved a movie to his wishlist", the target of the activity is "wishlist". The activity target MUST NOT be used to identity an indirect object that is not a target of the activity. An activity MAY contain a target property whose value is a single Object.
	title 					JSON [RFC4627] String				Natural-language title or headline for the activity encoded as a single JSON String containing HTML markup. An activity MAY contain a title property.
	updated 				[RFC3339] date-time					The date and time at which a previously published activity has been modified. An ActivityStreamItem MAY contain an updated property.
	url 					JSON [RFC4627] String				An IRI [RFC3987] identifying a resource providing an HTML representation of the activity. An activity MAY contain a url property.
	verb 					JSON [RFC4627] String				Identifies the action that the activity describes. An activity SHOULD contain a verb property whose value is a JSON String that is non-empty and matches either the "isegment-nz-nc" or the "IRI" production in [RFC3339]. Note that the use of a relative reference other than a simple name is not allowed. If the verb is not specified, or if the value is null, the verb is assumed to be "post".

*/

/*

	3.4.  Object Serialization

	Property				Value								Description

	attachments 			JSON [RFC4627] Array of Objects		A collection of one or more additional, associated objects, similar to the concept of attached files in an email message. An object MAY have an attachments property whose value is a JSON Array of Objects.
	author 					Object								Describes the entity that created or authored the object. An object MAY contain a single author property whose value is an Object of any type. Note that the author field identifies the entity that created the object and does not necessarily identify the entity that published the object. For instance, it may be the case that an object created by one person is posted and published to a system by an entirely different entity.
	content 				JSON [RFC4627] String				Natural-language description of the object encoded as a single JSON String containing HTML markup. Visual elements such as thumbnail images MAY be included. An object MAY contain a content property.
	displayName 			JSON [RFC4627] String				A natural-language, human-readable and plain-text name for the object. HTML markup MUST NOT be included. An object MAY contain a displayName property. If the object does not specify an objectType property, the object SHOULD specify a displayName.
	downstreamDuplicates 	JSON [RFC4627] Array of Strings		A JSON Array of one or more absolute IRI's [RFC3987] identifying objects that duplicate this object's content. An object SHOULD contain a downstreamDuplicates property when there are known objects, possibly in a different system, that duplicate the content in this object. This MAY be used as a hint for consumers to use when resolving duplicates between objects received from different sources.
	id 						JSON [RFC4627] String				Provides a permanent, universally unique identifier for the object in the form of an absolute IRI [RFC3987]. An object SHOULD contain a single id property. If an object does not contain an id property, consumers MAY use the value of the url property as a less-reliable, non-unique identifier.
	image 					Media Link							Description of a resource providing a visual representation of the object, intended for human consumption. An object MAY contain an image property whose value is a Media Link.
	objectType 				JSON [RFC4627] String				Identifies the type of object. An object MAY contain an objectType property whose value is a JSON String that is non-empty and matches either the "isegment-nz-nc" or the "IRI" production in [RFC3987]. Note that the use of a relative reference other than a simple name is not allowed. If no objectType property is contained, the object has no specific type.
	published 				[RFC3339] date-time					The date and time at which the object was published. An object MAY contain a published property.
	summary 				JSON [RFC4627] String				Natural-language summarization of the object encoded as a single JSON String containing HTML markup. Visual elements such as thumbnail images MAY be included. An activity MAY contain a summary property.
	updated 				[RFC3339] date-time					The date and time at which a previously published object has been modified. An Object MAY contain an updated property.
	upstreamDuplicates 		JSON [RFC4627] Array of Strings		A JSON Array of one or more absolute IRI's [RFC3987] identifying objects that duplicate this object's content. An object SHOULD contain an upstreamDuplicates property when a publisher is knowingly duplicating with a new ID the content from another object. This MAY be used as a hint for consumers to use when resolving duplicates between objects received from different sources.
	url 					JSON [RFC4627] String				An IRI [RFC3987] identifying a resource providing an HTML representation of the object. An object MAY contain a url property 
 
*/

class ActivityStreamItem implements Serializable
{
	
	public ActivityStreamItem()
	{
		this.uuid = java.util.UUID.randomUUID().toString();
	}

	static mapping = 
	{
		tablePerHierarchy false
	}
		
	static constraints = {
		
		content( size: 0..1024, nullable:true);
		updated(nullable:true);
		icon(nullable:true);
		uuid( nullable: false);
		actorUuid(nullable:false);
		actorUrl(nullable:true);
		actorContent(nullable:true);
		actorDisplayName(nullable:true);
		actorObjectType(nullable:false);
		actorImageUrl(nullable:true);
		actorImageHeight(nullable:true);
		actorImageWidth(nullable:true);

		streamObject( nullable:true );
		objectClass( nullable:false );
		
		objectUuid(nullable:true);
		objectUrl(nullable:true);
		objectContent(nullable:true);
		objectSummary(nullable:true);
		objectDisplayName(nullable:true);
		objectObjectType(nullable:true);
		objectImageUrl(nullable:true);
		objectImageHeight(nullable:true);
		objectImageWidth(nullable:true);
		
		targetUuid(nullable:false);
		targetUrl(nullable:true);
		targetContent(nullable:true);
		targetDisplayName(nullable:true);
		targetObjectType(nullable:false);
		targetImageUrl(nullable:true);
		targetImageHeight(nullable:true);
		targetImageWidth(nullable:true);
				
		generatorUrl(nullable:true);
		providerUrl(nullable:true);
		
		dateCreated()
	}
	
	static transients = ['templateName'];
		
	
	String 			content;
	Date			published;
	String			title;
	Date			updated;
	URL				url;
	String			verb;
	URL 			icon;
	
	
	String 			actorUuid;
	String 			actorUrl;
	String			actorContent;
	String 			actorDisplayName;
	String 			actorObjectType;
	String 			actorImageUrl;
	String 			actorImageHeight;
	String			actorImageWidth;


	
	String 			objectUuid;
	String			objectUrl;
	String			objectContent;
	String			objectSummary;
	String			objectDisplayName;
	String			objectObjectType;
	String			objectImageUrl;
	String			objectImageHeight;
	String			objectImageWidth;
		

	String			targetUrl;
	String			targetUuid;
	String			targetContent;
	String			targetDisplayName;
	String 			targetObjectType;
	String			targetImageUrl;
	String 			targetImageHeight;
	String			targetImageWidth;
	
	String 			generatorUrl
	String			providerUrl;

	String  		name;
	String  		uuid;
	User 			owner;
	StreamItemBase 	streamObject;
	String 			objectClass;
	Date 			dateCreated;
	
	
	public String getTemplateName()
	{
		if( streamObject != null )
		{
            log.trace( "streamObject: ${streamObject.class.name}");
            log.trace( "streamObject: ${streamObject.toString()}");
            
            
			// println "returning streamObject.getTemplateName() value";
			return streamObject.getTemplateName();
		}
		else
		{
			// println "returning renderActivity";
			return "/renderActivity";	
		}
	}
}
