

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AsyncHelloWorld
 */
@WebServlet(
	urlPatterns="/async/helloworld",
	asyncSupported=true
)
public class AsyncHelloWorld extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private List<AsyncContext> asyncs;
    
    @Override
    public void init() throws ServletException {
    	asyncs = (List<AsyncContext>) this.getServletContext().getAttribute("asyncs");
    }
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AsyncHelloWorld() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("[" + this.getServletName() + "]");
		
		response.setContentType("text/html; charset=UTF8");
		
		AsyncContext context = request.startAsync();
		this.doAsync(context)
			.thenApplyAsync(String::toUpperCase)
			.thenAcceptAsync(resource -> {
				try {
					context.getResponse().getWriter().println(resource);
					context.complete();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			});
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		synchronized(asyncs) {
			asyncs.add(request.startAsync());
		}
	}
	
	private CompletableFuture<String> doAsync(AsyncContext context) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				String resource = context.getRequest().getParameter("resource");
				Thread.sleep(5 * 1000);
				
				return String.format("%s back finally...", resource);
			}
			catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
