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
            predictors.add(new SVMPredict("/svm_models/leftModel.txt", "/svm_models/leftScale.txt", "evadeLeft"));
            predictors.add(new SVMPredict("/svm_models/rightModel.txt", "/svm_models/rightScale.txt", "evadeRight"));
            predictors.add(new SVMPredict("/svm_models/haltModel.txt", "/svm_models/haltScale.txt", "halt"));
            predictors.add(new SVMPredict("/svm_models/feignModel.txt", "/svm_models/feignScale.txt", "feign"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String evadeBullet(ScannedRobotEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}							