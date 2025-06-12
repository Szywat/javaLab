import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class CurrencyChartApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CurrencyChartApp::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Currency Chart App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel do wyboru dat i waluty
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JLabel currencyLabel = new JLabel("Waluta:");
        String[] currencies = {"usd", "eur", "gbp"};
        JComboBox<String> currencyComboBox = new JComboBox<>(currencies);
        controlPanel.add(currencyLabel);
        controlPanel.add(currencyComboBox);

        JLabel fromLabel = new JLabel("Od:");
        JTextField fromDateField = new JTextField("2000-01-01", 10);
        controlPanel.add(fromLabel);
        controlPanel.add(fromDateField);

        JLabel toLabel = new JLabel("Do:");
        JTextField toDateField = new JTextField("2010-12-31", 10);
        controlPanel.add(toLabel);
        controlPanel.add(toDateField);

        JButton fetchButton = new JButton("Pobierz dane");
        controlPanel.add(fetchButton);

        frame.add(controlPanel, BorderLayout.NORTH);

        // Panel wykresu
        JPanel chartPanel = new JPanel(new BorderLayout());
        frame.add(chartPanel, BorderLayout.CENTER);

        // Obsługa przycisku pobierania
        fetchButton.addActionListener(e -> {
            String selectedCurrency = (String) currencyComboBox.getSelectedItem();
            String fromDate = fromDateField.getText();
            String toDate = toDateField.getText();

            try {
                TimeSeries series = fetchCurrencyData(fromDate, toDate, selectedCurrency);
                JFreeChart chart = createChart(series);
                ChartPanel cp = new ChartPanel(chart);

                chartPanel.removeAll();
                chartPanel.add(cp, BorderLayout.CENTER);
                chartPanel.validate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Błąd podczas pobierania danych: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    /**
     * Pobiera dane walutowe z bazy danych dla wybranego zakresu dat i waluty.
     *
     * @param fromDate  początek zakresu dat (w formacie yyyy-MM-dd)
     * @param toDate    koniec zakresu dat (w formacie yyyy-MM-dd)
     * @param currency  kod waluty (np. "usd", "eur", "gbp")
     * @return TimeSeries z wynikami zapytania
     * @throws SQLException w razie problemu z bazą danych
     */
    private static TimeSeries fetchCurrencyData(String fromDate, String toDate, String currency) throws SQLException {
        TimeSeries series = new TimeSeries("Kurs " + currency.toUpperCase());
        String query = "SELECT data, " + currency + " AS kurs FROM waluty WHERE data BETWEEN ? AND ? ORDER BY data";

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./data/nazwabazy", "sa", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fromDate);
            stmt.setString(2, toDate);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                Date date = resultSet.getDate("data");
                double rate = resultSet.getDouble("kurs");
                series.add(new Day(date), rate);
            }
        }

        return series;
    }

    /**
     * Tworzy wykres na podstawie danych z TimeSeries.
     *
     * @param series dane walutowe
     * @return obiekt JFreeChart
     */
    private static JFreeChart createChart(TimeSeries series) {
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);
        return ChartFactory.createTimeSeriesChart(
                "Kurs waluty",      // Tytuł wykresu
                "Data",             // Oś X
                "Kurs (" + series.getKey() + ")", // Oś Y
                dataset,            // Dane
                true,               // Legenda
                true,               // Tooltipy
                false               // URL
        );
    }
}