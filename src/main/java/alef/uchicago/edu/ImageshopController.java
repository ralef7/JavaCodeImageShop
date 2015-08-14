package alef.uchicago.edu;

/**
 * Created by Robert on 8/10/2015.
 */

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.util.ResourceBundle;

public class ImageshopController implements Initializable {

    private ToggleGroup mToggleGroup = new ToggleGroup();

    public enum Pen {
        CIRCLE, SQUARE, FIL, OTHER;
    }

    private int penSize = 50;
    private Pen penStyle = Pen.CIRCLE;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private MenuItem redoOption;

    @FXML
    private MenuItem undoOption;

    @FXML
    private MenuItem openImage;

    @FXML
    private MenuItem saveAsOption;

    @FXML
    private MenuItem exitOption;

    @FXML
    private MenuItem closeOption;

    @FXML
    private MenuItem saveOption;

    @FXML
    private ImageView imageViewer;

    @FXML
    void exitOptionAction(ActionEvent event) {

        System.exit(0);
    }

    @FXML
    private ToggleButton monoChromeToggle;

    @FXML
    private Pane imagePane;

    @FXML
    private Slider opacitySlider;

    @FXML
    private Slider scalingSlider;

    @FXML
    private Slider sepiaSlider;

    @FXML
    private Button rotateBtn;

    @FXML
    private MenuItem motionBlur;

    @FXML
    private MenuItem boxBlur;

    @FXML
    private MenuItem gaussianBlur;

    @FXML
    private MenuItem brightSelect;

    @FXML
    private MenuItem darkSelect;

    @FXML
    private Slider hueSlider;

    @FXML
    private CheckMenuItem saturateCheckBtn;

    @FXML
    private MenuItem dropShadow;

    @FXML
    private Slider offsetXShadow;

    @FXML
    private Slider saturationSlider;

    @FXML
    private MenuItem blndModeMultiply;

    @FXML
    private MenuItem reflectionBtn;

    @FXML
    private ToggleButton tgbSquare;

    @FXML
    private ToggleButton tgbCircle;

    @FXML
    private ToggleButton tgbFilter;

    @FXML
    private Button hueBtn;
    private double hueLvl;

    @FXML
    private Button sepiaBtn;
    private double sepiaLvl;

    @FXML
    private Button opacityBtn;
    private double opacityLvl;

    @FXML
    private Button scalingBtn;
    private double scalingLvl;


    final static SepiaTone sepiaEffect = new SepiaTone();
    private final ColorAdjust monochrome = new ColorAdjust(0, -1, 0, 0);
    private Image image;
    private ArrayList<Image> imageViewArrayList = new ArrayList<>();
    private int imageCount = 0;
    BlurImage blurryEffect = new BlurImage();

    public void initialize(URL location, ResourceBundle resources) {

        closeOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Cc.getInstance().close(imageViewer);
            }
        });

        openImage.setOnAction(t -> {
            imagePane.setVisible(true);
            Cc.getInstance().setImageViewer(this.imageViewer);
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

            File file = fileChooser.showOpenDialog(null);

            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                Cc.getInstance().setImageAndRefreshView(image);
                imageViewArrayList.add(image);
                imageCount += 1;
            } catch (IOException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        undoOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(imageViewer.getImage().getHeight());
                imageViewer.setImage(undo(imageViewer.getImage(), imageViewArrayList));
                System.out.println(imageViewer.getImage().getHeight());
            }
        });

        saveAsOption.setOnAction(e -> saveToFile(imageViewer));

        monoChromeToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (monoChromeToggle.isSelected()) {
                    imageViewer.setEffect(monochrome);
                } else {
                    imageViewer.setEffect(null);
                }
                ImageView freezeImage = new ImageView();
                freezeImage.setImage(imageViewer.getImage());
                imageViewArrayList.add(snapShot(imageViewer.getImage().getWidth(), imageViewer.getImage().getHeight()));
                System.out.println(imageViewArrayList);
                imageCount += 1;
            }
        });

        opacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            opacityLvl = newValue.doubleValue();
        });
        opacityBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                imageViewer.setOpacity(opacityLvl);
                ImageView freezeImage = new ImageView();
                freezeImage.setImage(imageViewer.getImage());
                imageViewArrayList.add(snapShot(imageViewer.getImage().getWidth(), imageViewer.getImage().getHeight()));
                imageCount += 1;

            }
        });

        hueSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            hueLvl = newValue.doubleValue();
        });
        hueBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setHue(imageViewer, hueLvl);
                ImageView freezeImage = new ImageView();
                freezeImage.setImage(imageViewer.getImage());
                imageViewArrayList.add(snapShot(imageViewer.getImage().getWidth(), imageViewer.getImage().getHeight()));
                imageCount += 1;
            }
        });

        scalingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            scalingLvl = newValue.doubleValue();
        });
        scalingBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageViewer.setScaleX(scalingLvl);
                imageViewer.setScaleY(scalingLvl);
//                ImageView freezeImage = new ImageView();
//                freezeImage.setImage(imageViewer.getImage());
                imageViewArrayList.add(snapShot(imageViewer.getImage().getWidth(), imageViewer.getImage().getHeight()));
                imageCount += 1;
            }
        });

        offsetXShadow.valueProperty().addListener((observable1, oldValue, newValue) -> {
            dropShadowImage(imageViewer, newValue.doubleValue());
//            SnapshotParameters snapshotParameters = new SnapshotParameters();
//            snapshotParameters.setViewport(new Rectangle2D(0, 0, imageViewer.getFitWidth(), imageViewer.getFitHeight()));
//            Image snapShot = imagePane.snapshot(snapshotParameters, null);
            imageViewArrayList.add(snapShot(imageViewer.getImage().getWidth(), imageViewer.getImage().getHeight()));
            imageCount += 1;
        });

        sepiaSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sepiaLvl = newValue.doubleValue();
        });
        sepiaBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sepiaEffect.setLevel(sepiaLvl);
                imageViewer.setEffect(sepiaEffect);
//                SnapshotParameters snapshotParameters = new SnapshotParameters();
//                snapshotParameters.setViewport(new Rectangle2D(0, 0, imageViewer.getImage().getWidth(), imageViewer.getImage().getHeight()));
//                Image snapShot = imagePane.snapshot(snapshotParameters, null);
                imageViewArrayList.add(snapShot(imageViewer.getImage().getWidth(), imageViewer.getImage().getHeight()));
                imageCount += 1;
            }
        });
        rotateBtn.setOnAction(event -> {
            if ("Reflect".equals(rotateBtn.getText())) {
                imageViewer.setRotate(180);
                rotateBtn.setText("Restore");
            } else {
                imageViewer.setRotate(0);
                rotateBtn.setText("Reflect");
            }
            imageViewArrayList.add(snapShot(imageViewer.getImage().getWidth(), imageViewer.getImage().getHeight()));
            imageCount += 1;
        });


        tgbCircle.setToggleGroup(mToggleGroup);
        tgbSquare.setToggleGroup(mToggleGroup);
        tgbFilter.setToggleGroup(mToggleGroup);
        tgbCircle.setSelected(true);

        mToggleGroup.selectedToggleProperty().addListener(new javafx.beans.value.ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == tgbCircle) {
                    penStyle = Pen.CIRCLE;
                } else if (newValue == tgbSquare) {
                    penStyle = Pen.SQUARE;
                } else if (newValue == tgbFilter) {
                    penStyle = Pen.FIL;
                } else {
                    penStyle = Pen.CIRCLE;
                }
            }
        });

        gaussianBlur.setOnAction(event -> {blurryEffect.gaussianBlurImage(imageViewer);
            imageCount += 1;
        });

        motionBlur.setOnAction(event -> blurryEffect.motionBlurImage(imageViewer));

        boxBlur.setOnAction(event -> blurryEffect.boxBlurImage(imageViewer));

        brightSelect.setOnAction(event -> ColoringImage.brighterImage(imageViewer));

        darkSelect.setOnAction(event -> ColoringImage.darkerImage(imageViewer));

        saturationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ColoringImage.saturateImage(imageViewer, newValue.doubleValue());
        });

        blndModeMultiply.setOnAction(event -> blendModeMultiply(imageViewer));

        reflectionBtn.setOnAction(event -> reflection(imageViewer));
    }

        public static void saveToFile(ImageView imageView){
        FileChooser fileChooser = new FileChooser();
        File outputFile = fileChooser.showSaveDialog(null);

        try{

            ImageIO.write(SwingFXUtils.fromFXImage(imageView.snapshot(null, null), null), "png", outputFile);
        }
        catch (IOException e){
            e.getStackTrace();
        }
    }

    private void setHue(ImageView imageView, double hue){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(hue);
        imageView.setEffect(colorAdjust);
    }

    private void dropShadowImage(ImageView imageView, double offsetX){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(offsetX);
        dropShadow.setOffsetY(5);
        imageView.setEffect(dropShadow);
    }

    private void blendModeMultiply(ImageView imageView) {
        imageView.setBlendMode(BlendMode.MULTIPLY);
    }

    private void reflection(ImageView imageView){
        Reflection myReflection = new Reflection();
        myReflection.setFraction(.65);
        myReflection.bottomOpacityProperty();
        imageView.setEffect(myReflection);
    }

    private Image undo(Image currentImage, ArrayList<Image> imageArrayList) {
        System.out.println("Before: pointing to: " + imageArrayList.get(imageCount-1).toString());
        if (imageCount > 0)
        if (imageArrayList.size() > 1){
            currentImage = imageArrayList.get(imageCount-2);
        }
        else {
            currentImage = imageArrayList.get(imageArrayList.size()-1);
        }
        imageCount -= 1;
        System.out.println("After pointing to " + currentImage.toString());
        return currentImage;
    }

    private Image snapShot(double width, double height){
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setViewport(new Rectangle2D(0, 0, width, height));
        Image snapShot = imagePane.snapshot(snapshotParameters, null);
        return snapShot;
    }
}

