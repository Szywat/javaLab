public class Rank {
    private int value;
    public Rank(int value) {
        if (value < 1 || value > 4) {
            throw new IllegalArgumentException("Rank musi być w zakresie 1-4.");
        }
        this.value = value;
    }

    public int getRank() {return value;}
    public void addValue() {this.value += 1;}
    public String getName() {
        if (value == 1) {
            return "Szeregowy";
        }
        if (value == 2) {
            return "Kapral";
        }
        if (value == 3) {
            return "Kapitan";
        }
        if (value == 4) {
            return "Major";
        } else {
            throw new Error("Nieznana ranga");
        }
    };

}
