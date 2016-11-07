package game.dinoshoot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.dinoshoot.screen.HomeScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class DinoShoot extends Game {

    public static DinoShoot instance;

	private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
	
	@Override
	public void create () {
        DinoShoot.instance = this;

		batch = new SpriteBatch();
		font = new BitmapFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 700);

        this.setScreen(new HomeScreen());
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

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

}
