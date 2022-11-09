import java.util.Arrays;
import java.util.Map;

public class Assignment {
    int[] slots;
    int container;

    public Assignment(int[] slot, int container) {
        this.slots=slot;
        this.container=container;
    }

    public int[] getSlots() {
        return slots;
    }

    public void setSlots(int[] slots) {
        this.slots = slots;
    }

    public int getContainer() {
        return container;
    }

    public void setContainer(int container) {
        this.container = container;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "slots=" + Arrays.toString(slots) +
                ", container=" + container +
                '}';
    }
}
