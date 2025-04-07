package pojava.lab2a.zadA;

public class Hello {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: podano złą ilość argumentów!");
            return;
        }

        System.out.println("Witaj");
        String myString = args[0];
        int myInt = Integer.parseInt(myString);
        for (int i = 1; i <= myInt; i++) {
            System.out.println(i);
        }
    }
}
