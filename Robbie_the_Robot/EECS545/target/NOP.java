package EECS545.target;

//As the suggests does nothing.

import robocode.ScannedRobotEvent;

public class NOP implements Action{
    String name = "No Action Gun";    
    
    
    public void execute(ScannedRobotEvent e) {
        // nop
    }

    public String getName() {
        return name;
    }
    
}
