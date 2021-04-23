package edu.wpi.TeamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.lang.String;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.TeamE.App;
import edu.wpi.TeamE.databases.NodeDB;
import edu.wpi.TeamE.databases.RequestsDB;
import edu.wpi.TeamE.databases.makeConnection;
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
    private JFXComboBox<?> levelOfSecurity; // Value injected by FXMLLoader

        @FXML // fx:id="low"
        private String low; // Value injected by FXMLLoader

        @FXML // fx:id="medium"
        private String medium; // Value injected by FXMLLoader

        @FXML // fx:id="high"
        private String high; // Value injected by FXMLLoader

    @FXML // fx:id="levelOfUrgency"
    private JFXComboBox<?> levelOfUrgency; // Value injected by FXMLLoader

        @FXML // fx:id="urgent"
        private String urgent; // Value injected by FXMLLoader

        @FXML // fx:id="nonurgent"
        private String nonurgent; // Value injected by FXMLLoader

    @FXML // fx:id = "assignedPersonnel"
    JFXTextField assignedPersonnel;

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
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
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
        makeConnection connection = makeConnection.makeConnection();


        if(validateInput()){

            ArrayList<String> nodeIDS = NodeDB.getListOfNodeIDS();
            String securityLevel = levelOfSecurity.getSelectionModel().getSelectedItem().toString();
            String urgencyLevel = levelOfUrgency.getSelectionModel().getSelectedItem().toString();
            String assignee = assignedPersonnel.getText();
            String reason = reasonForRequest.getText();
            int nodeIDIndex = locationInput.getSelectionModel().getSelectedIndex();
            String nodeID = nodeIDS.get(nodeIDIndex);
            System.out.println(securityLevel + "" + urgencyLevel + "" + assignee + "" + nodeID);
            RequestsDB.addSecurityRequest(App.userID, assignee, nodeID, securityLevel, urgencyLevel);
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
        //makeConnection connection = makeConnection.makeConnection();
        ObservableList<String> locations = NodeDB.getAllNodeLongNames();
        locationInput.setItems(locations);
        assert helpSecurityService != null : "fx:id=\"helpSecurityService\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert locationInput != null : "fx:id=\"locationOfDelivery\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert levelOfSecurity != null : "fx:id=\"levelOfSecurity\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert low != null : "fx:id=\"low\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert medium != null : "fx:id=\"medium\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert high != null : "fx:id=\"high\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert levelOfUrgency != null : "fx:id=\"levelOfUrgency\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert urgent != null : "fx:id=\"urgent\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert nonurgent != null : "fx:id=\"nonurgent\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert reasonForRequest != null : "fx:id=\"reasonForRequest\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert cancelSecurityRequest != null : "fx:id=\"cancelSecurityRequest\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert submitSecurityRequest != null : "fx:id=\"submitSecurityRequest\" was not injected: check your FXML file 'SecurityService.fxml'.";

        exit.setOnMouseClicked(event -> {
            App app = new App();
            app.stop();
        });
    }
}
