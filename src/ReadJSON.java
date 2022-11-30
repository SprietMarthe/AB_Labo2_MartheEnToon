import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ReadJSON {
    public static Stack<Integer>[][] ReadJSONFile(String jsonFile, Map<Integer, Container> containers, Map<Integer, Slot> slots, Assignments assignments) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(jsonFile));
            JSONObject jsonObject = (JSONObject)obj;

            // Slots
            Stack<Integer>[][] yard;
            int maxX = -1;
            int maxY = -1;
            int minX = 99999;
            int minY = 99999;
            List<JSONObject> slotsOBJ = (List<JSONObject>) jsonObject.get("slots");
            Iterator<JSONObject> slotIt = slotsOBJ.iterator();
            while (slotIt.hasNext()) {
                //Container c = new Container();
                JSONObject jo = (JSONObject) slotIt.next();
                long id = (long) jo.get("id");
                long x = (long) jo.get("x");
                long y = (long) jo.get("y");
                if(maxX<x)
                    maxX=(int) x;
                if(maxY<y)
                    maxY=(int) y;
                if(minX>x)
                    minX=(int) x;
                if(minY>y)
                    minY=(int) y;

                Slot s = new Slot();
                s.setId((int)id);
                s.setX((int) x);
                s.setY((int) y);
                slots.put((int)id, s);
            }
            System.out.println("maxX: " + maxX + " maxY: " + maxY);
            yard = new Stack[maxX+1][maxY+1];
            for (int i = 0; i < yard.length; i++) {
                for (int j = 0; j < yard[0].length; j++) {
                    yard[i][j] = new Stack<>();
                }
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
                long cont_id = (long) jo.get("container_id");
                for (int i=0; i<ja.size();i++){
                    long s = (long) ja.get(i);
                    array[i] = (int) s;
                    //System.out.println("array[i]: " + array[i]);
                    yard[slots.get(array[i]).getX()][slots.get(array[i]).getY()].push((int) cont_id);
//                    System.out.println("yard: ");
//                    System.out.println("yard x size: " + yard.length);
//                    System.out.println("yard y size: " + yard[0].length);
//                    for (int j = 0; j < yard[0].length; j++) {
//                        for (int k = 0; k < yard.length; k++) {
//                            System.out.print(yard[k][j] + " ");
//                        }
//                        System.out.println();
//                    }
                }



                int hoogte = yard[slots.get(array[0]).getX()][slots.get(array[0]).getY()].size();
                //System.out.println("hoogte: " + hoogte);
                containers.get((int)cont_id).setHoogte(hoogte);

                //System.out.println(a.toString());
                assignments.put((int)cont_id, array);

            }
            return yard;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
