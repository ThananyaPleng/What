package game.dinoshoot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.dinoshoot.DinoShoot;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Dino Shoot";
		config.width = 600;
		config.height = 700;
		config.useGL30 = true;
		new LwjglApplication(new DinoShoot(), config);
	}
}
