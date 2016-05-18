package com.jeprolab.views;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabApplicationForms {
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
    public JeproLabApplicationForm currencyForm, addCurrencyForm, stateForm, addStateForm, countryForm, addCountryForm, addZoneForm, zonesForm;
    public JeproLabApplicationForm addRequestForm, sampleListForm, sampleForm;

    public JeproLabApplicationForms(){
        loginForm = new JeproLabApplicationForm("login.fxml");
        dashBoardForm = new JeproLabApplicationForm("dashboard/dashboard.fxml");
        categoryForm = new JeproLabApplicationForm("category/list.fxml");
        addCategoryForm = new JeproLabApplicationForm("category/add.fxml");
        aboutJeproLabForm = new JeproLabApplicationForm("help/jeprolab.fxml");
        updaterForm = new JeproLabApplicationForm("help/updater.fxml");
        feedsForm = new JeproLabApplicationForm("feeds/feeds.fxml");
        addFeedForm = new JeproLabApplicationForm("feeds/feed.fxml");
        feedBackForm = new JeproLabApplicationForm("feeds/feedBacks.fxml");
        viewFeedBackForm = new JeproLabApplicationForm("feeds/feedBack.fxml");
        customersForm = new JeproLabApplicationForm("customer/list.fxml");
        addCustomerForm = new JeproLabApplicationForm("customer/add.fxml");
        analyzeForm = new JeproLabApplicationForm("analyse/list.fxml");
        addAnalyzeForm = new JeproLabApplicationForm("analyse/add.fxml");
        analyzeMethodForm = new JeproLabApplicationForm("analyse/methods.fxml");
        addAnalyzeMethodForm = new JeproLabApplicationForm("analyse/method.fxml");
        attachmentForm = new JeproLabApplicationForm("attachment/list.fxml");
        addAttachmentForm = new JeproLabApplicationForm("attachment/add.fxml");
        addressesForm = new JeproLabApplicationForm("address/list.fxml");
        addAddressForm = new JeproLabApplicationForm("address/add.fxml");
        groupsForm = new JeproLabApplicationForm("group/list.fxml");
        addGroupForm = new JeproLabApplicationForm("group/add.fxml");
        requestForm = new JeproLabApplicationForm("request/list.fxml");
        addRequestForm = new JeproLabApplicationForm("request/add.fxml");
        sampleListForm = new JeproLabApplicationForm("request/sample_list.fxml");
        sampleForm = new JeproLabApplicationForm("request/sample.fxml");
        customerThreadsForm = new JeproLabApplicationForm("customer.fxml");
        contactsForm = new JeproLabApplicationForm("contact/list.fxml");
        laboratoriesForm = new JeproLabApplicationForm("lab/list.fxml");
        addLaboratoryForm = new JeproLabApplicationForm("lab/add.fxml");
        laboratoriesGroupsForm = new JeproLabApplicationForm("lab/groups.fxml");
        addLaboratoriesGroupForm = new JeproLabApplicationForm("lab/add_group.fxml");
        addTaxGroupForm = new JeproLabApplicationForm("taxes/add_group.fxml");
        addTaxForm = new JeproLabApplicationForm("taxes/add.fxml");
        taxesForm = new JeproLabApplicationForm("taxes/list.fxml");
        taxGroupForm = new JeproLabApplicationForm("taxes/groups.fxml");
        //addTaxGroupForm = new JeproLabApplicationForm("taxes/add_group.fxml");
        currencyForm = new JeproLabApplicationForm("currency/list.fxml");
        addCurrencyForm = new JeproLabApplicationForm("currency/add.fxml");
        stateForm = new JeproLabApplicationForm("country/states.fxml");
        addStateForm = new JeproLabApplicationForm("country/add_state.fxml");
        countryForm = new JeproLabApplicationForm("country/list.fxml");
        addCountryForm = new JeproLabApplicationForm("country/add.fxml");
        addZoneForm = new JeproLabApplicationForm("country/add_zone.fxml");
        zonesForm = new JeproLabApplicationForm("country/zones.fxml");
    }
}
