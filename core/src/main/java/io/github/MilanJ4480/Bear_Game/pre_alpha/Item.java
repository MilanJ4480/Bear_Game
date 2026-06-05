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
    private float sweep;
    private float sweepDiff;

    private int load;
    private float spacingX;
    private float spacingY;

    private  boolean held;
    private boolean carry;
    private  boolean face;
    private boolean isCarry;
    private boolean isHeld;

    public Item(TextureRegion texture, float x, float y) {
        this.item = new Sprite(texture);


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
        isHeld=false;
        sweep = 0;

        load = 0;
        spacingX = 0.1f;
        spacingY = 0.25f;

        item.setOrigin(1, h/2);
        hitbox.setOrigin(1, h/2);
    }

    public float getX() { return x; }
    public float getW() { return w; }
    public float getY() { return y; }
    public float getH() { return h; }
    public boolean getHeld() { return held; }
    public Polygon getHitbox() { return hitbox; }

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
        float a = Math.round(item.getRotation() % 360);

        float b;
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) b = 90;
        else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) b = -90;
        else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) && face) b = 180;
        else if(Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) && !face) b = 0;
        else if (face) {
            if (sweep == 0) {
                b = MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y - y), -Math.abs(mouse.x - x));
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                    sweep = b;
                    sweepDiff = a-b;
                    if (sweepDiff > 180) sweepDiff -= 360;
                    if (sweepDiff < -180) sweepDiff += 360;
                    if (Math.abs(sweepDiff) < 25) sweep = 0;
                }
            }
            else b = sweep;
        }
        else {
            if (sweep == 0) {
                b = MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y - y), Math.abs(mouse.x - x));
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                    sweep = b;
                    sweepDiff = a-b;
                    if (sweepDiff > 180) sweepDiff -= 360;
                    if (sweepDiff < -180) sweepDiff += 360;
                    if (Math.abs(sweepDiff) < 25) sweep = 0;
                }
            }
            else b=sweep;
        }
        //System.out.println(a);
        //System.out.println(b);
        //if (b>90 || b<-90) b -= b-90;

        float diff = a - b;

        if (diff > 180) diff -= 360;
        if (diff < -180) diff += 360;

        float r;

        if (sweep !=0){
            r = (float) ((150 + ( (450-150) * Math.pow(1-Math.abs(diff/sweepDiff), 0.5) )) * Math.signum(diff) * delta);
            if(diff < 5 && diff > -5) sweep = 0;
        }
        else r = delta * diff * 5;
        //rotation += delta * Math.signum(diff) * 5;

        //if (diff < 0.1 && diff > -0.1) rotation = 0;

        System.out.println("sweep: " + sweep + "\n diff: " + diff + "\n r: " + r + "\n sweepDiff: " + sweepDiff);

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
            //if (face) x = player.getX() + player.getWidth() * (1 / 5f);
            //else x = player.getX() + player.getWidth() * (7 / 10f) - w;
            x = getCarryX(player.getFrame(), player);
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
        if(face) yf = player.getY() + player.getHeight()*(4/5f) + (h * load * spacingY);
        else yf = player.getY() + player.getHeight()*(4/5f) + (h * load * spacingY);

        float diffX = xf-x;

        if ((diffX<1 && diffX>-1) && y == yf && ((face && item.getRotation()==0) || (!face && item.getRotation() < 181 && item.getRotation() > 179))) return true;
        else {
            float r;
            if(face) r = item.getRotation();
            else r = item.getRotation()-180;
            if (r > 180) r -= 360;
            if (r < -180) r += 360;

            if(face && (r<5 && r>-5)) {
                item.setRotation(0);
                hitbox.setRotation(0);
            }
            else if(!face && (r<185 && r>175)) {
                item.setRotation(180);
                hitbox.setRotation(180);
            }
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
        int f;

        if(player.getFace()) f=1;
        else f=-1;

        if(player.getFace()) x = player.getX() + player.getWidth()*(1/5f) + (w * spacingX * load);
        else x = player.getX() + player.getWidth() - w - player.getWidth()*(1/5f) - (w * load * spacingX);
        if(frame == 0 || frame == 17) x+=f;
        else if(frame == 1 || frame == 16) x+=2*f;
        else if(frame == 2 || frame == 15 || frame == 3 || frame == 14) x+=3*f;
        else if(frame == 4 || frame == 13) x+=4*f;
        else if(frame == 5 || frame == 12) x+=5*f;
        else if(frame == 6 || frame == 11) x+=6*f;
        else if(frame > 6) x+=7*f;

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
            if(held && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                held=false;
                sweep = 0;
            }
            else {
                held=true;
                isHeld=true;
                player.setHolding(true);
            }

            if(playerFace) {
                if(!face) {
                    float rx = MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y-y) , -Math.abs(mouse.x-x));
                    item.setRotation(rx);
                    hitbox.setRotation(rx);
                }
                x = player.getX();//-swipeBox.getBoundingRectangle().getWidth();
                //y = player.getY()+player.getHeight()/2-8;
                face=true;
                if (!item.isFlipX()) item.flip(true, false);
                if (!item.isFlipY()) item.flip(false, true);
                //System.out.println((item.getRotation()+360)%360);
                if(mouse.x>player.getX()+player.getWidth() && ((item.getRotation()+360)%360>0 && (item.getRotation()+360)%360<180)){
                    carry = true;
                    held = false;
                    sweep = 0;
                    item.flip(false, true);
                    //setCarry(mouse, player);
                }
            }
            else {
                if(face) {
                    float rx = MathUtils.radiansToDegrees * MathUtils.atan2((mouse.y-y) , Math.abs(mouse.x-x));
                    item.setRotation(rx);
                    hitbox.setRotation(rx);
                }
                x = player.getX()+player.getWidth();
                //y = player.getY()+player.getHeight()/2-8;
                face=false;
                if (!item.isFlipX()) item.flip(true, false);
                if (item.isFlipY()) item.flip(false, true);
                if(mouse.x<player.getX() && (item.getRotation()%360>0 && item.getRotation()%360<180)) {
                    carry = true;
                    held = false;
                    sweep = 0;
                    item.flip(false, true);
                    //setCarry(mouse, player);
                }
            }
            if(!isCarry) y = player.getY()+player.getHeight()/2-8;
            rotate(-delta, mouse);
        }

        else {
            //System.out.println("free");
            if(isHeld) {
                isHeld=false;
                player.setHolding(false);
            }
            gravity(delta, f);
        }

        item.setPosition(x, y);
        hitbox.setPosition(x, y);
    }

    public void render(Batch batch) {
        item.draw(batch);
        //batch.draw(item, x, y, w*0.25f, h*0.25f);
    }
}
