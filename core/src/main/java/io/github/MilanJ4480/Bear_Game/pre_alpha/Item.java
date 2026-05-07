package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;

public class Item {
    private Sprite item;
    private Polygon hitbox;
    private Intersector.MinimumTranslationVector mtv;


    private float x;
    private float y;
    private float w;
    private float h;

    private float g;
    private float v;

    private float floor;

    private  boolean held;
    private  boolean face;

    public Item(TextureRegion texture, float x, float y) {
        this.item = new Sprite(texture);

        float[] vertices = new float[] {
            0, 0,
            item.getWidth(), 0,
            item.getWidth(), item.getHeight(),
            0, item.getHeight()
        };
        this.hitbox = new Polygon(vertices);

        this.x = x;
        this.y = y;
        this.w = item.getWidth();
        this.h = item.getHeight();

        this.g = -1000;
        this.v=0;
        face=true;
    }

    public float getX() { return x; }
    public float getW() { return w; }
    public float getY() { return y; }

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

    private void rotate(float delta, Vector3 mouse){
        float a = item.getRotation() % 360;
        float b = MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y-y) , (mouse.x-x));

        float diff = a-b;

        if (diff>180) diff -= 360;
        if (diff<-180) diff += 360;

        if(Math.abs(a-b) > 5) {
            item.rotate(delta*Math.signum(diff));
        }
    }

    public void update(float delta, float f, float playerX, Polygon swipeBox, Vector3 mouse) {
        if((Intersector.overlapConvexPolygons(hitbox, swipeBox) && Gdx.input.isKeyJustPressed(Input.Keys.E)) || held){
            if(held && Gdx.input.isKeyJustPressed(Input.Keys.E)) held=false;
            else held=true;

            if(playerX>swipeBox.getX()) {
                x = swipeBox.getX()-swipeBox.getBoundingRectangle().getWidth();
                face=true;
                item.setOrigin(w, h);
                rotate(delta*240, mouse);
            }
            else {
                x = swipeBox.getX()+swipeBox.getBoundingRectangle().getWidth();
                face=false;
                item.setOrigin(0, 0);
                rotate(delta*-240, mouse);
            }
            y = swipeBox.getY()+swipeBox.getBoundingRectangle().getHeight();
        }
        else {
            gravity(delta, f);
            item.setRotation(0);
        }

        if (face && item.isFlipX()) item.flip(true, false);
        else if  (!face && !item.isFlipX()) item.flip(true, false);

        item.setPosition(x, y);
        hitbox.setPosition(x, y);
    }

    public void render(Batch batch) {
        item.draw(batch);
    }
}
