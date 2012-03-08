package EECS545;
import robocode.*;
import robocode.util.Utils;

/**
 * Bullet - class for tracking enemy bullets
 */
public class BulletTracking
{
	// information about the bullet
	private double velocity;
	private long bulletTime;
	private boolean tracking;
	
	// information about where the enemy shot the bullet from
	private double enemyX;
	private double enemyY;
	
	/**
	 * Constructor call - call immediately upon knowledge that enemy has fired a bullet
	 * 
	 * @param energyDrop
	 *            The amount of energy the enemy dropped upon firing the bullet (will
	 *			  be between 1 and 3). 
	 * @param e
	 *            The scan of the enemy which was used to detect that the enemy fired
	 *            a bullet.
	 * @param pose
	 *            The current pose of the robot: x, y, theta. Use the robot method calls 
	 *			  getX(), getY(), and getHeading() respectively. 
	 * @param time
	 *            The current time in the environment. Use the robot method call getTime(). 
	 */
	public BulletTracking(double energyDrop, ScannedRobotEvent e, double[] pose, long time){
		
		velocity = 20 - 3*energyDrop;
		bulletTime = time;
		tracking = true;
		
		// determine location of where the enemy fired from
		double beta = Utils.normalRelativeAngleDegrees(pose[2] + e.getBearing());
		enemyX = pose[0] + e.getDistance()*Math.sin(Math.toRadians(beta));
		enemyY = pose[1] + e.getDistance()*Math.cos(Math.toRadians(beta));
	}

	public BulletTracking(){
		tracking = false;
	}
	
	/**
	 * bulletPassed: If the robot is currently tracking this bullet, check to see if the 
	 *               bullet has passed the robot, and thus will not be hit by this bullet. 
	 * 
	 * @param robotX
	 *            The current x-axis location of the robot. Use the robot method call getX().
	 * @param robotY
	 *            The current y-axis location of the robot. Use the robot method call getY().
	 * @param time
	 *            The current time in the environment. Use the robot method call getTime().
	 *
	 * @return Returns true if the bullet is currently being tracked and it has missed the 
	 *         robot. The 'tracking' flag of this object is then set to false, as the bullet
	 *         no longer needs to be tracked. If this method is called on a bullet object that 
	 *         is not being tracked (i.e. the bullet has already missed the robot), then the 
	 *         method will return true. Otherwise, the bullet is being tracked, and has not 
	 *         yet passed the robot, and thus false is returned.  
	 */
	public boolean bulletPassed(double robotX, double robotY, long time){
		
		// check on if current tracking a bullet
		if(tracking){
		
			// calculate distance to where the enemy fired
			double distToEnemy = Math.sqrt(Math.pow(robotX - enemyX, 2) + Math.pow(robotY - enemyY, 2));
		
			// calculate the amount of distance the bullet could have traveled
			double distBulletTraveled = velocity*(time - bulletTime);
		
			// check if bullet has passed
			if((distToEnemy + 40) < distBulletTraveled) {
				tracking = false;
				return true;
			}
		
			return false;
		}
		
		return true;
	}

	/**
	 * checkHit: Check to see if the bullet that just hit the robot was this bullet that the  
	 *           robot is tracking.
	 *
	 * @param bulletHeading
	 *            The direction the bullet was heading before it hit the agent. Use the hit by
	 *			  bullet event method call getHeading().
	 * @param robotX
	 *            The current x-axis location of the robot. Use the robot method call getX().
	 * @param robotY
	 *            The current x-axis location of the robot. Use the robot method call getY().
	 * @param time
     *            The current time in the environment. Use the robot method call getTime().
	 *
	 * @return Returns true if the bullet that just hit the robot was this bullet that the 
	 *		   robot is tracking. If so, the tracking of this bullet is set to false. Otherwise,
	 *         false is returned and this bullet continues to considered as tracking.
	 */
	public boolean checkHit(double bulletHeading, double robotX, double robotY, long time){
		
		// check if we are tracking this bullet
		if(tracking){
			
			// angle from location of fired tracked bullet to current robot position
	  		double beta = Utils.normalAbsoluteAngleDegrees(90-Math.toDegrees(Math.atan2(robotY-enemyY, robotX-enemyX)));
		
			// calculate distance to where the enemy fired
			double distToEnemy = Math.sqrt(Math.pow(robotX - enemyX, 2) + Math.pow(robotY - enemyY, 2));
			
			// calculate angle tolereance
			double angleTolerance = Math.toDegrees(Math.atan2(30, distToEnemy));
		
			// difference between bullet heading and beta
			double bearingDiff = Math.abs(Utils.normalRelativeAngleDegrees(beta - bulletHeading));
		
			// difference between expected distance bullet traveled and 
			double distanceDiff = distToEnemy - (velocity * (time-bulletTime));
			
			// this bullet is the one that hit us
			if(bearingDiff < angleTolerance && distanceDiff > 0 && distanceDiff < 35){
				tracking = false;
				return true;
			}
			
			// this bullet is not the one that hit us
			return false;
		}
	
		// we aren't tracking this bullet, so it couldn't be the one that hit us (we have determined 
		// earlier that this bullet missed us)
		return false;
	}

	/**
	 * getStatus: Returns the tracking status of the bullet object. 
	 *
	 * @return Returns true if this bullet is currently being tracked, and false otherwise. 
	 *         Note that a return of false indicates that this bullet was at one point 
	 *         initialized, and has since been found to have missed the robot. 
	 */
	public boolean getStatus() {
		return tracking;
	}
}
