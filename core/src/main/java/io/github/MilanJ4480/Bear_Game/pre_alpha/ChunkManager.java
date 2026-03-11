package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class ChunkManager {
    TextureAtlas atlas;
    TextureRegion[] layers;
    long seed;
    Array<Chunk> chunks;
    float d;
    int[][] weight;

    public ChunkManager(){
        atlas = new TextureAtlas("layersAtlas/layers.atlas");
        seed = MathUtils.random(1, Long.MAX_VALUE);
        layers =  new TextureRegion[11];
        for(int i = 0; i < layers.length; i++){
            layers[i] = new TextureRegion(atlas.findRegion("ground"+(i+1)));
        }
        chunks = new Array<>();
//        weight = new int[10][10];
        weight = new int[][]{
            new int[]{10},
            new int[]{10},
            new int[]{9},
            new int[]{8},
            new int[]{8},
            new int[]{8},
            new int[]{7},
            new int[]{6},
            new int[]{5},
            new int[]{4},
            new int[]{0, 1, 2, 3},
        };
        for(int i = 0; i < 10; i++) {
            chunks.add(new Chunk(i, 11, seed, weight));
        }
    }
    public float getFloor(float x, float w, float y){
        for (Chunk chunk : chunks){
            if (chunk.getFloor()>y && (x>=chunk.x && x<=chunk.x+chunk.w || x+w>=chunk.x && x+w<=chunk.x+chunk.w)){
                return chunk.getFloor()-1;
            }
        }
        return 0;
    }
    public void update(float playerX){
        if (Math.abs(playerX)+640>chunks.get(chunks.size-1).x){
            System.out.println("Chunk Generated");
            chunks.add(new Chunk(chunks.size, 11, seed, weight));
        }
    }

    public void render(Batch batch, float playerX){
        for (Chunk chunk : chunks) if (Math.abs(playerX-chunk.getX())<=640) {
            for(int i = 0; i < chunk.layers.length; i++) {
                batch.draw(layers[chunk.layers[i]], chunk.x, chunk.y+(16*i), chunk.w, 16);
            }
        }
    }

    public void dispose(){
        atlas.dispose();
    }
}
