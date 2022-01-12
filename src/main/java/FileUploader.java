

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class FileUploader
 */
@MultipartConfig
@WebServlet(
	urlPatterns = {"/upload"},
	asyncSupported = true
)
public class FileUploader extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUploader() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getServletName());
		
		AsyncContext context = request.startAsync();
		Part photo = ((HttpServletRequest)context.getRequest()).getPart("photo");
		String filename = photo.getSubmittedFileName();
		
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			// still use blocking I/O here
			try(InputStream in = photo.getInputStream(); OutputStream out = new FileOutputStream("C:\\Users\\Danny_Wu.PFT\\Desktop\\" + filename)){
				byte[] buffer = new byte[1024];
				int length = -1;
				while((length = in.read(buffer)) != -1) {
					out.write(buffer, 0, length);
				}
			}
			catch(IOException e) {
				throw new UncheckedIOException(e);
			}
		});
		
		future.thenRun(() -> {
			try {
				context.getResponse().getWriter().println("Upload Successfully");
				context.complete();
			}
			catch(IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

}
