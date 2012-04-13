/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EECS545;

import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 *
 * @author Shiva
 */
public class Gun implements Action {
    String name;
    MirroringEvadingRobot robot;
    double orientation;    
    
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
    public Gun(MirroringEvadingRobot robot, String name, double orientation){
        this.robot = robot;
        this.name = name;               
    }
    
    /*
     * Fires the gun. Ensure that the the robot's gun has completed the required 
     * turn before caalling execute other wise it will fail and return FALSE.
     */
    public boolean execute() {
        //To check if the gun has completed the specified turn
        if(Math.abs(robot.getGunTurnRemaining())==0){
            robot.setFire(robot.getConstants().fiePower);
            return true;
        }
        else
            return false; // To specify bullet hasn't fired yet
        
    }
    
    public void turnGun(ScannedRobotEvent e){
        double gunTurn = robot.getHeading() + e.getBearing() - robot.getGunHeading();
        gunTurn += orientation;
        robot.setTurnGunRight(Utils.normalRelativeAngleDegrees(gunTurn));        
    }
    
    /*
     * Returns the name of the Gun as well as its orientation as a String
     */
    public String getName() {
        return new String("Gun"+name+":"+orientation);
    }
    
}
