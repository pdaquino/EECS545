package EECS545.target;

import robocode.ScannedRobotEvent;

/**
 *
 * @author Shiva
 */
public abstract class Action {
    public abstract void execute(ScannedRobotEvent e);
    
    protected abstract String makeName();
    public String getName() { return makeName(); }
}
