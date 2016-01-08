package com.jeprolab.views.application;



/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabApplicationForms {
    public JeproLabApplicationForm loginForm, dashBoardForm, categoryForm;
    public JeproLabApplicationForm addCategoryForm, aboutJeproLabForm, updaterForm;
    public JeproLabApplicationForm feedsForm, addFeedForm, feedBackForm;
    public JeproLabApplicationForm customersForm, addCustomerForm;
    public JeproLabApplicationForm laboratoriesForm, addLaboratoryForm;
    public JeproLabApplicationForm analyzeForm,addAnalyzeForm;
    public JeproLabApplicationForm attachmentForm, addAttachmentForm;
    public JeproLabApplicationForm addressesForm, addAddressForm;
    public JeproLabApplicationForm groupsForm, addGroupForm, requestForm;
    public JeproLabApplicationForm customerThreadsForm, contactsForm;

    public JeproLabApplicationForms(){
        loginForm = new JeproLabApplicationForm("login.fxml");
        dashBoardForm = new JeproLabApplicationForm("dashboard/dashboard.fxml");
        categoryForm = new JeproLabApplicationForm("category/list.fxml");
        addCategoryForm = new JeproLabApplicationForm("category/add.fxml");
        aboutJeproLabForm = new JeproLabApplicationForm("help/jeprolab.fxml");
        updaterForm = new JeproLabApplicationForm("help/updater.fxml");
        feedsForm = new JeproLabApplicationForm("feeds/feeds.fxml");
        addFeedForm = new JeproLabApplicationForm("feeds/feeds.fxml");
        feedBackForm = new JeproLabApplicationForm("feeds/feedBacks.fxml");
        customersForm = new JeproLabApplicationForm("customer/list.fxml");
        addCustomerForm = new JeproLabApplicationForm("customer/add.fxml");
        analyzeForm = new JeproLabApplicationForm("analyse/list.fxml");
        addAnalyzeForm = new JeproLabApplicationForm("analyse/add.fxml");
        attachmentForm = new JeproLabApplicationForm("attachment/list.fxml");
        addAttachmentForm = new JeproLabApplicationForm("attachment/add.fxml");
        addressesForm = new JeproLabApplicationForm("address/list.fxml");
        addAddressForm = new JeproLabApplicationForm("address/add.fxml");
        groupsForm = new JeproLabApplicationForm("group/list.fxml");
        addGroupForm = new JeproLabApplicationForm("group/add.fxml");
        requestForm = new JeproLabApplicationForm("request/list.fxml");
        customerThreadsForm = new JeproLabApplicationForm("customer.fxml");
        contactsForm = new JeproLabApplicationForm("contact/list.fxml");
        laboratoriesForm = new JeproLabApplicationForm("lab/list.fxml");
        addLaboratoryForm = new JeproLabApplicationForm("lab/add.fxml");
    }
}