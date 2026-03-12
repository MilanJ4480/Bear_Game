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
    TextureRegion[] rocks;
    TextureRegion[] layers;
    long seed;

    Weights weight;

    Array<Chunk> chunks;
    short biome;

    public ChunkManager(){
        atlas = new TextureAtlas("atlas/atlas.atlas");
        seed = MathUtils.random(1, Long.MAX_VALUE);
        layers =  new TextureRegion[11];
        for(int i = 0; i < layers.length; i++){
            layers[i] = new TextureRegion(atlas.findRegion("ground"+(i+1)));
        }
        rocks = new TextureRegion[69];
        for(int i = 0; i < rocks.length; i++){
            rocks[i] = new TextureRegion(atlas.findRegion("rock"+(i+1)));
        }
        chunks = new Array<>();
//        weight = new int[10][10];
        weight = new Weights(seed);
        biome = 0;
        chunks.add(new Chunk(0, 11, biome, weight));
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
            chunks.add(new Chunk(chunks.size, 11, biome, weight));
            biome = chunks.get(chunks.size-1).biome;
        }
    }

    public void render(Batch batch, float playerX){
        for (Chunk chunk : chunks) if (Math.abs(playerX-chunk.getX())<=640) {
            for(int i = 0; i < chunk.layers.length; i++) {
                batch.draw(layers[chunk.layers[i]], chunk.x, chunk.y + (16 * i), chunk.w, 16);
            }
            for(int j = 0; j < chunk.rocks.length; j++){
                if(chunk.rocks[j]>=0 && chunk.rockFace[j]) batch.draw(rocks[chunk.rocks[j]], chunk.rocksX[j], chunk.getFloor(), rocks[chunk.rocks[j]].getRegionWidth()*chunk.rockScale[j], rocks[chunk.rocks[j]].getRegionHeight()*chunk.rockScale[j]);
                else if(chunk.rocks[j]>=0 && !chunk.rockFace[j]) batch.draw(rocks[chunk.rocks[j]], chunk.rocksX[j], chunk.getFloor(), -rocks[chunk.rocks[j]].getRegionWidth()*chunk.rockScale[j], rocks[chunk.rocks[j]].getRegionHeight()*chunk.rockScale[j]);
            }
        }
    }

    public void dispose(){
        atlas.dispose();
    }
}
