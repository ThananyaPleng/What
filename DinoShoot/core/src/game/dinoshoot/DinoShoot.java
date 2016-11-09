package game.dinoshoot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.dinoshoot.game.GameManager;
import game.dinoshoot.screen.HomeScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class DinoShoot extends Game {

    public static DinoShoot instance;

    private AssetManager assetManager;
	private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    private GameManager gameManager;
	
	@Override
	public void create () {
        DinoShoot.instance = this;

        assetManager = new AssetManager();
		batch = new SpriteBatch();
		font = new BitmapFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 700);

        assetManager.load("img/red.png", Texture.class);
        assetManager.load("img/orange.png", Texture.class);
        assetManager.load("img/yellow.png", Texture.class);
        assetManager.load("img/green.png", Texture.class);
        assetManager.load("img/purple.png", Texture.class);
        assetManager.finishLoading();

        gameManager = new GameManager();

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

        assetManager.dispose();
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

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
