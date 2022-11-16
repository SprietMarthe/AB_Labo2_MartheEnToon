import javax.swing.*;
import java.awt.*;
import java.util.*;

/*
zorgen dat we containers kunnen verplaatsen
en dat er steeds wordt gekeken of de container kan verplaatst worden afh van de constraints
 */

public class Main extends Canvas{
    static Map<Integer,Container> containers;
    static Map<Integer, Slot> slots;
    static Assignments assignments;
    static Stack<Integer>[][] yard;
    public static void main(String[] args) throws InterruptedException {
        containers = new HashMap<>();
        slots = new HashMap<>();
        assignments = new Assignments();

        yard = ReadJSON.ReadJSONFile("JSON\\Terminal_4_3.json", containers, slots, assignments);

        for (int j = 0; j < yard[0].length; j++) {
            for (int k = 0; k < yard.length; k++) {
                System.out.print(yard[k][j] + " ");
            }
            System.out.println();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContainerClassUI(yard).setVisible(true);
            }
        });


//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //showFrame();
//        System.out.println(containers);
//        System.out.println(slots);
//        System.out.println(assignments);

        int idContainer = 1;
        int[] arSlot = new int[1];
        arSlot[0] = 4;

        Thread.sleep(1000);

        moveContainer(idContainer, arSlot);
        //moveContainer(idContainer, arSlot);
    }

    private static void moveContainer(int idContainer, int[] containerSlots) {
        boolean moved = false;
        while(!moved){
            int upperContainer = getUpperContainer(idContainer);
            if(upperContainer==idContainer) {//pak em vast en verplaats naar gewenste slot

           }
           else{ // andere container eerst verplaatsen
                moveContainerToTheSide(upperContainer, containerSlots);
           }
        }
    }

    private static void moveContainerToTheSide(int upperContainer, int[] containerSlots) {
        // kunnen we hem ergens plaatsen ja doe dan
        // move container to freeslots
//        for (int i = 0; i < yard.length; i++) {
//            for (int j = 0; j < yard[0].length; j++) {
//                for (int k = 0; k < containers.get(upperContainer).getLengte(); k++) {
//                    int somLengtes = 0;
//                    if(j != yard[0].length-k-1){
//                        System.out.println("i: " + i + " j: " + j + " k: " + k);
//                        somLengtes += containers.get(yard[i][j+k+1].peek()).getLengte();
//                        System.out.println("somLengtes: " + somLengtes);
//                        if(yard[i][j] == yard[i][j+1]){
//
//
//                        }
//
//                    }
//                }
//
//
//            }
//        }
    }



    private static int getUpperContainer(int idContainer) {
        int c = idContainer;
//        for (int i = 0; i < assignments.assignment.get(idContainer).length; i++) {
//            int xSlot = slots.get(assignments.assignment.get(idContainer)[i]).getX();
//            int ySlot = slots.get(assignments.assignment.get(idContainer)[i]).getY();
////            if (containers.get(idContainer).getHoogte() < yard[xSlot][ySlot]){
////                // er staat andere container boven de container
////
////            }
//        }
        Slot s = slots.get(assignments.assignment.get(idContainer)[0]);
        if(yard[s.getX()][s.getY()].size()>0)
            c = yard[s.getX()][s.getY()].pop();
        return c;
    }

    // begin movements

    //ArrayList<Verplaatsing> Verplaatsingen = new ArrayList<>();

    /*public int berekenTravelTime (){

        return 0;
    }*/

    // puntje 4: is er een conflict, zoja doe iets
}
