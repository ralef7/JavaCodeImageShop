package alef.uchicago.edu;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.AnchorPane;

import javax.swing.text.html.ImageView;

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
        dropShadow.setOffsetX(.7);
        dropShadow.setOffsetY(5);
        return dropShadow;
    }

    public static void resizeImage(AnchorPane ancPane, javafx.scene.image.ImageView imageViewer, double scalingLvl) {
        ancPane.setScaleX(scalingLvl);
        ancPane.setScaleY(scalingLvl);
        imageViewer.setFitWidth(ancPane.getPrefWidth());
        imageViewer.setFitHeight(ancPane.getPrefHeight());
    }
}
