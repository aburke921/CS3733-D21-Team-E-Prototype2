package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import edu.wpi.cs3733.D21.teamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public abstract class ServiceRequestFormComponents {

    /**
     * Returns to the service request page
     * @param event {@link ActionEvent} info for the cancel button call, passed automatically by system.
     */
    @FXML
    void handleButtonCancel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ServiceRequests.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns to default page
     * todo, in future iterations this button should SUBMIT instead
     * @param event {@link ActionEvent} info for the submit button call, passed automatically by system.
     */
    @FXML
    void handleButtonSubmit(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
