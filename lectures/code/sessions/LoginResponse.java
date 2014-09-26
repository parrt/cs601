import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class LoginResponse extends HttpServlet {
	public void doPost(HttpServletRequest request,
					   HttpServletResponse response)
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		String user = request.getParameter("user");
		out.println("Hello "+user+"<br><br>");
		out.println("</body></html>");
		out.close();
	}
}
