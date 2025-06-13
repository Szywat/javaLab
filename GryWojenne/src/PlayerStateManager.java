import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PlayerStateManager {
    private static final String SAVE_DIRECTORY = "player_saves";

    // Initialize the save directory
    static {
        File directory = new File(SAVE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    // Save a player's state to disk (legacy method, kept for compatibility)
    public static void savePlayerState(General general) {
        String fileName = SAVE_DIRECTORY + "\\" + general.getName() + ".save";

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            // Create a serializable player state object
            PlayerState state = new PlayerState(
                general.getName(),
                general.getMoney(),
                convertSoldiersToSerializable(general.getArmy().getSoldiers())
            );

            // Write the state to disk
            oos.writeObject(state);
            System.out.println("Zapisano stan gracza " + general.getName() + " do pliku.");
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania stanu gracza: " + e.getMessage());
        }
    }

    // Generate a consistent filename for two players regardless of order
    private static String generateSaveFileName(String player1Name, String player2Name) {
        // Sort player names alphabetically to ensure consistent filename regardless of player order
        if (player1Name.compareToIgnoreCase(player2Name) <= 0) {
            return player1Name + "_vs_" + player2Name + ".save";
        } else {
            return player2Name + "_vs_" + player1Name + ".save";
        }
    }

    // Save the entire game state to disk
    public static void saveGameState(General player1, General player2, General currentPlayer) {
        String defaultFileName = generateSaveFileName(player1.getName(), player2.getName());
        String filePath = SAVE_DIRECTORY + "\\" + defaultFileName;

        // Check for existing save files with different player order
        File saveFile = new File(filePath);
        if (saveFile.exists()) {
            System.out.println("Nadpisywanie istniejącego pliku zapisu dla graczy: " + player1.getName() + " i " + player2.getName());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            // Create a serializable game state object
            GameState state = new GameState(
                new PlayerState(
                    player1.getName(),
                    player1.getMoney(),
                    convertSoldiersToSerializable(player1.getArmy().getSoldiers())
                ),
                new PlayerState(
                    player2.getName(),
                    player2.getMoney(),
                    convertSoldiersToSerializable(player2.getArmy().getSoldiers())
                ),
                currentPlayer.getName()
            );

            // Write the state to disk
            oos.writeObject(state);
            System.out.println("Zapisano stan gry do pliku: " + defaultFileName);
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania stanu gry: " + e.getMessage());
        }
    }

    // Load a player's state from disk (legacy method, kept for compatibility)
    public static boolean loadPlayerState(General general) {
        String fileName = SAVE_DIRECTORY + "\\" + general.getName() + ".save";
        File saveFile = new File(fileName);

        if (!saveFile.exists()) {
            System.out.println("Nie znaleziono zapisanego stanu dla gracza " + general.getName());
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            // Read the state from disk
            PlayerState state = (PlayerState) ois.readObject();

            // Update the general's money
            while (general.getMoney() > 0) {
                general.spendMoney(1); // Reduce money to 0
            }
            general.addMoney(state.getMoney());

            // Clear the current army and add the saved soldiers
            general.getArmy().getSoldiers().clear();
            for (SerializableSoldier ss : state.getSoldiers()) {
                general.getArmy().addSoldier(new Soldier(ss.getRank(), ss.getExperience()));
            }

            System.out.println("Wczytano stan gracza " + general.getName() + " z pliku.");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Błąd podczas wczytywania stanu gracza: " + e.getMessage());
            return false;
        }
    }

    // Load a game state from disk
    public static GameState loadGameState() {
        JFileChooser fileChooser = new JFileChooser(new File(SAVE_DIRECTORY));
        fileChooser.setDialogTitle("Wybierz plik zapisu gry");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pliki zapisu (*.save)", "save"));

        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File selectedFile = fileChooser.getSelectedFile();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {
            // Read the state from disk
            GameState state = (GameState) ois.readObject();
            System.out.println("Wczytano stan gry z pliku: " + selectedFile.getName());
            return state;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Błąd podczas wczytywania stanu gry: " + e.getMessage());
            return null;
        }
    }

    // Convert a list of Soldier objects to a list of SerializableSoldier objects
    private static List<SerializableSoldier> convertSoldiersToSerializable(List<Soldier> soldiers) {
        List<SerializableSoldier> serializableSoldiers = new ArrayList<>();
        for (Soldier soldier : soldiers) {
            serializableSoldiers.add(new SerializableSoldier(soldier.getRank(), soldier.getExp()));
        }
        return serializableSoldiers;
    }

    // Inner class to represent a player's state in a serializable format
    public static class PlayerState implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String name;
        private final int money;
        private final List<SerializableSoldier> soldiers;

        public PlayerState(String name, int money, List<SerializableSoldier> soldiers) {
            this.name = name;
            this.money = money;
            this.soldiers = soldiers;
        }

        public String getName() {
            return name;
        }

        public int getMoney() {
            return money;
        }

        public List<SerializableSoldier> getSoldiers() {
            return soldiers;
        }
    }

    // Inner class to represent a soldier in a serializable format
    public static class SerializableSoldier implements Serializable {
        private static final long serialVersionUID = 1L;
        private final int rank;
        private final int experience;

        public SerializableSoldier(int rank, int experience) {
            this.rank = rank;
            this.experience = experience;
        }

        public int getRank() {
            return rank;
        }

        public int getExperience() {
            return experience;
        }
    }

    // Inner class to represent the complete game state in a serializable format
    public static class GameState implements Serializable {
        private static final long serialVersionUID = 1L;
        private final PlayerState player1State;
        private final PlayerState player2State;
        private final String currentPlayerName;

        public GameState(PlayerState player1State, PlayerState player2State, String currentPlayerName) {
            this.player1State = player1State;
            this.player2State = player2State;
            this.currentPlayerName = currentPlayerName;
        }

        public PlayerState getPlayer1State() {
            return player1State;
        }

        public PlayerState getPlayer2State() {
            return player2State;
        }

        public String getCurrentPlayerName() {
            return currentPlayerName;
        }
    }
}
