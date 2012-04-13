
package EECS545.target;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Scales the features according to the ranges passed in a file. The file
 * must be in the same format as the one returned by libsvm's svm-scale -s
 * @author Pedro
 */
public class FeatureScaler {
    private List<Double> min = new ArrayList<Double>();
    private List<Double> max = new ArrayList<Double>();
    public FeatureScaler(String filename) throws IOException {
        Scanner s = new Scanner(new File(filename));
        // this is an example file
        /*
            x
            -1 1
            1 41.24351687868336 760.9128229570244
            2 29.84143712580561 573.1942425959119
            3 -179.7277461012503 179.9903179470024
            4 -7.999983195146165 7.999999060013407
            5 -7.998909916797049 7.998370449330738
            6 -5.000000000000028 5.000000000000028
            7 25.79254512090287 774.2255267243039
            8 25.82665897504774 574.9312745624957
            
         */
        // first two lines, and the first number of the following lines, are ignored
        s.nextLine(); s.nextLine();
        while(s.hasNext()) {
            s.next(); // first number on every line
            min.add(s.nextDouble());
            max.add(s.nextDouble());
        }
    }
    
    public Double[] scale(Double[] features) {
        if(features.length != min.size()) {
            throw new IllegalArgumentException("The number of ranges and features must be the same.");
        }
        Double[] scaled = new Double[features.length];
        for(int i = 0; i < features.length; i++) {
            scaled[i] = scale(i, features[i]);
        }
        return scaled;
    }

    private double scale(int i, Double f) {
        // copied from SVMPredict
        double fmin = min.get(i);
        double fmax = max.get(i);
        return 2*(f-fmin)/(fmax-fmin);
    }
}
