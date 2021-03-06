package EECS545;

/*
*	Class to hold the various constants we need
*/

public class Constants{

    //Wall Avoidance
    int wallAvoidDistance = 60; 
    int wallAvoid_timeWait = 15; //How much time to wait to see if action was useful
    boolean wallAvoid_Enable = true; // Don't enable for Training
    //Mirroring Movement Constants
    double mirror_distance = 200.0;	//Distance to keep b/w Robbie and Opponent
    double mirror_Force_k1 = 1.5; //Decides b/w just Lateral Inversion and Diagonol Inversion
    public boolean mirror_Behavior_Enable;
    public boolean evasionLog_Enable = true;//Enables/Disables the evasion log - should be true for training
    public boolean survivalLog_Enable = true;//Enables/Disables the survival log
    public boolean mirror_variable_distance = true;
    /*
    *	Sets the mirroring constants
    */
    public void setMirrorConstants(double mirror_distance, double mirror_Force_k1){
            this.mirror_distance = mirror_distance;
            this.mirror_Force_k1 = mirror_Force_k1;
    }

    public void mirrorBehaviorEnable(){
        mirror_Behavior_Enable = true;
    }

    public void mirrorBehaviorDisable(){
        mirror_Behavior_Enable = false;
    }

    public boolean getMirrorBehaviorFlag(){
        return mirror_Behavior_Enable;
    }
    
    public double firePower = 2;//Sets the fire power of our guns
    
    //RL Weights Constants
    public double init_RL_weight = 0;//Default initializing weight for the RL-shooter
    public int no_of_RL_weights = 10;
}