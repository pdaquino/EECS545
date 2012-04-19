package EECS545.target;

import EECS545.MirroringEvadingRobot;
import java.io.IOException;
import java.util.List;
import robocode.*;

/**
 * Robocop uses a greedy policy on an approximated linear value function.
 *
 * @author Pedro
 */
public class Robocop extends MirroringEvadingRobot {
    private static final String scalerFilename = "qrange.txt";
    private FeatureScaler scaler;
    private Gun lastAction = null;
    private long lastActionTurn = 0;
    //private WeightVector weights = null;
    private List<WeightVector> weightsList = null;
    private final long TURN_QUOTA_ACTION = 2;
    private final double MIN_Q_TO_SHOOT = 1.2;
    
    @Override
    protected void initRobot() {
        CONSTANTS.mirror_variable_distance = false;
        try {
            WeightsIO wIO = new WeightsIO(this);
            if (wIO.weightFileExists()) {
                weightsList = wIO.loadWeights();
                out.println("Marvin: read weights from file");
            } else {
                throw new RuntimeException("Robocop: no weights file found");
            }
            scaler = new FeatureScaler(scalerFilename);
        } catch (IOException ex) {
            out.println(ex.getMessage());
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        super.onScannedRobot(e);
        if (lastAction == null) {
            Gun a = chooseBestAction(e);
            out.println("Chose action " + (a == null ? " NOP" : a.getName()));
            if (a != null) {
                a.execute(e);
            }
            lastAction = a;
            lastActionTurn = getTime();
        } else {
            if (lastAction.isFinished()) {
                Bullet firedBullet = lastAction.getBullet();
                Output.println("Last action finished on time");
                lastAction = null;
            } else if (getTime() - lastActionTurn > TURN_QUOTA_ACTION) {
                Output.println("Last action did not finish in time (lastActionTurn = " + lastActionTurn + ")");
                lastAction = null;
            }
        }
    }

    @Override
    public void onBulletHit(BulletHitEvent e) {
        //e.getBullet().getName() 
        out.println("Bullet being tracked hit the opponent");
    }

    @Override
    public void onBulletHitBullet(BulletHitBulletEvent e) {
        out.println("Bullet being tracked hit another bullet");
    }

    @Override
    public void onBulletMissed(BulletMissedEvent e) {
        out.println("Bullet being tracked missed");
    }

    @Override
    protected String evadeBullet(ScannedRobotEvent e) {
        return "feign";
    }

    private Gun chooseBestAction(ScannedRobotEvent e) {
        ReducedState s = new ReducedState(this);
        //SingleWGreedyPolicy.Choice choice = SingleWGreedyPolicy.chooseAction(weights, scaler, s);
        DiscreteWGreedyPolicy.Choice choice = DiscreteWGreedyPolicy.chooseAction(weightsList, scaler, s);
        if (choice.Q > MIN_Q_TO_SHOOT) {
            Output.println("Decided to shoot heading " + choice.orientation + " with Q = " + choice.Q);
            //return new Gun(this, choice.orientation);
            return new StochasticGun(this, choice.orientation, Constants.EPS);
        } else {
            Output.println("No good choice of firing angle (max Q = " + choice.Q + ")");
            return null;
        }
    }
}
