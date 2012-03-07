package EECS545;

/*
*	Class to hold the various constants we need
*/

public class Constants{
	//Mirroring Movement Constants
	double mirror_distance = 20.0;	//Distance to keep b/w Robbie and Opponent
	boolean mirror_diag_invert = true; //Decides b/w just Lateral Inversion and Diagonol Inversion
	
	/*
	*	Sets the mirroring constants
	*/
	public void setMirrorConstants(double mirror_distance, boolean mirror_diag_invert){
		this.mirror_distance = mirror_distance;
		this.mirror_diag_invert = mirror_diag_invert;
	}

}