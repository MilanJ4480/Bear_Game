package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static java.lang.Math.signum;

public class Deer extends Enemy {
    float[] pack;
    short id;
    int movement;
    boolean fear;
    float bear;

    private Sound deerCall;
    private Sound deerWarningCall;
    private Sound deerSnort;

    public Deer(Animation<TextureRegion> animation, float x, float y, float w, float h, int health, float[] pack, int id) {

        super(animation, x + (5 * id), y, w, h, health);

        this.pack = pack;
        this.id = (short) id;
        this.fear = false;

        this.pack[id] = x + (5 * id);

        deerCall = Gdx.audio.newSound(Gdx.files.internal("deerSound.mp3"));
        deerWarningCall = Gdx.audio.newSound(Gdx.files.internal("deerWarning.mp3"));
        deerSnort = Gdx.audio.newSound(Gdx.files.internal("deerSnort.mp3"));
    }

    public void doFlip(TextureRegion region){
        if ((Math.signum(movement)==-1 && region.isFlipX() && pack[pack.length-1]==0) || (pack[pack.length-1]<0 && region.isFlipX())) region.flip(true, false);
        else if  ((Math.signum(movement)==1 && !region.isFlipX() && pack[pack.length-1]==0) || (pack[pack.length-1]>0 && !region.isFlipX())) region.flip(true, false);
    }

    public void death(){
        pack[id] = Float.NaN;
        //System.out.println("DEAD" + pack[id]);
        super.death();
    }

    public void leader(){
        //System.out.println(pack[0]);
        if(Float.isNaN(pack[0])){
            System.out.println("Deer " + id + " is the new Leader");
            pack[0] = pack[id];
            pack[id] = Float.NaN;
            id=0;
            pack[pack.length-1] = id+1;

            fear = false;
        }
    }

    public int majority(float bear){
        int l=0;
        int r=0;
        for(int i = 0; i < pack.length-1; i++){
            if(!Float.isNaN(pack[i])){
                if (pack[i] > bear) r++;
                else l++;
            }
        }
        if(l>r) return -1;
        else return 1;
    }


    public void move(float delta, float playerX) {
        leader();
        bear = Math.abs(playerX) - Math.abs(pack[id]);
        //System.out.println("bear: " + bear);
        if ((bear < -500 || bear > 500) && id==Math.abs(pack[pack.length - 1])-1) pack[pack.length - 1] = 0;
        else if(pack[pack.length - 1]!=0) {
            fear=true;
            if(id==Math.abs(pack[pack.length - 1])-1) pack[pack.length - 1] = Math.abs(pack[pack.length - 1])*majority(playerX);
        }
        else if (((( (bear > -250 && bear < 250 && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) || (bear > -150 && bear < 150) ) && (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D))) && ((Math.signum(movement)==-1 && Math.signum(bear)==-1) || (Math.signum(movement)==1 && Math.signum(bear)==1))) || (bear > -75 && bear < 75) || super.hit) {
            fear=true;
            pack[pack.length - 1] = majority(playerX)*(id+1);
            deerWarningCall.play();
        }
        else fear=false;
        //else pack[pack.length - 1] = 0;
        //if (pack[pack.length - 1] != 0) movement = (int) (200*pack[pack.length - 1]);
        //System.out.println(pack[pack.length-1]);
        if(fear) {
            pack[id] += (int) (200 * delta * Math.signum(pack[pack.length - 1]));
            super.stateTime+=delta;
        }
        else if (movement == 0 && super.rand.nextInt(101) == 1) {
            movement = super.rand.nextInt(500) - 250;
            if (id != 0 && ((pack[id] + (75 * delta * movement) > pack[0] + (75 * 500 * delta) && movement > 0) || (pack[id] + (75 * delta * movement) < pack[0] - (75 * 500 * delta) && movement < 0))) {
                movement = 0;
            }
            else{
                super.stateTime+=delta;
            }
        }
        else if (movement != 0) {
            pack[id] += (int) (75 * delta * Math.signum(movement));
            if (movement > 0) movement -= 1;
            else movement += 1;
            super.stateTime+=delta;
        }
        super.x = pack[id];

        if(fear){
            super.walkAnimation.setFrameDuration(1f/64f);
        }
        else{
            super.walkAnimation.setFrameDuration(1f/32f);
        }

        if(!fear){
            int r = rand.nextInt(1000);

            if(r<=1){
                deerCall.play();
            }
            //else if(r==2){
               // deerSnort.play();
            //}
        }
    }


    public void dispose(){
        deerCall.dispose();
        deerSnort.dispose();
        deerWarningCall.dispose();
    }
}
