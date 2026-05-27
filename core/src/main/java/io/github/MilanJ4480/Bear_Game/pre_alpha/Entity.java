package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */

public class Entity {
    private Sprite entity;
//    private Rectangle hitbox;
    private Polygon hitbox;
    private Intersector.MinimumTranslationVector mtv;

    private float x;
    private float y;
    private float h;
    private float g;
    private float v;
    private float hv;
    private float ha;
    private float floor;
    private float r;
    private float nr;

    private boolean lock;

    public Entity(Texture texture, float x, float y, float w, float h) {
        this.entity = new Sprite(texture);
        this.x = x;
        this.y = y;
        this.g = -1000;
        this.v=0;
        this.hv=0;
        this.ha=0;

        lock = true;

        floor=0;

        entity.setPosition(x, y);
        entity.setSize(entity.getWidth()*w, entity.getHeight()*h);
        float[] vertices = new float[] {
            0, 0,
            entity.getWidth(), 0,
            entity.getWidth(), entity.getHeight(),
            0, entity.getHeight()
            };
        hitbox = new Polygon(vertices);
        hitbox.setPosition(x, y);
        this.h = entity.getHeight();
        mtv = new Intersector.MinimumTranslationVector();

    }

    public Polygon getPolygon() { return hitbox; }
    public void setPolygon(Polygon hitbox) { this.hitbox = hitbox; }
    public float getWidth() { return hitbox.getBoundingRectangle().getWidth(); }
    public float getHeight() { return hitbox.getBoundingRectangle().getHeight(); }
    public float getX() { return x; }
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

    private void horizontalVelocity(float delta, boolean g){
        if (x<=0 || g) ha = 500;
        else ha = 10;
        if(hv<-0.1) hv+=ha*delta;
        else if (hv>0.1) hv-=ha*delta;
        else hv = 0;
        x+=hv*delta;
    }

    private void rotate(float delta){
        if(r>0) {
            entity.rotate(delta*r);
            r+=delta*g;
            if(r<=0) r=-1;
        }
        else if(r<0){
            entity.rotate(delta*r);
            r+=delta*g;
            if((entity.getRotation()<5 && entity.getRotation()>-5) || entity.getRotation()==90) {
                entity.setRotation(0);
                r=0;
            }
        }
    }

    public void update(float delta, float f, Player player) {
        floor = f;
        rotate(delta);
        if(Intersector.overlapConvexPolygons(player.getSwipeBox(), hitbox) && player.getAttack() && player.getY()<hitbox.getY()+h-5){
            lock = false;
            r+=25;
            nr=-r;
        }
        else if (Intersector.overlapConvexPolygons(player.getPolygon(), hitbox, mtv)) {
            if (player.getY() >= hitbox.getY()+h || mtv.normal.y>0.1) { //(player.getLeftX()<hitbox.getX()+w || player.getRightX()-5>hitbox.getX()))){
                player.doGravity(false);
                player.setV(0);
                player.setJump(false);
                lock = true;
//                player.setY(hitbox.getY()+h);
//                System.out.println("On Top");
            }
            else if (mtv.normal.x > 0.1){
                if(lock){
                    player.addX(player.getS()*delta);
                }
                else {
                    x -= mtv.depth;
                    hv = -player.getS();
                }
            }
            else if (mtv.normal.x < 0.1){
                if(lock){
                    player.addX(-player.getS()*delta);
                }
                else {
                    x += mtv.depth;
                    hv = player.getS();
                }
            }
        }
        else{
            player.doGravity(true);
        }
        horizontalVelocity(delta, hitbox.getY()<=floor);
        gravity(delta, floor);

        if (y==floor) v=0;

        if(lock) {
            entity.setPosition(x, y-5);
            hitbox.setPosition(x, y-5);
        }
        else{
            entity.setPosition(x, y);
            hitbox.setPosition(x, y);
        }


    }

    public void render(Batch batch) {
        entity.draw(batch);
    }

    public void dispose() {
//        texture.dispose();
    }

}
