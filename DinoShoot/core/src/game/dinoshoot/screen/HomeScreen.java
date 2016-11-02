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

public class HomeScreen implements Screen, InputProcessor {

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
	
	public HomeScreen(final DinoShoot game) {
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 600, 700);
		
		//specify start button
		Texture start = new Texture(Gdx.files.internal("img/BtnStart.png"));
		Sprite startbtn = new Sprite(start);
		startbtn.setSize(150, 50);
		startbtn.setPosition(225, 400);
		
		Button startBtn = new Button(startbtn, "start_btn");
		startBtn.setOnClickListener(new Runnable() {
			
			@Override
			public void run() {
				HomeScreen.this.game.setScreen(new LevelScreen(HomeScreen.this.game));
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
				HomeScreen.this.game.setScreen(new LevelScreen(HomeScreen.this.game));
			}
		});
		buttons.add(quitBtn); //add qiutBtn to buttons array
		
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void show() { }

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		game.batch.setProjectionMatrix(camera.combined);
		camera.update();
		
		game.batch.begin();
		for(Button btn: buttons) {
			btn.getSprite().draw(game.batch);
		}
		game.batch.end();
		
	}

	@Override
	public void dispose() {
		dispose();
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
