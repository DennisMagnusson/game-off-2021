package tech.dennismagnusson.gameoff;

import com.badlogic.gdx.Game;
import tech.dennismagnusson.gameoff.screens.FishingScreen;
import tech.dennismagnusson.gameoff.screens.GameScreen;
import tech.dennismagnusson.gameoff.screens.MenuScreen;

public class MainGame extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen("levels/1.lvl"));
		// setScreen(new FishingScreen());
		// setScreen(new MenuScreen());
	}
}
