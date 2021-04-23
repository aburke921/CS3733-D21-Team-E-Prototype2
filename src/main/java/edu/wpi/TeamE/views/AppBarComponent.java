/**
 * Sample Skeleton for 'AppBarComponent.fxml' Controller Class
 */

package edu.wpi.TeamE.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class AppBarComponent {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="fullscreen"
    private Rectangle fullscreen; // Value injected by FXMLLoader

    @FXML // fx:id="hide"
    private Circle hide; // Value injected by FXMLLoader

    @FXML // fx:id="exit"
    private Polygon exit; // Value injected by FXMLLoader

    @FXML // fx:id="appBarTitleLabel"
    private Label appBarTitleLabel; // Value injected by FXMLLoader

    @FXML
    void getHelpDefault(ActionEvent event) {
        //add the help button only on certain pages
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert fullscreen != null : "fx:id=\"fullscreen\" was not injected: check your FXML file 'AppBarComponent.fxml'.";
        assert hide != null : "fx:id=\"hide\" was not injected: check your FXML file 'AppBarComponent.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'AppBarComponent.fxml'.";
        assert appBarTitleLabel != null : "fx:id=\"appBarTitleLabel\" was not injected: check your FXML file 'AppBarComponent.fxml'.";

        //todo, get text from somewhere and set it
        //appBarTitleLabel.setText();

    }
}
