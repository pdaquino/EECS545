package EECS545;

import libsvm.*;
import java.io.*;
import java.util.*;

public class SVMPredict {

	// svm test problem
	svm_problem testProblem;

	// svm model
	svm_model model;
	
	// identification
	String strategy;

	// scaling factors
	double[] featureMax;
	double[] featureMin;

	// number of features / scaled features
	int numFeatures = 0;
	int numScaledFeatures = 0;

	// args[0] = model file
	// args[1] = scaling file
	// args[2] = test file
	public static void main(String args[]) throws IOException {

		// testing functionality
		SVMPredict tester = new SVMPredict(args[0], args[1], "testing");
		tester.readFile(args[2]);

		// testing the original test values by scaling and testing
		int counter = 0;
		double result;
		for (int i = 0; i < tester.testProblem.l; i++) {
			result = tester.testPoint(tester.testProblem.x[i]);
			if ((result >= 0 && tester.testProblem.y[i] == 1)
					|| (result < 0 && tester.testProblem.y[i] == -1))
				counter++;
		}

		// test accuracy
		System.out.println("Test Accuracy: "
				+ (counter / (double) tester.testProblem.l) * 100);
	}

	// constructor
	public SVMPredict(String modelFile, String scaleFile, String strategy) throws IOException {

		// lock in strategy
		this.strategy = strategy;
	
		// load in SVM model
		model = svm.svm_load_model(modelFile);

		// load in training scale
		getScale(scaleFile);
	}

	// get strategy
	public String getStrategy(){
		
		return strategy;
	
	}

	// reads in scale file for data scaling
	private void getScale(String scaleFile) throws IOException {

		// string to hold contents of current line
		String line;

		// separate tokens
		StringTokenizer st;

		// used to skip first two lines in scale file
		int index = 0;

		// file reader
		BufferedReader fp = new BufferedReader(new FileReader(scaleFile));

		// first pass: determine number of features / number of scaled features
		while (true) {

			// increment index of file to skip first two lines
			index++;

			// next line
			line = fp.readLine();

			// skipping first two lines
			if (index < 3)
				continue;

			// all lines have been processed
			if (line == null)
				break;

			// separate tokens
			st = new StringTokenizer(line, " \t\n\r\f:");

			// number of features is the index on the last line
			numFeatures = Integer.parseInt(st.nextToken());

			// increment number of scaled features
			numScaledFeatures++;
		}

		// close file, open it back up
		fp.close();
		fp = new BufferedReader(new FileReader(scaleFile));

		// initialize feature parameter arrays
		featureMax = new double[numFeatures];
		featureMin = new double[numFeatures];

		// reset index counter
		index = 0;

		// run through file
		while (true) {

			// increment index of file
			index++;

			// next line
			line = fp.readLine();

			// skipping first two lines
			if (index < 3)
				continue;

			// all lines have been processed
			if (line == null)
				break;

			// separate tokens
			st = new StringTokenizer(line, " \t\n\r\f:");

			// scale feature index
			int featureIndex = Integer.parseInt(st.nextToken());

			// feature index corresponds with current search index
			featureMin[featureIndex - 1] = Double.parseDouble(st.nextToken());
			featureMax[featureIndex - 1] = Double.parseDouble(st.nextToken());
		}

		fp.close();
	}

	// reads in a log file for testing
	private void readFile(String inputFile) throws IOException {
		BufferedReader fp = new BufferedReader(new FileReader(inputFile));
		Vector<Double> vy = new Vector<Double>();
		Vector<svm_node[]> vx = new Vector<svm_node[]>();
		int max_index = 0;

		while (true) {
			String line = fp.readLine();
			if (line == null)
				break;

			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

			vy.addElement(Double.parseDouble(st.nextToken()));
			int m = st.countTokens() / 2;
			svm_node[] x = new svm_node[m];
			for (int j = 0; j < m; j++) {
				x[j] = new svm_node();
				x[j].index = Integer.parseInt(st.nextToken());
				x[j].value = Double.parseDouble(st.nextToken());
			}
			if (m > 0)
				max_index = Math.max(max_index, x[m - 1].index);
			vx.addElement(x);
		}

		testProblem = new svm_problem();
		testProblem.l = vy.size();
		testProblem.x = new svm_node[testProblem.l][];
		for (int i = 0; i < testProblem.l; i++)
			testProblem.x[i] = vx.elementAt(i);
		testProblem.y = new double[testProblem.l];
		for (int i = 0; i < testProblem.l; i++)
			testProblem.y[i] = vy.elementAt(i);

		fp.close();
	}

	// tests a single data point
	public double predict(Double[] features) {

		// return test value
		double[] val = new double[2];
		
		// svm_node test point
		svm_node[] point = new svm_node[numScaledFeatures];

		// index tracker
		int index = 0;

		// scale features
		for (int i = 0; i < numFeatures; i++) {

			if (featureMax[i] == featureMin[i])
				continue;

			point[index] = new svm_node();
			point[index].value = -1
					+ 2
					* ((features[i] - featureMin[i]) / (featureMax[i] - featureMin[i]));
			point[index].index = i + 1;
			index++;
		}

		// prediction
		double predClass = svm.svm_predict_probability(model, point, val);
		
		if(predClass == 1)
			return val[0];
		else
			return -val[0];
	}

	// for testing using main method only
	private double testPoint(svm_node[] node) {

		// return test value
		double[] val = new double[2];

		// svm_node test point
		svm_node[] point = new svm_node[numScaledFeatures];

		// index tracker
		int index = 0;

		// scale features
		for (int i = 0; i < numFeatures; i++) {

			if (featureMax[i] == featureMin[i])
				continue;
			point[index] = new svm_node();
			point[index].value = -1
					+ 2
					* ((node[i].value - featureMin[i]) / (featureMax[i] - featureMin[i]));
			point[index].index = i + 1;
			index++;
		}

		// prediction
		double predClass = svm.svm_predict_probability(model, point, val);

		if(predClass == 1)
			return val[0];
		else
			return -val[0];
	}
}