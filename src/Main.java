import java.awt.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

/*
get all containers that exceed maxheight
if no place -> optimize


als je de crane verplaatst aan de kant -> iets met deze kraan doen (optioneel)
map targetassignments opvoorhand sorteren
    map met targetassignments dat enkel kraan 1 of kraan 2 gebruikt of kraan + overlap
    een voor een toevoegen tot dat deze lijsten leeg zijn en dan al derest
als je containers aan de kant moet plaatsen, zet ze erna direct terug
getClosestCrane -> indien in overlap -> get crane dat kan oppakken en wegzetten
variable van safedistance ipv hardcoded
 */

public class Main extends Canvas{
    static Map<Integer,Container> containers;
    static Map<Integer, Slot> slots;
    static Map<Integer, Kraan> cranes;
    static Assignments assignments;     //huidige situatie
    static Assignments allTargetAssignments;
    static Assignments targetAssignments;
    static Assignments newTargetAssingments;
    static Stack<Integer>[][] yard;
    static InfoFromJSON infoFromJSON;
    static InfoFromJSON infoFromJSONTarget;
    static ArrayList<Beweging> movements;
    static double time;


    public static void main(String[] args){
        containers = new HashMap<>();
        slots = new HashMap<>();
        assignments = new Assignments();
        targetAssignments = new Assignments();
        newTargetAssingments = new Assignments();
        allTargetAssignments = new Assignments();
        infoFromJSON = new InfoFromJSON();
        infoFromJSONTarget = new InfoFromJSON();
        cranes = new HashMap<>();
        movements = new ArrayList<>();
        time = 0;

        // Read Files
        yard = JSONClass.ReadJSONFile("JSON\\5t\\TerminalB_20_10_3_2_160.json", containers, slots, assignments,cranes, infoFromJSON);
        JSONClass.ReadJSONTargetFile("JSON\\5t\\targetTerminalB_20_10_3_2_160.json", allTargetAssignments, infoFromJSONTarget);
        // "JSON\\terminal22_1_100_1_10.json"
        // "JSON\\terminal22_1_100_1_10target.json"
        // "JSON\\1t\\TerminalA_20_10_3_2_100.json"
        // "JSON\\1t\\targetTerminalA_20_10_3_2_100.json"
        // "JSON\\3t\\TerminalA_20_10_3_2_160.json"
        // "JSON\\3t\\targetTerminalA_20_10_3_2_160.json"
        // "JSON\\5t\\TerminalB_20_10_3_2_160.json"
        // "JSON\\5t\\targetTerminalB_20_10_3_2_160.json"
        // "JSON\\6t\\Terminal_10_10_3_1_100.json"
        // "JSON\\6t\\targetTerminal_10_10_3_1_100.json"
        // "JSON\\7t\\TerminalC_10_10_3_2_80.json"
        // "JSON\\7t\\targetTerminalC_10_10_3_2_80.json"
        // "JSON\\8t\\TerminalC_10_10_3_2_80.json"
        // "JSON\\8t\\targetTerminalC_10_10_3_2_80.json"
        // "JSON\\8t\\TerminalC_10_10_3_2_80.json"
        // "JSON\\8t\\targetTerminalC_10_10_3_2_80.json"
        // "JSON\\9t\\TerminalC_10_10_3_2_100.json"
        // "JSON\\9t\\targetTerminalC_10_10_3_2_100.json"
        // "JSON\\10t\\TerminalC_10_10_3_2_100.json"
        // "JSON\\10t\\targetTerminalC_10_10_3_2_100.json"


        //"JSON\\2mh\\MH2Terminal_20_10_3_2_100.json"
        //"JSON\\4mh\\MH2Terminal_20_10_3_2_160.json"


        setTargetAssignments();
//        Assignments sortAssignments = new Assignments(targetAssignments.assignment);
//        targetAssignments.assignment.clear();
//        for (Map.Entry<Integer,Integer> entry : sortAssignments.assignment.entrySet()) {
//
//        }
        // TODO sort containers met zelfde targetslot op lengte van container van klein naar groot


        // Visualisatie
//        ContainerClassUI.main(yard);

        // Print info
        System.out.println("Initial Yard");
        printYard();
        System.out.println(containers);
        System.out.println(slots);
        System.out.println(assignments);
        System.out.println(targetAssignments);


        double timeNeededForParallelCrane = 0;
        // de eerste van targetAssignment // for
        if (targetAssignments.assignment.size()!=0){
//            Iterator<Integer> itr=sortedCraneTargetAssignment.iterator();
            Iterator<Integer> itr=targetAssignments.assignment.keySet().iterator();
            while (itr.hasNext()) {
//                try {
//                    TimeUnit.SECONDS.sleep(3);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                int key = Integer.parseInt(itr.next().toString());
//                String value = targetAssignments.assignment.get(key).toString();
                if (timeNeededForParallelCrane > 0){
                    double endTime = time;

                    time = Math.max(0, time - timeNeededForParallelCrane + 1);
                    timeNeededForParallelCrane =  moveContainer(key, true);
                    time = Math.max(endTime, time);
                }
                else{
                    timeNeededForParallelCrane = moveContainer(key, false);
                }
            }
        }
        else {
            int hoogstePunt =9999;
            int targetHeight = infoFromJSON.getTargetHeight();

            while(hoogstePunt>targetHeight){
                Collection<Container> values = containers.values();
                ArrayList<Container> containerList = new ArrayList<>(values);
                containerList.sort(Comparator.comparing(Container::getHoogte));
                ArrayList<Container> hoogsteContainers = new ArrayList<>();
                Collections.reverse(containerList);
                hoogstePunt = containerList.get(0).getHoogte();
                int laatsteHoogte = hoogstePunt;
                int i = 0;
                while(hoogstePunt == laatsteHoogte){
                    hoogsteContainers.add(containerList.get(i));
                    i++;
                    laatsteHoogte = containerList.get(i).getHoogte();
                }
                hoogsteContainers.sort(Comparator.comparing(Container::getLengte));
                for(Container c : hoogsteContainers){
                    if(hoogstePunt>targetHeight){
                        int freeSlot = getNearestFreeSlot(c);
                        int cont = c.getId();
                        targetAssignments.put(cont,freeSlot);
                        allTargetAssignments.put(cont,freeSlot);
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (timeNeededForParallelCrane > 0){
                            double endTime = time;
                            time = time - timeNeededForParallelCrane + 2;
                            moveContainer(cont, false);
                            time = Math.max(endTime, time);
                            timeNeededForParallelCrane = 0;
                        }
                        else{
                            timeNeededForParallelCrane = moveContainer(cont, false);
                        }
                    }
                    printYard();
                }
            }
        }

        System.out.println("Solution Yard");
        printYard();
        printMovements();
        System.out.println("\nEnd algorithm");
    }



    private static double moveContainer(int idContainer, boolean isParallel) {
//        System.out.println("currentSlot: " + slots.get(assignments.assignment.get(idContainer)));
//        System.out.println("futureSlot: " + slots.get(targetAssignments.assignment.get(idContainer)));
        boolean moved = false;
        while(!moved){
            Container upperContainer = peekUpperContainer(idContainer);
            Container c = getUpperContainer(idContainer);
            if(upperContainer.id==idContainer) {                                                               // Top container is requested container
                if(checkIfFutureSlotsFree(idContainer, allTargetAssignments.assignment.get(idContainer))        // Future slot is ready for placement
                        && !Objects.equals(allTargetAssignments.assignment.get(idContainer), assignments.assignment.get(idContainer))
                ){
                    return useCranes(c,allTargetAssignments.assignment.get(idContainer), isParallel);
                    // verplaats kranen -> methode beide kranen samen werken om container te verplaatsen
                }
                else{
                    useCranes(c, -2, isParallel);
                    return useCranes(c,allTargetAssignments.assignment.get(idContainer), isParallel);
                }
            }
            else{ // andere container eerst verplaatsen
                useCranes(c,-1, isParallel);
                useCranes(c,allTargetAssignments.assignment.get(idContainer), isParallel);
            }
        }
        return 0;
    }

    private static double useCranes(Container c, Integer futureSlot, boolean isParallel) {
        if(futureSlot.equals(-1)){
            Slot slotToFree = slots.get(assignments.assignment.get(c.id));
            int lengteContainer = containers.get(c.id).getLengte();
            //moveContainer(yard[slotToFree.getY()][slotToFree.getX()].peek());
            for (int yValue = slotToFree.getY(); yValue < lengteContainer; yValue++) {
                if(yard[yValue][slotToFree.getX()].size()>0)
                    moveContainer(yard[yValue][slotToFree.getX()].peek(), false);
            }
            //moveContainerToTheSide(c, allTargetAssignments.assignment.get(c.id));
            return 0;
        }
        else if(futureSlot.equals(-2)){
            Slot desiredSlot = slots.get(allTargetAssignments.assignment.get(c.id));
            int lengteContainer = containers.get(c.id).getLengte();
            for (int yValue = desiredSlot.getY(); yValue < lengteContainer; yValue++) {
                if(yard[yValue][desiredSlot.getX()].size()>0) moveContainer(yard[yValue][desiredSlot.getX()].peek(), false);
            }
            return 0;
        }
        else{
            Slot sCurrent = slots.get(assignments.assignment.get(c.id));
            Slot sFuture = slots.get(allTargetAssignments.assignment.get(c.id));
            double centerContainer = (double) c.lengte/2;
            if(!Objects.equals(allTargetAssignments.assignment.get(c.id), assignments.assignment.get(c.id))){
                return moveClosestCrane(c, getClostestCrane(sCurrent,sFuture,centerContainer, isParallel), sCurrent, sFuture, centerContainer);
            }
           return 0;
        }
    }

    private static int getFreeSlotAtEdge(Container c, Kraan k, boolean ascending, double centerContainer) {
        double xmin = k.xmin, xmax = k.xmin+4;
        if (ascending){
            xmin = k.xmax-centerContainer-4;
            xmax = k.xmax-centerContainer;
        }
        if (checkIfFutureSlotsFree(c.id,assignments.assignment.get(c.id)) && locationIsBetweenInterval(slots.get(assignments.assignment.get(c.id)).x, xmin, xmax)){
            return assignments.assignment.get(c.id);
        }
        for (Map.Entry<Integer, Slot> entry : slots.entrySet()) {
            if (checkIfFutureSlotsFree(c.id, entry.getKey()) && locationIsBetweenInterval(entry.getValue().x, xmin, xmax)){
                return entry.getKey();
            }
        }
        return -1;
    }

    private static int getNearestFreeSlot(Container c) {
        // TODO ook kijken of het voor de lengte van de container geldt en niet enkel voor 1 enkel slot
        int dichtsteX = Integer.MAX_VALUE;
        int dichtsteY = Integer.MAX_VALUE;
        int minHeight = Integer.MAX_VALUE;
        Slot dichtsteSlot = null;
        for (Map.Entry<Integer, Slot> entry : slots.entrySet()) {
            if (checkContainerLower(c, entry.getKey()) && checkIfFutureSlotsFree(c.getId(), entry.getKey())){// fixed: kijken of het niet op dezelfde plaats komt met containerSlots
                int verschilX = Math.abs(entry.getValue().getX()-slots.get(assignments.assignment.get(c.getId())).getX());
                int verschilY = Math.abs(entry.getValue().getY()-slots.get(assignments.assignment.get(c.getId())).getY());
                if (verschilY != 0 && verschilX != 0 &&
                    yard[slots.get(assignments.assignment.get(c.getId())).getY()][slots.get(assignments.assignment.get(c.getId())).getX()].size() - 1 >  yard[entry.getValue().getY()][entry.getValue().getX()].size()
                ){
                    if(yard[entry.getValue().getY()][entry.getValue().getX()].size()<=minHeight){
                        if ( verschilX <= dichtsteX && verschilY <= dichtsteY){
                            dichtsteSlot = entry.getValue();
                            dichtsteX = verschilX;
                            dichtsteY = verschilY;
                            minHeight = yard[entry.getValue().getY()][entry.getValue().getX()].size();
                        }
                    }

                }
            }
        }
        assert dichtsteSlot != null;
        return dichtsteSlot.getId();
    }

    private static void moveContainerToTheSide(Container c, int containerSlots) {
        int futureSlot = getNearestFreeSlot(containers.get(c.id));
        Slot sCurrent = slots.get(assignments.assignment.get(c.id));
        double centerContainer = (double) c.lengte/2;
        moveClosestCrane(c, getClostestCrane(sCurrent, sCurrent, centerContainer, false), sCurrent, slots.get(futureSlot), centerContainer);
        newTargetAssingments.assignment.putIfAbsent(c.id, allTargetAssignments.assignment.get(c.id));
    }

    private static double moveClosestCrane(Container c, Kraan k, Slot sCurrent, Slot sFuture, double centerContainer) {
//        if (getLatestTimeCrane(k) > time)
//            time = getLatestTimeCrane(k);
        time = Math.min(getLatestTimeCrane(k), time);
        checkMoveOtherCranes(k, sCurrent, centerContainer);
        if (!locationIsBetweenInterval(sFuture.x, k.xmin-0.5, k.xmax+0.5))  {
            dropContainerAtEdgeAndChangeCrane(k, c, sCurrent, sFuture, centerContainer);                           // If future slot is outside of interval -> change cranes
            return 0;
        }
        else{
            double beginTime = Math.ceil(time + getMoveTime(sCurrent.x+centerContainer,sCurrent.y+0.5, k.x, k.y, k));
            k.setX(sCurrent.x + centerContainer);
            k.setY(sCurrent.y + 0.5);
            checkMoveOtherCranes(k, sFuture, centerContainer);
            double endTime = Math.ceil(beginTime + getMoveTime(sFuture.x+centerContainer,sFuture.y+0.5, k.x, k.y, k));
            double endX = sFuture.x + centerContainer;
            double endY = sFuture.y + 0.5;
            movements.add(new Beweging(k.id, c.id, (int) beginTime, (int) endTime, k.x, k.y, endX, endY));
            k.setX(endX);
            k.setY(endY);
            time = endTime;
            setContainer(c, allTargetAssignments.assignment.get(c.id));
            return checkForPossibleMovementOtherCranes(k, c, sFuture, centerContainer);
        }
    }

    private static double getLatestTimeCrane(Kraan k) {
        double latestTime = 0;
        for (Beweging b: movements) {
            if (b.craneID == k.id){
                latestTime = b.endTime;
            }
        }
        return latestTime;
    }

    private static double checkForPossibleMovementOtherCranes(Kraan k, Container c, Slot sFuture, double centerContainer) {
        double minX = Math.min(k.x, sFuture.x + centerContainer);
        double maxX = Math.max(k.x, sFuture.x + centerContainer);
        Slot sNextFuture = null;
        int nextContainerId = -1;
        Iterator<Map.Entry<Integer, Integer>> iterator = targetAssignments.assignment.entrySet().iterator();
//        Iterator<Integer> iterator = sortedCraneTargetAssignment.iterator();

        boolean nextSlot = false;
        while (iterator.hasNext()) {
//            int key = iterator.next();
            Map.Entry<Integer, Integer> entry = iterator.next();
            if (c.id == entry.getKey() && iterator.hasNext()){
                nextSlot = true;
            }
            else if (nextSlot){
                sNextFuture = slots.get(targetAssignments.assignment.get(entry.getKey()));
                nextContainerId = entry.getKey();
                break;
            }
        }
        if (sNextFuture != null) {
            Slot sNextCurrent = slots.get(assignments.assignment.get(nextContainerId));
            for (Map.Entry<Integer,Kraan> entry : cranes.entrySet()) {
                if(k != entry.getValue() &&
                        !locationIsBetweenInterval(sNextFuture.x, minX,maxX) &&
                        !locationIsBetweenInterval(sNextCurrent.x, minX, maxX) &&
                        locationIsBetweenInterval(sNextFuture.x, entry.getValue().xmin,entry.getValue().xmax) &&
                        locationIsBetweenInterval(sNextCurrent.x, entry.getValue().xmin,entry.getValue().xmax)
                ){
                    double moveTime = getMoveTime(sNextFuture.x+centerContainer,sNextFuture.y+0.5, sNextCurrent.x, sNextCurrent.y, entry.getValue()) +
                            getMoveTime(sNextCurrent.x+centerContainer,sNextCurrent.y+0.5, entry.getValue().x, entry.getValue().y, entry.getValue());
                    checkMoveOtherCranes(entry.getValue(), sNextCurrent, centerContainer);
                    double originX = entry.getValue().x;
                    double originY = entry.getValue().y;
                    entry.getValue().setX(sNextCurrent.x + centerContainer);
                    entry.getValue().setY(sNextCurrent.y + 0.5);
                    checkMoveOtherCranes(entry.getValue(), sNextFuture, centerContainer);
                    entry.getValue().setX(originX);
                    entry.getValue().setY(originY);
                    return moveTime;
                }
            }
        }
        return 0;
    }

    private static void dropContainerAtEdgeAndChangeCrane(Kraan k, Container c, Slot sCurrent, Slot sFuture, double centerContainer) {
        Kraan newCrane = getClostestCrane(sFuture, sFuture, centerContainer, false);
        int futureSlot = getFreeSlotAtEdge(c, k, !(k.x - sFuture.x > 0), centerContainer);
        // kraan to futureslot + moveaway + newCrane to futureSlot
        if (futureSlot != -1){
            if (slots.get(futureSlot) != sCurrent)
                moveClosestCrane(c, k, sCurrent, slots.get(futureSlot), centerContainer);
            moveClosestCrane(c, newCrane, slots.get(futureSlot), sFuture, centerContainer);
        }
        else{
            System.out.println("No free space at edge found!!");
        }
    }

    private static Kraan getClostestCrane(Slot s, Slot sFuture, double centerContainer, boolean isParallel) {
        Kraan k = null;
        double distance = Integer.MAX_VALUE;
        for (Map.Entry<Integer,Kraan> entry : cranes.entrySet()) {
            double d = getDistanceBetweenSlotAndCrane(s, entry.getValue());
//            if((locationIsBetweenInterval(s.x+centerContainer, entry.getValue().xmin-0.5, entry.getValue().xmax+0.5) &&
//                    locationIsBetweenInterval(sFuture.x+centerContainer, entry.getValue().xmin-0.5, entry.getValue().xmax+0.5))) {
//                if (isParallel){
//                    if (movements.get(movements.size()-1).craneID != entry.getKey()){
//                        return entry.getValue();
//                    }
//                }
//                else{
//                    k = entry.getValue();
//                    distance = d;
//                }
//            }
//            else if (d < distance &&
//                    locationIsBetweenInterval(s.x+centerContainer, entry.getValue().xmin-0.5, entry.getValue().xmax+0.5)){
//                k = entry.getValue();
//                distance = d;
//            }
            if (isParallel && movements.get(movements.size()-1).craneID != entry.getKey()
                    && locationIsBetweenInterval(s.x+centerContainer, entry.getValue().xmin-0.5, entry.getValue().xmax+0.5)){
                k = entry.getValue();
                distance = d;
            }
            if(d < distance
                    && locationIsBetweenInterval(s.x+centerContainer, entry.getValue().xmin-0.5, entry.getValue().xmax+0.5)){
                k = entry.getValue();
                distance = d;
            }
        }
        return k;
    }

    private static void checkMoveOtherCranes(Kraan k, Slot s, double centerContainer) {
        boolean ascending = true;
        double minX = Math.min(k.x, s.x + centerContainer);
        double maxX = Math.max(k.x, s.x + centerContainer);
        if (k.x - s.x > 0)
            ascending = false;
        for (Map.Entry<Integer,Kraan> entry : cranes.entrySet()) {
            if(k != entry.getValue() &&
                    locationIsBetweenInterval(entry.getValue().x, minX-1, maxX+1)){
                if (ascending){
                    double endTime = Math.abs((maxX + 1.5) - entry.getValue().x)*entry.getValue().xspeed;
                    time = Math.max(time, getLatestTimeCrane(entry.getValue()));
                    movements.add(new Beweging(entry.getValue().id, -1, (int) Math.ceil(time), (int) (time + Math.ceil(endTime)), entry.getValue().x, entry.getValue().y, maxX + 1.5, entry.getValue().y));
                    entry.getValue().setX(maxX + 1.5);
                }
                else{
                    double endTime = Math.abs((minX - 1.5) - entry.getValue().x)*entry.getValue().xspeed;
                    time = Math.max(time, getLatestTimeCrane(entry.getValue()));
                    movements.add(new Beweging(entry.getValue().id, -1, (int) Math.ceil(time), (int) (time + Math.ceil(endTime)), entry.getValue().x, entry.getValue().y, minX - 1.5, entry.getValue().y));
                    entry.getValue().setX(minX - 1.5);
                }
            }
        }
    }

    private static boolean locationIsBetweenInterval(double x, double minX, double maxX) {
        return x > minX && x < maxX;
    }

    private static double getMoveTime(double x1, double y1, double x2, double y2, Kraan k) {
        double xDist = Math.abs(x1-x2);
        double yDist = Math.abs(y1-y2);
        return Math.max(xDist*k.xspeed, yDist*k.yspeed);
    }

    private static double getDistanceBetweenSlotAndCrane(Slot s, Kraan k) {
        return Math.sqrt((s.x-k.x)*(s.x-k.x) + (s.y-k.y)*(s.y-k.y));
    }

    private static boolean checkIfFutureSlotsFree(int containerId, int futureSlot) {
        int containerLengte = containers.get(containerId).lengte;
        if (futureSlot+containerLengte < slots.size()){
            Slot s = slots.get(futureSlot);
            for (int i = 0; i < containerLengte; i++) {    // check all slots for container
                if (s.x+i < yard[0].length){
                    Stack<Integer> stack = yard[s.y][s.x+i];
                    if(yard[s.y][s.x+i].size() > 0 &&
                            containers.get(stack.peek()) != null &&                             // if there is no container, then there is no problem
                            !checkContainerLower(containers.get(stack.peek()),futureSlot) &&    // check stacking constraints
                            yard[s.y][s.x+i].size() < infoFromJSONTarget.maxHeight){            // height can not be larger than the maxheight
                        return false;
                    }
                }
            }
            return true;
        }
       return false;
    }

    private static void setContainer(Container c, int futureSlot) {
        if(!Objects.equals(allTargetAssignments.assignment.get(c.id), assignments.assignment.get(c.id))){
            c.setHoogte(yard[slots.get(futureSlot).y][slots.get(futureSlot).x].size());
            assignments.assignment.put(c.id,futureSlot);
            for (int i = 0; i < c.lengte; i++) {
                yard[slots.get(futureSlot).y][slots.get(futureSlot).x+i].push(c.id);
            }
        }
    }

    private static boolean checkContainerLower(Container container, int futureSlot) {
        // kijken of de container eronder kan gebruikt worden op op te stapelen
        int somLengtes = 0;
        int slotHoogte = yard[slots.get(futureSlot).y][slots.get(futureSlot).x].size();
        Stack<Integer> slotStack = new Stack<>();
        int containerLengte = container.lengte;
        ArrayList<Integer> idContainers = new ArrayList<>();
        if (slots.get(futureSlot).x+containerLengte < yard[0].length){
            Slot s = slots.get(futureSlot);
            for (int i = 0; i < containerLengte; i++) {
                if(slotHoogte != yard[s.y][s.x+i].size()){
                    return false;
                }
                slotStack.addAll(yard[s.y][s.x+i]);
//            System.out.println("slot stack: " + slotStack);
                if(slotStack.size()>0){
                    Container idC = containers.get(slotStack.pop());
                    while(idC.hoogte != yard[s.y][s.x+i].size() &&
                            slotStack.size()>=yard[s.y][s.x+i].size() &&
                            slotStack.size()>0){
                        idC = containers.get(slotStack.pop());
                    }
                    if(!idContainers.contains(idC.id)){
                        idContainers.add(idC.id);
                    }
                }
            }
            if(slotHoogte != 0){
                for (Integer idContainer : idContainers) {
                    somLengtes += containers.get(idContainer).getLengte();
//                System.out.println("somLengtes: " + somLengtes);
                    if (somLengtes != container.lengte) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private static Container getUpperContainer(int idContainer) {
        int c = idContainer;
        Slot s = slots.get(assignments.assignment.get(idContainer));
        if(yard[s.y][s.x].size()>0){
            for (int i = 0; i < containers.get(c).lengte; i++) {
                c = yard[s.y][s.x+i].pop();
            }
        }
        return containers.get(c);
    }

    private static Container peekUpperContainer(int idContainer) {
        int c = idContainer;
        Slot s = slots.get(assignments.assignment.get(idContainer));
        if(yard[s.y][s.x].size()>0){
            for (int i = 0; i < containers.get(c).lengte; i++) {
                c = yard[s.y][s.x+i].peek();
            }
        }
        return containers.get(c);
    }

    private static void printMovements() {
        System.out.println("Movements:");
        for (Beweging b: movements ) {
            System.out.println(b);
        }
    }

    private static void setTargetAssignments() {
        targetAssignments.assignment.putAll(allTargetAssignments.assignment);
        for (Map.Entry<Integer,Integer> entry : assignments.assignment.entrySet()) {
            if(Objects.equals(targetAssignments.assignment.get(entry.getKey()), entry.getValue())){
                targetAssignments.assignment.remove(entry.getKey());
            }
        }


//        List<Integer> onlyCrane0 = new LinkedList<>();
//        List<Integer> onlyCrane1 = new LinkedList<>();
//        List<Integer> onlyCrane0WithBorder = new LinkedList<>();
//        List<Integer> onlyCrane1WithBorder = new LinkedList<>();
//        List<Integer> bothCranes = new LinkedList<>();
//        double minCrane0 =              cranes.get(0).xmin;
//        double maxOnlyCrane0 =          cranes.get(1).xmin;
//        double minOnlyCrane1 =          cranes.get(0).xmax;
//        double maxCrane1 =              cranes.get(1).xmax;
//        for (Map.Entry<Integer,Integer> entry : allTargetAssignments.assignment.entrySet()) {
//            if(!Objects.equals(assignments.assignment.get(entry.getKey()), entry.getValue())){
////                targetAssignments.assignment.remove(entry.getKey());
//                if (locationIsBetweenInterval(slots.get(entry.getValue()).x, minCrane0-0.5, maxOnlyCrane0+0.5)
//                && locationIsBetweenInterval(slots.get(assignments.assignment.get(entry.getKey())).x, minCrane0-0.5, maxOnlyCrane0+0.5))
//                    onlyCrane0.add(entry.getKey());
//                else if (locationIsBetweenInterval(slots.get(entry.getValue()).x, minCrane0-0.5, minOnlyCrane1+0.5)
//                && locationIsBetweenInterval(slots.get(assignments.assignment.get(entry.getKey())).x, minCrane0-0.5, minOnlyCrane1+0.5))
//                    onlyCrane0WithBorder.add(entry.getKey());
//                else if (locationIsBetweenInterval(slots.get(entry.getValue()).x, minOnlyCrane1-0.5, maxCrane1+0.5)
//                && locationIsBetweenInterval(slots.get(assignments.assignment.get(entry.getKey())).x, minOnlyCrane1-0.5, maxCrane1+0.5))
//                    onlyCrane1.add(entry.getKey());
//                else if (locationIsBetweenInterval(slots.get(entry.getValue()).x, maxOnlyCrane0-0.5, maxCrane1+0.5)
//                && locationIsBetweenInterval(slots.get(assignments.assignment.get(entry.getKey())).x, maxOnlyCrane0-0.5, maxCrane1+0.5))
//                    onlyCrane1WithBorder.add(entry.getKey());
//                else {
//                    bothCranes.add(entry.getKey());
//                }
//            }
//        }
//        addAssignments(onlyCrane0, onlyCrane1WithBorder);
//        addAssignments(onlyCrane0WithBorder, onlyCrane1);
//        addAssignments(onlyCrane0, onlyCrane1);
//        addRestAssingment(bothCranes);
//        addRestAssingment(onlyCrane0);
//        addRestAssingment(onlyCrane1);
//        addRestAssingment(onlyCrane0WithBorder);
//        addRestAssingment(onlyCrane1WithBorder);
    }
//
//    private static void addRestAssingment(List<Integer> a1) {
//        if (a1.size()>0){
//            for(int key : a1){
//                targetAssignments.assignment.put(key, allTargetAssignments.assignment.get(key));
//                sortedCraneTargetAssignment.add(key);
//            }
//        }
//    }
//
//    private static void addAssignments(List<Integer> a1, List<Integer> a2) {
//        int length = Math.min(a1.size(), a2.size());
//        for (int i = 0; i < length; i++) {
//            int key = a2.iterator().next();
//            targetAssignments.assignment.put(key, allTargetAssignments.assignment.get(key));
//            sortedCraneTargetAssignment.add(key);
//            a2.remove(a2.indexOf(key));
//            key = a1.iterator().next();
//            targetAssignments.assignment.put(key, allTargetAssignments.assignment.get(key));
//            sortedCraneTargetAssignment.add(key);
//            a1.remove(a1.indexOf(key));
//        }
//    }

    private static void printYard() {
        System.out.println(yard.length);
        System.out.println(yard[0].length);
        for (int j = 0; j < yard.length; j++) {
            System.out.print(j+" ");
            for (int k = 0; k < yard[0].length; k++) {
                System.out.print(yard[j][k] + " ");
            }
            System.out.println();
        }
    }
}
