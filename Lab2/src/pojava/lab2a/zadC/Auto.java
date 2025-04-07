package pojava.lab2a.zadC;

import java.util.Random;

public class Auto {
    protected float[] przebieg;

    public Auto() {
        przebieg = new float[12];
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            przebieg[i] = 500 + random.nextFloat() * 1500;
        }
    }

    public float srPrzebieg() {
        float suma = 0;
        for (float km : przebieg) {
            suma += km;
        }
        return suma / przebieg.length;
    }

}

