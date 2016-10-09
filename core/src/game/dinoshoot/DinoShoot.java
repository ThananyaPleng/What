package game.dinoshoot;

import com.badlogic.gdx.Game;

import game.dinishoot.screen.HomeScreen;

public class DinoShoot extends Game {

	@Override
	public void create () {
		setScreen(new HomeScreen());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
	
	public void resize() {
		super.resize(600, 700);
	}
	
	public void pause() {
		super.pause();
	}
	
	public void resume() {
		super.resume();
	}
}
