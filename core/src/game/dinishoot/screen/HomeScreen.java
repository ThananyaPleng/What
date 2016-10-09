package game.dinishoot.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HomeScreen implements Screen {

	private SpriteBatch batch;
	private Sprite hscrn;
	@Override
	public void show() {
		batch = new SpriteBatch();
		
		Texture hscrnTexture = new Texture("img/images.jpg");
		hscrn = new Sprite(hscrnTexture);
		hscrn.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		hscrn.draw(batch);
		batch.end();		
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
