package EECS545;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *  To track how long the robot survives each round
 * 
 */
public class SurvivalLog {
    
    private String baseFileName = "survival.log";
    private String extension = ".txt";
    
    long survivalTime;
    
    MirroringEvadingRobot rob; //To access the Robocode PrintStream to O/P Error messages
    
    PrintWriter sLog;
    
    public SurvivalLog(MirroringEvadingRobot robot){    
        survivalTime=0;
        String fileName = baseFileName + extension;

        rob = robot;
        try {
            sLog = new PrintWriter(new BufferedWriter(new FileWriter(
                            rob.getDataFile(fileName).getName(),true), 2048));
            rob.out.println("Opened survival log file " + fileName);
        } 
        catch (IOException e) {
            rob.out.println("Error in trying to open the survival log file");
            rob.out.println(e.getMessage());
        }
        startSLog((long)robot.getRoundNum());
        startSLog(robot.getTime());

    }
    
    private void startSLog(long time){
        survivalTime = time;
        try{
            sLog.print(time+";");
        }
        catch(Exception e){
            rob.out.println("Error in trying to write to the survival log file");
            rob.out.println(e.getMessage());            
        }
    }
    
    public void endSLog(long time){
        survivalTime = time - survivalTime;
        try{
            sLog.println(time+";"+survivalTime+";");
        }
        catch(Exception e){
            rob.out.println("Error in trying to write to the survival log file");
            rob.out.println(e.getMessage());            
        }
        close();
    }
    
    public void close(){
        try{
            sLog.close();
        }
        catch(Exception e){
            rob.out.println("Error in trying to close the survival log file");
            rob.out.println(e.getMessage());
        }
    }
    
}
