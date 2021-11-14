package tech.dennismagnusson.gameoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import tech.dennismagnusson.gameoff.game.EffectManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class FishingScreen implements Screen {

    Sprite up;
    Sprite down;
    Sprite win;
    Sprite fail;
    List<Disposable> disposable;
    EffectManager effectManager;
    SpriteBatch batch;
    OrthographicCamera camera;

    Sound noise;
    Sound winSound;
    Sound loseSound;
    long noiseId;

    float WIDTH = 1600;
    float HEIGHT = 900;

    @Override
    public void show() {
        disposable = new LinkedList<>();
        effectManager = new EffectManager();
        effectManager.setNoise(0.4f);

        batch = new SpriteBatch();
        disposable.add(batch);

        camera = new OrthographicCamera(WIDTH, HEIGHT);

        Texture upTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
        disposable.add(upTexture);
        up = new Sprite(upTexture);
        up.setSize(WIDTH, HEIGHT);
        down = new Sprite(upTexture);
        down.setSize(WIDTH, HEIGHT);
        win = new Sprite(upTexture);
        win.setSize(WIDTH, HEIGHT);
        fail = new Sprite(upTexture);
        fail.setSize(WIDTH, HEIGHT);

        noise = Gdx.audio.newSound(Gdx.files.internal("sounds/noise.wav"));
        noiseId = noise.loop(0f);
        disposable.add(noise);

        winSound = Gdx.audio.newSound(Gdx.files.internal("sounds/noise.wav"));
        disposable.add(winSound);

        loseSound = Gdx.audio.newSound(Gdx.files.internal("sounds/noise.wav"));
        disposable.add(loseSound);
    }

    boolean fishDown = true;
    float timeToChange = -1f;

    float qTime = 0f;

    float splashTime = -1f;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        noise.setVolume(noiseId, qTime*0.5f);
        effectManager.setNoise(0.4f + qTime*10f);
        if(Gdx.input.isKeyPressed(Input.Keys.Q)) qTime += delta;
        else qTime = 0f;
        if(qTime > 2f) {
            System.out.println("EXIT PLZ");
        }

        if(splashTime > 0) {
            splashTime -= delta;
            // TODO Do something
            effectManager.update(delta);
            effectManager.begin();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            if(fishDown) {
                batch.draw(down, -0.5f * WIDTH, -0.5f * HEIGHT, WIDTH, HEIGHT);
                batch.draw(win, -0.5f * WIDTH, -0.5f * HEIGHT, WIDTH, HEIGHT);
            } else {
                batch.draw(up, -0.5f*WIDTH, -0.5f*HEIGHT, WIDTH, HEIGHT);
                batch.draw(fail, -0.5f * WIDTH, -0.5f * HEIGHT, WIDTH, HEIGHT);
            }
            batch.end();
            effectManager.end();
            return;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            splashTime = 3f;
            if(fishDown) winSound.play(1.0f);
            else loseSound.play(1.0f);
        }

        timeToChange -= delta;
        if(timeToChange < 0) {
            System.out.println("shift");
            fishDown = !fishDown;
            if(fishDown)
                timeToChange = 0.5f + new Random().nextFloat()*0.5f;
            else timeToChange = 3f + new Random().nextFloat()*15f;
        }

        effectManager.update(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        effectManager.begin();
        batch.setColor(fishDown ? Color.GREEN : Color.RED);
        if(fishDown)
            batch.draw(down, -0.5f*WIDTH, -0.5f*HEIGHT, WIDTH, HEIGHT);
        else batch.draw(up, -0.5f*WIDTH, -0.5f*HEIGHT, WIDTH, HEIGHT);
        batch.end();

        effectManager.end();
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
        for(Disposable d : disposable) d.dispose();
    }
}
