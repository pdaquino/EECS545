
package EECS545.target;

import java.io.PrintStream;

/**
 *
 * @author Pedro
 */
public class Output {
    private static PrintStream out = null;
    public static void setOutStream(PrintStream outp) {
        out = outp;
    }
    public static void println(String s) {
        if(out != null) {
            out.println(s);
        }
    }
    public static PrintStream getStream() {
        return out;
    }
}
