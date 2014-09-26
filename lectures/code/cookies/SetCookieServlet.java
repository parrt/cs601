import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SetCookieServlet extends HttpServlet {
	// Set-Cookie:user=parrt;Path=/;Expires=Thu, 25-Dec-2014 20:13:16 GMT
	public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		Cookie c = new Cookie("user", "parrt");
		c.setMaxAge( 3 * 30 * 24 * 60 * 60 ); // 3 months
		c.setPath( "/" );
		response.addCookie( c );
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);
		handler.addServletWithMapping(SetCookieServlet.class, "/set");
		server.start();
		server.join();
	}
}
