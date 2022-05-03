package com.team21direction.pirategame.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.team21direction.pirategame.Interactables.Weather;
import com.team21direction.pirategame.actors.*;

import java.util.ArrayList;
import java.util.Objects;

public class CannonballAction extends Action {
    float liveTime = 0.0f;
    private final GameActor attacker;
    private final String collegeName;
    private String victimCollege;

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
        this.victimCollege = "";
    }
    /**
     * Move the cannonball each frame for as long as it's alive.
     * @param delta the time since the last frame.
     * @return whether the cannonball is active (hence, if false, the action should cease executing on that ship).
     */
    @Override
    public boolean act(float delta) {
        liveTime += delta;
        Cannonball cannonball = (Cannonball) actor;
        float deltaX = (int)(cannonball.direction.x * 5);
        float deltaY = (int)(cannonball.direction.y * 5);
        int damage = cannonball.getDamage();
        GameActor victim = cannonball.screen.getCollision(cannonball.getX() + deltaX, cannonball.getY() + deltaY);
        // ... remove the College check for Ship collisions...
        if(victim instanceof Ship && !(((Ship) victim).isPlayer()) && victim != this.attacker) {
            victim.attack(100);
            cannonball.live = false;
            actor.remove();
            return true;
        } /*
        if (victim instanceof Ship) {
            this.victimCollege = ((Ship) victim).getParentCollegeName();
        } else if (victim instanceof College) {
            this.victimCollege = ((College) victim).getCollegeName();
        } */
        if (victim != null && victim != this.attacker && !(victim instanceof Weather)) {
            if (!Objects.equals(this.collegeName, this.victimCollege)) {
                victim.attack(damage);
                cannonball.live = false;
                actor.remove();
                return true;
            }
        }
        cannonball.setPosition(cannonball.getX() + deltaX, cannonball.getY() + deltaY);
        if (liveTime >= 1.5f) {
            cannonball.live = false;
            actor.remove();
        }
        return !(cannonball.live); // only 'complete' the action when the cannonball expires / hits something.
    }
}
