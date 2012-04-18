package EECS545;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
import java.util.*;

// API help: http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html
public class Chesty extends MirroringTargetingRobot {
	
	//Random rand = Utils.getRandom();

    @Override
    protected Double targetEnemy() {
		
		//int value = rand.nextInt(21);
		
		//if(value == 0)
			//return new Double(0.0);
		
		//if(value < 11)
			//return new Double(2*(double)value);
		
		//return new Double(-(2*((double)value-10)));
		return 0.0;
    }
}
	