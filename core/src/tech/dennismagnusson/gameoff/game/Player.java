package tech.dennismagnusson.gameoff.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import tech.dennismagnusson.gameoff.screens.GameScreen;

public class Player {

    private float x = 0f, y = 1f;
    private float width = 1f, height = 2f;
    private float xvel;
    private float yvel;
    private boolean inAir = true;

    boolean movable = true;
    boolean evadeable = true;
    boolean attackable = true;

    private float groundLevel = 0.5f;

    private boolean facingRight = true;

    int health = 5;

    private EffectManager effectManager;
    private GameScreen gameScreen;

    public Player(EffectManager effectManager, GameScreen gameScreen) {
        this.effectManager = effectManager;
        this.gameScreen = gameScreen;
    }

    private void handleInput(float delta) {
        final float maxxvel = 7.5f;
        final float maxyvel = 15f;
        final float hspeed = 70f;
        final float jumpspeed = 30f;
        final float gravity = 25f;
        final float slowdown = 5f;
        final float evadeDistance = 2.5f;
        final float airspeed = 30f;

        boolean l = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean r = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean jump = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean light = Gdx.input.isKeyPressed(Input.Keys.J);
        boolean heavy = Gdx.input.isKeyPressed(Input.Keys.K);
        final boolean evade = Gdx.input.isKeyPressed(Input.Keys.L);
        if(l && !r) facingRight = false;
        else if(!l && r) facingRight = true;
        else facingRight = xvel > 0;
        if(evade && evadeable) {

            Rectangle rect = new Rectangle();
            rect.x = x + width/2f;
            rect.y = y;
            rect.width = facingRight ? evadeDistance : -evadeDistance;
            rect.height = this.height;
            if(rect.width < 0) {
                rect.x += rect.width;
                rect.width = -rect.width;
            }

            x += facingRight ? evadeDistance : -evadeDistance;
            movable = false;
            yvel = 0f;
            schedMovable(0.2f);
            schedEvade(0.6f);
            gameScreen.createDamageArea(rect, 0.6f);
            return;
        }

        if(light && attackable) {
            xvel = 0f;
            yvel = Math.max(yvel, 0);
            movable = false;
            attackable = false;
            schedMovable(0.2f);
            schedAttackable(0.4f);
            return;
        }

        if(heavy && attackable) {
            // TODO combos and shit
            xvel = 0f;
            yvel = Math.max(yvel, 0);
            movable = false;
            attackable = false;
            schedMovable(0.3f);
            schedAttackable(0.8f);
            return;
        }

        if(l && movable) {
            if(!inAir) xvel -= delta*hspeed;
            else xvel -= delta*airspeed;
            // TODO Animatiuon
        }
        if(r && movable) {
            if(!inAir)
                xvel += delta*hspeed;
            else xvel += delta*airspeed;
            // TODO Animation
        }
        if(jump && movable && !inAir) {
            yvel = jumpspeed;
            inAir = true;
            // TODO Animation
        }

        if(inAir && movable) yvel -= gravity*delta;
        if(!r && !l) xvel *= (1-slowdown*delta);

        // Clamp velocities;
        xvel = Math.min(xvel, maxxvel);
        xvel = Math.max(xvel, -maxxvel);
        yvel = Math.min(yvel, maxyvel);
        yvel = Math.max(yvel, -maxyvel);
    }

    public void update(float delta) {
        if(movable) {
            handleInput(delta);

            if (y <= groundLevel && yvel <= 0) {
                yvel = 0f;
                y = groundLevel;
                inAir = false;
            }

            x += xvel * delta;
            y += yvel * delta;
        }

    }

    public void render(ShapeRenderer renderer) { // TODO Update to spritebatch
        renderer.rect(x, y, width, height);
        renderer.line(-10, groundLevel, 50, groundLevel);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    private int attackCnt = 0;
    private void schedAttackable(float delay) {
        attackCnt += 1;
        attackable = false;
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                attackCnt -= 1;
                if(attackCnt == 0)
                    attackable = true;
            }
        }, delay);
    }

    private int evadeCnt = 0;
    private void schedEvade(float delay) {
        evadeCnt += 1;
        evadeable = false;
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                evadeCnt -= 1;
                if(evadeCnt == 0)
                    evadeable = true;
            }
        }, delay);
    }

    private int movableCnt = 0;
    private void schedMovable(float delay) {
        movableCnt += 1;
        movable = false;
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                movableCnt -= 1;
                if(movableCnt == 0)
                movable = true;
            }
        }, delay);
    }

    public void takeDamage(float x, float y, int damage) {
        // TODO Do things here
        System.out.println("DAMAGE");
        health -= 1;
        effectManager.setNoise((5-health)*0.1f);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
