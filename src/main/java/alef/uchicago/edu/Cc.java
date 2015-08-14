package alef.uchicago.edu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * Created by Robert on 8/13/2015.
 */

//From jaLabJava chapter 3 horst
public class Cc {

    private static Cc stateManager;

    private Stage mMainStage;
    private ImageView imageViewer;
    private Image img, imgUndo;
    private int mPointTemp;

    public static final int MAX_UNDOS = 100;

    public static List<Image> backImages;

    public static Cc getInstance() {

        if(stateManager == null){
            stateManager = new Cc();
            backImages = new LinkedList<>();
        }

        return stateManager;
    }

    private Cc() {
    }

    //from Horstmann
    public static Image transform(Image in, UnaryOperator<Color> f) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(
                width, height);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                out.getPixelWriter().setColor(x, y,
                        f.apply(in.getPixelReader().getColor(x, y)));
        return out;
    }

    public static <T> Image transform(Image in, BiFunction<Color, T, Color> f, T arg) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(
                width, height);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                out.getPixelWriter().setColor(x, y,
                        f.apply(in.getPixelReader().getColor(x, y), arg));
        return out;
    }


    public Stage getMainStage() {
        return mMainStage;
    }

    public void setMainStage(Stage mMainStage) {
        this.mMainStage = mMainStage;
    }

    public ImageView getImageViewer() {
        return imageViewer;
    }

    public void setImageViewer(ImageView imageViewer) {
        this.imageViewer = imageViewer;
    }

    public Image getImg() {
        return img;
    }

    public void undo(){

        if (imgUndo != null){
            this.img = imgUndo;
            imageViewer.setImage(img);
        }
    }

    public void redo(){

    }


    public void setImageAndRefreshView(Image img){
        imgUndo = this.img;
        this.img = img;
        imageViewer.setImage(img);


    }


    public void close(ImageView imageView){

        imageView.setImage(null);
    }
}


