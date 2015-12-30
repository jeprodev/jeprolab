package com.jeprolab.views.application;

/**
 *
 * Created by jeprodev on 18/06/2014.
 */
public class JeproLabApplicationForms {
    public JeproLabApplicationForm loginForm, dashBoardForm, categoryForm, categoryAddForm, aboutJeproLabForm;
    public JeproLabApplicationForm feedsForm, feedBackForm;
    public JeproLabApplicationForm customersForm, addCustomerForm;
    public JeproLabApplicationForm analyzeForm, attachmentForm;
    public JeproLabApplicationForm addressesForm, addAddressForm;
    public JeproLabApplicationForm groupsForm, addGroupForm, requestForm;
    public JeproLabApplicationForm customerThreadsForm, contactsForm;

    public JeproLabApplicationForms(){
        loginForm = new JeproLabApplicationForm("login.fxml");
        dashBoardForm = new JeproLabApplicationForm("dashboard/dashboard.fxml");
        categoryForm = new JeproLabApplicationForm("category/list.fxml");
        categoryAddForm = new JeproLabApplicationForm("category/add.fxml");
        aboutJeproLabForm = new JeproLabApplicationForm("help/jeprolab.fxml");
        feedsForm = new JeproLabApplicationForm("feeds/feeds.fxml");
        feedBackForm = new JeproLabApplicationForm("feeds/feedBacks.fxml");
        customersForm = new JeproLabApplicationForm("customer/list.fxml");
        addCustomerForm = new JeproLabApplicationForm("customer/add.fxml");
        analyzeForm = new JeproLabApplicationForm("analyse/list.fxml");
        attachmentForm = new JeproLabApplicationForm("attachment/list.fxml");
        addressesForm = new JeproLabApplicationForm("address/list.fxml");
        addAddressForm = new JeproLabApplicationForm("address/add.fxml");
        groupsForm = new JeproLabApplicationForm("group/list.fxml");
        addGroupForm = new JeproLabApplicationForm("group/add.fxml");
        requestForm = new JeproLabApplicationForm("request/list.fxml");
        customerThreadsForm = new JeproLabApplicationForm("customer.fxml");
        contactsForm = new JeproLabApplicationForm("contact/list.fxml");
    }
}