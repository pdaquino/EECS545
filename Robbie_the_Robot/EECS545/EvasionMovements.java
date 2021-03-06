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
    private AdvancedRobot robot;
	
	// output flag
	boolean output;
	
	// constructor call
	public EvasionMovements(MirroringEvadingRobot robot, boolean output){
		this.robot = robot; 
		this.output = output;
	}

	public EvasionMovements(SVMBot robot, boolean output){
		this.robot = robot;
		this.output = output;
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
	public String executeRandomEvasion(ScannedRobotEvent e) {
		
		// int to hold which evasion to execute
		int evasion = rand.nextInt(4);
		
		// execute chosen evasion technique
		if(evasion == 0){
			evadeLeft(e);
			if(output)
				System.out.println("	Evading Left");
			return "evadeLeft";
		} else if (evasion == 1) {
			evadeRight(e);
			if(output)
				System.out.println("	Evading Right");
			return "evadeRight";
	    } else if (evasion == 2) {
			halt();
			if(output)
				System.out.println("	Evading Halt");
			return "halt";
	    } else {
			feign();
			if(output)
				System.out.println("	Evading Feign");
			return "feign";
		}
	}
        
    public String[] possibleMovements() {
        return new String[] {
            "evadeLeft", "evadeRight", "halt", "feign", "random"
        };
    }
        
    // employ pre-determined evasion strategy
    public void evade(String strategy, ScannedRobotEvent e) {
        
        if(strategy.equals("evadeLeft"))
            evadeLeft(e);
        else if(strategy.equals("evadeRight"))
            evadeRight(e);
        else if(strategy.equals("halt"))
            halt();
        else if(strategy.equals("feign"))
            feign();
        else
            random();
    }

	// move towards center of the room
	public void moveToCenter() {
		
		// angle to center
		double phi = -Math.toDegrees(Math.atan2((robot.getBattleFieldHeight()/2)-robot.getY(), (robot.getBattleFieldWidth()/2)-robot.getX())) + 90;
		
		// angle to x axis relative to robot's heading
		double psi = Utils.normalRelativeAngleDegrees(robot.getHeading());
		
		// angle to center of room relative to robot's heading
		double ang = -Utils.normalRelativeAngleDegrees(psi - phi);
		
		// checking conditions for which direction to turn / direction of engines 
		if(Math.abs(ang) < 90) {
			
			if(ang > 0) {
				
				robot.setTurnRight(ang);
				robot.setAhead(Double.POSITIVE_INFINITY);
				
			} else {
			
				robot.setTurnLeft(ang);
				robot.setAhead(Double.POSITIVE_INFINITY);
			
			}	
		} else {
		
			if(ang > 0) {
				
				robot.setTurnLeft(180 - ang);
				robot.setBack(Double.POSITIVE_INFINITY);
				
			} else {
			
				robot.setTurnRight(180 - Math.abs(ang));
				robot.setBack(Double.POSITIVE_INFINITY);
			
			}	
		}
		
	}
	
}
