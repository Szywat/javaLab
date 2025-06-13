import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Secretary {
    private static final String LOG_FILE = "game_log.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Initialize the log file
    public Secretary() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, false))) {
            writer.println("=== DZIENNIK ZDARZEŃ ===");
            writer.println("Zaczęto: " + LocalDateTime.now().format(formatter));
            writer.println("================");
        } catch (IOException e) {
            System.err.println("Error przt inicjalizacji dziennika: " + e.getMessage());
        }
    }

    // Log a general message
    public void logMessage(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write("[" + LocalDateTime.now().format(formatter) + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error przy zapisywaniu do dziennika: " + e.getMessage());
        }
    }

    // Log a maneuver action (only summary information)
    public void logManeuver(General general, List<Soldier> soldiers) {
        StringBuilder sb = new StringBuilder();
        sb.append("MANEWR: Generał ").append(general.getName())
          .append(" wysłał ").append(soldiers.size()).append(" żołnierzy na zwiady.");

        logMessage(sb.toString());
    }

    // Log an attack action
    public void logAttack(General attacker, General defender, boolean victory, int moneyTaken) {
        StringBuilder sb = new StringBuilder();
        sb.append("ATAK: Generał ").append(attacker.getName())
          .append(" zaatakował Generała ").append(defender.getName());

        if (victory) {
            sb.append("\n  - WYNIK: Zwycięża Generał").append(attacker.getName())
              .append(", zabrał ").append(moneyTaken).append("$ Generałowi ").append(defender.getName());
        } else if (moneyTaken == 0) {
            sb.append("\n  - WYNIK: Remis, obaj generałowie poświęcają żołnierza.");
        } else {
            sb.append("\n  - WYNIK: Przegrywa Generał ").append(attacker.getName())
              .append(", oddał ").append(moneyTaken).append("$ Generałowi ").append(defender.getName());
        }

        logMessage(sb.toString());
    }

    // Log buying soldiers
    public void logBuySoldiers(General general, int rank, int number, int totalCost) {
        StringBuilder sb = new StringBuilder();
        sb.append("ZAKUP: Generał ").append(general.getName())
          .append(" zakupił ").append(number).append(" żołnierzy o stopniu ").append(rank)
          .append(" za ").append(totalCost).append("$");

        logMessage(sb.toString());
    }

    // Log turn change
    public void logTurnChange(General current, General other) {
        logMessage("ZMIANA TURY: Generał " + other.getName() + " -> Generał " + current.getName());
    }

    // Log game end
    public void logGameEnd(General winner, General loser, String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append("KONIEC GRY: Generał ").append(winner.getName())
          .append(" pokonuje Generała ").append(loser.getName())
          .append("\n  - ").append(reason);

        logMessage(sb.toString());
    }

    // Log army status (only summary information)
    public void logArmyStatus(General general) {
        StringBuilder sb = new StringBuilder();
        sb.append("STATUS ARMII: Generał ").append(general.getName())
          .append(" ma ").append(general.getArmy().getSoldiers().size()).append(" żołnierzy")
          .append(" oraz ").append(general.getMoney()).append("$");

        logMessage(sb.toString());
    }

    // Read the log file and return its contents
    public String readLog() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error przy odczytywaniu dziennika: " + e.getMessage());
            return "Error przy odczytywaniu dziennika: " + e.getMessage();
        }
        return content.toString();
    }
}
