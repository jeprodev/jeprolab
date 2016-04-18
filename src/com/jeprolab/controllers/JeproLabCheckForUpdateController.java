package com.jeprolab.controllers;

import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.extend.controls.JeproFormPanel;
import com.jeprolab.assets.tools.JeproLabUpdater;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import jdk.internal.org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

/**
 *
 * Created by jeprodev on 02/01/2015.
 */
public class JeproLabCheckForUpdateController extends JeproLabController{
    @FXML
    public JeproFormPanel jeproLabCheckForUpdateWrapper;
    public Pane jeproLabCheckForUpdateContainerWrapper;
    public GridPane jeproLabCheckForUpdateLayout;
    public Label jeproLabInstalledVersionLabel, jeproLabInstalledVersion, jeproLabAvailableVersionLabel, jeproLabAvailableVersion, jeproLabNewFeaturesLabel;
    public Text jeproLabNewFeatures;
    public Button jeproLabIgnoreUpdateButton, jeproLabUpdateButton;

    @Override
    public void initialize(URL location, ResourceBundle resource){
        super.initialize(location, resource);
        bundle = resource;

        jeproLabCheckForUpdateContainerWrapper.setPrefSize(0.94 * JeproLab.APP_WIDTH, 0.70 * JeproLab.APP_HEIGHT);
        jeproLabCheckForUpdateContainerWrapper.setLayoutX(0.03 * JeproLab.APP_WIDTH);
        jeproLabCheckForUpdateContainerWrapper.setLayoutY(30);
        jeproLabCheckForUpdateLayout.getColumnConstraints().addAll(
                new ColumnConstraints(150), new ColumnConstraints(240), new ColumnConstraints(150), new ColumnConstraints(240)
        );
        jeproLabInstalledVersionLabel.setText(bundle.getString("JEPROLAB_INSTALLED_VERSION_LABEL"));
        jeproLabAvailableVersionLabel.setText(bundle.getString("JEPROLAB_LATEST_VERSION_LABEL"));
        jeproLabNewFeaturesLabel.setText(bundle.getString("JEPROLAB_NEW_FEATURES_LABEL"));
        jeproLabIgnoreUpdateButton.setText(bundle.getString("JEPROLAB_IGNORE_UPDATE_LABEL"));
        jeproLabUpdateButton.setText(bundle.getString("JEPROLAB_UPDATE_LABEL"));

        Text versionWrapper = new Text();
        versionWrapper.setLayoutY(10);
        versionWrapper.setLayoutY(10);
        /* jeproLabCheckForUpdateWrapper.getChildren().add(versionWrapper);
        try{
            //String data = JeproLabUpdater.getData("http://localhost/jeprotest/index.php?option=com_jeproshop&view=product&product_id=1");
            //versionWrapper.setText(data);
            //System.out.println(JeproLabUpdater.getLatestVersion());
        }catch (Exception exp){ exp.printStackTrace();} */
    }

    public void initializeContent(){
        /**
         * Grab update information
         */
        JeproLabRelease installed = new JeproLabRelease();
        installed.setReleaseVersion(JeproLabConfig.installedAppVesion);
        installed.setReleasePackage(JeproLabConfig.installedAppPackage);
        jeproLabInstalledVersion.setText(JeproLabConfig.installedAppVesion);

        JeproLabRelease release = JeproLabRelease.getRelease(JeproLabUpdater.getData(JeproLabConfig.appUpdateUrl));
        int answer = installed.compareTo(release);
        if(answer > 0){
            jeproLabAvailableVersion.setText(release.getReleaseVersion());
            jeproLabAvailableVersion.getStyleClass().add("success-label");
        }else{
            jeproLabInstalledVersion.getStyleClass().add("success-label");
        }
    }

    private static class JeproLabRelease implements Comparable{
        private String releaseDate, releaseVersion, releaseSeverity, releaseMessage, releasePackage;

        public String getReleaseDate(){
            return releaseDate;
        }

        public String getReleaseVersion(){
            return releaseVersion;
        }

        public String getReleaseSeverity(){
            return releaseSeverity;
        }

        public String getReleaseMessage(){
            return releaseMessage;
        }

        public String getReleasePackage(){
            return releasePackage;
        }

        public void setReleaseDate(String date){
            releaseDate = date;
        }

        public void setReleaseVersion(String version){
            releaseVersion = version;
        }

        public void setReleaseSeverity(String severity){
            releaseSeverity = severity;
        }

        public void setReleaseMessage(String message){
            releaseMessage = message;
        }

        public void setReleasePackage(String packageRel){
            releasePackage = packageRel;
        }

        @Override
        public String toString(){
            return "";
        }

        /**
         *
         * @param release the release to compare to
         * @return
         */
        @Override
        public int compareTo(Object release) {
            String[] newVersion = ((JeproLabRelease)release).getReleaseVersion().split("\\.");
            String[] oldVersion = this.getReleaseVersion().split("\\.");

            int ind = 0;
            while(ind < newVersion.length && ind < oldVersion.length && oldVersion[ind].equals(newVersion[ind])){ ind++; }

            //compare first non-equal ordinal number
            if(ind < oldVersion.length && ind < newVersion.length){
                int diff = Integer.valueOf(newVersion[ind]).compareTo(Integer.valueOf(oldVersion[ind]));
                return Integer.signum(diff);
            }else{
                return Integer.signum(newVersion.length - oldVersion.length);
            }
        }

        public static JeproLabRelease parse(String fileName, JeproLabUpdater.Mode mode){
            try {
                XMLReader reader = XMLReaderFactory.createXMLReader();
                JeproLabReleaseXmlParserHandler handler = new JeproLabReleaseXmlParserHandler();
                reader.setContentHandler(handler);
                reader.setErrorHandler(handler);

                if(mode == JeproLabUpdater.Mode.FILE){
                    reader.parse(new InputSource(new FileReader(new File(fileName))));
                }else{
                    URL url = new URL(fileName);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    System.out.println(JeproLabUpdater.getData(fileName));

                    //reader.parse(new InputSource(inputStream));
                }
                return handler.getRelease();
            } catch (org.xml.sax.SAXException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static JeproLabRelease getRelease(String updateInfo){
            JeproLabRelease release = new JeproLabRelease();
            release.setReleaseDate(updateInfo.substring(updateInfo.indexOf("<date>") + 6, updateInfo.indexOf("</date>")));
            release.setReleaseVersion(updateInfo.substring(updateInfo.indexOf("<version>") + 9, updateInfo.indexOf("</version>")));
            release.setReleaseMessage(updateInfo.substring(updateInfo.indexOf("<message>") + 9, updateInfo.indexOf("</message>")));
            release.setReleaseSeverity(updateInfo.substring(updateInfo.indexOf("<severity>") + 10, updateInfo.indexOf("</severity>")));
            //release.setReleaseSeverity(updateInfo.substring(updateInfo.indexOf("<>") + , updateInfo.indexOf("</>")));
            return release;
        }
    }

    private static class JeproLabReleaseXmlParserHandler extends DefaultHandler{
        private String currentElement = "";
        private JeproLabRelease release = new JeproLabRelease();

        public JeproLabReleaseXmlParserHandler(){
            super();
        }

        @Override
        public void startElement(String uri, String name, String qName, Attributes attributes){
            currentElement = qName;
        }

        @Override
        public void characters(char ch[], int start, int length){
            String value = "";
            if(!currentElement.equals("")){
                value = String.copyValueOf(ch, start, length).trim();
            }

            switch (currentElement) {
                case "date":
                    release.setReleaseDate(value);
                    break;
                case "version":
                    release.setReleaseVersion(value);
                    break;
                case "severity":
                    release.setReleaseSeverity(value);
                    break;
                case "message":
                    release.setReleaseMessage(value);
                    break;
            }
            currentElement = "";
        }

        public JeproLabRelease getRelease(){
            return release;
        }
    }
}