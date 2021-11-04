package tech.dennismagnusson.gameoff.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import tech.dennismagnusson.gameoff.screens.GameScreen;

public class BasicEnemy implements Enemy {

    private float x;
    private float y;
    private float xvel;
    private float yvel;
    private int damage;

    private boolean exists = true;

    public BasicEnemy(float x, float y, float xvel, float yvel) {
        this.xvel = xvel;
        this.yvel = yvel;
        this.x = x;
        this.y = y;
        this.damage = 1;
    }

    @Override
    public void init() {
        // TODO Create a sprite or something
    }

    @Override
    public void update(float delta, Player player) {
        if(!exists) return;
        x += xvel*delta;
        y += yvel*delta;
        // TODO Check for intersect with player

        if(player.getRect().contains(new Vector2(x, y))) {
            player.takeDamage(x, y, damage);
            exists = false;
        }

        if(x < 0 || y < 0 || x > GameScreen.WIDTH || y > GameScreen.HEIGHT) exists = false;
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
