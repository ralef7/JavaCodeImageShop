package alef.uchicago.edu;

import com.sun.prism.j2d.paint.*;
import com.sun.prism.j2d.paint.MultipleGradientPaint;
import javafx.event.ActionEvent;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.*;

import java.awt.*;
import java.awt.Color;

/**
 * Created by Robert on 8/12/2015.
 */
public class ColoringImage {

    public static Image brighterImage(ImageView imageView) {
        Image curImage = imageView.getImage();
        Image brighter = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return colorAtXY.brighter();
            }
        });
        return brighter;
    }

    public static Image darkerImage(ImageView imageView){
        Image curImage = imageView.getImage();
        Image darker = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return colorAtXY.darker();
            }
        });
       return darker;
    }

    public static Image saturateImage(ImageView imageView){
        Image curImage = imageView.getImage();
        Image saturated = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return  colorAtXY.saturate();
            }
        });
        return saturated;
    }

    public static Image monochromeImage (ImageView imageView)
    {
        Image curImage = imageView.getImage();
        Image monochrome = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return  colorAtXY.grayscale();
            }
        });
        return monochrome;
    }

    public static Image invertColorImage (ImageView imageView)
    {
        Image curImage = imageView.getImage();
        Image invert = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return colorAtXY.invert();
            }
        });
        return invert;
    }

    public static Image goldenBlingOutImage (ImageView imageView)
    {
            Image curImage = imageView.getImage();
            Image golden = ImageTransform.transform(curImage, new ColorTransformer() {
                @Override
                public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                    return colorAtXY.interpolate(javafx.scene.paint.Color.GOLD, .8);
                }
            });
            return golden;
    }

    public static Image desaturateImage (ImageView imageView)
    {
        Image curImage = imageView.getImage();
        Image desaturate = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return colorAtXY.desaturate();
            }
        });
        return desaturate;
    }

    public static Image linearGradientImage (ImageView imageView)
    {
        javafx.scene.paint.Color wickedGradient = javafx.scene.paint.Color.GREEN.interpolate(javafx.scene.paint.Color.RED, 35 / 100);
        Image curImage = imageView.getImage();
        Image desaturate = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return colorAtXY.interpolate(wickedGradient, .35);
            }
        });
        return desaturate;
    }
}
