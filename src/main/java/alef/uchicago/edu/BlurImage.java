package alef.uchicago.edu;

import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.ImageView;

/**
 * Created by Robert on 8/12/2015.
 */
public class BlurImage {

    public Effect gaussianBlurImage(){
        GaussianBlur blurEffect = new GaussianBlur();
        return blurEffect;
    }


    public Effect boxBlurImage(){
        BoxBlur boxBlurEffect = new BoxBlur();
        boxBlurEffect.setWidth(7);
        boxBlurEffect.setHeight(7);
        boxBlurEffect.setIterations(3);
        return boxBlurEffect;
    }

    public Effect motionBlurImage(){
        MotionBlur motionBlur = new MotionBlur();

        motionBlur.setRadius(20.0f);
        motionBlur.setAngle(50.0f);
        return motionBlur;
    }

    public void blurImageOff(ImageView imageView){
        imageView.setEffect(null);
    }
}
