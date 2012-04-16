package EECS545.target;

import EECS545.MirroringEvadingRobot;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    private String fileName = "weights.log.txt";
    
    MirroringEvadingRobot rob; //To access the Robocode PrintStream to O/P Error messages
    
    public WeightsIO(MirroringEvadingRobot robot){    
        rob = robot;
    }
    
//    public void initWeights(int n){
//        List<Weight> temp_wts = new ArrayList<Weight>();
//        double default_wt = rob.getConstants().init_RL_weight;
//        for(int i=0;i<n;i++){
//            temp_wts.add(new Weight(default_wt));
//        }
//        saveWeights(temp_wts);
//    }
    
    public boolean weightFileExists(){
        return new File(fileName).exists();
    }
    
//    public void setWeights(List<Weight> weights){
//        this.weights = new ArrayList<Weight>();
//        for(Weight wt : weights){
//            this.weights.add(new Weight(wt.getWeight()));
//        }        
//    }
    
//    public List getWeights(){
//        return weights;
//    }
    
    public void saveWeights(List<WeightVector> weights){
        PrintWriter weightLogWrite = null;
        try {
            weightLogWrite = new PrintWriter(new BufferedWriter(new FileWriter(
                            fileName,false), 2048));            
            //^false is used to prevent file append
            weightLogWrite.println(weights.size());
            for (WeightVector wt : weights) {
                weightLogWrite.println(wt.toString());
            }
            rob.out.println("Weights saved in weight log file " + fileName);
        } catch (Exception e) {
            rob.out.println("Error in trying to access the weights to save to "
                    + "the log file");
            rob.out.println(e.getMessage());
        } finally {
            if(weightLogWrite != null)
                weightLogWrite.close();
        }
    }

    public List<WeightVector> loadWeights(){
        Scanner weightLogRead = null;
        List<WeightVector> weights = null;
        Output.println("Reading weights from " + fileName);
        try {
            weightLogRead = new Scanner(new File(fileName));
            int size = weightLogRead.nextInt();
            weightLogRead.next();
            weights = new ArrayList<WeightVector>(size);
            for(int i=0;i<size;i++){   
                weights.add(WeightVector.fromString(weightLogRead.nextLine()));
            }   
        }
        catch (Exception ex) {
            rob.out.println("Error in trying to access the weights to read from "
                                                            + "the log file");
            rob.out.println(ex.toString());
            ex.printStackTrace();
            
            return null;
        } finally {
            if(weightLogRead != null)
                weightLogRead.close();
        }
        return weights;        
    }
    
}
