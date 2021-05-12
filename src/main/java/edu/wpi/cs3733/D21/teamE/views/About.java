package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class About {

    @FXML // fx:id="imageView"
    private ImageView imageView;

    @FXML
    private AnchorPane appBarAnchorPane;

    @FXML
    public void initialize() {

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false);
            App.setShowLogin(true);
            App.setPageTitle("About Us");
            App.setHelpText(""); //todo help text
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image about = new Image("edu/wpi/cs3733/D21/teamE/about.png");
        imageView.setImage(about);
    }

    @FXML
    private void exit(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
