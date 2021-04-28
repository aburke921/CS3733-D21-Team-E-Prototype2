package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;

import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.shape.Polygon;

public class SecurityService extends ServiceRequestFormComponents {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // fx:id = "locationInput"
    private JFXComboBox<String> locationInput;

    @FXML // fx:id="helpSecurityService"
    private Button helpSecurityService; // Value injected by FXMLLoader

    @FXML // fx:id="levelOfSecurity"
    private JFXComboBox<String> levelOfSecurity; // Value injected by FXMLLoader

    @FXML // fx:id="levelOfUrgency"
    private JFXComboBox<String> levelOfUrgency; // Value injected by FXMLLoader

    @FXML // fx:id = "assignedPersonnel"
    private JFXComboBox<String> assignedPersonnel;

    @FXML // fx:id="reasonForRequest"
    private JFXTextArea reasonForRequest; // Value injected by FXMLLoader

    @FXML // fx:id="cancelSecurityRequest"
    private JFXButton cancelSecurityRequest; // Value injected by FXMLLoader

    @FXML // fx:id="submitSecurityRequest"
    private JFXButton submitSecurityRequest; // Value injected by FXMLLoader

    @FXML // fx:id="exit"
    private Polygon exit;

    /**
     * Returns to default page
     * todo, in future iterations this button should SUBMIT instead
     * @param event {@link ActionEvent} info for the submit button call, passed automatically by system.
     */
    @FXML
    void handleButtonSubmit(ActionEvent event) {
        if (validateInput()) {
            try {
                saveData(event);
                System.out.println(event); //Print the ActionEvent to console
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean validateInput(){
        RequiredFieldValidator validator = new RequiredFieldValidator();

        validator.setMessage("Input required");


        locationInput.getValidators().add(validator);
        levelOfSecurity.getValidators().add(validator);
        levelOfUrgency.getValidators().add(validator);
        assignedPersonnel.getValidators().add(validator);
        reasonForRequest.getValidators().add(validator);

        return  locationInput.validate() && levelOfSecurity.validate() && levelOfUrgency.validate() && assignedPersonnel.validate() && assignedPersonnel.validate() && reasonForRequest.validate();

    }

    @FXML
    private void saveData(ActionEvent actionEvent){
        if(validateInput()){

            ArrayList<String> nodeIDS = DB.getListOfNodeIDS();
            String securityLevel = levelOfSecurity.getSelectionModel().getSelectedItem().toString();
            String urgencyLevel = levelOfUrgency.getSelectionModel().getSelectedItem().toString();
            int assignee = Integer.parseInt(assignedPersonnel.getSelectionModel().getSelectedItem());
            String reason = reasonForRequest.getText();
            int nodeIDIndex = locationInput.getSelectionModel().getSelectedIndex();
            String nodeID = nodeIDS.get(nodeIDIndex);
            System.out.println(securityLevel + "" + urgencyLevel + "" + assignee + "" + nodeID);
            DB.addSecurityRequest(App.userID, assignee, nodeID, securityLevel, urgencyLevel);
            super.handleButtonSubmit(actionEvent);
        }
    }

    /**
     * todo This function will cause a pop-up modal to appear with help information for this form's fields
     * @param event {@link ActionEvent} info for the help button call, passed automatically by system.
     */
    @FXML
    void getHelpSecurityService(ActionEvent event) {
        System.out.println(event);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        ObservableList<String> locations = DB.getAllNodeLongNames();
        locationInput.setItems(locations);
        assert helpSecurityService != null : "fx:id=\"helpSecurityService\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert locationInput != null : "fx:id=\"locationOfDelivery\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert levelOfSecurity != null : "fx:id=\"levelOfSecurity\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert levelOfUrgency != null : "fx:id=\"levelOfUrgency\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert reasonForRequest != null : "fx:id=\"reasonForRequest\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert cancelSecurityRequest != null : "fx:id=\"cancelSecurityRequest\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert submitSecurityRequest != null : "fx:id=\"submitSecurityRequest\" was not injected: check your FXML file 'SecurityService.fxml'.";

        exit.setOnMouseClicked(event -> {
            App app = new App();
            app.stop();
        });
    }
}
