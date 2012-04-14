
package EECS545.target;

import EECS545.MirroringEvadingRobot;
import EECS545.State;
import robocode.AdvancedRobot;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;

/**
 *
 * @author Pedro
 */
public class ActionTracker {
    // how many timesteps do we wait before capturing s' after we fired
    // (we called this number "x" when we were discussing)
    public final int sPrimeWait = 5;
    
    private boolean trackingNOP = false;
    private long roundCaptureSPrime = -1;
    private double reward;
    private State s = null; // state when we fired
    private State sPrime = null; // state sPrimeWait turns after we fired
    private boolean finished = false;
    private Action action = null;
    private MirroringEvadingRobot rob;
    
    public ActionTracker(Action a, MirroringEvadingRobot rob) {
        this.action = a;
        this.rob = rob;
        if(a.getName().equals("NOP")) {
            trackingNOP = true;
        }
        this.roundCaptureSPrime = rob.getTime() + sPrimeWait;
        this.s = new State(rob);
    }
    
    public void trackTime() {
        if(sPrime == null && rob.getTime() >= roundCaptureSPrime) {
            sPrime = new State(rob);
            if(trackingNOP) {
                finished = true;
            }
        }
    }
    
    public void updateBulletStatus(BulletHitEvent e) {
        assert(!trackingNOP);
        reward = Rewards.getReward(e);
        if(sPrime == null) {
            sPrime = new State(rob);
        }
        finished = true;
    }
    
    public void updateBulletStatus(BulletHitBulletEvent e) {
        assert(!trackingNOP);
        reward = Rewards.getReward(e);
        if(sPrime == null) {
            sPrime = new State(rob);
        }
        finished = true;
    }
    
    public void updateBulletStatus(BulletMissedEvent e) {
        assert(!trackingNOP);
        reward = Rewards.getReward(e);
        if(sPrime == null) {
            sPrime = new State(rob);
        }
        finished = true;
    }

    public Action getAction() {
        return action;
    }
    
    public boolean isFinished() {
        return finished;
    }
    
    public double getReward() {
        return reward;
    }

    public State getS() {
        return s;
    }

    public State getsPrime() {
        return sPrime;
    }
    
    
}
