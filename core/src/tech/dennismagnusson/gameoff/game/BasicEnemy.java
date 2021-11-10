package tech.dennismagnusson.gameoff.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import tech.dennismagnusson.gameoff.screens.GameScreen;

public class BasicEnemy implements Enemy {

    private float x;
    private float y;
    private float xvel;
    private float yvel;
    private int damage;
    Player player;

    private boolean exists = true;
    private GameScreen gameScreen;

    public BasicEnemy(float x, float y, float xvel, float yvel) {
        this.xvel = xvel;
        this.yvel = yvel;
        this.x = x;
        this.y = y;
        this.damage = 1;
    }

    @Override
    public void init(Player player, GameScreen gameScreen) {
        this.player = player;
        this.gameScreen = gameScreen;
        // TODO Create a sprite or something
    }

    @Override
    public void update(float delta) {
        if(!exists) return;
        x += xvel*delta;
        y += yvel*delta;

        if(player.getRect().contains(new Vector2(x, y))) {
            player.takeDamage(x, y, damage);
            exists = false;
        }

        if(x < 0 || y < 0 || x > GameScreen.WIDTH+gameScreen.camera.position.x || y > GameScreen.HEIGHT) exists = false;
    }

    @Override
    public void render(ShapeRenderer renderer) {
        if(exists)
            renderer.circle(x, y, 0.2f);
    }

    @Override
    public void takeDamage(int amount) {
        exists = false;
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    @Override
    public boolean isAlive() {
        return exists;
    }
}
