import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

public class Main extends Canvas{
    static Map<Integer,Container> containers;
    static Map<Integer, Slot> slots;
    static Map<Integer, Assignment> assignments;
    static int[][] yard = new int[10][10];
    public static void main(String[] args){
        containers = new HashMap<>();
        slots = new HashMap<>();
        assignments = new HashMap<>();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ContainerClassUI(yard).setVisible(true);
            }
        });

        ReadJSON.ReadJSONFile("JSON\\Terminal_4_3.json", containers, slots, assignments,yard);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //showFrame();
        /*System.out.println(containers);
        System.out.println(slots);
        System.out.println(assignments);*/


    }



    private void draw(Graphics g) {
        g.drawString("Hello",40,40);
        setBackground(Color.WHITE);
        g.fillRect(130, 30,100, 80);
        g.drawOval(30,130,50, 60);
        setForeground(Color.RED);
        g.fillOval(130,130,50, 60);
        g.drawArc(30, 200, 40,50,90,60);
        g.fillArc(30, 130, 40,50,180,40);

    }



    // begin movements

    //ArrayList<Verplaatsing> Verplaatsingen = new ArrayList<>();

    /*public int berekenTravelTime (){

        return 0;
    }*/

    // puntje 4: is er een conflict, zoja doe iets
}
