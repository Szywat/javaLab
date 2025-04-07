package pojava.lab1.zadA;

import java.io.PrintStream;

public class Hello {
    public static void main(String[] args) {
        System.out.println("Witaj!");
        if (args.length == 0) {
            System.out.println("Error: brak argumentów");
            return;
        }
        String myString = args[0];
        int myInt = Integer.parseInt(myString);
                for (int i = 1; i <= myInt; i++) {
                    System.out.println(i);
                }
    }
}
