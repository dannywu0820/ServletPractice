

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
		ServletInputStream in = request.getInputStream();
		ReadListener listener = new NonBlockingReadListener(in, new ByteArrayOutputStream(), context);
		in.setReadListener(listener);
	}
}

class NonBlockingReadListener implements ReadListener {
	InputStream in;
	OutputStream out;
	AsyncContext context;

	NonBlockingReadListener(InputStream in, OutputStream out, AsyncContext context) {
		this.in = in;
		this.out = out;
		this.context = context;
	}
	
	@Override
	public void onAllDataRead() throws IOException {
		byte[] body = ((ByteArrayOutputStream) out).toByteArray();
		ServletRequest request = context.getRequest();
		ServletResponse response = context.getResponse();

        String contentAsTxt = new String(body, "ISO-8859-1");
        String filename = filename(contentAsTxt);
        Range fileRange = fileRange(contentAsTxt, request.getContentType());
        write(body, 
            contentAsTxt.substring(0, fileRange.start)
                        .getBytes("ISO-8859-1")
                        .length, 
            contentAsTxt.substring(0, fileRange.end)
                        .getBytes("ISO-8859-1")
                        .length, 
            "C:\\Users\\Danny_Wu.PFT\\Desktop\\" + filename
        );

        response.getWriter().println("Upload Successfully...");
        context.complete();
	}

	@Override
	public void onDataAvailable() throws IOException {
		byte[] buffer = new byte[1024];
		int length = -1;
		while(((ServletInputStream)in).isReady() && (length = in.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
	}

	@Override
	public void onError(Throwable throwable) {
		context.complete();
		throw new RuntimeException(throwable);
	}
	
	private String filename(String contentTxt) throws UnsupportedEncodingException {
        Pattern pattern = Pattern.compile("filename=\"(.*)\"");
        Matcher matcher = pattern.matcher(contentTxt);
        matcher.find();
        return matcher.group(1);
    }
	
	private static class Range {
        final int start;
        final int end;
        public Range(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
	
	private Range fileRange(String content, String contentType) {
        Pattern pattern = Pattern.compile("filename=\".*\"\\r\\n.*\\r\\n\\r\\n(.*+)");
        Matcher matcher = pattern.matcher(content);
        matcher.find();
        int start = matcher.start(1);

        String boundary = contentType.substring(
                contentType.lastIndexOf("=") + 1, contentType.length());
        int end = content.indexOf(boundary, start) - 4;   

        return new Range(start, end);
    }
	
	private void write(byte[] content, int start, int end, String file) throws IOException {
		try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			fileOutputStream.write(content, start, (end - start));
		}   
	}
}
