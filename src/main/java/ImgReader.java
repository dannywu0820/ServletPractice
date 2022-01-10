

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImgReader
 */
@WebServlet(
	urlPatterns = {"/images"},
	initParams = {
		@WebInitParam(name = "IMAGE_DIR", value = "/images")
	}
)
public class ImgReader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String IMAGE_DIR;
       
	@Override
	public void init() throws ServletException {
		super.init();
		IMAGE_DIR = getInitParameter("IMAGE_DIR");
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImgReader() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<body>");
        
        System.out.println("Image Dir: " + IMAGE_DIR);
        getServletContext().getResourcePaths(IMAGE_DIR)
        .forEach(avatar -> {
        	out.printf("<img src='%s'>%n", avatar.replaceFirst("/", ""));
        });

        out.println("</body>");
        out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
