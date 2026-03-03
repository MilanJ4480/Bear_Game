package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;

public class Player{

    private TextureAtlas bearWalk1Atlas;
    private Animation<TextureRegion> bearWalk1;
    private Sprite bear;
    private Rectangle rectBear;

    private float bearX;
    private float bearY;
    private float bearV;
    private boolean bearFace;
    private boolean jump;
    private float g;
    private float stateTime;
    private float floor;

    public Player(float X, float Y){

        //Walking Animation
        bearWalk1Atlas = new TextureAtlas("bearWalk1Atlas/bearWalk.atlas");
        bearWalk1 = new Animation<>(0.18f, bearWalk1Atlas.findRegions("bear_walk_1"));
        bearWalk1.setPlayMode(Animation.PlayMode.LOOP);
        bear = new Sprite( bearWalk1.getKeyFrame(0));
        bear.setScale(1, 1);
        bear.setPosition(bearX, bearY);
        rectBear = new Rectangle( bear.getX(), bear.getY(), bear.getWidth(), bear.getHeight());

        bearV = 0;
        bearX = X;
        bearY = Y;
        g=-750;
        jump = false;
        bearFace=false;

    }

    public float getX(){ return bearX; }
    public float getY(){ return bearY; }
    public Rectangle getRectangle() { return rectBear; }
    public boolean getFace(){ return bearFace; }
    public void setX(float X){bearX = X;}
    public void setY(float Y){bearY = Y;}

    public float getRightX(){ return bearX + rectBear.getWidth();}
    public float getLeftX(){ return bearX; }
    public float getCenter() { return bearX + rectBear.getWidth() / 2;}

    private void gravity(float delta){
        if (bearY<floor){
            bearY = floor;
        }
        else{
            bearV += g*delta;
            bearY += bearV*delta;
            if (bearY<floor) bearY = floor;
        }
    }

    public void doFlip(TextureRegion region){
        if (bearFace && region.isFlipX()) region.flip(true, false);
        else if  (!bearFace && !region.isFlipX()) region.flip(true, false);
    }

    public void update(Rectangle rectGround, float delta) {
        if (rectBear.overlaps(rectGround)) floor=rectGround.getHeight()-5;
        else floor=0;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            stateTime += delta;
            bearX += (75) * delta;
            if (jump) bearX+=200*delta;
            bearFace = false;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            stateTime += delta;
            bearX += (-75) * delta;
            if (jump) bearX-=200*delta;
            bearFace = true;
            }
        if  (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !jump) {
            jump=true;
            bearV+=250;
        }
        gravity(delta);
        if (bearY<=floor) {
            jump = false;
            bearV=0;
        }

        bear.setPosition(bearX, bearY);
        rectBear.setPosition(bearX, bearY);
    }

    public void render(SpriteBatch batch){
        TextureRegion region = bearWalk1.getKeyFrame(stateTime, true);
        doFlip(region);
        bear.setRegion(region);
        bear.draw(batch);
    }

    public void dispose(){
        bearWalk1Atlas.dispose();
    }

}
