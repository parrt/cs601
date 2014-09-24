// Derived from:
// http://download.eclipse.org/jetty/stable-9/xref/org/eclipse/jetty/embedded/FileServer.html
// Copyright (c) 1995-2014 Mort Bay Consulting Pty. Ltd.
// Released under Eclipse Public License v1.0 and Apache License v2.0

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class FileServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
		resource_handler.setResourceBase(".");                    // must set root dir

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, 	  // file handler
											 new DefaultHandler() // handles 404 etc...
		});
        server.setHandler(handlers);

        server.start();
        server.join();
    }

}
