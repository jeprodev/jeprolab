package com.jeprolab.assets.extend.controls;


import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class JeproFieldSet extends StackPane {
    private Label legend;
    private Node legendNode;
    private  StackPane contentBox, legendBox;

    public JeproFieldSet(String legendStr){
        super();
        legend = new Label(legendStr);
        legendBox = new StackPane();
        legendBox.getChildren().add(legend);
        configureFieldSet();
    }

    public JeproFieldSet(Node legendNode){
        super();
        this.legendNode = legendNode;
        legendBox = new StackPane();
        legendBox.getChildren().add(legendNode);
        configureFieldSet();
    }

    private void configureFieldSet(){
        super.setPadding(new Insets(10, 0, 0, 0));
        super.setAlignment(Pos.TOP_LEFT);
        super.getStyleClass().add("default-fieldset");

        contentBox = new StackPane();
        contentBox.getStyleClass().add("fieldset");
        contentBox.setAlignment(Pos.TOP_LEFT);

        legendBox.setPadding(new Insets(0, 5, 0, 5));

        Group grp = new Group();
        grp.setTranslateX(20);
        grp.setTranslateY(-8);
        grp.getChildren().add(legendBox);

        super.getChildren().addAll(contentBox, grp);
        setBackGroundColor("#FFFFFF");

        /** Adding listeners for styles. **/
        getStyleClass().addListener((ListChangeListener.Change<? extends String> paramChange) -> {
            System.out.println();
        });
    }

    public void setContent(Node content){
        contentBox.getChildren().add(content);
    }

    public void setBackGroundColor(String color){
        super.setStyle("-fx-backgroud-color:" + color + "; ");
        contentBox.setStyle("-fx-backgroud-color:" + color + "; ");
        legendBox.setStyle("-fx-backgroud-color:" + color + "; ");
    }

    public void setStyleClassForBorder(String claz){
        contentBox.getStyleClass().add(claz);
    }

    public void removeStyleClassForBorder(String claz){
        contentBox.getStyleClass().remove(claz);
    }
}
