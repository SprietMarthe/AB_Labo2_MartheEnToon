import java.awt.*;
import java.awt.Container;
import java.util.Stack;
import javax.swing.*;

public class ContainerClassUI extends JFrame{
    static Stack<Integer>[][] y;
    static int breedte = 20;
    static Color[] colors;
    static Graphics graph;
    JScrollPane scrollPane;

    public ContainerClassUI(Stack<Integer>[][] yard) {
        super("Yard");
        y=yard;
        colors = new Color[5];
        colors[0] = Color.BLACK;
        colors[1] = Color.RED;
        colors[2] = Color.ORANGE;
        colors[3] = Color.YELLOW;
        colors[4] = Color.GREEN;

        scrollPane = new JScrollPane();
        getContentPane().add(scrollPane);
        pack();

        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        int xwaarde = y.length*breedte+30;
        int ywaarde = y[0].length*breedte+2;
        setSize(ywaarde,xwaarde);
        setVisible(true);
    }

    void drawRectangles() throws InterruptedException {
        Graphics2D g2d = (Graphics2D) graph;
        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < y[0].length; j++) {
                g2d.setColor(colors[y[i][j].size()]);
                g2d.fillRect(j*breedte + 2, i*breedte + 30, breedte, breedte);
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
