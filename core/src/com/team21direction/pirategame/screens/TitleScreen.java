package com.team21direction.pirategame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team21direction.pirategame.PirateGame;

public class TitleScreen implements Screen {
    private final PirateGame game;
    protected Stage stage;
    private final Viewport viewport;
    protected Skin skin;
    protected TextureAtlas atlas;
    private final Music music;

    private final OrthographicCamera camera;

    private float timeSinceLastMusicToggle = 0.0f;
    private boolean isPlayingMusic = true;

    /**
     * TitleScreen is the Screen for the main menu.
     * @param game the PirateGame object to allow this screen to trigger a screen change when the play button is pressed.
     */
    public TitleScreen(PirateGame game) {
        this.game = game;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));

        camera = new OrthographicCamera();

        viewport = new FitViewport(300, 300, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        music = Gdx.audio.newMusic(Gdx.files.internal("titleMusic.ogg"));
        music.setLooping(true);

        stage = new Stage(viewport);

        Table uiTable = new Table();
        uiTable.setFillParent(true);
        uiTable.top();

        Label title = new Label(PirateGame.TITLE, skin);

        final SelectBox difficulty = new SelectBox(skin);
        Array<String> difficulties= new Array();
        difficulties.add("Select Difficulty", "Easy", "Normal", "Hard");
        difficulty.setItems(difficulties);

        Label instructions = new Label("Defeat all colleges to win.\nWASD to move.\nSPACE to fire cannons.\nM to toggle mute.\nB to open shop\nESCAPE to pause.", skin);

        TextButton playButton = new TextButton("New Game", skin);
        // Listen for clicks and switch to the play screen when triggered.
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TitleScreen.this.game.mainScreen = new MainScreen(TitleScreen.this.game, difficulty.getSelected().toString());
                TitleScreen.this.game.setScreen(TitleScreen.this.game.mainScreen);
            }
        });

        TextButton exitButton = new TextButton("Quit", skin);
        // Listen for clicks and switch to the play screen when triggered.
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        uiTable.add(title);
        uiTable.row();

        uiTable.add(playButton);
        uiTable.row();

        uiTable.add(difficulty);
        uiTable.row();

        uiTable.add(exitButton);
        uiTable.row();

        uiTable.add(instructions);
        stage.addActor(uiTable);
    }

    /**
     * show() is called when the screen becomes visible.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        if (isPlayingMusic) music.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0.6f, 1, 1);
        stage.act();
        stage.draw();

        timeSinceLastMusicToggle += delta;
        if (Gdx.input.isKeyPressed(Input.Keys.M) && timeSinceLastMusicToggle >= 0.5f) {
            timeSinceLastMusicToggle = 0.0f;
            isPlayingMusic = !isPlayingMusic;
            if (isPlayingMusic) music.play();
            else music.pause();
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
        skin.dispose();
        atlas.dispose();
    }
}