package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;

public class EntityManager {
    private Array<Deer> deer;
    Texture deerTexture;
    Texture leader;

    public EntityManager(){
        deer = new Array<>();
        deerTexture = new Texture("deer.png");
        leader = new Texture("leader.png");
    }

    public void spawn(float x, float y, int biome){
        float[] deerX = new  float[6];
        deer.add(new Deer(leader, x, y, 0.25f, 0.25f, 5, deerX, 0));
        for(int i=1; i<5; i++) {
            deer.add(new Deer(deerTexture, x, y, 0.1f, 0.1f, 5, deerX, i));
        }
    }

    public void update(float delta, float floor, boolean attack, float playerX, Polygon swipe){
        for(int i=0; i<5; i++){
            deer.get(i).update(delta, floor, attack, playerX, swipe);
        }
    }
    public void render(Batch batch){
        for(int i=0; i<5; i++){
            deer.get(i).render(batch);
        }
    }

    public void dispose(){
        leader.dispose();
        deerTexture.dispose();
    }
}
