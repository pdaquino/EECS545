
package EECS545;

import java.awt.Color;
import java.awt.Rectangle;
import robocode.*;
import robocode.util.Utils;

/**
 *
 * @author Pedro . . . you may have refactored . . . but this isn't all yours, selfish!
 */
public abstract class MirroringEvadingRobot extends AdvancedRobot {
	//output option
   	boolean output = false;
	// close to wall flag
	boolean closeToWall = false;
    // battle field information
    double envWidth;
    double envHeight;
	//energy drop of opponent
	double eDrop = 0;
    // last time orientation of robot and enemy
    double lastRobotHeading = 0;
    double lastEnemyHeading = 0;
    // current evasion strategy
    String strategy;
    // constant object 
    Constants CONSTANTS;
    // evasion techniques
    EvasionMovements em = new EvasionMovements((MirroringEvadingRobot)this, output);
    // holds the last scan of the enemy for access to other methods
    ScannedRobotEvent lastE;
    // holds the previous energy of the opponent
    double prevEnergy = 100.0;
    // tracking of a bullet 
    BulletTracking incoming = new BulletTracking();
    // evasion log file
    EvasionLog evasionLog = null;
    //survival log file
    SurvivalLog survLog = null;
    
    // method must return the strategy it performed
    protected abstract String evadeBullet(ScannedRobotEvent e);
    protected void initRobot() { }

    /*
     * Do not create a constructor for Robbie, it creats a whole bunch of weird
     * errors. Put all initialization code in the run() method -
     */
    //Force Arbitrator for movement control
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

        // body, gun, radar color
        setColors(Color.red, Color.blue, Color.green);

        // constant object
        CONSTANTS = new Constants();

        // enable mirroring behavior
        CONSTANTS.mirrorBehaviorEnable();

        // search for opponent using radar
        setTurnRadarRight(Double.POSITIVE_INFINITY);

        //Initializae Evasion Log
        try {
            if (CONSTANTS.evasionLog_Enable) {
                evasionLog = new EvasionLog(this);
            }
        } catch (Exception e) {
            out.println("**ERROR in trying to initiliaze the EvasionLog obj");
            out.println(e.getMessage());
        }

        if (evasionLog == null) {
            out.println("** EvasionLog not Initiliazed **");
        }
        
        //Initializae Survival Log
        try {
            if (CONSTANTS.survivalLog_Enable) {
                survLog = new SurvivalLog(this);
            }
        } catch (Exception e) {
            out.println("**ERROR in trying to initiliaze the SurvivalLog obj");
            out.println(e.getMessage());
        }

        if (survLog == null) {
            out.println("** SurvivalLog not Initiliazed **");
        }

        // main robot loop
        while (true) {

            // interrupts onScannedRobot event 
            scan();

            // update tracking of enemy bullet
            if (lastE != null) {
                updateBulletTracking();
            }

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

        // turn radar according to angle above
        setTurnRadarRight(Utils.normalRelativeAngleDegrees(radarTurn));

		// if mirror behavior is enabled, continue to mirror opponent
        if (CONSTANTS.getMirrorBehaviorFlag()) 
            mirrorBehavior(e);
		else if (closeToWall)
			em.halt();

		// if too close to a wall, move away from it towards the center of the room
        if (CONSTANTS.wallAvoid_Enable) {
			if(getX() < CONSTANTS.wallAvoidDistance || getX() > (envWidth - CONSTANTS.wallAvoidDistance) || getY() < CONSTANTS.wallAvoidDistance || getY() > (envHeight - CONSTANTS.wallAvoidDistance)){
				if(output)
					out.println("Too close to wall - heading towards center");
				em.moveToCenter();
				closeToWall = true;
            }
			else
				closeToWall = false;
        }
    }

    // evade a possible bullet (it could have been  wall hit).
    // returns true if it was an actual bullet
    private void evadePossibleBullet() {
	
        // calculate enemy's location
        double beta = Utils.normalRelativeAngleDegrees(getHeading() + lastE.getBearing());
        double enemyX = getX() + lastE.getDistance() * Math.sin(Math.toRadians(beta));
        double enemyY = getY() + lastE.getDistance() * Math.cos(Math.toRadians(beta));

        // ensure enemy is not close to a wall IOT avoid mistaking wall collision for a bullet fire
        if (enemyX < 25 || enemyX > (envWidth - 25) || enemyY < 25 || enemyY > (envHeight - 25)) {
            // reset previous energy value
            prevEnergy = lastE.getEnergy();
            return;
        }

        // update energy drop of enemy
		eDrop = prevEnergy - lastE.getEnergy();
        // reset previous energy value
        prevEnergy = lastE.getEnergy();
        // check boundaries of firing
        if (eDrop > 0 && eDrop <= 3) {

            // enemy fired a bullet, begin tracking
            incoming = new BulletTracking(eDrop, lastE, new double[]{getX(), getY(), getHeading()}, getTime());

			// output message
            if (output) {
                out.println("Enemy Fired a Bullet");
            }
		
            // select an evasion movement
            strategy = evadeBullet(lastE);
            
            // also log the bullet (make sure that we are only logging after the
            // strategy has been chosen)
            if (CONSTANTS.evasionLog_Enable) {
                evasionLog.startTrackingBullet();
            }

            // disable the mirror behavior
            CONSTANTS.mirrorBehaviorDisable();
        }
    }

    /*
     * Mirrors the Opponent's moves either just laterally or diagonolly while
     * maintaining a minimum distance threshold 'd'. This is done as series of
     * Forces modelled as: F = k1 * (Distance to Opponent - d) d = d_const -
     * (Robbie's Health - Opponent's Health)
     */
    private void mirrorBehavior(ScannedRobotEvent e) {
        double F;
        double d;
        d = CONSTANTS.mirror_distance - (getEnergy() - e.getEnergy());
        F = CONSTANTS.mirror_Force_k1 * (e.getDistance() - d);
        //out.println("d = "+d);//debug
        //out.println("dist to enemy = "+e.getDistance());
        //out.println("F = "+F);
        setTurnRight(e.getBearing());
        setAhead(F);
    }

    /**
     * onHitByBullet: What to do when you're hit by a bullet
     */
    public void onHitByBullet(HitByBulletEvent e) {

        // check if the bullet that hit us is the bullet we were tracking
        if (incoming.checkHit(e.getHeading(), getX(), getY(), getTime())) {
            if (CONSTANTS.evasionLog_Enable) {
                evasionLog.endTrackingBullet(true);
            }
            // output message
            if (output) {
                out.println("		Bullet Being Tracked Hit Us");
            }

            // return to mirroring enemy
            CONSTANTS.mirrorBehaviorEnable();
        }
    }

    /**
     * onHitWall: What to do when you hit a wall
     */
    public void onHitWall(HitWallEvent e) {
	
		if(output)
			out.println("HIT A WALL!!!!!!");
    }

    /**
     * updateBulletTracking: Check to see if the enemy fired / update a bullet
     * being tracked
     *
     */
    public void updateBulletTracking() {

        // we are already tracking a bullet
        if (incoming.getStatus()) {

            // reset previous energy value
            prevEnergy = lastE.getEnergy();
			
            // check if the tracked bullet has missed us
            if (incoming.bulletPassed(getX(), getY(), getTime())) {
                if (CONSTANTS.evasionLog_Enable) {
                    evasionLog.endTrackingBullet(false);
                }
                // output message
                if (output) {
                    out.println("		The Bullet Being Tracked Missed");
                }

                // turn mirror opponent back on	
                CONSTANTS.mirrorBehaviorEnable();
            }
            // we are not currently tracking a bullet	
        } else {
            evadePossibleBullet();
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

    // return last employed strategy
    public String getStrategy() {
        return strategy;
    }

    // return height of environment
    public double getEnvHeight() {
        return envHeight;
    }

    // return width of environment
    public double getEnvWidth() {
        return envWidth;
    }

	// return energy drop of enemy
	public double getEnemyEnergyDrop() {
		return eDrop;
	}

    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        out.println("The round has ended");
        evasionLog.close();
        survLog.endSLog(this.getTime());
    }

    public String[] listEvasionStrategies() {
        return em.possibleMovements();
    }
}
