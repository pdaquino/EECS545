package EECS545;

import java.awt.Color;
import java.awt.Rectangle;
import robocode.*;
import robocode.util.Utils;

public abstract class MirroringTargetingRobot extends AdvancedRobot {
	//output option
    boolean output = false;
    // close to wall flag
    boolean closeToWall = false;
    // battle field information
    double envWidth;
    double envHeight;
    // last time orientation of robot and enemy
    double lastRobotHeading = 0;
    double lastEnemyHeading = 0;
	// constant object 
    Constants CONSTANTS;
    // holds the last scan of the enemy for access to other methods
    ScannedRobotEvent lastE;
    // target log file
    TargetLog targetLog = null;
	// lets the robot know that it is on target after first finding the enemy
	boolean onTarget = false;
	// flag on if we have shot and do not have a result of the shot yet
	boolean roundDownRange = false;
	// bullet fired at enemy
	Bullet shot = null;
	// last angle fired at
	Double angleFired = 0.0;
	// flag for if we are aligning the gun for a shot or not
	boolean aligningGun = false;
	double angleToAlign = 0;
    // method must return the cannon angle
    protected abstract Double targetEnemy();
	// init robot
    protected void initRobot() {}

    public void run() {

        // initialize robot
        initRobot();

        // grab battle field information
        envWidth = getBattleFieldWidth();
        envHeight = getBattleFieldHeight();

        // radar and robot independent turning
        setAdjustRadarForRobotTurn(true);

        // gun and robot independent turning
        setAdjustGunForRobotTurn(true);

		// radar and gun independent turning
        setAdjustRadarForGunTurn(true);
	
		// constant object
        CONSTANTS = new Constants();

        // body, gun, radar color
        setColors(Color.red, Color.blue, Color.green);

        // search for opponent using radar
        setTurnRadarRight(Double.POSITIVE_INFINITY);
		setTurnGunRight(Double.POSITIVE_INFINITY);
		
        // initialize targeting log
        try {
        	targetLog = new TargetLog(this);
        } catch (Exception e) {
            out.println("Error it trying to initialize the targeting log.");
            out.println(e.getMessage());
        }

        if (targetLog == null) {
            out.println("Targeting log was not initialized.");
        }

        // main robot loop
        while (true) {

            // interrupts onScannedRobot event 
            scan();

            // update tracking of enemy bullet
            if (lastE != null && onTarget)
                updateTargeting();

            // update last bearing for both enemy and robot
            lastRobotHeading = getHeading();
            if (lastE != null) {
                lastEnemyHeading = lastE.getHeading();
            }
        }
    }

    /**
     * onScannedRobot: What to do when you see another robot
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {

        // update last scan object
        lastE = e;

        // determine angle from enemy to current radar direction
        double radarTurn = getHeading() + e.getBearing() - getRadarHeading();
		double gunTurn = getHeading() + e.getBearing() - getGunHeading();

        // turn radar according to above
        setTurnRadarRight(Utils.normalRelativeAngleDegrees(radarTurn));
		
		// turn gun according to above if we are not attempting to align for a shot
		if(!aligningGun)
			setTurnGunRight(Utils.normalRelativeAngleDegrees(gunTurn));
		
		// check to see if the gun is on the enemy
		if(!onTarget) {
			if(Math.abs(Utils.normalRelativeAngleDegrees(gunTurn)) < 2)
				onTarget = true;
		}
			
        // mirror opponent
		if(!closeToWall)
	    	mirrorBehavior(e);

        // if too close to a wall, move away from it towards the center of the room
        if (getX() < CONSTANTS.wallAvoidDistance || getX() > (envWidth - CONSTANTS.wallAvoidDistance) || getY() < CONSTANTS.wallAvoidDistance || getY() > (envHeight - CONSTANTS.wallAvoidDistance)) {
        	if (output)
            	out.println("Too close to wall - heading towards center");
            moveToCenter();
            closeToWall = true;
        } else {
            closeToWall = false;
        }
    }

    /*
     * Mirrors the opponent.
     */
    private void mirrorBehavior(ScannedRobotEvent e) {
        double F;
        double d;
        d = CONSTANTS.mirror_distance - (getEnergy() - e.getEnergy());
        F = CONSTANTS.mirror_Force_k1 * (e.getDistance() - d);
        setTurnRight(e.getBearing());
        setAhead(F);
    }

	// determining weather to shoot or update a shot we have taken
    public void updateTargeting() {

        // we are already tracking a shot at the enemy
        if (roundDownRange) {

            // bullet it no longer active
            if (!shot.isActive()) {
	
				// shot hit the enemy
                if (shot.getVictim() != null) {
                    targetLog.endTargetBullet(false);
					if (output)
						out.println("The shot taken hit the enemy.");
					roundDownRange = false;
                } else {
					targetLog.endTargetBullet(true);
					if (output)
						out.println("The shot taken missed the enemy.");
					roundDownRange = false;
				}	
            }
        
		// we are not currently tracking a shot at the enemy
        } else {

			// capable of shooting and not currently aligning our gun
            if(getGunHeat() == 0 && !aligningGun){
				
				// determine angle to fire at, and turn to that angle
				Double angleToFire = targetEnemy();
				
				if(angleToFire == null)
					return;
				else
					angleFired = angleToFire;
				
				// if angle is 0, we can just shoot
				if(angleFired == 0) {
					shot = fireBullet(1.0);
					roundDownRange = true;
					targetLog.startTargetBullet();
					aligningGun = false;
					return;
				}
			
				// calculate proper gun angle for shot
				aligningGun = true;
				angleToAlign = Utils.normalAbsoluteAngleDegrees(getGunHeading() + angleFired);
				setTurnGunRight(angleFired);
			}
		
			// currently aligning gun
			else if(aligningGun){
				
				// alignment difference
				double diff = angleToAlign - getGunHeading();
				
				// we have aligned the gun
				if(Math.abs(diff) < 0.1){
					shot = fireBullet(1.0);
					roundDownRange = true;
					targetLog.startTargetBullet();
					aligningGun = false;
				}
			}
        }
    }

    // return the last scan
    public ScannedRobotEvent getLastE() {
        return lastE;
    }

    // return last robot heading
    public double getLastRobotHeading() {
        return lastRobotHeading;
    }

    // return last enemy heading
    public double getLastEnemyHeading() {
        return lastEnemyHeading;
    }

    // return height of environment
    public double getEnvHeight() {
        return envHeight;
    }

    // return width of environment
    public double getEnvWidth() {
        return envWidth;
    }

	// return last angle robot fired in
	public double getAngleFired() {
		return angleFired;
	}

    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        out.println("The round has ended");
        targetLog.close();
    }

	// move towards center of the room (from EM movements)
	public void moveToCenter() {
		
		// angle to center
		double phi = -Math.toDegrees(Math.atan2((envHeight/2)-getY(), (envWidth/2)-getX())) + 90;
		
		// angle to x axis relative to robot's heading
		double psi = Utils.normalRelativeAngleDegrees(getHeading());
		
		// angle to center of room relative to robot's heading
		double ang = -Utils.normalRelativeAngleDegrees(psi - phi);
		
		// checking conditions for which direction to turn / direction of engines 
		if(Math.abs(ang) < 90) {
			
			if(ang > 0) {
				
				setTurnRight(ang);
				setAhead(Double.POSITIVE_INFINITY);
				
			} else {
			
				setTurnLeft(ang);
				setAhead(Double.POSITIVE_INFINITY);
			
			}	
		} else {
		
			if(ang > 0) {
				
				setTurnLeft(180 - ang);
				setBack(Double.POSITIVE_INFINITY);
				
			} else {
			
				setTurnRight(180 - Math.abs(ang));
				setBack(Double.POSITIVE_INFINITY);
			
			}	
		}
		
	}
}									