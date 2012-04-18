package EECS545;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 * State - a class for tracking state when a bullet is fired
 */
public class State {
    private String evasionStrategy;
    private boolean bulletHit;

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
        RobotBearingCenter,
        DistanceToOpp,
        RobotHealth,
        OppHealthDrop,
		AngleFired
        //BulletHit,
        //StrategyEmployed,
        //OppName
    }
    private EnumMap<Feature, Double> features = new EnumMap<Feature, Double>(Feature.class);
    private AdvancedRobot robot;

    protected final void addFeature(Feature f, double value) {
        if (features.put(f, value) != null) {
            robot.out.println("Warning: feature " + f + " is being overwritten");
        } else {
            //robot.out.println("Feature " + f + "=" + value + " added successfully");
        }
    }

    public Double getFeatureValue(Feature f) {
        return features.get(f);
    }

    // constructor for evading robot
    public State(MirroringEvadingRobot robot) {
        this.robot = robot;
        // last scanned event
        ScannedRobotEvent e = robot.getLastE();

        // calculate robot's location
        double robotX = robot.getX();
        double robotY = robot.getY();
        double robotT = -1 * Utils.normalRelativeAngleDegrees(robot.getHeading()-90);
        addFeature(Feature.RobotGlobalPoseX, robotX);
        addFeature(Feature.RobotGlobalPoseY, robotY);
        addFeature(Feature.RobotGlobalPoseT, robotT);

        // calculate robot's velocity
        double robotVX = robot.getVelocity() * Math.cos(Math.toRadians(robotT));
        double robotVY = robot.getVelocity() * Math.sin(Math.toRadians(robotT));
        double robotVT = -1 * ((Utils.normalRelativeAngleDegrees(robot.getHeading() - robot.getLastRobotHeading())) / 2);
        addFeature(Feature.RobotGlobalVelX, robotVX);
        addFeature(Feature.RobotGlobalVelY, robotVY);
        addFeature(Feature.RobotGlobalVelT, robotVT);

        // calculate enemy's location
        double angle = Utils.normalRelativeAngleDegrees(robot.getHeading() + e.getBearing());
        double opponentX = robotX + e.getDistance() * Math.sin(Math.toRadians(angle));
        double opponentY = robotY + e.getDistance() * Math.cos(Math.toRadians(angle));
		double opponentT = -1 * Utils.normalRelativeAngleDegrees(e.getHeading()-90);
        addFeature(Feature.OppGlobalPoseX, opponentX);
        addFeature(Feature.OppGlobalPoseY, opponentY);
        addFeature(Feature.OppGlobalPoseT, opponentT);

        // calculate enemy's velocity
        double opponentVX = e.getVelocity() * Math.cos(Math.toRadians(opponentT));
        double opponentVY = e.getVelocity() * Math.sin(Math.toRadians(opponentT));
		double opponentVT = -1 * ((Utils.normalRelativeAngleDegrees(e.getHeading() - robot.getLastEnemyHeading())) / 2);
        addFeature(Feature.OppGlobalVelX, opponentVX);
        addFeature(Feature.OppGlobalVelY, opponentVY);
        addFeature(Feature.OppGlobalVelT, opponentVT);

        // opponent relative location
        angle = -1 * e.getBearing();
        double opponentRelativeX = e.getDistance() * Math.cos(Math.toRadians(angle));
        double opponentRelativeY = e.getDistance() * Math.sin(Math.toRadians(angle));
        double opponentRelativeT = Utils.normalRelativeAngleDegrees(opponentT - robotT);
        addFeature(Feature.OppRelPoseX, opponentRelativeX);
        addFeature(Feature.OppRelPoseY, opponentRelativeY);
        addFeature(Feature.OppRelPoseT, opponentRelativeT);

        // opponent relative velocity
        double opponentRelativeVX = opponentVX - robotVX;
        double opponentRelativeVY = opponentVY - robotVY;
        double opponentRelativeVT = opponentVT - robotVT;
        addFeature(Feature.OppRelVelX, opponentRelativeVX);
        addFeature(Feature.OppRelVelY, opponentRelativeVY);
        addFeature(Feature.OppRelVelT, opponentRelativeVT);

        // distance and relative bearing to room center
        double distanceToCenter = Math.sqrt(Math.pow(robotX - (robot.getEnvWidth() / 2), 2) + Math.pow(robotY - (robot.getEnvHeight() / 2), 2));
        double bearingToCenter = Math.toDegrees(Math.atan2((robot.getEnvHeight() / 2) - robotY, (robot.getEnvWidth() / 2) - robotX));
        addFeature(Feature.RobotDistCenter, distanceToCenter);
        addFeature(Feature.RobotBearingCenter, bearingToCenter);

        // distance to opponent
        addFeature(Feature.DistanceToOpp, e.getDistance());

        // health values
        addFeature(Feature.RobotHealth, robot.getEnergy());
        addFeature(Feature.OppHealthDrop, robot.getEnemyEnergyDrop());

        // strategy employed
        //addFeature(Feature.StrategyEmployed, robot.getStrategy());
        evasionStrategy = robot.getStrategy();

        // opponent name
        //addFeature(Feature.OppName, e.getName());
    }

    // constructor
    public State(MirroringTargetingRobot robot) {
        this.robot = robot;
        // last scanned event
        ScannedRobotEvent e = robot.getLastE();

        // calculate robot's location
        double robotX = robot.getX();
        double robotY = robot.getY();
        double robotT = -1 * Utils.normalRelativeAngleDegrees(robot.getHeading()-90);
        addFeature(Feature.RobotGlobalPoseX, robotX);
        addFeature(Feature.RobotGlobalPoseY, robotY);
        addFeature(Feature.RobotGlobalPoseT, robotT);

        // calculate robot's velocity
        double robotVX = robot.getVelocity() * Math.cos(Math.toRadians(robotT));
        double robotVY = robot.getVelocity() * Math.sin(Math.toRadians(robotT));
        double robotVT = -1 * ((Utils.normalRelativeAngleDegrees(robot.getHeading() - robot.getLastRobotHeading())) / 2);
        addFeature(Feature.RobotGlobalVelX, robotVX);
        addFeature(Feature.RobotGlobalVelY, robotVY);
        addFeature(Feature.RobotGlobalVelT, robotVT);

        // calculate enemy's location
        double angle = Utils.normalRelativeAngleDegrees(robot.getHeading() + e.getBearing());
        double opponentX = robotX + e.getDistance() * Math.sin(Math.toRadians(angle));
        double opponentY = robotY + e.getDistance() * Math.cos(Math.toRadians(angle));
		double opponentT = -1 * Utils.normalRelativeAngleDegrees(e.getHeading()-90);
        addFeature(Feature.OppGlobalPoseX, opponentX);
        addFeature(Feature.OppGlobalPoseY, opponentY);
        addFeature(Feature.OppGlobalPoseT, opponentT);

        // calculate enemy's velocity
        double opponentVX = e.getVelocity() * Math.cos(Math.toRadians(opponentT));
        double opponentVY = e.getVelocity() * Math.sin(Math.toRadians(opponentT));
		double opponentVT = -1 * ((Utils.normalRelativeAngleDegrees(e.getHeading() - robot.getLastEnemyHeading())) / 2);
        addFeature(Feature.OppGlobalVelX, opponentVX);
        addFeature(Feature.OppGlobalVelY, opponentVY);
        addFeature(Feature.OppGlobalVelT, opponentVT);

        // opponent relative location
        angle = -1 * e.getBearing();
        double opponentRelativeX = e.getDistance() * Math.cos(Math.toRadians(angle));
        double opponentRelativeY = e.getDistance() * Math.sin(Math.toRadians(angle));
        double opponentRelativeT = Utils.normalRelativeAngleDegrees(opponentT - robotT);
        addFeature(Feature.OppRelPoseX, opponentRelativeX);
        addFeature(Feature.OppRelPoseY, opponentRelativeY);
        addFeature(Feature.OppRelPoseT, opponentRelativeT);

        // opponent relative velocity
        double opponentRelativeVX = opponentVX - robotVX;
        double opponentRelativeVY = opponentVY - robotVY;
        double opponentRelativeVT = opponentVT - robotVT;
        addFeature(Feature.OppRelVelX, opponentRelativeVX);
        addFeature(Feature.OppRelVelY, opponentRelativeVY);
        addFeature(Feature.OppRelVelT, opponentRelativeVT);

        // distance and relative bearing to room center
        double distanceToCenter = Math.sqrt(Math.pow(robotX - (robot.getEnvWidth() / 2), 2) + Math.pow(robotY - (robot.getEnvHeight() / 2), 2));
        double bearingToCenter = Math.toDegrees(Math.atan2((robot.getEnvHeight() / 2) - robotY, (robot.getEnvWidth() / 2) - robotX));
        addFeature(Feature.RobotDistCenter, distanceToCenter);
        addFeature(Feature.RobotBearingCenter, bearingToCenter);

        // distance to opponent
        addFeature(Feature.DistanceToOpp, e.getDistance());

        // health values
        addFeature(Feature.RobotHealth, robot.getEnergy());
        addFeature(Feature.OppHealthDrop, e.getEnergy());

        // angle fired
        addFeature(Feature.AngleFired, robot.getAngleFired());
    }

    // return state to write to file
    public Double[] getState() {
        return features.values().toArray(new Double[0]);
    }

    // returns a list with all the feature names, in the same order
    // as getState()
    public String[] getFeatureNames() {
        Feature[] fs = Feature.values();
        ArrayList<String> names = new ArrayList<String>();
        for (Feature f : fs) {
            names.add(f.toString());
        }
        return names.toArray(new String[0]);
    }
    
    public String getEvasionStrategy() {
        return evasionStrategy;
    }
    
    public boolean getBulletHit() {
        return bulletHit;
    }

    // set the result of the bullet
    public void bulletHit(boolean hit) {
        bulletHit = hit;
    }
}
