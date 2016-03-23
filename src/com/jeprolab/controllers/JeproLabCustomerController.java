package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import javafx.fxml.FXML;

//import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabCustomerController extends JeproLabController{
    private ResourceBundle bundle;
    private Button addCustomerBtn;

    @FXML
    public JeproFormPanel jeproLabCustomerListWrapper;
    public Button addNewCustomerButton;
    public TextField customerSearchBox;
    public TableView customersTableView;
    public TableColumn customerListNumberColumn, customerCheckBoxColumn, customerTitleColumn, customerNameColumn;
    public TableColumn customerFistNameColumn, customerLastVisitColumn, customerDateAddColumn, customerAllowAdsColumn;
    public TableColumn customerActiveColumn, customerCompanyColumn, customerEmailAddressColumn,customerReportColumn, customerActionsColumn;

    public void initialize(URL location , ResourceBundle resource){
        bundle = resource;
        double tableWidth = .98 * JeproLab.APP_WIDTH;
        customersTableView.setPrefWidth(.98 * JeproLab.APP_WIDTH);
        customersTableView.setLayoutX(.01 * JeproLab.APP_WIDTH);
        customersTableView.setLayoutY(10);
        customerListNumberColumn.setText("#");
        customerListNumberColumn.setPrefWidth(30);
        customerCheckBoxColumn.setText("");
        customerCheckBoxColumn.setPrefWidth(25);
        customerTitleColumn.setText(bundle.getString("JEPROLAB_TITLE_LABEL"));
        customerTitleColumn.setPrefWidth(45);
        customerNameColumn.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        customerNameColumn.setPrefWidth(.1 * (tableWidth - 100));
        customerFistNameColumn.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        customerFistNameColumn.setPrefWidth(.1*(tableWidth - 100));
        customerLastVisitColumn.setText(bundle.getString("JEPROLAB_LAST_VISIT_LABEL"));

        customerDateAddColumn.setText(bundle.getString("JEPROLAB_DATE_ADD_LABEL"));
        customerAllowAdsColumn.setText(bundle.getString("JEPROLAB_ALLOWS_ADS_LABEL"));
        customerActiveColumn.setText(bundle.getString("JEPROLAB_ACTIVE_LABEL"));
        customerCompanyColumn.setText(bundle.getString("JEPROLAB_COMPANY_LABEL"));
        customerEmailAddressColumn.setText(bundle.getString("JEPROLAB_EMAIL_LABEL"));
        customerReportColumn.setText(bundle.getString("JEPROLAB_REPORT_LABEL"));
        customerActionsColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));

        addNewCustomerButton = new Button();
        addNewCustomerButton.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL"));
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addCustomerBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addCustomerBtn);
    }
}