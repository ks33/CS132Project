package main.java;

import com.google.gson.Gson;

import spark.*;

public abstract class JsonTransformerRoute extends ResponseTransformerRoute {
	private Gson gson = new Gson();
	
    protected JsonTransformerRoute(String path) {
       super(path, "application/json");
    }
	
    @Override
    public String render(Object model) {
	   return gson.toJson(model);
    }
}
