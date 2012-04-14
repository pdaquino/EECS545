
package EECS545.target;

import EECS545.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Pedro
 */
public class GreedyPolicy {
    public static class Choice {
        public String actionName;
        public double Q;
        public Choice(String n, double q) {
            this.actionName = n;
            this.Q = q;
        }
    }
    public static Choice chooseAction(List<WeightVector> weights, FeatureScaler scaler,
            State s) {
        double max = Double.NEGATIVE_INFINITY;
        String bestAction = null;
        for(WeightVector w : weights) {
            double q = w.transTimes(scaler.scale(s.getState()));
            if(q > max) {
                max = q;
                bestAction = w.getActionName();
            }
        }
        return new Choice(bestAction, max);
    }
}
