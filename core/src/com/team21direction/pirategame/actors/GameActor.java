package com.team21direction.pirategame.actors;

import com.team21direction.pirategame.screens.MainScreen;

public class GameActor extends InteractableActor {
  // the damage that the actor outputs
  private static int damage = 10;
  // a difficulty multiplier, adds a multiplier to certain things based on difficulty selected
  protected double diffMult;
  private int maxHealth = 100;
  private int health = maxHealth;
  private boolean invulnerable = false;

  public GameActor(MainScreen screen) {
    super(screen);
  }

  /**
   * Return the immutable max (starting) health of the actor.
   *
   * @return an integer representing the maximum health of the actor.
   */
  public int getMaxHealth() {
    return maxHealth;
  }

  /**
   * Return the current health of the actor.
   *
   * @return an integer representing the actor's current health: health() <= maxHealth()
   */
  public int getHealth() {
    return health;
  }

  /**
   * Sets the health to a certain parameter
   *
   * @param health the value of the health of the actor after completion.
   */
  public void setHealth(int health) {
    this.health = health;
  }

  /**
   * Adds health to the total.
   *
   * @param health the amount of health to be added.
   */
  public void addHealth(int health) {
    this.health += health;
  }

  /**
   * Sets the difficulty of the game (sets the actors health)
   *
   * @param maxHealth the maxHealth wanted depending on difficulty.
   */
  public void setDifficulty(int maxHealth) {
    this.maxHealth = maxHealth;
    this.health = maxHealth;
  }

  /**
   * Reduce the actor's health by the appropriate damage, and update `isActive` as appropriate.
   *
   * @param damage the amount of damage to inflict on the actor.
   * @return the new value of `isActive`.
   */
  public boolean attack(int damage) {
    this.health -= damage;
    this.setActive(this.health > 0);
    return this.isActive();
  }

  /**
   * Get the damage the college will do to the actor (defence).
   *
   * @return the damage to apply to the actor.
   */
  public int getDamage() {
    return damage;
  }

  /**
   * Increase **all** actors' damage by the specified delta, to allow for an increase in difficulty.
   *
   * @param delta the amount to increase actors' damage by.
   */
  public void increaseDamage(int delta) {
    damage += delta;
  }

  /**
   * Returns whether the actor is invulnerable.
   *
   * @return true if invulnerable, false otherwise.
   */
  public boolean isInvulnerable() {
    return invulnerable;
  }

  /**
   * Sets the actors invulerability status
   *
   * @param invulnerable true or false, depending on the status required
   */
  public void setInvulnerable(boolean invulnerable) {
    this.invulnerable = invulnerable;
  }
}
