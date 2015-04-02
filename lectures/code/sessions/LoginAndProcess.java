import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginAndProcess extends HttpServlet {
	public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
		throws ServletException, IOException
	{
		generate(request,response);
	}

	public void doPost(HttpServletRequest request,
					   HttpServletResponse response)
		throws ServletException, IOException
	{
		generate(request,response);
	}

	public void generate(HttpServletRequest request,
						 HttpServletResponse response)
		throws ServletException, IOException
	{
		String event = request.getParameter("event");
		if ( event==null || event.equals("view") ) {
			view(request,response);
		}
		else {
			process(request,response);
		}
	}

	public void view(HttpServletRequest request,
					 HttpServletResponse response)
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<form method=post action=/login?event=process>");
		out.println("User: <input type=text name=user><br>");
		out.println("Password: <input type=password name=password><br><br>");
		out.println("<input type=submit value=login>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
		out.close();
	}

	public void process(HttpServletRequest request,
						HttpServletResponse response)
		throws ServletException, IOException
	{
		System.out.println("LoginAndProcess.doPost");
		String user = request.getParameter("user");
		String password = request.getParameter("password");
		if ( user.equals("parrt") && password.equals("foobar") ) {
			// all is ok, direct to "home page"
			response.sendRedirect("/home");
		}
		else {
			// oops...back to login.
			response.sendRedirect("/login?event=view");
		}
	}

	public static void main(String[] args) throws Exception {
		org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(8080);
		// To use sessions, we have to use ServletContextHandler not
		// ServletHandler
		ServletContextHandler handler = new
		            ServletContextHandler(ServletContextHandler.SESSIONS);
		server.setHandler(handler);
		// only need to register one page.
		handler.addServlet(LoginAndProcess.class, "/login");
		server.start();
		server.join();
	}
}
