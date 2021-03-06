package com.team21direction.pirategame.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.team21direction.pirategame.actors.College;

/**
 * Controls the surrender animations of the colleges.
 */
public class WhiteFlagRoutine extends Action {
  float totalAnimationDuration = 0.0f;
  int animState = 0;

  /**
   * Acts out the animations for the changing of sprites of a college upon death
   * @param delta the time since last frame
   * @return true when the animation is complete, finishing the act.
   */
  @Override
  public boolean act(float delta) {
    College college = (College) actor;
    totalAnimationDuration += delta;
    int newAnimState = (int) (totalAnimationDuration / 0.5f);
    if (animState != newAnimState && newAnimState <= 3) {
      animState = newAnimState;
      college.collegeBase =
          new Sprite(
              new Texture(Gdx.files.internal("colleges/college-defeated-" + animState + ".png")));
    }
    if (totalAnimationDuration >= 10.0f) {
      // mercy; the College joins the player's College.
      college.screen.experience += college.screen.experiencePerCollege;
      college.setCollegeName(college.screen.player.parentCollege.getCollegeName());
      college.isWhiteFlag = false;
      college.collegeBase =
          new Sprite(new Texture(Gdx.files.internal("colleges/college-defeated-0.png")));
      college.clearActions();
    }
    return totalAnimationDuration >= 10.0f; // only 'complete' the action when the college is killed.
  }
}
