package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class ChunkManager {
    TextureAtlas atlas;
    TextureAtlas alpineAtlas;
    TextureRegion[] rocks;
    TextureRegion[] layers;
    TextureRegion[] plants;
    TextureRegion[][] alpine;
    long seed;

    Weights weight;

    Array<Chunk> chunks;
    short biome;

    public ChunkManager(){
        atlas = new TextureAtlas("atlas/atlas.atlas");
        alpineAtlas = new TextureAtlas("alpineAtlas/alpine.atlas");
        seed = MathUtils.random(1, Long.MAX_VALUE);

        layers = new TextureRegion[12];
        for(int i = 0; i < layers.length-1; i++){
            layers[i] = new TextureRegion(atlas.findRegion("ground"+(i+1)));
        }
        layers[11] = new TextureRegion(alpineAtlas.findRegion("alpine_layer"));

        alpine = new TextureRegion[6][];
        alpine[0] = new TextureRegion[2]; //alpine_fir
        alpine[1] = new TextureRegion[3]; //alpine_fir_shrub
        alpine[2] = new TextureRegion[6]; //alpine_flower
        alpine[3] = new TextureRegion[9]; //alpine_grass
        alpine[4] = new TextureRegion[5]; //alpine_rock
        alpine[5] = new TextureRegion[3]; //alpine_tile

        for (int i=0; i<alpine[0].length; i++){
            alpine[0][i] = new TextureRegion(alpineAtlas.findRegion("alpine_fir" + (i+1)));
        }

        for (int i=0; i<alpine[1].length; i++){
            alpine[1][i] = new TextureRegion(alpineAtlas.findRegion("alpine_fir_shrub" + (i+1)));
        }

        for (int i=0; i<alpine[2].length; i++){
            alpine[2][i] = new TextureRegion(alpineAtlas.findRegion("alpine_flower" + (i+1)));
        }

        for (int i=0; i<alpine[3].length; i++){
            alpine[3][i] = new TextureRegion(alpineAtlas.findRegion("alpine_grass" + (i+1)));
        }

        for (int i=0; i<alpine[4].length; i++){
            alpine[4][i] = new TextureRegion(alpineAtlas.findRegion("alpine_rock" + (i+1)));
        }

        for (int i=0; i<alpine[5].length; i++){
            alpine[5][i] = new TextureRegion(alpineAtlas.findRegion("alpine_tile" + (i+1)));
        }


        rocks = new TextureRegion[69];
        for(int i = 0; i < rocks.length; i++){
            rocks[i] = new TextureRegion(atlas.findRegion("rock"+(i+1)));
        }

        plants = new TextureRegion[9];
        for(int i = 0; i < plants.length; i++){
            plants[i] = new TextureRegion(atlas.findRegion("plants"+(i+1)));
        }

        chunks = new Array<>();
//        weight = new int[10][10];
        weight = new Weights(seed);
        biome = 0;
        chunks.add(new Chunk(chunks.size, 11, biome, weight));
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
            chunks.add(new Chunk(chunks.size, 11, biome, weight));
            biome = chunks.get(chunks.size-1).biome;
        }
    }

    public void render(Batch batch, float playerX){
        for (Chunk chunk : chunks) if (Math.abs(playerX-chunk.getX())<=640) {
            for(int i = 0; i < chunk.layers.length; i++) {
                batch.draw(layers[chunk.layers[i]], chunk.x, chunk.y + (16 * i), chunk.w, 16);
            }
        }
    }

    public void renderTrees(Batch batch, float playerX){
        for (Chunk chunk : chunks) if (Math.abs(playerX-chunk.getX())<=640) {
            if (chunk.biome==4) {
                for(int i = 0; i < chunk.world[0].length; i++) {
                    if(chunk.world[0][i]!=-1) batch.draw(alpine[0][chunk.world[0][i]], chunk.worldCords[0][i], chunk.getFloor());
                }
            }
        }
    }

    public void renderRocks(Batch batch, float playerX){
        for (Chunk chunk : chunks) if (Math.abs(playerX-chunk.getX())<=640) {
            if (chunk.biome==4){
                for(int j = 0; j < chunk.world[4].length; j++){
                    if(chunk.world[4][j]!=-1) batch.draw(alpine[4][chunk.world[4][j]], chunk.worldCords[4][j], chunk.getFloor());
                }
            }
            else {
                for (int j = 0; j < chunk.rocks.length; j++) {
                    if (chunk.rocks[j] >= 0 && chunk.rockFace[j])
                        batch.draw(rocks[chunk.rocks[j]], chunk.rocksX[j], chunk.getFloor() - 1, rocks[chunk.rocks[j]].getRegionWidth() * chunk.rockScale[j], rocks[chunk.rocks[j]].getRegionHeight() * chunk.rockScale[j]);
                    else if (chunk.rocks[j] >= 0 && !chunk.rockFace[j])
                        batch.draw(rocks[chunk.rocks[j]], chunk.rocksX[j], chunk.getFloor() - 1, -rocks[chunk.rocks[j]].getRegionWidth() * chunk.rockScale[j], rocks[chunk.rocks[j]].getRegionHeight() * chunk.rockScale[j]);
                }
            }
        }
    }
    public void renderPlants(Batch batch, float playerX){
        for (Chunk chunk : chunks) if (Math.abs(playerX-chunk.getX())<=640) {
            if (chunk.biome==4){
                for(int j = 0; j < chunk.world[0].length/2; j++) if(chunk.world[0][j]!=-1) batch.draw(alpine[0][chunk.world[0][j]], chunk.worldCords[0][j], chunk.getFloor());
                for(int j = 0; j < chunk.world[3].length/2; j++) if(chunk.world[3][j]!=-1) batch.draw(alpine[3][chunk.world[3][j]], chunk.worldCords[3][j], chunk.getFloor());
                for(int j = 0; j < chunk.world[1].length/2; j++) if(chunk.world[1][j]!=-1) batch.draw(alpine[1][chunk.world[1][j]], chunk.worldCords[1][j], chunk.getFloor());
                for(int j = 0; j < chunk.world[2].length/2; j++) if(chunk.world[2][j]!=-1) batch.draw(alpine[2][chunk.world[2][j]], chunk.worldCords[2][j], chunk.getFloor());
                if(chunk.world[5][0]!=-1) batch.draw(alpine[5][chunk.world[5][0]], chunk.x, chunk.getFloor()-((float) alpine[5][chunk.world[5][0]].getRegionHeight() /2));
            }
            else {
                for (int k = 0; k < chunk.plants.length/2; k++) {
                    if (chunk.plantFace[k])
                        batch.draw(plants[chunk.plants[k]], chunk.plantsX[k], chunk.getFloor(), plants[chunk.plants[k]].getRegionWidth() + (0.25f * k), plants[chunk.plants[k]].getRegionHeight() + (0.25f * k));
                    else
                        batch.draw(plants[chunk.plants[k]], chunk.plantsX[k], chunk.getFloor(), -plants[chunk.plants[k]].getRegionWidth() + (0.25f * k), plants[chunk.plants[k]].getRegionHeight() + (0.25f * k));
                }
            }
        }
    }

    public void renderPlantsFront(Batch batch, float playerX){
        for (Chunk chunk : chunks) if (Math.abs(playerX-chunk.getX())<=640) {
            if (chunk.biome==4){
                for(int j = chunk.world[0].length/2; j < chunk.world[0].length; j++) if(chunk.world[0][j]!=-1) batch.draw(alpine[0][chunk.world[0][j]], chunk.worldCords[0][j], chunk.getFloor());
                for(int j = chunk.world[3].length/2; j < chunk.world[3].length; j++) if(chunk.world[3][j]!=-1) batch.draw(alpine[3][chunk.world[3][j]], chunk.worldCords[3][j], chunk.getFloor());
                for(int j = chunk.world[1].length/2; j < chunk.world[1].length; j++) if(chunk.world[1][j]!=-1) batch.draw(alpine[1][chunk.world[1][j]], chunk.worldCords[1][j], chunk.getFloor());
                for(int j = chunk.world[2].length/2; j < chunk.world[2].length; j++) if(chunk.world[2][j]!=-1) batch.draw(alpine[2][chunk.world[2][j]], chunk.worldCords[2][j], chunk.getFloor());
                if(chunk.world[5][0]!=-1) batch.draw(alpine[5][chunk.world[5][0]], chunk.x, chunk.getFloor()-((float) alpine[5][chunk.world[5][0]].getRegionHeight() /2));
                System.out.println(alpine[5][chunk.world[5][0]].getRegionWidth());
            }
            else {
                for (int k = chunk.plants.length/2; k < chunk.plants.length; k++) {
                    if (chunk.plantFace[k])
                        batch.draw(plants[chunk.plants[k]], chunk.plantsX[k], chunk.getFloor(), plants[chunk.plants[k]].getRegionWidth() + (0.25f * k), plants[chunk.plants[k]].getRegionHeight() + (0.25f * k));
                    else
                        batch.draw(plants[chunk.plants[k]], chunk.plantsX[k], chunk.getFloor(), -plants[chunk.plants[k]].getRegionWidth() + (0.25f * k), plants[chunk.plants[k]].getRegionHeight() + (0.25f * k));
                }
            }
        }
    }

    public void dispose(){
        atlas.dispose();
        alpineAtlas.dispose();
    }
}
