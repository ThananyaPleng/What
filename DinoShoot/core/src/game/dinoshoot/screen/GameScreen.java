package game.dinoshoot.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import game.dinoshoot.DinoShoot;
import game.dinoshoot.game.Egg;
import game.dinoshoot.game.GameManager;
import game.dinoshoot.ui.Button;
import game.dinoshoot.utility.PositionHelper;

public class GameScreen extends ScreenAdapter {

    // Display elements
    SpriteBatch batch;
    OrthographicCamera camera;

    // Game Manager
    GameManager gameManager;

    // UIs
    Sprite background;
    ArrayList<Button> buttons = new ArrayList<Button>();

    // Overlay UIs
    ShapeRenderer shapeRenderer;
    Button oResumeBtn, oQuitBtn; // overlay buttons

    // DEBUGs
    boolean DEBUG_Draw_Grid = true;

	public GameScreen() {
        batch = DinoShoot.instance.getBatch();
        camera = DinoShoot.instance.getCamera();
        shapeRenderer = new ShapeRenderer();

        gameManager = DinoShoot.instance.getGameManager();

        prepareButtons();

        // BG
        Texture backGroundImage = new Texture(Gdx.files.internal("img/Background3.jpg"));
        background = new Sprite(backGroundImage);
        background.setSize(425, 700);

        // Setup game
        gameManager.setup();
	}

	private void prepareButtons() {
        Texture pause = new Texture(Gdx.files.internal("img/BtnPause.png"));
        Sprite pausebtn = new Sprite(pause);
        pausebtn.setSize(150, 50);
        pausebtn.setPosition(437.5f, 500);

        Button pauseBtn = new Button(pausebtn, "pause_btn");
        pauseBtn.setOnClickListener(new Runnable() {

            @Override
            public void run() {
                //open overlay (set pause state to true, set overlay state to true)
                GameScreen.this.gameManager.setPause(true);
            }
        });
        buttons.add(pauseBtn);

        Texture resume = new Texture(Gdx.files.internal("img/BtnResume.png"));
		Sprite oResumeSprite = new Sprite(resume);
        oResumeSprite.setSize(150, 50);
        oResumeSprite.setPosition(225, 350);

        oResumeBtn = new Button(oResumeSprite, "overlay_resume_btn");
        oResumeBtn.setOnClickListener(new Runnable() {

            @Override
            public void run() {
                //close overlay (set pause state to false, set overlay state to false)
                GameScreen.this.gameManager.setPause(false);
            }
        });

		Texture quit = new Texture(Gdx.files.internal("img/BtnQuit.png"));
		Sprite oQuitSprite = new Sprite(quit);
        oQuitSprite.setSize(150, 50);
        oQuitSprite.setPosition(225, 300);

        oQuitBtn = new Button(oQuitSprite, "overlay_quit_btn");
        oQuitBtn.setOnClickListener(new Runnable() {

            @Override
            public void run() {
                //exit game
                Gdx.app.exit();
            }
        });
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

            for(Button btn: buttons) {
                btn.getSprite().draw(batch);
            }

            for(Egg egg : DinoShoot.instance.getGameManager().getAllEggs()) {
                egg.draw(batch);
            }
		batch.end();

        drawDebug();

        // If game pause, draw overlay
        if(gameManager.isPause()) {
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
            oResumeBtn.getSprite().draw(batch);
            oQuitBtn.getSprite().draw(batch);
            batch.end();
            /* END RENDER OVERLAY BUTTON */
        }
	}

	private void update(float dt) {
        gameManager.update(dt);
    }

    private void drawDebug() {
        if(DEBUG_Draw_Grid) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(255, 0, 0, 1f);

            for(int row = 0; row <= 10; row++) {
                for(int col = 0; col <= 7; col++) {
                    float y = (row * 50) + 23;
                    float x = (col * 50) + 23 + (PositionHelper.calcShiftPosition(row, gameManager.isShiftBaseRow()) ? 0 : 25);
                    shapeRenderer.rect(x, y + (50 * gameManager.getHeightOffset()), 4, 4);
                }
            }

            shapeRenderer.end();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE) {
            gameManager.setPause(!gameManager.isPause());
        } else if(keycode == Input.Keys.F1) {
            DEBUG_Draw_Grid = !DEBUG_Draw_Grid;
        }

        return false;
    }

    @Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 screenCoords = new Vector3(screenX, screenY, 0);
		Vector3 worldCoords = camera.unproject(screenCoords);

        if(!gameManager.isPause()) {
            for(Button btn: buttons) {
                if(btn.isInside(worldCoords)) {
                    btn.executeOnClick();
                }
            }

            if(button == Input.Buttons.LEFT) {
                gameManager.fireEggToPoint(new Vector2(worldCoords.x, worldCoords.y));
            } else if(button == Input.Buttons.MIDDLE) {
                gameManager.createEggByWorldPosition(new Vector2(worldCoords.x, worldCoords.y));
            } else if(button == Input.Buttons.RIGHT) {
                gameManager.removeEggByWorldPosition(new Vector2(worldCoords.x, worldCoords.y));
            }
        } else {
            if(oResumeBtn.isInside(worldCoords)) {
                oResumeBtn.executeOnClick();
            } else if(oQuitBtn.isInside(worldCoords)) {
                oQuitBtn.executeOnClick();
            }
        }

		return false;
	}

}
