public class MoneyBag {
    private int balance;

    public MoneyBag(int startingBalance) {
        if (startingBalance < 0) {
            throw new IllegalArgumentException("Balans początkowy nie może być mniejszy od 0!");
        }
        if (startingBalance > GameConfig.MONEY_LIMIT) {
            throw new IllegalArgumentException("Balans początkowy nie może być większy od ustawionego limitu " + GameConfig.MONEY_LIMIT);
        }
        this.balance = startingBalance;
    }

    public int getBalance() {return balance;}
    public void addMoney(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Nie można dodać ujemnej kwoty!");
        }
        balance += amount;
        if (balance > GameConfig.MONEY_LIMIT) {
            balance = GameConfig.MONEY_LIMIT;
        };
    }
    public boolean spendMoney(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Nie można wydać ujemnej kwoty pieniędzy!");
        }
        if (amount <= balance) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }
    @Override
    public String toString() {
        return "Balans: " + balance + "$";
    }
}
