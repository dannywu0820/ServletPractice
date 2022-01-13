package servlet.dispatcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Some
 */
@WebServlet("/some")
public class Some extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Some() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.authenticate(response)) {
			response.setContentType("text/html; charset=utf-8");
			
			PrintWriter out = response.getWriter();
	        out.println("Some do one...");
	        
	        RequestDispatcher dispatcher = request.getRequestDispatcher("other");
	        List<String> books = Stream.of(1,2,3).map(e -> "book" + e).collect(Collectors.toList());
	        request.setAttribute("books", books);
	        dispatcher.include(request, response);
	        
	        out.println("Some do two...");
	        out.println("<a href='logout'>µn¥X</a>");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
