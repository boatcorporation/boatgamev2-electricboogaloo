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
        String[] files = { "Constantine-ship-down", 
                        "Constantine-ship-downleft",
                        "Constantine-ship-downright",
                        "Constantine-ship-left",
                        "Constantine-ship-right",
                        "Constantine-ship-up",
                        "Constantine-ship-upleft",
                        "Constantine-ship-upright" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/ships/" + f + ".png").exists());
		}
    }

    @Test 
    public void DerwentShipExists(){
        String[] files = { "Derwent-ship-down",
        "Derwent-ship-downleft",
        "Derwent-ship-downright",
        "Derwent-ship-left",
        "Derwent-ship-right",
        "Derwent-ship-up",
        "Derwent-ship-upleft",
        "Derwent-ship-upright" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/ships/" + f + ".png").exists());
		}
    }

    @Test 
    public void HalifaxShipExists(){
        String[] files = { "Halifax-ship-down",
        "Halifax-ship-downleft",
        "Halifax-ship-downright",
        "Halifax-ship-left",
        "Halifax-ship-right",
        "Halifax-ship-up",
        "Halifax-ship-upleft",
        "Halifax-ship-upright" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/ships/" + f + ".png").exists());
		}
    }

    @Test
    public void LangwithShipExists(){
        String[] files = { "Langwith-ship-down",
        "Langwith-ship-downleft",
        "Langwith-ship-downright",
        "Langwith-ship-left",
        "Langwith-ship-right",
        "Langwith-ship-up",
        "Langwith-ship-upleft",
        "Langwith-ship-upright" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/ships/" + f + ".png").exists());
		}
    }

    @Test
    public void VanbrughShipExists(){
        String[] files = { "Vanbrugh-ship-down",
        "Vanbrugh-ship-downleft",
        "Vanbrugh-ship-downright",
        "Vanbrugh-ship-left",
        "Vanbrugh-ship-right",
        "Vanbrugh-ship-up",
        "Vanbrugh-ship-upleft",
        "Vanbrugh-ship-upright" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/ships/" + f + ".png").exists());
		}
    }
    

}
