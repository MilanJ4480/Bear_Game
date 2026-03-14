package io.github.MilanJ4480.Bear_Game.pre_alpha;

import java.util.Random;

public class Weights {
    Random rand;

    short[] layersReturn;
    short[] biomes;

    short[][] layers;
    boolean[] background;

    short[] rocks;
    short[] plants;

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

        if (n%4==0 && rand.nextInt(2) == 1) biome = biomes[rand.nextInt(biomes.length)];
        else biome = prev;

        for (int i = 0; i < layersReturn.length-1; i++) layersReturn[i] = layers[i][rand.nextInt(layers[i].length)];
        layersReturn[layersReturn.length-1] = biome;

        return layersReturn;
    }

    public int getGround(short biome){
        if (biome==1) return rand.nextInt(10);
        else return -1;
    }

    public short getRock() {
        rock = (short) rand.nextInt(201);
        if (rock>69) rock = -1;
        else {
            if (rock > 64) rock = (short) rand.nextInt(70);
            if (rock > 64) rock = (short) rand.nextInt(70);
            if (rock > 64) rock = (short) rand.nextInt(70);
        }
        return rock;
    }

    public int getPlants(short biome) {
        if (biome == 1) return 0;
        else if (biome == 3) {
            if (randBool()) return 7;
            else {
                if (randBool()) return 6;
                else return 5;
            }
        }
        else return rand.nextInt(9);
    }
    public int getPlantNum(short biome) {
        if (biome == 1) return rand.nextInt(16-2)+2;
        else if  (biome == 3) return rand.nextInt(21-10)+10;
        else return rand.nextInt(5);
    }

    public float randX(float x, float w) {return x+ rand.nextFloat()*w;}
    public boolean randBool() {return rand.nextBoolean();}
    public short randScale() {return (short) rand.nextInt(5); }
}
