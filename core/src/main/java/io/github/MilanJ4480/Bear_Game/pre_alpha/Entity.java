package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import sun.awt.windows.WPrinterJob;

import java.awt.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */

public class Entity {
    private Texture texture;
    private Sprite entity;
    private Rectangle hitbox;

    private float x;
    private float y;
    private float w;
    private float h;
    private float g;
    private float v;
    private float floor;

    public Entity(Texture texture, float x, float y, float w, float h) {
        this.texture = texture;
        this.entity = new Sprite(this.texture);
        hitbox = new Rectangle(x, y, w, h);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.g = -1000;
        this.v=0;

        floor=0;

        entity.setPosition(x, y);
        entity.setSize(entity.getWidth()*w, entity.getHeight()*h);
        hitbox.setSize(entity.getWidth(), entity.getHeight());
    }

    public Rectangle getRectangle() { return hitbox; }

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

    public void update(float delta, Rectangle ground, Player player, boolean face) {
        if (hitbox.overlaps(ground)) floor = ground.getY()+ground.getHeight()-1;
        else floor=0;
        if (hitbox.overlaps(player.getRectangle())) {
            if (player.getY() >= hitbox.getY()+hitbox.getHeight() || (player.getY() > hitbox.getY() && (player.getLeftX()<hitbox.getX()+hitbox.getWidth() || player.getRightX()-5>hitbox.getX()))){
                player.doGravity(false);
                player.setV(0);
                player.setJump(false);
                player.setY(hitbox.getY()+hitbox.getHeight());
//                System.out.println("On Top");
            }
            else if (player.getCenter()>hitbox.getX()+(hitbox.getWidth()/2)) {
                x -= ((player.getS()*delta)+(1*delta)); //+ ((y-floor) * delta * player.getS());
                if (player.getLeftX()+5<hitbox.getX()+hitbox.getWidth()) y += 236 * delta;
                player.setTL(true);
                //player.doGravity(true);
            }
            else if (player.getCenter()<hitbox.getX()+(hitbox.getWidth()/2)) {
                x += ((player.getS()*delta)+(1*delta)); //+ ((y-floor) * delta * player.getS());
                if (player.getRightX()-5>hitbox.getX()) y += 236 * delta;
                player.setTR(true);
                //player.doGravity(true);
            }
        }
        else player.doGravity(true);
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
        texture.dispose();
    }

}
