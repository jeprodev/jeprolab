package com.jeprolab.assets.extend.controls;

import com.jeprolab.JeproLab;
import com.jeprolab.models.JeproLabCustomerModel;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproCustomerField extends Pane {
    private TextField customerField;
    private Button customerLink;
    private int customer_id = 0;

    public JeproCustomerField(){
        customerField = new TextField();
        customerLink = new Button();

        customerField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals("")){
                setCustomer(JeproLabCustomerModel.getCustomerByEmail(newValue));
            }
        });

        customerLink.setOnAction(evt -> {
            if(customer_id > 0){
                JeproLab.getInstance().goToForm(JeproLab.getInstance().getApplicationForms().addCustomerForm);
                JeproLab.getInstance().getApplicationForms().addCustomerForm.controller.initializeContent(customer_id);
            }
        });
    }

    public void setCustomer(JeproLabCustomerModel customer){
        getChildren().clear();
        if(customer != null && customer.customer_id > 0){
            customer_id = customer.customer_id;
            customerLink.setText(customer.firstname + " " + customer.lastname.toUpperCase());
            getChildren().add(customerLink);
        }else{
            customerField.setPromptText(JeproLab.getBundle().getString("JEPROLAB_ENTER_EMAIL_LABEL"));
            getChildren().add(customerField);
        }
    }
}
