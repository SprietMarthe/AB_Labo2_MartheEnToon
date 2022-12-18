import java.util.HashMap;

public class Assignments {
    HashMap<Integer, Integer> assignment; //id container + id first slot

    public Assignments() {
        this.assignment = new HashMap<Integer, Integer>() {
        };
    }

    public void put(int container, int slot){
        this.assignment.put(container, slot);
    }

    @Override
    public String toString() {

        return "Assignments{" +
                "assignment(idContainer=slotId)=" + assignment +
                '}';
    }
}
