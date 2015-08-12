package alef.uchicago.edu;

import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.ImageView;

/**
 * Created by Robert on 8/12/2015.
 */
public class BlurImage {

    public void gaussianBlurImage(ImageView imageView){
        GaussianBlur blurEffect = new GaussianBlur();

        imageView.setEffect(blurEffect);

    }

    public void boxBlurImage(ImageView imageView){
        BoxBlur boxBlurEffect = new BoxBlur();
        boxBlurEffect.setWidth(7);
        boxBlurEffect.setHeight(7);
        boxBlurEffect.setIterations(3);
        imageView.setEffect(boxBlurEffect);
    }

    public void motionBlurImage(ImageView imageView){
        MotionBlur motionBlur = new MotionBlur();

        motionBlur.setRadius(20.0f);
        motionBlur.setAngle(50.0f);
        imageView.setEffect(motionBlur);
    }
}
