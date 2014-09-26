import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class Login extends HttpServlet {
	public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<form method=post action=/response>");
		out.println("User: <input type=text name=user><br>");
		out.println("Password: <input type=password name=password><br><br>");
		out.println("<input type=submit value=login>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	public static void main(String[] args) throws Exception {
		org.eclipse.jetty.server.Server server = new Server(8080);
		// To use sessions, we have to use ServletContextHandler not
		// ServletHandler
		ServletContextHandler handler = new
		            ServletContextHandler(ServletContextHandler.SESSIONS);
		server.setHandler(handler);
		handler.addServlet(Login.class, "/login");
		handler.addServlet(LoginResponse.class, "/response");
		server.start();
		server.join();
	}
}
