package main.java;

import weka.core.converters.*;

import java.awt.BorderLayout;
import java.io.*;
import java.util.Random;

import weka.core.*;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.*;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

public class WekaProcessing {
    public static void main(String[] args) throws Exception {
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
        
        // generate curve
        ThresholdCurve tc = new ThresholdCurve();
        int classIndex = 0;
        Instances result = tc.getCurve(eval.predictions(), classIndex);
        
        // plot curve
        ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
        vmc.setROCString("(Area under ROC = " +  Utils.doubleToString(tc.getROCArea(result), 4) + ")");
        vmc.setName(result.relationName());
        PlotData2D tempd = new PlotData2D(result);
        tempd.setPlotName(result.relationName());
        tempd.addInstanceNumberAttribute();
        // specify which points are connected
        boolean[] cp = new boolean[result.numInstances()];
        for (int n = 1; n < cp.length; n++)
            cp[n] = true;
        tempd.setConnectPoints(cp);
        // add plot
        vmc.addPlot(tempd);
        
        // display curve
        String plotName = vmc.getName();
        final javax.swing.JFrame jf =
        new javax.swing.JFrame("Weka Classifier Visualize: "+plotName);
        jf.setSize(500,400);
        jf.getContentPane().setLayout(new BorderLayout());
        jf.getContentPane().add(vmc, BorderLayout.CENTER);
        jf.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                jf.dispose();
            }
        });
        jf.setVisible(true);
    }
    
}
