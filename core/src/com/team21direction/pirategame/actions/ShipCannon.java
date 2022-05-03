package com.team21direction.pirategame.actions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.team21direction.pirategame.actors.Ship;

public class ShipCannon  extends Action {
    float timeSinceLastCannon = 0.0f;

    /**
     * Controls shooting of the cannons on NPC ships.
     * @param delta the time since the last frame.
     * @return whether the ship is still active. If the ship is dead, the action ceases.
     */
    @Override
    public boolean act(float delta) {
        double random = Math.random();
        Ship ship = (Ship)actor;
        timeSinceLastCannon += delta;
        if(timeSinceLastCannon > 6.0f + random) {
            ship.screen.fireCannon(ship, new Vector2(ship.screen.player.getX() - ship.getX(), ship.screen.player.getY() - ship.getY()).nor().scl(3.0f));
            timeSinceLastCannon = 0;
        }
        return !ship.isActive();
    }
}
