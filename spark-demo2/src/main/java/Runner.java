package main.java;

import static spark.Spark.*;
import spark.*;
import spark.template.velocity.VelocityRoute;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import org.apache.velocity.VelocityContext;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Runner {
	public static Classifier MyClassifier;
	public static WekaProcessing MyWekaProcessing;
	public static QueryDB q1;
	public static double[] attributeAVG;
    
	public static void main(String[] args) throws Exception {
		final List<String> attributeFields = new ArrayList<>();
		final List<String> attributeFieldType = new ArrayList<>();
		final String finalAttributeType;
		final String finalAttribute;
		final List<String> possibilities;
		final List<Attribute> attributeListData = new ArrayList<>();
	
		staticFileLocation("/assets");
		//org.apache.log4j.BasicConfigurator.configure();
		 
		MyWekaProcessing = new WekaProcessing();
        try {
        	MyClassifier = MyWekaProcessing.train();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Scanner sc = null;
		try {
			sc = new Scanner(new FileReader("outputFile.arff"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String line = sc.nextLine();
		while(!line.equals("@data"))
		{
			if(line.startsWith("@attribute"))
			{
				String[] lineSplit = line.split(" (?=(([^'\']*['\']){2})*[^'\']*$)");
				for (int i = 0; i < lineSplit.length; i++)
				{
					lineSplit[i] = lineSplit[i].replace("'", "");
				}
				attributeFields.add(lineSplit[1]);
				attributeFieldType.add(lineSplit[2]);
			}
			line = sc.nextLine();
		}
		sc.close();
		//these are the final value, not something we display on the page.
		finalAttribute = attributeFields.get(attributeFields.size()-1);
		String attributeType = attributeFieldType.get(attributeFieldType.size()-1);
		possibilities = Arrays.asList(attributeType.substring(1, attributeType.length()-1).split(","));
		attributeFields.remove(attributeFields.size()-1);
		attributeFieldType.remove(attributeFieldType.size()-1);

		
		//Database
		
//		BuildDB.createDB();		
		q1 = new QueryDB("Database.db");
		ResultSet rs = q1.avgQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		System.out.println("RSMD DATACOUNT: " + rsmd.getColumnCount());
		//get avg values
		attributeAVG = new double[15];
		for(int i = 0; i < rsmd.getColumnCount(); i ++){
			attributeAVG[i] = rs.getDouble(i+1);
		} 
		System.out.println("attribute: " + Arrays.toString(attributeAVG));

		
		// HTTP paths
		get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {
	            return "Hello World!";
            }
        });
		
		get(new VelocityRoute("/DataFileUpload")
		{
			@Override
			public Object handle(final Request request, final Response response)
			{
                Map<String, Object> model = new HashMap<>();

				return modelAndView(model, "uploadFile.wm");
			}
		});
		get(new VelocityRoute("/makeTrainingSet")
		{
			@Override
			public Object handle(final Request request, final Response reponse)
			{
				Map<String, Object> model = new HashMap<>();
				Scanner sc = null;
				try {
					sc = new Scanner(new FileReader("data1.csv"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String[] fields = sc.nextLine().split(",");
				List<List<String>> values = new ArrayList<List<String>>();
				while (sc.hasNextLine())
				{
					String s = sc.nextLine();
					values.add(Arrays.asList(s.split(",")));
				}
				model.put("Fields", fields);
				model.put("Values", values);
				return modelAndView(model, "viewData.wm");
			}
		});
		get(new VelocityRoute("/hello1") {
            @Override
            public Object handle(final Request request, final Response response) {
                Map<String, Object> model = new HashMap<>();  
                model.put("attributeAVG", Arrays.toString(attributeAVG));
                model.put("Model-Precision", MyWekaProcessing.getPrecision());
			    model.put("tokens", attributeFields);
                // The wm files are located under the resources directory
                return modelAndView(model, "hello.wm");
            }
        });
		
		post(new JsonTransformerRoute("/hello") {
		    @Override
		    public Object handle (Request request, Response response) {
		    	double result = -1;
		    	attributeListData.clear();
                Map<String, Object> model = new HashMap<>();
		    	Gson gson = new Gson();
		    	System.out.println("Request body" + request.body());
			    DataObject mydata = new DataObject(request.body());
			    VelocityContext context = new VelocityContext();
			    
		    	System.out.println(mydata.toString());
		    	/* Creating an instance with our values */
		    	//declare attributes
		    	FastVector attributeList = new FastVector(attributeFields.size());
	            FastVector fvNominalVal = new FastVector(possibilities.size());
	            Attribute attribute1 = new Attribute(finalAttribute, fvNominalVal);

	            for(String elem : possibilities)
	            	fvNominalVal.addElement(elem);

		    	for (String elem : attributeFields)
		    	{
		    		Attribute attr = new Attribute(elem);
		    		attributeList.addElement(attr);
		    		attributeListData.add(attr);
		    	}
		    	attributeList.addElement(attribute1);

		    	Instance inst = new Instance(attributeFields.size());
		        
		    	Instances data = new Instances("TestInstances",attributeList,attributeFields.size());
		    	data.setClassIndex(data.numAttributes()-1);
                
		    	for (int i = 0; i < attributeListData.size(); i ++)
		    	{
					try
					{
					double value = Double.valueOf(mydata.getData(attributeListData.get(i).toString().split(" ")[1]));
					inst.setValue(attributeListData.get(i), value);
					}
					catch (Exception e)
					{
						System.out.println("value not a double");
					}
		    	}
		    	//set attributes into instance
		    	System.out.println("The instance: " + inst);
		    	System.out.println("=====");
		    	
		    	data.add(inst);
		    	try {
					result = MyClassifier.classifyInstance(data.instance(0));
					System.out.println("result is: " + result);
					model.put("result", result);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	response.type("application/json");
				return new MyMessage(finalAttribute + ": " + possibilities.get((int)result));
		    }
        });
	}
}