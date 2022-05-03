package com.team21direction.pirategame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team21direction.pirategame.PirateGame;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TitleScreen implements Screen {
    private final PirateGame game;
    protected Stage stage;
    private final Viewport viewport;
    protected Skin skin;
    protected TextureAtlas atlas;
    private final Music music;
    private final SelectBox<String> difficulty;

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

        difficulty = new SelectBox<>(skin);
        Array<String> difficulties = new Array<>();
        difficulties.add("Select Difficulty", "Easy", "Normal", "Hard");
        difficulty.setItems(difficulties);

        Label instructions = new Label("Defeat all colleges to win\nWASD to move\nSPACE to fire cannons\n" +
                "M to toggle mute\nB to open shop\nESCAPE to pause\n1 through 5 to save", skin);

        TextButton playButton = new TextButton("New Game", skin);
        // Listen for clicks and switch to the play screen when triggered.
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                newGame();
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

        SelectBox<String> saveSelect = new SelectBox<>(skin);
        Array<String> saves = new Array<>();
        saves.add("1", "2", "3", "4");
        saves.add("5");
        saveSelect.setItems(saves);

        TextButton loadGame = new TextButton("Load Save", skin);
        // Listen for clicks and switch to the play screen when triggered.
        loadGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadGame(saveSelect.getSelected());
            }
        });

        uiTable.add(title).colspan(2);

        uiTable.row();
        uiTable.add(playButton).colspan(2).fill();

        uiTable.row();
        uiTable.add(loadGame).right().fill();
        uiTable.add(saveSelect).left().fill();

        uiTable.row();
        uiTable.add(difficulty).colspan(2).fill();

        uiTable.row();
        uiTable.add(exitButton).colspan(2).fill();

        uiTable.row();
        uiTable.add(instructions).colspan(2);

        stage.addActor(uiTable);
    }

    public void newGame() {
        this.game.mainScreen = new MainScreen(this.game, difficulty.getSelected());
        this.game.setScreen(this.game.mainScreen);
    }

    public void loadGame(String saveSelect) {
        // Only attempts to load the game if the preference files have already been written to
        try (InputStream input = new FileInputStream("save" + saveSelect + ".properties")) {
            Properties config = new Properties();
            config.load(input);

            MainScreen loadedGame = new MainScreen(this.game, config.get("Difficulty").toString());
            loadedGame.player.setPosition(Float.parseFloat(config.get("x").toString()), Float.parseFloat(config.get("y").toString()));
            loadedGame.player.setHealth(Integer.parseInt(config.get("Health").toString()));
            loadedGame.player.setGold(Integer.parseInt(config.get("Gold").toString()));
            loadedGame.setExperience(Integer.parseInt(config.get("Exp").toString()));
            loadedGame.setWeather(config);
            loadedGame.setColleges(config);
            loadedGame.setShips(config);
            loadedGame.setPowerups(config);
            loadedGame.setObjectives(config);
            this.game.mainScreen = loadedGame;
            this.game.setScreen(this.game.mainScreen);
            } catch (IOException ignored) {}
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
