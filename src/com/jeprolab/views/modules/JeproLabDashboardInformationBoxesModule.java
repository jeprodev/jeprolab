package com.jeprolab.views.modules;

import com.jeprolab.JeproLab;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 *
 * Created by jeprodev on 25/03/2016.
 */
public class JeproLabDashboardInformationBoxesModule extends HBox{
    private DashBoardInformationWebSessionBlock webSiteSessionsBlock;
    private DashBoardInformationCurrentVisitorsBlock currentVisitorsBlock;
    private DashBoardInformationFaceBookViewsBlock faceBookPageViewsBlock;
    private DashBoardInformationTwitterFollowersBlock twitterFollowersBlock;
    private DashBoardInformationGooglePlusViewsBlock googlePlusViewsBlock;

    public JeproLabDashboardInformationBoxesModule(){
        super(4);

        webSiteSessionsBlock = new DashBoardInformationWebSessionBlock(JeproLab.getBundle().getString("JEPROLAB_WEBSITE_SESSION_LABEL"), "resources/images/globe.png");
        //webSiteSessionsBlock ;
        currentVisitorsBlock  = new DashBoardInformationCurrentVisitorsBlock(JeproLab.getBundle().getString("JEPROLAB_CURRENT_VISITORS_LABEL"), "resources/images/globe.png");
        //currentVisitors;
        faceBookPageViewsBlock = new DashBoardInformationFaceBookViewsBlock(JeproLab.getBundle().getString("JEPROLAB_FACEBOOK_VIEWS_LABEL"), "resources/images/globe.png");
        //faceBookPageViewsBlock ;
        twitterFollowersBlock = new DashBoardInformationTwitterFollowersBlock(JeproLab.getBundle().getString("JEPROLAB_TWITTER_FOLLOWERS_LABEL"), "resources/images/globe.png");
        //twitterFollowersBlock;

        googlePlusViewsBlock = new DashBoardInformationGooglePlusViewsBlock(JeproLab.getBundle().getString("JEPROLAB_GOOGLE_PLUS_LABEL"), "resources/images/globe.png");

        this.getChildren().addAll(webSiteSessionsBlock, currentVisitorsBlock, faceBookPageViewsBlock, twitterFollowersBlock, googlePlusViewsBlock);
    }

    public void setModuleSize(double width, double height){
        double blockWidth = (width/5) - 4;
        webSiteSessionsBlock.setModuleSize(blockWidth, height);
        currentVisitorsBlock.setModuleSize(blockWidth, height);
        faceBookPageViewsBlock.setModuleSize(blockWidth, height);
        twitterFollowersBlock.setModuleSize(blockWidth, height);
        googlePlusViewsBlock.setModuleSize(blockWidth, height);
    }

    public static class DashBoardInformationWebSessionBlock extends JeproLabDashboardModule{
        public DashBoardInformationWebSessionBlock(String formTitle, String iconPath){
            super(formTitle, iconPath);
        }
    }

    public static class DashBoardInformationCurrentVisitorsBlock extends JeproLabDashboardModule{
        public DashBoardInformationCurrentVisitorsBlock(String formTitle, String iconPath){
            super(formTitle, iconPath);
        }
    }

    public static class DashBoardInformationFaceBookViewsBlock extends JeproLabDashboardModule{
        public DashBoardInformationFaceBookViewsBlock(String formTitle, String iconPath){
            super(formTitle, iconPath);
        }
    }

    public static class DashBoardInformationGooglePlusViewsBlock extends JeproLabDashboardModule{
        public DashBoardInformationGooglePlusViewsBlock(String formTitle, String iconPath){
            super(formTitle, iconPath);
        }
    }

    public static class DashBoardInformationTwitterFollowersBlock extends JeproLabDashboardModule{
        public DashBoardInformationTwitterFollowersBlock(String formTitle, String iconPath){
            super(formTitle, iconPath);
        }

        /*private Pane formTitleWrapper, formContentWrapper;
        private HBox titleContentWrapper;
        public Label titleWrapper;

        public DashBoardInformationBlock(String formTitle, String iconPath) {
            formContentWrapper = new Pane();
            formTitleWrapper = new Pane();
            titleContentWrapper = new HBox(5);
            formTitleWrapper.getChildren().add(titleContentWrapper);

            titleWrapper = new Label();
            titleWrapper.getStyleClass().add("module-label");
            formTitleWrapper.getStyleClass().add("module-title-container");
            formContentWrapper.getStyleClass().add("module-panel-container");
            this.getChildren().addAll(formTitleWrapper, formContentWrapper);

            titleWrapper.setText(formTitle);
            ImageView icon = new ImageView(new Image(JeproLab.class.getResourceAsStream(iconPath)));
            titleContentWrapper.getChildren().addAll(icon, titleWrapper);
        }

        public  void setBlockSize(double width, double height){
            this.setPrefSize(width, height);
            this.formContentWrapper.setLayoutY(20);
            this.formContentWrapper.setPrefSize(width, height - 20);
            this.formTitleWrapper.setPrefSize(width, 20);
        }*/

    }
}
