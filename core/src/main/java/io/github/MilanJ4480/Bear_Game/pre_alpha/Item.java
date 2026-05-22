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

    private int load;
    private float spacingX;
    private float spacingY;

    private  boolean held;
    private boolean carry;
    private  boolean face;
    private boolean isCarry;

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
        face=false;
        isCarry=false;

        load = 0;
        spacingX = 0.1f;
        spacingY = 0.25f;
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
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) b = 90;
        else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) b = -90;
        else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) && face) b = 180;
        else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) && !face) b = 0;
        else if (face) b = MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y - y), -Math.abs(mouse.x - x));
        else b = MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y - y), Math.abs(mouse.x - x));
        //System.out.println(a);
        //System.out.println(b);
        //if (b>90 || b<-90) b -= b-90;

        float diff = a - b;

        if (diff > 180) diff -= 360;
        if (diff < -180) diff += 360;

        float r = delta * diff * 5;

        if (Math.abs(diff) > 5) {
            item.rotate(r);
            hitbox.rotate(r);
        }
    }

    public void setCarry(Vector3 mouse, Player player){
            //held = false;
            isCarry = true;
            player.addLoad(1);
            load = player.getLoad();
            item.setRotation(0);
            hitbox.setRotation(0);
            if (face) x = player.getX() + player.getWidth() * (1 / 5f);
            else x = player.getX() + player.getWidth() * (7 / 10f) - w;
            y = player.getY() + player.getHeight() * (4 / 5f) + (h * load * spacingY);

            if (face && !item.isFlipX()) item.flip(true, false);
            else if (!face && item.isFlipX()) item.flip(true, false);
            if (item.isFlipY()) item.flip(false, true);
    }

    public boolean carrying(Player player, float delta){
        float xf;
        if(face) xf = getCarryX(player.getFrame(), player);
        else xf = getCarryX(player.getFrame(), player) + (w);
        float yf;
        if(face) yf = player.getY() + player.getHeight()*(4/5f)+ (h * load * spacingY);
        else yf = player.getY() + player.getHeight()+ (h * load * spacingY);

        float diffX = xf-x;

        if ((diffX<5 && diffX>-5) && y == yf && ((face && item.getRotation()==0) || (!face && item.getRotation() < 181 && item.getRotation() > 179))) return true;
        else {
            float r;
            if(face) r = item.getRotation();
            else r = item.getRotation()-180;
            if (r > 180) r -= 360;
            if (r < -180) r += 360;

            if(face && (r<5 && r>-5)) item.setRotation(0);
            else if(!face && (r<185 && r>175)) item.setRotation(180);
            else {
                item.rotate(delta * -r * 10);
                hitbox.rotate(delta * -r * 10);
            }

            if(diffX<5 && diffX>-5) x = xf;
            else x+=delta * diffX * 5;

            float diffY = yf-y;
            if(diffY<5 && diffY>-5) y = yf;
            else y+=delta * diffY * 5;

            return false;
        }
    }

    public float getCarryX(int frame, Player player){
        float x;

        if(face) x = player.getX() + player.getWidth()*(1/5f) + (w * spacingX * load);
        else x = player.getX() + player.getWidth()*(7/10f) - (w * load * spacingX) - (w);
        if(frame == 0 || frame == 17) x+=1;
        else if(frame == 1 || frame == 16) x+=2;
        else if(frame == 2 || frame == 15 || frame == 3 || frame == 14) x+=3;
        else if(frame == 4 || frame == 13) x+=4;
        else if(frame == 5 || frame == 12) x+=5;
        else if(frame == 6 || frame == 11) x+=6;
        else if(frame > 6) x+=7;

        return x;
    }

    public void update(float delta, float f, Player player, Vector3 mouse) {
        boolean playerFace = player.getFace();

        if(carry) {
            player.setHolding(false);
            if (isCarry) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                    isCarry = false;
                    carry = false;
                    player.addLoad(-1);
                    //if (!item.isFlipX()) item.flip(true, false);
                    //if (item.isFlipY()) item.flip(false, true);
                }
                else {
                    x = getCarryX(player.getFrame(), player);
                    y = player.getY() + (player.getHeight() * (4 / 5f)) + (h * load * 0.5f);
                    face = player.getFace();
                    if (playerFace && !item.isFlipX()) {
                        item.flip(true, false);
                        //item.setRotation(-15);
                    } else {
                        if (!playerFace && item.isFlipX()) item.flip(true, false);
                        item.setRotation(0);
                    }
                }
            }
            else {
                if(carrying(player, delta)) setCarry(mouse, player);
            }
        }
        else if((Intersector.overlapConvexPolygons(hitbox, player.getSwipeBox()) && Gdx.input.isKeyJustPressed(Input.Keys.E) && !player.getHolding()) || held){
            if(held && Gdx.input.isKeyJustPressed(Input.Keys.E)) held=false;
            else {
                held=true;
                player.setHolding(true);
            }

            if(playerFace) {
                if(!face) item.setRotation(MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y-y) , -Math.abs(mouse.x-x)));
                x = player.getX();//-swipeBox.getBoundingRectangle().getWidth();
                face=true;
                if (!item.isFlipX()) item.flip(true, false);
                if (!item.isFlipY()) item.flip(false, true);
                //System.out.println((item.getRotation()+360)%360);
                if(mouse.x>player.getX()+player.getWidth() && ((item.getRotation()+360)%360>0 && (item.getRotation()+360)%360<180)){
                    carry = true;
                    held = false;
                    item.flip(false, true);
                    //setCarry(mouse, player);
                }
                //item.setOrigin(0, 0);
            }
            else {
                if(face) item.setRotation(MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y-y) , Math.abs(mouse.x-x)));
                x = player.getX()+player.getWidth();
                face=false;
                if (!item.isFlipX()) item.flip(true, false);
                if (item.isFlipY()) item.flip(false, true);
                if(mouse.x<player.getX() && (item.getRotation()%360>0 && item.getRotation()%360<180)) {
                    carry = true;
                    held = false;
                    item.flip(false, true);
                    //setCarry(mouse, player);
                }
            }
            if(!isCarry) y = player.getY()+player.getHeight()/2;
            rotate(-delta, mouse);
        }
        else {
            //System.out.println("free");
            player.setHolding(false);
            gravity(delta, f);
            //if (item.isFlipX() && item.isFlipY() && item.getRotation()==0) item.flip(false, true);
            //else if (!item.isFlipX() && !item.isFlipY()) item.flip(false, true);
            //item.setRotation(0);
        }
        //if ((!carry || held) && face && !item.isFlipY()) item.flip(false, true);
        //else if  (!carry && !face && item.isFlipY()) item.flip(false, true);

        item.setPosition(x, y);
        hitbox.setPosition(x, y);
    }

    public void render(Batch batch) {
        item.draw(batch);
        //batch.draw(item, x, y, w*0.25f, h*0.25f);
    }
}
