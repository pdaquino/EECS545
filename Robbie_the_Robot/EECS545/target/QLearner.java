
package EECS545.target;

import EECS545.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DEPRECATED. THIS CLASS IS NOT BEING USED.
 * @author Pedro
 */
public class QLearner {
    private HashMap<String, Action> actionMap = new HashMap<String, Action>();
    private HashMap<String, WeightVector> weightMap = new HashMap<String, WeightVector>();
    private FeatureScaler scaler;
    
    private final int numFeatures = State.getNumFeatures();
    // important tweaking constants: learning rate
    private final double alpha = 4;
    // discount factor
    private final double gamma = 0.8;
    
    public QLearner(List<Action> actions, List<WeightVector> initialWeights,
            FeatureScaler scaler) {
        this.scaler = scaler;
        for(WeightVector w : initialWeights) {
            if(w.size() != numFeatures) {
                throw new IllegalArgumentException("Weight vector must have the same dimension as the feature vector");
            }
            weightMap.put(w.getActionName(), w);
        }
        for(Action a : actions) {
            actionMap.put(a.getName(), a);
            if(!weightMap.containsKey(a.getName())) {
                weightMap.put(a.getName(), makeDefaultWeightVector(a.getName()));
            }
        }
    }
    
    public Action queryActionByName(String name) {
        if(actionMap.containsKey(name))
            return actionMap.get(name);
        else
            return null;
    }
    
    public void learn(State s, Action a, double reward, State sPrime) {
//        WeightVector w_a = weightMap.get(a.getName());
//        Double[] f = s.getState();
//        for(int i = 0; i < w_a.size(); i++) {
//            double maxQ_sPrime = GreedyPolicy.chooseAction(
//                    new ArrayList<WeightVector>(weightMap.values()), scaler,
//                    sPrime).Q;
//            double Q_s_a = w_a.transTimes(f);
//            double adjustment = alpha*f[i]*(reward + gamma*maxQ_sPrime - Q_s_a);
//            w_a.updateWeight(i, adjustment);
//        }
    }

    private WeightVector makeDefaultWeightVector(String actionName) {
        final double defaultWeight = 0;
        List<Double> weights = new ArrayList<Double>();
        for(int i = 0; i < numFeatures; i++) {
            weights.add(defaultWeight);
        }
        return new WeightVector(actionName, weights);
    }
    
}
