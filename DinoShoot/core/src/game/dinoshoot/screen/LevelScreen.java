package game.dinoshoot.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import game.dinoshoot.DinoShoot;
import game.dinoshoot.screen.HomeScreen.Button;

public class LevelScreen implements Screen, InputProcessor {

	public static class Button {
		
		private Sprite sprite;
		private String name;
		private Runnable runnable;
		
		public Button(Sprite sprite, String name) {
			this.sprite = sprite;
			this.name = name;
			
			this.runnable = new Runnable() {
				@Override
				public void run() {
					System.out.println(Button.this.name);
				}
			};
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Sprite getSprite() {
			return sprite;
		}
		
		public void setSprite(Sprite sprite) {
			this.sprite = sprite;
		}
		
		public void executeOnClick() {
			runnable.run();
		}
		
		public void setOnClickListener(Runnable runnable) {
			this.runnable = runnable;
		}
		
		public boolean isInside(Vector3 worldCoords) {
			return worldCoords.x >= sprite.getX() && worldCoords.x <= sprite.getX() + sprite.getWidth() &&
					worldCoords.y >= sprite.getY() && worldCoords.y <= sprite.getY() + sprite.getHeight();
		}
		
	}
	
	ArrayList<Button> buttons = new ArrayList<Button>();
	
	final DinoShoot game;
	OrthographicCamera camera;
	
	public LevelScreen(final DinoShoot game) {
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 600, 700);
		
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
				LevelScreen.this.game.setScreen(new GameScreen(LevelScreen.this.game));
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
				LevelScreen.this.game.setScreen(new GameScreen(LevelScreen.this.game));
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
				LevelScreen.this.game.setScreen(new GameScreen(LevelScreen.this.game));
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
				LevelScreen.this.game.setScreen(new HomeScreen(LevelScreen.this.game));
			}
		});
		buttons.add(backBtn); //add backBtn to buttons array
		
		Gdx.input.setInputProcessor(this);
		
	}
	
	@Override
	public void show() { }

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		for(Button btn: buttons) {
			btn.getSprite().draw(game.batch);
		}
		game.batch.end();
		
	}

	@Override
	public void resize(int width, int height) { }

	@Override
	public void pause() { }

	@Override
	public void resume() { }

	@Override
	public void hide() { }

	@Override
	public void dispose() { }

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
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

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
