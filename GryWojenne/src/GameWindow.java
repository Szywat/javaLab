import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameWindow extends JFrame {
    private General player1;
    private General player2;
    private General currentPlayer;
    private General otherPlayer;
    private Secretary secretary;
    private GameGUI mainMenu;

    private JPanel mainPanel;
    private JPanel playerInfoPanel;
    private JPanel actionPanel;
    private JPanel logPanel;

    private JLabel currentPlayerLabel;
    private JLabel moneyLabel;
    private JTextArea armyTextArea;
    private JTextArea logTextArea;

    private JButton maneuverButton;
    private JButton attackButton;
    private JButton buySoldiersButton;
    private JButton showArmyButton;
    private JButton showLogButton;
    private JButton saveButton;
    private JButton surrenderButton;

    public GameWindow(General player1, General player2, Secretary secretary, GameGUI mainMenu) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.otherPlayer = player2;
        this.secretary = secretary;
        this.mainMenu = mainMenu;

        setTitle("Gry Wojenne - Rozgrywka");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        createPlayerInfoPanel();

        createActionPanel();

        createLogPanel();

        mainPanel.add(playerInfoPanel, BorderLayout.NORTH);
        mainPanel.add(actionPanel, BorderLayout.CENTER);
        mainPanel.add(logPanel, BorderLayout.SOUTH);

        add(mainPanel);

        checkWinningConditions();

        updateUI();
    }

    private void createPlayerInfoPanel() {
        playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new BorderLayout());
        playerInfoPanel.setBorder(BorderFactory.createTitledBorder("Informacje o graczu"));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 1));

        currentPlayerLabel = new JLabel();
        moneyLabel = new JLabel();

        infoPanel.add(currentPlayerLabel);
        infoPanel.add(moneyLabel);

        armyTextArea = new JTextArea();
        armyTextArea.setEditable(false);
        JScrollPane armyScrollPane = new JScrollPane(armyTextArea);
        armyScrollPane.setPreferredSize(new Dimension(400, 150));

        playerInfoPanel.add(infoPanel, BorderLayout.NORTH);
        playerInfoPanel.add(armyScrollPane, BorderLayout.CENTER);
    }

    private void createActionPanel() {
        actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(7, 1, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Akcje"));

        maneuverButton = new JButton("Wykonaj manewr zwiadowczy");
        attackButton = new JButton("Zaatakuj wroga");
        buySoldiersButton = new JButton("Kup żołnierzy");
        saveButton = new JButton("Zapisz grę");
        surrenderButton = new JButton("Poddaj się");

        maneuverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performManeuver();
            }
        });

        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attackEnemy();
            }
        });

        buySoldiersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buySoldiers();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePlayerState();
            }
        });

        surrenderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                surrender();
            }
        });

        actionPanel.add(maneuverButton);
        actionPanel.add(attackButton);
        actionPanel.add(buySoldiersButton);
        actionPanel.add(saveButton);
        actionPanel.add(surrenderButton);
    }

    private void createLogPanel() {
        logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Ostatnie wydarzenia"));

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        logScrollPane.setPreferredSize(new Dimension(400, 150));

        logPanel.add(logScrollPane, BorderLayout.CENTER);
    }

    private void updateUI() {
        currentPlayerLabel.setText("Tura generała: " + currentPlayer.getName());
        moneyLabel.setText("Pieniądze: " + currentPlayer.getMoney() + "$");

        armyTextArea.setText(getArmyDisplay(currentPlayer));

        String logContent = secretary.readLog();
        String[] logLines = logContent.split("\n");

        StringBuilder logBuilder = new StringBuilder();
        int startLine = Math.max(0, logLines.length - 10);
        for (int i = startLine; i < logLines.length; i++) {
            logBuilder.append(logLines[i]).append("\n");
        }

        logTextArea.setText(logBuilder.toString());
    }

    private String getArmyDisplay(General general) {
        StringBuilder sb = new StringBuilder();
        sb.append("Armia generała ").append(general.getName()).append(":\n");

        List<Soldier> soldiers = general.getArmy().getSoldiers();
        for (int i = 0; i < soldiers.size(); i++) {
            Soldier soldier = soldiers.get(i);
            sb.append(i + 1).append(". Ranga: ").append(soldier.rank.getName())
              .append(", Doświadczenie: ").append(soldier.getExp()).append("\n");
        }

        return sb.toString();
    }

    private void performManeuver() {
        List<Soldier> soldiers = currentPlayer.getArmy().getSoldiers();
        if (soldiers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nie masz żadnych żołnierzy!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<JCheckBox> checkBoxes = new ArrayList<>();
        for (int i = 0; i < soldiers.size(); i++) {
            Soldier soldier = soldiers.get(i);
            JCheckBox checkBox = new JCheckBox((i + 1) + ". Ranga: " + soldier.rank.getName() + 
                                              ", Doświadczenie: " + soldier.getExp());
            checkBoxes.add(checkBox);
            panel.add(checkBox);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Wybierz żołnierzy do manewru", 
                                                  JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            List<Soldier> selectedSoldiers = new ArrayList<>();

            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isSelected()) {
                    selectedSoldiers.add(soldiers.get(i));
                }
            }

            if (selectedSoldiers.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nie wybrano żadnych żołnierzy!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean maneuverSuccess = Strategy.performManeuver(currentPlayer, selectedSoldiers);

            if (maneuverSuccess) {
                secretary.logManeuver(currentPlayer, selectedSoldiers);

                if (!checkWinningConditions()) {
                    switchTurns();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Niewystarczająca kwota na wykonanie tego manewru!", 
                                             "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void attackEnemy() {
        int result = JOptionPane.showConfirmDialog(this, 
                                                  "Czy na pewno chcesz zaatakować generała " + otherPlayer.getName() + "?", 
                                                  "Atak", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            secretary.logMessage("General " + currentPlayer.getName() + " is attacking General " + otherPlayer.getName());

            int currentMoneyBefore = currentPlayer.getMoney();

            Strategy.attackEnemy(currentPlayer, otherPlayer);

            int currentMoneyAfter = currentPlayer.getMoney();
            int moneyDifference = currentMoneyAfter - currentMoneyBefore;

            if (moneyDifference > 0) {
                secretary.logAttack(currentPlayer, otherPlayer, true, moneyDifference);
            } else if (moneyDifference < 0) {
                secretary.logAttack(currentPlayer, otherPlayer, false, -moneyDifference);
            } else {
                secretary.logAttack(currentPlayer, otherPlayer, false, 0);
            }

            if (!checkWinningConditions()) {
                switchTurns();
            }
        }
    }

    private void buySoldiers() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 5, 5));

        JLabel rankLabel = new JLabel("Ranga (1-4):");
        JSpinner rankSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));

        JLabel numberLabel = new JLabel("Liczba żołnierzy:");
        JSpinner numberSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        panel.add(rankLabel);
        panel.add(rankSpinner);
        panel.add(numberLabel);
        panel.add(numberSpinner);

        int result = JOptionPane.showConfirmDialog(this, panel, "Kup żołnierzy", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int rank = (int) rankSpinner.getValue();
            int number = (int) numberSpinner.getValue();

            int totalCost = 10 * rank * number;

            secretary.logMessage("General " + currentPlayer.getName() + " is attempting to buy " + 
                               number + " soldiers of rank " + rank + " for " + totalCost + "$");

            int moneyBefore = currentPlayer.getMoney();

            Strategy.buySoldier(currentPlayer, new Rank(rank), number);

            if (currentPlayer.getMoney() < moneyBefore) {
                secretary.logBuySoldiers(currentPlayer, rank, number, totalCost);

                if (!checkWinningConditions()) {
                    switchTurns();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Nie udało się zakupić żołnierzy. Niewystarczająca ilość pieniędzy.", 
                                             "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void savePlayerState() {
        // Save the entire game state instead of just the current player
        PlayerStateManager.saveGameState(player1, player2, currentPlayer);
        JOptionPane.showMessageDialog(this, 
                                     "Zapisano stan gry z graczami " + player1.getName() + " i " + player2.getName() + ".", 
                                     "Informacja", JOptionPane.INFORMATION_MESSAGE);
    }

    private void surrender() {
        int result = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz się poddać?", 
                                                 "Poddaj się", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            secretary.logGameEnd(otherPlayer, currentPlayer, "Player surrender");

            JOptionPane.showMessageDialog(this, "Generał " + currentPlayer.getName() + " poddał się!\n" + 
                                         "Generał " + otherPlayer.getName() + " wygrywa!", 
                                         "Koniec gry", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            mainMenu.showMainMenu();
        }
    }

    private boolean checkWinningConditions() {
        if (otherPlayer.getArmy().getSoldiers().isEmpty() && otherPlayer.getMoney() < 10) {
            secretary.logGameEnd(currentPlayer, otherPlayer, "Opponent has 0 soldiers and less than 10 money");

            JOptionPane.showMessageDialog(this, "Generał " + otherPlayer.getName() + 
                                         " nie ma żołnierzy i ma mniej niż 10 pieniędzy!\n" + 
                                         "Generał " + currentPlayer.getName() + " wygrywa!", 
                                         "Koniec gry", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            mainMenu.showMainMenu();

            return true;
        }

        long rank4Count = currentPlayer.getArmy().getSoldiers().stream()
                .filter(soldier -> soldier.getRank() == 4)
                .count();

        if (rank4Count >= 3) {
            secretary.logGameEnd(currentPlayer, otherPlayer, "Player has 3 or more soldiers with rank 4 (Major)");

            JOptionPane.showMessageDialog(this, "Generał " + currentPlayer.getName() + 
                                         " ma 3 żołnierzy rangi Major!\n" + 
                                         "Generał " + currentPlayer.getName() + " wygrywa!", 
                                         "Koniec gry", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            mainMenu.showMainMenu();

            return true;
        }

        return false;
    }

    private void switchTurns() {
        General temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;

        secretary.logTurnChange(currentPlayer, otherPlayer);

        updateUI();

        JOptionPane.showMessageDialog(this, "Tura przechodzi do generała " + currentPlayer.getName(), 
                                     "Zmiana tury", JOptionPane.INFORMATION_MESSAGE);
    }
}
