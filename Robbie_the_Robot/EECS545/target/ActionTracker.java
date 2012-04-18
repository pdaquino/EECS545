
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
    
    private boolean verbose = false;
    
    public ActionTracker(Action a, MirroringEvadingRobot rob) {
        this.action = a;
        this.rob = rob;
        if(a.getName().equals("NOP")) {
            trackingNOP = true;
        }
        if(a.isFinished()) {
            this.roundCaptureSPrime = rob.getTime() + sPrimeWait;
            rob.out.println("action finished in ctor; waiting until turn " + roundCaptureSPrime);
        }
        this.s = new State(rob);
    }
    
    public void trackTime() {
        if(roundCaptureSPrime == -1) {
            if(action.isFinished()) {
                this.roundCaptureSPrime = rob.getTime() + sPrimeWait;
                output("trackTime: action just finished; waiting until turn " + roundCaptureSPrime + " to capture s'");
            } else {
                output("trackTime: action isn't finished yet");
            }
            return;
        }
        if (sPrime == null && rob.getTime() >= roundCaptureSPrime) {
            output("trackTime: capturing s'");
            sPrime = new State(rob);
            if (trackingNOP) {
                output("trackTime: was tracking NOP action; we are finished now");
                finished = true;
            }
        } else {
            if (sPrime != null) {
                //output("trackTime: s' has been captured already");
            } else {
                output("trackTime: waiting until round " + roundCaptureSPrime + " to capture s'");
            }
        }
    }
    
    public void setVerbose(boolean v) {
        this.verbose = v;
    }
    private void output(String s) {
        if(verbose) {
            rob.out.println("ActionTracker: " + s);
        }
    }
    public void updateBulletStatus(BulletHitEvent e) {
        assert(!trackingNOP);
        reward = Rewards.getReward(e);
        output("Bullet hit, reward = " + reward);
        if(sPrime == null) {
            sPrime = new State(rob);
            output("Event happened before capturing s'; capturing now");
        } else {
            output("s' has been captured");
        }
        finished = true;
    }
    
    public void updateBulletStatus(BulletHitBulletEvent e) {
        assert(!trackingNOP);
        reward = Rewards.getReward(e);
        output("Bullet hit another bullet, reward = " + reward);
        if(sPrime == null) {
            sPrime = new State(rob);
            output("Event happened before capturing s'; capturing now");
        } else {
            output("s' has been captured");
        }
        finished = true;
    }
    
    public void updateBulletStatus(BulletMissedEvent e) {
        assert(!trackingNOP);
        reward = Rewards.getReward(e);
        output("Bullet missed, reward = " + reward);
        if(sPrime == null) {
            sPrime = new State(rob);
            output("Event happened before capturing s'; capturing now");
        } else {
            output("s' has been captured");
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
