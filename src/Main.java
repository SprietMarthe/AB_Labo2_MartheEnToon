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

 7/12
 lijst creeren met mogelijke volgorde van hoe containers kunnen geplaatst worden
    eerst check of containers goed staan
    hoeveel containers op zelfde plaats (op lengte kijken)
    sortoren op slot ids
 zorgen dat maxheight voor alle klassen niet overschreven worden

 */

public class Main extends Canvas{
    static Map<Integer,Container> containers;
    static Map<Integer, Slot> slots;
    static Map<Integer, Kraan> kranen;
    static Assignments assignments;     //huidige situatie
    static Assignments allTargetAssignments;
    static Assignments targetAssignments;
    static Stack<Integer>[][] yard;
    static InfoFromJSON infoFromJSON;
    static InfoFromJSON infoFromJSONTarget;


    public static void main(String[] args) throws InterruptedException {
        containers = new HashMap<>();
        slots = new HashMap<>();
        assignments = new Assignments();
        targetAssignments = new Assignments();
        allTargetAssignments = new Assignments();
        infoFromJSON = new InfoFromJSON();
        infoFromJSONTarget = new InfoFromJSON();
        kranen = new HashMap<>();

        // Read Files
        yard = JSONClass.ReadJSONFile("JSON\\terminal22_1_100_1_10.json", containers, slots, assignments,kranen, infoFromJSON);
        JSONClass.ReadJSONTargetFile("JSON\\terminal22_1_100_1_10target.json", allTargetAssignments, infoFromJSONTarget);

        setTargetAssignments();
        // sort containers met zelfde targetslot op lengte van container van klein naar groot


        // Visualisatie
        ContainerClassUI.main(yard);

        // Print info
//        printYard();
        System.out.println(containers);
        System.out.println(slots);
        System.out.println(assignments);
        System.out.println(targetAssignments);


//        // TODO later: kijken of container op zelfde slot moet blijven maar van hoogte veranderd
//
//        Thread.sleep(1000);

        // de eerste van targetAssignment // for
        for (Map.Entry<Integer,Integer> entry : targetAssignments.assignment.entrySet()) {
            moveContainer(entry.getKey());
        }


//        moveContainer(idContainer, futureSlot);
        printYard();
    }

    private static void setTargetAssignments() {
        targetAssignments.assignment.putAll(allTargetAssignments.assignment);
        for (Map.Entry<Integer,Integer> entry : assignments.assignment.entrySet()) {
            if(Objects.equals(targetAssignments.assignment.get(entry.getKey()), entry.getValue())){
                targetAssignments.assignment.remove(entry.getKey());
            }
        }
    }

    private static void printYard() {
        for (int j = 0; j < yard.length; j++) {
            for (int k = 0; k < yard[0].length; k++) {
                System.out.print(yard[j][k] + " ");
            }
            System.out.println();
        }
    }

//    private static void moveContainer(int idContainer, int futureSlot, int heightFutureAssignment) {
//        boolean moved = false;
//        while(!moved){
//            int upperContainer = peekUpperContainer(idContainer);
//            if(upperContainer==idContainer) {                                                   //pak em vast en verplaats naar gewenste slot
//                Container c = getUpperContainer(idContainer);
//                if(checkIfFutureSlotsFree(idContainer, futureSlot, heightFutureAssignment)){    // zet hem direct
//
//                    setContainer(c, futureSlot, heightFutureAssignment);
//                    moved=true;
//                }
//                else{
//                    // zorg dat er plaats is
//                    //TODO
//                    moved=true;
//                    makeFutureSlotFree(c, futureSlot, heightFutureAssignment);
//                }
//
//           }
//           else{ // andere container eerst verplaatsen
//               //TODO
//                moved=true;
//                moveContainerToTheSide(upperContainer, futureSlot);
//           }
//        }
//    }
    private static void moveContainer(int idContainer) {
        boolean moved = false;
        while(!moved){
            int upperContainer = peekUpperContainer(idContainer);
            if(upperContainer==idContainer) {                                                   //pak em vast en verplaats naar gewenste slot
                Container c = getUpperContainer(idContainer);
                if(checkIfFutureSlotsFree(idContainer, targetAssignments.assignment.get(idContainer))){    // zet hem direct
                    setContainer(c, targetAssignments.assignment.get(idContainer));
                    moved=true;
                }
                else{
                    // zorg dat er plaats is
                    //TODO
                    moved=true;
                    makeFutureSlotFree(c, targetAssignments.assignment.get(idContainer));
                }

            }
            else{ // andere container eerst verplaatsen
                //TODO
//                moved=true;
                moveContainerToTheSide(upperContainer, targetAssignments.assignment.get(idContainer));
            }
        }
    }

    private static void makeFutureSlotFree(Container c, int futureSlot) {

    }

    private static void setContainer(Container c, int futureSlot) {
        assignments.assignment.put(c.id,futureSlot);
        c.setHoogte(yard[slots.get(futureSlot).getX()][slots.get(futureSlot).getY()].size());
        for (int i = 0; i < c.lengte; i++) {
            yard[slots.get(futureSlot).getX()+i][slots.get(futureSlot).getY()].push(c.id);
        }
    }

    private static boolean checkIfFutureSlotsFree(int containerId, int futureSlot) {
        // hoe hoog is het -> opvullen/containers weghalen
        // kunnen we hem plaatsen? -> zelfde hoogte en geen grotere containers onder
        int containerLengte = containers.get(containerId).lengte;
        Slot s = slots.get(futureSlot);
        for (int i = 0; i < containerLengte; i++) {
            Stack<Integer> stack = yard[s.getX()+i][s.getY()];
            if(yard[s.getX()+i][s.getY()].size() > 0 &&
                    containers.get(stack.peek()) != null &&
                    !checkContainerLower(
                            containers.get(stack.peek()),
                            futureSlot
                    )){
                System.out.println("future slots false! -> lower container false");
                return false;
            }
        }
        return true;
    }

    private static boolean checkContainerLower(Container container, int futureSlot) {
        System.out.println("container: " + container);
        System.out.println("futureSlot: " + futureSlot);
        // kijken of de container eronder kan gebruikt worden op op te stapelen
        int somLengtes = 0;
        int slotHoogte = yard[slots.get(futureSlot).getX()][slots.get(futureSlot).getY()].size();
        Stack<Integer> slotStack = new Stack<>();
        int containerLengte = container.lengte;
        ArrayList<Integer> idContainers = new ArrayList<>();
        Slot s = slots.get(futureSlot);
        for (int i = 0; i < containerLengte; i++) {
            if(slotHoogte != yard[s.getX()+i][s.getY()].size()){
                return false;
            }
            slotStack.addAll(yard[s.getX()+i][s.getY()]);
            System.out.println("slot stack: " + slotStack);
            if(slotStack.size()>0){
                Container idC = containers.get(slotStack.pop());
                while(idC.hoogte != yard[s.getX()+i][s.getY()].size() &&
                        slotStack.size()>=yard[s.getX()+i][s.getY()].size() &&
                        slotStack.size()>0){
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
        Slot s = slots.get(assignments.assignment.get(idContainer));
        if(yard[s.getX()][s.getY()].size()>0)
            c = yard[s.getX()][s.getY()].peek();
        System.out.println("container peek: " + c);
        return c;
    }

    private static Container getUpperContainer(int idContainer) {
        int c = idContainer;
        Slot s = slots.get(assignments.assignment.get(idContainer)); //TODO bij verplaatsen container ook assignments mee vernaderen
        if(yard[s.getX()][s.getY()].size()>0){
            for (int i = 0; i < containers.get(c).lengte; i++) {
                c = yard[s.getX()+i][s.getY()].pop();
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
