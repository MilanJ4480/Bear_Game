package io.github.MilanJ4480.Bear_Game.pre_alpha;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Color;

import java.awt.*;

public class Main extends ApplicationAdapter {
    //Important
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    //Classes
    private Player player;
    private Entity entity;
    private Background bgGround;


    //Textures
    private Texture background;
    private Texture ground;
    private Texture log;


    //Rectangles
    private ShapeRenderer shapeRenderer;

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
        shapeRenderer = new ShapeRenderer();

        background = new Texture("white.png");
        log = new Texture("log.png");

        ground = new Texture("ground.png");
        bgGround = new Background(ground, 0, 0, 1, 1, true);

        player=new Player(50, ground.getHeight());
        entity=new Entity(log, 150, 0, 3, 3);

    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void hitbox(float x, float y, float w, float h){
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED); // Choose a color for visibility
        shapeRenderer.rect(x, y, w, h);
        shapeRenderer.end();
    }

    float stateTime = 0f;
    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);

        float delta = Gdx.graphics.getDeltaTime();

        player.update(bgGround.getRectangle(), delta);
        entity.update(delta, player, bgGround.getY() + bgGround.getHeight(), player.getFace());

        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();


        batch.begin();

//        batch.draw(ground, 0, 0);
        player.render(batch);
        entity.render(batch);
        bgGround.render(batch);

        batch.end();

        hitbox(player.getX(), player.getY(), player.getRectangle().getWidth(), player.getRectangle().getHeight());
        hitbox(entity.getRectangle().getX(), entity.getRectangle().getY(), entity.getRectangle().getWidth(), entity.getRectangle().getHeight());
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
