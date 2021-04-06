package edu.wpi.TeamE.views;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;

public class SecurityService {

    @FXML
    private TextArea locationForSecurityService;

    @FXML
    private Button helpButton;

    @FXML
    private CheckBox lowSecurityLevel;

    @FXML
    private CheckBox mediumSecurityLevel;

    @FXML
    private CheckBox highSecurityLevel;

    @FXML
    private CheckBox urgent;

    @FXML
    private CheckBox nonUrgent;

    @FXML
    private TextArea descriptionForSecurityService;

    @FXML
    private Button submitForSecurityService;

    @FXML
    private Button cancelSecurityServiceRequest;

    private void toDefault(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamname/views/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void securityServiceHelp(ActionEvent anEvent) {

    }

    public void highSecurity() {
        this.highSecurityLevel.setSelected(true);
    }

    public void mediumSecurity() {
        this.mediumSecurityLevel.setSelected(true);
    }

    public void lowSecurity() {
        this.lowSecurityLevel.setSelected(true);
    }

    public void urgentSelected() {
        this.urgent.setSelected(true);
    }

    public void nonUrgentSelected() {
        this.nonUrgent.setSelected(true);
    }

    public void submitForm(ActionEvent anAction) {
        toDefault(anAction);
    }

    public void cancelForm(ActionEvent anAction) {
        toDefault(anAction);
    }


}
