
package EECS545.target;

import EECS545.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Q-Learner that uses the firing angle as a feature, and therefore
 * only trains one weight vector
 * @author Pedro
 */
public class SingleWQLearner {
    private WeightVector w;
    private FeatureScaler scaler;
    private final int numFeatures = ReducedState.getNumFeatures();
    private final int numParameters = numFeatures + 1;
     // important tweaking constants: learning rate
    private final double alpha = .005;
    // discount factor
    private final double gamma = 0.2;
    
    public SingleWQLearner(WeightVector initialWeights,
            FeatureScaler scaler) {
        this.scaler = scaler;
        if(initialWeights == null) {
            this.w = makeDefaultWeightVector("New");
        } else {
            this.w = initialWeights;
        }
        if (this.w.size() != numParameters) {
            throw new IllegalArgumentException("Weight vector must have the same dimension as the feature vector ("
                    + w.size() + " vs " + numParameters + ")");
        }
    }
    
    public void learn(ReducedState s, double firingAngle, double reward, ReducedState sPrime) {
        List<Double> scaledFeatures = new ArrayList<Double>(
                Arrays.asList(scaler.scale(s.getState())));  
        scaledFeatures.add(Gun.orientationToScale(firingAngle));
        
        double maxQ_sPrime = GreedyPolicy.chooseAction(w,
                    scaler,
                    sPrime).Q;
        double Q_s_a = w.transTimes(scaledFeatures);                
        double baseAdjustment = alpha*(reward + gamma*maxQ_sPrime - Q_s_a);
        
        Output.println("Adjustment: " + baseAdjustment + "; reward = " + reward + "; maxQ_s' = " + maxQ_sPrime + "; Q_s_a" + Q_s_a);
        Output.println("Original features");
        Util.print(s.getState());
        Output.println("Scaled Features: ");
        Util.print(scaledFeatures);
        Output.println("Weights: " + w.toString());

        for(int i = 0; i < scaledFeatures.size(); i++) {            
            double adjustment = scaledFeatures.get(i)*baseAdjustment;
            w.updateWeight(i, adjustment);
        }
        Output.println("New weights: " + w.toString());
        double newQSa = w.transTimes(scaledFeatures);
        Output.println("New Q_s_a: " + newQSa);
        if(Math.abs(newQSa) > 1E6) {
            Output.println("ALGORITHM IS DIVERGING!!!!\nALGORITHM IS DIVERGING!!!\nALGORITHM IS DIVERGING!!!\nALGORITHM IS DIVERGING!!!");
        }
    }

    public WeightVector getW() {
        return w;
    }
    
    private WeightVector makeDefaultWeightVector(String actionName) {
        final double defaultWeight = 0;
        List<Double> weights = new ArrayList<Double>();
        for(int i = 0; i < numParameters; i++) {
            weights.add(defaultWeight);
        }
        return new WeightVector(actionName, weights);
    }
}
