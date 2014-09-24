// Derived from:
// http://download.eclipse.org/jetty/stable-9/xref/org/eclipse/jetty/embedded/FileServer.html
// Copyright (c) 1995-2014 Mort Bay Consulting Pty. Ltd.
// Released under Eclipse Public License v1.0 and Apache License v2.0

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletHandler;

public class MinimalServletsWithLogging {
    public static void main(String[] args) throws Exception {
        // Create a basic jetty server object that will listen on port 8080.  Note that if you set this to port 0
        // then a randomly available port will be assigned that you can either look in the logs for the port,
        // or programmatically obtain it for use in test cases.
        Server server = new Server(8080);
		HandlerCollection handlers = new HandlerCollection();
		server.setHandler(handlers);

        ServletHandler servlet = new ServletHandler();
		servlet.addServletWithMapping(HelloServlet.class, "/*");
		handlers.addHandler(servlet);

		handlers.addHandler(new DefaultHandler()); // must be after servlet it seems

		// log using NCSA (common log format)
		// http://en.wikipedia.org/wiki/Common_Log_Format
		NCSARequestLog requestLog = new NCSARequestLog();
		requestLog.setFilename("/tmp/yyyy_mm_dd.request.log");
		requestLog.setFilenameDateFormat("yyyy_MM_dd");
		requestLog.setRetainDays(90);
		requestLog.setAppend(true);
		requestLog.setExtended(true);
		requestLog.setLogCookies(false);
		requestLog.setLogTimeZone("GMT");
		RequestLogHandler requestLogHandler = new RequestLogHandler();
		requestLogHandler.setRequestLog(requestLog);
		handlers.addHandler(requestLogHandler);

		// Start things up! By using the server.join() the server thread will join with the current thread.
		// See "http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Thread.html#join()" for more details.
		server.start();
		server.join();
	}

	public static class HelloServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			response.setContentType("text/html");
            response.getWriter().println("<h1>Hello SimpleServlet</h1>");
        }
    }
}
