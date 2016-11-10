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

public class GameScreen extends ScreenAdapter {

	ArrayList<Button> buttons = new ArrayList<Button>();

    SpriteBatch batch;
    OrthographicCamera camera;

    GameManager gameManager;

    // Overlay
    ShapeRenderer shapeRenderer;
    Button oResumeBtn, oQuitBtn; // overlay buttons

    Sprite background;

	private World world;
	private Box2DDebugRenderer renderer;
	private static final float PPM = 100;

	public GameScreen() {
        batch = DinoShoot.instance.getBatch();
        camera = DinoShoot.instance.getCamera();
        shapeRenderer = new ShapeRenderer();
		renderer = new Box2DDebugRenderer();

        gameManager = DinoShoot.instance.getGameManager();

        preparePhysics();
        prepareButtons();

        // BG
        Texture backGroundImage = new Texture(Gdx.files.internal("img/Background3.jpg"));
        background = new Sprite(backGroundImage);
        background.setSize(425, 700);

        DinoShoot.instance.getGameManager().setup();
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

    private void preparePhysics() {
        this.world = new World(new Vector2(0, -9.81f), true);
        // edit to Edge after
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(400 / PPM , 700 / PPM);
        Body bodyGround = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) 0.3 / PPM, 700 / PPM);

        fixtureDef.shape = shape;
        bodyGround.createFixture(fixtureDef);
        // edit to Edge after
    }

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float dt = Gdx.graphics.getDeltaTime();
        world.step(dt, 8, 3);

        gameManager.update(dt);

		camera.update();
        batch.setProjectionMatrix(camera.combined);

		renderer.render(world, camera.combined);
		
		batch.begin();
        background.draw(batch);

		for(Button btn: buttons) {
			btn.getSprite().draw(batch);
		}

        for(Egg egg : DinoShoot.instance.getGameManager().getAllEggs()) {
            egg.draw(batch);
        }
		batch.end();

        if(gameManager.isPause()) {
            /* RENDER OVERLAY RECT TO COVER THE SCREEN */
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, (float) 0.75);
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

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE) {
            gameManager.setPause(!gameManager.isPause());
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
                gameManager.fireEggAtPosition(new Vector2(worldCoords.x, worldCoords.y));
            } else if(button == Input.Buttons.RIGHT) {
//                gameManager.removeEggByWorldPosition(new Vector2(worldCoords.x, worldCoords.y));
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
