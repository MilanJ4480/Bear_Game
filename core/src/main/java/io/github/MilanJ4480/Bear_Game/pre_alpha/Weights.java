package io.github.MilanJ4480.Bear_Game.pre_alpha;

import java.util.Random;

public class Weights {
    Random rand;

    short[] layersReturn;
    short[] biomes;

    short[][] layers;
    boolean[] background;

    short[] rocks;
    short[] trees;

    short biome;
    short rock;

    public Weights(long seed){
        rand = new Random(seed);

        biome = 0;

        layers = new short[][]{
            new short[]{10},
            new short[]{9, 10, 10, 10, 10, 10, 10, 10, 10, 10},
            new short[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 10},
            new short[]{8, 8, 8, 8, 8, 8, 8, 8, 8, 9},
            new short[]{8},
            new short[]{8, 8, 8, 8, 8, 8, 8, 7},
            new short[]{6, 7, 7, 7, 7, 7, 7, 7},
            new short[]{5, 6, 6, 6, 6, 6, 6, 7},
            new short[]{4, 5, 5, 5, 5, 5, 5, 6},
            new short[]{4},
            new short[]{0}
        };
        biomes = new short[]{0, 1, 2, 3};
    }

    public short[] getLayers(short prev, int n) {
        layersReturn = new short[11];

        if (rand.nextInt(6) == 1) biome = biomes[rand.nextInt(biomes.length)];
        else biome = prev;

        for (int i = 0; i < layersReturn.length-1; i++) layersReturn[i] = layers[i][rand.nextInt(layers[i].length)];
        layersReturn[layersReturn.length-1] = biome;

        return layersReturn;
    }

    public short getRocks(boolean prev) {
        if(prev){
            rock = (short) rand.nextInt(201);
            if (rock>69) rock = -1;
            else if (rock>65) rock = (short) rand.nextInt(201);
        }
        else{
            rock = (short) rand.nextInt(70);
            if (rock>65) rock = (short) rand.nextInt(70);
        }

        return rock;
    }
}
