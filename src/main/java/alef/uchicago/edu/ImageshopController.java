package alef.uchicago.edu;

/**
 * Created by Robert on 8/10/2015.
 */

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
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
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.util.ResourceBundle;

public class ImageshopController implements Initializable {

    private ToggleGroup mToggleGroup = new ToggleGroup();

    public enum Pen {
        CIRCLE, SQUARE, FIL, OTHER;
    }

    public enum FilterStyle {
        SAT, DRK,  BRIGHT, INVERT, MONOCHROME, GOLD, OTHER
    }

    private int penSize = 50;
    private Pen penStyle = Pen.CIRCLE;
    private FilterStyle mFilterStyle = FilterStyle.DRK;
    private javafx.scene.paint.Color mColor = javafx.scene.paint.Color.WHITE;

    @FXML
    private ComboBox<String> cboSome;

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
    private AnchorPane ancPane;


    @FXML
    private Slider opacitySlider;

    @FXML
    private Slider scalingSlider;

    @FXML
    private Slider sepiaSlider;

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
    private MenuItem saturateMenuItem;

    @FXML
    private MenuItem dropShadow;

    @FXML
    private Slider offsetXShadow;


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
    private MenuItem monochromeMenuItem;

    @FXML
    private MenuItem invertMenuItem;

    @FXML
    private MenuItem goldenMenuItem;

    @FXML
    private MenuItem blurOffMenuItem;

    @FXML
    private ComboBox<String> boxFilters;

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

    private Image image;
    private ArrayList<Image> imageViewArrayList = new ArrayList<>();
    private ArrayList<Effect>  imageViewEffect = new ArrayList<>();
    private ArrayList<BlendMode> imageViewBlendMode = new ArrayList<>();
    private ArrayList<Rotate> imageViewRotate = new ArrayList<>();
    private ArrayList<javafx.scene.shape.Shape> removeShapes = new ArrayList<>(1000);

    final static SepiaTone sepiaEffect = new SepiaTone();
    //Positions we need to find for mouse events.
    private double xPosForMouseEvent, yPosForMouseEvent, hPosForMouseEvent, wPosForMouseEvent;

    private int imageCount = 0;
    BlurImage blurryEffect = new BlurImage();

    public void initialize(URL location, ResourceBundle resources) {

        cboSome.setValue("Darker");

        closeOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Cc.getInstance().close(imageViewer);
                imageViewArrayList = new ArrayList<>();
            }
        });

        openImage.setOnAction(t -> {
            ancPane.setVisible(true);
            Cc.getInstance().setImageViewer(this.imageViewer);
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

            File file = fileChooser.showOpenDialog(null);

            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
             //   Cc.getInstance().setImageAndRefreshView(image);
                imageViewer.setImage(image);
                imageViewArrayList.add(image);
                imageCount += 1;
            } catch (IOException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        undoOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                undo();
            }
        });

        redoOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                redo();
            }
        });

        saveAsOption.setOnAction(e -> saveToFile(imageViewer));


        opacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            opacityLvl = newValue.doubleValue();
        });
        hueSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            hueLvl = newValue.doubleValue();
        });
        hueBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setHue(imageViewer, hueLvl);
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
            }
        });

        offsetXShadow.valueProperty().addListener((observable1, oldValue, newValue) -> {
            dropShadowImage(imageViewer, newValue.doubleValue());
        });

        sepiaSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sepiaLvl = newValue.doubleValue();
        });
        sepiaBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sepiaEffect.setLevel(sepiaLvl);
                imageViewer.setEffect(sepiaEffect);
            }
        });
        mToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == tgbCircle) {
                    penStyle = Pen.CIRCLE;
                } else if (newValue == tgbSquare) {
                    penStyle = Pen.SQUARE;
                } else if (newValue == tgbFilter) {
                    penStyle = Pen.FIL;
                } else {
                    penStyle = Pen.FIL;
                }
            }
        });

        ancPane.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                System.out.println("Mouse pressed at point " + event.getX() + " and " + event.getY());
                if (penStyle == Pen.FIL) {
                    xPosForMouseEvent = (int) event.getSceneX();
                    yPosForMouseEvent = (int) event.getSceneY();
                }
                event.consume();
            }
        });

        ancPane.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_RELEASED, new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                System.out.println("Mouse released at: " + event.getX() + " " + event.getY());
                wPosForMouseEvent = (int) event.getSceneX();
                hPosForMouseEvent = (int) event.getSceneY();

                Image transformImage = null;

                switch (mFilterStyle) {
                    case DRK:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (x >= xPosForMouseEvent && x <= wPosForMouseEvent)
                                        && (y >= yPosForMouseEvent && y <= hPosForMouseEvent) ? c.deriveColor(0, 1, .5, 1) : c
                        );
                        break;
                    case SAT:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (x >= xPosForMouseEvent && x <= wPosForMouseEvent)
                                        && (y >= yPosForMouseEvent && y <= hPosForMouseEvent) ? c.deriveColor(0, 1.0 / .1, 1.0, 1.0) : c
                        );
                        break;
                    case BRIGHT:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (x >= xPosForMouseEvent && x <= wPosForMouseEvent)
                                        && (y >= yPosForMouseEvent && y <= hPosForMouseEvent) ? c.brighter() : c
                        );
                        break;
                    case MONOCHROME:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (x >= xPosForMouseEvent && x <= wPosForMouseEvent)
                                        && (y >= yPosForMouseEvent && y <= hPosForMouseEvent) ? c.grayscale() : c
                        );
                        break;
                    case INVERT:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (x >= xPosForMouseEvent && x <= wPosForMouseEvent)
                                        && (y >= yPosForMouseEvent && y <= hPosForMouseEvent) ? c.invert() : c
                        );
                        break;
                    case GOLD:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (x >= xPosForMouseEvent && x <= wPosForMouseEvent)
                                        && (y >= yPosForMouseEvent && y <= hPosForMouseEvent) ? c.interpolate(javafx.scene.paint.Color.GOLD, .7) : c
                        );
                        break;
                    default:

                }
                setMyImage(transformImage);
                event.consume();
            }
        });

        imageViewer.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_DRAGGED, new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                event.consume();
            }
        });

        cboSome.getItems().addAll(
                "Darker",
                "Saturate",
                "Brighter",
                "Invert",
                "Bling",
                "Monochrome"

        );


        cboSome.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                switch (newValue) {
                    case "Saturate":
                        mFilterStyle = FilterStyle.SAT;
                        break;
                    case "Darker":
                        mFilterStyle = FilterStyle.DRK;
                        break;
                    case "Brighter":
                        mFilterStyle = FilterStyle.BRIGHT;
                        break;
                    case "Invert":
                        mFilterStyle = FilterStyle.INVERT;
                        break;
                    case "Bling":
                        mFilterStyle = FilterStyle.GOLD;
                        break;
                    case "Monochrome":
                        mFilterStyle = FilterStyle.MONOCHROME;
                        break;

                    default:
                        mFilterStyle = FilterStyle.DRK;
                        break;

                }
            }
        });



        tgbCircle.setToggleGroup(mToggleGroup);
        tgbSquare.setToggleGroup(mToggleGroup);
        tgbFilter.setToggleGroup(mToggleGroup);
        tgbCircle.setSelected(true);

        mToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == tgbSquare) {
                    penStyle = Pen.SQUARE;
                } else if (newValue == tgbFilter) {
                    penStyle = Pen.FIL;
                } else {
                    penStyle = Pen.CIRCLE;
                }
            }
        });


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


//        sldSize.valueProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                double temp = (Double) newValue; //automatic unboxing
//                penSize = (int) Math.round(temp);
//            }
//        });


        gaussianBlur.setOnAction(event -> blurryEffect.gaussianBlurImage(imageViewer));
        motionBlur.setOnAction(event -> blurryEffect.motionBlurImage(imageViewer));
        boxBlur.setOnAction(event -> blurryEffect.boxBlurImage(imageViewer));
        blurOffMenuItem.setOnAction(event -> blurryEffect.blurImageOff(imageViewer));

        brightSelect.setOnAction(event -> setMyImage(ColoringImage.brighterImage(imageViewer)));
        darkSelect.setOnAction(event -> setMyImage(ColoringImage.darkerImage(imageViewer)));
        monochromeMenuItem.setOnAction(event -> setMyImage(ColoringImage.monochromeImage(imageViewer)));
        saturateMenuItem.setOnAction(event -> setMyImage(ColoringImage.saturateImage(imageViewer)));
        invertMenuItem.setOnAction(event -> setMyImage(ColoringImage.invertColorImage(imageViewer)));
        goldenMenuItem.setOnAction(event -> setMyImage(ColoringImage.goldenBlingOutImage(imageViewer)));

//        blndModeMultiply.setOnAction(event -> {blendModeMultiply(imageViewer);
//            imageViewArrayList.add(snapShot(imageViewer.getImage().getWidth(), imageViewer.getImage().getHeight()));
//            imageCount += 1;});
//
//        reflectionBtn.setOnAction(event -> {reflection(imageViewer);
//            imageViewArrayList.add(snapShot(imageViewer.getImage().getWidth(), imageViewer.getImage().getHeight()));
//            imageCount += 1;});
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

    //call setimage function ... reset head of arraylist, increment imagecount, display image
    private void setMyImage(Image image){

        imageCount += 1;
        imageViewArrayList.add(image);
        imageViewArrayList = new ArrayList<>(imageViewArrayList.subList(0, imageCount));
        imageViewer.setImage(image);
        System.out.println("Just added " + image.toString());
    }

    private void undo() {

        if (imageViewArrayList.size() == imageCount)
        {
            imageCount = Math.max(0, imageCount-2);
            imageViewer.setImage(imageViewArrayList.get(imageCount));
        }
        else
        {
            System.out.println("Before: pointing to: " + imageViewer.getImage().toString());

            if (imageViewArrayList.size() < 1) return;
            imageCount = Math.max(0, imageCount - 1);
            imageViewer.setImage(imageViewArrayList.get(imageCount));
            System.out.println("After pointing to: " + imageViewer.getImage().toString());
            System.out.println(imageViewArrayList.toString());
        }
    }

    private void redo()
    {

        if (imageViewArrayList.size() < 1 || imageCount >= imageViewArrayList.size()) return;
        imageCount = Math.max(0, imageCount + 1);
        imageViewer.setImage(imageViewArrayList.get(imageCount));
    }

    private Image snapShot(double width, double height){
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setViewport(new Rectangle2D(0, 0, width, height));
        Image snapShot = ancPane.snapshot(snapshotParameters, null);
        return snapShot;
    }
}

