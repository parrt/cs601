import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class PageCount extends HttpServlet {
	public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
		throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		User u = (User)session.getAttribute("user");
		if ( u==null ) {
			System.out.println("creating parrt");
			u = new User("parrt", 234123434);
			session.setAttribute("user", u);
		}
		u.pageviews++;
		int n = u.pageviews;

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println(u.name+"'s pageviews: "+n);
		out.println("</body>");
		out.println("</html>");
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		// To use sessions, we have to use ServletContextHandler not
		// ServletHandler
		ServletContextHandler handler = new
			ServletContextHandler(ServletContextHandler.SESSIONS);
		server.setHandler(handler);
		handler.addServlet(PageCount.class, "/count");
		server.start();
		server.join();
	}
}
