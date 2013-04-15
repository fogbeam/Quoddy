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

(IMPORTANT NOTE: This is under very heavy development, is very much "pre-alpha" and is nowhere near
ready to use for anything.  It does (usually) build, compile and run and there is useful functionality
implement (or partly implemented).  But the only real reason to run this now is if you're interested in
hacking on it.  If you're looking for something "ready to deploy," well... we're not there yet. Sorry) 

First, you'll need Groovy and Grails (and associated dependencies, such as a JVM) installed and
working.  If you can run the 'grails' command from the command line, you are ready to go.
Quoddy is currently based on Grails 1.3.6; making it work with different versions may take
some effort.  We develop with the latest version of Groovy (1.7.6 at the moment.)

(IMPORTANT NOTE: an LDAP server is no longer required to run Quoddy.  LDAP support for Users is
strictly optional.)

There are currently no binary distributions, so you'll have to get the code from source control.  The most direct
route is to do a 'git clone' on the Quoddy repository on Github.  

Once you have the code, you'll need to A. create a database, and configure your DataSource.groovy file.  Create an
empty database (preferably in postgres), and then point to it in the DataSource.groovy config.  None of this is
ready for production yet, so you might as well make the "Development" datasource and the "Production" datasource the 
same, so it works whether you "grails run-app" or war it up and deploy to a server.

Once you're pointing to a database (make sure the login credentials are right as well), just do a "grails run-app"
from inside the directory where the code is checked out.  Once the server finishes starting, browse to
http://localhost:8080/ and you should get the Quoddy homepage.  

You can login as testuser1 with a password of 'secret', or you can modify Bootstrap.groovy to create
different default users.

For help, see the fogcutter-dev Google Group, or check #fogcutter on Freenode.net IRC.	

Commercial Support
------------------

Commercial support is available from [Fogbeam Labs](http://www.fogbeam.com).  For more information on
Quoddy Enterprise, please visit [http://wwww.fogbeam.com/quoddy_enterprise.html](http://wwww.fogbeam.com/quoddy_enterprise.html).
