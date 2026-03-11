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

    public float getFloor(){
        return h*16;
    }

    Chunk(int n, int h, long seed, int[][] levels){
        this.h = h;
        this.seed = seed;
        Random rand = new Random(seed+n);
        layers =  new short[h];
        for (int i = 0; i < h; i++){
            layers[i] = (short) levels[i][rand.nextInt(levels[i].length)];
        }
        w = 160;
        x = w*n;
        y = 0;
    }

    public float getX(){ return x; }
    public float getY(){ return y; }
}
