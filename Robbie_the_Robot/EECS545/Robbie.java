package EECS545;

import robocode.*;
import java.awt.Color;
import robocode.util.Utils;

//Version 0.1
//Danger Will Robinson!
// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html
/**
 * Robbie - a robot by (your name here)
 */
public class Robbie extends AdvancedRobot {
    /*
     * run: Robbie's default behavior
     */

    Constants CONSTANTS;
    // holds globally last ScannedRobotEvent
    ScannedRobotEvent lastEvent;

    public void run() {

        // radar and robot independent turning
        setAdjustRadarForRobotTurn(true);

        // gun and robot independent turning
        setAdjustGunForRobotTurn(true);

        // radar and gun independent turning
        setAdjustRadarForGunTurn(true);

        // body, gun, radar color
        setColors(Color.red, Color.blue, Color.green);

        CONSTANTS = new Constants();

        CONSTANTS.mirrorBehaviorEnable();
        setTurnRadarRight(Double.POSITIVE_INFINITY);
        setAhead(Double.POSITIVE_INFINITY);

        // Robot main loop
        while (true) {

            scan();/*
             * Interrupts onScannedRobot event immediately and starts it from
             * the top. KEEP AS LAST LINE IN THE WHILE LOOP
             */
        }
    }

    /**
     * onScannedRobot: What to do when you see another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {

        // determine angle from enemy to current radar direction
        double radarTurn = getHeading() + e.getBearing() - getRadarHeading();

        // turn radar according to angle above
        setTurnRadarRight(Utils.normalRelativeAngleDegrees(radarTurn));

        if (CONSTANTS.getMirrorBehaviorFlag()) {
            mirrorBehavior(e);
        }

    }

    /*
     * Mirrors the Opponent's moves either just laterally or diagonolly while
     * maintaining a minimum distance threshold 'd'. This is done as series of
     * Forces modelled as: F = k1 * (Distance to Opponent - d) d = d_const -
     * (Robbie's Health - Opponent's Health)
     */
    private void mirrorBehavior(ScannedRobotEvent e) {

        e.getDistance();
    }

    /**
     * onHitByBullet: What to do when you're hit by a bullet
     */
    public void onHitByBullet(HitByBulletEvent e) {
        // Replace the next line with any behavior you would like
    }

    /**
     * onHitWall: What to do when you hit a wall
     */
    public void onHitWall(HitWallEvent e) {
    }

    /**
     * evadeLeft: Head tangental-left of the direct line of sight between agent
     * and opponent
     */
    public void evadeLeft(ScannedRobotEvent e) {
        // angle to enemey relative to agent's heading
        double ang = e.getBearing();

        // checking conditions for which direction to turn / direction of engines 
        if (Math.abs(ang) < 90) {
            if (ang > 0) {
                setTurnLeft(90 - ang);
                setAhead(Double.POSITIVE_INFINITY);
            } else {
                setTurnRight(90 - Math.abs(ang));
                setBack(Double.POSITIVE_INFINITY);
            }
        } else {
            if (ang > 0) {
                setTurnRight(ang - 90);
                setAhead(Double.POSITIVE_INFINITY);
            } else {
                setTurnLeft(Math.abs(ang) - 90);
                setBack(Double.POSITIVE_INFINITY);
            }
        }
    }

    /**
     * evadeRight: Head tangental-right of the direct line of sight between
     * agent and opponent
     */
    public void evadeRight(ScannedRobotEvent e) {
        // angle to enemey relative to agent's heading
        double ang = e.getBearing();

        // checking conditions for which direction to turn / direction of engines 
        if (Math.abs(ang) < 90) {
            if (ang > 0) {
                setTurnLeft(90 - ang);
                setBack(Double.POSITIVE_INFINITY);
            } else {
                setTurnRight(90 - Math.abs(ang));
                setAhead(Double.POSITIVE_INFINITY);
            }
        } else {
            if (ang > 0) {
                setTurnRight(ang - 90);
                setBack(Double.POSITIVE_INFINITY);
            } else {
                setTurnLeft(Math.abs(ang) - 90);
                setAhead(Double.POSITIVE_INFINITY);

            }
        }
    }
    
    public void halt() {
        setAhead(0);
    }
    
    public void feign() {
        System.out.println("Feigning: distanceRemaining is " + getDistanceRemaining());
        setAhead(-1*getDistanceRemaining()*Double.POSITIVE_INFINITY);
    }
    
    @Override
    public void onKeyPressed(java.awt.event.KeyEvent e) {
        halt();
    }
}
