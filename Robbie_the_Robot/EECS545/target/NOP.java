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
    
}
