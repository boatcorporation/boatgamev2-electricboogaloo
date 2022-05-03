package com.team21direction.pirategame.Interactables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.team21direction.pirategame.actors.GameActor;
import com.team21direction.pirategame.screens.MainScreen;

import java.util.List;
import java.util.Random;

public class Powerup extends GameActor {

    public enum Type {Speed, Health, Invisible, Experience, Gold}
    public Type type;
    private Texture speedTexture;
    private Sprite speedSprite;

    public Powerup(MainScreen screen) {
        super(screen);
        // selects a random type out of the enums for this instance
        type = Type.values()[new Random().nextInt(Type.values().length)];
        speedTexture = new Texture(Gdx.files.internal("powerups/" + type.toString() + ".png"));
        speedSprite = new Sprite(speedTexture);
        this.radius = 300;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
        speedTexture = new Texture(Gdx.files.internal("powerups/" + this.type.toString() + ".png"));
        speedSprite = new Sprite(speedTexture);
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(speedSprite, getX() - speedSprite.getWidth(), getY() - speedSprite.getHeight());
    }
}
