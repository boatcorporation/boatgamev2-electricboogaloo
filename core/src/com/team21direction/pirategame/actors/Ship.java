package com.team21direction.pirategame.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.team21direction.pirategame.Interactables.Powerup;
import com.team21direction.pirategame.actions.MoveRandomly;
import com.team21direction.pirategame.actions.ShipCannon;
import com.team21direction.pirategame.screens.MainScreen;

import java.util.HashMap;

public class Ship extends GameActor {

  private final boolean isPlayer;
  public College parentCollege; // The College this ship is allied with.
  private Direction direction = Direction.Right;
  private HashMap<Direction, Texture> textures;
  private Sprite texture;
  private final ShipCannon cannon;
  private int gold;
  private float speedl;
  private float speedd;
  /**
   * Construct a new Ship which is a member of the supplied parentCollege.
   *
   * @param parentCollege the College which the ship is allied to.
   */
  public Ship(MainScreen screen, College parentCollege, boolean isPlayer, double diffMult) {
    super(screen);
    this.radius = 150;
    this.parentCollege = parentCollege;
    textures = new HashMap<>();
    textures.put(
        Direction.Up,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-up.png")));
    textures.put(
        Direction.UpLeft,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-upleft.png")));
    textures.put(
        Direction.UpRight,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-upright.png")));
    textures.put(
        Direction.Left,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-left.png")));
    textures.put(
        Direction.Right,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-right.png")));
    textures.put(
        Direction.Down,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-down.png")));
    textures.put(
        Direction.DownLeft,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-downleft.png")));
    textures.put(
        Direction.DownRight,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-downright.png")));

    texture = new Sprite(textures.get(direction));
    cannon = new ShipCannon();

    this.isPlayer = isPlayer;
    this.diffMult = diffMult;
    if (!isPlayer) {
      this.setDifficulty((int) (this.getMaxHealth() * diffMult));
      this.addAction(cannon);
      this.addAction(new MoveRandomly());
    } else {
      this.setDifficulty((int) (this.getMaxHealth() / diffMult));
      this.gold = 0;
    }

    this.speedl = 4f + screen.experience / 10f;
    this.speedd = 2.83f + screen.experience / 20f;
  }

  public Ship(MainScreen screen, College parentCollege, double diffMult) {
    this(screen, parentCollege, false, diffMult);
  }

  public Direction getDirection() {
    return direction;
  }

  /**
   * Set the direction the ship is facing.
   *
   * @param direction the new direction the ship is facing.
   */
  public void setDirection(Direction direction) {
    this.direction = direction;
    texture = new Sprite(textures.get(direction));
  }

  /**
   * Get the instance of the ship's college
   *
   * @return the College that the ship belongs to
   */
  public College getParentCollege() {
    return parentCollege;
  }

  /**
   * Get the name of the college the ship belongs to
   *
   * @return the String name of the college the ship belongs to.
   */
  public String getParentCollegeName() {
    return parentCollege.getCollegeName();
  }

  /**
   * Sets the ships college, usually done when a college is captured.
   *
   * @param playerCollege the college instance that the ship should now belong to.
   */
  public void setCollege(College playerCollege) {
    this.parentCollege = playerCollege;
    textures = new HashMap<>();
    textures.put(
        Direction.Up,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-up.png")));
    textures.put(
        Direction.UpLeft,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-upleft.png")));
    textures.put(
        Direction.UpRight,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-upright.png")));
    textures.put(
        Direction.Left,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-left.png")));
    textures.put(
        Direction.Right,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-right.png")));
    textures.put(
        Direction.Down,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-down.png")));
    textures.put(
        Direction.DownLeft,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-downleft.png")));
    textures.put(
        Direction.DownRight,
        new Texture(
            Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-downright.png")));

    texture = new Sprite(textures.get(direction));
    this.removeAction(this.cannon);
  }

  /**
   * If the ship is the player, receiving fatal damage resets the screen to the loss Screen, if not
   * then the ship is simply removed.
   *
   * @param damage the amount of damage to inflict on the actor.
   * @return whether the ship is active or not.
   */
  @Override
  public boolean attack(int damage) {
    super.attack(damage);
    if (isPlayer && !isActive()) {
      // game over
      screen.game.setScreen(screen.game.lossScreen);
    }
    return isActive();
  }

  /**
   * Applies the inputted powerup to the ship. Only the player can interact with powerups.
   *
   * @param powerup the powerup to be applied
   */
  public void applyPowerup(Powerup powerup) {
    System.out.println("Powerup");
    System.out.println(powerup.getType());
    switch (powerup.getType()) {
      case Speed:
        this.speedd = (2.83f + screen.experience / 20f) * 2;
        this.speedl = (4f + screen.experience / 10f) * 2;
        break;
      case Health:
        this.addHealth(10);
        break;
      case Invisible:
        this.setVisible(false);
        break;
      case Experience:
        this.screen.experience += 5;
        break;
      case Gold:
        this.gold += 10;
        break;
    }
  }

  /** Removes all powerups from the player */
  public void removePowerup() {
    System.out.println("Removed");

    this.speedl = 4f + screen.experience / 10f;
    this.speedd = 2.83f + screen.experience / 20f;
    this.setVisible(true);
  }

  /**
   * Gets the players gold total
   *
   * @return the players gold
   */
  public int getGold() {
    return this.gold;
  }

  public void setGold(int value) {
    this.gold = value;
  }

  public void addGold(int value) {
    this.gold += value;
  }

  public boolean isPlayer() {
    return isPlayer;
  }

  /**
   * Draw the ship on the screen. This should be called once per frame by `Stage.draw()`.
   *
   * @param batch the Batch to draw the ship as part of for GPU optimisation.
   * @param parentAlpha the parent Actor's alpha value for alpha blending.
   */
  public void draw(Batch batch, float parentAlpha) {
    if (isActive()) {
      batch.draw(
          texture, getX() - (texture.getWidth() / 2.0f), getY() - (texture.getHeight() / 2.0f));
    }
  }

  public float getSpeedl() {
    return speedl;
  }

  public void setSpeedl(float speed) {
    this.speedl = speed;
  }

  public float getSpeedd() {
    return speedd;
  }

  public void setSpeedd(float speed) {
    this.speedd = speed;
  }

  public enum Direction {
    Up,
    UpLeft,
    UpRight,
    Left,
    Right,
    Down,
    DownLeft,
    DownRight
  }
}
