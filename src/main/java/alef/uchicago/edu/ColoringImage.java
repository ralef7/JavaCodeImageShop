package alef.uchicago.edu;

import com.sun.prism.j2d.paint.*;
import com.sun.prism.j2d.paint.MultipleGradientPaint;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.awt.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.*;

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

    public static Image darkerImage(ImageView imageView) {
        Image curImage = imageView.getImage();
        Image darker = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return colorAtXY.darker();
            }
        });
        return darker;
    }

    public static Image saturateImage(ImageView imageView) {
        Image curImage = imageView.getImage();
        Image saturated = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return colorAtXY.saturate();
            }
        });
        return saturated;
    }

    public static Image monochromeImage(ImageView imageView) {
        Image curImage = imageView.getImage();
        Image monochrome = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return colorAtXY.grayscale();
            }
        });
        return monochrome;
    }

    public static Image invertColorImage(ImageView imageView) {
        Image curImage = imageView.getImage();
        Image invert = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return colorAtXY.invert();
            }
        });
        return invert;
    }

    public static Image goldenBlingOutImage(ImageView imageView) {
        Image curImage = imageView.getImage();
        Image golden = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return colorAtXY.interpolate(javafx.scene.paint.Color.GOLD, .8);
            }
        });
        return golden;
    }

    public static Image desaturateImage(ImageView imageView) {
        Image curImage = imageView.getImage();
        Image desaturate = ImageTransform.transform(curImage, new ColorTransformer() {
            @Override
            public javafx.scene.paint.Color apply(int x, int y, javafx.scene.paint.Color colorAtXY) {
                return colorAtXY.desaturate();
            }
        });
        return desaturate;
    }

    //http://harmoniccode.blogspot.com/2012/07/more-fun-with-pixels.html
    public static Image pixelateImage(ImageView imageView) {
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
    //http://harmoniccode.blogspot.com/2012/07/more-fun-with-pixels.html
//    public static Group pixelate(Image image, int blockSize) {
//        PixelReader pixelReader = image.getPixelReader();
//        int width = (int) image.getWidth();
//        int height = (int) image.getHeight();
//        Group newImage = new Group();
//        java.util.List<javafx.scene.paint.Color> colors = new ArrayList<>();
//
//
//        for (int y = 0; y < height; y += blockSize) {
//            for (int x = 0; x < width; x += blockSize) {
//                javafx.scene.paint.Color col = pixelReader.getColor(x, y);
//                int newRed = 0;
//                int newGreen = 0;
//                int newBlue = 0;
//                colors.clear();
//
//                for (int blockY = y; blockY < y + blockSize; ++blockY) {
//                    for (int blockX = x; blockX < x + blockSize; ++blockX) {
//                        if (blockX < 0 || blockX >= width) {
//                            colors.add(col);
//                            continue;
//                        }
//                        if (blockY < 0 || blockY >= height) {
//                            colors.add(col);
//                            continue;
//                        }
//                        colors.add(pixelReader.getColor(blockX, blockY));
//                    }
//                }
//
//                for (javafx.scene.paint.Color color : colors) {
//                    newRed += (int) (color.getRed() * 255) & 0xFF;
//                    newGreen += (int) (color.getGreen() * 255) & 0xFF;
//                    newBlue += (int) (color.getBlue() * 255) & 0xFF;
//                }
//
//                int noOfColors = colors.size();
//                newRed /= noOfColors;
//                newGreen /= noOfColors;
//                newBlue /= noOfColors;
//
//
//                javafx.scene.shape.Rectangle box = new javafx.scene.shape.Rectangle(x + blockSize - 1, y + blockSize - 1,
//                        blockSize, blockSize);
//                box.setFill(javafx.scene.paint.Color.rgb(newRed, newGreen, newBlue));
//                newImage.getChildren().add(box);
//            }
//        }
//       return newImage;
//    }
}
