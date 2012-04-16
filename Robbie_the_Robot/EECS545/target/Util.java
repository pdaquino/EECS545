
package EECS545.target;

import java.util.List;

/**
 *
 * @author Pedro
 */
public class Util {
    public static void print(List<Double> v) {
        print(v.toArray(new Double[0]));
    }
    public static void print(Double[] v) {
        StringBuilder blder = new StringBuilder();
        for(Double d : v) {
            blder.append(d).append(' ');
        }
        Output.println(blder.toString());
    }
}
