import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Strategy  {
    public static boolean performManeuver(General general, List<Soldier> soldiers) {
        int totalCost = 0;

        for (Soldier soldier : soldiers) {
            totalCost += soldier.getRank();
        }

        if (!general.spendMoney(totalCost)) {
            System.out.println("Niewystarczająca kwota na wykonanie tego manewru!");
            return false;
        }
        for (Soldier soldier : soldiers) {
            soldier.addExp(1);
        }
        System.out.println("Wysłano żołnierzy.");
        return true;
    }

    public static void attackEnemy(General attacker, General enemy) {
        Army attackerArmy = attacker.getArmy();
        Army enemyArmy = enemy.getArmy();

        int attackerStrength = attackerArmy.calculateTotalStrength();
        int enemyStrength = enemyArmy.calculateTotalStrength();

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

        for (Soldier soldier : winner.getArmy().getSoldiers()) {
            soldier.addExp(1);
        }
        for (Soldier soldier : loser.getArmy().getSoldiers()) {
            soldier.subExp(1);
        }
        loser.getArmy().removeDeadSoldiers();

        System.out.println("Generał " + winner.getName() + " wygrywa bitwę!");
        System.out.println("Generał " + loser.getName() + " przekazuje " + moneyTaken +"$ Generałowi " + winner.getName());

    }

    public static void handleDraw(General general1, General general2) {
        System.out.println("Bitwa kończy się remisem!");

        Random random = new Random();

        if (!general1.getArmy().getSoldiers().isEmpty()) {
            int index = random.nextInt(general1.getArmy().getSoldiers().size());
            Soldier sacrificed = general1.getArmy().getSoldiers().remove(index);
            System.out.println(general1.getName() + " poświęca " + sacrificed);
        }

        if (!general2.getArmy().getSoldiers().isEmpty()) {
            int index = random.nextInt(general2.getArmy().getSoldiers().size());
            Soldier sacrificed = general2.getArmy().getSoldiers().remove(index);
            System.out.println(general2.getName() + " poświęca " + sacrificed);
        }

    }

    public static void buySoldier(General general, Rank rank, int number) {
        int totalCost = 10 * rank.getRank() * number;

        if (!general.spendMoney(totalCost)) {
            System.out.println("Nie udało się zakupić żołnierzy.");
            return;
        }

        for (int i = 0; i < number; i++) {
            general.getArmy().addSoldier(new Soldier(rank.getRank(), 1));
        }
        System.out.println("Zakupiono żołnierzy!");

    }
}
