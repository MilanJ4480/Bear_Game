package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;

public class Main extends ApplicationAdapter {
    //Important
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    //Classes
    private Player player;
    private Entity entity;


    //Textures
    private Texture background;
    private Texture ground;
    private Texture log;


    //Rectangles
    private Rectangle rectGround;

    //Basic Variables
    private float worldWidth;
    private float worldHeight;




    @Override
    public void create() {

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        worldWidth = 640;
        worldHeight = 360;
        viewport = new FitViewport(worldWidth, worldHeight, camera);

        background = new Texture("white.png");
        log = new Texture("log.png");

        ground = new Texture("ground.png");
        rectGround = new Rectangle(0, 0, ground.getWidth(), ground.getHeight());

        player=new Player(50, ground.getHeight());
        entity=new Entity(log, 75, 0, 1, 1);

    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }



    float stateTime = 0f;
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);

        float delta = Gdx.graphics.getDeltaTime();

        player.update(rectGround, delta);
        entity.update(delta, player, ground.getHeight(), player.getFace());

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        batch.begin();

        batch.draw(ground, 0, 0);
        player.render(batch);
        entity.render(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        ground.dispose();
        player.dispose();
        log.dispose();
    }
}
