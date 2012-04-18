package EECS545;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import robocode.RobocodeFileOutputStream;

public class TargetLog {    
    private State state;
	PrintWriter tLog;
    private String fileName = "targeting.log.txt";
    MirroringTargetingRobot rob;
    
    public TargetLog(MirroringTargetingRobot robot){    

    	rob = robot;
        try {
            tLog = new PrintWriter(new BufferedWriter(new FileWriter(
                            rob.getDataFile(fileName).getName(),true), 2048));
            rob.out.println("Opened targeting log file " + fileName);
        } 
        catch (IOException e) {
            rob.out.println("Error in trying to open the targeting log file");
            rob.out.println(e.getMessage());
        }
    }
    
    public void startTargetBullet(){
        state = new State(rob);
    }
    
    public void endTargetBullet(boolean hit){
			
        //Pass hit/miss data to state object
        state.bulletHit(hit);        
        writeToFile(formatString());
    }
    
    private String formatString(){
        // libsvm's format is
        // label feature_id:feature_value feature_id:feature_value ...
        // where
        //  label_feature = 1 (miss) or -1 (hit)
        //  feature_value = unique id for the feature
        StringBuilder sb = new StringBuilder();
        sb.append(state.getBulletHit() ? -1 : 1).append(' ');
        Double[] featureValues = state.getState();
        for(int i = 0; i < featureValues.length; i++) {
            // making the feature_id 1-based just to be safe (this is how
            // the example files I have look like)
            sb.append(i+1).append(':').append(featureValues[i]).append(' ');
        }
        return sb.toString();
    }
    
    private void writeToFile(String str){
        try{
            tLog.println(str);
        }
        catch(Exception e){
            rob.out.println("Error in trying to write to the log file.");
            rob.out.println(e.getMessage());            
        }
    }
    
    public void close(){
        try{
            tLog.close();
        }
        catch(Exception e){
            rob.out.println("Error in trying to close the log file");
            rob.out.println(e.getMessage());
        }
    }
}