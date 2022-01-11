package servlet.request.wrapper;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CharacterRequestWrapper extends HttpServletRequestWrapper{
	private Map<String, String> escapes;
	
	public CharacterRequestWrapper(HttpServletRequest request, Map<String, String> escapes) {
		super(request);
		this.escapes = escapes;
	}
	
	@Override
    public String getParameter(String name) {
		System.out.println("invoke: " + name);
        return Optional.ofNullable(getRequest().getParameter(name))
                       .map(this::escape)
                       .orElse(null);
    }

    private String escape(String value) {
        String result = value;
        for(String origin : escapes.keySet()) {
            result = result.replaceAll(origin, escapes.get(origin));
        }
        return result;
    }

}
