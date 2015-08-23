package alef.uchicago.edu;

import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * Created by Robert on 8/14/2015.
 */
public class AdvancedImageFilters {

    public static Effect reflection(){
        Reflection myReflection = new Reflection();
        myReflection.setFraction(.65);
        myReflection.bottomOpacityProperty();
        return myReflection;
    }

    public static Effect setHue(double hue){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(hue);
        return colorAdjust;
    }

    public static Effect dropShadowImage(){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(.8);
        dropShadow.setOffsetY(10);
        return dropShadow;
    }

    public static Effect innerShadowImage(){
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(10);
        innerShadow.setOffsetY(10);
        innerShadow.setRadius(9);
        innerShadow.setChoke(0);
        innerShadow.setColor(Color.DARKBLUE);

        return innerShadow;
    }

    //This doesn't actually resize the image when it comes to saving. it just makes the image easier to work on if
    //it is too big or too small in your screen.
    public static void resizeImage(Image image, AnchorPane ancPane, javafx.scene.image.ImageView imageViewer, double scalingLvl) {

        ancPane.setPrefSize(scalingLvl * image.getWidth(), scalingLvl * image.getHeight());
        imageViewer.setFitWidth(ancPane.getPrefWidth());
        imageViewer.setFitHeight(ancPane.getPrefHeight());
    }
}
