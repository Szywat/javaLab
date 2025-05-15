public class Soldier {
    private Rank rank;
    private int exp;
    private int strength;
    private boolean alive;

    private Soldier(Rank rank, int exp) {
        if (exp < 0) throw new IllegalArgumentException("Doświadczenie nie może być ujemne");
        this.rank = rank;
        this.exp = exp;
        this.alive = exp > 0;
    }

    public Rank getRank() {return rank;}
    public int getExp() {return exp;}
    public int getStrength() {return rank.getValue() * exp;}
    public boolean getAlive() {return alive;}
    public void addExp(int n) {
        if (!alive) throw new IllegalArgumentException("Ten żołnierz nie żyje!");
        if (n < 0) throw new IllegalArgumentException("Nie można odejmować doświadczenia!");
        this.exp += n;
    };



}
