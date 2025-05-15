public class Rank {
    private final int value;
    public Rank(int value) {
        if (value < 1 || value > 4) {
            throw new IllegalArgumentException("Rank musi być w zakresie 1-4.");
        }
        this.value = value;
    }

    public int getValue() {return value;}
    public String getRank() {
        if (value == 1) return "Szeregowy ( "+ value +" stopień )";
        if (value == 2) return "Kapral ( "+ value +" stopień )";
        if (value == 3) return "Kapitan ( "+ value +" stopień )";
        if (value == 4) return "Major ( "+ value +" stopień )";
        throw new IllegalArgumentException("Stopień powinien znajdować się w zakresie 1-4!");
    }

}
