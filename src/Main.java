import java.util.*;

public class Main {
    static Map<Integer,Container> containers;
    static Map<Integer, Slot> slots;
    static Map<Integer, Assignment> assignments;
    static int[][] yard = new int[10][10];
    public static void main(String[] args){
        containers = new HashMap<>();
        slots = new HashMap<>();
        assignments = new HashMap<>();
        ReadJSON.ReadJSONFile("JSON\\Terminal_4_3.json", containers, slots, assignments,yard);

        System.out.println(containers);
        System.out.println(slots);
        System.out.println(assignments);
    }

    // begin movements

    //ArrayList<Verplaatsing> Verplaatsingen = new ArrayList<>();

    /*public int berekenTravelTime (){

        return 0;
    }*/

    // puntje 4: is er een conflict, zoja doe iets
}
