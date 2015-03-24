import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class SimpleResponseServlet extends HttpServlet {
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        String query = request.getParameter("query");
        out.println("You searched for "+query);
        out.println("</body></html>");
        out.close();
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        String name = request.getParameter("name");
        out.println("Hello "+name+"<br><br>");
        out.println("You requested URL: "+
                    HttpUtils.getRequestURL(request)+request.getQueryString());
        out.println("</body></html>");
        out.close();
    }
}
