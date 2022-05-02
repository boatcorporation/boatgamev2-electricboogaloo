package de.boatcorporation.gdxtesting.general;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;

import de.boatcorporation.gdxtesting.GdxTestRunner;


@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void WeatherExists() {
		String[] files = { "weather" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/weather/" + f + ".png").exists());
		}
	}

    @Test
    public void ConstantineShipExists(){
        String[] files = { "Constantine-ship-down.png", 
                        "Constantine-ship-downleft.png",
                        "Constantine-ship-downright.png",
                        "Constantine-ship-left.png",
                        "Constantine-ship-right.png",
                        "Constantine-ship-up.png",
                        "Constantine-ship-upleft.png",
                        "Constantine-ship-upright.png" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/ships/" + f + ".png").exists());
		}
    }

    @Test 
    public void DerwentShipExists(){
        String[] files = { "Derwent-ship-down.png",
        "Derwent-ship-downleft.png",
        "Derwent-ship-downright.png",
        "Derwent-ship-left.png",
        "Derwent-ship-right.png",
        "Derwent-ship-up.png",
        "Derwent-ship-upleft.png",
        "Derwent-ship-upright.png" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/ships/" + f + ".png").exists());
		}
    }

    @Test 
    public void HalifaxShipExists(){
        String[] files = { "Halifax-ship-down.png",
        "Halifax-ship-downleft.png",
        "Halifax-ship-downright.png",
        "Halifax-ship-left.png",
        "Halifax-ship-right.png",
        "Halifax-ship-up.png",
        "Halifax-ship-upleft.png",
        "Halifax-ship-upright.png" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/ships/" + f + ".png").exists());
		}
    }
    
}
