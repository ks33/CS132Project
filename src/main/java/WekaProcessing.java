package main.java;

import weka.core.converters.*;

import java.awt.BorderLayout;
import java.io.*;
import java.util.Random;

import static spark.Spark.*;

import spark.Request;
import spark.Response;
import spark.Route;


import weka.core.*;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.*;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

public class WekaProcessing {
    public static Classifier train() throws Exception {
    	setPort(5678);
        // load CSV file and convert to Arff file
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File("Data.csv"));
        Instances data = loader.getDataSet();
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        File outputAndDestination = new File("outputFile.arff");
        saver.setFile(outputAndDestination);
        saver.writeBatch();
        BufferedReader br = new BufferedReader(new FileReader("outputFile.arff"));
        Instances train =  new Instances(br);
        train.setClassIndex(train.numAttributes()-1);
        br.close();
        
		// train classifier
        Classifier myClassifier = new NaiveBayes();
        myClassifier.buildClassifier(train);
        Evaluation eval =  new Evaluation(train);
        eval.crossValidateModel(myClassifier, train, 10, new Random(1));
        System.out.println(eval.toSummaryString("\nResults\n==========\n", true));
        System.out.println("Precision is: " + eval.precision(1));
        return myClassifier;
        
    }
    
}