package alef.uchicago.edu;

/**
 * Created by Robert on 8/10/2015.
 */

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    final static SepiaTone sepiaEffect = new SepiaTone();

    private final ColorAdjust monochrome = new ColorAdjust(0, -1, 0, 0);
    private Image image;
    BlurImage blurryEffect = new BlurImage();

    public void initialize(URL location, ResourceBundle resources) {

        openImage.setOnAction(t -> {
            imagePane.setVisible(true);
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

            File file = fileChooser.showOpenDialog(null);

            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                System.out.println(image.toString());
                imageViewer.setImage(image);
                imageViewer.getImage();
            } catch (IOException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
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
            }
        });

        opacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            imageViewer.setOpacity(newValue.doubleValue());
        });


        hueSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            setHue(imageViewer, newValue.doubleValue());
        });

        scalingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            imageViewer.setScaleX(newValue.doubleValue());
            imageViewer.setScaleY(newValue.doubleValue());
        });


        offsetXShadow.valueProperty().addListener((observable1, oldValue, newValue) -> {
                dropShadowImage(imageViewer, newValue.doubleValue());
        });

        sepiaSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sepiaEffect.setLevel(newValue.doubleValue());
            imageViewer.setEffect(sepiaEffect);
        });
        rotateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if ("Reflect".equals(rotateBtn.getText())) {
                    imageViewer.setRotate(180);
                    rotateBtn.setText("Restore");
                } else {
                    imageViewer.setRotate(0);
                    rotateBtn.setText("Reflect");
                }
            }
        });

        gaussianBlur.setOnAction(event -> blurryEffect.gaussianBlurImage(imageViewer));

        motionBlur.setOnAction(event -> blurryEffect.motionBlurImage(imageViewer));

        boxBlur.setOnAction(event -> blurryEffect.boxBlurImage(imageViewer));

        brightSelect.setOnAction(event -> brighterImage(imageViewer));

        darkSelect.setOnAction(event -> darkerImage(imageViewer));

//        dropShadow.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                dropShadowImage(imageViewer, 10);
//            }
//        });

        if (!saturateCheckBtn.isSelected()){
            saturateCheckBtn.setOnAction(event -> saturateImage(imageViewer, .3));
        }

    }
//        saveAsOption.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                try {
//                    File output = new File("C:/JavaFX/");
//                    System.out.println("Trying to save");
//
//                    ImageIO.write(SwingFXUtils.fromFXImage(imageViewer.snapshot(null, null), null), "jpg", output);
//                } catch(IOException e)
//                    {
//                        System.out.println("Saving failed because of " + e);
//                    }
//            }
//        });
        public static void saveToFile(ImageView imageView){
        FileChooser fileChooser = new FileChooser();

        File outputFile = fileChooser.showSaveDialog(null);

       // BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try{

            ImageIO.write(SwingFXUtils.fromFXImage(imageView.snapshot(null, null), null), "png", outputFile);
        }
        catch (IOException e){
            e.getStackTrace();
        }
    }



    private void brighterImage(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.5);
        imageView.setEffect(colorAdjust);
    }

    private void darkerImage(ImageView imageView){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-.5);
        imageView.setEffect(colorAdjust);
    }

    private void setHue(ImageView imageView, double hue){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(hue);
        imageView.setEffect(colorAdjust);
    }

    private void saturateImage(ImageView imageView, double saturateLevel){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(saturateLevel);
        imageView.setEffect(colorAdjust);
    }

    private void dropShadowImage(ImageView imageView, double offsetX){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(offsetX);
        dropShadow.setOffsetY(5);
        imageView.setEffect(dropShadow);
    }

}

