package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.extend.controls.JeproFormPanelContainer;
import com.jeprolab.assets.extend.controls.JeproFormPanelTitle;
import com.jeprolab.assets.extend.controls.switchbutton.JeproSwitchButton;
import com.jeprolab.models.JeproLabTaxModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabTaxGroupsAddController extends JeproLabController {
    private JeproLabTaxModel.JeproLabTaxRulesGroupModel tax_rules_group;
    @FXML
    public Label jeproLabTaxRulesGroupNameLabel, jeproLabTaxRulesGroupPublishedLabel;
    public TextField jeproLabTaxRulesGroupName;
    public JeproSwitchButton jeproLabTaxRulesGroupPublished;
    public JeproFormPanel jeproLabTaxRulesFormWrapper;
    public JeproFormPanelTitle jeproLabTaxRulesFormTitleWrapper;
    public JeproFormPanelContainer jeproLabTaxRulesFormContainerWrapper;
    public GridPane jeproLabTaxRulesGroupLayout;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        formWidth = 500;

        jeproLabTaxRulesFormWrapper.setLayoutX((JeproLab.APP_WIDTH - formWidth)/2);
        jeproLabTaxRulesFormWrapper.setLayoutY(40);

        formTitleLabel.setText(bundle.getString("JEPROLAB_ADD_NEW_LABEL") +  " " + bundle.getString("JEPROLAB_TAX_RULES_GROUP_LABEL"));
        formTitleLabel.setPrefWidth(formWidth);

        jeproLabTaxRulesFormTitleWrapper.setPrefSize(formWidth, 40);
        jeproLabTaxRulesFormTitleWrapper.getChildren().add(formTitleLabel);

        jeproLabTaxRulesFormContainerWrapper.setPrefWidth(formWidth);
        jeproLabTaxRulesFormContainerWrapper.setLayoutY(40);

        jeproLabTaxRulesGroupLayout.getColumnConstraints().addAll( new ColumnConstraints(150), new ColumnConstraints(250));

        GridPane.setMargin(jeproLabTaxRulesGroupNameLabel, new Insets(15, 0, 0, 10));
        GridPane.setMargin(jeproLabTaxRulesGroupName, new Insets(15, 0, 0, 10));
        GridPane.setMargin(jeproLabTaxRulesGroupPublishedLabel, new Insets(15, 0, 20, 10));
        GridPane.setMargin(jeproLabTaxRulesGroupPublished, new Insets(15, 0, 20, 10));

        jeproLabTaxRulesGroupNameLabel.setText(bundle.getString("JEPROLAB_NAME_LABEL"));
        jeproLabTaxRulesGroupPublishedLabel.setText(bundle.getString("JEPROLAB_PUBLISHED_LABEL"));
    }

    @Override
    public void initializeContent(){
        loadTaxGroup();
        jeproLabTaxRulesGroupName.setText(tax_rules_group.name);
        jeproLabTaxRulesGroupPublished.setSelected(tax_rules_group.published);
    }

    private void loadTaxGroup(){
        int taxRulesGroupId = JeproLab.request.getRequest().containsKey("tax_rules_group_id") ? Integer.parseInt(JeproLab.request.getRequest().get("tax_rules_group_id")) : 0;

        if(taxRulesGroupId > 0){
            if(tax_rules_group == null){
                tax_rules_group = new JeproLabTaxModel.JeproLabTaxRulesGroupModel(taxRulesGroupId);
            }
        }else{
            tax_rules_group = new JeproLabTaxModel.JeproLabTaxRulesGroupModel();
        }
    }
}
