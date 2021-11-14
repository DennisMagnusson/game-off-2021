package tech.dennismagnusson.gameoff.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import tech.dennismagnusson.gameoff.MainGame;
import tech.dennismagnusson.gameoff.screens.GameScreen;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TextBoxManager {

    Player player;
    List<TextBox> textBoxes;
    TextBox currTextbox;
    Queue<TextBox> queue;
    GameScreen game;
    BitmapFont font;
    SpriteBatch batch;
    OrthographicCamera camera;

    Sprite backgroundImage;

    public TextBoxManager(GameScreen game) {
        textBoxes = new LinkedList<>();
        queue = new LinkedList<>();
        this.game = game;
        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(2.5f);
        camera = new OrthographicCamera(1600, 900);
        camera.position.set(-800f, -450f, 0f);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        Texture t = new Texture(Gdx.files.internal("badlogic.jpg"));
        game.disposables.add(t);
        backgroundImage = new Sprite(t);
        // TODO Configure size correctly
        backgroundImage.setSize(1600, 250);
    }

    public void addTextBox(String text, String soundFilename, String sprite, float playerX) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/"+soundFilename));
        game.disposables.add(sound);
        Texture t = new Texture(Gdx.files.internal(sprite));
        Sprite s = new Sprite(t);
        // TODO Configure position and shit
        game.disposables.add(t);

        textBoxes.add(new TextBox(text, s, sound, playerX));
    }

    float textBoxTime = 0;
    public void update(float delta) {
        if(player == null) player = game.player;
        textBoxTime += delta;
        if(textBoxes.size() > 0 && player.getX() > textBoxes.get(0).playerX) {
            queue.add(textBoxes.remove(0));
        }
        if(currTextbox == null && queue.size() > 0) {
            currTextbox = queue.remove();
            currTextbox.sound.play();
            textBoxTime = 0f;
        }
        if(textBoxTime > 3f) {
            currTextbox = null;
        }
    }

    public void render() {
        if(currTextbox == null) return;

        String text = currTextbox.text.substring(0, Math.min(currTextbox.text.length(), (int) (textBoxTime*80f)));
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, text, -700, -200, 1400, Align.left, true);
        batch.end();

        // TODO Fade or something
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private class TextBox {
        String text;
        Sprite portrait;
        Sound sound;
        float playerX;
        private TextBox(String text, Sprite portrait, Sound sound, float playerX) {
            this.text = text;
            this.portrait = portrait;
            this.sound = sound;
            this.playerX = playerX;
        }
    }
}


