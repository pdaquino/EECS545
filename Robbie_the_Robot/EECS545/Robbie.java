package EECS545;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

//Version 0.1
//Danger Will Robinson!
//
// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html
public class Robbie extends MirroringEvadingRobot {

    @Override
    public String evadeBullet(ScannedRobotEvent e) {
        return em.executeRandomEvasion(lastE);
    }
    // output flag
}
