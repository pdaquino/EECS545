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
       
}