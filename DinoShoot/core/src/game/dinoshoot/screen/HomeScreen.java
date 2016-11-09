package game.dinoshoot.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import game.dinoshoot.DinoShoot;
import game.dinoshoot.ui.Button;

public class HomeScreen extends ScreenAdapter {
	
	ArrayList<Button> buttons = new ArrayList<Button>();

	SpriteBatch batch;
	OrthographicCamera camera;
	
	Sprite background;

	public HomeScreen() {
        batch = DinoShoot.instance.getBatch();
        camera = DinoShoot.instance.getCamera();

	Texture backGroundImage = new Texture(Gdx.files.internal("img/background2.png"));
        background = new Sprite(backGroundImage);

        prepareButtons();
	}

	private void prepareButtons() {
        //specify start button
        Texture start = new Texture(Gdx.files.internal("img/BtnStart.png"));
        Sprite startbtn = new Sprite(start);
        startbtn.setSize(150, 50);
        startbtn.setPosition(225, 400);

        Button startBtn = new Button(startbtn, "start_btn");
        startBtn.setOnClickListener(new Runnable() {

            @Override
            public void run() {
                DinoShoot.instance.setScreen(new LevelScreen());
            }

        });
        buttons.add(startBtn); //add startBtn to buttons array

        //specify quit button
        Texture quit = new Texture(Gdx.files.internal("img/BtnQuit.png"));
        Sprite quitbtn = new Sprite(quit);
        quitbtn.setSize(150, 50);
        quitbtn.setPosition(225, 300);

        Button quitBtn = new Button(quitbtn, "quit_btn");
        quitBtn.setOnClickListener(new Runnable() {

            @Override
            public void run() {
                Gdx.app.exit();
            }

        });
        buttons.add(quitBtn); //add quitBtn to buttons array
    }

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		batch.setProjectionMatrix(camera.combined);
		camera.update();

		batch.begin();
		background.draw(batch);
		for(Button btn: buttons) {
			btn.getSprite().draw(batch);
		}
		batch.end();

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 screenCoords = new Vector3(screenX, screenY, 0);
		Vector3 worldCoords = camera.unproject(screenCoords);
		
		for(Button btn: buttons) {
			if(btn.isInside(worldCoords)) {
				btn.executeOnClick();
			}
		}
		
		return false;
	}

}
