package game.dinoshoot.screen;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import game.dinoshoot.DinoShoot;

public class GameScreen extends JFrame implements Screen, InputProcessor {

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
	Texture backGroundImage = new Texture(Gdx.files.internal("img/Background3.jpg"));
	Sprite bg = new Sprite(backGroundImage);
	final DinoShoot game;
	OrthographicCamera camera;

	private World world;
	private Box2DDebugRenderer renderer;
	private static final float PPM = 100;
	
	JFrame newframe = new JFrame();

	public GameScreen(final DinoShoot game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 600, 700);
		//camera.setToOrtho(false, 600 / PPM, 700 / PPM);
		renderer = new Box2DDebugRenderer();
		
		Texture pause = new Texture(Gdx.files.internal("img/BtnPause.png"));
		Sprite pausebtn = new Sprite(pause);
		pausebtn.setSize(150, 50);
		pausebtn.setPosition(425, 500);
		//pausebtn.setPosition(425 / PPM, 500 / PPM);------------------------
		
		Button pauseBtn = new Button(pausebtn, "pause_btn");
		pauseBtn.setOnClickListener(new Runnable() {

			@Override
			public void run() {
				//edit to popup screen
				System.out.println("sys");
				GameScreen.this.game.setScreen(new HomeScreen(GameScreen.this.game));
//				newframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				newframe.setBounds(100, 600, 500, 500);
//				newframe.setTitle("title");
//				newframe.setVisible(true);
			}
		});
		buttons.add(pauseBtn);
		
/*		Texture resume = new Texture(Gdx.files.internal("img/BtnResume.png"));
		Sprite resumebtn = new Sprite(resume);
		resumebtn.setSize(150, 50);
		resumebtn.setPosition(225, 350);
		
		Texture quit = new Texture(Gdx.files.internal("img/BtnQuit.png"));
		Sprite quitbtn = new Sprite(quit);
		quitbtn.setSize(150, 50);
		quitbtn.setPosition(225, 300);*/
		
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
		
		Gdx.input.setInputProcessor((InputProcessor) this);
	}
	
	@Override
	public void show() { }

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		//game.batch.setProjectionMatrix(camera3.combined);
		
		float dt = Gdx.graphics.getDeltaTime();
		
		world.step(dt, 8, 3);
		renderer.render(world, camera.combined);
		
		game.batch.begin();
		game.batch.draw(backGroundImage, 0, 0);
		for(Button btn: buttons) {
			btn.getSprite().draw(game.batch);
		}
		game.batch.end();
		
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
	public void dispose() { }

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
