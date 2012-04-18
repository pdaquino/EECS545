package EECS545.target;

//As the suggests does nothing.

import robocode.ScannedRobotEvent;

public class NOP extends Action{ 
    
    
    public void execute(ScannedRobotEvent e) {
        // nop
    }

    @Override
    protected String makeName() {
        return "NOP";
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public double getAngle() {
        return -1;
    }
    
}
