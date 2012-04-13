package EECS545;

//As the suggests does nothing.
public class NOP implements Action{
    String name = "No Action Gun";    
    
    
    public boolean execute() {
        return true;
    }

    public String getName() {
        return name;
    }
    
}
