package main.java;

import static spark.Spark.*;
import spark.*;
import spark.template.velocity.VelocityRoute;
import java.util.*;


public class Runner {

	public static void main(String[] args) {
        // 1) Change WekaProcessing main function's return type to Classifer, change name?
		// 2) Make function return classifier
		// c = (new WekaProcessing()).train()
		// 3) create a new POST route (JsonTransformer)
		// 			in this function, parse JSON from request body
		//          create Instance() out of it (weka class)
		//			pass it into c.classifyInstance()
		//			create a Java class similar to MyMessage that will have instance variables for the attributes
		//			of your response
		//			return an instance of this class to generate a response
		//	handle in client-side javascript
		
		c = (new WekaProcessing()).train();
		
		post(new JsonTransformerRoute("/train") {
		    @Override
		    public Object handle(Request request, Response response) {
		    	
                return ;
		    }
        });
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
