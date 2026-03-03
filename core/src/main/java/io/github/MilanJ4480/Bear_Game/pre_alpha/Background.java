package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Background {
    private Sprite sprite;
    private Rectangle rect;
    public Background(Texture texture, float x, float y, float w, float h, boolean hitbox){
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(texture.getWidth()*w, texture.getHeight()*h);
        if (hitbox){
            this.rect = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
        }
    }

    public float getY(){ return rect.getY(); }
    public float getX(){ return rect.getX(); }
    public float getWidth(){ return rect.getWidth(); }
    public float getHeight(){ return rect.getHeight(); }
    public float getR(){ return rect.getX()+rect.width; }
    public float getL(){ return rect.getX(); }
    public Rectangle getRectangle() { return rect; }

    public void render(Batch batch){
        sprite.draw(batch);
    }
}
