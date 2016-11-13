package game.dinoshoot.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import game.dinoshoot.DinoShoot;
import game.dinoshoot.game.Egg;
import game.dinoshoot.game.GameManager;
import game.dinoshoot.input.InputCommand;
import game.dinoshoot.ui.Button;
import game.dinoshoot.utility.PositionHelper;

public class GameScreen extends ScreenAdapter {

    // Display elements
    SpriteBatch batch = DinoShoot.instance.getBatch();
    OrthographicCamera camera = DinoShoot.instance.getCamera();
    BitmapFont font = DinoShoot.instance.getFont();

    // Asset Manager
    AssetManager assetManager = DinoShoot.instance.getAssetManager();

    // Game Manager
    GameManager gameManager = DinoShoot.instance.getGameManager();

    // UIs
    Sprite background;
    Sprite deadline;
    Sprite shoot;
    Sprite scorewood;
    Sprite gameover;
    Button pauseBtn;

    // Overlay UIs
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    Button oResumeBtn, oRestartBtn, oMainMenuBtn; // overlay buttons

    // DEBUGs
    boolean DEBUG_Draw_Grid = true;

	public GameScreen(GameManager.Level level) {

		//gameover
		gameover = new Sprite(assetManager.get("img/gameover.png", Texture.class));
		gameover.setSize(600, 500);
		gameover.setPosition(0, 700);
		
        prepareButtons();
        prepareSprite();

        // Setup game
        gameManager.restart(level);

	}

	private void prepareButtons() {
        pauseBtn = new Button(
                assetManager.get("img/BtnPause.png", Texture.class),
                new Vector2(150, 50),
                new Vector2(437.5f, 500),
                new Runnable() {
                    @Override
                    public void run() {
                        //open overlay (change game state to pause)
                        gameManager.setState(GameManager.State.PAUSE);
                    }
                }
        );

        oResumeBtn = new Button(
                assetManager.get("img/BtnResume.png", Texture.class),
                new Vector2(150, 50),
                new Vector2(225f, 400),
                new Runnable() {
                    @Override
                    public void run() {
                        //close overlay (change game state to playing)
                        gameManager.setState(GameManager.State.PLAYING);
                    }
                }
        );

        oRestartBtn = new Button(
                assetManager.get("img/BtnRestart.png", Texture.class),
                new Vector2(150, 50),
                new Vector2(225f, 350),
                new Runnable() {
                    @Override
                    public void run() {
                        //close overlay (change game state to playing)
                        gameManager.restart(gameManager.getLevel());
                    }
                }
        );

        oMainMenuBtn = new Button(
                assetManager.get("img/BtnMainMenu.png", Texture.class),
                new Vector2(150, 50),
                new Vector2(225f, 300),
                new Runnable() {
                    @Override
                    public void run() {
                        //go to home screen
                        DinoShoot.instance.setScreen(new HomeScreen());
                    }
                }
        );
    }

    private void prepareSprite() {
        // BG
        background = new Sprite(assetManager.get("img/Background3.jpg", Texture.class));
        background.setSize(425, 700);

        // Deadline
        deadline = new Sprite(assetManager.get("img/deadline.png", Texture.class));
        deadline.setPosition(0, 190);
        deadline.setSize(425, 10);
        
        //shoot
        shoot = new Sprite(assetManager.get("img/shoot.png", Texture.class));
        shoot.setSize(150, 50);
		shoot.setPosition(140, 0);
		
    }

	@Override
	public void render(float delta) {
        // Clear screen buffer
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Get delta time
        float dt = Gdx.graphics.getDeltaTime();

        // Update screen
        update(dt);

        // Update camera & batch
		camera.update();
              batch.setProjectionMatrix(camera.combined);

        // Begin draw
        batch.begin();
            background.draw(batch);
            deadline.draw(batch);
            shoot.draw(batch);
            pauseBtn.draw(batch);

            for(Egg egg : DinoShoot.instance.getGameManager().getAllEggs()) {
                egg.draw(batch);
            }

            font.draw(batch, String.valueOf(gameManager.getScore()), 430, 200);
		batch.end();

        debug_DrawGrid();

        // If game pause, draw overlay
        if(gameManager.getState() == GameManager.State.PAUSE) {
            drawPause();
        } else if(gameManager.getState() == GameManager.State.GAMEOVER) {
            drawGameOver();
        }
	}

	private void drawPause() {
        /* RENDER OVERLAY RECT TO COVER THE SCREEN */
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.75f);
        shapeRenderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
            /* END RENDER */

            /* RENDER OVERLAY BUTTON */
        batch.begin();
        oResumeBtn.draw(batch);
        oRestartBtn.draw(batch);
        oMainMenuBtn.draw(batch);
        batch.end();
            /* END RENDER OVERLAY BUTTON */
    }

    private void drawGameOver() {
         /* RENDER OVERLAY RECT TO COVER THE SCREEN */
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.75f);
        shapeRenderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        /* END RENDER */

        /* RENDER OVERLAY BUTTON */
        batch.begin();
        font.draw(batch, String.valueOf(gameManager.getScore()), 225, 500);
        oRestartBtn.draw(batch);
        oMainMenuBtn.draw(batch);
        gameover.draw(batch);
        batch.end();
        /* END RENDER OVERLAY BUTTON */
    }

    private void debug_DrawGrid() {
        if(DEBUG_Draw_Grid) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(255, 0, 0, 1f);

            for(int row = 0; row <= 10; row++) {
                for(int col = 0; col <= 7; col++) {
                    float y = (row * 50) + 23;
                    float x = (col * 50) + 23 + (PositionHelper.calcShiftPosition(row, gameManager.isShiftRowZero()) ? 0 : 25);
                    shapeRenderer.rect(x, y + (50 * gameManager.getHeightOffset()), 4, 4);
                }
            }

            shapeRenderer.end();
        }
    }

	private void update(float dt) {
        gameManager.update(dt);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(gameManager.getState() == GameManager.State.PLAYING) {
            if(keycode == InputCommand.OPEN_MENU_GAME) {
                gameManager.setState(GameManager.State.PAUSE);
            }
        } else if(gameManager.getState() == GameManager.State.PAUSE) {
            if(keycode == InputCommand.OPEN_MENU_GAME) {
                gameManager.setState(GameManager.State.PLAYING);
            }
        }

        if(keycode == InputCommand.DEBUG_DRAW_GRID) {
            DEBUG_Draw_Grid = !DEBUG_Draw_Grid;
        }

        return false;
    }

    @Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 screenCoords = new Vector3(screenX, screenY, 0);
		Vector3 worldCoords = camera.unproject(screenCoords);
        Vector2 worldCoords2DSpace = new Vector2(worldCoords.x, worldCoords.y);

        if(gameManager.getState() == GameManager.State.PLAYING) {
            if (button == InputCommand.BUTTON_ACTION) {
                if (pauseBtn.isInside(worldCoords2DSpace)) {
                    pauseBtn.executeOnClick();
                    return false;
                }
            }

            if (button == InputCommand.SHOOT_EGG) {
                gameManager.fireEggToPoint(worldCoords2DSpace);
            }

            if(button == InputCommand.CREATE_EGG) {
                gameManager.createEggByWorldPosition(worldCoords2DSpace);
            }

            if(button == InputCommand.REMOVE_EGG) {
                gameManager.removeEggByWorldPosition(worldCoords2DSpace);
            }
        } else if(gameManager.getState() == GameManager.State.PAUSE) {
            if(button == InputCommand.BUTTON_ACTION) {
                if(oResumeBtn.isInside(worldCoords2DSpace)) {
                    oResumeBtn.executeOnClick();
                } else if(oMainMenuBtn.isInside(worldCoords2DSpace)) {
                    oMainMenuBtn.executeOnClick();
                } else if(oRestartBtn.isInside(worldCoords2DSpace)) {
                    oRestartBtn.executeOnClick();
                }
            }
        } else if(gameManager.getState() == GameManager.State.GAMEOVER) {
            if(button == InputCommand.BUTTON_ACTION) {
                if(oMainMenuBtn.isInside(worldCoords2DSpace)) {
                    oMainMenuBtn.executeOnClick();
                } else if(oRestartBtn.isInside(worldCoords2DSpace)) {
                    oRestartBtn.executeOnClick();
                }
            }
        }

		return false;
	}

}
