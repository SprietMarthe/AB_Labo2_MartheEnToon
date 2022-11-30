import javax.swing.*;
import java.awt.*;
import java.util.*;

/*
zorgen dat we containers kunnen verplaatsen
en dat er steeds wordt gekeken of de container kan verplaatst worden afh van de constraints


30/11
container future plaats is gegeven door 1 slot
yard is altijd van links naar rechts

kraan pikt de container op in het midden
    stel container lengte 1 -> kraan pakt container op (0.5 , 0.5)
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

        yard = ReadJSON.ReadJSONFile("JSON\\Terminal_4_3_Test.json", containers, slots, assignments);

        printYard();

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

        int idContainer = 3;
        int futureSlot = 6;
        int heightFutureAssignment = 1;
        // TODO later: kijken of container op zelfde slot moet blijven maar van hoogte veranderd

        Thread.sleep(1000);

        moveContainer(idContainer, futureSlot, heightFutureAssignment);
        printYard();
    }

    private static void printYard() {
        for (int j = 0; j < yard.length; j++) {
            for (int k = 0; k < yard[0].length; k++) {
                System.out.print(yard[j][k] + " ");
            }
            System.out.println();
        }
    }

    private static void moveContainer(int idContainer, int futureSlot, int heightFutureAssignment) {
        boolean moved = false;
        while(!moved){
            int upperContainer = peekUpperContainer(idContainer);
            if(upperContainer==idContainer) {//pak em vast en verplaats naar gewenste slot
                if(checkIfFutureSlotsFree(idContainer, futureSlot, heightFutureAssignment)){
                    // zet hem direct
                    Container c = getUpperContainer(idContainer);
                    setContainer(c, futureSlot, heightFutureAssignment);
                    moved=true;
                }
                else{
                    // zorg dat er plaats is
                    //TODO
                }

           }
           else{ // andere container eerst verplaatsen
               //TODO
                moveContainerToTheSide(upperContainer, futureSlot);
           }
        }
    }

    private static void setContainer(Container c, int futureSlot, int heightFutureAssignment) {
        int[] arSlots = new int[c.lengte];
        for (int i = 0; i < c.lengte; i++) {
            yard[slots.get(futureSlot).getX()][slots.get(futureSlot).getY() +i].push(c.id);
            arSlots[i] = futureSlot+i;
        }
        assignments.put(c.id,arSlots);
    }

    private static boolean checkIfFutureSlotsFree(int containerId, int futureSlot, int heightFutureAssignment) {
        // hoe hoog is het -> opvullen/containers weghalen
        // kunnen we hem plaatsen? -> zelfde hoogte en geen grotere containers onder
        int containerLengte = containers.get(containerId).lengte;
        for (int i = 0; i < containerLengte; i++) {
            Slot s = slots.get(futureSlot+i);
            if (yard[s.getX()][s.getY()].size() != heightFutureAssignment - 1){
                System.out.println("future slots false! -> height !=");
                return false;
            }
            else if(heightFutureAssignment>0 && containers.get(yard[s.getX()][s.getY()]) != null &&!checkContainerLower(containers.get(yard[s.getX()][s.getY()]), futureSlot, heightFutureAssignment-1)){
                System.out.println("future slots false! -> lower container false");
                return false;
            }
        }
        return true;
    }

    private static boolean checkContainerLower(Container container, int futureSlot, int height) {
        System.out.println("container: " + container);
        System.out.println("futureSlot: " + futureSlot);
        System.out.println("hoogte future: " + height);
        // kijken of de container eronder kan gebruikt worden op op te stapelen
        int somLengtes = 0;
        int slotHoogte = yard[slots.get(futureSlot).getX()][slots.get(futureSlot).getY()].size();
        Stack slotStack;
        int containerLengte = container.lengte;
        ArrayList<Integer> idContainers = new ArrayList<>();
        for (int i = 0; i < containerLengte; i++) {
            Slot s = slots.get(futureSlot + i);
            if(slotHoogte != yard[slots.get(futureSlot + i).getX()][slots.get(futureSlot + i).getY()].size()){
                return false;
            }
            slotStack = yard[s.getX()][s.getY()];
            System.out.println("slot stack: " + slotStack);
            if(slotStack.size()>0){
                Container idC = containers.get(slotStack.pop());;
                while(idC.hoogte != height && slotStack.size()>=height){
                    idC = containers.get(slotStack.pop());
                }
                if(!idContainers.contains(idC.id)){
                    idContainers.add(idC.id);
                }
            }
        }
        if(slotHoogte != 0){
            for (int i = 0; i < idContainers.size(); i++) {
                somLengtes += containers.get(idContainers.get(i)).getLengte();
                System.out.println("somLengtes: " + somLengtes);
                if (somLengtes != container.lengte){
                    return false;
                }
            }
        }
        return true;
    }

    private static void moveContainerToTheSide(int upperContainer, int containerSlots) {
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



    private static int peekUpperContainer(int idContainer) {
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
            c = yard[s.getX()][s.getY()].peek();
        System.out.println("container peek: " + c);
        return c;
    }

    private static Container getUpperContainer(int idContainer) {
        int c = idContainer;
        Slot s = slots.get(assignments.assignment.get(idContainer)[0]); //TODO bij verplaatsen container ook assignments mee vernaderen
        if(yard[s.getX()][s.getY()].size()>0){
            for (int i = 0; i < containers.get(c).lengte; i++) {
                c = yard[s.getX()][s.getY()+i].pop();
            }
        }

        System.out.println("container pop: " + c);
        return containers.get(c);
    }

    // begin movements

    //ArrayList<Verplaatsing> Verplaatsingen = new ArrayList<>();

    /*public int berekenTravelTime (){

        return 0;
    }*/

    // puntje 4: is er een conflict, zoja doe iets
}
