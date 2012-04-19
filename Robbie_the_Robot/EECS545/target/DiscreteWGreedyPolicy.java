
package EECS545.target;

import java.util.List;

/**
 *
 * @author Pedro
 */
public class DiscreteWGreedyPolicy {
    public static class Choice {
        public double orientation;
        public double Q;
        public Choice(double orientation, double q) {
            this.orientation = orientation;
            this.Q = q;
        }
    }
    public static Choice chooseAction(List<WeightVector> weights, FeatureScaler scaler,
            ReducedState s) {
        double max = Double.NEGATIVE_INFINITY;
        String bestAction = null;
        double bestAngle = 0;
        for(WeightVector w : weights) {
            double q = w.transTimes(scaler.scale(s.getState()));
            Output.println("Angle " + w.getOrientation() + " => Q = " + q);
            if(q > max) {
                max = q;
                bestAction = w.getActionName();
                bestAngle = w.getOrientation();
            }
        }
        Output.println("GreedyPolicy chose to fire at " + bestAngle);
        return new Choice(bestAngle, max);
    }

}
