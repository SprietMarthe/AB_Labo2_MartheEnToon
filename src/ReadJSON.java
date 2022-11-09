import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ReadJSON {
    public static void ReadJSONFile(String jsonFile, Map<Integer, Container> containers, Map<Integer, Slot> slots, Map<Integer, Assignment> assignments, int[][] yard) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(jsonFile));
            JSONObject jsonObject = (JSONObject)obj;

            // Slots
            List<JSONObject> slotsOBJ = (List<JSONObject>) jsonObject.get("slots");
            Iterator<JSONObject> slotIt = slotsOBJ.iterator();
            while (slotIt.hasNext()) {
                //Container c = new Container();
                JSONObject jo = (JSONObject) slotIt.next();
                long id = (long) jo.get("id");
                long x = (long) jo.get("x");
                long y = (long) jo.get("y");

                Slot s = new Slot((int)id,(int) x,(int) y);
                slots.put((int)id, s);
            }

            // containers
            List<JSONObject> containersObj = (List<JSONObject>) jsonObject.get("containers");
            Iterator<JSONObject> iterator = containersObj.iterator();

            while (iterator.hasNext()) {
            //Container c = new Container();
                JSONObject jo = (JSONObject) iterator.next();
                long id = (long) jo.get("id");
                long lengte = (long) jo.get("length");

                Container c = new Container((int)id,(int) lengte, -1);
                containers.put((int)id, c);
            }




            //assignments
            List<JSONObject> assignmentsObj = (List<JSONObject>) jsonObject.get("assignments");
            Iterator<JSONObject> iterator2 = assignmentsObj.iterator();

            while (iterator2.hasNext()) {
                JSONObject jo = (JSONObject) iterator2.next();
                JSONArray ja = (JSONArray) jo.get("slot_id");
                int[] array = new int[ja.size()];
                for (int i=0; i<ja.size();i++){
                    long s = (long) ja.get(i);
                    array[i] = (int) s;
                    yard[slots.get(array[i]).getX()][slots.get(array[i]).getY()]++;
                    /*System.out.println("yard: ");
                    for (int j = 0; j < yard.length; j++) {
                        for (int k = 0; k < yard[0].length; k++) {
                            System.out.print ( yard[j][k] + " ");
                        }
                        System.out.println();
                    }*/
                }
                long cont_id = (long) jo.get("container_id");


                Assignment a = new Assignment(array, (int)cont_id);
                int hoogte = yard[slots.get(array[0]).getX()][slots.get(array[0]).getY()];
                //System.out.println("hoogte: " + hoogte);
                containers.get((int)cont_id).setHoogte(hoogte);

                //System.out.println(a.toString());
                assignments.put((int)cont_id, a);

            }


        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
