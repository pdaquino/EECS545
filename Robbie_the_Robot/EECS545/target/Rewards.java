
package EECS545.target;

import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;

/**
 *
 * @author Pedro
 */
public class Rewards {
    /*
     * Reward when the bullet hits the opponent
     */
    public static double getReward(BulletHitEvent e) {
        return 40; // TODO does this value make sense?
    }
    /*
     * Reward when the bullet hits another bullet
     */
    public static double getReward(BulletHitBulletEvent e) {
        // this is just bad luck
        return 0;
    }
    
    public static double getReward(BulletMissedEvent e) {
        return -10;
    }
    
    public static double getRewardNOP() {
        return -1;
    }
    
}
