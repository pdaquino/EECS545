package EECS545;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Logs data for training
 * out.write("aString");
 * out.close();
 */

public class EvasionLog {    
    State state;
    BufferedWriter out;
    
    String fileName;
    
    String[] finalStateArray;
    
    Robbie rob; //To access the Robocode PrintStream to O/P Error messages
    
    public EvasionLog(String fileName, Robbie robot){        
        this.fileName = fileName;
        rob = robot;
        try {
            out = new BufferedWriter(new FileWriter(fileName, true));            
        } 
        catch (IOException e) {
            rob.out.println("Error in trying to open the log file");
            rob.out.println(e.getMessage());            
        }
    }
    
    public void startTrackingBullet(Robbie robot){
        state = new State(robot);
    }
    
    public void endTrackingBullet(boolean hit){
        //Pass hit/miss data to state object
        state.bulletHit(hit);        
        writeToFile(formatString(state));
    }
    
    private String formatString(State finalState){
        finalStateArray = finalState.getState();
        String outputString = "";
        for(int i=0; i<finalStateArray.length; i++){
            outputString = outputString+";"+finalStateArray[i];
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