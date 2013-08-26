Quoddy
========

Quoddy - an Open Source Enterprise Social Network - is part of the Fogcutter suite of tools for building intelligent applications.

Why "Quoddy?"
----------------

We like lighthouses, so Quoddy is named after the famous West Quoddy Head Lighthouse.

Ok, but what does it do?
--------------------------

Quoddy is an Enterprise Social Network. With an intereface similar to consumer social networks like
Facebook or G+, Quoddy builds on the APIs for social-graph management, ActivityStrea.ms, activity profiling, 
tagging, and user profiles. Quoddy Provides the front-end  for managing connections and for letting users 
provide information about themselves, their interests, etc. as well as viewing their news feed.  Quoddy also
interoperates with Hatteras, our Busienss Events Subscription engine, to allow subscriptions to business
events and workflow activities from the backend ESB/SOA and BPM systems.  And while Quoddy supports apps via
OpenSocial, unlike Facebook there is no silly Pirates vs. Ninjas or Farmville stuff.

How does it work?
--------------------

Quoddy stores information about users, relationships between those users (eg, "friend", "follower," etc), groups,
relationships between users and "things" that are of interest to the user, and provides for management of those
relationships, as well as using those relationships to show a user updates regarding activity within their
network (of friends, followers and other "things).

Quoddy can also be combined with [Neddick](https://github.com/fogbeam/Neddick) and [Heceta](https://github.com/fogbeam/Heceta)
to provide a rich, tightly integrated platform for knowledge discovery, navigation and management.

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
