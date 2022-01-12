package servlet.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import servlet.request.wrapper.CharacterRequestWrapper;

/**
 * Servlet Filter implementation class CharacterFilter
 */
@WebFilter(
	urlPatterns = {"/*"},
	initParams = {
		@WebInitParam(name = "ESCAPE_LIST", value= "/WEB-INF/escapes.txt")
	},
	asyncSupported=true
)
public class CharacterFilter extends HttpFilter implements Filter {
	private Map<String, String> escapes = new HashMap<>();
    /**
     * @see HttpFilter#HttpFilter()
     */
    public CharacterFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// pass the request along the filter chain
		chain.doFilter(new CharacterRequestWrapper((HttpServletRequest) request, escapes), response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		super.init(fConfig);
		
		ServletContext context = getServletContext();
		InputStreamReader inputStream = new InputStreamReader(context.getResourceAsStream(getInitParameter("ESCAPE_LIST")));
		try(BufferedReader reader = new BufferedReader(inputStream)) {
			String input = null;
			while((input = reader.readLine()) != null) {
				String[] tokens = input.split("\t");
				escapes.put(tokens[0], tokens[1]);
			}
		}
		catch(IOException ex) {
			
		}
	}

}
