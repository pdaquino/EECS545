package EECS545;

import robocode.*;
import robocode.util.Utils;

/**
 * State - a class for tracking state when a bullet is fired
 */
public class State
{
	
	// robot x, y, theta location globally
	double robotX;
	double robotY;
	double robotT;
	
	// robot velocity in x, y, and theta globablly
	double robotVX;
	double robotVY;
	double robotVT;
	
	// opponent x, y, theta location globally
        double opponentX;
        double opponentY;
        double opponentT;
	
	// opponent velocity in x, y, and theta globally
   	double opponentVX;
	double opponentVY;
	double opponentVT;
	
	// opponent relative location
	double opponentRelativeX;
	double opponentRelativeY;
	double opponentRelativeT;

	// opponent relative velocity
	double opponentRelativeVX;
	double opponentRelativeVY;
	double opponentRelativeVT;
	
	// distance and relative bearing to room center
	double distanceToCenter;
	double relativeBearingToCenter;
	
	// distance to opponent
	double distanceToOpponent;
	
	// health values
	double robotHealth;
	double opponentHealth;
	
	// hit by bullet
	double hit;
	
	// strategy employed
	String strategy;
	
	// opponent name
	String opponent;
	
	// constructor
	public State(Robbie robot) {
		
		// last scanned event
		ScannedRobotEvent e = robot.getLastE();
	
		// calculate robot's location
		robotX = robot.getX();
		robotY = robot.getY();
		robotT = robot.getHeading();
		
		// calculate robot's velocity
		double angle = -1*Utils.normalRelativeAngleDegrees(robotT-90);
		robotVX = robot.getVelocity()*Math.cos(Math.toRadians(angle));
		robotVY = robot.getVelocity()*Math.sin(Math.toRadians(angle));
		robotVT = (Utils.normalRelativeAngleDegrees(robotT-robot.getLastRobotHeading()))/2;
		
		// calculate enemy's location
		angle = Utils.normalRelativeAngleDegrees(robotT + e.getBearing());
		opponentX = robotX + e.getDistance()*Math.sin(Math.toRadians(angle));
		opponentY = robotY + e.getDistance()*Math.cos(Math.toRadians(angle));
		opponentT = e.getHeading();
		
		// calculate enemy's velocity
		angle = -1*Utils.normalRelativeAngleDegrees(opponentT-90);
		opponentVX = e.getVelocity()*Math.cos(Math.toRadians(angle));
		opponentVY = e.getVelocity()*Math.sin(Math.toRadians(angle));
		opponentVT = (Utils.normalRelativeAngleDegrees(opponentT-robot.getLastEnemyHeading()))/2;
		
		// opponent relative location
		angle = -1*e.getBearing();
		opponentRelativeX = e.getDistance()*Math.cos(Math.toRadians(angle));
		opponentRelativeY = e.getDistance()*Math.sin(Math.toRadians(angle));
		opponentRelativeT = Utils.normalRelativeAngleDegrees(robotT-opponentT);

		// opponent relative velocity
		angle = Utils.normalRelativeAngleDegrees(robotT-robot.getLastEnemyHeading());
		opponentRelativeVX = e.getVelocity()*Math.cos(Math.toRadians(opponentRelativeT));
		opponentRelativeVY = e.getVelocity()*Math.sin(Math.toRadians(opponentRelativeT));
		opponentRelativeVT = (Utils.normalRelativeAngleDegrees(opponentRelativeT - angle))/2;	
		
		// distance and relative bearing to room center
		distanceToCenter = Math.sqrt(Math.pow(robotX-(robot.getEnvWidth()/2),2) + Math.pow(robotY-(robot.getEnvHeight()/2),2));
		relativeBearingToCenter = Math.atan2((robot.getEnvHeight()/2)-robotY, (robot.getEnvWidth()/2) - robotX);
	
		// distance to opponent
		distanceToOpponent = e.getDistance();
	
		// health values
		robotHealth = robot.getEnergy();
		opponentHealth = e.getEnergy();
	
		// strategy employed
		strategy = robot.getStrategy();
	
		// opponent name
		opponent = e.getName();
	}

	// return state to write to file
	public String[] getState() {
		return null;
	}

	// set the result of the bullet
	public void bulletHit(boolean result){
		if(result)
			hit = 1;
		else
			hit = -1;
	}
	
}
