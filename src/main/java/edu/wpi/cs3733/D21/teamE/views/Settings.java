/**
 * Sample Skeleton for 'Settings.fxml' Controller Class
 */

package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.database.makeConnection;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.ServiceRequestObjs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Settings {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="stackPane"
    private StackPane stackPane; // Value injected by FXMLLoader

    @FXML // fx:id="background"
    private ImageView background; // Value injected by FXMLLoader

    @FXML // fx:id="clientToggle"
    private JFXToggleButton clientToggle; // Value injected by FXMLLoader

    @FXML // fx:id="cancel"
    private JFXButton cancel; // Value injected by FXMLLoader

    @FXML // fx:id="submit"
    private JFXButton submit; // Value injected by FXMLLoader

    @FXML
    void handleButtonCancel(ActionEvent event) {

    }

//    shut down an in-memory database using the embedded driver
//    jdbc:derby:memory:myInMemDB;shutdown=true
//    shut down an in-memory database using the Network Server
//    jdbc:derby://localhost:1527/memory:myInMemDB;shutdown=true

    @FXML
    void switchDatabases(ActionEvent event){
        String directory = System.getProperty("user.dir");
        if (clientToggle.isSelected()) {

            //terminate the embedded DB connection
            DB.terminateConnection("jdbc:derby:;shutdown=true");
            System.out.println("Client Driven Connection");

            //Create the driver URL for the client driver connection
            String driverURL = "jdbc:derby://localhost:1527/bw;createFrom=" + directory + "/BWDB";

            //make the new client driver connection
            makeConnection.makeConnection(driverURL);

        }
        else {

            //TODO: embedded driver wanted (need to figure out how to save client data to embedded driver)
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        Stage primaryStage = App.getPrimaryStage();
        Image backgroundImg = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
        Image backgroundImage = backgroundImg;
        background.setImage(backgroundImage);
        background.setEffect(new GaussianBlur());

        //background.setPreserveRatio(true);
        background.fitWidthProperty().bind(primaryStage.widthProperty());
        //background.fitHeightProperty().bind(primaryStage.heightProperty());


        assert appBarAnchorPane != null : "fx:id=\"appBarAnchorPane\" was not injected: check your FXML file 'Settings.fxml'.";
        assert stackPane != null : "fx:id=\"stackPane\" was not injected: check your FXML file 'Settings.fxml'.";
        assert background != null : "fx:id=\"background\" was not injected: check your FXML file 'Settings.fxml'.";
        assert clientToggle != null : "fx:id=\"clientToggle\" was not injected: check your FXML file 'Settings.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'Settings.fxml'.";
        assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'Settings.fxml'.";

    }
}
