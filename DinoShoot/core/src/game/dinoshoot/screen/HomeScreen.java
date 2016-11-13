package game.dinoshoot.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import game.dinoshoot.DinoShoot;
import game.dinoshoot.input.InputCommand;
import game.dinoshoot.ui.Button;

@SuppressWarnings("Duplicates")
public class HomeScreen extends ScreenAdapter {
	
	ArrayList<Button> buttons = new ArrayList<Button>();

	SpriteBatch batch;
	OrthographicCamera camera;

	// Asset Manager
	AssetManager assetManager = DinoShoot.instance.getAssetManager();
	
	Sprite background;
	Sprite Dinogreen;

	public HomeScreen() {
        batch = DinoShoot.instance.getBatch();
        camera = DinoShoot.instance.getCamera();
		background = new Sprite(assetManager.get("img/Background6.jpg", Texture.class));
		Dinogreen = new Sprite(assetManager.get("img/Dinogreen.png", Texture.class));
		Dinogreen.setPosition(295, 65);
        prepareButtons();
	}

	private void prepareButtons() {
        // Specify start button
		buttons.add(new Button(
				assetManager.get("img/BtnStart.png", Texture.class),
				new Vector2(150, 50),
				new Vector2(225, 400),
				new Runnable() {

					@Override
					public void run() {
						DinoShoot.instance.setScreen(new LevelScreen());
					}

				}
		));

        // Specify quit button
		buttons.add(new Button(
				assetManager.get("img/BtnQuit.png", Texture.class),
				new Vector2(150, 50),
				new Vector2(225, 300),
				new Runnable() {

					@Override
					public void run() {
						Gdx.app.exit();
					}

				}
		));
    }

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		batch.setProjectionMatrix(camera.combined);
		camera.update();

		batch.begin();
		background.draw(batch);
		Dinogreen.draw(batch);
		for(Button btn: buttons) {
			btn.draw(batch);
		}
		batch.end();

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 screenCoords = new Vector3(screenX, screenY, 0);
		Vector3 worldCoords = camera.unproject(screenCoords);
		Vector2 worldCoords2DSpace = new Vector2(worldCoords.x, worldCoords.y);

		if(button == InputCommand.BUTTON_ACTION) {
			for(Button btn: buttons) {
				if(btn.isInside(worldCoords2DSpace)) {
					btn.executeOnClick();
					return false;
				}
			}
		}

		return false;
	}

}
