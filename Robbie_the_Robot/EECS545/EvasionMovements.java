package EECS545;

import robocode.*;
import robocode.util.Utils;
import java.util.*;

/**
 * EvasionMovements - A class to hold the evasion methods
 */
public class EvasionMovements {
	
	// random number generator 
	Random rand = Utils.getRandom();
	
	// robot
	AdvancedRobot robot;
	
	// constructor call
	public EvasionMovements(AdvancedRobot robot){
		this.robot = robot;
	}
	
	/**
	 * evadeLeft: Head tangental-left of the direct line of sight between agent and opponent
	 */
	public void evadeLeft(ScannedRobotEvent e) {
		
		// angle to enemey relative to agent's heading
		double ang = e.getBearing();
		
		// checking conditions for which direction to turn / direction of engines 
		if(Math.abs(ang) < 90) {
			
			if(ang > 0) {
				
				robot.setTurnLeft(90 - ang);
				robot.setAhead(Double.POSITIVE_INFINITY);
				
			} else {
			
				robot.setTurnRight(90 - Math.abs(ang));
				robot.setBack(Double.POSITIVE_INFINITY);
			
			}	
		} else {
		
			if(ang > 0) {
				
				robot.setTurnRight(ang - 90);
				robot.setAhead(Double.POSITIVE_INFINITY);
				
			} else {
			
				robot.setTurnLeft(Math.abs(ang) - 90);
				robot.setBack(Double.POSITIVE_INFINITY);
			
			}	
		}
	}

	/**
	 * evadeRight: Head tangental-right of the direct line of sight between agent and opponent
	 */
	public void evadeRight(ScannedRobotEvent e) {

		// angle to enemey relative to agent's heading
		double ang = e.getBearing();
		
		// checking conditions for which direction to turn / direction of engines 
		if(Math.abs(ang) < 90) {
			
			if(ang > 0) {
				
				robot.setTurnLeft(90 - ang);
				robot.setBack(Double.POSITIVE_INFINITY);
				
			} else {
			
				robot.setTurnRight(90 - Math.abs(ang));
				robot.setAhead(Double.POSITIVE_INFINITY);
			
			}	
		} else {
		
			if(ang > 0) {
				
				robot.setTurnRight(ang - 90);
				robot.setBack(Double.POSITIVE_INFINITY);
				
			} else {
			
				robot.setTurnLeft(Math.abs(ang) - 90);
				robot.setAhead(Double.POSITIVE_INFINITY);
			
			}	
		}
	}			

	/**
	 * random: Head to a randomly selected point
	 */
	public void random() {
		
		// choose random direction to go to
		double turnAng = 90*rand.nextDouble();
		
		// choose at random left of right turn
		int turn = rand.nextInt(2);
		
		// turn left
		if(turn == 0){
			robot.setTurnLeft(turnAng);
			robot.setAhead(Double.POSITIVE_INFINITY);
		
		// turn right	
		} else {
			robot.setTurnRight(turnAng);
			robot.setAhead(Double.POSITIVE_INFINITY);
		}		
	}
	
	/**
	 * halt: Stop all engines
	 */
	public void halt() {
        robot.setAhead(0);
    }
	
	/**
	 * feign: Reverse the direction the robot is moving in
	 */
	public void feign() {
        robot.setAhead(-1*robot.getDistanceRemaining()*Double.POSITIVE_INFINITY);
    }

	// choose a random evasion to execute
	public void executeRandomEvasion(ScannedRobotEvent e) {
		
		// int to hold which evasion to execute
		int evasion = rand.nextInt(5);
		
		// execute chosen evasion technique
		if(evasion == 0){
			evadeLeft(e);
			System.out.println("Evading Left");
		} else if (evasion == 1) {
			evadeRight(e);
			System.out.println("Evading Right");
	    } else if (evasion == 2) {
			random();
			System.out.println("Evading Randomly");
	    } else if (evasion == 3) {
			halt();
			System.out.println("Evading Halt");
	    } else {
			feign();
			System.out.println("Evading Feign");
		}
		
	}
	
}
