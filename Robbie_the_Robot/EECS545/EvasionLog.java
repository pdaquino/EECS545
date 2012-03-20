package EECS545;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Logs data for training
 * out.write("aString");
 * out.close();
 */

public class EvasionLog {    
    State state;
    BufferedWriter out;
    
    String baseFileName = "evasion.log.";
    
    //String[] finalStateArray;
    
    Robbie rob; //To access the Robocode PrintStream to O/P Error messages
    
    public EvasionLog(Robbie robot){        
        String fileName = baseFileName +
                new SimpleDateFormat("MM_dd_HH_mm_ss_S").format(new Date());
        
        rob = robot;
        try {
            out = new BufferedWriter(new FileWriter(fileName, true));            
            rob.out.println("Opened log file " + fileName);
        } 
        catch (IOException e) {
            rob.out.println("Error in trying to open the log file");
            rob.out.println(e.getMessage());            
        }
    }
    
    public void startTrackingBullet(){
        state = new State(rob);
        writeToFile(formatString(state.getFeatureList()));
    }
    
    public void endTrackingBullet(boolean hit){
        //Pass hit/miss data to state object
        state.bulletHit(hit);        
        writeToFile(formatString(state.getState()));
    }
    
    private String formatString(String[] array){
        String outputString = "";
        for(int i=0; i<array.length; i++){
            outputString = outputString+";"+array[i];
        }        
        return outputString;
    }
    
    private void writeToFile(String str){
        try{
            out.write(str);
            out.newLine();
        }
        catch(IOException e){
            rob.out.println("Error in trying to write to the log file");
            rob.out.println(e.getMessage());            
        }
    }
    
    private void close(){
        try{
            out.close();
        }
        catch(IOException e){
            rob.out.println("Error in trying to close the log file");
            rob.out.println(e.getMessage());
        }
    }
}