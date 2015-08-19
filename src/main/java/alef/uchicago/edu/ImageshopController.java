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
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.ScrollPane;
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
import javafx.scene.control.Menu;
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
        SAT, DRK,  BRIGHT, INVERT, MONOCHROME, GOLD, DESATURATE, WHITEOUT, BLACKOUT, OTHER
    }

    private int penSize = 50;
    private Pen penStyle = Pen.CIRCLE;
    private FilterStyle mFilterStyle = FilterStyle.DRK;
    private javafx.scene.paint.Color mColor;

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
    private MenuItem blndModeMultiply;

    @FXML
    private MenuItem reflectionBtn;

    @FXML
    private MenuItem innerShadowBtn;

    @FXML
    private ToggleButton tgbSquare;

    @FXML
    private ToggleButton tgbCircle;

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
    private ArrayList<FullImage> fullImageArrayList = new ArrayList<>();
    private ArrayList<BlendMode> imageViewBlendMode = new ArrayList<>();
    private ArrayList<Rotate> imageViewRotate = new ArrayList<>();
    private ArrayList<javafx.scene.shape.Shape> removeShapes = new ArrayList<>(1000);

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

                fullImageArrayList = new ArrayList<>();
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

                fullImageArrayList.add(new FullImage(null, image));
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

        saveAsOption.setOnAction(e -> saveToFile());

//        opacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            opacityLvl = newValue.doubleValue();
//        });
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

                    xPosForMouseEvent = (int) event.getX();
                    yPosForMouseEvent = (int) event.getY();

                    System.out.println("printing at " + xPosForMouseEvent + " " + yPosForMouseEvent);
                }
                event.consume();
            }
        });


        ancPane.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_DRAGGED, new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (penStyle == Pen.FIL){
                    event.consume();
                    return;
                }
                xPosForMouseEvent = (int) event.getX();
                yPosForMouseEvent = (int) event.getY();

                int nShape = 0;
                //default value
                javafx.scene.shape.Shape shape = new Circle(xPosForMouseEvent, yPosForMouseEvent, 10);
                switch (penStyle) {
                    case CIRCLE:
                        shape = new Circle(xPosForMouseEvent, yPosForMouseEvent, penSize);
                        break;
                    case SQUARE:
                        shape = new javafx.scene.shape.Rectangle(xPosForMouseEvent, yPosForMouseEvent, penSize, penSize);
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
            }
        });


        ancPane.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_RELEASED, new EventHandler<javafx.scene.input.MouseEvent>() {
             @Override
             public void handle(javafx.scene.input.MouseEvent event) {
                 System.out.println("Mouse released at: " + event.getX() + " " + event.getY());
                 wPosForMouseEvent = (int) event.getX();
                 hPosForMouseEvent = (int) event.getY();
                 if (penStyle == Pen.CIRCLE || penStyle == Pen.SQUARE){

                     Image snapshot = snapShot(ancPane.getPrefWidth(), ancPane.getPrefHeight());

                     setMyImage(snapshot, imageViewer.getEffect());
                     ancPane.getChildren().removeAll(removeShapes);
                     removeShapes.clear();
                 }

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
                 setMyImage(transformImage, imageViewer.getEffect());
                 event.consume();
             }
         });

        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mColor = colorPicker.getValue();
            }
        });

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
            }
        });

        tgbCircle.setToggleGroup(mToggleGroup);
        tgbSquare.setToggleGroup(mToggleGroup);
        tgbFilter.setToggleGroup(mToggleGroup);
        tgbFilter.setSelected(true);

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
                    penStyle = Pen.FIL;
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

        //blur menu items
        gaussianBlur.setOnAction(event -> setMyImage(imageViewer.getImage(), blurryEffect.gaussianBlurImage()));
        motionBlur.setOnAction(event -> setMyImage(imageViewer.getImage(), blurryEffect.motionBlurImage()));
        boxBlur.setOnAction(event -> setMyImage(imageViewer.getImage(), blurryEffect.boxBlurImage()));
        blurOffMenuItem.setOnAction(event -> setMyImage(imageViewer.getImage(), null));

        //color menu items
        brightSelect.setOnAction(event -> setMyImage(ColoringImage.brighterImage(imageViewer), imageViewer.getEffect()));
        darkSelect.setOnAction(event -> setMyImage(ColoringImage.darkerImage(imageViewer), imageViewer.getEffect()));
        monochromeMenuItem.setOnAction(event -> setMyImage(ColoringImage.monochromeImage(imageViewer), imageViewer.getEffect()));
        saturateMenuItem.setOnAction(event -> setMyImage(ColoringImage.saturateImage(imageViewer), imageViewer.getEffect()));
        invertMenuItem.setOnAction(event -> setMyImage(ColoringImage.invertColorImage(imageViewer), imageViewer.getEffect()));
        goldenMenuItem.setOnAction(event -> setMyImage(ColoringImage.goldenBlingOutImage(imageViewer), imageViewer.getEffect()));
        desaturateMenuItem.setOnAction(event -> setMyImage(ColoringImage.desaturateImage(imageViewer), imageViewer.getEffect()));
        gradientMenu.setOnAction(event -> setMyImage(ColoringImage.linearGradientImage(imageViewer), imageViewer.getEffect()));

        //Other advanced filters and actions for image
        hueSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            hueLvl = newValue.doubleValue();
        });
        hueBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setMyImage(imageViewer.getImage(), AdvancedImageFilters.setHue(hueLvl));
            }
        });

        reflectionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setMyImage(imageViewer.getImage(), AdvancedImageFilters.reflection());
            }
        });

        scalingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            scalingLvl = newValue.doubleValue();
        });

        scalingBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                AdvancedImageFilters.resizeImage(ancPane, imageViewer, scalingLvl);
            }
        });

        dropShadow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setMyImage(imageViewer.getImage(), AdvancedImageFilters.dropShadowImage());
            }
        });

        innerShadowBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setMyImage(imageViewer.getImage(), AdvancedImageFilters.innerShadowImage());
            }
        });

        sepiaSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sepiaLvl = newValue.doubleValue();
        });
        sepiaBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sepiaEffect.setLevel(sepiaLvl);
                setMyImage(imageViewer.getImage(), sepiaEffect);
            }
        });

//        blndModeMultiply.setOnAction(event -> {blendModeMultiply(imageViewer);
//            imageViewArrayList.add(snapShot(imageViewer.getImage().getWidth(), imageViewer.getImage().getHeight()));
//            imageCount += 1;});
//
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

//    private void blendModeMultiply(ImageView imageView) {
//        imageView.setBlendMode(BlendMode.MULTIPLY);
//
//    }

    //call setimage function ... reset head of arraylist, increment imagecount, display image
    private void setMyImage(Image image, Effect effect){

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
        FullImage fullImage = new FullImage(effect, image);
        fullImageArrayList.add(fullImage);
        fullImageArrayList = new ArrayList<>(fullImageArrayList.subList(0, imageCount));

        //fixed this gnarly bug where if you began rebuilding your arraylist after undoing some, it would
        //hold the wrong image in imageCount-1.  Hard to spot. should be fixed. hollaaaa
        fullImageArrayList.set(imageCount - 1, fullImage);

        imageViewer.setEffect(effect);
        imageViewer.setImage(image);
    }

    private void undo() {
        if (fullImageArrayList.size() == imageCount)
        {
            imageCount = Math.max(0, imageCount-2);
            FullImage backupImage = fullImageArrayList.get(imageCount);
            imageViewer.setImage(backupImage.getImage());
            imageViewer.setEffect(backupImage.getEffect());
        }
        else
        {
            if (fullImageArrayList.size() < 1) return;
            imageCount = Math.max(0, imageCount - 1);
            FullImage backupImage = fullImageArrayList.get(imageCount);
            imageViewer.setImage(backupImage.getImage());
            imageViewer.setEffect(backupImage.getEffect());
        }
    }

    private void redo()
    {
        if (fullImageArrayList.size() < 1 || imageCount >= fullImageArrayList.size()-1) return;
        imageCount = Math.max(0, imageCount + 1);
        FullImage redoImage = fullImageArrayList.get(imageCount);
        imageViewer.setImage(redoImage.getImage());
        imageViewer.setEffect(redoImage.getEffect());
    }

    private Image snapShot(double width, double height){
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setViewport(new Rectangle2D(0, 0, width, height));
        Image snapShot = ancPane.snapshot(snapshotParameters, null);
        return snapShot;
    }
}

