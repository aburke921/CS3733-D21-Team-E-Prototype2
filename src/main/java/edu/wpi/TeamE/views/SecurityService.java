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
    public void toDefault(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamname/views/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}