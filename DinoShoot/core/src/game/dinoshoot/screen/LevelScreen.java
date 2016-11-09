package game.dinoshoot.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import game.dinoshoot.DinoShoot;
import game.dinoshoot.ui.Button;

import java.util.ArrayList;

public class LevelScreen extends ScreenAdapter {
	
	ArrayList<Button> buttons = new ArrayList<Button>();

	SpriteBatch batch;
	OrthographicCamera camera;
	
	Sprite background;

	public LevelScreen() {
        batch = DinoShoot.instance.getBatch();
        camera = DinoShoot.instance.getCamera();

        Texture backGroundImage = new Texture(Gdx.files.internal("img/background2.png"));
        background = new Sprite(backGroundImage);

		prepareButtons();
	}

    private void prepareButtons() {
        //specify easy button
        Texture easy = new Texture(Gdx.files.internal("img/BtnEasy.png"));
        Sprite easybtn = new Sprite(easy);
        easybtn.setSize(150, 50);
        easybtn.setPosition(225, 400);

        Button easyBtn = new Button(easybtn, "easy_btn");
        easyBtn.setOnClickListener(new Runnable(){

            @Override
            public void run() {
                // must edit use easy mode game
                DinoShoot.instance.setScreen(new GameScreen());
            }

        });
        buttons.add(easyBtn); //add easyBtn to buttons array

        //specify medium button
        Texture medium = new Texture(Gdx.files.internal("img/BtnMedium.png"));
        Sprite mediumbtn = new Sprite(medium);
        mediumbtn.setSize(150, 50);
        mediumbtn.setPosition(225, 350);

        Button mediumBtn = new Button(mediumbtn, "medium_btn");
        mediumBtn.setOnClickListener(new Runnable() {

            @Override
            public void run() {
                //must edit use medium mode game
                DinoShoot.instance.setScreen(new GameScreen());
            }

        });
        buttons.add(mediumBtn); //add mediumBtn to buttons array

        //specify hard button
        Texture hard = new Texture(Gdx.files.internal("img/BtnHard.png"));
        Sprite hardbtn = new Sprite(hard);
        hardbtn.setSize(150, 50);
        hardbtn.setPosition(225, 300);

        Button hardBtn = new Button(hardbtn, "hard_btn");
        hardBtn.setOnClickListener(new Runnable() {

            @Override
            public void run() {
                //must edit use hard mode game
                DinoShoot.instance.setScreen(new GameScreen());
            }

        });
        buttons.add(hardBtn); //add hardBtn to buttons array

        //specify back button
        Texture back = new Texture(Gdx.files.internal("img/BtnBack.png"));
        Sprite backbtn = new Sprite(back);
        backbtn.setSize(150, 50);
        backbtn.setPosition(50, 50);

        Button backBtn = new Button(backbtn, "back_btn");
        backBtn.setOnClickListener(new Runnable() {

            @Override
            public void run() {
                DinoShoot.instance.setScreen(new HomeScreen());
            }

        });
        buttons.add(backBtn); //add backBtn to buttons array
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
