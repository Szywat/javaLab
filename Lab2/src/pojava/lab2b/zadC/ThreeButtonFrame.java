package pojava.lab2b.zadC;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ThreeButtonFrame extends JFrame {
    private int a = 0;

    public ThreeButtonFrame() throws HeadlessException {
        this.setSize(640,480);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Stworzenie panelu
        JPanel panel = new JPanel();
        this.add(panel);

        // Przycisk dodawania do counter
        JLabel counter = new JLabel("Licznik: "+a);
        panel.add(counter);
        JButton additionButton = new JButton("+1");

        additionButton.addActionListener(e -> { a += 1; counter.setText("Licznik: "+a); });
        panel.add(additionButton);

        // Przycisk odejmowania od counter
        JButton subtractionButton = new JButton("-1");

        subtractionButton.addActionListener(e -> {a -= 1; counter.setText("Licznik: "+a);});
        panel.add(subtractionButton);

        // Przycisk wyjścia
        JButton exitButton = new JButton("Wyjdź");

        exitButton.addActionListener(e -> { System.exit(0);});
        panel.add(exitButton);

        // Zmiana labelu
        JLabel label = new JLabel("Label");

        JButton labelButton = new JButton("Zmień label");
        labelButton.addActionListener(e -> {Random random = new Random(); label.setText(String.valueOf(random.nextInt(100)));});

        panel.add(label);
        panel.add(labelButton);
        panel.setLayout(new GridLayout(2,3));
    }

    public static void main(String[] args) {
        ThreeButtonFrame frame = new ThreeButtonFrame();
        frame.setVisible(true);
    }
}
