package com.team21direction.pirategame.Interactables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.team21direction.pirategame.actors.GameActor;
import com.team21direction.pirategame.screens.MainScreen;

public class Obstacle extends GameActor {

  private final Texture texture;
  private final Sprite sprite;

  public Obstacle(MainScreen screen) {
    super(screen);
    texture = new Texture(Gdx.files.internal("rock.png"));
    sprite = new Sprite(texture);
    this.radius = 100;
  }

  public void draw(Batch batch, float parentAlpha) {
    batch.draw(sprite, getX() - sprite.getWidth(), getY() - sprite.getHeight());
  }
}
