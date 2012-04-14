/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EECS545.target;

import EECS545.MirroringEvadingRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 *
 * @author Shiva
 */
public class Gun extends Action {
    MirroringEvadingRobot robot;
    double orientation;
    String name;
    
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
    public Gun(MirroringEvadingRobot robot, double orientation){
        this.robot = robot;
        this.name = Double.toString(orientation);               
    }
    
    /*
     * Fires the gun. Ensure that the the robot's gun has completed the required 
     * turn before caalling execute other wise it will fail and return FALSE.
     */
    public void execute(ScannedRobotEvent e) {
        turnGun(e);
        // block while the gun is turning
        while(Math.abs(robot.getGunTurnRemaining())>0){}
            robot.setFire(robot.getConstants().firePower);
        }
    
    private void turnGun(ScannedRobotEvent e){
        double gunTurn = robot.getHeading() + e.getBearing() - robot.getGunHeading();
        gunTurn += orientation;
        robot.setTurnGunRight(Utils.normalRelativeAngleDegrees(gunTurn));        
    }
    
    @Override
    protected String makeName() {
        return "Gun"+name+":"+orientation;
    }
    
}
