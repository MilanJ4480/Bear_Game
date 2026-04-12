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
    private float w;
    private float h;
    private float g;
    private float v;
    private float hv;
    private float ha;
    private float floor;

    public Entity(Texture texture, float x, float y, float w, float h) {
        this.entity = new Sprite(texture);
        this.x = x;
        this.y = y;
        this.g = -1000;
        this.v=0;
        this.hv=0;
        this.ha=0;

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
        this.w = entity.getWidth();
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

    public void update(float delta, float f, Player player, boolean face) {
        floor = f;
        if (Intersector.overlapConvexPolygons(player.getPolygon(), hitbox, mtv)) {
            if (player.getY() >= hitbox.getY()+h || mtv.normal.y>0.1) { //(player.getLeftX()<hitbox.getX()+w || player.getRightX()-5>hitbox.getX()))){
                player.doGravity(false);
                player.setV(0);
                player.setJump(false);
//                player.setY(hitbox.getY()+h);
//                System.out.println("On Top");
            }
            else if (mtv.normal.x > 0.1){//(player.getCenter()>hitbox.getX()+(w/2)) {
                x -= mtv.depth; //((player.getS()*delta)+(1*delta)); //+ ((y-floor) * delta * player.getS());
//                if (player.getLeftX()+5<hitbox.getX()+w) y += 236 * delta;
                player.setTL(true);
                hv = -player.getS();
                //player.doGravity(true);
            }
            else if (mtv.normal.x < 0.1){//if (player.getCenter()<hitbox.getX()+(w/2)) {
                x += mtv.depth;//((player.getS()*delta)+(1*delta)); //+ ((y-floor) * delta * player.getS());
//                if (player.getRightX()-5>hitbox.getX()) y += 236 * delta;
                player.setTR(true);
                hv = player.getS();
                //player.doGravity(true);
            }
        }
        else{
            player.doGravity(true);
        }
        horizontalVelocity(delta, hitbox.getY()<=floor);
        gravity(delta, floor);

        if (y==floor) v=0;
        player.setTL(false);
        player.setTR(false);

        entity.setPosition(x, y);
        hitbox.setPosition(x, y);
    }

    public void render(Batch batch) {
        entity.draw(batch);
    }

    public void dispose() {
//        texture.dispose();
    }

}
