
package EECS545.target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author Pedro
 */
public class DiscreteWQLearner {
    private List<WeightVector> weightList;
    private FeatureScaler scaler;
    
    private final int numFeatures = ReducedState.getNumFeatures();
    
    // important tweaking constants: learning rate
    private final double alpha = 0.1;
    // discount factor
    private final double gamma = 0.8;
    
    public DiscreteWQLearner(List<WeightVector> initialWeights,
            FeatureScaler scaler) {
        this.scaler = scaler;
        if(initialWeights == null || initialWeights.isEmpty()) {
            Output.println("No initial weights; setting them to the default");
            this.weightList = makeDefaultWeightVectors();
        } else {
            for (WeightVector w : initialWeights) {
                if (w.size() != numFeatures) {
                    throw new IllegalArgumentException("Weight vector must have "
                            + "the same dimension as the feature vector (" +
                            w.size() + " vs " + numFeatures + ")");
                }
            }
            this.weightList = initialWeights;
        }
        
    }
    
//    public Action queryActionByName(String name) {
//        if(actionMap.containsKey(name))
//            return actionMap.get(name);
//        else
//            return null;
//    }
    
    public void learn(ReducedState s, Action a, double reward, ReducedState sPrime) {
        WeightVector w_a = getWeightVectorByAngle(a.getAngle());
        List<Double> scaledFeatures = new ArrayList<Double>(
                Arrays.asList(scaler.scale(s.getState())));  
        
        double maxQ_sPrime = DiscreteWGreedyPolicy.chooseAction(weightList,
                    scaler,
                    sPrime).Q;
        double Q_s_a = w_a.transTimes(scaledFeatures);   
        double baseAdjustment = alpha*(reward + gamma*maxQ_sPrime - Q_s_a);
        
        Output.println("Updating weight vector number " + weightList.indexOf(w_a));
        Output.println("Adjustment: " + baseAdjustment + "; reward = " + reward + "; maxQ_s' = " + maxQ_sPrime + "; Q_s_a" + Q_s_a);
        //Output.println("Original features");
        //Util.print(s.getState());
        //Output.println("Scaled Features: ");
        //Util.print(scaledFeatures);
        //Output.println("Weights: " + w_a.toString());
        
        for(int i = 0; i < w_a.size(); i++) {
            double adjustment = scaledFeatures.get(i)*baseAdjustment;
            w_a.updateWeight(i, adjustment);
        }
        
        //Output.println("New weights: " + w_a.toString());
        double newQSa = w_a.transTimes(scaledFeatures);
        Output.println("New Q_s_a: " + newQSa);
        if(Math.abs(newQSa) > 1E6) {
            Output.println("ALGORITHM IS DIVERGING!!!!\nALGORITHM IS DIVERGING!!!\nALGORITHM IS DIVERGING!!!\nALGORITHM IS DIVERGING!!!");
        }
    }

    private List<WeightVector> makeDefaultWeightVectors() {
        List<WeightVector> weights = new ArrayList<WeightVector>();
        // we create numGuns guns, ranging from Gun.MIN_ORIENTATION to
        // Gun.MAX_ORIENTATION. each gun is spaced by eps degrees, and has
        // eps degrees of variation
        double eps = Constants.EPS;
        for(int i = 0; i < Constants.NUM_GUNS; i++) {
            weights.add(makeDefaultWeightVector(Gun.MIN_ORIENTATION + i*eps));
        }
        return weights;
    }
    private WeightVector makeDefaultWeightVector(double orientation) {
        final double defaultWeight = 0;
        List<Double> weights = new ArrayList<Double>();
        for(int i = 0; i < numFeatures; i++) {
            weights.add(defaultWeight);
        }
        return new WeightVector(orientation, weights);
    }
    
    private WeightVector getWeightVectorByAngle(double orientation) {
        for(WeightVector w : weightList) {
            if(w.getOrientation() == orientation) {
                return w;
            }
        }
        throw new IllegalArgumentException("No weight vector at " + orientation);
    }

    public List<WeightVector> getWeightList() {
        return weightList;
    }
    
}
