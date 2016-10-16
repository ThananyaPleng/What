package game.dinoshoot.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.dinoshoot.DinoShoot;

public class HomeScreen implements Screen {

	private Sprite startbtn;
	
	final DinoShoot game;
	OrthographicCamera camera;
	
	Texture start;
	//Texture quit;
	
	public HomeScreen(final DinoShoot game) {
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 600, 700);
		
		start = new Texture(Gdx.files.internal("img/button.png"));
		startbtn = new Sprite(start);
		startbtn.setSize(50, 30);
		//quit = new Texture(Gdx.files.internal(""));
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
		game.font.draw(game.batch, "Dino Shoot", 200, 200);
		game.batch.end();
	}

	@Override
	public void dispose() {
		dispose();
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

}
