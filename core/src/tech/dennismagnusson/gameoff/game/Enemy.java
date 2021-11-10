package tech.dennismagnusson.gameoff.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import tech.dennismagnusson.gameoff.screens.GameScreen;

public interface Enemy {

    float x = 0;
    float y = 0;

    void init(Player player, GameScreen gameScreen);

    void update(float delta);

    void render(ShapeRenderer renderer);

    void takeDamage(int amount);

    Vector2 getPosition();

    boolean isAlive();
}
