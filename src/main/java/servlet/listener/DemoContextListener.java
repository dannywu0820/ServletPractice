package servlet.listener;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class DemoContextListener
 *
 */
@WebListener
public class DemoContextListener implements ServletContextListener {
	private List<AsyncContext> asyncs = new ArrayList<>();
	
    /**
     * Default constructor. 
     */
    public DemoContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
    	System.out.println("Context Destroyed");
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	System.out.println("Context Initialized");
    	sce.getServletContext().setAttribute("asyncs", asyncs);
    	
    	new Thread(() -> {
    		while(true) {
    			try {
    				Thread.sleep((long) (Math.random() * 5000));
    				response(Math.random() * 10);
    			}
    			catch(Exception e) {
    				throw new RuntimeException(e);	
    			}
    		}
    	}).start();
    }
	
    private void response(double num) {
    	synchronized(asyncs) {
    		asyncs.forEach(context -> {
    			try {
    				context.getResponse().getWriter().println(num);
    				context.complete();
    			}
    			catch(IOException e) {
    				throw new UncheckedIOException(e);
    			}
    		});
    		asyncs.clear();
    	}
    }
}
