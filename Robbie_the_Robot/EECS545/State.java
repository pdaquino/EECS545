package EECS545;

import java.util.ArrayList;
import java.util.HashMap;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 * State - a class for tracking state when a bullet is fired
 */
public class State
{
    public enum Feature {
        RobotGlobalPoseX,
        RobotGlobalPoseY,
        RobotGlobalPoseT,
        RobotGlobalVelX,
        RobotGlobalVelY,
        RobotGlobalVelT, 
        OppGlobalPoseX,
        OppGlobalPoseY,
        OppGlobalPoseT,
        OppGlobalVelX,
        OppGlobalVelY,
        OppGlobalVelT,
        OppRelPoseX,
        OppRelPoseY,
        OppRelPoseT,
        OppRelVelX,
        OppRelVelY,
        OppRelVelT,
        RobotDistCenter,
        RobotRelBearingCenter,
        DistanceToOpp,
        RobotHealth,
        OppHealth,
        BulletHit,
        StrategyEmployed,
        OppName
    }
    
    private HashMap<Feature, String> features;
    
    protected final void addFeature(Feature f, double value) {
        addFeature(f, Double.toString(value));
    }
    
    protected final void addFeature(Feature f, String value) {
        try{
            if( features.put(f, value) != null)  {
                //System.err.println("Warning: feature " + f + " is being overwritten");
                /*^ can't access System.err.. if you want to really debug this 
                 * with a text O/P you'll have to pass a Robbie-object and use:
                 *      rob_obj.out.println("......")
                 */
            }
        }
        catch(Exception e){
            
        }
    }
    
    public String getFeature(Feature f) {
        return features.get(f);
    }
	
	// robot x, y, theta location globally
//	double robotX;
//	double robotY;
//	double robotT;
//	
//	// robot velocity in x, y, and theta globablly
//	double robotVX;
//	double robotVY;
//	double robotVT;
//	
//	// opponent x, y, theta location globally
//    double opponentX;
//    double opponentY;
//    double opponentT;
//	
//	// opponent velocity in x, y, and theta globally
//   	double opponentVX;
//	double opponentVY;
//	double opponentVT;
//	
//	// opponent relative location
//	double opponentRelativeX;
//	double opponentRelativeY;
//	double opponentRelativeT;
//
//	// opponent relative velocity
//	double opponentRelativeVX;
//	double opponentRelativeVY;
//	double opponentRelativeVT;
//	
//	// distance and relative bearing to room center
//	double distanceToCenter;
//	double relativeBearingToCenter;
//	
//	// distance to opponent
//	double distanceToOpponent;
//	
//	// health values
//	double robotHealth;
//	double opponentHealth;
//	
//	// hit by bullet
//	double hit;
//	
//	// strategy employed
//	String strategy;
//	
//	// opponent name
//	String opponent;
	
	// constructor
	public State(Robbie robot) {
		
		// last scanned event
		ScannedRobotEvent e = robot.getLastE();
	
		// calculate robot's location
		double robotX = robot.getX();
                double robotY = robot.getY();
		double robotT = robot.getHeading();
                addFeature(Feature.RobotGlobalPoseX, robotX);
                addFeature(Feature.RobotGlobalPoseY, robotY);
                addFeature(Feature.RobotGlobalPoseT, robotT);
		
		// calculate robot's velocity
		double angle = -1*Utils.normalRelativeAngleDegrees(robotT-90);
		double robotVX = robot.getVelocity()*Math.cos(Math.toRadians(angle));
		double robotVY = robot.getVelocity()*Math.sin(Math.toRadians(angle));
		double robotVT = (Utils.normalRelativeAngleDegrees(robotT-robot.getLastRobotHeading()))/2;
                addFeature(Feature.RobotGlobalVelX, robotVX);
                addFeature(Feature.RobotGlobalVelY, robotVY);
                addFeature(Feature.RobotGlobalVelT, robotVT);
                		
		// calculate enemy's location
		angle = Utils.normalRelativeAngleDegrees(robotT + e.getBearing());
		double opponentX = robotX + e.getDistance()*Math.sin(Math.toRadians(angle));
		double opponentY = robotY + e.getDistance()*Math.cos(Math.toRadians(angle));
		double opponentT = e.getHeading();
                addFeature(Feature.OppGlobalPoseX, opponentX);
                addFeature(Feature.OppGlobalPoseY, opponentY);
                addFeature(Feature.OppGlobalPoseT, opponentT);
		
		// calculate enemy's velocity
		angle = -1*Utils.normalRelativeAngleDegrees(opponentT-90);
		double opponentVX = e.getVelocity()*Math.cos(Math.toRadians(angle));
		double opponentVY = e.getVelocity()*Math.sin(Math.toRadians(angle));
		double opponentVT = (Utils.normalRelativeAngleDegrees(opponentT-robot.getLastEnemyHeading()))/2;
                addFeature(Feature.OppGlobalVelX, opponentVX);
                addFeature(Feature.OppGlobalVelY, opponentVY);
                addFeature(Feature.OppGlobalVelT, opponentVT);
		
		// opponent relative location
		angle = -1*e.getBearing();
		double opponentRelativeX = e.getDistance()*Math.cos(Math.toRadians(angle));
		double opponentRelativeY = e.getDistance()*Math.sin(Math.toRadians(angle));
		double opponentRelativeT = Utils.normalRelativeAngleDegrees(robotT-opponentT);
                addFeature(Feature.OppRelPoseX, opponentRelativeX);
                addFeature(Feature.OppRelPoseY, opponentRelativeY);
                addFeature(Feature.OppRelPoseT, opponentRelativeT);

		// opponent relative velocity
		angle = Utils.normalRelativeAngleDegrees(robotT-robot.getLastEnemyHeading());
		double opponentRelativeVX = e.getVelocity()*Math.cos(Math.toRadians(opponentRelativeT));
		double opponentRelativeVY = e.getVelocity()*Math.sin(Math.toRadians(opponentRelativeT));
		double opponentRelativeVT = (Utils.normalRelativeAngleDegrees(opponentRelativeT - angle))/2;	
                addFeature(Feature.OppRelVelX, opponentRelativeVX);
                addFeature(Feature.OppRelVelY, opponentRelativeVY);
                addFeature(Feature.OppRelVelT, opponentRelativeVT);
		
		// distance and relative bearing to room center
		double distanceToCenter = Math.sqrt(Math.pow(robotX-(robot.getEnvWidth()/2),2) + Math.pow(robotY-(robot.getEnvHeight()/2),2));
		double relativeBearingToCenter = Math.atan2((robot.getEnvHeight()/2)-robotY, (robot.getEnvWidth()/2) - robotX);
                addFeature(Feature.RobotDistCenter, distanceToCenter);
                addFeature(Feature.RobotRelBearingCenter, relativeBearingToCenter);
	
		// distance to opponent
		addFeature(Feature.DistanceToOpp, e.getDistance());
	
		// health values
		addFeature(Feature.RobotHealth, robot.getEnergy());
		addFeature(Feature.OppHealth, e.getEnergy());
	
		// strategy employed
		addFeature(Feature.StrategyEmployed, robot.getStrategy());
	
		// opponent name
		addFeature(Feature.OppName, e.getName());
	}

	// return state to write to file
	public String[] getState() {
            return (String[]) features.values().toArray();
	}
        
        // returns a list with all the feature names, in the same order
        // as getState()
        public String[] getFeatureList() {
            Feature[] fs = Feature.values();
            ArrayList<String> names = new ArrayList<String>();
            for(Feature f : fs) {
                names.add(f.toString());
            }
            return names.toArray(new String[0]);
        }

	// set the result of the bullet
	public void bulletHit(boolean hit){
		addFeature(Feature.BulletHit, hit ? 1 : -1);
	}
        
}

