package EECS545.target;

import robocode.ScannedRobotEvent;

/**
 *
 * @author Shiva
 */
public interface Action {
    public void execute(ScannedRobotEvent e);
    
    public String getName();
}
