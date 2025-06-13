import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame {
    private JPanel mainPanel;
    private JButton playButton;
    private JButton continueButton;
    private JButton rulesButton;
    private JButton quitButton;
    private Secretary secretary;

    public GameGUI() {
        setTitle("Gry Wojenne");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Gry Wojenne");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titlePanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        playButton = new JButton("Zacznij rozgrywkę!");
        continueButton = new JButton("Kontynuuj grę z pliku zapisu!");
        rulesButton = new JButton("Przeczytaj zasady!");
        quitButton = new JButton("Zamknij program");

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continueGame();
            }
        });

        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRules();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(playButton);
        buttonPanel.add(continueButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(quitButton);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void startNewGame() {
        JTextField player1Field = new JTextField();
        JTextField player2Field = new JTextField();

        Object[] message = {
            "Imię pierwszego generała:", player1Field,
            "Imię drugiego generała:", player2Field
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nowa gra", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String player1Name = player1Field.getText().trim();
            String player2Name = player2Field.getText().trim();

            if (player1Name.isEmpty() || player2Name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Imiona generałów nie mogą być puste!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            secretary = new Secretary();

            Strategy.setSecretary(secretary);

            General player1 = new General(player1Name, 100);
            General player2 = new General(player2Name, 100);

            for (int i = 0; i < GameConfig.STARTING_ARMY; i++) {
                player1.getArmy().addSoldier(new Soldier(1, 1));
                player2.getArmy().addSoldier(new Soldier(1, 1));
            }

            secretary.logMessage("Game started with generals: " + player1Name + " and " + player2Name);
            secretary.logMessage("Starting army size: " + GameConfig.STARTING_ARMY);
            secretary.logArmyStatus(player1);
            secretary.logArmyStatus(player2);

            openGameWindow(player1, player2);
        }
    }

    private void continueGame() {
        // Initialize the secretary
        secretary = new Secretary();
        Strategy.setSecretary(secretary);

        // Load game state from file
        PlayerStateManager.GameState gameState = PlayerStateManager.loadGameState();

        if (gameState == null) {
            // User canceled or error occurred
            return;
        }

        // Extract player states
        PlayerStateManager.PlayerState player1State = gameState.getPlayer1State();
        PlayerStateManager.PlayerState player2State = gameState.getPlayer2State();

        // Create generals with loaded states
        General player1 = new General(player1State.getName(), 0);
        General player2 = new General(player2State.getName(), 0);

        // Add money to generals
        player1.addMoney(player1State.getMoney());
        player2.addMoney(player2State.getMoney());

        // Add soldiers to generals' armies
        for (PlayerStateManager.SerializableSoldier ss : player1State.getSoldiers()) {
            player1.getArmy().addSoldier(new Soldier(ss.getRank(), ss.getExperience()));
        }

        for (PlayerStateManager.SerializableSoldier ss : player2State.getSoldiers()) {
            player2.getArmy().addSoldier(new Soldier(ss.getRank(), ss.getExperience()));
        }

        secretary.logMessage("Game continued with generals: " + player1.getName() + " and " + player2.getName());
        secretary.logArmyStatus(player1);
        secretary.logArmyStatus(player2);

        // Determine which player should go first based on saved state
        General currentPlayer = player1;
        General otherPlayer = player2;
        if (player2.getName().equals(gameState.getCurrentPlayerName())) {
            currentPlayer = player2;
            otherPlayer = player1;
        }

        openGameWindow(currentPlayer, otherPlayer);
    }

    private void showRules() {
        String rules = "Każdy z generałów (graczy) dowodzi swoją armią.\n" +
                "Celem gry jest pokonanie przeciwnika poprzez wyszkolenie trzech żołnierzy do rangi Major\n" +
                "lub doprowadzenie przeciwnika do bankructwa. Generałowie mogą wykonywać akcje, tj.\n\n" +
                "Manewr zwiadowczy - wyszkala wybranych żołnierzy podnosząc ich doświadczenie o 1pkt.\n" +
                "Atak na wroga - wygrany przejmuje 10% banku przeciwnika i podnosi doświadczenie swoich żołnierzy o 1pkt,\n" +
                "natomiast żołnierze armii przegranego tracą po 1pkt doświadczenia.\n" +
                "Zakup żołnierzy - zakup i wyszkolenie żołnierzy. Koszt zależy od ilości jak i rangi nabytych żołnierzy\n" +
                "Żołnierze pokonani, których doświadczenie spada do 0pkt giną.";

        JTextArea textArea = new JTextArea(rules);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Zasady gry", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openGameWindow(General player1, General player2) {
        setVisible(false);

        GameWindow gameWindow = new GameWindow(player1, player2, secretary, this);
        gameWindow.setVisible(true);
    }

    public void showMainMenu() {
        setVisible(true);
    }
}
