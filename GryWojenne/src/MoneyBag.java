public class MoneyBag {
    private int amount;

    public MoneyBag(int amount) {
        if (amount < 0 ) {
            throw new IllegalArgumentException("Nie można mieć pieniędzy na minusie!");
        }
        this.amount = amount;
    }
    public int getAmount() {
        return amount;
    }

    public void addAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Nie można dodać ujemnych pieniędzy!");
        }
        this.amount += amount;
    }

    public void subtractAmount(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Nie można odjąć ujemnych pieniędzy!");
        }
        this.amount -= amount;
    }




}
