package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Polygon;

import java.util.Arrays;

import static java.lang.Math.sqrt;

public class Player{

    private TextureAtlas bearWalk1Atlas;
    private TextureAtlas bearSwipeAtlas;
    private Animation<TextureRegion> bearAnimation;
    private Animation<TextureRegion> bearWalk1;
    private Animation<TextureRegion> bearSwipe;
    private Sprite bear;
//    private Rectangle rectBear;
    private Polygon polyBear;
    private Polygon swipeBox;

    private float bearX;
    private float bearY;
    private float bearV;
    private boolean bearFace;
    private boolean jump;
    private float g;
    private float stateTime;
    private float floor;
    private float bearS;
    private boolean tr;
    private boolean tl;
    private boolean doG;
    private float jumpStrength;
    private boolean attack;

    public Player(float X, float Y){

        //Walking Animation
        bearWalk1Atlas = new TextureAtlas("bearWalk1Atlas/bearWalk.atlas");
        bearWalk1 = new Animation<>(1f/12f, bearWalk1Atlas.findRegions("bear_walk_1"));
        bearWalk1.setPlayMode(Animation.PlayMode.LOOP);
        bearAnimation = bearWalk1;

        bearSwipeAtlas = new TextureAtlas("bearWalk1Atlas/swipe.atlas");
        bearSwipe = new Animation<>(1f/80f, bearSwipeAtlas.findRegions("swipe"));

        bear = new Sprite( bearAnimation.getKeyFrame(0));
        bear.setScale(1, 1);
        bear.setPosition(bearX, bearY);
//        rectBear = new Rectangle( bear.getX(), bear.getY(), bear.getWidth(), bear.getHeight());
        float[] vertices = new float[] {
            0, 0,
            bear.getWidth(), 0,
            bear.getWidth(), bear.getHeight(),
            0, bear.getHeight()
        };
        polyBear = new Polygon(vertices);
        polyBear.setPosition(X,Y);

        float[] swipeVertices = new float[] {
            0, 0,
            polyBear.getBoundingRectangle().getWidth()/2, 0,
            polyBear.getBoundingRectangle().getWidth()/2, polyBear.getBoundingRectangle().getHeight()/2,
            0, polyBear.getBoundingRectangle().getHeight()/2
        };
        swipeBox = new Polygon(swipeVertices);


        bearS = 75;
        bearV = 0;
        bearX = X;
        bearY = Y;
        g=-750;
        jumpStrength= (float) sqrt(2 * (g*-1) * 75);
        jump = false;
        bearFace=false;
        tr=false;
        tl=false;
        doG=true;
        attack=false;
    }

    public float getX(){ return polyBear.getX(); }
    public float getY(){ return polyBear.getY(); }
    public Polygon getPolygon() { return polyBear; }
    public Polygon getSwipeBox(){return swipeBox;}

    public boolean getFace(){ return bearFace; }
    public boolean getAttack(){ return attack; }

    public float getV() { return bearV; }
    public void setX(float X){bearX = X;}
    public void setY(float Y){bearY = Y;}
    public float getS() { return bearS; }
    public float getWidth() { return polyBear.getBoundingRectangle().getWidth();}
    public float getHeight() { return polyBear.getBoundingRectangle().getHeight();}

    public void setV(float V){bearV = V;}
    public void setJump(boolean j){jump = j;}
    public void setTR(boolean t) {tr = t;}
    public void setTL(boolean t) {tl = t;}
    public void doGravity(boolean G) { doG = G; }

    public float getRightX(){ return bearX + polyBear.getBoundingRectangle().getWidth();}
    public float getLeftX(){ return bearX; }
    public float getCenter() { return bearX + polyBear.getBoundingRectangle().getWidth() / 2;}

    public void swapAnimation(Animation<TextureRegion> animation, boolean loop){
        if (loop) animation.setPlayMode(Animation.PlayMode.LOOP);
        else animation.setPlayMode(Animation.PlayMode.NORMAL);
        bearAnimation = animation;
        stateTime = 0f;
    }

    private void gravity(float delta){
        if (bearY<floor){
            bearY = floor;
        }
        else{
            bearV += g*delta;
            if (bearY + bearV*delta > floor) bearY += bearV * delta;
            else bearY = floor;
        }
    }

    public void doFlip(TextureRegion region){
        if (bearFace && region.isFlipX()) region.flip(true, false);
        else if  (!bearFace && !region.isFlipX()) region.flip(true, false);
    }

    public void update(float f, float delta) {
//        if (Intersector.overlapConvexPolygons(polyBear, ground)) floor=ground.getBoundingRectangle().getHeight()-5;
        floor = f;
        if (Gdx.input.isKeyPressed(Input.Keys.E)) bearS=175;
        else bearS=75;
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && bearAnimation==bearWalk1){
            swapAnimation(bearSwipe, false);
            attack = true;
        }
        else if (bearAnimation.isAnimationFinished(stateTime) && bearAnimation!=bearWalk1){
            swapAnimation(bearWalk1,true);
            attack = false;
        }
        if (bearAnimation == bearSwipe){
            stateTime += delta;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            stateTime += delta;
//            if (jump && !tr) bearX+=250*delta;
            bearX += (bearS) * delta;
            bearFace = false;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            stateTime += delta;
//            if (jump && !tl) bearX-=250*delta;
            bearX += (-bearS) * delta;
            bearFace = true;
            }
        else{
            if(!bearAnimation.getKeyFrame(stateTime).equals(bearAnimation.getKeyFrames()[16])){
                if (bearAnimation.getKeyFrameIndex(stateTime) > 8 && bearAnimation.getKeyFrameIndex(stateTime)<16) stateTime += delta;
                else {
                    stateTime -= delta;
                    if (stateTime < 0){
                        stateTime = bearAnimation.getAnimationDuration();
                    }
                }
            }
        }
        if  (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !jump) {
            jump=true;
            bearV+=jumpStrength;
        }
        if(doG || bearV>0) gravity(delta);
        if (bearY<=floor) {
            jump = false;
            bearV=0;
        }

        bear.setPosition(bearX, bearY);
        polyBear.setPosition(bearX, bearY);
        if(bearFace) swipeBox.setPosition(bearX, bearY);
        else swipeBox.setPosition(bearX+(bear.getWidth()/2), bearY);
    }

    public void render(SpriteBatch batch){
        TextureRegion region = bearAnimation.getKeyFrame(stateTime);
        doFlip(region);
        bear.setRegion(region);
        bear.draw(batch);
    }

    public void dispose(){
        bearSwipeAtlas.dispose();
        bearWalk1Atlas.dispose();
    }

}
