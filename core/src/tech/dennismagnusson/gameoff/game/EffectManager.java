package tech.dennismagnusson.gameoff.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;

public class EffectManager {

    public VfxManager manager;
    private FilmGrainEffect noise;
    public RadialBlurEffect radialBlur;
    public ChromaticAberrationEffect chromaticAberration;

    private final boolean enabled = true;

    public EffectManager() {
        manager = new VfxManager(Pixmap.Format.RGB888);
        chromaticAberration = new ChromaticAberrationEffect(5);
        manager.addEffect(chromaticAberration);
        manager.addEffect(new BloomEffect());
        manager.addEffect(new GaussianBlurEffect());
        radialBlur = new RadialBlurEffect(2);
        radialBlur.setStrength(0.075f);
        // vfxManager.addEffect(rbe);
        manager.addEffect(new VignettingEffect(false));
        noise = new FilmGrainEffect();
        noise.setNoiseAmount(0.3f);
        manager.addEffect(noise);
    }

    public void update(float delta) {
        if(!enabled) return;
        manager.cleanUpBuffers();
        manager.update(delta);
    }

    public void begin() {
        if(!enabled) return;
        manager.beginInputCapture();
    }

    public void end() {
        if(!enabled) return;
        manager.endInputCapture();
        manager.applyEffects();
        manager.renderToScreen();
    }

    public void setNoise(float amount) {
        noise.setNoiseAmount(amount);
    }
}
