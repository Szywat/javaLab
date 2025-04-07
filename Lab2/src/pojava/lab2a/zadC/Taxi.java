package pojava.lab2a.zadC;

import java.util.Random;

public class Taxi extends Auto {
    private float[] zarobki;

    public Taxi() {
        super(); // Wywołanie konstruktora klasy Auto
        zarobki = new float[12];
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            zarobki[i] = 3000 + random.nextFloat() * 7000; // Losowe wartości od 3000 do 10000
        }
    }

    public float srZarobki() {
        float suma = 0;
        for (float zarobek : zarobki) {
            suma += zarobek;
        }
        return suma / zarobki.length;
    }

    public static void main(String[] args) {
        Taxi taxi = new Taxi();
        System.out.println("Średni przebieg: " + taxi.srPrzebieg() + " km");
        System.out.println("Średnie zarobki: " + taxi.srZarobki() + " zł");
    }
}
