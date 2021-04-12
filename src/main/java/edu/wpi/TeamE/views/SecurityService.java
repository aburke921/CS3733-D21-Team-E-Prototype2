package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.lang.String;
import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class SecurityService extends ServiceRequestFormComponents {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="helpSecurityService"
    private Button helpSecurityService; // Value injected by FXMLLoader

    @FXML // fx:id="locationOfDelivery"
    private JFXTextField locationOfDelivery; // Value injected by FXMLLoader

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

    @FXML // fx:id="reasonForRequest"
    private JFXTextArea reasonForRequest; // Value injected by FXMLLoader

    @FXML // fx:id="cancelSecurityRequest"
    private JFXButton cancelSecurityRequest; // Value injected by FXMLLoader

    @FXML // fx:id="submitSecurityRequest"
    private JFXButton submitSecurityRequest; // Value injected by FXMLLoader

    /**
     * Returns to default page
     * todo, in future iterations this button should SUBMIT instead
     * @param event {@link ActionEvent} info for the submit button call, passed automatically by system.
     */
    @FXML
    void handleButtonSubmit(ActionEvent event) {
        try {
            System.out.println(event); //Print the ActionEvent to console
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/ServiceRequests.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
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
        assert helpSecurityService != null : "fx:id=\"helpSecurityService\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert locationOfDelivery != null : "fx:id=\"locationOfDelivery\" was not injected: check your FXML file 'SecurityService.fxml'.";
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

    }
}
