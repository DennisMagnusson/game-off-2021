package tech.dennismagnusson.gameoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;
import javafx.scene.effect.Bloom;
import tech.dennismagnusson.gameoff.game.*;

import java.util.*;

public class GameScreen implements Screen {

    public Player player;
    List<Enemy> enemies;
    ShapeRenderer shapeRenderer;
    public OrthographicCamera camera;
    public List<Disposable> disposables;

    EffectManager effectManager;

    List<Rectangle> damageAreas;

    List<Rectangle> platforms;

    SpriteBatch batch;
    TextBoxManager textBoxManager;

    public static final float WIDTH = 16;
    public static final float HEIGHT = 9;

    private String levelfilename;

    public GameScreen(String filename) {
        this.levelfilename = filename;
    }

    public void readFile(String filename) {
        String[] file = Gdx.files.internal(filename).readString().split("\\r?\\n");
        LinkedList<String> lines = new LinkedList<>();
        for(String s : file) lines.add(s);
        textBoxManager = new TextBoxManager(this);

        int levelNumber = Integer.parseInt(lines.remove(0));
        String startImageFilename = lines.remove(0); // Splash image
        String musicfilename = lines.remove(0);
        String bossmusicfilename  = lines.remove(0);

        platforms = new ArrayList<>();
        lines.remove(0); // Platforms
        while(true) {
            String line = lines.remove(0);
            if(line.equalsIgnoreCase("enemies")) break;
            String[] things = line.split("\\ ");
            float x = Float.parseFloat(things[0]);
            float y = Float.parseFloat(things[1]);
            float w = Float.parseFloat(things[2]);
            float h = Float.parseFloat(things[3]);
            platforms.add(new Rectangle(x, y, w, h));
        }

        // ENEMIES
        while(true) {
            String line = lines.remove(0);
            if(line.equalsIgnoreCase("text")) break;
            String[] things = line.split("\\ ");
            if(things[0].equalsIgnoreCase("flying")) {
                // TODO Spawn a thing
            }
        }

        // LINES
        while(true) {
            String line = lines.remove(0);
            if(line.equalsIgnoreCase("boss")) break;
            String text = line;
            String[] things = lines.remove(0).split("\\ ");
            textBoxManager.addTextBox(text, things[0], things[1], Float.parseFloat(things[2]));
        }

        // TODO BOSS HERE
    }

    @Override
    public void show() {
        disposables = new ArrayList<>();
        readFile(levelfilename);

        effectManager = new EffectManager();
        disposables.add(effectManager.manager);
        damageAreas = new LinkedList<>();

        batch = new SpriteBatch();
        disposables.add(batch);

        final Random random = new Random();
        enemies = new ArrayList<>();
        player = new Player(effectManager, this);
        final GameScreen gs = this;
        new Timer().schedule(new Timer.Task() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++) {
                    float x = random.nextFloat()*WIDTH;
                    float y = random.nextFloat()*HEIGHT;
                    addEnemy(new BasicEnemy(x, y, (x-(WIDTH/2f))*0.3f, (y-(HEIGHT/2f))*0.3f));
                }
            }
        }, 5, 3);

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.position.set(WIDTH/2, HEIGHT/2, 0f);
        disposables.add(shapeRenderer);

        addEnemy(new FlyingEnemy(20, 2));
        addEnemy(new FlyingEnemy(30, 2));
        addEnemy(new FlyingEnemy(45, 4));
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
            e.update(delta);

        }
        if(player.getX() > camera.position.x) {
            camera.position.x = player.getX();
        }

        textBoxManager.update(delta);

        // XXX RENDER

        effectManager.begin();


        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.setColor(Color.PURPLE);
        for(Enemy e : enemies) {
            e.render(shapeRenderer);
        }
        shapeRenderer.setColor(Color.FIREBRICK);
        for(Rectangle r : damageAreas) {
            shapeRenderer.rect(r.x, r.y, r.width, r.height);
        }
        float groundLevel = 0.5f;
        shapeRenderer.line(-10, groundLevel, 50, groundLevel);// Ground
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        batch.end();

        effectManager.end();

        textBoxManager.render();
    }

    public void createDamageArea(final Rectangle rect, float duration) {
        if(rect.width < 0) {
            rect.width = -rect.width;
            rect.x -= rect.width;
        }
        damageAreas.add(rect);
        new Timer().scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                damageAreas.remove(rect);
            }
        }, duration);
    }

    public void addEnemy(Enemy e) {
        this.enemies.add(e);
        e.init(player, this);
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
