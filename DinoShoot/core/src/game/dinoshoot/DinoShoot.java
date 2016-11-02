package game.dinoshoot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.dinoshoot.screen.HomeScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class DinoShoot extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	
	public OrthographicCamera camera;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 600, 700);
		
		this.setScreen(new HomeScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		
		batch.dispose();
		font.dispose();
	}
	
}
