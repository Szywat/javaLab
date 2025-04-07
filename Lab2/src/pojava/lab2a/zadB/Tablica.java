package pojava.lab2a.zadB;

import java.util.Arrays;

public class Tablica {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Error: Podano za mało argumentów!");
            return;
        }
        String[] table = new String[4];

        for (int i = 0; i <= args.length-1; i++) {
            table[i] = args[i];
        }

        System.out.println(Arrays.toString(table));
    }
}
