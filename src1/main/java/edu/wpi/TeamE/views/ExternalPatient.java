package edu.wpi.TeamE.views;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExternalPatient {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField locationInput;

    @FXML
    private MenuButton requestTypeInput;

    @FXML
    private MenuButton severityInput;

    @FXML
    private TextField patientIdInput;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private TextField ETAInput;

    @FXML
    private Button submit; //todo calls handleButton for now, will need to actually submit in future iterations

    @FXML
    private Button cancel; //cals handleButton

    /**
     * todo This function will cause a pop-up modal to appear with help information for this form's fields
     * @param event
     */
    @FXML
    void getHelpExternalPatient(ActionEvent event) {

    }

    /**
     * Returns to default page
     * @param event
     */
    @FXML
    void handleButton(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sets value of drop down list to the selected value for "Request Type" field
     * @param event
     */
    @FXML
    void selectRequestType(ActionEvent event) {
        String selected = ((MenuItem) event.getSource()).getText();
        requestTypeInput.setText(selected);
    }

    /**
     * Sets value of drop down list to the selected value for "Severity Type" field
     * @param event
     */
    @FXML
    void selectSeverityType(ActionEvent event) {
        String selected = ((MenuItem) event.getSource()).getText();
        severityInput.setText(selected);
    }

    @FXML
    void initialize() {
        assert locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert requestTypeInput != null : "fx:id=\"requestTypeInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert severityInput != null : "fx:id=\"severityInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert patientIdInput != null : "fx:id=\"patientIdInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert ETAInput != null : "fx:id=\"ETAInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'ExternalPatient.fxml'.";

    }
}
