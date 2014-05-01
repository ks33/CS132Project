package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataObject {
	
	private HashMap<String, String> data = new HashMap<>();
	private List<String> list ;
	
	public DataObject(String body)
	{
		String realString = body.substring(1, body.length()-2);
		System.out.println("HELP ELP");
		System.out.println(realString);
		String[] tokens = realString.split(",");
		for (String elem : tokens)
		{
			String[] pair = elem.replace("\"", "").split(":");
			System.out.println("PAIR:" + pair[0]+ " " + pair[1]);
			data.put(pair[0], pair[1]);
		}
	}

	public String getData(String key)
	{
		return data.get(key);
	}
}