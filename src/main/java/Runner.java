package main.java;

import static spark.Spark.*;
import spark.*;
import spark.template.velocity.VelocityRoute;
import java.util.*;


public class Runner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		get(new Route("/") {
	         @Override
	         public Object handle(Request request, Response response) {
	            return "Hello World!";
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
	}

}
