import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CookieServlet extends HttpServlet {
	public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		String old = CookieMonster.getCookieValue(request, "user");

		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("old cookie: "+old);
		String user = request.getParameter("user");
		if ( user==null ) {
			user = "parrt";
		}
		CookieMonster.setCookieValue(response, "user", user);
		out.println("<p>Set cookie to "+user);
		out.println("</body>");
		out.println("</html>");
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);
		handler.addServletWithMapping(CookieServlet.class, "/cookies");
		server.start();
		server.join();
	}
}
