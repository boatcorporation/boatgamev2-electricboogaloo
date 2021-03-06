package com.team21direction.pirategame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.team21direction.pirategame.interactables.Objective;
import com.team21direction.pirategame.interactables.Obstacle;
import com.team21direction.pirategame.interactables.Powerup;
import com.team21direction.pirategame.interactables.Weather;
import com.team21direction.pirategame.PirateGame;
import com.team21direction.pirategame.actors.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

/**
 * The main screen of the game whilst the game is being played.
 */
public class MainScreen implements Screen {

  public final PirateGame game;
  public final ArrayList<Ship> ships;
  public final ArrayList<Objective> objectives;
  public final String finalBoss;
  public final String replacement;
  public final Ship player;
  private final Batch batch;
  private final Batch shopBatch;
  private final Viewport viewport;
  private final BitmapFont font;
  private final Music music;
  private final Sound cannonballSound;
  private final Shop shop;
  private final OrthographicCamera camera;
  private final Weather[] weathers;
  private final Obstacle[] obstacles;
  private final College[] colleges;
  private final ArrayList<Powerup> powerups;
  private final Vector2 position = new Vector2();
  private final Vector2 cannonball_velocity = new Vector2();
  private final Vector2 movement = new Vector2();
  private final Vector2 mouse = new Vector2();
  private final Vector2 dir = new Vector2();
  private final String difficulty;
  public int experience = 0;
  /** Amount of experience gained when taking over a College. */
  public int experiencePerCollege = 10;
  /** Amount of gold gained when razing a College. */
  public int goldPerCollege = 10;
  public int goldPerShip = 5;
  protected Stage stage;
  protected Stage weatherStage;
  protected Skin skin;
  protected TextureAtlas atlas;
  boolean isToggled = false;
  private float timeSinceLastCannon = 0.0f;
  private float timeSinceLastExpDrop = 0.0f;
  private float timeSinceLastMusicToggle = 0.0f;
  private float timeSinceLastShopToggle = 0.0f;
  private float timeSinceLastPowerup = 0.0f;
  private boolean isPoweredUp = false;
  private float timeSinceLastSave = 0.0f;
  private float timeSinceLastCollision = 0.0f;
  private boolean isPlayingMusic = true;

  /**
   * Instantiate the main screen and all entities required for the start of the game.
   *
   * @param game the game instance.
   * @param difficulty the selected difficulty.
   */
  public MainScreen(PirateGame game, String difficulty) {
    this.difficulty = difficulty;
    this.game = game;
    skin = new Skin(Gdx.files.internal("uiskin.json"));
    atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
    font = new BitmapFont();
    font.getData().setScale(4.0f);

    camera = new OrthographicCamera();

    batch = new SpriteBatch();
    shopBatch = new SpriteBatch();
    Batch weatherBatch = new SpriteBatch();

    viewport = new FitViewport(2670, 2000, camera);
    viewport.apply();

    camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    camera.update();

    music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
    music.setLooping(true);

    cannonballSound = Gdx.audio.newSound(Gdx.files.internal("cannonball.mp3"));

    double difficultyMultiplier = 1;
    if (Objects.equals(this.difficulty, "Easy")) {
      difficultyMultiplier = 0.5;
    } else if (Objects.equals(this.difficulty, "Hard")) {
      difficultyMultiplier = 2;
    }

    stage = new Stage(viewport, batch);
    weatherStage = new Stage(viewport, weatherBatch);

    colleges =
        new College[] {
          new College(this, "Derwent", difficultyMultiplier),
          new College(this, "Langwith", difficultyMultiplier),
          new College(this, "Constantine", difficultyMultiplier),
          new College(this, "Halifax", difficultyMultiplier),
        };

    // Spawn all weather entities and move them randomly
    weathers = new Weather[200];
    for (int i = 0; i < 200; i++) {
      weathers[i] = new Weather(this);
      boolean success;
      do {
        success =
            weathers[i].move(
                (float) (Math.random() * PirateGame.WORLD_WIDTH) - PirateGame.WORLD_WIDTH / 2.0f,
                (float) (Math.random() * PirateGame.WORLD_HEIGHT) - PirateGame.WORLD_WIDTH / 2.0f);
      } while (!success);
      weatherStage.addActor(weathers[i]);
    }

    // Spawn all powerups and move them randomly.
    powerups = new ArrayList<>();
    for (int i = 0; i < 15; i++) {
      Powerup tempPowerup = new Powerup(this);
      powerups.add(tempPowerup);
      boolean success;
      do {
        success =
            tempPowerup.move(
                (float) (Math.random() * PirateGame.WORLD_WIDTH) - PirateGame.WORLD_WIDTH / 2.0f,
                (float) (Math.random() * PirateGame.WORLD_HEIGHT) - PirateGame.WORLD_WIDTH / 2.0f);
      } while (!success);
      stage.addActor(tempPowerup);
    }

    // Spawn all obstacles on the map and move them randomly
    obstacles = new Obstacle[50];
    for (int i = 0; i < 50; i++) {
      obstacles[i] = new Obstacle(this);
      boolean success;
      do {
        success =
            obstacles[i].move(
                (float) (Math.random() * PirateGame.WORLD_WIDTH) - PirateGame.WORLD_WIDTH / 2.0f,
                (float) (Math.random() * PirateGame.WORLD_HEIGHT) - PirateGame.WORLD_WIDTH / 2.0f);
      } while (!success);
      stage.addActor(obstacles[i]);
    }

    // Spawn all starting points for ships & their colleges on the map and move them randomly
    ships = new ArrayList<>();
    for (College college : colleges) {
      boolean success;
      do {
        success =
            college.move(
                (float) (Math.random() * PirateGame.WORLD_WIDTH) - PirateGame.WORLD_WIDTH / 2.0f,
                (float) (Math.random() * PirateGame.WORLD_HEIGHT) - PirateGame.WORLD_WIDTH / 2.0f);
      } while (!success);

      stage.addActor(college);
      for (int j = 0; j < PirateGame.SHIPS_PER_COLLEGE; j++) {
        Ship tempShip = new Ship(this, college, difficultyMultiplier);
        ships.add(tempShip);
        do {
          success =
              tempShip.move(
                  (float) (Math.random() * PirateGame.WORLD_WIDTH) - PirateGame.WORLD_WIDTH / 2.0f,
                  (float) (Math.random() * PirateGame.WORLD_HEIGHT)
                      - PirateGame.WORLD_WIDTH / 2.0f);
        } while (!success);
        stage.addActor(tempShip);
      }
    }

    // Spawn the player and move them to a random point in the map. The default college is Vanbrugh.
    player =
        new Ship(
            this, new College(this, "Vanbrugh", difficultyMultiplier), true, difficultyMultiplier);
    boolean success;
    do {
      success =
          player.move(
              (float) (Math.random() * PirateGame.WORLD_WIDTH) - PirateGame.WORLD_WIDTH / 2.0f,
              (float) (Math.random() * PirateGame.WORLD_HEIGHT) - PirateGame.WORLD_WIDTH / 2.0f);
    } while (!success);
    stage.addActor(player);

    shop = new Shop(shopBatch, player, this);

    // Objective handling
    objectives = new ArrayList<>();
    double numObj = 4 * difficultyMultiplier;
    int intObj = (int) numObj;
    int shipObjs;
    do {
      objectives.clear();
      shipObjs = 0;
      for (int i = 0; i < intObj; i++) {
        Objective tempObj = new Objective(colleges, false);
        if (Objective.Type.HaveShips == tempObj.getType()) {
          shipObjs++;
        }
        objectives.add(tempObj);
      }
    } while (shipObjs > 3);

    objectives.add(new Objective(colleges, true));
    finalBoss = objectives.get(objectives.size() - 1).getToDestroy();
    for (College college : colleges) {
      if (Objects.equals(college.getCollegeName(), finalBoss)) {
        college.setInvulnerable(true);
        break;
      }
    }
    replacement =
        objectives
            .get(objectives.size() - 1)
            .getDisplay()
            .substring(0, objectives.get(objectives.size() - 1).getDisplay().length() - 15);
    objectives.get(0).startTracking(player, ships, this);
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setExperience(int experience) {
    this.experience = experience;
  }

  public Weather[] getWeather() {
    return weathers;
  }

  public void setWeather(Properties config) {
    for (int i = 0; i < weathers.length; i++) {
      weathers[i].setPosition(
          Float.parseFloat(config.get("weatherX" + i).toString()),
          Float.parseFloat(config.get("weatherY" + i).toString()));
    }
  }

  public College[] getColleges() {
    return colleges;
  }

  public void setColleges(Properties config) {
    for (int i = 0; i < colleges.length; i++) {
      colleges[i].setPosition(
          Float.parseFloat(config.get("collegeX" + i).toString()),
          Float.parseFloat(config.get("collegeY" + i).toString()));
      colleges[i].setHealth(Integer.parseInt(config.get("collegeH" + i).toString()));
    }
  }

  public ArrayList<Ship> getShips() {
    return ships;
  }

  public void setShips(Properties config) {
    int count = 0;
    for (Ship ship : ships) {
      ship.setPosition(
          Float.parseFloat(config.get("shipX" + count).toString()),
          Float.parseFloat(config.get("shipY" + count).toString()));
      ship.setHealth((int) Float.parseFloat(config.get("shipH" + count).toString()));
      count++;
    }
  }

  public ArrayList<Powerup> getPowerups() {
    return powerups;
  }

  public void setPowerups(Properties config) {
    int count = 0;
    for (Powerup powerup : powerups) {
      powerup.setPosition(
          Float.parseFloat(config.get("powerX" + count).toString()),
          Float.parseFloat(config.get("powerY" + count).toString()));
      powerup.setType(Powerup.Type.valueOf(config.get("powerT" + count).toString()));
      count++;
    }
  }

  public ArrayList<Objective> getObjectives() {
    return objectives;
  }

  public void setObjectives(Properties config) {
    int count = 0;
    for (Objective objective : objectives) {
      objective.setActive(Boolean.parseBoolean(config.get("objActive" + count).toString()));
      objective.setType(config.get("objType" + count).toString());
      objective.setFinalObjective(Boolean.parseBoolean(config.get("objFinal" + count).toString()));
      objective.setGoalNum(Integer.parseInt(config.get("objGoal" + count).toString()));
      objective.setToDestroy(config.get("objDestroy" + count).toString());
      objective.setStartGold(Integer.parseInt(config.get("objGold" + count).toString()));
      objective.setStartXP(Integer.parseInt(config.get("objXP" + count).toString()));
      objective.setStartShips(Integer.parseInt(config.get("objShips" + count).toString()));
      objective.setDisplay(config.get("objDisplay" + count).toString());
      count++;
    }
  }

  public Obstacle[] getObstacles() {
    return obstacles;
  }

  public void setObstacles(Properties config) {
    for (int i = 0; i < obstacles.length; i++) {
      obstacles[i].setPosition(
          Float.parseFloat(config.get("obstacleX" + i).toString()),
          Float.parseFloat(config.get("obstacleY" + i).toString()));
    }
  }

  public void saveGame(MainScreen savedScreen, String fileName) {
    try (OutputStream output = new FileOutputStream(fileName)) {
      Properties config = new Properties();

      config.put("Difficulty", savedScreen.getDifficulty());
      config.put("Gold", Integer.toString(savedScreen.player.getGold()));
      config.put("Health", Integer.toString(savedScreen.player.getHealth()));
      config.put("Exp", Integer.toString(savedScreen.experience));
      config.put("x", Float.toString(savedScreen.player.getX()));
      config.put("y", Float.toString(savedScreen.player.getY()));

      // Saves the position of each weather item
      int weatherIndex = 0;
      for (Weather weather : savedScreen.getWeather()) {
        config.put("weatherX" + weatherIndex, Float.toString(weather.getX()));
        config.put("weatherY" + weatherIndex, Float.toString(weather.getY()));
        weatherIndex++;
      }

      // Saves the position and health of each college
      int collegeIndex = 0;
      for (College college : savedScreen.getColleges()) {
        config.put("collegeX" + collegeIndex, Float.toString(college.getX()));
        config.put("collegeY" + collegeIndex, Float.toString(college.getY()));
        config.put("collegeH" + collegeIndex, Integer.toString(college.getHealth()));
        collegeIndex++;
      }

      // Saves the position and health of each ship
      int shipIndex = 0;
      for (Ship ship : savedScreen.getShips()) {
        config.put("shipX" + shipIndex, Float.toString(ship.getX()));
        config.put("shipY" + shipIndex, Float.toString(ship.getY()));
        config.put("shipH" + shipIndex, Float.toString(ship.getHealth()));
        shipIndex++;
      }

      // Saves position and type of each powerup
      int powerIndex = 0;
      for (Powerup powerup : savedScreen.getPowerups()) {
        config.put("powerX" + powerIndex, Float.toString(powerup.getX()));
        config.put("powerY" + powerIndex, Float.toString(powerup.getY()));
        config.put("powerT" + powerIndex, powerup.getType().toString());
        powerIndex++;
      }

      // Saves each objective
      int objIndex = 0;
      for (Objective objective : savedScreen.getObjectives()) {
        config.put("objActive" + objIndex, Boolean.toString(objective.isActive()));
        config.put("objType" + objIndex, objective.getType().toString());
        config.put("objFinal" + objIndex, Boolean.toString(objective.isFinalObjective()));
        config.put("objGoal" + objIndex, Integer.toString(objective.getGoalNum()));
        config.put("objDestroy" + objIndex, objective.getToDestroy());
        config.put("objGold" + objIndex, Integer.toString(objective.getStartGold()));
        config.put("objXP" + objIndex, Integer.toString(objective.getStartXP()));
        config.put("objShips" + objIndex, Integer.toString(objective.getStartShips()));
        config.put("objDisplay" + objIndex, objective.getDisplay());
        objIndex++;
      }

      // Saves the position of each obstacle
      int obstIndex = 0;
      for (Obstacle obstacle : savedScreen.getObstacles()) {
        config.put("obstacleX" + obstIndex, Float.toString(obstacle.getX()));
        config.put("obstacleY" + obstIndex, Float.toString(obstacle.getY()));
        obstIndex++;
      }

      config.store(output, null);

    } catch (IOException ignored) {
    }
    System.out.println("Saved!");
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
    timeSinceLastPowerup += delta;
    timeSinceLastSave += delta;
    timeSinceLastCollision += delta;

    InteractableActor collidingWith = getInteractableActorCollision(player.getX(), player.getY());
    if (collidingWith instanceof Weather) {
      if (timeSinceLastExpDrop >= 2.0f) {
        player.attack(1);
        experience++;
        timeSinceLastExpDrop = 0.0f;
      }
    }
    if (collidingWith instanceof Obstacle) {
      if (timeSinceLastCollision >= 2.0f) {
        player.attack(25);
        experience--;
        timeSinceLastCollision = 0.0f;
      }
    }
    if (!isPoweredUp) {
      if (collidingWith instanceof Powerup) {
        isPoweredUp = true;
        collidingWith.remove();
        player.applyPowerup((Powerup) collidingWith);
        timeSinceLastPowerup = 0;
      }
    } else {
      if (timeSinceLastPowerup > 2.0f) {
        isPoweredUp = false;
        player.removePowerup();
      }
    }

    if (timeSinceLastExpDrop >= 10.0f) {
      timeSinceLastExpDrop = 0.0f;
      experience++;
    }

    // update(Gdx.graphics.getDeltaTime());

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
    if (isToggled) {
      shop.getStage().draw();
      shop.getStage().act(delta);
    }

    for (int i = 0; i < objectives.size(); i++) {
      if (objectives.get(i).isActive()) {
        objectives.get(i).checkActive(player, ships, colleges, this);
        if (!(objectives.get(i).isActive())) {
          if (i == objectives.size() - 1) {
            // No objectives left; The player wins
            this.game.setScreen(this.game.winScreen);
          } else if (i == objectives.size() - 2) {
            // Final objective
            objectives.get(objectives.size() - 1).setFinalObjective(false);
            objectives.get(objectives.size() - 1).setDisplay(replacement);
            for (College college : colleges) {
              if (Objects.equals(college.getCollegeName(), finalBoss)) {
                college.setInvulnerable(false);
                break;
              }
            }
          } else {
            objectives.get(i + 1).startTracking(player, ships, this);
          }
        }
        break;
      }
    }

    batch.begin();
    font.draw(
        batch,
        "Health: " + player.getHealth() + " / " + player.getMaxHealth(),
        camera.position.x - camera.viewportWidth / 2,
        camera.position.y + camera.viewportHeight / 2);
    font.draw(
        batch,
        "Exp: " + experience,
        camera.position.x - camera.viewportWidth / 2,
        camera.position.y + camera.viewportHeight / 2 - font.getLineHeight());
    font.draw(
        batch,
        "Gold: " + player.getGold(),
        camera.position.x - camera.viewportWidth / 2,
        camera.position.y + camera.viewportHeight / 2 - font.getLineHeight() * 2);
    font.draw(
        batch,
        "",
        camera.position.x - camera.viewportWidth / 2,
        camera.position.y + camera.viewportHeight / 2 - font.getLineHeight() * 3);
    font.draw(
        batch,
        "Objectives: ",
        camera.position.x - camera.viewportWidth / 2,
        camera.position.y + camera.viewportHeight / 2 - font.getLineHeight() * 4);
    int activeObjs = 0;
    for (Objective objective : objectives) {
      if (objective.isActive()) {
        if (objective.isFinalObjective()) {
          font.setColor(Color.RED);
          font.draw(
              batch,
              objective.getDisplay(),
              camera.position.x - camera.viewportWidth / 2,
              camera.position.y
                  + camera.viewportHeight / 2
                  - font.getLineHeight() * (activeObjs + 5));
          font.setColor(Color.WHITE);
        } else {
          font.setColor(Color.WHITE);
          font.draw(
              batch,
              objective.getDisplay(),
              camera.position.x - camera.viewportWidth / 2,
              camera.position.y
                  + camera.viewportHeight / 2
                  - font.getLineHeight() * (activeObjs + 5));
        }
        activeObjs++;
      }
    }
    batch.end();

    for (College college : colleges) {
      if (!college.isActive() && !college.isConquered()) {
        for (Ship ship : ships) {
          if (ship != null) {
            if (Objects.equals(ship.getParentCollegeName(), college.getCollegeName())) {
              ship.setCollege(player.getParentCollege());
            }
          }
        }
        college.setConquered(true);
      }
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

  public InteractableActor getInteractableActorCollision(float x, float y) {
    for (Weather weather : weathers) {
      if (weather != null) {
        if (weather.collision(x, y)) {
          return weather;
        }
      }
    }

    if (powerups != null) {
      for (Powerup powerup : powerups) {
        if (powerup != null) {
          if (powerup.collision(x, y)) {
            return powerup;
          }
        }
      }
    }

    if (obstacles != null) {
      for (Obstacle obstacle : obstacles) {
        if (obstacle != null) {
          if (obstacle.collision(x, y)) {
            return obstacle;
          }
        }
      }
    }
    return null;
  }

  public GameActor getGameActorCollision(float x, float y) {
    if (ships != null) {
      for (Ship ship : ships) {
        if (ship != null && ship.isActive()) {
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

    if (player != null) {
      if (player.collision(x, y)) {
        return player;
      }
    }
    return null;
  }

  public void update_keyboard() {

    float speedl = player.getSpeedl();
    float speedd = player.getSpeedd();

    float deltaX = 0.0f;
    float deltaY = 0.0f;

    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      player.setDirection(Ship.Direction.Left);
      deltaX = -speedl;
    } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      player.setDirection(Ship.Direction.Right);
      deltaX = speedl;
    } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      player.setDirection(Ship.Direction.Up);
      deltaY = speedl;
    } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      player.setDirection(Ship.Direction.Down);
      deltaY = -speedl;
    }
    if (Gdx.input.isKeyPressed(Input.Keys.W) && (Gdx.input.isKeyPressed(Input.Keys.D))) {
      player.setDirection(Ship.Direction.UpRight);
      deltaX = speedd;
      deltaY = speedd;
    } else if (Gdx.input.isKeyPressed(Input.Keys.W) && (Gdx.input.isKeyPressed(Input.Keys.A))) {
      player.setDirection(Ship.Direction.UpLeft);
      deltaX = -speedd;
      deltaY = speedd;
    } else if (Gdx.input.isKeyPressed(Input.Keys.S) && (Gdx.input.isKeyPressed(Input.Keys.D))) {
      player.setDirection(Ship.Direction.DownRight);
      deltaX = speedd;
      deltaY = -speedd;
    } else if (Gdx.input.isKeyPressed(Input.Keys.S) && (Gdx.input.isKeyPressed(Input.Keys.A))) {
      player.setDirection(Ship.Direction.DownLeft);
      deltaX = -speedd;
      deltaY = -speedd;
    }

    player.move(deltaX, deltaY);

    if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && timeSinceLastCannon >= 0.75f) {
      timeSinceLastCannon = 0.0f;
      if (player.getDirection() == Ship.Direction.Down)
        cannonball_velocity.set(0, -speedl + deltaY);
      else if (player.getDirection() == Ship.Direction.DownLeft)
        cannonball_velocity.set(-speedl + deltaX, -speedl + deltaY);
      else if (player.getDirection() == Ship.Direction.DownRight)
        cannonball_velocity.set(speedl + deltaX, -speedl + deltaY);
      else if (player.getDirection() == Ship.Direction.UpRight)
        cannonball_velocity.set(speedl + deltaX, speedl + deltaY);
      else if (player.getDirection() == Ship.Direction.UpLeft)
        cannonball_velocity.set(-speedl + deltaX, speedl + deltaY);
      else if (player.getDirection() == Ship.Direction.Up)
        cannonball_velocity.set(0, speedl + deltaY);
      else if (player.getDirection() == Ship.Direction.Left)
        cannonball_velocity.set(-speedl + deltaX, 0);
      else if (player.getDirection() == Ship.Direction.Right)
        cannonball_velocity.set(speedl + deltaX, 0);
      fireCannon(player, cannonball_velocity);
    }

    if (Gdx.input.isKeyPressed(Input.Keys.NUM_1) && timeSinceLastSave >= 1.0f) {
      timeSinceLastSave = 0.0f;
      this.saveGame(this, "save1.properties");
    } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2) && timeSinceLastSave >= 1.0f) {
      timeSinceLastSave = 0.0f;
      this.saveGame(this, "save2.properties");
    } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3) && timeSinceLastSave >= 1.0f) {
      timeSinceLastSave = 0.0f;
      this.saveGame(this, "save3.properties");
    } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4) && timeSinceLastSave >= 1.0f) {
      timeSinceLastSave = 0.0f;
      this.saveGame(this, "save4.properties");
    } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_5) && timeSinceLastSave >= 1.0f) {
      timeSinceLastSave = 0.0f;
      this.saveGame(this, "save5.properties");
    }

    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
      this.game.setScreen(this.game.titleScreen);
    }

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
    Cannonball ball =
        new Cannonball(this, attacker.getX(), attacker.getY(), velocity, attacker, colleges, ships);
    stage.addActor(ball);
    if (isPlayingMusic) cannonballSound.play();
  }
}
