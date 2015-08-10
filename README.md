Quoddy
========

Quoddy - an Open Source Enterprise Social Network - is part of the Fogcutter suite of tools for building intelligent applications.

Why "Quoddy?"
----------------

We like lighthouses, so Quoddy is named after the famous West Quoddy Head Lighthouse.

Ok, but what does it do?
--------------------------

Quoddy is an Open Source Enterprise Social Network, based on Groovy & Grails, and making up one component of 
the [Fogcutter Suite](http://code.google.com/p/fogcutter).   Quoddy shares a common "look and feel", and 
substantial functionality, with consumer facing Social Networks like [Facebook](http://www.facebook.com) or 
[Google+](http://plus.google.com) as well as proprietary Enterprise Social Network products like the products 
from [Jive Software](http://www.jivesoftware.com) and [Yammer](http://www.yammer.com) among others.   
You could call Quoddy "Facebook for the Enterprise" but that would be oversimplifying a bit.  Quoddy is intended 
mainly for organizational use and therefore has capabilities which make it uniquely suited to provide the social 
fabric to support organizational knowledge sharing, knowledge transfer, collaboration and decision support.

Relative to consumer Social Networks and other Enterprise Social Networking products, Quoddy adds features like:

* Calendar Feed Subscriptions - Quoddy supports subscriptions to any calendaring server which provides an iCal feed, so you can see your  upcoming events rendered in your feed alongside other important events.  Future releases will support CalDAV and interactive editing of Calendar Events from right in the stream.
* RSS/Atom Feed Subscriptions - Quoddy supports subscriptions to any arbitrary RSS feed, so you can find crucial information in your feed.  Subscribe to internal blog servers, document management systems, wikis, and any other repository which provides RSS, or subscribe to your favorite blogs, Google Alerts, and other external information sources.
* Business Events Subscriptions - using the [Hatteras](https://github.com/fogbeam/Hatteras) Business Events Subscription engine, Quoddy allows users to define subscriptions to business events right off of the enterprise SOA/ESB backbone.  Find out exactly what is going on in your enterprise, in real-time with Quoddy and Hatteras.
* BPM Integration - Quoddy supports subscribing to User Tasks from BPM / Workflow engines.  We currently only support Activiti, the leading Open Source BPM offering, but support for other workflow products will be coming in future releases.
* Fine-grained User Stream definitions.  Quoddy provides robust support for creating different "user stream" definitions, which filter content by type, source, group, or other criteria, and makes it trivially easy to quickly change the current Stream.  For example, you may tke a quick peek at an enterprise-wide view of all the activity from every user, and then quickly switch back to a personalized view which includes only your BPM User Tasks.  Quoddy emphasizes putting *you* in control of the content which is rendered in your stream, helping avoid information overload.
* [ActivityStrea.ms protocol](http://activitystrea.ms) support using REST.  Quoddy exposes a HTTP interface for external systems to post ActivityStrea.ms messages.  This allows our integration with [Neddick](http://code.google.com/p/neddick/) as well as other 3rd party products.


How does it work?
--------------------

Quoddy stores information about users, relationships between those users (eg, "friend", "follower," etc), groups,
relationships between users and "things" that are of interest to the user, and provides for management of those
relationships, as well as using those relationships to show a user updates regarding activity within their
network (of friends, followers and other "things).

Quoddy can also be combined with [Neddick](https://github.com/fogbeam/Neddick) and [Heceta](https://github.com/fogbeam/Heceta)
to provide a rich, tightly integrated platform for knowledge discovery, navigation and management.

Where's the issue tracker?
--------------------

Right here:  http://dev.fogbeam.org/bugzilla/describecomponents.cgi?product=Quoddy

What about OpenSocial?
--------------------------

Work is currently underway to add OpenSocial container support, using Apache Shindig.  When finished, this work
will allow a Quoddy instance to host any valid OpenSocial application.

How to build & run Quoddy?
----------------------------

Instructions for deploying the latest release can be found at

https://github.com/fogbeam/Quoddy/releases/edit/v0.0.0-tpr1

Once Quoddy is installed and running, browse to http://localhost:8080 and you should get the Quoddy homepage.  

You can login as testuser1 with a password of 'secret', or you can modify Bootstrap.groovy to create
different default users.

For help, see the fogcutter-dev Google Group, or check #fogcutter on Freenode.net IRC.	

Commercial Support
------------------

Commercial support is available from [Fogbeam Labs](http://www.fogbeam.com).  For more information on
Quoddy Enterprise, please visit [http://www.fogbeam.com/quoddy_enterprise.html](http://www.fogbeam.com/quoddy_enterprise.html).
