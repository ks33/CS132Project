package main.java;

import static spark.Spark.*;
import spark.*;
import spark.template.velocity.VelocityRoute;

import java.util.*;

public class Runner {

	public static void main(String[] args) {
		/*
		 * Initial configuration:
		 * Set static file directory to assets directory (under resources)
		 * Set up basic logging
		 */
		staticFileLocation("/assets");
		org.apache.log4j.BasicConfigurator.configure();
		
		
		get(new Route("/") {
	         @Override
	         public Object handle(Request request, Response response) {
	            return "Hello World!";
	         }
	      });
		
		get(new VelocityRoute("/home") {
            @Override
            public Object handle(final Request request, final Response response) {
                Map<String, Object> model = new HashMap<>();
                model.put("status", "Yes!");
                return modelAndView(model, "home.wm");
            }
        });
		
		get(new JsonTransformerRoute("/hello") {
		    @Override
		    public Object handle(Request request, Response response) {
		       return new MyMessage("Hello World");
		    }
		 });
		
		get(new VelocityRoute("/hello1") {
            @Override
            public Object handle(final Request request, final Response response) {
                Map<String, Object> model = new HashMap<>();
                model.put("hello", "Velocity World");
                model.put("m", new MyMessage("hi bro"));
                // The wm files are located under the resources directory
                return modelAndView(model, "hello.wm");
            }
        });
		
		get(new JsonTransformerRoute("/clickme") {
			@Override
			public Object handle(Request request, Response response) {
				response.type("application/json");
				return new MyMessage(Double.toString(Math.random()));
			}
			
		});
	}

}
