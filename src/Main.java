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
    static Map<Integer, Kraan> cranes;
    static Assignments assignments;     //huidige situatie
    static Assignments allTargetAssignments;
    static Assignments targetAssignments;
    static Stack<Integer>[][] yard;
    static InfoFromJSON infoFromJSON;
    static InfoFromJSON infoFromJSONTarget;
    static ArrayList<Beweging> movements;
    static int time;


    public static void main(String[] args) throws InterruptedException {
        containers = new HashMap<>();
        slots = new HashMap<>();
        assignments = new Assignments();
        targetAssignments = new Assignments();
        allTargetAssignments = new Assignments();
        infoFromJSON = new InfoFromJSON();
        infoFromJSONTarget = new InfoFromJSON();
        cranes = new HashMap<>();
        movements = new ArrayList<>();
        time = 0;

        // Read Files
        yard = JSONClass.ReadJSONFile("JSON\\terminal22_1_100_1_10.json", containers, slots, assignments,cranes, infoFromJSON);
        JSONClass.ReadJSONTargetFile("JSON\\terminal22_1_100_1_10target.json", allTargetAssignments, infoFromJSONTarget);

        setTargetAssignments();
        // TODO sort containers met zelfde targetslot op lengte van container van klein naar groot


        // Visualisatie
        ContainerClassUI.main(yard);

        // Print info
//        printYard();
        System.out.println(containers);
        System.out.println(slots);
        System.out.println(assignments);
        System.out.println(targetAssignments);



        // de eerste van targetAssignment // for
        for (Map.Entry<Integer,Integer> entry : targetAssignments.assignment.entrySet()) {
            moveContainer(entry.getKey());
        }


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
            System.out.println(j+" ");
            for (int k = 0; k < yard[0].length; k++) {
                System.out.print(yard[j][k] + " ");
            }
            System.out.println();
        }
    }

    private static void moveContainer(int idContainer) {
        boolean moved = false;
        while(!moved){
            int upperContainer = peekUpperContainer(idContainer);
            if(upperContainer==idContainer) {                                                   //pak em vast en verplaats naar gewenste slot
                Container c = getUpperContainer(idContainer);
                if(checkIfFutureSlotsFree(idContainer, targetAssignments.assignment.get(idContainer))){    // zet hem direct
                    setContainer(c, targetAssignments.assignment.get(idContainer));
                    moved=true;
                    useCranes(c,targetAssignments.assignment.get(idContainer));
                    // verplaats kranen -> methode beide kranen samen werken om container te verplaatsen
                }
                else{
                    // zorg dat er plaats is
                    //TODO
                    makeFutureSlotFree(c, targetAssignments.assignment.get(idContainer));
                    useCranes(c, null);
                    moved=true;
                }
            }
            else{ // andere container eerst verplaatsen
                //TODO
                useCranes(containers.get(upperContainer),null);
//                moved=true;
                moveContainerToTheSide(upperContainer, targetAssignments.assignment.get(idContainer));
            }
        }
    }

    private static void useCranes(Container c, Integer futureSlot) {
        if(futureSlot.equals(null)){
            futureSlot = getNearestFreeSlot(c);
        }
        moveClosestCrane(c);


    }

    private static int getNearestFreeSlot(Container c) {
        int dichtsteX = Integer.MAX_VALUE;
        int dichtsteY = Integer.MAX_VALUE;
        Slot dichtsteSlot = null;
        for (Map.Entry<Integer, Slot> entry : slots.entrySet()) {
            if (checkIfFutureSlotsFree(c.id, entry.getKey())){
                if (entry.getValue().getX() <= dichtsteX && entry.getValue().getY() <= dichtsteY){
                    dichtsteSlot = entry.getValue();
                    dichtsteX = entry.getValue().getX();
                    dichtsteY = entry.getValue().getY();
                }
            }
        }
        return dichtsteSlot.getId();
    }

    private static void moveClosestCrane(Container c) {
        Kraan k = cranes.get(0);
        Slot s = slots.get(assignments.assignment.get(c.id));
        double distance = Integer.MAX_VALUE;
        for (Map.Entry<Integer,Kraan> entry : cranes.entrySet()) {
            double d = getDistanceBetweenSlotAndCrane(s, entry.getValue());
            if(d < distance){
                k = entry.getValue();
                distance = d;
            }
        }
        int endTime = getEndTime(s, k);
        double endX = s.x/2;
        double endY = s.y/2;
        movements.add(new Beweging(k.id, -1, time, endTime, k.x, k.y, endX, endY));
        k.x = s.x;
        k.y = s.y;
        time = endTime;
        // een movement voor begin kraan movement en zet container op eind positie

    }

    private static int getEndTime(Slot s, Kraan k) {
        double xDist = Math.abs(s.x-k.x);
        double yDist = Math.abs(s.y-k.y);
        int t = (int) Math.max(xDist*k.xspeed, yDist*k.yspeed);
        System.out.println("endTime: " + t);
        return t;
    }

    private static double getDistanceBetweenSlotAndCrane(Slot s, Kraan k) {
        double distance = Math.sqrt((s.x-k.x)*(s.x-k.x) + (s.y-k.y)*(s.y-k.y));
        System.out.println("distance: " + distance);
        return distance;
    }

    private static void makeFutureSlotFree(Container c, int futureSlot) {
        //TODO
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
        //TODO
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
        Slot s = slots.get(assignments.assignment.get(idContainer));
        if(yard[s.getX()][s.getY()].size()>0)
            c = yard[s.getX()][s.getY()].peek();
        System.out.println("container peek: " + c);
        return c;
    }

    private static Container getUpperContainer(int idContainer) {
        int c = idContainer;
        Slot s = slots.get(assignments.assignment.get(idContainer)); //TODO bij verplaatsen container ook assignments mee veranderen
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
