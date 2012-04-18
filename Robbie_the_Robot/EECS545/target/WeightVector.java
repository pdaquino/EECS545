
package EECS545.target;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * A weight vector for one particular action. This is essentially a wrapper
 * for a List<Double>
 * @author Pedro
 */
public class WeightVector {
    private List<Double> weights;
    private String actionName = "Unknown";
    
    public WeightVector(List<Double> w) {
        this.weights = w;
    }
    
    public WeightVector(String actionName, List<Double> w) {
        this.actionName = actionName;
        this.weights = w;
    }
    
    public int size() {
        return weights.size();
    }
    
    public String getActionName() {
        return actionName;
    }
    
    public List<Double> getW() {
        return weights;
    }
    
    // returns w^T * b
    public double transTimes(List<Double> b) {
        return transTimes(b.toArray(new Double[1]));
    }
    
    public double transTimes(Double[] b) {
        if(b.length != weights.size())
            throw new IllegalArgumentException("Dimensions don't match");
        double sum = 0;
        for(int i = 0; i < b.length; i++) {
            sum += weights.get(i) * b[i];
        }
        return sum;
    }
    
    public double transTimes(Double[] b, double scaledFiringAngle) {
        List<Double> bList = new ArrayList<Double>(
                Arrays.asList(b));  
        bList.add(scaledFiringAngle);
        return transTimes(bList);
    }
    
    public void updateWeight(int idx, double adjustment) {
        double w = weights.get(idx);
        weights.set(idx, w + adjustment);
    }
    
    @Override
    public String toString() {
        StringBuilder blder = new StringBuilder();
        blder.append(getActionName()).append(' ');
        for(Double w : weights) {
            blder.append(w).append(' ');
        }
        return blder.toString();
    }
    
    public static WeightVector fromString(String s) {
        Output.println("fromString: " + s);
        Scanner lineScanner = new Scanner(s);
        //String name = lineScanner.next();
        List<Double> weights = new ArrayList<Double>();
        while(lineScanner.hasNextDouble()) {
            weights.add(lineScanner.nextDouble());
        }
        return new WeightVector("new", weights);
    }
}
