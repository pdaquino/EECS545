package EECS545;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import robocode.RobocodeFileOutputStream;


/*
 * Logs data for training
 */

public class EvasionLog {    
    private State state;
    private HashMap<String, PrintStream> outStreams = new HashMap<String, PrintStream>();
    private boolean hasPrintFeatureList = false;
    
    private String baseFileName = "evasion.log.";
    private String extension = ".txt";
    
    //String[] finalStateArray;
    
    Robbie rob; //To access the Robocode PrintStream to O/P Error messages
    
    public EvasionLog(Robbie robot){    
        // open one file for every strategy
        String[] strategies = robot.listEvasionStrategies();
        for (String s : strategies) {
            String fileName = baseFileName + s + extension;

            rob = robot;
            try {
                outStreams.put(s, new PrintStream(
                        new RobocodeFileOutputStream(rob.getDataFile(fileName).getName(),
                        true // append
                        )));
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
        writeToFile(state.getEvasionStrategy(), formatString(state.getState()));
    }
    
    private String formatString(String[] array){
        // from http://stackoverflow.com/questions/794248/a-method-to-reverse-effect-of-java-string-split/6116469#6116469
        return Arrays.toString(array).replace(", ", ";").replaceAll("[\\[\\]]", "");
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
            for(PrintStream out : outStreams.values()) {
                out.close();
            }
        }
        catch(Exception e){
            rob.out.println("Error in trying to close the log file");
            rob.out.println(e.getMessage());
        }
    }
}