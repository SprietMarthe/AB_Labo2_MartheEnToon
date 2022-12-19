import java.awt.*;
import java.awt.Container;
import java.util.Stack;
import javax.swing.*;

public class ContainerClassUI extends JPanel{
    static Stack<Integer>[][] y;
    static int breedte = 15;
    static Color[] colors;
    static Graphics graph;

//    public ContainerClassUI(Stack<Integer>[][] yard) {
//        super("Yard");
//        y=yard;
//        colors = new Color[5];
//        colors[0] = Color.BLACK;
//        colors[1] = Color.RED;
//        colors[2] = Color.ORANGE;
//        colors[3] = Color.YELLOW;
//        colors[4] = Color.GREEN;
//
//        JScrollPane scrollPane = new JScrollPane(this);
//        add(scrollPane, BorderLayout.CENTER);
//        pack();
//        getContentPane().setBackground(Color.WHITE);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        setVisible(true);
//    }

    void drawRectangles() throws InterruptedException {
        Graphics2D g2d = (Graphics2D) graph;
        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < y[0].length; j++) {
                g2d.setColor(colors[y[i][j].size()]);
                g2d.fillRect(j*breedte + j*2,i*breedte + i*2, breedte, breedte);
                repaint();
            }
        }
//        Thread.sleep(2000);

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

    @Override
    public Dimension getPreferredSize() {
        int b = y[0].length*breedte + y[0].length*2;
        int l = y.length*breedte + y.length*2;
        return new Dimension(b, l);
    }

    public static void main(Stack<Integer>[][] yard) {
        y=yard;
        colors = new Color[7];
        colors[0] = Color.BLACK;
        colors[1] = Color.RED;
        colors[2] = Color.ORANGE;
        colors[3] = Color.YELLOW;
        colors[4] = Color.GREEN;
        colors[5] = Color.BLUE;
        colors[6] = Color.PINK;

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                ContainerClassUI ui = new ContainerClassUI();
                JFrame frame = new JFrame();
                JScrollPane scrollPane = new JScrollPane(ui);
                frame.add(scrollPane);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            }
        });
    }
}
