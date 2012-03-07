package EECS545;
import robocode.*;
import java.awt.Color;
import robocode.util.Utils;



//Version 0.1
//Danger Will Robinson!

/*
*	Working ON:

*	ADDED:
	Gun and Radar independence to Robbie's movement
	Robbies Colors
	Radar Lock
	



*	REMOVED:


*/

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Robbie - a robot by (your name here)
 */
public class Robbie extends Robot
{
	/**
	 * run: Robbie's default behavior
	 */
	 
	boolean mirror_Behavior_Enable;
	Constants CONSTANTS;
	
	public void run() {
		// Initialization of the robot should be put here
		setAdjustRadarForRobotTurn(true);// Make Gun and Radar Movement independent
		setAdjustGunForRobotTurn(true);// of the Robot's movement
		setAdjustRadarForGunTurn(true);//
		setColors(Color.red,Color.blue,Color.green); // body,gun,radar
		
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
		double radarTurn =
        // Absolute bearing to target
        getHeading() + e.getBearing()
        // Subtract current radar heading to get turn required
        - getRadarHeading();
 		
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
}
