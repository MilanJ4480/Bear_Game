package io.github.MilanJ4480.Bear_Game.pre_alpha;

public class Chunk {
    float x;
    float y;
    int h;
    float w;
    short biome;

    short[] layers;


    short[] rocks;
    float[] rocksX;
    boolean[] rockFace;
    short[] rockScale;

    short[] plants;
    float[] plantsX;
    boolean[] plantFace;

    public float getFloor(){return h*16;}

    Chunk(int n, int h, short biome, Weights weight){
        this.h = h;
        layers = weight.getLayers(biome, n);
        this.biome = layers[layers.length-1];

        w = 160;
        x = w*n;
        y = 0;

        rocks = new short[3];
        rocksX = new float[3];
        rockFace = new boolean[3];
        rockScale = new short[3];
        for(int i = 0; i < 3; i++){
            rocks[i] = (weight.getRock());
            rocksX[i] = weight.randX(x, w);
            rockFace[i] = weight.randBool();
            rockScale[i] = weight.randScale();
            rockScale[i] = 2;
        }
        plants = new short[weight.getPlantNum(this.biome)];
        plantsX = new float[plants.length];
        plantFace = new boolean[plants.length];
        for (int i = 0; i < plants.length; i++){
            plants[i] = (short) weight.getPlants(this.biome);
            plantsX[i] = weight.randX(x, w);
            plantFace[i] = weight.randBool();
        }

    }

    public float getX(){ return x; }
    public float getY(){ return y; }
}
