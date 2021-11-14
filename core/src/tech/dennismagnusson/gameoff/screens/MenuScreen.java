package tech.dennismagnusson.gameoff.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import tech.dennismagnusson.gameoff.game.EffectManager;

import java.util.LinkedList;
import java.util.List;

public class MenuScreen implements Screen {
    // Sprite button1, button2, button3, fishingbutton, background, cursor;
    Sprite background, cursor;
    List<Disposable> disposables;
    SpriteBatch batch;
    OrthographicCamera camera;
    EffectManager effectManager;
    BitmapFont font;
    SpriteBatch fontBatch;

    List<MenuButton> buttons;

    public void show() {
        effectManager = new EffectManager();
        effectManager.radialBlur.setDisabled(true);
        effectManager.chromaticAberration.setMaxDistortion(0.5f);
        disposables = new LinkedList<>();

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(3.5f);
        camera = new OrthographicCamera(1600, 900);

        batch = new SpriteBatch();
        fontBatch = new SpriteBatch();
        disposables.add(batch);
        disposables.add(fontBatch);

        Texture buttonTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
        disposables.remove(buttonTexture);

        float xpos = -700f;
        float width = 700f;
        float height = 150f;

        cursor = new Sprite(buttonTexture);
        buttons = new LinkedList<>();
        buttons.add(new MenuButton("LEVEL 1", font, "badlogic.jpg", 250f));
        buttons.add(new MenuButton("AAAAAAAA", font, "badlogic.jpg", 50f));
        buttons.add(new MenuButton("DEADMAN", font, "badlogic.jpg", -150f));
        buttons.add(new MenuButton("FISHING MINIGAME", font, "badlogic.jpg", -350f));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float xpos = -700f;
        float width = 700f;
        float height = 150f;

        effectManager.update(delta);
        effectManager.begin();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        Vector3 mousePos3 = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 mousePos = new Vector2(mousePos3.x, mousePos3.y);
        for(MenuButton b : buttons) b.render(delta, mousePos, batch);
        /*
        for(Sprite s : buttons) {
            if(s.getBoundingRectangle().contains(mousePos)) {
                s.setColor(Color.WHITE);
                s.setScale(1.2f);
            } else {
                s.setColor(Color.DARK_GRAY);
                s.setScale(1.0f);
            }
        }
        batch.setColor(Color.DARK_GRAY);
        button1.draw(batch);
        button2.draw(batch);
        button3.draw(batch);
        fishingbutton.draw(batch);
        batch.draw(button1, xpos, 250f, width, height);
        batch.draw(button2, xpos, 50f, width, height);
        batch.draw(button3, xpos, -150f, width, height);
        batch.draw(fishingbutton, xpos, -350f, width, height);
        batch.setColor(Color.WHITE);
        font.draw(batch, "LEVEL 1 (complete)", xpos, 250f+height*0.5f, width, Align.left, true);
        font.draw(batch, "LEVEL 2 (complete)", xpos, 50f+height*0.5f, width, Align.left, true);
        font.draw(batch, "LEVEL 3 (complete)", xpos, -150f+height*0.5f, width, Align.left, true);
        font.draw(batch, "FISHING MINIGAME", xpos, -350f+height*0.5f, width, Align.left, true);
         */

        batch.draw(cursor, mousePos.x-64, mousePos.y-64, 128, 128);
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

    }
    class MenuButton {

        Sprite sprite;
        Sprite portrait;
        BitmapFont font;
        float scale = 1f;
        Sound sound;

        float time = 0f;
        String text;

        public MenuButton(String text, BitmapFont font, String filename, float ypos) {
            float xpos = -700f;
            float width = 700f;
            float height = 150f;
            this.font = font;
            Texture t = new Texture(Gdx.files.internal(filename));
            sprite = new Sprite(t);
            sprite.setBounds(xpos, ypos, width, height);

            portrait = new Sprite(t);
            portrait.setBounds(200, -500, 600, 800);

            this.text = text;
            sound = Gdx.audio.newSound(Gdx.files.internal("sounds/noise.wav"));
        }

        float alpha = 0;
        float targetAlpha = 0f;
        boolean playing = false;

        public void render(float delta, Vector2 mousePos, SpriteBatch batch) {
            time += delta;
            if(sprite.getBoundingRectangle().contains(mousePos)) {
                scale += 0.3*delta;
                sprite.setColor(Color.WHITE);
                targetAlpha = (float) Math.sin(time)*0.3f + 0.7f;
                if(!playing)
                    sound.play(1.0f);
                playing = true;
            } else {
                scale -= 0.3*delta;
                sprite.setColor(Color.DARK_GRAY);
                targetAlpha = 0f;
                playing = false;
            }
            alpha += (targetAlpha - alpha)*delta*10;

            portrait.setAlpha(alpha);
            portrait.draw(batch);

            scale = Math.min(scale, 1.2f);
            scale = Math.max(scale, 1.0f);
            sprite.setScale(scale);

            sprite.draw(batch);
            font.draw(batch, text, sprite.getX(), sprite.getY()+105f, sprite.getWidth(), Align.left, true);
        }

    }
}
