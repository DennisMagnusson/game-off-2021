package tech.dennismagnusson.gameoff;

import com.badlogic.gdx.Game;
import tech.dennismagnusson.gameoff.screens.GameScreen;

public class MainGame extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
}
