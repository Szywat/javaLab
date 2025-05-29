public class Soldier extends Rank {
    public Rank rank;
    public int experience;
    public int strength;
    public int cost;


    public Soldier(int rankValue, int experience) {
        super(rankValue);
        if (experience < 0) throw new IllegalArgumentException("Doświadczenie nie może być ujemne");
        this.rank = new Rank(rankValue);
        this.experience = experience;
        this.getStrength();
        this.cost = rankValue * 10;
    }

    public int getRank() {return rank.getRank();}
    public int getExp() {return experience;}
    public int getStrength() {return this.strength = rank.getRank() * experience;}
    public boolean isAlive() {return experience > 0;}
    public void addExp(int n) {
        if (n < 0) throw new IllegalArgumentException("Podaj wartość dodatnią!");

        this.experience += n;
        if (this.experience > 5) {
            this.experience = 5;
        }
        if (this.experience == 5 && this.rank.getRank() != 4) {
            this.rank.addValue();
            this.experience = 1;
            System.out.println("Żołnierz awansuje! Jego nowa ranga: " + this.rank.getName());
        }

    }
    public void subExp(int n) {
        if (n < 0) throw new IllegalArgumentException("Podaj wartość dodatnią!");
        this.experience -= n;
    }
}
