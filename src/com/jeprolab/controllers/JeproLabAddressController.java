package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.tools.JeproLabContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabAddressController extends JeproLabController{
    private ObservableList<JeproLabAddressRecord> addressList;
    private CheckBox selectAll;
    private Button addAddressBtn;

    @FXML
    public Button jeproLabAddAddressButton, jeproLabDeleteAddressesButton;
    public TableView<JeproLabAddressRecord> jeproLabAddressesList;
    public TableColumn<JeproLabAddressRecord, String> addressIndexColumn;
    public TableColumn<JeproLabAddressRecord, Boolean> addressCheckBoxColumn;
    public TableColumn<JeproLabAddressRecord, String> addressLastNameColumn;
    public TableColumn<JeproLabAddressRecord, HBox> addressActionColumn, addressCountryColumn;
    public TableColumn<JeproLabAddressRecord, String> addressFirstNameColumn;
    public TableColumn<JeproLabAddressRecord, String> addressDetailColumn, addressZipCodeColumn, addressCityColumn;
    public JeproFormPanel jeproLabAddressContainer;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);

        double padding = 0.01 * JeproLab.APP_WIDTH;
        double layoutWidth = (0.98 * JeproLab.APP_WIDTH) - 57;
        selectAll = new CheckBox();

        jeproLabAddressContainer.setPrefSize(JeproLab.APP_WIDTH * 0.98, JeproLab.APP_HEIGHT - 160);
        jeproLabAddressesList.setPrefSize(JeproLab.APP_WIDTH * 0.98, JeproLab.APP_HEIGHT - 160);
        jeproLabAddressesList.setLayoutY(10);
        jeproLabAddressesList.setLayoutX(padding);

        addressIndexColumn.setText("#");
        addressIndexColumn.setPrefWidth(35);

        addressCheckBoxColumn.setPrefWidth(22);
        addressCheckBoxColumn.setGraphic(selectAll);

        addressLastNameColumn.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        addressLastNameColumn.setPrefWidth(0.14 * layoutWidth);
        tableCellAlign(addressLastNameColumn, Pos.CENTER_LEFT);
        addressActionColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        addressActionColumn.setPrefWidth(0.08 * layoutWidth);
        addressCountryColumn.setText(bundle.getString("JEPROLAB_COUNTRY_LABEL"));
        addressCountryColumn.setPrefWidth(0.1 * layoutWidth);
        tableCellAlign(addressCountryColumn, Pos.CENTER);
        addressFirstNameColumn.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        addressFirstNameColumn.setPrefWidth(0.13 * layoutWidth);
        tableCellAlign(addressFirstNameColumn, Pos.CENTER_LEFT);
        addressDetailColumn.setText(bundle.getString("JEPROLAB_ADDRESS_LABEL"));
        addressDetailColumn.setPrefWidth(0.32 * layoutWidth);
        tableCellAlign(addressDetailColumn, Pos.CENTER_LEFT);
        addressZipCodeColumn.setText(bundle.getString("JEPROLAB_ZIP_CODE_LABEL"));
        addressZipCodeColumn.setPrefWidth(0.10 * layoutWidth);
        tableCellAlign(addressZipCodeColumn, Pos.CENTER);
        addressCityColumn.setText(bundle.getString("JEPROLAB_CITY_LABEL"));
        addressCityColumn.setPrefWidth(0.13 * layoutWidth);
        tableCellAlign(addressCityColumn, Pos.CENTER);
        //updateToolBar();
    }

    @Override
    public void initializeContent(){
        addressList = FXCollections.observableArrayList();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addAddressBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addAddressBtn);
    }

    private static class JeproLabAddressRecord {

    }
}