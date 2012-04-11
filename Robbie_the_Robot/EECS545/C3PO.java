package EECS545;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import robocode.ScannedRobotEvent;


// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html
public class C3PO extends MirroringEvadingRobot {

    // State object to grab features
    private List<SVMPredict> predictors = new ArrayList<SVMPredict>();

    @Override
    protected void initRobot() {
        // SVMs
        try {
            predictors.add(new SVMPredict(getDataFile("/svm_models/leftModel.txt").getAbsolutePath(), getDataFile("/svm_models/leftScale.txt").getAbsolutePath(), "evadeLeft"));
            predictors.add(new SVMPredict(getDataFile("/svm_models/rightModel.txt").getAbsolutePath(), getDataFile("/svm_models/rightScale.txt").getAbsolutePath(), "evadeRight"));
            predictors.add(new SVMPredict(getDataFile("/svm_models/haltModel.txt").getAbsolutePath(), getDataFile("/svm_models/haltScale.txt").getAbsolutePath(), "halt"));
            predictors.add(new SVMPredict(getDataFile("/svm_models/feignModel.txt").getAbsolutePath(), getDataFile("/svm_models/feignScale.txt").getAbsolutePath(), "feign"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String evadeBullet(ScannedRobotEvent e) {
	
		State state = new State(this);
		double bestValue = 0;
		String bestStrategy = new String();
		
		for(SVMPredict predictor:predictors){

			double val = predictor.predict(state.getState());
			
			if(val > bestValue){
				bestValue = val;
				bestStrategy = predictor.getStrategy();
			}
		}
	
        // output what strategy was chosen
		if(bestValue <=0){
			em.evade("random", lastE);
			return "random";
		}
	
        // execute chosen evasion strategy
        em.evade(bestStrategy, lastE);
		
		return bestStrategy;
    }
}							