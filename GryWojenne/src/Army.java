import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Army {
    private List<Soldier> soldiers;

    // Constructor initializes an empty army
    public Army() {
        soldiers = new ArrayList<>();
    }

    // Add a soldier to the army
    public void addSoldier(Soldier soldier) {
        soldiers.add(soldier);
    }


    // Calculate total strength of the army
    public int calculateTotalStrength() {
        return soldiers.stream().mapToInt(Soldier::getStrength).sum();
    }

    // Check if the army is empty (no soldiers left)
    public void removeDeadSoldiers() {
            soldiers.removeIf(soldier -> !soldier.isAlive());
    }

    public List<Soldier> getSoldiers() {
        return soldiers;
    }

    @Override
    public String toString() {
        return "Armia:\n" +
                IntStream.range(0, soldiers.size())
                        .mapToObj(i -> (i + 1) + ". Ranga: " + soldiers.get(i).rank.getName() + ", Doświadczenie: " + soldiers.get(i).getExp())
                        .collect(Collectors.joining("\n"));
    }

}