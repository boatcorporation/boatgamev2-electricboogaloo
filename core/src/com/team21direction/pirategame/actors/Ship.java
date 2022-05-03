package com.team21direction.pirategame.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.team21direction.pirategame.actions.MoveRandomly;
import com.team21direction.pirategame.actions.ShipCannon;
import com.team21direction.pirategame.Interactables.Powerup;
import com.team21direction.pirategame.screens.MainScreen;

import java.util.HashMap;

public class Ship extends GameActor {

    public enum Direction{Up, UpLeft, UpRight, Left, Right, Down, DownLeft, DownRight}
    private Direction direction = Direction.Right;

    public College parentCollege; // The College this ship is allied with.

    private HashMap<Direction, Texture> textures;
    private Sprite texture;
    private final ShipCannon cannon;

    private final boolean isPlayer;
    private int gold;
    private String currentPow;

    private float speedl;
    private float speedd;


    /**
     * Construct a new Ship which is a member of the supplied parentCollege.
     * @param parentCollege the College which the ship is allied to.
     */
    public Ship(MainScreen screen, College parentCollege, boolean isPlayer, double diffMult) {
        super(screen);
        this.radius = 150;
        this.parentCollege = parentCollege;
        textures = new HashMap<>();
        textures.put(Direction.Up, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-up.png")));
        textures.put(Direction.UpLeft, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-upleft.png")));
        textures.put(Direction.UpRight, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-upright.png")));
        textures.put(Direction.Left, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-left.png")));
        textures.put(Direction.Right, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-right.png")));
        textures.put(Direction.Down, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-down.png")));
        textures.put(Direction.DownLeft, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-downleft.png")));
        textures.put(Direction.DownRight, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-downright.png")));

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

    /**
     * Set the direction the ship is facing.
     * @param direction the new direction the ship is facing.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
        texture = new Sprite(textures.get(direction));
    }

    public Direction getDirection() {
        return direction;
    }

    public College getParentCollege() { return parentCollege; }

    public String getParentCollegeName() { return parentCollege.getCollegeName(); }

    public void setCollege(College playerCollege) {
        this.parentCollege = playerCollege;
        textures = new HashMap<>();
        textures.put(Direction.Up, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-up.png")));
        textures.put(Direction.UpLeft, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-upleft.png")));
        textures.put(Direction.UpRight, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-upright.png")));
        textures.put(Direction.Left, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-left.png")));
        textures.put(Direction.Right, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-right.png")));
        textures.put(Direction.Down, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-down.png")));
        textures.put(Direction.DownLeft, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-downleft.png")));
        textures.put(Direction.DownRight, new Texture(Gdx.files.internal("ships/" + parentCollege.getCollegeName() + "-ship-downright.png")));

        texture = new Sprite(textures.get(direction));
        this.removeAction(this.cannon);
        screen.addGold(screen.goldPerShip);
    }

    @Override
    public boolean attack(int damage) {
        super.attack(damage);
        if (!isActive()) {
            if (isPlayer) {
                screen.game.setScreen(screen.game.lossScreen);
            }
            screen.addGold(screen.goldPerShip);
        }
        return isActive();
    }

    public void applyPowerup(Powerup powerup) {
        currentPow = powerup.getType().toString();
        switch(powerup.getType()) {
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

    public void removePowerup() {
        this.speedl = 4f + screen.experience / 10f;
        this.speedd = 2.83f + screen.experience / 20f;
        this.setVisible(true);
        currentPow = "";
    }

    public String getCurrentPow() {
        return currentPow;
    }

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
     * Draw the ship on the screen.
     * This should be called once per frame by `Stage.draw()`.
     * @param batch the Batch to draw the ship as part of for GPU optimisation.
     * @param parentAlpha the parent Actor's alpha value for alpha blending.
     */
    public void draw(Batch batch, float parentAlpha) {
        if (isActive()) {
            batch.draw(texture, getX() - (texture.getWidth() / 2.0f), getY() - (texture.getHeight() / 2.0f));
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
}
