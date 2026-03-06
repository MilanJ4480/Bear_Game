package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class Background {
    private Sprite sprite;
    private Polygon poly;
    public Background(Texture texture, float x, float y, float w, float h, boolean hitbox){
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(texture.getWidth()*w, texture.getHeight()*h);
        if (hitbox){
            float[] vertices = new float[]{
                0, 0,
                sprite.getWidth(), 0,
                sprite.getWidth(), sprite.getHeight(),
                0, sprite.getHeight(),
            };
            poly = new Polygon(vertices);
            poly.setPosition(x, y);
        }
    }

    public float getY(){ return poly.getY(); }
    public float getX(){ return poly.getX(); }
    public float getWidth(){ return poly.getBoundingRectangle().getWidth(); }
    public float getHeight(){ return poly.getBoundingRectangle().getHeight(); }
    public float getR(){ return poly.getX()+poly.getBoundingRectangle().getWidth(); }
    public float getL(){ return poly.getX(); }
    public Polygon getPolygon() { return poly; }

    public void render(Batch batch){
        sprite.draw(batch);
    }
}
