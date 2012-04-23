package EECS545.target;

import EECS545.MirroringEvadingRobot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import robocode.*;

/**
 * Marvin is our Q-Learner
 *
 * @author Pedro, KK and Shiva (happy now?) You made my day!
 */
public class Marvin extends MirroringEvadingRobot {

    // configuration constants
    // the filename where we store the ranges of the features
    private static final String scalerFilename = "qrange.txt";
    private ActionTracker actionTracker = null;
    //private List<WeightVector> weights = new ArrayList<WeightVector>();
    private FeatureScaler scaler;
    private List<StochasticGun> actions = null;
    private DiscreteWQLearner qLearner = null;
    private Random rnd = new Random();

//    @Override
//    protected void initRobot() {
//        CONSTANTS.mirror_variable_distance = false;
//        try {
//            WeightsIO wIO = new WeightsIO(this);
//            WeightVector weights = null;
//            if(wIO.weightFileExists()) {
//                weights = wIO.loadWeights().get(0);
//                out.println("Marvin: read weights from file");
//            } else {
//                out.println("Marvin: no weights file found; initializing weights to 0");
//            }
//            // if we pass QLearner an empty list of weights, it'll just
//            // create default weights
//            //actions = createActions();
//            scaler = new FeatureScaler(scalerFilename);
//            qLearner = new SingleWQLearner(weights, scaler);
//        } catch (IOException ex) {
//            out.println(ex.getMessage());
//        }
//    }
    @Override
    protected void initRobot() {
        CONSTANTS.mirror_variable_distance = false;
        try {
            WeightsIO wIO = new WeightsIO(this);
            List<WeightVector> weights = null;;
            if (wIO.weightFileExists()) {
                weights = wIO.loadWeights();
                out.println("Marvin: read weights from file");
            } else {
                out.println("Marvin: no weights file found; initializing weights to 0");
            }
            // if we pass QLearner an empty list of weights, it'll just
            // create default weights
            scaler = new FeatureScaler(scalerFilename);
            qLearner = new DiscreteWQLearner(weights, scaler);
            actions = createActions();
        } catch (IOException ex) {
            out.println(ex.getMessage());
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        super.onScannedRobot(e);
        if(this.getGunHeat() > 0) { return; }
        if (actionTracker == null) {
            // we are not tracking any action, so we execute a random one
            Action a = chooseRandomAction();
            out.println("Chose action " + a.getName());
            a.execute(e);
            actionTracker = new ActionTracker(a, this);
            //actionTracker.setVerbose(true);
        } else {
            actionTracker.trackTime();
            if (actionTracker.isFinished()) {
                // it can be finished either because enough time has passed
                // (if the action was NOP) or because the bullet hit or missed
                // the bullet status is update in the event handlers below
                out.println("Marvin: actionTracker has finished");
                learn();
                actionTracker = null; // we are done with this tracker
            }
        }
    }

    @Override
    public void onBulletHit(BulletHitEvent e) {
        out.println("Bullet being tracked hit the opponent");
        assert (actionTracker != null);
        actionTracker.updateBulletStatus(e);
    }

    @Override
    public void onBulletHitBullet(BulletHitBulletEvent e) {
        out.println("Bullet being tracked hit another bullet");
        assert (actionTracker != null);
        actionTracker.updateBulletStatus(e);
    }

    @Override
    public void onBulletMissed(BulletMissedEvent e) {
        out.println("Bullet being tracked missed");
        assert (actionTracker != null);
        actionTracker.updateBulletStatus(e);
    }

    @Override
    protected String evadeBullet(ScannedRobotEvent e) {
        return "feign";
    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        super.onRoundEnded(event);
//        List<WeightVector> weightsList = new ArrayList<WeightVector>();
//        weightsList.add(qLearner.getW());
        List<WeightVector> weightsList = qLearner.getWeightList();
        new WeightsIO(this).saveWeights(weightsList);
    }

    private List<StochasticGun> createActions() {
        List<StochasticGun> actns = new ArrayList<StochasticGun>();
        double variation = Constants.EPS;
        for (WeightVector w : qLearner.getWeightList()) {
            actns.add(new StochasticGun(this, w.getOrientation(), variation));
            Output.println("Marvin: created gun centered at " + w.getOrientation() + ", eps = " + variation);
        }
        return actns;
    }

//    private Action chooseRandomAction() {
//        double rndScaledAngle = rnd.nextDouble();
//        return new Gun(this, Gun.scaleToOrientation(rndScaledAngle));
//        //return new Gun(this, 0.3);
//    }
    private Action chooseRandomAction() {
        int rndIdx = rnd.nextInt(actions.size());
        //int rndIdx = Constants.NUM_GUNS / 2; // fires always at 0
        return actions.get(rndIdx);
    }

    private void learn() {
        assert (actionTracker != null);
        qLearner.learn(
                actionTracker.getS(),
                actionTracker.getAction(),
                actionTracker.getReward(),
                actionTracker.getsPrime());
    }
}
