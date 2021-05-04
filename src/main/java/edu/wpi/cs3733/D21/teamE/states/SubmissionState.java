package edu.wpi.cs3733.D21.teamE.states;

import edu.wpi.cs3733.D21.teamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class SubmissionState {

    public void switchScene(ActionEvent event) {

        String buttonName = ((Button) event.getSource()).getId();

        if(buttonName.equals("statusButton")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ServiceRequestStatus.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("requestButton")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ServiceRequests.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("defaultButton")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
