package com.jeprolab.views;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.tools.JeproLabContext;
import com.jeprolab.assets.tools.events.JeproLabEvent;
import com.jeprolab.assets.tools.events.JeproLabEventProcessingException;
import com.jeprolab.assets.tools.events.JeproLabEventProducer;
import com.jeprolab.assets.tools.events.JeproLabEventType;
import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import com.jeprolab.controllers.JeproLabController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.apache.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabApplicationForms implements JeproLabEventProducer{
    public Worker<JeproLabApplicationForm> worker;
    public JeproLabApplicationForm loginForm, dashBoardForm, categoryForm;
    public JeproLabApplicationForm addCategoryForm, aboutJeproLabForm, updaterForm;
    public JeproLabApplicationForm feedsForm, addFeedForm, feedBackForm, viewFeedBackForm;
    public JeproLabApplicationForm customersForm, addCustomerForm;
    public JeproLabApplicationForm laboratoriesForm, addLaboratoryForm, laboratoriesGroupsForm, addLaboratoriesGroupForm;
    public JeproLabApplicationForm analyzeForm,addAnalyzeForm, analyzeMethodForm, addAnalyzeMethodForm;
    public JeproLabApplicationForm attachmentForm, addAttachmentForm;
    public JeproLabApplicationForm addressesForm, addAddressForm;
    public JeproLabApplicationForm groupsForm, addGroupForm, requestForm;
    public JeproLabApplicationForm customerThreadsForm, contactsForm;
    public JeproLabApplicationForm addTaxGroupForm, addTaxForm, taxesForm, taxGroupForm;
    public JeproLabApplicationForm currencyForm, addCurrencyForm;
    public JeproLabApplicationForm stateForm, addStateForm, countryForm, addCountryForm, addZoneForm, zonesForm;
    public JeproLabApplicationForm addRequestForm, sampleListForm, sampleForm, requestStatusForm, requestRefundsForm;
    public JeproLabApplicationForm requestBillsForm, requestAddBillForm, requestComplainsForm, requestAddComplainForm;
    public JeproLabApplicationForm requestAddStatusForm, requestMessagesForm, requestAddMessagesForm;
    public JeproLabApplicationForm requestAddRefundForm, addTaxRuleForm, taxRulesForm;

    public List<JeproLabApplicationForm> forms;

    public JeproLabApplicationForms(){
        forms = new ArrayList();
        loginForm = new JeproLabApplicationForm("login.fxml");
        if(!forms.contains(loginForm)){ forms.add(loginForm); }

        dashBoardForm = new JeproLabApplicationForm("dashboard/dashboard.fxml");
        if(!forms.contains(dashBoardForm)){ forms.add(dashBoardForm); }

        categoryForm = new JeproLabApplicationForm("category/list.fxml");
        if(!forms.contains(categoryForm)){ forms.add(categoryForm); }

        addCategoryForm = new JeproLabApplicationForm("category/add.fxml");
        if(!forms.contains(addCategoryForm)){ forms.add(addCategoryForm); }

        aboutJeproLabForm = new JeproLabApplicationForm("help/about.fxml");
        if(!forms.contains(aboutJeproLabForm)){ forms.add(aboutJeproLabForm); }

        updaterForm = new JeproLabApplicationForm("help/updater.fxml");
        if(!forms.contains(updaterForm)){ forms.add(updaterForm); }

        feedsForm = new JeproLabApplicationForm("feeds/feeds.fxml");
        if(!forms.contains(feedsForm)){ forms.add(feedsForm); }

        addFeedForm = new JeproLabApplicationForm("feeds/feed.fxml");
        if(!forms.contains(addFeedForm)){ forms.add(addFeedForm); }

        feedBackForm = new JeproLabApplicationForm("feeds/feedbacks.fxml");
        if(!forms.contains(feedBackForm)){ forms.add(feedBackForm); }

        viewFeedBackForm = new JeproLabApplicationForm("feeds/feedback.fxml");
        if(!forms.contains(viewFeedBackForm)){ forms.add(viewFeedBackForm); }

        customersForm = new JeproLabApplicationForm("customer/list.fxml");
        if(!forms.contains(customersForm)){ forms.add(customersForm); }

        addCustomerForm = new JeproLabApplicationForm("customer/add.fxml");
        if(!forms.contains(addCustomerForm)){ forms.add(addCustomerForm); }

        analyzeForm = new JeproLabApplicationForm("analyse/list.fxml");
        if(!forms.contains(analyzeForm)){ forms.add(analyzeForm); }

        addAnalyzeForm = new JeproLabApplicationForm("analyse/add.fxml");
        if(!forms.contains(addAnalyzeForm)){ forms.add(addAnalyzeForm); }

        analyzeMethodForm = new JeproLabApplicationForm("analyse/methods.fxml");
        if(!forms.contains(analyzeMethodForm)){ forms.add(analyzeMethodForm); }

        addAnalyzeMethodForm = new JeproLabApplicationForm("analyse/method.fxml");
        if(!forms.contains(addAnalyzeMethodForm)){ forms.add(addAnalyzeMethodForm); }

        attachmentForm = new JeproLabApplicationForm("attachment/list.fxml");
        if(!forms.contains(attachmentForm)){ forms.add(attachmentForm); }

        addAttachmentForm = new JeproLabApplicationForm("attachment/add.fxml");
        if(!forms.contains(addAttachmentForm)){ forms.add(addAttachmentForm); }

        addressesForm = new JeproLabApplicationForm("address/list.fxml");
        if(!forms.contains(addressesForm)){ forms.add(addressesForm); }

        addAddressForm = new JeproLabApplicationForm("address/add.fxml");
        if(!forms.contains(addAddressForm)){ forms.add(addAddressForm); }

        groupsForm = new JeproLabApplicationForm("group/list.fxml");
        if(!forms.contains(groupsForm)){ forms.add(groupsForm); }

        addGroupForm = new JeproLabApplicationForm("group/add.fxml");
        if(!forms.contains(addGroupForm)){ forms.add(addGroupForm); }

        requestForm = new JeproLabApplicationForm("request/list.fxml");
        if(!forms.contains(requestForm)){ forms.add(requestForm); }

        addRequestForm = new JeproLabApplicationForm("request/add.fxml");
        if(!forms.contains(addRequestForm)){ forms.add(addRequestForm); }

        sampleListForm = new JeproLabApplicationForm("request/sample_list.fxml");
        if(!forms.contains(sampleListForm)){ forms.add(sampleListForm); }

        requestStatusForm = new JeproLabApplicationForm("request/statues.fxml");
        if(!forms.contains(requestStatusForm)){ forms.add(requestStatusForm); }

        requestBillsForm = new JeproLabApplicationForm("request/bills.fxml");
        if(!forms.contains(requestBillsForm)){ forms.add(requestBillsForm); }

        requestAddBillForm = new JeproLabApplicationForm("request/add_bill.fxml");
        if(!forms.contains(requestAddBillForm)){ forms.add(requestAddBillForm); }

        requestComplainsForm = new JeproLabApplicationForm("request/complain.fxml");
        if(!forms.contains(requestComplainsForm)){ forms.add(requestComplainsForm); }

        requestAddComplainForm = new JeproLabApplicationForm("request/add_complain.fxml");
        if(!forms.contains(requestAddComplainForm)){ forms.add(requestAddComplainForm); }

        requestAddRefundForm = new JeproLabApplicationForm("request/add_refund.fxml");
        if(!forms.contains(requestAddRefundForm)){ forms.add(requestAddRefundForm); }

        sampleForm = new JeproLabApplicationForm("request/sample.fxml");
        if(!forms.contains(sampleForm)){ forms.add(sampleForm); }

        customerThreadsForm = new JeproLabApplicationForm("customer/threads.fxml");
        if(!forms.contains(customerThreadsForm)){ forms.add(customerThreadsForm); }

        contactsForm = new JeproLabApplicationForm("contact/list.fxml");
        if(!forms.contains(contactsForm)){ forms.add(contactsForm); }

        laboratoriesForm = new JeproLabApplicationForm("lab/list.fxml");
        if(!forms.contains(laboratoriesForm)){ forms.add(laboratoriesForm); }

        addLaboratoryForm = new JeproLabApplicationForm("lab/add.fxml");
        if(!forms.contains(addLaboratoryForm)){ forms.add(addLaboratoryForm); }

        laboratoriesGroupsForm = new JeproLabApplicationForm("lab/groups.fxml");
        if(!forms.contains(laboratoriesGroupsForm)){ forms.add(laboratoriesGroupsForm); }

        addLaboratoriesGroupForm = new JeproLabApplicationForm("lab/group.fxml");
        if(!forms.contains(addLaboratoriesGroupForm)){ forms.add(addLaboratoriesGroupForm); }

        addTaxGroupForm = new JeproLabApplicationForm("taxes/group.fxml");
        if(!forms.contains(addTaxGroupForm)){ forms.add(addTaxGroupForm); }

        addTaxForm = new JeproLabApplicationForm("taxes/add.fxml");
        if(!forms.contains(addTaxForm)){ forms.add(addTaxForm); }

        taxesForm = new JeproLabApplicationForm("taxes/list.fxml");
        if(!forms.contains(taxesForm)){ forms.add(taxesForm); }

        taxGroupForm = new JeproLabApplicationForm("taxes/groups.fxml");
        if(!forms.contains(taxGroupForm)){ forms.add(taxGroupForm); }

        taxRulesForm = new JeproLabApplicationForm("taxes/rules.fxml");
        if(!forms.contains(taxRulesForm)){ forms.add(taxRulesForm); }

        addTaxRuleForm = new JeproLabApplicationForm("taxes/rule.fxml");
        if(!forms.contains(addTaxRuleForm)){ forms.add(addTaxRuleForm); }
        //addTaxGroupForm = new JeproLabApplicationForm("taxes/group.fxml");

        currencyForm = new JeproLabApplicationForm("currency/list.fxml");
        if(!forms.contains(currencyForm)){ forms.add(currencyForm); }

        addCurrencyForm = new JeproLabApplicationForm("currency/add.fxml");
        if(!forms.contains(addCurrencyForm)){ forms.add(addCurrencyForm); }

        stateForm = new JeproLabApplicationForm("country/states.fxml");
        if(!forms.contains(stateForm)){ forms.add(stateForm); }

        addStateForm = new JeproLabApplicationForm("country/add_state.fxml");
        if(!forms.contains(addStateForm)){ forms.add(addStateForm); }

        countryForm = new JeproLabApplicationForm("country/list.fxml");
        if(!forms.contains(countryForm)){ forms.add(countryForm); }

        addCountryForm = new JeproLabApplicationForm("country/add.fxml");
        if(!forms.contains(addCountryForm)){ forms.add(addCountryForm); }

        addZoneForm = new JeproLabApplicationForm("country/add_zone.fxml");
        if(!forms.contains(addZoneForm)){ forms.add(addZoneForm); }

        zonesForm = new JeproLabApplicationForm("country/zones.fxml");
        if(!forms.contains(zonesForm)){ forms.add(zonesForm); }

        requestRefundsForm = new JeproLabApplicationForm("request/refunds.fxml");
        if(!forms.contains(requestRefundsForm)){ forms.add(requestRefundsForm); }

        requestAddStatusForm = new JeproLabApplicationForm("request/status.fxml");
        if(!forms.contains(requestAddStatusForm)){ forms.add(requestAddStatusForm); }

        requestMessagesForm = new JeproLabApplicationForm("request/messages.fxml");
        if(!forms.contains(requestMessagesForm)){ forms.add(requestMessagesForm); }

        requestAddMessagesForm = new JeproLabApplicationForm("request/message.fxml");
        if(!forms.contains(requestAddMessagesForm)){ forms.add(requestAddMessagesForm); }
    }


    @Override
    public void eventProcessingFinished(JeproLabEvent<? extends JeproLabEventType> event) {

    }

    @Override
    public boolean eventProcessingException(JeproLabEvent<? extends JeproLabEventType> event, JeproLabEventProcessingException exception) {
        return false;
    }


    public static class JeproLabApplicationForm {
        private boolean isCreated = false;
        private Pane formWrapper = null;
        private String formLayoutPath;
        public JeproLabController controller;
        //private final static Object threadKey = new Object();

        public JeproLabApplicationForm(){}

        public JeproLabApplicationForm(String layoutPath){
            String layoutBasePath = "views/forms/";
            formLayoutPath = layoutBasePath + layoutPath;
        }

        public void createView(){
            createViewNode();
        }


        public Node createViewNode(){
            try {
                if (!isCreated || formWrapper == null) {
                    formWrapper = new Pane();
                    URL location = JeproLab.class.getResource(formLayoutPath);

                    FXMLLoader formLoader = new FXMLLoader();
                    formLoader.setLocation(location);
                    formLoader.setBuilderFactory(new JavaFXBuilderFactory());
                    formLoader.setResources(JeproLab.getBundle());
                    formWrapper = formLoader.load(location.openStream());
                    controller = formLoader.getController();

                    isCreated = true;
                }
                return formWrapper;
            }catch(IOException ignored){
                JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            }
            return new Pane();
        }

        public void setFormVisible(boolean visible){
            formWrapper.setVisible(visible);
        }
    }
}
