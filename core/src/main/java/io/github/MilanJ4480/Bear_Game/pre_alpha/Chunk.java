package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.Random;

public class Chunk {
    float x;
    float y;
    int h;
    long seed;
    short[] layers;
    float w;
    short biome;

    public float getFloor(){return h*16;}

    Chunk(int n, int h, short biome, Weights weight){
        this.h = h;
        this.seed = seed;
        Random rand = new Random(seed+n);
        layers = weight.getLayers(biome, n);
        this.biome = layers[layers.length-1];

        w = 160;
        x = w*n;
        y = 0;
    }

    public float getX(){ return x; }
    public float getY(){ return y; }
}
