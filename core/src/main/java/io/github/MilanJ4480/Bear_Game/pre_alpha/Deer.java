package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static java.lang.Math.signum;

public class Deer extends Enemy {
    float[] pack;
    short id;
    int movement;
    boolean fear;

    public Deer(Texture texture, float x, float y, float w, float h, int health, float[] pack, int id) {
        super(texture, x + (5 * id), y, w, h, health);
        this.pack = pack;
        this.id = (short) id;
        this.fear = false;

        this.pack[id] = x + (5 * id);
    }

    public int majority(float bear){
        int l=0;
        int r=0;
        for(int i = 0; i < pack.length-1; i++){
            if(pack[i] > bear) r++;
            else l++;
        }
        System.out.println(l>r);
        if(l>r) return -1;
        else return 1;
    }


    public void move(float delta, float playerX) {
        float bear = Math.abs(playerX) - Math.abs(pack[id]);
        //System.out.println("bear: " + bear);
        if ((bear < -500 || bear > 500) && id==Math.abs(pack[pack.length - 1])-1) pack[pack.length - 1] = 0;
        else if(pack[pack.length - 1]!=0) {
            fear=true;
            if(id==Math.abs(pack[pack.length - 1])-1) pack[pack.length - 1] = Math.abs(pack[pack.length - 1])*majority(playerX);
        }
        else if ((bear > -250 && bear < 250)) {
            fear=true;
            pack[pack.length - 1] = majority(playerX)*(id+1);
        }
        else fear=false;
        //else pack[pack.length - 1] = 0;
        //if (pack[pack.length - 1] != 0) movement = (int) (200*pack[pack.length - 1]);
        //System.out.println(pack[pack.length-1]);
        if(fear) pack[id] += (int) (150 * delta * Math.signum(pack[pack.length - 1]));
        else if (movement == 0 && super.rand.nextInt(101) == 1) {
            movement = super.rand.nextInt(500) - 250;
            if (id != 0 && ((pack[id] + (75 * delta * movement) > pack[0] + (75 * 500 * delta) && movement > 0) || (pack[id] + (75 * delta * movement) < pack[0] - (75 * 500 * delta) && movement < 0))) {
                movement = 0;
            }
        }
        else if (movement != 0) {
            pack[id] += (int) (75 * delta * Math.signum(movement));
            if (movement > 0) movement -= 1;
            else movement += 1;
        }
        super.x =pack[id];
    }
}
