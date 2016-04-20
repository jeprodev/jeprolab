package com.jeprolab.controllers;


import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import javafx.fxml.FXML;

//import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


import java.net.URL;
import java.util.ResourceBundle;

public class JeproLabCustomerController extends JeproLabController{
    private ResourceBundle bundle;
    private Button addCustomerBtn;

    @FXML
    public JeproFormPanel jeproLabCustomerListWrapper;
    public Button searchCustomerButton;
    public ToolBar jeproLabCustomerSearchToolBar;
    public TextField customerSearchBox;
    public TableView<JeproLabCustomerRecord> customersTableView;
    public TableColumn customerListNumberColumn, customerCheckBoxColumn, customerTitleColumn, customerNameColumn;
    public TableColumn customerFistNameColumn, customerLastVisitColumn, customerDateAddColumn, customerAllowAdsColumn;
    public TableColumn customerActiveColumn, customerCompanyColumn, customerEmailAddressColumn,customerReportColumn, customerActionsColumn;

    public void initialize(URL location , ResourceBundle resource){
        bundle = resource;
        double tableWidth = .98 * JeproLab.APP_WIDTH;
        double remainingWidth = (tableWidth - 272);

        jeproLabCustomerSearchToolBar.setPrefWidth(tableWidth);
        jeproLabCustomerSearchToolBar.setLayoutX(0.01 * JeproLab.APP_WIDTH);
        jeproLabCustomerSearchToolBar.setLayoutY(10);

        customersTableView.setPrefWidth(.98 * JeproLab.APP_WIDTH);
        customersTableView.setLayoutX(.01 * JeproLab.APP_WIDTH);
        customersTableView.setLayoutY(50);
        customerListNumberColumn.setText("#");
        customerListNumberColumn.setPrefWidth(30);
        CheckBox checkAll = new CheckBox();
        customerCheckBoxColumn.setGraphic(checkAll);
        customerCheckBoxColumn.setPrefWidth(22);
        customerTitleColumn.setText(bundle.getString("JEPROLAB_TITLE_LABEL"));
        customerTitleColumn.setPrefWidth(45);
        customerNameColumn.setText(bundle.getString("JEPROLAB_LAST_NAME_LABEL"));
        customerNameColumn.setPrefWidth(.18 * remainingWidth);
        customerFistNameColumn.setText(bundle.getString("JEPROLAB_FIRST_NAME_LABEL"));
        customerFistNameColumn.setPrefWidth(.18 * remainingWidth);
        customerLastVisitColumn.setText(bundle.getString("JEPROLAB_LAST_VISIT_LABEL"));
        customerLastVisitColumn.setPrefWidth(0.11 * remainingWidth);
        customerDateAddColumn.setText(bundle.getString("JEPROLAB_DATE_ADD_LABEL"));
        customerDateAddColumn.setPrefWidth(.15 * remainingWidth);
        customerAllowAdsColumn.setText(bundle.getString("JEPROLAB_ALLOWS_ADS_LABEL"));
        customerAllowAdsColumn.setPrefWidth(.13 * remainingWidth);
        customerActiveColumn.setText(bundle.getString("JEPROLAB_ACTIVE_LABEL"));
        customerActiveColumn.setPrefWidth(55);
        customerCompanyColumn.setText(bundle.getString("JEPROLAB_COMPANY_LABEL"));
        customerCompanyColumn.setPrefWidth(.15 * remainingWidth);
        customerEmailAddressColumn.setText(bundle.getString("JEPROLAB_EMAIL_LABEL"));
        customerEmailAddressColumn.setPrefWidth(55);
        customerReportColumn.setText(bundle.getString("JEPROLAB_REPORT_LABEL"));
        customerReportColumn.setPrefWidth(0.1 * remainingWidth);
        customerActionsColumn.setText(bundle.getString("JEPROLAB_ACTIONS_LABEL"));
        customerActionsColumn.setPrefWidth(65);

        //addNewCustomerButton = new Button();
        //addNewCustomerButton.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL"));
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        addCustomerBtn = new Button(bundle.getString("JEPROLAB_ADD_NEW_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/add.png"))));
        commandWrapper.getChildren().addAll(addCustomerBtn);
    }

    public class JeproLabCustomerRecord{

    }
}