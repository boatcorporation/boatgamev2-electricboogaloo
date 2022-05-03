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

    @Test
    public void CollegeExists(){
        String[] files = { "Constantine-college-fullhealth",
            "Derwent-college-fullhealth",
            "Halifax-college-fullhealth",
            "Langwith-college-fullhealth",
            "Vanbrugh-college-fullhealth" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/colleges/" + f + ".png").exists());
		}
    }

    @Test
    public void CollegeDefeatExists(){
        String[] files = { "college-defeated-0",
        "college-defeated-1",
        "college-defeated-2",
        "college-defeated-3",
        "college-halfhealth" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/colleges/" + f + ".png").exists());
		}
    }

    @Test
    public void MusicExists(){
        String[] files = { "cannonball", "music" };
		for (String f : files) {
			assertTrue("the file " + f + ".mp3 does not exist",
					Gdx.files.internal("../core/assets/" + f + ".mp3").exists());
		}
    }

    @Test
    public void MiscPngExists(){
        String[] files = { "cannonball", "uiskin" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/" + f + ".png").exists());
		}
    }

    @Test
    public void OggSoundsExists(){
        String[] files = { "titleMusic" };
		for (String f : files) {
			assertTrue("the file " + f + ".ogg does not exist",
					Gdx.files.internal("../core/assets/" + f + ".ogg").exists());
		}
    }

    @Test
    public void FontExists(){
        String[] files = { "default" };
		for (String f : files) {
			assertTrue("the file " + f + ".fnt does not exist",
					Gdx.files.internal("../core/assets/" + f + ".fnt").exists());
		}
    }

    @Test
    public void AtlasExists(){
        String[] files = { "uiskin" };
		for (String f : files) {
			assertTrue("the file " + f + ".atlas does not exist",
					Gdx.files.internal("../core/assets/" + f + ".atlas").exists());
		}
    }

    @Test
    public void JsonExists(){
        String[] files = { "uiskin" };
		for (String f : files) {
			assertTrue("the file " + f + ".json does not exist",
					Gdx.files.internal("../core/assets/" + f + ".json").exists());
		}
    }

    @Test
    public void PowerUpsExists(){
        String[] files = { "Experience",
        "Gold",
        "Health",
        "Invisible",
        "Speed" };
		for (String f : files) {
			assertTrue("the file " + f + ".png does not exist",
					Gdx.files.internal("../core/assets/powerups/" + f + ".png").exists());
		}
    }

}
