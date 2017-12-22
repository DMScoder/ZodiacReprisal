package com.zodiac.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zodiac.ZodiacReprisal;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());

		//For testing purposes
		boolean fullscreen = false;

		if(!fullscreen){
			config.fullscreen = false;
			config.width /=1.5f;
			config.height /=1.5f;
		}

		config.addIcon("eagleicon1.png", Files.FileType.Internal);
		config.addIcon("eagleicon2.png", Files.FileType.Internal);
		config.addIcon("eagleicon3.png", Files.FileType.Internal);
		config.resizable = false;
		config.title = "Zodiac";
		config.samples = 16;
		config.vSyncEnabled = true;

		new LwjglApplication(new ZodiacReprisal(), config);
	}
}
