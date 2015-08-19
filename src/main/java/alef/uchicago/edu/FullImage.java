package alef.uchicago.edu;

import javafx.scene.effect.Effect;
import javafx.scene.image.Image;

/**
 * Created by Robert on 8/19/2015.
 */
public class FullImage {
    private Effect effect;
    private Image image;

    public FullImage(Effect effect, Image image){
        this.effect = effect;
        this.image = image;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
