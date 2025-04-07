package pojava.lab2b.zadB;

import pojava.lab2b.zadA.CloseableFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ThreeShapesPanel extends JPanel {
    private final Color[] colorTable = new Color[3];

    public ThreeShapesPanel() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            colorTable[i] = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(colorTable[0]);

        // Prostokąt
        g.fillRect(50, 50, 100, 75);

        g.setColor(colorTable[1]);
        // Koło
        g.fillOval(150, 150, 100, 100);

        g.setColor(colorTable[2]);
        // Trójkąt
        int[] xPoints = {50, 100, 150};
        int[] yPoints = {350, 250, 350};
        g.fillPolygon(xPoints, yPoints, 3);
    }

    public static void main(String[] args) {
        CloseableFrame frame = new CloseableFrame();
        frame.setLayout(new GridLayout(1,2));

        ThreeShapesPanel panel1 = new ThreeShapesPanel();
        panel1.setBackground(Color.white);
        frame.add(panel1);

        JPanel panel2 = new JPanel();
        frame.add(panel2);
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
        panel2.add(new JButton("Przycisk 1"));
        panel2.add(new JButton("Przycisk 2"));
        panel2.add(new JLabel("Tekst"));
        panel2.add(new JTextField("Wpisz tekst"));

        frame.setVisible(true);
    }
}
