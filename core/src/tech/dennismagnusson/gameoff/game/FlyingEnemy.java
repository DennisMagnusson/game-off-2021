package tech.dennismagnusson.gameoff.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import tech.dennismagnusson.gameoff.screens.GameScreen;

public class FlyingEnemy implements Enemy {

    boolean alive = true;
    float x, y;
    GameScreen gameScreen;
    Player player;
    public FlyingEnemy(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void init(Player player, GameScreen gameScreen) {
        this.player = player;
        this.gameScreen = gameScreen;

        final Player finalPlayer = player;
        final GameScreen finalGameScreen = gameScreen;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if(finalPlayer.getX() - x < 10 && alive)
                    finalGameScreen.addEnemy(new BasicEnemy(x, y, -5, 0));
            }
        }, 3, 0.8f);
    }

    @Override
    public void update(float delta) {
        if(!alive) return;
        float diff = player.getY() + player.getHeight()/2 - y;
        y += diff*delta*0.5f;
    }

    @Override
    public void render(ShapeRenderer renderer) {
        if(!alive) return;
        renderer.circle(x, y, 1);
    }

    @Override
    public void takeDamage(int amount) {
        alive = false;
        // TODO Explode or some shit
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    @Override
    public boolean isAlive() {
        return alive;
    }
}
