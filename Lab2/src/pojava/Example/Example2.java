package pojava.Example;

import pojava.lab2b.zadA.CloseableFrame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Example2 {
    public static void main(String[] args) {
        CloseableFrame frame = new CloseableFrame();
        JButton button1 = new JButton("Przycisk 1");
        frame.add(button1, BorderLayout.PAGE_START);

        JButton button2 = new JButton("Przycisk 2");
        frame.add(button2, BorderLayout.PAGE_END);

        JLabel label = new JLabel("To jest etykieta");
        frame.add(label);

        JTextField field = new JTextField("A to pole tekstowe");
        frame.add(field);

        frame.setLayout(new FlowLayout()); // Ustawia wszystkie elementy obok siebie
        frame.getContentPane().setBackground(Color.blue);

//        frame.setLayout(new GridLayout(2, 2)); // Tworzy siatkę wypełniającą całą ramkę
        frame.setVisible(true);
    }
}
