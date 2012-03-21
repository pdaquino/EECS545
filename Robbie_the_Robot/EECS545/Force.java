/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EECS545;

/**
 *  Force Class which models forces
 * 
 */
public class Force {
    public double magnitude;
    public double direction;
    
    public Force(double magnitude, double direction){
        this.magnitude = magnitude;
        this.direction = direction;
    }
    
    public Force sum(Force F2){
        double mag, dir;
        double xSum, ySum;
        xSum = getHorizontalComp() + F2.getHorizontalComp();
        ySum = getVerticalComp() + F2.getVerticalComp();
        mag = Math.sqrt((xSum*xSum)+(ySum*ySum));
        dir = Math.atan2(ySum, xSum);        
        return new Force(mag,dir);
    }
    
    public double getHorizontalComp(){
        return magnitude*Math.cos(direction);
    }
    
    public double getVerticalComp(){
        return magnitude*Math.sin(direction);
    }
    
}
