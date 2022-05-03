package com.team21direction.pirategame.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.team21direction.pirategame.Interactables.Obstacle;
import com.team21direction.pirategame.PirateGame;
import com.team21direction.pirategame.screens.MainScreen;

public class InteractableActor extends Actor {
  public MainScreen screen;
  protected float radius;
  private boolean isActive = true;
  private float x = 0.0f;
  private float y = 0.0f;

  public InteractableActor(MainScreen screen) {
    this.screen = screen;
  }

  /**
   * Return the actor's `x` coordinate in the world.
   *
   * @return a float representing the actor's location in the game world's x-axis.
   */
  public float getX() {
    return x;
  }

  /**
   * Return the actor's `y` coordinate in the world.
   *
   * @return a float representing the actor's location in the game world's y-axis.
   */
  public float getY() {
    return y;
  }

  /**
   * Return whether the actor is still active (ie, not defeated).
   *
   * @return true if the actor is still active.
   */
  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    this.isActive = active;
  }

  /**
   * Change the actor's location in the game world by absolute value.
   *
   * @param x the x-coordinate of the new location.
   * @param y the y-coordinate of the new location.
   */
  public void setPosition(float x, float y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Move the actor's location relative to their old position in the game world.
   *
   * @param deltaX how far to move the actor along the x-axis (direction determined by sign).
   * @param deltaY how far to move the actor along the y-axis (direction determined by sign).
   * @return whether the movement took place legitimately or not (false if OOB or collision).
   */
  public boolean move(float deltaX, float deltaY) {
    if (this.x + deltaX >= PirateGame.WORLD_WIDTH / 2.0f
        || this.x + deltaX <= -(PirateGame.WORLD_WIDTH / 2.0f)
        || this.y + deltaY >= PirateGame.WORLD_HEIGHT / 2.0f
        || this.y + deltaY <= -(PirateGame.WORLD_HEIGHT / 2.0f)
        || screen.getGameActorCollision(this.x + deltaX, this.y + deltaY) instanceof College
        || screen.getInteractableActorCollision(this.x + deltaX, this.y + deltaY)
            instanceof Obstacle) return false;
    this.x += deltaX;
    this.y += deltaY;
    return true;
  }

  /**
   * Returns whether there is a collision with this object and the parameter coordinates.
   *
   * @param x x-coordinate of the place to check.
   * @param y y-coordinate of the place to check.
   * @return true if collision, false otherwise.
   */
  public boolean collision(float x, float y) {
    double distance = Math.hypot(this.x - x, this.y - y);
    return (distance < this.radius);
  }
}
