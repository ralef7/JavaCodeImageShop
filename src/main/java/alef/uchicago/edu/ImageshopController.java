package alef.uchicago.edu;

/**
 * Created by Robert on 8/10/2015.
 */

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.util.ResourceBundle;

public class ImageshopController implements Initializable {

    private ToggleGroup mToggleGroup = new ToggleGroup();

    public enum Pen {
        CIRCLE, SQUARE, FIL, SUNLIGHT;
    }

    public enum FilterStyle {
        SAT, DRK,  BRIGHT, INVERT, MONOCHROME, GOLD, DESATURATE, WHITEOUT, BLACKOUT }

    private int penSize = 50;
    private Pen penStyle = Pen.CIRCLE;
    private FilterStyle mFilterStyle = FilterStyle.DRK;
    private javafx.scene.paint.Color mColor = javafx.scene.paint.Color.WHITE;

    @FXML
    private MenuItem mnuItemDifferenceBlend;

    @FXML
    private MenuItem mnuItemColorBurnBlend;

    @FXML
    private MenuItem blendModeOff;

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
    private ImageView imageViewer;

    @FXML
    void exitOptionAction(ActionEvent event) {

        System.exit(0);
    }

    @FXML
    private AnchorPane ancPane;


    @FXML
    private Slider sldSize;

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
    private MenuItem reflectionBtn;

    @FXML
    private MenuItem innerShadowBtn;

    @FXML
    private ToggleButton tgbSquare;

    @FXML
    private ToggleButton tgbCircle;

    @FXML
    private ToggleButton tgbSunlight;

    @FXML
    private ToggleButton tgbFilter;

    @FXML
    private MenuItem monochromeMenuItem;

    @FXML
    private MenuItem desaturateMenuItem;

    @FXML
    private MenuItem invertMenuItem;

    @FXML
    private MenuItem goldenMenuItem;

    @FXML
    private MenuItem blurOffMenuItem;

    @FXML
    private MenuItem gradientMenu;

    @FXML
    private Button hueBtn;
    private double hueLvl;

    @FXML
    private Button sepiaBtn;
    private double sepiaLvl;

    @FXML
    private Pane imgPane;

    private Image image;
    private ArrayList<FullImage> fullImageArrayList = new ArrayList<>();
    private ArrayList<javafx.scene.shape.Shape> removeShapes = new ArrayList<>(1000);
    double viewerWidth;
    double viewerHeight;

    final static SepiaTone sepiaEffect = new SepiaTone();
    //Positions we need to find for mouse events.
    private int xPosForMouseEvent, yPosForMouseEvent, hPosForMouseEvent, wPosForMouseEvent;

    private int imageCount = 0;
    BlurImage blurryEffect = new BlurImage();

    public void initialize(URL location, ResourceBundle resources) {

        cboSome.setValue("Darker");

        closeOption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Cc.getInstance().close(imageViewer);
                imageCount = 0;
                ancPane.setVisible(false);
                fullImageArrayList = new ArrayList<>();
                imageViewer.setBlendMode(null);
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
                imageViewer.setImage(image);
                imageViewer.setEffect(null);

                ancPane.setPrefSize(image.getWidth(), image.getHeight());
                imageViewer.setFitWidth(ancPane.getPrefWidth());
                imageViewer.setFitHeight(ancPane.getPrefHeight());

                viewerHeight = image.getHeight();
                viewerWidth = image.getWidth();

                fullImageArrayList.add(new FullImage(null, image, imageViewer.getBlendMode()));
                imageCount += 1;

            } catch (IOException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        undoOption.setOnAction(event -> undo());

        redoOption.setOnAction(event -> redo());

        saveAsOption.setOnAction(e -> saveToFile());

        mToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == tgbCircle) {
                penStyle = Pen.CIRCLE;
            } else if (newValue == tgbSquare) {
                penStyle = Pen.SQUARE;
            } else if (newValue == tgbFilter) {
                penStyle = Pen.FIL;
            } else if (newValue == tgbSunlight) {
                penStyle = Pen.SUNLIGHT;
            } else {
                penStyle = Pen.FIL;
            }
        });

        ancPane.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (penStyle == Pen.FIL)
            {
                xPosForMouseEvent = (int) event.getX();
                yPosForMouseEvent = (int) event.getY();
            }
            event.consume();
        });

        ancPane.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_DRAGGED, event -> {
            if (penStyle == Pen.FIL) {
                event.consume();
                return;
            }
            xPosForMouseEvent = (int) event.getX();
            yPosForMouseEvent = (int) event.getY();

            int nShape = 0;
            //default value
            Shape shape = new Circle(xPosForMouseEvent, yPosForMouseEvent, 10);
            switch (penStyle) {
                case CIRCLE:
                    shape = new Circle(xPosForMouseEvent, yPosForMouseEvent, penSize);
                    break;
                case SQUARE:
                    shape = new Rectangle(xPosForMouseEvent, yPosForMouseEvent, penSize, penSize);
                    break;
                case SUNLIGHT:
                    shape = new Polygon(xPosForMouseEvent, yPosForMouseEvent, penSize, penSize);
                    break;
                default:
                    shape = new Circle(xPosForMouseEvent, yPosForMouseEvent, penSize);
                    break;
            }

            shape.setStroke(mColor);
            shape.setFill(mColor);

            ancPane.getChildren().add(shape);
            removeShapes.add(shape);
            event.consume();
        });

        ancPane.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_RELEASED, event -> {
            System.out.println("Mouse released at: " + event.getX() + " " + event.getY());
            wPosForMouseEvent = (int) event.getX();
            hPosForMouseEvent = (int) event.getY();
            if (penStyle == Pen.CIRCLE || penStyle == Pen.SQUARE || penStyle == Pen.SUNLIGHT) {

                Image snapshot = snapShot(viewerWidth, viewerHeight);
                setMyImage(snapshot, imageViewer.getEffect(), imageViewer.getBlendMode());
                ancPane.getChildren().removeAll(removeShapes);
                removeShapes.clear();
                System.out.println("Image params: " + "Height: " + imageViewer.getImage().getHeight() + "Width: " + imageViewer.getImage().getWidth());
                System.out.println("ImageViewer params: " + "Height: " + imageViewer.getFitHeight() + "Width: " + imageViewer.getFitWidth());
                System.out.println("AncPane params: " + "Height: " + ancPane.getHeight() + " Width " + ancPane.getWidth());
                System.out.println("Pane Params: " + "Height: " + imgPane.getHeight() + "Width " + imgPane.getWidth());

            } else {

                Image transformImage = snapShot(wPosForMouseEvent - xPosForMouseEvent, hPosForMouseEvent - yPosForMouseEvent);

                switch (mFilterStyle) {
                    case DRK:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (x > xPosForMouseEvent && x < wPosForMouseEvent)
                                        && (y > yPosForMouseEvent && y < hPosForMouseEvent) ? c.deriveColor(0, 1, .5, 1) : c
                        );
                        break;
                    case SAT:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (x > xPosForMouseEvent && x < wPosForMouseEvent)
                                        && (y > yPosForMouseEvent && y < hPosForMouseEvent) ? c.deriveColor(0, 1.0 / .1, 1.0, 1.0) : c
                        );
                        break;
                    case BRIGHT:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (x > xPosForMouseEvent && x < wPosForMouseEvent)
                                        && (y > yPosForMouseEvent && y < hPosForMouseEvent) ? c.brighter() : c
                        );
                        break;
                    case MONOCHROME:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (x > xPosForMouseEvent && x < wPosForMouseEvent)
                                        && (y > yPosForMouseEvent && y < hPosForMouseEvent) ? c.grayscale() : c
                        );
                        break;
                    case INVERT:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (xPosForMouseEvent < x && wPosForMouseEvent > x
                                        && yPosForMouseEvent < y && hPosForMouseEvent > y) ? c.invert() : c
                        );
                        System.out.println("should have painted: " + xPosForMouseEvent + " " + yPosForMouseEvent + " to " + wPosForMouseEvent + " " + hPosForMouseEvent);
                        break;
                    case GOLD:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (x > xPosForMouseEvent && x < wPosForMouseEvent)
                                        && (y > yPosForMouseEvent && y < hPosForMouseEvent) ? c.interpolate(javafx.scene.paint.Color.GOLD, .7) : c
                        );
                        break;
                    case DESATURATE:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (xPosForMouseEvent < x && wPosForMouseEvent > x
                                        && yPosForMouseEvent < y && hPosForMouseEvent > y) ? c.desaturate() : c
                        );
                        break;
                    case WHITEOUT:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (xPosForMouseEvent < x && wPosForMouseEvent > x
                                        && yPosForMouseEvent < y && hPosForMouseEvent > y) ? c.web("White") : c
                        );
                        break;
                    case BLACKOUT:
                        transformImage = ImageTransform.transform(imageViewer.getImage(),
                                (x, y, c) -> (xPosForMouseEvent < x && wPosForMouseEvent > x
                                        && yPosForMouseEvent < y && hPosForMouseEvent > y) ? c.web("Black") : c
                        );
                        break;
                    default:
                }
                setMyImage(transformImage, imageViewer.getEffect(), imageViewer.getBlendMode());
            }
            event.consume();
        });

        colorPicker.setOnAction(event -> mColor = colorPicker.getValue());

        cboSome.getItems().addAll(
                "Darker",
                "Saturate",
                "Brighter",
                "Invert",
                "Bling",
                "Monochrome",
                "Desaturate",
                "Blackout",
                "Whiteout"
        );

        cboSome.valueProperty().addListener((observable, oldValue, newValue) -> {
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
                case "Desaturate":
                    mFilterStyle = FilterStyle.DESATURATE;
                    break;
                case "Blackout":
                    mFilterStyle = FilterStyle.BLACKOUT;
                    break;
                case "Whiteout":
                    mFilterStyle = FilterStyle.WHITEOUT;
                    break;
                default:
                    mFilterStyle = FilterStyle.DRK;
                    break;
            }
        });

        tgbCircle.setToggleGroup(mToggleGroup);
        tgbSquare.setToggleGroup(mToggleGroup);
        tgbFilter.setToggleGroup(mToggleGroup);
        tgbSunlight.setToggleGroup(mToggleGroup);
        tgbFilter.setSelected(true);

        mToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == tgbSquare) {
                penStyle = Pen.SQUARE;
            } else if (newValue == tgbFilter) {
                penStyle = Pen.FIL;
            } else if (newValue == tgbSunlight) {
                penStyle = Pen.SUNLIGHT;
            } else {
                penStyle = Pen.CIRCLE;
            }
        });

        mToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == tgbCircle) {
                penStyle = Pen.CIRCLE;
            } else if (newValue == tgbSquare) {
                penStyle = Pen.SQUARE;
            } else if (newValue == tgbFilter) {
                penStyle = Pen.FIL;
            } else if (newValue == tgbSunlight) {
                penStyle = Pen.SUNLIGHT;
            } else {
                penStyle = Pen.FIL;
            }
        });

        sldSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            double temp = (Double) newValue; //automatic unboxing
            penSize = (int) Math.round(temp);
        });

        //blur menu items
        gaussianBlur.setOnAction(event -> setMyImage(imageViewer.getImage(), blurryEffect.gaussianBlurImage(), imageViewer.getBlendMode()));
        motionBlur.setOnAction(event -> setMyImage(imageViewer.getImage(), blurryEffect.motionBlurImage(), imageViewer.getBlendMode()));
        boxBlur.setOnAction(event -> setMyImage(imageViewer.getImage(), blurryEffect.boxBlurImage(), imageViewer.getBlendMode()));
        blurOffMenuItem.setOnAction(event -> setMyImage(imageViewer.getImage(), null, imageViewer.getBlendMode()));

        //color menu items
        brightSelect.setOnAction(event -> setMyImage(ColoringImage.brighterImage(imageViewer), imageViewer.getEffect(), imageViewer.getBlendMode()));
        darkSelect.setOnAction(event -> setMyImage(ColoringImage.darkerImage(imageViewer), imageViewer.getEffect(), imageViewer.getBlendMode()));
        monochromeMenuItem.setOnAction(event -> setMyImage(ColoringImage.monochromeImage(imageViewer), imageViewer.getEffect(), imageViewer.getBlendMode()));
        saturateMenuItem.setOnAction(event -> setMyImage(ColoringImage.saturateImage(imageViewer), imageViewer.getEffect(), imageViewer.getBlendMode()));
        invertMenuItem.setOnAction(event -> setMyImage(ColoringImage.invertColorImage(imageViewer), imageViewer.getEffect(), imageViewer.getBlendMode()));
        goldenMenuItem.setOnAction(event -> setMyImage(ColoringImage.goldenBlingOutImage(imageViewer), imageViewer.getEffect(), imageViewer.getBlendMode()));
        desaturateMenuItem.setOnAction(event -> setMyImage(ColoringImage.desaturateImage(imageViewer), imageViewer.getEffect(), imageViewer.getBlendMode()));
        gradientMenu.setOnAction(event -> setMyImage(ColoringImage.linearGradientImage(imageViewer), imageViewer.getEffect(), imageViewer.getBlendMode()));

        //blend mode menu items be careful using these because they keep changing the colors of additional filters. It looks at the colors you have and then
        //gets to work blending them all up.  So you'll see some unusual things happen with the other filters when you start blending.
        mnuItemDifferenceBlend.setOnAction(event -> setMyImage(imageViewer.getImage(), imageViewer.getEffect(), ImageBlendMode.differenceBlend()));
        mnuItemColorBurnBlend.setOnAction(event -> setMyImage(imageViewer.getImage(), imageViewer.getEffect(), ImageBlendMode.colorBurnBlend()));
        blendModeOff.setOnAction(event -> setMyImage(imageViewer.getImage(), imageViewer.getEffect(), null));

        //Other advanced filters and actions for image
        hueSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            hueLvl = newValue.doubleValue();
        });
        hueBtn.setOnAction(event -> setMyImage(imageViewer.getImage(), AdvancedImageFilters.setHue(hueLvl), imageViewer.getBlendMode()));

        reflectionBtn.setOnAction(event -> setMyImage(imageViewer.getImage(), AdvancedImageFilters.reflection(), imageViewer.getBlendMode()));

        dropShadow.setOnAction(event -> setMyImage(imageViewer.getImage(), AdvancedImageFilters.dropShadowImage(), imageViewer.getBlendMode()));

        innerShadowBtn.setOnAction(event -> setMyImage(imageViewer.getImage(), AdvancedImageFilters.innerShadowImage(), imageViewer.getBlendMode()));

        sepiaSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sepiaLvl = newValue.doubleValue();
        });
        sepiaBtn.setOnAction(event -> {
            sepiaEffect.setLevel(sepiaLvl);
            setMyImage(imageViewer.getImage(), sepiaEffect, imageViewer.getBlendMode());
        });
    }

        private void saveToFile(){
        FileChooser fileChooser = new FileChooser();
        File outputFile = fileChooser.showSaveDialog(null);

        try{

            ImageIO.write(SwingFXUtils.fromFXImage(imageViewer.snapshot(null, null), null), "png", outputFile);
        }
        catch (IOException e){
            e.getStackTrace();
        }
    }

    //call setimage function ... reset head of arraylist, increment imagecount, display image
    private void setMyImage(Image image, Effect effect, javafx.scene.effect.BlendMode blendMode){

        if (fullImageArrayList.size() != imageCount)
        {
            //Fixed bug where if you started undoing through arraylists and then started applying filters again, you would
            //skip one of the applied filters when resuming your undo.
            imageCount += 2;
        }
        else
        {
            imageCount += 1;
        }
        FullImage fullImage = new FullImage(effect, image, blendMode);
        fullImageArrayList.add(fullImage);
        fullImageArrayList = new ArrayList<>(fullImageArrayList.subList(0, imageCount));

        //fixed this gnarly bug where if you began rebuilding your arraylist after undoing some, it would
        //hold the wrong image in imageCount-1.  Hard to spot. should be fixed. hollaaaa
        fullImageArrayList.set(imageCount - 1, fullImage);

        imageViewer.setEffect(effect);
        imageViewer.setImage(image);
        imageViewer.setBlendMode(blendMode);
    }

    private void undo() {
        if (fullImageArrayList.size() == imageCount)
        {
            imageCount = Math.max(0, imageCount-2);
            FullImage backupImage = fullImageArrayList.get(imageCount);
            imageViewer.setImage(backupImage.getImage());
            imageViewer.setEffect(backupImage.getEffect());
            imageViewer.setBlendMode(backupImage.getBlendMode());
        }
        else
        {
            if (fullImageArrayList.size() < 1) return;
            imageCount = Math.max(0, imageCount - 1);
            FullImage backupImage = fullImageArrayList.get(imageCount);
            imageViewer.setImage(backupImage.getImage());
            imageViewer.setEffect(backupImage.getEffect());
            imageViewer.setBlendMode(backupImage.getBlendMode());
        }
    }

    private void redo()
    {
        if (fullImageArrayList.size() < 1 || imageCount >= fullImageArrayList.size()-1) return;
        imageCount = Math.max(0, imageCount + 1);
        FullImage redoImage = fullImageArrayList.get(imageCount);
        imageViewer.setImage(redoImage.getImage());
        imageViewer.setEffect(redoImage.getEffect());
        imageViewer.setBlendMode(redoImage.getBlendMode());
    }

    private Image snapShot(double width, double height){
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setViewport(new Rectangle2D(0, 0, width, height));
        Image snapShot = ancPane.snapshot(snapshotParameters, null);
        return snapShot;
    }
}

