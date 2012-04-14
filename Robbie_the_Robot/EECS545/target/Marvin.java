
package EECS545.target;

import EECS545.MirroringEvadingRobot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import robocode.*;

/**
 * Marvin is our Q-Learner
 * @author Pedro, KK and Shiva (happy now?) You made my day!
 */
public class Marvin extends MirroringEvadingRobot {

    // configuration constants
    // the filename where we store the ranges of the features
    private static final String scalerFilename = "qrange.txt";
    // the number of guns
    public static final int numGuns = 5;
    // orientation of the left- and rightmost guns
    public static final double minGunOrientation = -50;
    public static final double maxGunOrientation = 50;
    
    private ActionTracker actionTracker = null;

    private List<WeightVector> weights = new ArrayList<WeightVector>();
    private FeatureScaler scaler;
    
    private List<Action> actions = null;
    
    private QLearner qLearner = null;
    
    private Random rnd = new Random();

    @Override
    protected void initRobot() {
        try {
            WeightsIO wIO = new WeightsIO(this);
            if(wIO.weightFileExists()) {
                weights = wIO.loadWeights();
            }
            // if we pass QLearner an empty list of weights, it'll just
            // create default weights
            actions = createActions();
            scaler = new FeatureScaler(scalerFilename);
            qLearner = new QLearner(actions, weights, scaler);
        } catch (IOException ex) {
            out.println(ex.getMessage());
        }
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        super.onScannedRobot(e);
        if(actionTracker == null) {
            // we are not tracking any action, so we execute a random one
            Action a = chooseRandomAction();
            out.println("Chose action " + a.getName());
            a.execute(e);
            actionTracker = new ActionTracker(a, this);
        } else {
            actionTracker.trackTime();
            if(actionTracker.isFinished()) {
                // it can be finished either because enough time has passed
                // (if the action was NOP) or because the bullet hit or missed
                // the bullet status is update in the event handlers below
                learn();
                actionTracker = null; // we are done with this tracker
            }
        }
    }
    
    @Override
    public void onBulletHit(BulletHitEvent e) {
        out.println("Bullet being tracked hit the opponent");
        assert(actionTracker != null);
        actionTracker.updateBulletStatus(e);
    }
    
    @Override
    public void onBulletHitBullet(BulletHitBulletEvent e) {
        out.println("Bullet being tracked hit another bullet");
        assert(actionTracker != null);
        actionTracker.updateBulletStatus(e);
    }
    
    @Override
    public void onBulletMissed(BulletMissedEvent e) {
        out.println("Bullet being tracked missed");
        assert(actionTracker != null);
        actionTracker.updateBulletStatus(e);
    }
    
    @Override
    protected String evadeBullet(ScannedRobotEvent e) {
        return "feign";
    }
    
    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        super.onRoundEnded(event);
        new WeightsIO(this).saveWeights(weights);
    } 

    private List<Action> createActions() {
        List<Action> actns = new ArrayList<Action>();
        actns.add(new NOP());
        double gunOrientInterval = (maxGunOrientation - minGunOrientation) / numGuns;
        for(int i = 0; i < numGuns; i++) {
            double gunOrientation = minGunOrientation + i*gunOrientInterval;
            actns.add(new Gun(this, gunOrientation));
        }
        return actns;
    }

    private Action chooseRandomAction() {
        int idx = rnd.nextInt(actions.size());
        return actions.get(idx);
    }

    private void learn() {
        assert(actionTracker != null);
        qLearner.learn(
                actionTracker.getS(),
                actionTracker.getAction(),
                actionTracker.getReward(),
                actionTracker.getsPrime());
    }

}
