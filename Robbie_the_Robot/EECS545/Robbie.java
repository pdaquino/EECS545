package EECS545;
import robocode.*;
import java.awt.Color;
import robocode.util.Utils;

//Version 0.1
//Danger Will Robinson!

/*
*	Working ON:

*	ADDED:
	Left / Right Evasion Methods


*	REMOVED:


*/

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Robbie - a robot by (your name here)
 */
public class Robbie extends AdvancedRobot
{
	/*
	 * run: Robbie's default behavior
	 */
	 
	boolean mirror_Behavior_Enable;
	Constants CONSTANTS;
	
	public void run() {
		
		// radar and robot independent turning
		setAdjustRadarForRobotTurn(true);
		
		// gun and robot independent turning
		setAdjustGunForRobotTurn(true);
		
		// radar and gun independent turning
		setAdjustRadarForGunTurn(true);
		
		// body, gun, radar color
		setColors(Color.red,Color.blue,Color.green);
		
		CONSTANTS = new Constants();
		
		mirror_Behavior_Enable = true;
		turnRadarRight(Double.POSITIVE_INFINITY);	
		
		
		// Robot main loop
		while(true) {
		
			scan();/* Interrupts onScannedRobot event immediately and starts it 
					* from the top. KEEP AS LAST LINE IN THE WHILE LOOP
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
    	turnRadarRight(Utils.normalRelativeAngleDegrees(radarTurn));

		if(mirror_Behavior_Enable){
			mirrorBehavior(e);
		}
		
	}
	
	/*
	*	Mirrors the Opponent's moves either just laterally or diagonolly while 
	*	maintaining a minimum distance threshold 'd'
	*/
	private void mirrorBehavior(ScannedRobotEvent e) {
		

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
		// Replace the next line with any behavior you would like
		
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
				
				setTurnLeft(90 - ang);
				setAhead(Double.POSITIVE_INFINITY);
				
			} else {
			
				setTurnRight(90 - Math.abs(ang));
				setBack(Double.POSITIVE_INFINITY);
			
			}	
		} else {
		
			if(ang > 0) {
				
				setTurnRight(ang - 90);
				setAhead(Double.POSITIVE_INFINITY);
				
			} else {
			
				setTurnLeft(Math.abs(ang) - 90);
				setBack(Double.POSITIVE_INFINITY);
			
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
				
				setTurnLeft(90 - ang);
				setBack(Double.POSITIVE_INFINITY);
				
			} else {
			
				setTurnRight(90 - Math.abs(ang));
				setAhead(Double.POSITIVE_INFINITY);
			
			}	
		} else {
		
			if(ang > 0) {
				
				setTurnRight(ang - 90);
				setBack(Double.POSITIVE_INFINITY);
				
			} else {
			
				setTurnLeft(Math.abs(ang) - 90);
				setAhead(Double.POSITIVE_INFINITY);
			
			}	
		}
	}								
}
