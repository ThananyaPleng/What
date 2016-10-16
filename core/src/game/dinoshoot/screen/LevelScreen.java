package game.dinoshoot.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import game.dinoshoot.DinoShoot;

public class LevelScreen implements Screen {

	final DinoShoot game;
	private Sprite easybtn;
	Texture easy;
	Texture medium;
	Texture hard;
	OrthographicCamera camera;
	
	public LevelScreen(final DinoShoot game) {
		this.game = game;
		
		easy = new Texture(Gdx.files.internal("img/button.png"));
		easybtn = new Sprite(easy);
		easybtn.setSize(50, 30);
		//medium = new Texture(Gdx.files.internal(""));
		//hard = new Texture(Gdx.files.internal(""));
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 600, 700);
	}
	
	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		game.batch.draw(easy, 400, 500);
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}

}
