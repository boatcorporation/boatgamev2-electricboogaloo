package com.team21direction.pirategame.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.team21direction.pirategame.actors.Cannonball;
import com.team21direction.pirategame.actors.College;
import com.team21direction.pirategame.actors.GameActor;
import com.team21direction.pirategame.actors.Ship;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Controls the moving of every cannonball, along with collision detection.
 */
public class CannonballAction extends Action {
  private final GameActor attacker;
  private final String collegeName;
  float liveTime = 0.0f;

  public CannonballAction(GameActor attacker, College[] colleges, ArrayList<Ship> ships) {
    super();
    this.attacker = attacker;
    if (attacker instanceof Ship) {
      this.collegeName = ((Ship) attacker).getParentCollegeName();
    } else if (attacker instanceof College) {
      this.collegeName = ((College) attacker).getCollegeName();
    } else {
      this.collegeName = "";
    }
  }
  /**
   * Move the cannonball each frame for as long as it's alive.
   *
   * @param delta the time since the last frame.
   * @return whether the cannonball is active (hence, if false, the action should cease executing on
   *     that ship).
   */
  @Override
  public boolean act(float delta) {
    liveTime += delta;
    Cannonball cannonball = (Cannonball) actor;
    float deltaX = (int) (cannonball.direction.x * 5);
    float deltaY = (int) (cannonball.direction.y * 5);
    int damage = cannonball.getDamage();
    GameActor victim =
        cannonball.screen.getGameActorCollision(
            cannonball.getX() + deltaX, cannonball.getY() + deltaY);
    String victimCollege = "";
    if (victim instanceof Ship) {
      victimCollege = ((Ship) victim).getParentCollegeName();
    } else if (victim instanceof College) {
      victimCollege = ((College) victim).getCollegeName();
    }
    // ... remove the College check for Ship collisions...
    if (victim != null && victim != this.attacker) {
      if (this.attacker instanceof Ship
          && ((Ship) this.attacker).isPlayer()
          && !(Objects.equals(this.collegeName, victimCollege))) {
        if (victim instanceof Ship) {
          victim.attack(100);
        } else {
          if (!victim.isInvulnerable()) {
            victim.attack(damage);
          }
        }
      }
      if (victim instanceof Ship && ((Ship) victim).isPlayer()) {
        victim.attack(damage);
      }
      cannonball.live = false;
      actor.remove();
      return true;
    }
    cannonball.setPosition(cannonball.getX() + deltaX, cannonball.getY() + deltaY);
    if (liveTime >= 1.5f) {
      cannonball.live = false;
      actor.remove();
    }
    return !(cannonball
        .live); // only 'complete' the action when the cannonball expires / hits something.
  }
}
