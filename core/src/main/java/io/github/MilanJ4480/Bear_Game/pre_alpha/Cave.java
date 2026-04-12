package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Cave {
    Sprite cave;

    float x;
    float y;

    public float getFloor() { return y + 48; }

    public Cave(Texture texture, float x, float y) {
        cave = new Sprite(texture);

        this.x = x;
        this.y = y;

        cave.setPosition(x, y);
    }

    public void render(Batch batch) {
        cave.draw(batch);
    }
}
