package io.github.MilanJ4480.Bear_Game.pre_alpha;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import java.util.Random;

public class Enemy{
    private Sprite entity;
    private Polygon hitbox;
    public Random rand;

    private int health;
    private boolean hit;

    public float x;
    public float y;

    private float g;
    private float v;
    private float s;

    private boolean d;


    public Enemy(Texture texture, float x, float y, float w, float h, int health) {
        this.entity = new Sprite(texture);
        this.x = x;
        this.y = y;
        this.g = -1000;
        this.v=0;
        s=100;
        this.health = health;



        rand = new Random(MathUtils.random(1, Long.MAX_VALUE));

        float[] vertices = new float[] {
            0, 0,
            entity.getWidth(), 0,
            entity.getWidth(), entity.getHeight(),
            0, entity.getHeight()
        };

        hitbox = new Polygon(vertices);
        hitbox.setPosition(x, y);

        entity.setPosition(x, y);
        entity.setSize(entity.getWidth()*w, entity.getHeight()*h);
    }

    public float getWidth() { return hitbox.getBoundingRectangle().getWidth(); }
    public float getHeight() { return hitbox.getBoundingRectangle().getHeight(); }
    public float getX() { return x; }
    public float getY() { return y; }

    public void playerContact(Polygon swipe){
        if(!hit && Intersector.overlapConvexPolygons(hitbox, swipe)) health -= 1;
        hit = true;
    }

    public void move(float delta, float playerX){

        if(rand.nextInt(51)==1) d=!d;

        if(playerX-x<100 && playerX-x>0){
            d=false;
        }
        else if(x-playerX<100 && x-playerX>0){
            d=true;
        }
        if(playerX-x<50 && playerX-x>0){
            s=125;
        }
        else if(x-playerX<50 && x-playerX>0){
            s=125;
        }
        else s=50;
        if(d) x+=delta*s;
        else x-=delta*s;
    }

    private void gravity(float delta, float floor){
        if (y<=floor){
            y = floor;
        }
        else{
            v += g*delta;
            y += v*delta;
            if (y<floor) y = floor;
        }
    }

    public void update(float delta, float floor, boolean attack, float playerX, Polygon swipe){
        if(health>0){
            move(delta, playerX);
            gravity(delta, floor);
            hitbox.setPosition(x, y);
            entity.setPosition(x, y);
            if(attack) {
                playerContact(swipe);
            }
            else hit = false;
        }
    }

    public void render(Batch batch) {
        if (health>0) entity.draw(batch);
    }

}
