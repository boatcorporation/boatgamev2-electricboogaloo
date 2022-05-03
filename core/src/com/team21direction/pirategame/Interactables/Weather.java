package com.team21direction.pirategame.Interactables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.team21direction.pirategame.actors.GameActor;
import com.team21direction.pirategame.screens.MainScreen;

public class Weather extends GameActor {

  private final Texture weatherTexture;
  private final Sprite weatherSprite;

  public Weather(MainScreen screen) {
    super(screen);
    weatherTexture = new Texture(Gdx.files.internal("weather/weather.png"));
    weatherSprite = new Sprite(weatherTexture);
    this.radius = 150;
  }

  public void draw(Batch batch, float parentAlpha) {
    batch.draw(
        weatherSprite, getX() - weatherSprite.getWidth(), getY() - weatherSprite.getHeight());
  }
}
