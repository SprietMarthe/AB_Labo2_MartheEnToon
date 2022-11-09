import java.awt.*;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

public class ContainerClassUI extends JFrame{
    static int[][] y;
    static int breedte = 50;
    static Color[] colors;

    public ContainerClassUI(int[][] yard) {
        super("Rectangles Drawing Demo");
        y=yard;
        colors = new Color[5];
        colors[0] = Color.BLACK;
        colors[1] = Color.RED;
        colors[2] = Color.ORANGE;
        colors[3] = Color.YELLOW;
        colors[4] = Color.GREEN;

        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(yard.length*breedte,yard[0].length*breedte+30);
        setVisible(true);

    }

    void drawRectangles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLUE);

        g2d.fillRect(0, 30, 50, 50);
        g2d.setColor(Color.RED);
        g2d.drawRect(30, 50, 420, 120);
        // code to draw rectangles goes here...

        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < y[0].length; j++) {
                g2d.setColor(colors[y[i][j]]);
                g2d.fillRect(i*breedte, j*breedte + 30, breedte, breedte);
            }
        }

    }

    public void paint(Graphics g) {
        super.paint(g);
        drawRectangles(g);

        //repaint();
    }
}
