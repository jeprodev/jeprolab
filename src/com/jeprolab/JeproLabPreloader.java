package com.jeprolab;

//import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.log4j.Level;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabPreloader extends Preloader implements ChangeListener<Number>{
    private Stage stage;
    private Scene scene;

    private List<Animation> animations = new ArrayList<>();

    private static final double SPLASH_WIDTH = 520;
    private static final double SPLASH_HEIGHT = 294;

    private SVGPath path1, path2, path3, path4, path5;
    private Circle particle1, particle2, particle3, particle4, particle5, neonLoad;
    private Rectangle progress;
    private  DropShadow glow, shadow;

    private Group paths;
    private LoaderExplode explode;
    private String loadingMessage = "Loading...";  //JeproLab.getBundle().getString("JEPROLAB_LOADING_LABEL");
    private Label message;
    private BoxBlur boxBlur;

    @Override
    public void start(Stage loaderStage) throws Exception{
        stage = loaderStage;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        animate();
        explodeAnimation();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChange){
        if(stateChange.getType() == StateChangeNotification.Type.BEFORE_START){
            stage.hide();
        }
    }

    @Override
    public void handleProgressNotification(ProgressNotification progressNotification){
        //update(progressNotification.getProgress());
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification arg0) {
        if (arg0 instanceof ProgressNotification) {
            ProgressNotification pn= (ProgressNotification) arg0;
            update(pn.getProgress());
        }
    }

    /**
     * All Animation is started
     */
    private void animate(){
        Timeline mainAnimation = new Timeline();
        mainAnimation.getKeyFrames().addAll(
            new KeyFrame(Duration.millis(0), new KeyValue(glow.radiusProperty(), 1d)),
            new KeyFrame(Duration.millis(5000), new KeyValue(glow.radiusProperty(), 13d)),
            new KeyFrame(Duration.millis(10000), new KeyValue(glow.radiusProperty(), 0d)),

            new KeyFrame(Duration.millis(1000), new KeyValue(shadow.radiusProperty(), 5d)),
            new KeyFrame(Duration.millis(1000), new KeyValue(boxBlur.iterationsProperty(), 10d)),

            new KeyFrame(Duration.millis(0), new KeyValue(paths.opacityProperty(), 0.6d)),
            new KeyFrame(Duration.millis(5000), new KeyValue(paths.opacityProperty(), 1d)),
            new KeyFrame(Duration.millis(10000), new KeyValue(paths.opacityProperty(), 0.6d))
        );
        mainAnimation.setCycleCount(-1);
        animations.add(mainAnimation);
        mainAnimation.play();

        animateParticle(particle1, path1);
        animateParticle(particle2, path2);
        animateParticle(particle3, path3);
        animateParticle(particle4, path4);
        animateParticle(particle5, path5);
    }

    /**
     * build all the main component of this animation
     */
    public void init() throws Exception{
        ResourceBundle bundle = ResourceBundle.getBundle("com.jeprolab.resources.i18n.messages");
        initEffects();
        makePaths();
        makeParticle();

        paths = new Group();
        paths.getChildren().addAll(path1, path2, path3, path4, path5);
        paths.setOpacity(0.6);

        progress = new Rectangle();
        progress.setHeight(2);
        progress.setLayoutX(25);
        progress.setLayoutY(SPLASH_HEIGHT - 75);
        progress.setStroke(Color.YELLOWGREEN);
        progress.setStrokeWidth(2);
        progress.setWidth(0);

        message = new Label(loadingMessage);
        message.setPrefSize(SPLASH_WIDTH, 20);
        message.setAlignment(Pos.CENTER);
        message.setLayoutX(0);
        message.setLayoutY(SPLASH_HEIGHT - 55);
        message.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        message.setTextFill(Color.WHEAT);
        message.setEffect(glow);

        HBox applicationNameWrapper = new HBox();
        applicationNameWrapper.setPrefSize(SPLASH_WIDTH - 50, 20);
        applicationNameWrapper.setLayoutX(35);
        applicationNameWrapper.setLayoutY(100);

        //JeproLabConfig.initialize();
        Label versionLabel = new Label(bundle.getString("JEPROLAB_VER_LABEL").toLowerCase() + ". 1.0"); //
        // + JeproLabConfig.INSTALLED_APP_VERSION);
        Label applicationName = new Label(bundle.getString("JEPROLAB_SITE_MANAGER_TITLE"));
        applicationName.setFont(Font.font("myriad regular", FontWeight.BOLD, 40));
        applicationName.setTextFill(Color.WHITE);
        applicationNameWrapper.getChildren().addAll(applicationName, versionLabel);

        Group particles = new Group();
        particles.getChildren().addAll(particle1, particle2, particle3, particle4, particle5);

        explode = new LoaderExplode();

        Group explodes = new Group(); //new Group(neonLoad);
        ImageView microscope = new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/microscope_loader.png")));
        microscope.setLayoutX(30);
        microscope.setLayoutY(20);
        Group group = new Group();
        ImageView background = new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/splash-screen.jpg")));
        group.getChildren().addAll(background, microscope, applicationNameWrapper, paths, particles, message, explode, explodes, progress);
        //explodes.setClip(progress);

        scene = new Scene(group, SPLASH_WIDTH, SPLASH_HEIGHT);
    }

    /**
     * Initialize the effects which is to be used in the controls and graphics
     */
    private void initEffects(){
        boxBlur = new BoxBlur();
        boxBlur.setWidth(10);
        boxBlur.setHeight(3);

        glow = new DropShadow();
        glow.setColor(Color.YELLOW);
        glow.setOffsetX(0);
        glow.setOffsetY(0);
        glow.setRadius(1);

        shadow = new DropShadow();
        shadow.setOffsetX(4);
        shadow.setOffsetY(4);
        shadow.setRadius(1);
    }

    /**
     * Make the particles which moves the path defined
     */
    private void makeParticle(){
        particle1 = new Circle(0, 0, 2, Color.YELLOW);
        particle1.setEffect(glow);
        particle2 = new Circle(0, 0, 2, Color.RED);
        particle2.setEffect(glow);
        particle3 = new Circle(0, 0, 2, Color.ORANGE);
        particle3.setEffect(glow);
        particle4 = new Circle(0, 0, 2, Color.WHEAT);
        particle4.setEffect(glow);
        particle5 = new Circle(0, 0, 2, Color.AQUA);
        particle5.setEffect(glow);

        neonLoad = new Circle(25, SPLASH_HEIGHT - 75, 2, Color.AQUA);
        neonLoad.setEffect(glow);
    }

    /**
     * When the loading finished
     */
    private void dispose(){
        neonLoad.layoutXProperty().removeListener(this);
        animations.forEach(Animation::stop);
    }

    /**
     * When neon loading's loaded then the explode of neon is done from this function
     */
    private void explodeAnimation(){
        neonLoad.layoutXProperty().addListener(this);
        neonLoad.setRadius(4);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
            new KeyFrame(Duration.millis(0), new KeyValue(neonLoad.layoutXProperty(), 15)),
            new KeyFrame(Duration.millis(800), new KeyValue(neonLoad.layoutXProperty(), (SPLASH_WIDTH - 50)))
        );
        timeline.setCycleCount(-1);
        timeline.play();
        animations.add(timeline);
    }

    /**
     * This is the main method witch is triggered from class and this makes the gui info the loading progress
     */
    private void update(double percent){
        if(percent > 1){
            dispose();
        }else{
            Platform.runLater(
                () -> {
                    message.setText(loadingMessage + Math.round(percent * 100) + "%");
                    Timeline animation = new Timeline();
                    animation.getKeyFrames().addAll(
                        new KeyFrame(Duration.millis(600),
                            new KeyValue(progress.widthProperty(), (SPLASH_WIDTH - 50) * percent))
                    );
                    animation.play();
                }
            );
        }
    }

    private void animateParticle(Node particle, Shape path){
        PathTransition transition = new PathTransition();
        transition.setPath(path);
        transition.setNode(particle);
        transition.setDuration(Duration.seconds(getRandom()));
        transition.setCycleCount(-1);
        animations.add(transition);
        transition.play();
    }

    private double getRandom(){
        double rand = Math.random() * 15;
        return rand > 5 ? rand : getRandom();
    }

    /**
     *
     */
    public void makePaths(){
        try{
            PathLoader loader = new PathLoader();
            path1 = new SVGPath();
            path2 = new SVGPath();
            path3 = new SVGPath();
            path4 = new SVGPath();
            path5 = new SVGPath();

            path1.setContent(loader.getPath("1"));
            path2.setContent(loader.getPath("2"));
            path3.setContent(loader.getPath("3"));
            path4.setContent(loader.getPath("4"));
            path5.setContent(loader.getPath("5"));

            path1.setStroke(Color.YELLOW);
            path1.setFill(Color.TRANSPARENT);
            path1.setEffect(boxBlur);

            path2.setStroke(Color.RED);
            path2.setFill(Color.TRANSPARENT);
            path2.setEffect(boxBlur);

            path3.setStroke(Color.ORANGE);
            path3.setFill(Color.TRANSPARENT);
            path3.setEffect(boxBlur);

            path4.setStroke(Color.WHEAT);
            path4.setFill(Color.TRANSPARENT);
            path4.setEffect(boxBlur);

            path5.setStroke(Color.AQUA);
            path5.setFill(Color.TRANSPARENT);
            path5.setEffect(boxBlur);
        }catch (IOException ignored){

        }
    }

    private static class PathLoader {
        String getPath(String fileName) throws IOException{
            StringBuilder builder = new StringBuilder();
            InputStream inputStream = this.getClass().getResourceAsStream("resources/paths/path" + fileName);
            int read = inputStream.read();
            while (read != -1){
                builder.append((char)read);
                read = inputStream.read();
            }
            return builder.toString();
        }
    }

    private static class LoaderExplode extends Group {
        Circle circle1, circle2, circle3, circle4, circle5, circle6;
        SimpleDoubleProperty opacity = new SimpleDoubleProperty(1);

        LoaderExplode(){ init(); }

        void init(){
            Color color = Color.AQUA;
            circle1 = new Circle(0, 20, 1, color);
            circle2 = new Circle(0, 20, 1, color);
            circle3 = new Circle(0, 20, 1, color);
            circle4 = new Circle(0, 20, 1, color);
            circle5 = new Circle(0, 20, 1, color);
            circle6 = new Circle(0, 20, 1, color);

            circle1.opacityProperty().bind(opacity);
            circle2.opacityProperty().bind(opacity);
            circle3.opacityProperty().bind(opacity);
            circle4.opacityProperty().bind(opacity);
            circle5.opacityProperty().bind(opacity);
            circle6.opacityProperty().bind(opacity);

            this.getChildren().addAll(circle1, circle2, circle3, circle4, circle5, circle6);
        }

        void explode(){
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), new KeyValue(opacity, 1d)),
                new KeyFrame(Duration.millis(0), event -> {
                    for (Node node : getChildren()) {
                        node.setLayoutX(0);
                        node.setLayoutY(0);
                    }
                }),
                new KeyFrame(Duration.millis(100), new KeyValue(circle1.layoutXProperty(), 30d)),
                new KeyFrame(Duration.millis(100), new KeyValue(circle2.layoutXProperty(), 30d)),
                new KeyFrame(Duration.millis(100), new KeyValue(circle3.layoutXProperty(), 30d)),
                new KeyFrame(Duration.millis(100), new KeyValue(circle4.layoutXProperty(), 30d)),
                new KeyFrame(Duration.millis(100), new KeyValue(circle5.layoutXProperty(), 30d)),
                new KeyFrame(Duration.millis(100), new KeyValue(circle6.layoutXProperty(), 30d)),

                new KeyFrame(Duration.millis(600), new KeyValue(circle1.layoutXProperty(), 3d)),
                new KeyFrame(Duration.millis(600), new KeyValue(circle2.layoutXProperty(), 15d)),
                new KeyFrame(Duration.millis(600), new KeyValue(circle3.layoutXProperty(), 9d)),
                new KeyFrame(Duration.millis(600), new KeyValue(circle4.layoutXProperty(), 19d)),
                new KeyFrame(Duration.millis(600), new KeyValue(circle5.layoutXProperty(), 6d)),
                new KeyFrame(Duration.millis(600), new KeyValue(circle6.layoutXProperty(), 11d)),

                new KeyFrame(Duration.millis(600), new KeyValue(circle1.layoutYProperty(), 0d)),
                new KeyFrame(Duration.millis(600), new KeyValue(circle2.layoutYProperty(), 5d)),
                new KeyFrame(Duration.millis(600), new KeyValue(circle3.layoutYProperty(), 13d)),
                new KeyFrame(Duration.millis(600), new KeyValue(circle4.layoutYProperty(), 9d)),
                new KeyFrame(Duration.millis(600), new KeyValue(circle5.layoutYProperty(), 26d)),
                new KeyFrame(Duration.millis(600), new KeyValue(circle6.layoutYProperty(), 30d)),

                new KeyFrame(Duration.millis(600), new KeyValue(opacity, 0d)),
                new KeyFrame(Duration.millis(600), event -> {
                    for (Node node : getChildren()) {
                        node.setLayoutX(0);
                        node.setLayoutY(0);
                    }
                })
            );
            timeline.play();
        }
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldVal, Number newVal){
        int x = (int)(Math.floor(progress.getWidth()));
        int curr = (int)(Math.floor(newVal.doubleValue()));
        if( curr >= x-5 && curr <= x+5){
            explode.setLayoutY(progress.getLayoutY()-40);
            explode.setLayoutX(newVal.doubleValue());
            explode.explode();
        }
    }


    /**
     * ====================
     * TESTING PURPOSE ONLY
     * ====================
     * @param stage
     * @throws Exception
     * /
    @Override
    public void start(Stage stage) throws Exception {

        buildComponents();
        stage.setScene(scene);

        stage.show();
        animate();
        explodeAnimation();
        Task t = new Task(){

            @Override
            protected Object call() throws Exception {
                double i = 0.0;
                while(i < 1){
                    try {
                        Thread.sleep((long)(Math.random()*5000));
                        i+= 0.1;
                        update(i);
                        System.out.println(i);
                    } catch (InterruptedException ex) {
                        JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ex);
                        //Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                return null;
            }

        };
        Thread th = new Thread(t);
        th.start();


    }

    public static void main(String[] args){ launch(args); } */
}
