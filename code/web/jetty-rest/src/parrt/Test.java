package parrt;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;


public class Test {

	public static void main2(String[] args) throws Exception {
		Server server = new Server(8081);

		ServletHolder sh = new ServletHolder(ServletContainer.class);
		sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass",
							"com.sun.jersey.api.core.PackagesResourceConfig");
		// look in my package parrt for TestResource and other annotations.
		sh.setInitParameter("com.sun.jersey.config.property.packages", "parrt");
		sh.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

		ServletContextHandler context =
			new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
		context.addServlet(sh, "/foo");

		server.start();
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(8081);
		ServletHolder servletHolder = new ServletHolder(org.glassfish.jersey.servlet.ServletContainer.class);

		// IMPORTANT: you have to specify the package where your resources are located in order for Jetty to pick them up
		servletHolder.setInitParameter("jersey.config.server.provider.packages", "parrt");

		ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
		context.addServlet(servletHolder, "/*");

		server.start();
		server.join();
	}

	public static void main1(String[] args) throws Exception {
		Server server = new Server(8081);
//		ResourceHandler resource_handler = new ResourceHandler();
//		resource_handler.setDirectoriesListed(true);
//		resource_handler.setWelcomeFiles(new String[] { "index.html" });
//		resource_handler.setResourceBase(".");

//		ServletHandler servlet = new ServletHandler();
//		servlet.addServletWithMapping(org.glassfish.jersey.servlet.ServletContainer.class, "/foo");

		ServletContextHandler context =
			new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
		ServletHolder sh = new ServletHolder(ServletContainer.class);
		sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass",
							"com.sun.jersey.api.core.PackagesResourceConfig");
		//Set the package where the services reside
		sh.setInitParameter("com.sun.jersey.config.property.packages", "rest");
		sh.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
		context.addServlet(sh, "/");

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] {context, new DefaultHandler()});
//		handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
		server.setHandler(handlers);

		server.start();
		server.join();
	}
}
