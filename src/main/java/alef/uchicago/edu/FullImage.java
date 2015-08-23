package alef.uchicago.edu;

import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;

/**
 * Created by Robert on 8/19/2015.
 */
public class FullImage {
    private Effect effect;
    private Image image;
    private BlendMode blendMode;

    public FullImage(Effect effect, Image image, BlendMode blendMode){
        this.effect = effect;
        this.image = image;
        this.blendMode = blendMode;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {this.effect = effect;}

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setBlendMode(BlendMode blendMode) {this.blendMode = blendMode; }

    public BlendMode getBlendMode(){return blendMode; }

}
