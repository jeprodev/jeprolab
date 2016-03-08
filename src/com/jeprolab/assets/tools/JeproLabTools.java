package com.jeprolab.assets.tools;


import com.jeprolab.JeproLab;
import com.jeprolab.models.JeproLabCurrencyModel;
import com.jeprolab.models.JeproLabModel;
import com.jeprolab.models.JeproLabSettingModel;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class JeproLabTools {
    private static int default_currency_id = 0;
    private static int price_round_method = -1;

    private static Alert dialogBox = null;

    public static boolean isLoadedObject(JeproLabModel model, String key){
        return false;
    }

    public static String md5(String text){
        return text;
    }

    public static void copy(String originalPath, String destinationPath){
        InputStream inStream = null;
        OutputStream outputStream = null;
        try {
            File fileIn = new File(originalPath);
            File fileOut = new File(destinationPath);

            inStream = new FileInputStream(fileIn);
            outputStream = new FileOutputStream(fileOut);

            byte[] buffer = new byte[1024];
            int length;
            while((length = inStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }

            inStream.close();
            outputStream.close();
        }catch (IOException ignored){

        }
    }

    public static void displayError(int errorCode, String errorMessage){
        if(dialogBox == null){
            dialogBox = new Alert(Alert.AlertType.ERROR);
        }else{
            dialogBox.setAlertType(Alert.AlertType.ERROR);
        }
        dialogBox.setTitle(JeproLab.getBundle().getString("JEPROLAB_AN_ERROR_OCCURRED_LABEL"));
        dialogBox.setContentText(errorMessage);
        dialogBox.showAndWait();
    }

    public static void displayWarning(int errorCode, String errorMessage){
        if(dialogBox == null){
            dialogBox = new Alert(Alert.AlertType.WARNING);
        }else{
            dialogBox.setAlertType(Alert.AlertType.WARNING);
        }
        dialogBox.setTitle(JeproLab.getBundle().getString("JEPROLAB_WARNING_LABEL"));
        dialogBox.setContentText(errorMessage);
        dialogBox.showAndWait();
    }

    public static boolean isOrderBy(String order){
        String pattern = "/^[a-zA-Z0-9._-]+$/";
        return true; //order.matches(pattern);
    }

    public static boolean isOrderWay(String way){
        return (way.toLowerCase().equals("asc") | way.toLowerCase().equals("desc"));
    }
    public static String date(String format){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        return format1.format(calendar.getTime());
    }

    public static Date getDate(String value){
        //value = value.replace("-", "/");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return formatter.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*public static boolean (){}*/

    public static float convertPrice(float price){
        return convertPrice(price, 0, true, null);
    }

    public static float convertPrice(float price, int currencyId){
        return convertPrice(price, currencyId, true, null);
    }
    public static float convertPrice(float price, int currencyId, boolean toCurrency){
        return convertPrice(price, currencyId, toCurrency, null);
    }

    /**
     * return converted price
     * @param price The price to be converted
     * @param currencyId currency Id
     * @param toCurrency to the new currency
     * @param context current app context
     * @return float
     */
    public static float convertPrice(float price, int currencyId, boolean toCurrency, JeproLabContext context){
        if(default_currency_id == 0){
            default_currency_id = JeproLabSettingModel.getIntValue("default_currency");
        }
        if(context == null){ context = JeproLabContext.getContext(); }

        if(currencyId <= 0){
            currencyId = context.currency.currency_id;
        }
        JeproLabCurrencyModel currency = JeproLabCurrencyModel.getCurrencyInstance(currencyId);

        //$currency_id = (is_object($currency) ? $currency->currency_id : $currency['currency_id']);
        float conversionRate = currency.conversion_rate;

        if(currencyId != default_currency_id){
            if(toCurrency) {price *= conversionRate; }
            else { price /= conversionRate; }
        }
        return price;
    }

    public static float roundPrice(float value){
        return roundPrice(value, 0);
    }

    public static float roundPrice(float value, int precision){
        if(price_round_method < 0){
            price_round_method = JeproLabSettingModel.getIntValue("price_round_mode");
        }
        if(price_round_method == JeproLabConfigurationSettings.JEPROLAB_ROUND_UP_PRICE){
            return JeproLabTools.priceCeil(value, precision);
        }else if(price_round_method == JeproLabConfigurationSettings.JEPROLAB_ROUND_DOWN_PRICE){
            return JeproLabTools.priceFloor(value, precision);
        }
        return Math.round(value); //, precision);
    }

    public static float priceCeil(float value){
        return priceCeil(value, 0);
    }

    public static float priceCeil(float value, int precision){
        int precisionFactor = (precision == 0 ? 1 : (int)Math.pow(10, precision));
        float tmp = value * precisionFactor;
        /*String tmp2 = tmp;
        // If the current value has already the desired precision
        if(strpos($tmp2, '.') == false){ return value; }
        if($tmp2[strlen($tmp2) - 1] == 0){  return value; } */

        return (float)(Math.ceil(tmp) / precisionFactor);
    }

    public static float priceFloor(float value){
        return priceFloor(value, 0);
    }

    public static float priceFloor(float value, int precision){
        int precisionFactor = precision == 0 ? 1 : (int)Math.pow(10, precision);
        float tmp = value * precisionFactor;
        /* $tmp2 = (string)$tmp;

        // If the current value has already the desired precision
        if(strpos($tmp2, '.') == false){
            return value;
        }
        if($tmp2[strlen($tmp2) - 1] == 0){
            return value;
        } */
        return (float)(Math.floor(tmp) / precisionFactor);
    }

    /*public static List orderbyPrice(List items, String orderWay) {
        /*foreach($array as & $row) {
            $row['price_tmp'] = Product::getPriceStatic
            ($row['id_product'], true, ((isset($row['id_product_attribute']) && !empty($row['id_product_attribute'])) ? (int) $row['id_product_attribute'] : null), 2)
            ;
        }

        unset($row);

        if (Tools::strtolower ($order_way) == 'desc'){
            uasort($array, 'cmpPriceDesc');
        }else{
            uasort($array, 'cmpPriceAsc');
        }
        foreach($array as & $row) {
            unset($row['price_tmp']);
        }* /
    } */

    /***************** Numeric Validation Limit the  characters to maxLength AND to ONLY DigitS *********************/
    public static EventHandler<KeyEvent> numericValidation(final Integer maxLength) {
        return e -> {
            TextField txtTextField = (TextField) e.getSource();
            if (txtTextField.getText().length() >= maxLength) {
                e.consume();
            }
            if(e.getCharacter().matches("[0-9.]")){
                if(txtTextField.getText().contains(".") && e.getCharacter().matches("[.]")){
                    e.consume();
                }else if(txtTextField.getText().length() == 0 && e.getCharacter().matches("[.]")){
                    e.consume();
                }
            }else{
                e.consume();
            }
        };
    }

    /***************** Numeric Validation Limit the  characters to maxLength AND to ONLY DigitS *********************/
    public static EventHandler<KeyEvent> codeValidation(final Integer maxLength) {
        return e -> {
            TextField txtTextField = (TextField) e.getSource();
            if (txtTextField.getText().length() >= maxLength) {
                e.consume();
            }
            if(e.getCharacter().matches("[0-9]")){
                if(txtTextField.getText().contains(".") && e.getCharacter().matches("[.]")){
                    e.consume();
                }else if(txtTextField.getText().length() == 0 && e.getCharacter().matches("[.]")){
                    e.consume();
                }
            }else{
                e.consume();
            }
        };
    }

    /************* Letters Validation Limit the  characters to maxLengh AND to ONLY Letters **************/
    public EventHandler<KeyEvent> letterValidation(final Integer maxLength) {
        return evt -> {
            TextField txt_TextField = (TextField) evt.getSource();
            if (txt_TextField.getText().length() >= maxLength) {
                evt.consume();
            }
            if(!evt.getCharacter().matches("[A-Za-z]")){
                evt.consume();
            }

        };
    }

    public static String getCleanDescription(String value){
        return value;
    }

    public static String strReplace(String value, String search, String replaceWith){
        return value.replaceAll(search, replaceWith);
    }

    public static String strStr(String value, String regex){
        return value.substring(value.indexOf(regex), value.length() - 1);
    }

    public static String rightTrim(String param, String regex){
        return  param;
    }

    public static boolean fileExistsInCache(String filePath){
        return true;
    }

    public static String getOrderFormToken(){
        return "a";
    }

    public static boolean checkCategoryToken(){
        return true;
    }

    public static String getCategoryToken(){
        return "";
    }

    public static String getAnalyzeToken(){
        return "";
    }

    public static boolean checkProductToken(){
        return true;
    }

    public static String getAttributeGroupToken(){
        return "";
    }

    public static boolean checkAttributeGroupToken(){
        return true;
    }

    public static String getAttachmentToken(){
        return "";
    }

    public static boolean checkAttachmentToken(){
        return true;
    }

    public static String getDiscountToken(){
        return "";
    }

    public static boolean checkDiscountToken(){
        return true;
    }

    public static String getFeatureToken(){
        return "";
    }

    public static boolean checkFeatureToken(){
        return true;
    }
    public static String getSupplierToken(){
        return "";
    }

    public static boolean checkSupplierToken(){
        return true;
    }

    public static String getAddressToken(){
        return "";
    }

    public static boolean checkAddressToken(){
        return true;
    }

    public static String getCartToken(){
        return "";
    }

    public static boolean checkCartToken(){
        return true;
    }

    public static boolean checkCountryToken(){
        return true;
    }

    public static String getCountryToken(){
        return "";
    }

    public static String getCustomerToken(){
        return "";
    }

    public static boolean checkCustomerToken(){
        return true;
    }
    public static String getCurrencyToken(){
        return "";
    }

    public static boolean checkCurrencyToken(){
        return true;
    }

    public static String getGroupToken(){
        return "";
    }

    public static boolean checkGroupToken(){
        return true;
    }

    public static String getTaxToken(){
        return "";
    }

    public static boolean checkTaxToken(){
        return true;
    }
}