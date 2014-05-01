package main.java;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.filters.Filter;

public class WkaProcessingTwo {
	
	protected Classifier m_Classifier = null;
	protected Filter m_Filter = null;
	protected String m_TrainingFile = "Data.csv";
	protected Instances m_Training = null;
	protected Evaluation m_Evaluation = null;

	public WkaProcessingTwo()
	{
		super();
	}
	
	public void setClassifier(String classifier) throws Exception
	{
		m_Classifier = Classifier.forName("weka.classifiers.bayes.NaiveBayes", null);
	}
	public void setTraining() throws Exception
	{
		m_Training = new Instances(new BufferedReader(new FileReader(m_TrainingFile)), m_Training.numAttributes()-1);
	}
}
