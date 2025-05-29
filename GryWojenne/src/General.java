public class General {
    private final String name;
    private Army army;
    private MoneyBag moneyBag;

    public General(String name, int startingBalance) {
        this.name = name;
        this.army = new Army();
        this.moneyBag = new MoneyBag(startingBalance);
    }

    public String getName() {return name;}
    public Army getArmy() {return army;}
    public int getMoney() {return moneyBag.getBalance();}
    public boolean spendMoney(int amount) {return moneyBag.spendMoney(amount);}
    public void addMoney(int amount) {moneyBag.addMoney(amount);}
}
