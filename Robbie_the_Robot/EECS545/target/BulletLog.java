package EECS545.target;

import EECS545.MirroringEvadingRobot;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Holds a hash-map of Q-values and bullet-names of the shots that were fired 
 * to determine accuracy for different values of Q 
 */
public class BulletLog {    
    private HashMap<String, Double> bulletMap;   
    private String baseFileName = "bullet.log";
    private String extension = ".txt";
    PrintWriter bLog;
    MirroringEvadingRobot rob; //To access the Robocode PrintStream to O/P Error messages
    
    public BulletLog(MirroringEvadingRobot robot){            
        String fileName = baseFileName+extension;
        bulletMap = new HashMap<String, Double>();
        String[] strategies = robot.listEvasionStrategies();
        rob = robot;
        try {
            bLog = new PrintWriter(new BufferedWriter(new FileWriter(
                            rob.getDataFile(fileName).getName(),true), 2048));
            rob.out.println("Opened survival log file " + fileName);
        } 
        catch (IOException e) {
            rob.out.println("Error in trying to open the bullet log file");
            rob.out.println(e.getMessage());
        }               
    }
     
    //Closes the stream
    public void close(){
        try{
            bLog.close();
        }
        catch(Exception e){
            rob.out.println("Error in trying to close the bullet log file");
            rob.out.println(e.getMessage());
        }
    }
    
    //Adds a bullet-Qvalue mapping to bulletMap
    public void addBullet(String bulletName, double Qval){
        bulletMap.put(bulletName, Qval);
    }    
    
    
    /*
     * Checks if bulletName is mapped to a Q-Value, if so it removes the mapping
     * and writes the result of that bullet (hit = 1 and miss = 0) to the log file
     */    
    public void bulletResult(String bulletName, boolean hitMiss){
        try{
            Double QValue = bulletMap.remove(bulletName).doubleValue();
            if(!Double.isNaN(QValue)){
                String hit, str;
                if(hitMiss)
                    hit = ";1;";
                else
                    hit = ";0;";
                str = roundTwoDecimals(QValue.doubleValue())+ hit;
                writeToLog(str);
            }
        }
        catch(Exception e){
            
        }
    }
    
    //Writes to the log file
    public void writeToLog(String str){
        try{
            bLog.println(str);
        }
        catch(Exception e){
            rob.out.println("Error in trying to write to the bullet log file");
            rob.out.println(e.getMessage());
        }
    }
    
    //Used to round the Q value to 2 decimal places. This is done to dicretize it
    private double roundTwoDecimals(double d) {
           DecimalFormat twoDForm = new DecimalFormat("#.##");
       return Double.valueOf(twoDForm.format(d));
    }
    
}
