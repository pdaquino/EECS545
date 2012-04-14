package EECS545.target;

import EECS545.MirroringEvadingRobot;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Saves and reloads learned weights b/w rounds.
 * Weights are saved as:
 *  n    -#of Weights
 *  Weight[0]
 *  .
 *  .
 *  .
 *  Weight[n]
 *  ---  -Indicates EOF as a sanity check
 */
public class WeightsIO {
    List<Weight> weights;
    
    private String baseFileName = "weights.log";
    private String extension = ".txt";
    
    MirroringEvadingRobot rob; //To access the Robocode PrintStream to O/P Error messages
    
    PrintWriter weightLogWrite;
    BufferedReader weightLogRead;
    
    public WeightsIO(MirroringEvadingRobot robot){    
        rob = robot;
    }
    
    public void initWeights(int n){
        List<Weight> temp_wts = new ArrayList<Weight>();
        double default_wt = rob.getConstants().init_RL_weight;
        for(int i=0;i<n;i++){
            temp_wts.add(new Weight(default_wt));
        }
        saveWeights(temp_wts);
    }
    
    public boolean weightFileExists(){
        String fileName = baseFileName + extension;
        try {
            weightLogRead = new BufferedReader(new FileReader(rob.getDataFile(fileName)));
            return true;
        } 
        catch (FileNotFoundException ex) {
            return false;
        }       
    }
    
    public void setWeights(List<Weight> weights){
        this.weights = new ArrayList<Weight>();
        for(Weight wt : weights){
            this.weights.add(new Weight(wt.getWeight()));
        }        
    }
    
    public List getWeights(){
        return weights;
    }
    
    public void saveWeights(List<Weight> weights){
        String fileName = baseFileName + extension;
        try {
            weightLogWrite = new PrintWriter(new BufferedWriter(new FileWriter(
                            rob.getDataFile(fileName).getName(),false), 2048));            
            //^false is used to prevent file append
        } 
        catch (IOException e) {
            rob.out.println("Error in trying to open the weight log file");
            rob.out.println(e.getMessage());
            weightLogWrite = null;
        }
        if(weightLogWrite!=null){
            try{
                weightLogWrite.println(weights.size());
                for(Weight wt : weights){
                    weightLogWrite.println(wt.getWeight());
                }
                weightLogWrite.println("---");
                rob.out.println("Weights saved in weight log file " + fileName);
            }
            catch (Exception e) {
                rob.out.println("Error in trying to access the weights to save to "
                                                                + "the log file");
                rob.out.println(e.getMessage());
            }
        }
    }
    
    public List loadWeights(){
        String fileName = baseFileName + extension;
        String EOF = "---";
        weights = new ArrayList<Weight>();
        try {
            weightLogRead = new BufferedReader(new FileReader(rob.getDataFile(fileName)));
            int size = Integer.parseInt(weightLogRead.readLine());
            for(int i=0;i<size;i++){
                weights.add(new Weight(Double.parseDouble(weightLogRead.readLine())));
            }
            if(EOF.equals(weightLogRead.readLine())){
                return weights;
            }
            else{
                rob.out.println("Weights Log size mismatch error");
                return null;
            }
                
        } 
        catch (Exception ex) {
            rob.out.println("Error in trying to access the weights to read from "
                                                            + "the log file");
            rob.out.println(ex.getMessage());
            return null;
        }
        
        
    }
    
    
}
