package EECS545;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import robocode.ScannedRobotEvent;

/**
 * Just like C3PO, but samples the best strategy according to the prediction
 * values (ie the "best" prediction is selected with probability proportional
 * to its prediction value).
 * @author Pedro (yes I wrote it)
 */
public class R2D2 extends MirroringEvadingRobot {
    
    // State object to grab features
    private List<SVMPredict> predictors = new ArrayList<SVMPredict>();
    private Random random = new Random();
    
    @Override
    protected void initRobot() {
        // SVMs
        try {
            predictors.add(new SVMPredict(getDataFile("/svm_models/leftModel.txt").getAbsolutePath(), getDataFile("/svm_models/leftScale.txt").getAbsolutePath(), "evadeLeft"));
            predictors.add(new SVMPredict(getDataFile("/svm_models/rightModel.txt").getAbsolutePath(), getDataFile("/svm_models/rightScale.txt").getAbsolutePath(), "evadeRight"));
            predictors.add(new SVMPredict(getDataFile("/svm_models/haltModel.txt").getAbsolutePath(), getDataFile("/svm_models/haltScale.txt").getAbsolutePath(), "halt"));
            predictors.add(new SVMPredict(getDataFile("/svm_models/feignModel.txt").getAbsolutePath(), getDataFile("/svm_models/feignScale.txt").getAbsolutePath(), "feign"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String evadeBullet(ScannedRobotEvent e) {
        Double[] state = new State(this).getState();
        List<Double> positivePredictions = new ArrayList<Double>();
        List<SVMPredict> positivePredictors = new ArrayList<SVMPredict>();
        double totalWeight = 0;
        for(SVMPredict predictor : predictors) {
            double val = predictor.predict(state);
            out.println("* " + predictor.getStrategy() + ": " + val);
            if(val > 0) {
                positivePredictions.add(val);
                positivePredictors.add(predictor);
                totalWeight += val;
            }
        }
        out.println("* total weight: " + totalWeight);
        if (totalWeight == 0) {
            out.println("Chose Random Strategy");
            out.println();
            em.evade("random", e);
            return "random";
        }
        int sampledIndex = sample(totalWeight, positivePredictions);
        String sampledStrategy = positivePredictors.get(sampledIndex).getStrategy();
        out.println("Sampled strategy " + sampledStrategy +
                " with value = " + positivePredictions.get(sampledIndex));
        em.evade(sampledStrategy, e);
        return sampledStrategy;
    }

    // returns the index of the prediction that was sampled
    private int sample(double totalWeight, List<Double> predictions) {
        double rand = random.nextDouble() * totalWeight;
        double weightSum = 0;
        for(int i = 0; i < predictions.size(); i++) {
            weightSum += predictions.get(i); 
            if (weightSum >= rand) {
                return i;
            }
        }
        throw new IllegalStateException("No predictor could be sampled");
    }    
}
