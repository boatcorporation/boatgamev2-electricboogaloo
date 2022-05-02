package com.team21direction.pirategame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team21direction.pirategame.PirateGame;
import com.team21direction.pirategame.actors.*;

import java.util.ArrayList;
import java.util.Objects;

public class MainScreen implements Screen {

    public final PirateGame game;
    private final Batch batch;
    private final Batch shopBatch;
    private final Batch weatherBatch;

    protected Stage stage;
    protected Stage weatherStage;
    private final Viewport viewport;
    protected Skin skin;
    protected TextureAtlas atlas;
    private final BitmapFont font;
    private final Music music;
    private final Sound cannonballSound;
    private Shop shop;

    private final OrthographicCamera camera;

    private final Weather[] weathers;
    private final College[] colleges;
    public final Ship[] ships;
    public final Ship player;
    private final Vector2 position = new Vector2();
    private final Vector2 cannonball_velocity = new Vector2();
    private final Vector2 movement = new Vector2();
    private final Vector2 mouse = new Vector2();
    private final Vector2 dir = new Vector2();

    private float timeSinceLastCannon = 0.0f;

    public int experience = 0;
    public int gold = 0;

    /**
     * Amount of experience gained when taking over a College.
     */
    public int experiencePerCollege = 10;

    /**
     * Amount of gold gained when razing a College.
     */
    public int goldPerCollege = 10;

    private float timeSinceLastExpDrop = 0.0f;
    private float timeSinceLastMusicToggle = 0.0f;
    private float timeSinceLastShopToggle = 0.0f;

    private boolean isPlayingMusic = true;
    boolean isToggled = false;


    private double difficultyMultiplier = 1;

    public MainScreen(PirateGame game, String difficulty) {
        this.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        font = new BitmapFont();
        font.getData().setScale(4.0f);

        camera = new OrthographicCamera();

        batch = new SpriteBatch();
        shopBatch = new SpriteBatch();
        weatherBatch = new SpriteBatch();

        viewport = new FitViewport(2670, 2000, camera);
        viewport.apply();


        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);

        cannonballSound = Gdx.audio.newSound(Gdx.files.internal("cannonball.mp3"));

        if (difficulty == "Easy") {
            difficultyMultiplier = 0.5;
        } else if (difficulty == "Hard") {
            difficultyMultiplier = 2;
        }

        stage = new Stage(viewport, batch);
        weatherStage = new Stage(viewport, weatherBatch);


        colleges = new College[] {
                new College(this, "Derwent", difficultyMultiplier),
                new College(this, "Langwith", difficultyMultiplier),
                new College(this, "Constantine", difficultyMultiplier),
                new College(this, "Halifax", difficultyMultiplier),
        };
        weathers = new Weather[200];
        for(int i = 0; i < 200; i++) {
            weathers[i] = new Weather(this);
            boolean success;
            do {
                success = weathers[i].move((float)(Math.random() * PirateGame.WORLD_WIDTH) - PirateGame.WORLD_WIDTH / 2.0f, (float)(Math.random() * PirateGame.WORLD_HEIGHT) - PirateGame.WORLD_WIDTH / 2.0f);
            } while (!success);
            weatherStage.addActor(weathers[i]);
        }


        ships = new Ship[PirateGame.SHIPS_PER_COLLEGE * colleges.length];
        for (int i = 0; i < colleges.length; i++) {
            boolean success;
            do {
                success = colleges[i].move((float)(Math.random() * PirateGame.WORLD_WIDTH) - PirateGame.WORLD_WIDTH / 2.0f, (float)(Math.random() * PirateGame.WORLD_HEIGHT) - PirateGame.WORLD_WIDTH / 2.0f);
            } while (!success);

            stage.addActor(colleges[i]);
            for (int j = 0; j < PirateGame.SHIPS_PER_COLLEGE; j++) {
                ships[i + j] = new Ship(this, colleges[i], difficultyMultiplier);
                do {
                    success = ships[i + j].move((float)(Math.random() * PirateGame.WORLD_WIDTH) - PirateGame.WORLD_WIDTH / 2.0f, (float)(Math.random() * PirateGame.WORLD_HEIGHT) - PirateGame.WORLD_WIDTH / 2.0f);
                } while (!success);
                stage.addActor(ships[i + j]);
            }
        }


        player = new Ship(this, new College(this,"Vanbrugh", difficultyMultiplier), true, difficultyMultiplier);
        boolean success;
        do {
            success = player.move((float)(Math.random() * PirateGame.WORLD_WIDTH) - PirateGame.WORLD_WIDTH / 2.0f, (float)(Math.random() * PirateGame.WORLD_HEIGHT) - PirateGame.WORLD_WIDTH / 2.0f);
        } while (!success);
        stage.addActor(player);
        // send clouds to front so they're displayed on top of player

        shop = new Shop(shopBatch, player, this);



    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, shop.getStage()));
        if (isPlayingMusic) music.play();
    }

    @Override
    public void render(float delta) {
        timeSinceLastShopToggle += delta;
        timeSinceLastCannon += delta;
        timeSinceLastExpDrop += delta;
        timeSinceLastMusicToggle += delta;

        GameActor collidingWith = getCollision(player.getX(), player.getY());
        if(collidingWith instanceof Weather) {
            if(timeSinceLastExpDrop >= 2.0f) {
                player.attack(1);
                experience++;
                timeSinceLastExpDrop = 0.0f;
            }
        }

        if (timeSinceLastExpDrop >= 10.0f) {
            timeSinceLastExpDrop = 0.0f;
            experience++;
        }

        //update(Gdx.graphics.getDeltaTime());

        float lerp = 5f;
        camera.position.x += (player.getX() - camera.position.x) * lerp * delta;
        camera.position.y += (player.getY() - camera.position.y) * lerp * delta;

        // camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
        update_keyboard();

        batch.setProjectionMatrix(camera.combined);
        // fix for some PNG transparency quirks...
        batch.enableBlending();
        ScreenUtils.clear(0, 0.6f, 1, 1);
        Gdx.gl.glClear(GL20.GL_ALPHA_BITS);
        stage.act(delta);
        stage.draw();
        weatherStage.act(delta);
        weatherStage.draw();

        shopBatch.setProjectionMatrix(shop.getStage().getCamera().combined);
        if (Gdx.input.isKeyPressed(Input.Keys.B) && (timeSinceLastShopToggle >= 0.5f)) {
            timeSinceLastShopToggle = 0.0f;
            isToggled = !isToggled;
        }
        if(isToggled) {
            shop.getStage().draw();
            shop.getStage().act(delta);
        }

        batch.begin();
        font.draw(batch, "Health: " + player.getHealth() + " / " + player.getMaxHealth(), camera.position.x - camera.viewportWidth / 2, camera.position.y + camera.viewportHeight / 2);
        font.draw(batch, "Exp: " + experience, camera.position.x - camera.viewportWidth / 2, camera.position.y + camera.viewportHeight / 2 - font.getLineHeight());
        font.draw(batch, "Gold: " + player.getGold(), camera.position.x - camera.viewportWidth / 2, camera.position.y + camera.viewportHeight / 2 - font.getLineHeight() * 2);
        batch.end();

        for (College college : colleges) {
            if (!college.isActive() && !college.isConquered()) {
                for (Ship ship : ships) {
                    if (ship != null) {
                        if (ship.getParentCollegeName() == college.getCollegeName()) {
                            ship.setCollege(player.getParentCollege());
                        }
                    }
                }
                college.setConquered(true);
             }
        }

        boolean collegeActive = false;
        for (College college : colleges) {
            if (college.isActive()) {
                collegeActive = true;
                break;
            }
        }
        if (!collegeActive) {
            // all colleges are no longer active -- game over
            game.setScreen(game.winScreen);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        music.pause();
    }

    @Override
    public void dispose() {
        music.dispose();
        cannonballSound.dispose();
        batch.dispose();
        skin.dispose();
        atlas.dispose();
        shop.dispose();
        shopBatch.dispose();
        weatherStage.dispose();
        stage.dispose();
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();

        camera.translate(-x,y);
        return true;
    }


//    public void update_mouse(float deltaTime) {
//        mouse.set(Gdx.input.getX(), camera.viewportHeight - Gdx.input.getY());
//        //position.set(pointer.getX(), pointer.getY());
//        dir.set(mouse).sub(position).nor();
//        velocity.set(dir).scl(speed);
//        movement.set(velocity).scl(deltaTime);
//            position.set(mouse);
//        //pointer.setPosition(position.x, position.y);
//    }

    public GameActor getCollision(float x, float y) {
        if (ships != null) {
            for (Ship ship : ships) {
                if (ship != null) {
                    if (ship.collision(x, y)) {
                        return ship;
                    }
                }
            }
        }

        for (College college : colleges) {
            if (college != null) {
                if (college.collision(x, y)) {
                    return college;
                }
            }
        }

        for(Weather weather : weathers) {
            if (weather != null) {
                if(weather.collision(x, y)) {
                    return weather;
                }
            }
        }

//        for (Ship ship : ships) {
//            if (ship != null)
//                if (ship.collision(x, y))
//                    return ship;
//        }
        if (player != null) {
            if (player.collision(x, y)) {
                return player;
            }
        }
        return null;
    }



    public void update_keyboard() {

        float speedl = 4f + experience / 10f;
        float speedd = 2.83f + experience / 20f;



        float deltaX = 0.0f;
        float deltaY = 0.0f;

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            player.setDirection(Ship.Direction.Left);
            deltaX = -speedl;
        }else if(Gdx.input.isKeyPressed(Input.Keys.D)){
            player.setDirection(Ship.Direction.Right);
            deltaX = speedl;
        }else if(Gdx.input.isKeyPressed(Input.Keys.W)){
            player.setDirection(Ship.Direction.Up);
            deltaY = speedl;
        }else if(Gdx.input.isKeyPressed(Input.Keys.S)){
            player.setDirection(Ship.Direction.Down);
            deltaY = -speedl;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W) && (Gdx.input.isKeyPressed(Input.Keys.D))) {
            player.setDirection(Ship.Direction.UpRight);
            deltaX = speedd;
            deltaY = speedd;
        } else if(Gdx.input.isKeyPressed(Input.Keys.W)&& (Gdx.input.isKeyPressed(Input.Keys.A))){
            player.setDirection(Ship.Direction.UpLeft);
            deltaX = -speedd;
            deltaY = speedd;
        } else if(Gdx.input.isKeyPressed(Input.Keys.S)&& (Gdx.input.isKeyPressed(Input.Keys.D))){
            player.setDirection(Ship.Direction.DownRight);
            deltaX = speedd;
            deltaY = -speedd;
        } else if(Gdx.input.isKeyPressed(Input.Keys.S)&& (Gdx.input.isKeyPressed(Input.Keys.A))){
            player.setDirection(Ship.Direction.DownLeft);
            deltaX = -speedd;
            deltaY = -speedd;
        }

        player.move(deltaX, deltaY);

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && timeSinceLastCannon >= 0.75f){
            timeSinceLastCannon = 0.0f;
            if (player.getDirection() == Ship.Direction.Down) cannonball_velocity.set(0, -speedl+deltaY);
            else if (player.getDirection() == Ship.Direction.DownLeft) cannonball_velocity.set(-speedl+deltaX, -speedl+deltaY);
            else if (player.getDirection() == Ship.Direction.DownRight) cannonball_velocity.set(speedl+deltaX, -speedl+deltaY);
            else if (player.getDirection() == Ship.Direction.UpRight) cannonball_velocity.set(speedl+deltaX, speedl+deltaY);
            else if (player.getDirection() == Ship.Direction.UpLeft) cannonball_velocity.set(-speedl+deltaX, speedl+deltaY);
            else if (player.getDirection() == Ship.Direction.Up) cannonball_velocity.set(0, speedl+deltaY);
            else if (player.getDirection() == Ship.Direction.Left) cannonball_velocity.set(-speedl+deltaX, 0);
            else if (player.getDirection() == Ship.Direction.Right) cannonball_velocity.set(speedl+deltaX, 0);
            fireCannon(player, cannonball_velocity);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) game.setScreen(game.titleScreen);

        if (Gdx.input.isKeyPressed(Input.Keys.M) && timeSinceLastMusicToggle >= 0.5f) {
            timeSinceLastMusicToggle = 0.0f;
            isPlayingMusic = !isPlayingMusic;
            if (isPlayingMusic) music.play();
            else music.pause();
        }


    }

    public void addGold(int value) {
        player.addGold(value);
    }

    public void fireCannon(GameActor attacker, Vector2 velocity) {
        Cannonball ball = new Cannonball(this, attacker.getX(), attacker.getY(), velocity, attacker, colleges, ships);
        stage.addActor(ball);
        if (isPlayingMusic) cannonballSound.play();
    }
}
