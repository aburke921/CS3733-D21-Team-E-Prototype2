package edu.wpi.cs3733.D21.teamE.views;

import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.database.UserAccountDB;
import edu.wpi.cs3733.D21.teamE.states.CreateAccountState;
import edu.wpi.cs3733.D21.teamE.states.ServiceRequestState;
import edu.wpi.cs3733.D21.teamE.states.ServiceRequestStatusState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;

import java.io.IOException;

public class ServiceRequests {

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

        String userType = UserAccountDB.getUserType(App.userID);
        if(userType.equals("visitor")) {
            adminServices.setVisible(false);
            visitorServices.setVisible(true);
        }

        if(userType.equals("patient")) {
            adminServices.setVisible(false);
            patientServices.setVisible(true);
        }

    }

    @FXML
    private void toDefault(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toStatus(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toFloralDelivery(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toExternalPatient(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toLanguageInterpreter(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toMedicineDelivery(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toSanitation(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toFoodDelivery(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toLaundryRequest(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toMaintenanceRequest(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toInternalPatient(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toSecurity(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

    @FXML
    private void toReligous(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }
}

