import java.util.HashMap;

public class Assignments {
    HashMap<Integer, int[]> assignment;

    public Assignments() {
        this.assignment = new HashMap<Integer, int[]>() {
        };
    }

    public void put(int container, int[] slots){
        this.assignment.put(container, slots);
    }

    @Override
    public String toString() {

        return "Assignments{" +
                "assignment=" + assignment +
                '}';
    }
}
