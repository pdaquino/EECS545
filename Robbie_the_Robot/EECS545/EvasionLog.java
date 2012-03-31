package EECS545;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import robocode.RobocodeFileOutputStream;


/*
 * Logs data for training
 */

public class EvasionLog {    
    private State state;
    private HashMap<String, PrintWriter> outStreams = new HashMap<String, PrintWriter>();
    private boolean hasPrintFeatureList = false;
    
    private String baseFileName = "evasion.log.";
    private String extension = ".txt";
    
    //String[] finalStateArray;
    
    MirroringEvadingRobot rob; //To access the Robocode PrintStream to O/P Error messages
    
    public EvasionLog(MirroringEvadingRobot robot){    
        // open one file for every strategy
        String[] strategies = robot.listEvasionStrategies();
        for (String s : strategies) {
            String fileName = baseFileName + s + extension;

            rob = robot;
            try {
                outStreams.put(s, new PrintWriter(new BufferedWriter(
                        new FileWriter(rob.getDataFile(fileName).getName(),
                        true // append
                        ), 2048)));
                rob.out.println("Opened log file " + fileName);
            } catch (IOException e) {
                rob.out.println("Error in trying to open the log file");
                rob.out.println(e.getMessage());
            }
        }
    }
    
    public void startTrackingBullet(){
        state = new State(rob);
//        if(!hasPrintFeatureList) {
//            writeToFile(formatString(state.getFeatureList()));
//            hasPrintFeatureList = true;
//        }
    }
    
    public void endTrackingBullet(boolean hit){
        //Pass hit/miss data to state object
        state.bulletHit(hit);        
        writeToFile(state.getEvasionStrategy(), formatString());
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
    
    private void writeToFile(String strategy, String str){
        try{
            outStreams.get(strategy).println(str);
        }
        catch(Exception e){
            rob.out.println("Error in trying to write to the log file " + strategy);
            rob.out.println(e.getMessage());            
        }
    }
    
    public void close(){
        try{
            for(PrintWriter out : outStreams.values()) {
                out.close();
            }
        }
        catch(Exception e){
            rob.out.println("Error in trying to close the log file");
            rob.out.println(e.getMessage());
        }
    }
}