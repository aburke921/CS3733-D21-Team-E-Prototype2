package edu.wpi.TeamE.views;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;

public class SecurityService extends ServiceRequestFormComponents {

    @FXML
    private Button helpSecurityService;

    @FXML
    private TextField locationOfDelivery;

    @FXML
    private MenuButton levelOfSecurity;

    @FXML
    private MenuButton levelOfUrgency;

    @FXML
    private TextArea reasonForRequest;

    @FXML
    private Button submitSecurityRequest;

    @FXML
    private Button cancelSecurityRequest;

    @FXML
    public void helpSecurityService(ActionEvent event) {

    }

    @FXML
    public void selectSecurityLevel(ActionEvent action) {
        String level =((MenuItem) action.getSource()).getText();
        levelOfSecurity.setText(level);
    }

    @FXML
    public void selectUrgency(ActionEvent action) {
        String urgency =((MenuItem) action.getSource()).getText();
        levelOfUrgency.setText(urgency);
    }



    @FXML
    void initialize() {
        assert helpSecurityService != null : "fx:id=\"helpSecurityService\" was not injected";
        assert locationOfDelivery != null : "fx:id=\"locationOfDelivery\" was not injected";
        assert levelOfSecurity != null : "fx:id=\"levelOfSecurity\" was not injected";
        assert levelOfUrgency != null : "fx:id=\"levelOfUrgency\" was not injected";
        assert reasonForRequest != null : "fx:id=\"reasonForRequest\" was not injected";
        assert submitSecurityRequest != null : "fx:id=\"submitSecurityRequest\" was not injected";
        assert cancelSecurityRequest != null : "fx:id=\"cancelSecurityRequest\" was not injected";

    }

}