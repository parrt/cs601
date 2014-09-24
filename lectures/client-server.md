Client-server architecture
====

Raw notes on client/server stuff.

In some sense, we've had this model forever starting with the old time-sharing machines that connected to terminals. Terminals were incredibly stupid browsers you might say. The web dominates client/server architecture today so there more or less synonymous.

But, databases also typically represent a client/server architecture where the database is the server and their multiple clients. A client is typically a library that allows a programming language like Java or Python to connect to that database using a socket protocol.

Even mail servers are considered client/server architectures. Basically anything that receives requests over the net is a client/server architecture.

A server has multiple clients, e.g. browsers, connecting to it. This is basically called a two-tier system where the client is in one and the servers and the other.

A three-tier architecture is where the server is split into the business logic and typically the data storage or other resource management stuff. I tend not to get overly concerned with this kind of terminology.

![](http://webingineer.files.wordpress.com/2010/07/2layer.jpg)

One might even have a cloud of Web servers that really just make simple request to a main application server. They themselves would do HTML page generation and other things like that to take load off of the main application server.

![](http://weblogs.foxite.com/photos/1000.257.6938.cs003.jpg)

Clients make requests to the server and they respond with appropriate data in some format or store the data sent from the client. In many ways this can resemble just a remote method call.

The terms thin and thick clients really talks about how sophisticated the client is.  An old terminal is a thin client. A browser that just displays static HTML is a thin client probably. a browser that runs JavaScript is a thick client but could also be considered then if it doesn't do much beyond displaying some data.

Client/server architectures are potentially very scalable as much of the computation can be spread to the client. Secondly, we can actually make multiple servers look like a single server with a load balancer. That's a good way to scale as long as there's not a single chokepoint such as a single database machine. Databases also can be scaled by sharding and replication.

Client/server architecture should be distinguished from peer-to-peer architecture where any machine can talk to any other machine.

Because we use sockets, clients and servers can be implemented in different languages as long as they speak the same protocol.
