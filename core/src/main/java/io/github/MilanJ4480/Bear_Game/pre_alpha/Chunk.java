package io.github.MilanJ4480.Bear_Game.pre_alpha;

public class Chunk {
    float x;
    float y;
    int h;
    float w;
    short biome;

    short[] layers;


    int[] rocks;
    float[] rocksX;
    boolean[] rockFace;
    short[] rockScale;

    short[] plants;
    float[] plantsX;
    boolean[] plantFace;

    int[][] world;
    float[][] worldCords;

    public float getFloor(){return h*16;}
    public int[][] getWorld() {return world;}

    Chunk(int chunkN, int h, short biome, Weights weight){
        this.h = h;
        layers = weight.getLayers(biome);
        this.biome = layers[layers.length-1];
        w = 128;
        x = w * chunkN;
        y = 0;

        if(this.biome==4){
            world = new int[][] {
                new int[2],
                new int[5],
                new int[6],
                new int[16],
                new int[1],
                {weight.getTile(this.biome)}
            };

            for (int i=0; i<world[0].length; i++){world[0][i] = weight.getTree(this.biome);}
            for (int i=0; i<world[1].length; i++){world[1][i] = weight.getShrub(this.biome);}
            for (int i=0; i<world[2].length; i++){world[2][i] = weight.getFlower(this.biome);}
            for (int i=0; i<world[3].length; i++){world[3][i] = weight.getGrass(this.biome);}
            for (int i=0; i<world[4].length; i++){world[4][i] = weight.getRock(this.biome);}

            worldCords = new float[world.length][];
            for (int i=0; i<worldCords.length; i++){
                worldCords[i] = new float[world[i].length];
                for (int j=0; j<world[i].length; j++){
                    worldCords[i][j] = weight.randX(x, w);
                }
            }
        }
        else {

            rocks = new int[3];
            rocksX = new float[3];
            rockFace = new boolean[3];
            rockScale = new short[3];
            for (int i = 0; i < 3; i++) {
                rocks[i] = (weight.getRock(biome));
                rocksX[i] = weight.randX(x, w);
                rockFace[i] = weight.randBool();
                rockScale[i] = weight.randScale();
                rockScale[i] = 2;
            }
            plants = new short[weight.getPlantNum(this.biome)];
            plantsX = new float[plants.length];
            plantFace = new boolean[plants.length];
            for (int i = 0; i < plants.length; i++) {
                plants[i] = (short) weight.getPlants(this.biome);
                plantsX[i] = weight.randX(x, w);
                plantFace[i] = weight.randBool();
            }
        }
    }

    public float getX(){ return x; }
    public float getY(){ return y; }
}
