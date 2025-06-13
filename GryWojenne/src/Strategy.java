import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Strategy  {
    private static Secretary secretary;

    public static void setSecretary(Secretary secretary) {
        Strategy.secretary = secretary;
    }
    public static boolean performManeuver(General general, List<Soldier> soldiers) {
        int totalCost = 0;

        for (Soldier soldier : soldiers) {
            totalCost += soldier.getRank();
        }

        // Log maneuver attempt
        if (secretary != null) {
            secretary.logMessage("Generał " + general.getName() + " wykonuje manewr zwiadowczy z " +
                               soldiers.size() + " żołnierzami za " + totalCost + "$");
        }

        if (!general.spendMoney(totalCost)) {
            if (secretary != null) {
                secretary.logMessage("Manewr zakończony niepowodzeniem - brak środków.");
            }

            return false;
        }

        for (Soldier soldier : soldiers) {
            soldier.addExp(1);
        }
        return true;
    }

    public static void attackEnemy(General attacker, General enemy) {
        Army attackerArmy = attacker.getArmy();
        Army enemyArmy = enemy.getArmy();

        int attackerStrength = attackerArmy.calculateTotalStrength();
        int enemyStrength = enemyArmy.calculateTotalStrength();

        // Log battle details
        if (secretary != null) {
            secretary.logMessage("Szczegóły bitwy: " +
                               attacker.getName() + " (siła: " + attackerStrength + ") vs " +
                               enemy.getName() + " (siła: " + enemyStrength + ")");
        }

        if (attackerStrength > enemyStrength) {
            handleVictory(attacker, enemy);
        } else if (attackerStrength < enemyStrength) {
            handleVictory(enemy, attacker);
        } else {
            handleDraw(attacker, enemy);
        }
    }

    public static void handleVictory(General winner, General loser) {
        int moneyTaken = (int) (loser.getMoney() * 0.1);
        loser.spendMoney(moneyTaken);
        winner.addMoney(moneyTaken);

        // Log detailed battle outcome
        if (secretary != null) {
            secretary.logMessage("Wynik bitwy: Generał " + winner.getName() +
                               " pokonał Generała " + loser.getName() +
                               " i zabrał " + moneyTaken + "$.");
        }

        for (Soldier soldier : winner.getArmy().getSoldiers()) {
            soldier.addExp(1);
        }
        for (Soldier soldier : loser.getArmy().getSoldiers()) {
            soldier.subExp(1);
        }

        // Count soldiers before removal
        int soldiersBefore = loser.getArmy().getSoldiers().size();

        loser.getArmy().removeDeadSoldiers();

        // Count soldiers after removal
        int soldiersAfter = loser.getArmy().getSoldiers().size();
        int soldiersDied = soldiersBefore - soldiersAfter;

        // Log soldier deaths if any
        if (secretary != null && soldiersDied > 0) {
            secretary.logMessage("Straty: Generał " + loser.getName() +
                               " stracił " + soldiersDied + " żołnierzy w bitwie.");
        }
    }

    public static void handleDraw(General general1, General general2) {
        if (secretary != null) {
            secretary.logMessage("Wynik bitwy: Remis pomiędzy Generałem " +
                               general1.getName() + " a Generałem " + general2.getName());
        }

        Random random = new Random();
        Soldier sacrificed1 = null;
        Soldier sacrificed2 = null;

        if (!general1.getArmy().getSoldiers().isEmpty()) {
            int index = random.nextInt(general1.getArmy().getSoldiers().size());
            sacrificed1 = general1.getArmy().getSoldiers().get(index);
            int soldierRank = sacrificed1.getRank();
            int soldierExp = sacrificed1.getExp();

            // Log sacrifice before removing
            if (secretary != null) {
                secretary.logMessage("Poświęcenie: Generał " + general1.getName() +
                                   " poświęca żołnierza z rangą " + soldierRank +
                                   " i doświadczeniem równym " + soldierExp);
            }

            general1.getArmy().getSoldiers().remove(index);
        }

        if (!general2.getArmy().getSoldiers().isEmpty()) {
            int index = random.nextInt(general2.getArmy().getSoldiers().size());
            sacrificed2 = general2.getArmy().getSoldiers().get(index);
            int soldierRank = sacrificed2.getRank();
            int soldierExp = sacrificed2.getExp();

            // Log sacrifice before removing
            if (secretary != null) {
                secretary.logMessage("Poświęcenie: Generał " + general2.getName() +
                                   " poświęca żołnierza z rangą " + soldierRank +
                                   " i doświadczeniem równym " + soldierExp);
            }

            general2.getArmy().getSoldiers().remove(index);
        }
    }

    public static void buySoldier(General general, Rank rank, int number) {
        int totalCost = 10 * rank.getRank() * number;

        // Log purchase attempt
        if (secretary != null) {
            secretary.logMessage("Zakup: Generał " + general.getName() +
                               " kupuje " + number + " żołnierzy z rangą " +
                               rank.getRank() + " za " + totalCost + "$");
        }

        if (!general.spendMoney(totalCost)) {
            if (secretary != null) {
                secretary.logMessage("Zakup żołnierzy nieudany - brak środków.");
            }
            return;
        }

        for (int i = 0; i < number; i++) {
            general.getArmy().addSoldier(new Soldier(rank.getRank(), 1));
        }
    }
}
