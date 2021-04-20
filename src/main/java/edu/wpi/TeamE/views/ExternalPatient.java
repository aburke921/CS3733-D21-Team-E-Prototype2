package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.lang.String;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.TeamE.App;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.shape.Polygon;

public class ExternalPatient extends ServiceRequestFormComponents  {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="locationInput"
    private JFXTextField locationInput; // Value injected by FXMLLoader

    @FXML // fx:id="requestTypeInput"
    private JFXComboBox<?> requestTypeInput; // Value injected by FXMLLoader

    @FXML // fx:id="ambulance"
    private String ambulance; // Value injected by FXMLLoader

    @FXML // fx:id="helicopter"
    private String helicopter; // Value injected by FXMLLoader

    @FXML // fx:id="plane"
    private String plane; // Value injected by FXMLLoader

    @FXML // fx:id="severityInput"
    private JFXComboBox<?> severityInput; // Value injected by FXMLLoader

    @FXML // fx:id="high_severity"
    private String high_severity; // Value injected by FXMLLoader

    @FXML // fx:id="medium_severity"
    private String medium_severity; // Value injected by FXMLLoader

    @FXML // fx:id="low_severity"
    private String low_severity; // Value injected by FXMLLoader

    @FXML // fx:id="patientIdInput"
    private JFXTextField patientIdInput; // Value injected by FXMLLoader

    @FXML // fx:id="assignedPersonnel"
    private JFXTextField assignedPersonnel; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionInput"
    private JFXTextArea descriptionInput; // Value injected by FXMLLoader

    @FXML // fx:id="ETAInput"
    private JFXTextField ETAInput; // Value injected by FXMLLoader

    @FXML // fx:id="cancel"
    private JFXButton cancel; // Value injected by FXMLLoader

    @FXML // fx:id="submit"
    private JFXButton submit; // Value injected by FXMLLoader

    @FXML // fx:id="exit"
    private Polygon exit;

    makeConnection connection = makeConnection.makeConnection();
    RequiredFieldValidator validator = new RequiredFieldValidator();
    /**
     * todo This function will cause a pop-up modal to appear with help information for this form's fields
     * @param event {@link ActionEvent} info for the help button call, passed automatically by system.
     */
    @FXML
    void getHelpExternalPatient(ActionEvent event) {

    }
    /**
     * Detects if the user has entered all required fields
     *
     */
    private boolean validateInput(){

        validator.setMessage("Input required");


        locationInput.getValidators().add(validator);
        requestTypeInput.getValidators().add(validator);
        severityInput.getValidators().add(validator);
        patientIdInput.getValidators().add(validator);
        ETAInput.getValidators().add(validator);
        descriptionInput.getValidators().add(validator);
        assignedPersonnel.getValidators().add(validator);

        return  locationInput.validate() && requestTypeInput.validate() && severityInput.validate() && patientIdInput.validate() && assignedPersonnel.validate() && descriptionInput.validate() && ETAInput.validate();


    }
    /**
     * records inputs from user into a series of String variables and returns to the main page
     * @param actionEvent
     */
    @FXML
    private void saveData(ActionEvent actionEvent){


        if(validateInput()){
            //String detailedInstructions = sdetailedInstructionsInput.getText();
            //creating the service request

            //System.out.println(request.getAssignmentField());
            //Adding service request to table
            //makeConnection connection = makeConnection.makeConnection();
            //connection.addRequest("sanitationServices", request);
            String location = locationInput.getText();
            String type = requestTypeInput.getSelectionModel().toString();
            String severity = severityInput.getSelectionModel().toString();
            String patientID = patientIdInput.getText();
            String ETA = ETAInput.getText();
            String details = descriptionInput.getText();
            String assignee = assignedPersonnel.getText();

            //connection.addExternalPatientRequest(15, location,severity,patientID,ETA,details);
            super.handleButtonSubmit(actionEvent);
            //Setting up all variables to be entered
        }
    }
    @FXML // This method is called by the FXMLLoader when initialization is complete

    void initialize() {
        assert locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert requestTypeInput != null : "fx:id=\"requestTypeInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert ambulance != null : "fx:id=\"ambulance\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert helicopter != null : "fx:id=\"helicopter\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert plane != null : "fx:id=\"plane\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert severityInput != null : "fx:id=\"severityInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert high_severity != null : "fx:id=\"high_severity\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert medium_severity != null : "fx:id=\"medium_severity\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert low_severity != null : "fx:id=\"low_severity\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert patientIdInput != null : "fx:id=\"patientIdInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert ETAInput != null : "fx:id=\"ETAInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert assignedPersonnel != null : "fx:id=\"assignedPersonnel\" was not injected: check your FXML file 'ExternalPatient.fxml'.";

        exit.setOnMouseClicked(event -> {
            App app = new App();
            app.stop();
        });

    }
}
