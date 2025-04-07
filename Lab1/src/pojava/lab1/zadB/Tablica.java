package pojava.lab1.zadB;

import java.util.Arrays;

public class Tablica {
    public static void main(String[] args) {
        String[] table = new String[4];
//        String[] table;
//        table = new String[4]
        if (args.length < 4) {
            System.out.println("Error: Podano za mało argumentów");
            return;
        }
        for (int i = 0; i <= table.length-1; i++) {
            table[i] = args[i];
        }
        System.out.println(Arrays.toString(table));
    }
}
