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
					Gdx.files.internal("../core/assets/weather" + f + ".png").exists());
		}
	}
    
}
