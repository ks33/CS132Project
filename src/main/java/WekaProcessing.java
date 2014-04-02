package main.java;

import weka.core.converters.*;
import java.io.*;
import weka.core.*;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.*;

public class WekaProcessing {

	public WekaProcessing(){
		//Think about giving the location of the file path to
		//the constructor of this class in some cool
		//user friendly way like having a button
		//on the home page that lets you upload
		//a csv file
		CSVLoader loader = new CSVLoader();
		try{
			loader.setSource(new File("Data.csv"));
		}
		catch(IOException ex){
			System.out.println("IO Exception getting data");
		}
		Instances data = null;
		try{
			data = loader.getDataSet();
		}
		catch(IOException ex){
			System.out.println("IOException with processing data");
		}
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		File outputAndDestination = null;
		
		try{
			outputAndDestination = new File("outputFile.arff");
			saver.setFile(outputAndDestination);
			saver.writeBatch();
		}
		
		catch(IOException ex){
			System.out.println("IOException output file");
		}

		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader("outputFile.arff"));
		}
		catch(FileNotFoundException ex){
			System.out.println("File not found buffered reader");
		}
		Instances train = null;
		try{
			train = new Instances(br);
		}
		catch(IOException ex){
			System.out.println("IO Exception");
		}
		train.setClassIndex(train.numAttributes()-1);
		try{
			br.close();
		}
		catch(IOException ex){
			System.out.println("IOException br close");
		}
		Classifier myClassifier = new NaiveBayes();
		try{
			myClassifier.buildClassifier(train);
		}
		catch(Exception ex){
			System.out.println("buildClassifier exception");
		}
		try{
			Evaluation eval =  new Evaluation(train);
		}
		catch(Exception ex){
			System.out.println("evaluation exception");
		}
		eval.crossValidateModel(myClassifier, train, 10, new Random(1));
        System.out.println(eval.toSummaryString());
	}
	
}
