package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabGroupController extends JeproLabController{
    private Button addGroupBtn;
    private CheckBox checkAll;

    @FXML
    public TableView jeproLabGroupTableView;
    public TableColumn jeproLabGroupIndexColumn;
    public TableColumn jeproLabGroupCheckBoxColumn;
    public TableColumn jeproLabGroupNameColumn;
    public TableColumn jeproLabGroupReductionColumn;
    public TableColumn jeproLabGroupMembersColumn;
    public TableColumn jeproLabGroupDisplayPricesColumn;
    public TableColumn jeproLabGroupCreatedDateColumn;
    public TableColumn jeproLabGroupActionColumn;


    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        double remainingWidth = (0.98 * JeproLab.APP_WIDTH) - 125;
        checkAll = new CheckBox();

        jeproLabGroupTableView.setPrefWidth(0.98 * JeproLab.APP_WIDTH);
        jeproLabGroupTableView.setLayoutX(0.01 *JeproLab.APP_WIDTH);
        jeproLabGroupTableView.setLayoutY(20);

        jeproLabGroupIndexColumn.setPrefWidth(30);
        jeproLabGroupIndexColumn.setText("#");
        jeproLabGroupCheckBoxColumn.setPrefWidth(25);
        jeproLabGroupCheckBoxColumn.setGraphic(checkAll);
        jeproLabGroupNameColumn.setPrefWidth(0.4 * remainingWidth);
        jeproLabGroupNameColumn.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabGroupReductionColumn.setPrefWidth(0.15 * remainingWidth);
        jeproLabGroupReductionColumn.setText(bundle.getString("JEPROLAB_REDUCTION_LABEL"));
        jeproLabGroupMembersColumn.setPrefWidth(0.15 * remainingWidth);
        jeproLabGroupMembersColumn.setText(bundle.getString("JEPROLAB_MEMBERS_LABEL"));
        jeproLabGroupDisplayPricesColumn.setPrefWidth(0.15 * remainingWidth);
        jeproLabGroupDisplayPricesColumn.setText(bundle.getString("JEPROLAB_DISPLAY_PRICES_LABEL"));
        jeproLabGroupCreatedDateColumn.setPrefWidth(0.15 * remainingWidth);
        jeproLabGroupCreatedDateColumn.setText(bundle.getString("JEPROLAB_CREATED_DATE_LABEL"));
        jeproLabGroupActionColumn.setPrefWidth(70);
        jeproLabGroupActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));

        initializeContent();
    }

    @Override
    protected void initializeContent(){
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addGroupBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addGroupBtn);
    }
}