
package EECS545.target;

import EECS545.MirroringEvadingRobot;
import robocode.ScannedRobotEvent;

/**
 * Marvin is our Q-Learner
 * @author Pedro, KK and Shiva (happy now?) You made my day!
 */
public class Marvin extends MirroringEvadingRobot {

    @Override
    protected String evadeBullet(ScannedRobotEvent e) {
        return "feign";
    }

}
