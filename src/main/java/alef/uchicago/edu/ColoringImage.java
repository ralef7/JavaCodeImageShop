package alef.uchicago.edu;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

/**
 * Created by Robert on 8/12/2015.
 */
public class ColoringImage {

    public static void brighterImage(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.5);
        imageView.setEffect(colorAdjust);
    }

    public static void darkerImage(ImageView imageView){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-.5);
        imageView.setEffect(colorAdjust);
    }

    public static void saturateImage(ImageView imageView, double saturateLevel){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(saturateLevel);
        imageView.setEffect(colorAdjust);
    }


}
