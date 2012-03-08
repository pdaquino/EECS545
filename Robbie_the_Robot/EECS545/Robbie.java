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
public class Robbie extends AdvancedRobot
{
	 
	Constants CONSTANTS;
	
	// holds the last scan of the enemy for access to other methods
	ScannedRobotEvent lastE;
	
	// holds the previous energy of the opponent
	double prevEnergy = 100.0;
	
	// tracking of a bullet 
	BulletTracking incoming = new BulletTracking();
	
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
		
		CONSTANTS.mirrorBehaviorEnable();
		setTurnRadarRight(Double.POSITIVE_INFINITY);	
		
		
		// main robot loop
		while(true) {
			
			// interrupts onScannedRobot event 
			scan();
			
			// update tracking of enemy bullet
			if(lastE != null)
				updateBulletTracking();

		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		
		// update last scan object
		lastE = e;
		
		// determine angle from enemy to current radar direction
		double radarTurn = getHeading() + e.getBearing() - getRadarHeading();
 		
		// turn radar according to angle above
    	setTurnRadarRight(Utils.normalRelativeAngleDegrees(radarTurn));

		if(CONSTANTS.getMirrorBehaviorFlag()){
			mirrorBehavior(e);
		}
		
	}
	
	/*
	 * Mirrors the Opponent's moves either just laterally or diagonolly 
	 * while maintaining a minimum distance threshold 'd'. This is done as
         * series of Forces modelled as:
         *          F = k1 * (Distance to Opponent - d)
         *          d = d_const - (Robbie's Health - Opponent's Health)
	 */
	private void mirrorBehavior(ScannedRobotEvent e) {
            
            e.getDistance();
	}
	

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		
		// check if the bullet that hit us is the bullet we were tracking
		boolean hit = incoming.checkHit(e.getHeading(), getX(), getY(), getTime());
		
		if(hit)
			out.println("	Bullet Being Tracked Hit Us");
		else
			out.println("	A Different Bullet Hit US");
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		
	}

	/**
	 * updateBulletTracking: Check to see if the enemy fired / update a bullet being tracked
	 *
	 */
	public void updateBulletTracking() {
		
		// we are already tracking a bullet
		if(incoming.getStatus()){
			
			// reset previous energy value
			prevEnergy = lastE.getEnergy();
			
			// check if the last bullet has missed us
			boolean passed = incoming.bulletPassed(getX(), getY(), getTime());
			if(passed)
				out.println("	The Bullet Being Tracked Missed");
			else
				out.println("	Bullet is still active");
			
			return;
		
		// we are not currently tracking a bullet	
		} else {
		
			// energy drop of enemy
			double eDrop = prevEnergy - lastE.getEnergy();
		
			// reset previous energy value
			prevEnergy = lastE.getEnergy();
		
			// check boundaries of firing
			if(eDrop > 0 && eDrop <= 3) {
				
				// enemy fired a bullet, begin tracking
				incoming = new BulletTracking(eDrop, lastE, new double[] {getX(), getY(), getHeading()}, getTime());	
				out.println("Enemy Fired a Bullet");
			}
		}
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
