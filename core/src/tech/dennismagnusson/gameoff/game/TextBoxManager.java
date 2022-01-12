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

    Sprite textBoxBackground;

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
        Texture t = new Texture(Gdx.files.internal("black.png"));
        game.disposables.add(t);
        backgroundImage = new Sprite(t);
        backgroundImage.setSize(1600, 250);
        backgroundImage.setPosition(-800, -450);
        backgroundImage.setAlpha(0.1f);
    }

    public void addTextBox(String text, String soundFilename, String sprite, float playerX) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/"+soundFilename));
        game.disposables.add(sound);
        Texture t = new Texture(Gdx.files.internal(sprite));
        Sprite s = new Sprite(t);
        s.setPosition(-800, -450f);
        s.setSize(300, 600);
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

        float alpha = 1f;
        if(textBoxTime > 2.5f && queue.isEmpty())
            alpha = 6-textBoxTime*2f;
        if(textBoxTime < 0.5f)
            alpha = textBoxTime*2f;
        System.out.println("ALPHA="+alpha);

        String text = currTextbox.text.substring(0, Math.min(currTextbox.text.length(), (int) (textBoxTime*80f)));
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        backgroundImage.setAlpha(0.1f*alpha);
        backgroundImage.draw(batch);
        currTextbox.portrait.setAlpha(alpha);
        currTextbox.portrait.draw(batch);
        font.draw(batch, text, -450, -300, 1100, Align.left, true);
        batch.end();
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


