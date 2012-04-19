/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EECS545.target;

import EECS545.MirroringEvadingRobot;
import java.util.logging.Level;
import java.util.logging.Logger;
import robocode.Bullet;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 *
 * @author Shiva
 */
public class Gun extends Action {

    private MirroringEvadingRobot robot;
    private double orientation;
    private boolean finished = false;
    public static final double ORIENTATION_ARCH = 80;
    public static final double MIN_ORIENTATION = -ORIENTATION_ARCH/2;
    public static final double MAX_ORIENTATION = ORIENTATION_ARCH/2;
    private Bullet bullet = null;

    public static double orientationToScale(double orientation) {
        double scale = orientation / (ORIENTATION_ARCH/2) + 1;
        return scale;
    }
    
    public static double scaleToOrientation(double scale) {
        return (scale*ORIENTATION_ARCH/2 - ORIENTATION_ARCH/2);
    }
    
    /*
     * Initializes the Gun class.
     *
     * @param robot - instance of the calling class
     *
     * @param name - integer name of the Gun
     *
     * @orientation - the angulat offset from the central line that connects the
     * enemy and the gun. +ve for Clockwise -ve for Anti CLockwise
     */
    public Gun(MirroringEvadingRobot robot, double orientation) {
        this.orientation = orientation;
        this.robot = robot;
    }

    /*
     * Fires the gun. Ensure that the the robot's gun has completed the required
     * turn before caalling execute other wise it will fail and return FALSE.
     */
    public void execute(ScannedRobotEvent e) {
        finished = false;
        turnGun(e);
        robot.out.println(makeName() + " is turning...");
    }

    protected void turnGun(ScannedRobotEvent e) {
        turnGun(e, orientation);
    }
    
    protected void turnGun(ScannedRobotEvent e, double angle) {
        double gunTurn = robot.getHeading() + e.getBearing() - robot.getGunHeading();
        gunTurn += angle;
        robot.setTurnGunRight(Utils.normalRelativeAngleDegrees(gunTurn));
    }

    @Override
    protected String makeName() {
        return "Gun" + orientation;
    }

    @Override
    public boolean isFinished() {
        if (finished) {
            return true;
        }
        if (Math.abs(robot.getGunTurnRemaining()) > 0) {
            return false;
        } else {
            bullet = robot.setFireBullet(robot.getConstants().firePower); 
            if(bullet != null) {
                robot.out.println(makeName() + " fired successfully");
                finished = true;
            } else {
                robot.out.println(makeName() + " couldn't fire; will try again");
                finished = false;
            }
            return finished;
        }
    }

    public Bullet getBullet() {
        return bullet;
    }
    
    

    @Override
    public double getAngle() {
        return orientation;
    }
}
