package edu.wpi.cs3733.D21.teamE.views;

import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.states.ServiceRequestState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.io.IOException;

public class ServiceRequests {


    @FXML // fx:id="background"
    private ImageView background;

    @FXML // fx:id="exit"
    private Polygon exit;

    @FXML // fx:id="adminServices"
    private VBox adminServices;

    @FXML // fx:id="adminServices"
    private VBox patientServices;

    @FXML // fx:id="adminServices"
    private VBox visitorServices;

    @FXML //anchorPane for the appBar
    private AnchorPane appBarAnchorPane;

//    @FXML //stackPane for DialogBoxes
//    private StackPane stackPane;

    public void initialize() {

        Stage primaryStage = App.getPrimaryStage();
//        Image backgroundImg = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
//        Image backgroundImage = backgroundImg;
//        background.setImage(backgroundImage);
//        background.setEffect(new GaussianBlur());

        //background.setPreserveRatio(true);
        background.fitWidthProperty().bind(primaryStage.widthProperty());
        //background.fitHeightProperty().bind(primaryStage.heightProperty());

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false); // show help or not
            App.setShowLogin(true); // show login or not
            App.setPageTitle("Service Request System"); //set AppBar title
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }

        String userType = DB.getUserType(App.userID);
        if(userType.equals("visitor")) {
            adminServices.setVisible(false);
            visitorServices.setVisible(true);
        }

        if(userType.equals("patient")) {
            adminServices.setVisible(false);
            patientServices.setVisible(true);
        }

    }

    /**
     * Switch to a different scene
     * @param e tells which button was pressed
     */
    @FXML
    private void switchScene(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }
}

