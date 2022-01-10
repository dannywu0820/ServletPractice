

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Another
 */
@WebServlet(name="Another", urlPatterns="/request")
public class Another extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Another() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		//Parameter
		String name = request.getParameter("name");
		String[] values = request.getParameterValues("name");
		Enumeration<String> e1 = request.getParameterNames();
		Collections.list(e1).stream()
			.map(request::getParameter)
			.forEach(out::println);
		Map<String, String[]> m = request.getParameterMap();
		m.entrySet().stream()
			.forEach(out::println);
		
		//Header
		String contentType = request.getHeader("Content-Type");
		Enumeration<String> headers = request.getHeaders(name);
		Enumeration<String> e2 = request.getHeaderNames();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Body
		PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<body>");
        out.println(bodyContent(request.getReader()));
        out.println("</body>");
        out.println("</html>");
	}
	
	private String bodyContent(BufferedReader reader) throws IOException {
        String input = null;
        StringBuilder requestBody = new StringBuilder();
        while((input = reader.readLine()) != null) {
            requestBody.append(input)
                       .append("<br>");
        }
        return requestBody.toString();
    }
}
