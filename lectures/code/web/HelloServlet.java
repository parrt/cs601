import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HelloServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
		      HttpServletResponse response)
	throws ServletException, IOException
    {
	PrintWriter out = response.getWriter();

	out.println("<html>");
	out.println("<body>");
	out.println("<h1>Servlet test</h1>");

	out.println("Hello, there!");

	out.println("</body>");
	out.println("</html>");
    }
}
