// Derived from:
// http://download.eclipse.org/jetty/stable-9/xref/org/eclipse/jetty/embedded/FileServer.html
// Copyright (c) 1995-2014 Mort Bay Consulting Pty. Ltd.
// Released under Eclipse Public License v1.0 and Apache License v2.0

import org.eclipse.jetty.server.Server;

public class OneHandler
{
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new HelloHandler());

        server.start();
        server.join();
    }
}
