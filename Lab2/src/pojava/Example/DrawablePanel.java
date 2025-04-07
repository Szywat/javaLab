package pojava.Example;

import pojava.lab2b.zadA.CloseableFrame;

import javax.swing.*;
import java.awt.*;

public class DrawablePanel extends JPanel {

    public void paintComponents(Graphics g) {
        super.paintComponents(g);

        g.setColor(Color.red);
        g.fillRect(50, 50, 150, 100);

        g.setColor(Color.blue);
        g.fillOval(250, 250, 150, 150);
    }

    public static void main(String[] args) {
        CloseableFrame frame = new CloseableFrame();
        DrawablePanel panel = new DrawablePanel();

//        panel.setBackground(Color.blue);
        frame.add(panel);

        frame.setVisible(true);
    }
}
