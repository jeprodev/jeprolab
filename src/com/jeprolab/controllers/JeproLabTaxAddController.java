package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.JeproMultiLangTextField;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.models.JeproLabTaxModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabTaxAddController extends JeproLabController {
    private JeproLabTaxModel tax;
    private Button saveTaxButton, cancelButton;
    @FXML
    public Label jeproLabTaxNameLabel, jeproLabTaxRateLabel, jeproLabTaxRateUnitLabel, jeproLabTaxPublishedLabel;
    public JeproMultiLangTextField jeproLabTaxName;
    public TextField jeproLabTaxRate;
    public JeproSwitchButton jeproLabTaxPublished;
    public GridPane jeproLabTaxLayout;
    public JeproFormPanel jeproLabFormPanelWrapper;
    public JeproFormPanelTitle jeproLabFormPanelTitleWrapper;
    public JeproFormPanelContainer jeproLabFormPanelContainerWrapper;
    public HBox jeproLabTaxRateWrapper;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        super.initialize(location, resource);
        formWidth = 550;

        jeproLabFormPanelWrapper.setLayoutX((JeproLab.APP_WIDTH - formWidth)/2);
        jeproLabFormPanelWrapper.setLayoutY(40);

        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") +  " " + bundle.getString("JEPROLAB_TAX_LABEL"));
        formTitleLabel.setPrefSize(formWidth, 40);
        jeproLabFormPanelTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabFormPanelTitleWrapper.getChildren().add(formTitleLabel);

        jeproLabFormPanelContainerWrapper.setPrefWidth(formWidth);
        jeproLabFormPanelContainerWrapper.setLayoutY(40);

        jeproLabTaxLayout.getColumnConstraints().addAll(new ColumnConstraints(150), new ColumnConstraints(250));

        GridPane.setMargin(jeproLabTaxNameLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabTaxName, new Insets(10, 0, 5, 0));
        GridPane.setMargin(jeproLabTaxRateLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabTaxRateWrapper, new Insets(10, 0, 5, 0));
        GridPane.setMargin(jeproLabTaxPublishedLabel, new Insets(10, 0, 5, 10));
        GridPane.setMargin(jeproLabTaxPublished, new Insets(10, 0, 5, 0));

        jeproLabTaxNameLabel.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabTaxRateLabel.setText(bundle.getString("JEPROLAB_RATE_LABEL"));
        jeproLabTaxRateUnitLabel.setText("%");
        jeproLabTaxPublishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));

        HBox.setMargin(jeproLabTaxRateUnitLabel, new Insets(5, 0, 0, 5));
    }

    @Override
    public void initializeContent(){
        tax = null;
        loadTax();

        jeproLabTaxName.setText(tax.name);
        jeproLabTaxRate.setAlignment(Pos.CENTER_RIGHT);
        jeproLabTaxRate.setPrefWidth(80);
        jeproLabTaxRate.setText(String.valueOf(tax.rate));

        jeproLabTaxPublished.setSelected(tax.published);
        updateToolBar();
    }

    @Override
    public void updateToolBar(){
        HBox commandWrapper = JeproLab.getInstance().getApplicationToolBarCommandWrapper();
        commandWrapper.getChildren().clear();
        commandWrapper.setSpacing(4);
        saveTaxButton = new Button("", new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/floppy-icon.png"))));
        if (tax.tax_id > 0) {
            saveTaxButton.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));
        } else {
            saveTaxButton.setText(bundle.getString("JEPROLAB_SAVE_LABEL"));
        }
        cancelButton = new Button(bundle.getString("JEPROLAB_CANCEL_LABEL"), new ImageView(new Image(JeproLab.class.getResourceAsStream("resources/images/unpublished.png"))));

        commandWrapper.getChildren().addAll(saveTaxButton, cancelButton);
    }

    private void loadTax(){
        int taxId = JeproLab.request.getRequest().containsKey("tax_id") ? Integer.parseInt(JeproLab.request.getRequest().get("tax_id")) : 0;

        if(taxId > 0){
            if(tax == null){
                tax = new JeproLabTaxModel(taxId);
            }
        }else{
            tax = new JeproLabTaxModel();
        }
    }
}
