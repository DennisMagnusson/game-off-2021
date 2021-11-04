package tech.dennismagnusson.gameoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;
import javafx.scene.effect.Bloom;
import tech.dennismagnusson.gameoff.game.BasicEnemy;
import tech.dennismagnusson.gameoff.game.EffectManager;
import tech.dennismagnusson.gameoff.game.Enemy;
import tech.dennismagnusson.gameoff.game.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen {

    Player player;
    List<Enemy> enemies;
    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;
    public List<Disposable> disposables;

    EffectManager effectManager;

    List<Rectangle> damageAreas;

    public static final float WIDTH = 16;
    public static final float HEIGHT = 9;

    @Override
    public void show() {
        disposables = new ArrayList<>();

        effectManager = new EffectManager();
        disposables.add(effectManager.manager);
        damageAreas = new LinkedList<>();

        final Random random = new Random();
        enemies = new ArrayList<>();
        player = new Player(effectManager, this);
        new Timer().schedule(new Timer.Task() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++) {
                    float x = random.nextFloat()*WIDTH;
                    float y = random.nextFloat()*HEIGHT;
                    enemies.add(new BasicEnemy(x, y, (x-(WIDTH/2f))*0.3f, (y-(HEIGHT/2f))*0.3f));
                }
            }
        }, 5, 3);

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.position.set(WIDTH/2, HEIGHT/2, 0f);
        disposables.add(shapeRenderer);
    }

    @Override
    public void render(float delta) {
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        effectManager.update(delta);

        player.update(delta);
        for(Enemy e : enemies) {
            if(!e.isAlive()) continue;
            Vector2 pos = e.getPosition();
            for(Rectangle r : damageAreas) {
                if(r.contains(pos)) e.takeDamage(1);
            }
            e.update(delta, player);

        }



        effectManager.begin();
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        player.render(shapeRenderer);
        shapeRenderer.setColor(Color.PURPLE);
        for(Enemy e : enemies) {
            e.render(shapeRenderer);
        }
        shapeRenderer.setColor(Color.FIREBRICK);
        for(Rectangle r : damageAreas) {
            shapeRenderer.rect(r.x, r.y, r.width, r.height);
        }
        shapeRenderer.end();

        effectManager.end();
    }

    public void createDamageArea(final Rectangle rect, float duration) {
        damageAreas.add(rect);
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                damageAreas.remove(rect);
            }
        }, duration);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
