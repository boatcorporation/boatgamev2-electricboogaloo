
package de.boatcorporation.gdxtesting.screentests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.BeforeClass;


import com.badlogic.gdx.Gdx;

import de.boatcorporation.gdxtesting.GdxTestRunner;

import com.team21direction.pirategame.screens.MainScreen;
import com.team21direction.pirategame.PirateGame;

import com.badlogic.gdx.Game;



@RunWith(GdxTestRunner.class)
public class screenTests {
    
    private static MainScreen screen;
    private static PirateGame game;



    @BeforeClass
    public static void init()
    {
        game = new PirateGame();
        screen = new MainScreen(game, "normal");

    }


}
