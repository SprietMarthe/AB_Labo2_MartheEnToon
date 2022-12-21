import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class JSONClass {
    public static Stack<Integer>[][] ReadJSONFile(String jsonFile, Map<Integer, Container> containers, Map<Integer, Slot> slots, Assignments assignments, Map<Integer, Kraan> kranen,InfoFromJSON infoFromJSON) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(jsonFile));
            JSONObject jsonObject = (JSONObject)obj;

            //name
            String name = (String) jsonObject.get("name");
            //length
            long length = (long) jsonObject.get("length");
            //width
            long width = (long) jsonObject.get("width");
            //maxheight
            long maxHeight = (long) jsonObject.get("maxheight");
            long targetHeight = (long) jsonObject.get("targetheight");
            infoFromJSON.setName(name);
            infoFromJSON.setLength((int) length);
            infoFromJSON.setWidth((int) width);
            infoFromJSON.setMaxHeight((int) maxHeight);
            infoFromJSON.setTargetHeight((int) targetHeight);

            // Slots
            Stack[][] yard;
            List<JSONObject> slotsOBJ = (List<JSONObject>) jsonObject.get("slots");
            Iterator<JSONObject> slotIt = slotsOBJ.iterator();
            while (slotIt.hasNext()) {
                JSONObject jo = slotIt.next();
                long id = (long) jo.get("id");
                long x = (long) jo.get("x");
                long y = (long) jo.get("y");
                Slot s = new Slot();
                s.setId((int)id);
                s.setX((int) x);
                s.setY((int) y);
                slots.put((int)id, s);
            }
            yard = new Stack[(int) width][(int) length];
            for (int i = 0; i < yard.length; i++) {
                for (int j = 0; j < yard[0].length; j++) {
                    yard[i][j] = new Stack<>();
                }
            }

            // containers
            List<JSONObject> containersObj = (List<JSONObject>) jsonObject.get("containers");
            Iterator<JSONObject> iterator = containersObj.iterator();
            while (iterator.hasNext()) {
                JSONObject jo = iterator.next();
                long id = (long) jo.get("id");
                long lengte = (long) jo.get("length");

                Container c = new Container((int)id,(int) lengte, -1);
                containers.put((int)id, c);
            }


            // cranes
            List<JSONObject> cranesObj = (List<JSONObject>) jsonObject.get("cranes");
            Iterator<JSONObject> iteratorCrane = cranesObj.iterator();
            while (iteratorCrane.hasNext()) {
                JSONObject jo = iteratorCrane.next();
                try{
                    double x = Double.parseDouble(String.valueOf((long) jo.get("x")));
                    Object yObj = jo.get("y");
                    double y = 0;
                    if (yObj instanceof Double)
                        y = (double) yObj;
                    if (yObj instanceof Long)
                        y = Double.parseDouble(String.valueOf((long) yObj));
//                    double y = Double.parseDouble(String.valueOf((double) jo.get("y")));
                    long xmin = (long) jo.get("xmin");
                    long ymin = (long) jo.get("ymin");
                    long id = (long) jo.get("id");
                    long xspeed = (long) jo.get("xspeed");
                    long yspeed = (long) jo.get("yspeed");
                    long xmax = (long) jo.get("xmax");
                    long ymax = (long) jo.get("ymax");

                    Kraan k = new Kraan(x,y, xmin, ymin, id, xspeed, yspeed, xmax, ymax);
                    kranen.put((int)id, k);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //assignments
            List<JSONObject> assignmentsObj = (List<JSONObject>) jsonObject.get("assignments");
            Iterator<JSONObject> iterator2 = assignmentsObj.iterator();
            while (iterator2.hasNext()) {
                JSONObject jo = iterator2.next();
                long slotObject = (long) jo.get("slot_id");
                int slotID = (int) slotObject;
                long cont_id = (long) jo.get("container_id");
                try{
                    for (int i = 0; i < containers.get((int) cont_id).lengte; i++) {
                        yard[slots.get(slotID).getY()][slots.get(slotID).getX()+i].push((int) cont_id);
                    }
//                JSONArray ja = (JSONArray) jo.getInteger("slot_id");
//                int[] array = new int[ja.size()];
//                long cont_id = (long) jo.get("container_id");
//                for (int i=0; i<ja.size();i++){
//                    long s = (long) ja.get(i);
//                    array[i] = (int) s;
//                    yard[slots.get(array[i]).getX()][slots.get(array[i]).getY()].push((int) cont_id);
//                }

                    int hoogte = yard[slots.get(slotID).getY()][slots.get(slotID).getX()].size();
                    containers.get((int)cont_id).setHoogte(hoogte);

                    assignments.put((int)cont_id, slotID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return yard;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void ReadJSONTargetFile(String s, Assignments targetAssignments, InfoFromJSON infoFromJSONTarget) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(s));
            JSONObject jsonObject = (JSONObject) obj;
            //name
            String name = (String) jsonObject.get("name");
            //maxHeight
            long maxHeight = (long) jsonObject.get("maxheight");
            infoFromJSONTarget.setName("name");
            infoFromJSONTarget.setMaxHeight((int) maxHeight);


            //assignments
            List<JSONObject> assignmentsObj = (List<JSONObject>) jsonObject.get("assignments");
            Iterator<JSONObject> iterator2 = assignmentsObj.iterator();
            while (iterator2.hasNext()) {
                JSONObject jo = iterator2.next();
                long slotObject = (long) jo.get("slot_id");
                int slotID = (int) slotObject;
                long contObject = (long) jo.get("container_id");
                int cont_id = (int) contObject;

                targetAssignments.put(cont_id, slotID);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("no target yard: start minimizing");
        }

    }

    public static void WriteJSONFile(){

    }


}
