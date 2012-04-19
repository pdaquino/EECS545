
package EECS545.target;

import EECS545.MirroringEvadingRobot;
import java.util.Random;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 *
 * @author Pedro
 */
public class StochasticGun extends Gun {
    private final double variation;
    private Random rnd = new Random();
    /*
     * Creates a gun centered at orientation degrees, that chooses a firing
     * angle between [orientation - variation/2, orientation + variation/2]
     */
    public StochasticGun(MirroringEvadingRobot robot, double orientation,
            double variation) {
        super(robot, orientation);
        this.variation = variation;
    }
    
    @Override
    protected void turnGun(ScannedRobotEvent e) {
        double noise = (rnd.nextDouble() - 0.5) * variation;
        double firingAngle = getAngle() + noise;
        Output.println("StochasticGun centered at " + getAngle() + " aiming at " + firingAngle);
        turnGun(e, firingAngle);
    }
}
