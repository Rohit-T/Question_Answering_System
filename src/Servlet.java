import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;

public class Servlet extends HttpServlet {
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        String str_question = request.getParameter("search");
        
        String input_to_analyze = str_question;

        ArrayList<String> tweets = new ArrayList<String>();
        AnalyzeText app = new AnalyzeText();
        try {
            tweets = app.GoogleNLP(input_to_analyze);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        String[] output = new String[tweets.size()];
        output = tweets.toArray(output);
        request.setAttribute("output",output);
        request.getRequestDispatcher("search.jsp").forward(request, response);

    }
}
