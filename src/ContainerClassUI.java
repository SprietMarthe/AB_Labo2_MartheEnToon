import java.awt.*;
import java.util.Stack;
import javax.swing.JFrame;

public class ContainerClassUI extends JFrame{
    static Stack<Integer>[][] y;
    static int breedte = 50;
    static Color[] colors;
    static Graphics graph;

    public ContainerClassUI(Stack<Integer>[][] yard) {
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

    void drawRectangles() throws InterruptedException {
        Graphics2D g2d = (Graphics2D) graph;

        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < y[0].length; j++) {
                g2d.setColor(colors[y[i][j].size()]);
                g2d.fillRect(i*breedte, j*breedte + 30, breedte, breedte);
            }
        }
        Thread.sleep(2000);
        repaint();

    }

    public void paint(Graphics g) {
        graph = g;
        super.paint(g);
        try {
            drawRectangles();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
