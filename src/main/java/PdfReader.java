

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PdfReader
 */
@WebServlet(
	urlPatterns = {"/pdf"},
	initParams = {
		@WebInitParam(name="PDF_FILE", value="/WEB-INF/sample.pdf")	
	}
)
public class PdfReader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String PDF_FILE;
	
	@Override
	public void init() throws ServletException {
		super.init();
		PDF_FILE = getInitParameter("PDF_FILE");
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PdfReader() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/pdf");
		
		try(InputStream in = getServletContext().getResourceAsStream(PDF_FILE)) {
			OutputStream out = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
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
