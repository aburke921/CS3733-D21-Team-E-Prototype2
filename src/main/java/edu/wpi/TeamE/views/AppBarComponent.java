/**
 * Sample Skeleton for 'AppBarComponent.fxml' Controller Class
 */

package edu.wpi.TeamE.views;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class AppBarComponent {


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private VBox mainVBox;

//    @FXML // fx:id="appBarAnchorPane"
//    private AnchorPane appBarAnchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="fullscreen"
    private Rectangle fullscreen; // Value injected by FXMLLoader

    @FXML // fx:id="hide"
    private Circle hide; // Value injected by FXMLLoader

    @FXML // fx:id="exit"
    private Polygon exit; // Value injected by FXMLLoader

    @FXML // fx:id="appBarTitleLabel"
    private Label appBarTitleLabel; // Value injected by FXMLLoader

    @FXML // fx:id="appBarHelpButton"
    private JFXButton appBarHelpButton; // Value injected by FXMLLoader

    @FXML
    void getHelpDefault(ActionEvent event) {
        //add the help button only on certain pages
    }

    //If exit button is clicked, exit app
    @FXML
    void exitApplication(MouseEvent event) {
        App app = new App();
        app.stop();
    }

    @FXML
    void fullscreenApplication(MouseEvent event) {
        App.getPrimaryStage().setFullScreen(!App.getPrimaryStage().isFullScreen());
    }

    @FXML
    void hideApplication(MouseEvent event) {
        App.getPrimaryStage().setIconified(true);
    }

    public void setAppBarTitleLabel(String title) {
        this.appBarTitleLabel.setText(title);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert fullscreen != null : "fx:id=\"fullscreen\" was not injected: check your FXML file 'AppBarComponent.fxml'.";
        assert hide != null : "fx:id=\"hide\" was not injected: check your FXML file 'AppBarComponent.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'AppBarComponent.fxml'.";
        assert appBarTitleLabel != null : "fx:id=\"appBarTitleLabel\" was not injected: check your FXML file 'AppBarComponent.fxml'.";

        //todo, get text from somewhere and set it
        if (App.getPageTitle() != null) { //if pageTitle is set
            appBarTitleLabel.setText(App.getPageTitle());
        }


        //make responsive to parent
        AnchorPane.setBottomAnchor(mainVBox, 0.0);
        AnchorPane.setRightAnchor(mainVBox, 0.0);
        AnchorPane.setLeftAnchor(mainVBox, 0.0);
        AnchorPane.setTopAnchor(mainVBox, 0.0);


    }
}
