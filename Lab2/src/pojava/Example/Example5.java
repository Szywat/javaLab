package pojava.Example;

import pojava.lab2b.zadA.CloseableFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Example5 {
    public static void main(String[] args) {
        CloseableFrame frame = new CloseableFrame();
        JPanel panel = new JPanel();
        frame.add(panel);
        JButton exitButton = new JButton("Zakończ");
        ActionListener exitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        };
        exitButton.addActionListener(exitListener);
        panel.add(exitButton);

        frame.setVisible(true);
    }
}
