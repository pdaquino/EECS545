package EECS545;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import robocode.RobocodeFileOutputStream;


/*
 * Logs data for training
 */

public class EvasionLog {    
    private State state;
    private PrintStream out;
    private boolean hasPrintFeatureList = false;
    
    private String baseFileName = "evasion.log.";
    private String extension = ".txt";
    
    //String[] finalStateArray;
    
    Robbie rob; //To access the Robocode PrintStream to O/P Error messages
    
    public EvasionLog(Robbie robot){        
        String fileName = baseFileName +
                new SimpleDateFormat("MM_dd_HH_mm_ss_S").format(new Date()) +
                extension;
        
        rob = robot;
        try {
            out = new PrintStream(new RobocodeFileOutputStream(rob.getDataFile(fileName)));            
            rob.out.println("Opened log file " + fileName);
        } 
        catch (IOException e) {
            rob.out.println("Error in trying to open the log file");
            rob.out.println(e.getMessage());            
        }
    }
    
    public void startTrackingBullet(){
        state = new State(rob);
        if(!hasPrintFeatureList) {
            writeToFile(formatString(state.getFeatureList()));
            hasPrintFeatureList = true;
        }
    }
    
    public void endTrackingBullet(boolean hit){
        //Pass hit/miss data to state object
        state.bulletHit(hit);        
        writeToFile(formatString(state.getState()));
    }
    
    private String formatString(String[] array){
        // from http://stackoverflow.com/questions/794248/a-method-to-reverse-effect-of-java-string-split/6116469#6116469
        return Arrays.toString(array).replace(", ", ";").replaceAll("[\\[\\]]", "");
    }
    
    private void writeToFile(String str){
        try{
            out.println(str);
        }
        catch(Exception e){
            rob.out.println("Error in trying to write to the log file");
            rob.out.println(e.getMessage());            
        }
    }
    
    public void close(){
        try{
            out.close();
        }
        catch(Exception e){
            rob.out.println("Error in trying to close the log file");
            rob.out.println(e.getMessage());
        }
    }
}