package EECS545;

import java.io.IOException;
import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import java.util.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html
public class Smedley extends MirroringTargetingRobot {
	
	Random rand = Utils.getRandom();
	
	SVMPredict predictor;
	
	//Tic time = new Tic();
	
	@Override
	protected void initRobot() {
		
		//SVM
		try {
			predictor = new SVMPredict(getDataFile("/svm_models/targetModel.txt").getAbsolutePath(), getDataFile("/svm_models/targetScale.txt").getAbsolutePath(), "target");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

    @Override
    protected Double targetEnemy() {
		
		// init
		State state = new State(this);
		double bestValue = 0;
		Double bestAngle = 0.0;
		double angle;
		
		// feature values (angle must be appended onto end of this array)
		Double[] features = state.getState();
		
		// run through all angle values
		for(int i = 0; i < 21; i++){
			
			// map angle 
			if(i < 11)
				features[features.length - 1] = new Double(2*(double)i);
			else
				features[features.length - 1] = new Double(-2*((double)i-10));
				
			// test angle	
			double val = predictor.predict(features);
			
			// best SVM prediction thus far
			if(val > bestValue) {
				bestValue = val;
				bestAngle = features[features.length - 1];
			}
		}
		
		if(bestValue > 0.6)
			return bestAngle;
		else
			return null;	
    }
}
		