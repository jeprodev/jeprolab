package com.jeprolab.assets.extend.controls.switchbutton;

import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;


/**
 *
 * Created by jeprodev on 19/06/2014.
 */
public class JeproSwitchButtonSkin extends LabeledSkinBase<JeproSwitchButton, JeproSwitchButtonBehavior<JeproSwitchButton>> {
    private static final double BOX_WIDTH = 90;
    private static final double BOX_HEIGHT = 26;
    private static final double THUMB_WIDTH = 30;
    private static final double THUMB_HEIGHT = 24;
    private static final double TRANS_DISTANCE = 24;
    private final StackPane box = new StackPane();
    private Region markBox;
    private Region crossBox;
    private Region  thumb;
    private Timeline selectTimeLine;
    private Timeline deSelectTimeLine;

    public JeproSwitchButtonSkin(JeproSwitchButton chkBox){
        super(chkBox, new JeproSwitchButtonBehavior<>(chkBox));
        initGraphics();
        initTimeLines();
        registerListeners();
    }

    private void initGraphics(){
        markBox = new Region();
        markBox.getStyleClass().setAll("mark");
        markBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        markBox.setTranslateX(-(TRANS_DISTANCE + 3));
        markBox.setPrefSize(THUMB_WIDTH, THUMB_HEIGHT);
        markBox.setMinSize(THUMB_WIDTH, THUMB_HEIGHT);
        markBox.setMaxSize(THUMB_WIDTH, THUMB_HEIGHT);

        crossBox = new Region();
        crossBox.getStyleClass().setAll("cross");
        crossBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        crossBox.setTranslateX(TRANS_DISTANCE);
        crossBox.setPrefSize(THUMB_WIDTH, THUMB_HEIGHT);
        crossBox.setMinSize(THUMB_WIDTH, THUMB_HEIGHT);
        crossBox.setMaxSize(THUMB_WIDTH, THUMB_HEIGHT);

        thumb = new Region();
        thumb.setPrefSize(THUMB_WIDTH -5, THUMB_HEIGHT -5);
        thumb.setMinSize(THUMB_WIDTH  -5, THUMB_HEIGHT -5);
        thumb.setMaxSize(THUMB_WIDTH -5, THUMB_HEIGHT  -5);

        if(getSkinnable().isSelected()){
            crossBox.setOpacity(0);
            thumb.setTranslateX(TRANS_DISTANCE);
        }else{
            markBox.setOpacity(0);
            thumb.setTranslateX(-TRANS_DISTANCE);
        }

        box.getStyleClass().setAll("box");
        box.getChildren().addAll(markBox, crossBox, thumb);
        updateChildren();
    }

    private void initTimeLines(){
        selectTimeLine = getSelectTimeLine();
        deSelectTimeLine = getDeselectTimeLine();
    }

    private void registerListeners(){
        getSkinnable().selectedProperty().addListener(observable -> toggle());
    }

    /*** process method ****/
    @Override
    protected void updateChildren(){
        super.updateChildren();
        if(box != null){ getChildren().add(box); }
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset){
        return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset) + snapSize(box.minWidth(-1));
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset){
        return Math.max(super.computeMinHeight(width - box.minWidth(-1), topInset, rightInset, bottomInset, leftInset), topInset + box.minHeight(-1) + bottomInset);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset){
        return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset) + snapSize(box.prefWidth(-1) + 46);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset){
        return Math.max(super.computePrefHeight(width - box.prefWidth(-1), topInset, rightInset, bottomInset, leftInset), topInset + box.prefHeight(-1) + bottomInset);
    }

    @Override
    protected void layoutChildren(final double x, final double y, final  double width, final double height){
        final JeproSwitchButton checkBox = getSkinnable();
        final double computeWidth = Math.max(checkBox.prefWidth(-1), checkBox.minWidth(-1));
        final double labelWidth = Math.min(computeWidth - BOX_WIDTH, width - snapSize(BOX_WIDTH) + 100);
        final double labelHeight = Math.min(checkBox.prefHeight(labelWidth), height);
        final double maxHeight = Math.max(BOX_HEIGHT, labelHeight);
        final double xOffset = computeXOffset(width, labelWidth + BOX_WIDTH, checkBox.getAlignment().getHpos()) + x;
        final double yOffset = computeYOffset(height, maxHeight, checkBox.getAlignment().getVpos()) + y;

        layoutLabelInArea(xOffset + BOX_WIDTH , yOffset, labelWidth, maxHeight, checkBox.getAlignment());
        thumb.resize(THUMB_WIDTH, THUMB_HEIGHT);
        box.resize(BOX_WIDTH, BOX_HEIGHT);

        positionInArea(box, xOffset, yOffset, BOX_WIDTH, maxHeight, 0, checkBox.getAlignment().getHpos(), checkBox.getAlignment().getVpos());
    }

    private void toggle(){
        if(getSkinnable().isSelected()){
            selectTimeLine.play();
        }else{
            deSelectTimeLine.play();
        }
    }

    private Timeline getSelectTimeLine(){
        final KeyValue startSelectedThumbTranslationKeyValue = new KeyValue(thumb.translateXProperty(), -TRANS_DISTANCE, Interpolator.EASE_BOTH);
        final KeyValue stopSelectedThumbTranslationKeyValue = new KeyValue(thumb.translateXProperty(), TRANS_DISTANCE, Interpolator.EASE_BOTH);
        final KeyValue startSelectedMarkBoxOpacityKeyValue = new KeyValue(markBox.opacityProperty(), 0, Interpolator.EASE_BOTH);
        final KeyValue stopSelectedMarkBoxOpacityKeyValue = new KeyValue(markBox.opacityProperty(), 1, Interpolator.EASE_BOTH);
        final KeyValue startSelectedMarkBoxScaleXKeyValue = new KeyValue(markBox.scaleXProperty(), 0, Interpolator.EASE_BOTH);
        final KeyValue stopSelectedMarkBoxScaleXKeyValue = new KeyValue(markBox.scaleXProperty(), 1, Interpolator.EASE_BOTH);
        final KeyValue startSelectedMarkBoxScaleYKeyValue = new KeyValue(markBox.scaleYProperty(), 0, Interpolator.EASE_BOTH);
        final KeyValue stopSelectedMarkBoxScaleYKeyValue = new KeyValue(markBox.scaleYProperty(), 1, Interpolator.EASE_BOTH);
        final KeyValue startSelectedMarkBoxScaleUpXKeyValue = new KeyValue(markBox.scaleXProperty(), 1, Interpolator.EASE_BOTH);
        final KeyValue stopSelectedMarkBoxScaleUpXKeyValue = new KeyValue(markBox.scaleXProperty(), 1.5, Interpolator.EASE_BOTH);
        final KeyValue startSelectedMarkBoxScaleUpYKeyValue = new KeyValue(markBox.scaleYProperty(), 1, Interpolator.EASE_BOTH);
        final KeyValue stopSelectedMarkBoxScaleUpYKeyValue = new KeyValue(markBox.scaleYProperty(), 1.5, Interpolator.EASE_BOTH);
        final KeyValue startSelectedMarkBoxScaleDownXKeyValue = new KeyValue(markBox.scaleXProperty(), 1.5, Interpolator.EASE_BOTH);
        final KeyValue stopSelectedMarkBoxScaleDownXKeyValue = new KeyValue(markBox.scaleXProperty(), 1, Interpolator.EASE_BOTH);
        final KeyValue startSelectedMarkBoxScaleDownYKeyValue = new KeyValue(markBox.scaleYProperty(), 1.5, Interpolator.EASE_BOTH);
        final KeyValue stopSelectedMarkBoxScaleDownYKeyValue = new KeyValue(markBox.scaleYProperty(), 1, Interpolator.EASE_BOTH);
        final KeyValue startSelectedCrossBoxOpacityKeyValue = new KeyValue(crossBox.opacityProperty(), 1, Interpolator.EASE_BOTH);
        final KeyValue stopSelectedCrossBoxOpacityKeyValue = new KeyValue(crossBox.opacityProperty(), 0, Interpolator.EASE_BOTH);
        final KeyValue startSelectedCrossBoxScaleXKeyValue = new KeyValue(crossBox.scaleXProperty(), 1, Interpolator.EASE_BOTH);
        final KeyValue stopSelectedCrossBoxScaleXKeyValue = new KeyValue(crossBox.scaleXProperty(), 0, Interpolator.EASE_BOTH);
        final KeyValue startSelectedCrossBoxScaleYKeyValue = new KeyValue(crossBox.scaleYProperty(), 1, Interpolator.EASE_BOTH);
        final KeyValue stopSelectedCrossBoxScaleYKeyValue = new KeyValue(crossBox.scaleYProperty(), 0, Interpolator.EASE_BOTH);


        final KeyFrame startKeyFrame = new KeyFrame(Duration.ZERO, startSelectedThumbTranslationKeyValue, startSelectedMarkBoxOpacityKeyValue, startSelectedMarkBoxScaleXKeyValue, startSelectedMarkBoxScaleYKeyValue, startSelectedCrossBoxOpacityKeyValue, startSelectedCrossBoxScaleXKeyValue, startSelectedCrossBoxScaleYKeyValue);
        final KeyFrame stopKeyFrame = new KeyFrame(Duration.millis(180), stopSelectedThumbTranslationKeyValue, stopSelectedMarkBoxOpacityKeyValue, stopSelectedMarkBoxScaleXKeyValue, stopSelectedMarkBoxScaleYKeyValue, stopSelectedCrossBoxOpacityKeyValue, stopSelectedCrossBoxScaleXKeyValue, stopSelectedCrossBoxScaleYKeyValue);
        final KeyFrame startScaleUpKeyFrame = new KeyFrame(Duration.millis(250), startSelectedMarkBoxScaleUpXKeyValue, startSelectedMarkBoxScaleUpYKeyValue);
        final KeyFrame stopScaleUpKeyFrame = new KeyFrame(Duration.millis(350), stopSelectedMarkBoxScaleUpXKeyValue, stopSelectedMarkBoxScaleUpYKeyValue);
        final KeyFrame startScaleDownKeyFrame = new KeyFrame(Duration.millis(350), startSelectedMarkBoxScaleDownXKeyValue, startSelectedMarkBoxScaleDownYKeyValue);
        final KeyFrame stopScaleDownKeyFrame = new KeyFrame(Duration.millis(500), stopSelectedMarkBoxScaleDownXKeyValue, stopSelectedMarkBoxScaleDownYKeyValue);

        final Timeline timeLine = new Timeline();
        timeLine.getKeyFrames().setAll(startKeyFrame, stopKeyFrame, startScaleUpKeyFrame, stopScaleUpKeyFrame, startScaleDownKeyFrame,stopScaleDownKeyFrame);
        return timeLine;
    }

    private Timeline getDeselectTimeLine(){
        final KeyValue startDeselectThumbTranslationKeyValue = new KeyValue(thumb.translateXProperty(), TRANS_DISTANCE);
        final KeyValue stopDeselectedThumbTranslationKeyValue = new KeyValue(thumb.translateXProperty(), -TRANS_DISTANCE);
        final KeyValue startDeselectedCrossBoxScaleXKeyValue = new KeyValue(crossBox.scaleXProperty(), 0);
        final KeyValue stopDeselectedCrossBoxScaleXKeyValue = new KeyValue(crossBox.scaleXProperty(), 1);
        final KeyValue startDeselectedCrossBoxScaleYKeyValue = new KeyValue(crossBox.scaleYProperty(), 0);
        final KeyValue stopDeselectedCrossBoxScaleYKeyValue = new KeyValue(crossBox.scaleYProperty(), 1);
        final KeyValue startDeselectedCrossBoxOpacityKeyValue = new KeyValue(crossBox.opacityProperty(), 0);
        final KeyValue stopDeselectedCrossBoxOpacityKeyValue = new KeyValue(crossBox.opacityProperty(), 1);
        final KeyValue startDeselectedCrossBoxRotationKeyValue = new KeyValue(crossBox.rotateProperty(), 0);
        final KeyValue stopDeselectedCrossBoxRotationKeyValue = new KeyValue(crossBox.rotateProperty(), 360);
        final KeyValue startDeselectedMarKBoxOpacityKeyValue = new KeyValue(markBox.opacityProperty(), 1);
        final KeyValue stopDeselectedMarkBoxOpacityKeyValue = new KeyValue(markBox.opacityProperty(), 0);
        final KeyValue startDeselectedMarkBoxScaleXKeyValue = new KeyValue(markBox.scaleXProperty(), 1);
        final KeyValue stopDeselectedMarkBoxScaleXKeyValue = new KeyValue(markBox.scaleXProperty(), 0);
        final KeyValue startDeselectedMarkBoxScaleYKeyValue = new KeyValue(markBox.scaleYProperty(), 1);
        final KeyValue stopDeselectedMarkBoxScaleYKeyValue = new KeyValue(markBox.scaleYProperty(), 0);


        final KeyFrame startKeyFrame = new KeyFrame(Duration.ZERO, startDeselectThumbTranslationKeyValue, startDeselectedMarKBoxOpacityKeyValue, startDeselectedMarkBoxScaleXKeyValue, startDeselectedMarkBoxScaleYKeyValue, startDeselectedCrossBoxOpacityKeyValue, startDeselectedCrossBoxScaleXKeyValue, startDeselectedCrossBoxScaleYKeyValue);
        final KeyFrame stopKeyFrame = new KeyFrame(Duration.millis(180), stopDeselectedThumbTranslationKeyValue, stopDeselectedMarkBoxOpacityKeyValue, stopDeselectedMarkBoxScaleXKeyValue, stopDeselectedMarkBoxScaleYKeyValue, stopDeselectedCrossBoxOpacityKeyValue, stopDeselectedCrossBoxScaleXKeyValue, stopDeselectedCrossBoxScaleYKeyValue );

        final KeyFrame startRotateKeyFrame = new KeyFrame(Duration.millis(250), startDeselectedCrossBoxRotationKeyValue);
        final KeyFrame stopRotateKeyFrame = new KeyFrame(Duration.millis(750), stopDeselectedCrossBoxRotationKeyValue);

        final Timeline timeLine = new Timeline();
        timeLine.getKeyFrames().setAll(startKeyFrame, stopKeyFrame, startRotateKeyFrame, stopRotateKeyFrame);
        timeLine.setOnFinished(event -> crossBox.setRotate(0));
        return timeLine;
    }

    private static  double computeXOffset(double width, double contentWidth, HPos hPos){
        switch(hPos){
            case LEFT :
                return 0;
            case CENTER :
                return (width - contentWidth) / 2;
            case RIGHT :
                return width - contentWidth;
            default:
                return 0;

        }
    }

    private static  double computeYOffset(double height, double contentHeight, VPos vPos){
        switch(vPos){
            case TOP :
                return 0;
            case CENTER :
                return (height - contentHeight) / 2;
            case BOTTOM :
                return height - contentHeight;
            default:
                return 0;

        }
    }
}