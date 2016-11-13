package game.dinoshoot.screen;

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
import game.dinoshoot.game.GameManager;
import game.dinoshoot.input.InputCommand;
import game.dinoshoot.ui.Button;

import java.util.ArrayList;

@SuppressWarnings("Duplicates")
public class LevelScreen extends ScreenAdapter {
	
	ArrayList<Button> buttons = new ArrayList<Button>();

	SpriteBatch batch;
	OrthographicCamera camera;

    // Asset Manager
    AssetManager assetManager = DinoShoot.instance.getAssetManager();
	
	Sprite background;

	public LevelScreen() {
        batch = DinoShoot.instance.getBatch();
        camera = DinoShoot.instance.getCamera();
        background = new Sprite(assetManager.get("img/background2.png", Texture.class));

		prepareButtons();
	}

    private void prepareButtons() {
        // Specify easy button
        buttons.add(new Button(
                assetManager.get("img/BtnEasy.png", Texture.class),
                new Vector2(150, 50),
                new Vector2(225, 400),
                new Runnable(){
                    @Override
                    public void run() {
                        // must edit use easy mode game
                        DinoShoot.instance.setScreen(new GameScreen(GameManager.Level.EASY));
                    }

                }
        ));

        // Specify medium button
        buttons.add(new Button(
                assetManager.get("img/BtnMedium.png", Texture.class),
                new Vector2(150, 50),
                new Vector2(225, 350),
                new Runnable() {

                    @Override
                    public void run() {
                        //must edit use medium mode game
                        DinoShoot.instance.setScreen(new GameScreen(GameManager.Level.MEDIUM));
                    }

                }
        ));

        // Specify hard button
        buttons.add(new Button(
                assetManager.get("img/BtnHard.png", Texture.class),
                new Vector2(150, 50),
                new Vector2(225, 300),
                new Runnable() {

                    @Override
                    public void run() {
                        //must edit use hard mode game
                        DinoShoot.instance.setScreen(new GameScreen(GameManager.Level.HARD));
                    }

                }
        ));

        //specify back button
        buttons.add(new Button(
                assetManager.get("img/BtnBack.png", Texture.class),
                new Vector2(150, 50),
                new Vector2(50, 50),
                new Runnable() {

                    @Override
                    public void run() {
                        DinoShoot.instance.setScreen(new HomeScreen());
                    }

                }
        ));
    }

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
            background.draw(batch);

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
