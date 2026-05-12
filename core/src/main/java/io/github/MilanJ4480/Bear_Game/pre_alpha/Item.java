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
    private boolean carry;
    private  boolean face;

    public Item(TextureRegion texture, float x, float y) {
        this.item = new Sprite(texture);

        item.setOrigin(0, 0);
        item.flip(true, false);

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
        carry=false;
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
        float b;
        if(face) b = MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y-y) , -Math.abs(mouse.x-x));
        else b = MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y-y) , Math.abs(mouse.x-x));
        //System.out.println(a);
        //System.out.println(b);

        //if (b>90 || b<-90) b -= b-90;

        float diff = a-b;

        if (diff>180) diff -= 360;
        if (diff<-180) diff += 360;

        if(Math.abs(diff) > 5) item.rotate(delta * diff * 5);
    }

    public void update(float delta, float f, Player player, Vector3 mouse) {
        if(carry){
            if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                carry=false;
                if (!item.isFlipX()) item.flip(true, false);
            }
            else {
                x = player.getX();
                y = player.getY() + player.getHeight();
                if (player.getFace() && !item.isFlipX()) item.flip(true, false);
                else if (!player.getFace() && item.isFlipX()) item.flip(true, false);
            }
        }
        else if((Intersector.overlapConvexPolygons(hitbox, player.getSwipeBox()) && Gdx.input.isKeyJustPressed(Input.Keys.E)) || held){
            if(held && Gdx.input.isKeyJustPressed(Input.Keys.E)) held=false;
            else held=true;

            if(player.getFace()) {
                if(!face) item.setRotation(MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y-y) , -Math.abs(mouse.x-x)));
                x = player.getX();//-swipeBox.getBoundingRectangle().getWidth();
                face=true;
                //System.out.println((item.getRotation()+360)%360);
                if(mouse.x>player.getX()+player.getWidth() && ((item.getRotation()+360)%360>0 && (item.getRotation()+360)%360<180)){
                    held=false;
                    carry=true;
                    face=false;
                    item.setRotation(0);
                }
                //item.setOrigin(0, 0);
            }
            else {
                if(face) item.setRotation(MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y-y) , Math.abs(mouse.x-x)));
                x = player.getX()+player.getWidth();
                face=false;
                if(mouse.x<player.getX() && (item.getRotation()%360>0 && item.getRotation()%360<180)) {
                    held=false;
                    carry=true;
                    item.setRotation(0);
                }
                //item.setOrigin(0, 0);
            }
            y = player.getY()+player.getHeight()/2;
            rotate(-delta, mouse);
        }
        else {
            gravity(delta, f);
            //item.setRotation(0);
        }

        if (face && !item.isFlipY()) item.flip(false, true);
        else if  (!face && item.isFlipY()) item.flip(false, true);

        item.setPosition(x, y);
        hitbox.setPosition(x, y);
    }

    public void render(Batch batch) {
        item.draw(batch);
        //batch.draw(item, x, y, w*0.25f, h*0.25f);
    }
}
