import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class H2DatabaseApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("H2 Database App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());

            // Górny panel: JTextField i JButton
            JPanel topPanel = new JPanel(new BorderLayout());
            JTextField queryField = new JTextField();
            JButton executeButton = new JButton("Wykonaj zapytanie");
            topPanel.add(queryField, BorderLayout.CENTER);
            topPanel.add(executeButton, BorderLayout.EAST);

            // Dolny panel: JTextArea do wyświetlania wyniku
            JTextArea resultArea = new JTextArea();
            resultArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(resultArea);

            frame.add(topPanel, BorderLayout.NORTH);
            frame.add(scrollPane, BorderLayout.CENTER);

            // Obsługa wciśnięcia przycisku
            executeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String query = queryField.getText();
                    if (query.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Proszę wpisać zapytanie!", "Błąd", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Łączenie z bazą danych H2 i wykonanie zapytania
                    try (Connection connection = DriverManager.getConnection("jdbc:h2:./data/nazwabazy", "sa", "");
                         Statement statement = connection.createStatement();
                         ResultSet resultSet = statement.executeQuery(query)) {
                        // Wyświetlanie wyników w JTextArea
                        StringBuilder result = new StringBuilder();
                        int columnCount = resultSet.getMetaData().getColumnCount();

                        // Pobieranie nazw kolumn
                        for (int i = 1; i <= columnCount; i++) {
                            result.append(resultSet.getMetaData().getColumnName(i)).append("\t");
                        }
                        result.append("\n");

                        // Pobieranie danych
                        while (resultSet.next()) {
                            for (int i = 1; i <= columnCount; i++) {
                                result.append(resultSet.getString(i)).append("\t");
                            }
                            result.append("\n");
                        }

                        resultArea.setText(result.toString());
                    } catch (SQLException ex) {
                        resultArea.setText("Błąd wykonania zapytania:\n" + ex.getMessage());
                    }
                }
            });

            frame.setVisible(true);
        });
    }
}