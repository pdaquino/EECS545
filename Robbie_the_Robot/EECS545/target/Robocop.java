package EECS545.target;

import EECS545.MirroringEvadingRobot;
import java.io.IOException;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.ScannedRobotEvent;

/**
 * Robocop uses a greedy policy on an approximated linear value function.
 *
 * @author Pedro
 */
public class Robocop extends MirroringEvadingRobot {
    private static final String scalerFilename = "qrange.txt";
    private FeatureScaler scaler;
    private Action lastAction = null;
    private long lastActionTurn = 0;
    private WeightVector weights = null;
    private final long TURN_QUOTA_ACTION = 2;
    private final double MIN_Q_TO_SHOOT = -0.2;

    @Override
    protected void initRobot() {
        CONSTANTS.mirror_variable_distance = false;
        try {
            WeightsIO wIO = new WeightsIO(this);
            if (wIO.weightFileExists()) {
                weights = wIO.loadWeights().get(0);
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
            Action a = chooseBestAction(e);
            out.println("Chose action " + (a == null ? " NOP" : a.getName()));
            if (a != null) {
                a.execute(e);
            }
            lastAction = a;
            lastActionTurn = getTime();
        } else {
            if (lastAction.isFinished()) {
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

    private Action chooseBestAction(ScannedRobotEvent e) {
        ReducedState s = new ReducedState(this);
        SingleWGreedyPolicy.Choice choice = SingleWGreedyPolicy.chooseAction(weights, scaler, s);
        if (choice.Q > MIN_Q_TO_SHOOT) {
            Output.println("Decided to shoot heading " + choice.orientation + " with Q = " + choice.Q);
            return new Gun(this, choice.orientation);
        } else {
            Output.println("No good choice of firing angle (max Q = " + choice.Q + ")");
            return null;
        }
    }
}
