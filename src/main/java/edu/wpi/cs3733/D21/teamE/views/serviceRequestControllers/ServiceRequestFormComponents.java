package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.states.CreateAccountState;
import edu.wpi.cs3733.D21.teamE.states.RequestFormState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import edu.wpi.cs3733.D21.teamE.email.*;
import java.io.IOException;

public abstract class ServiceRequestFormComponents {

    /**
     * Returns to the service request page
     * @param event {@link ActionEvent} info for the cancel button call, passed automatically by system.
     */
    @FXML
    void handleButtonCancel(ActionEvent event) {
        RequestFormState requestFormState = new RequestFormState();
        requestFormState.switchScene(event);
    }

    /**
     * Returns to default page
     * todo, in future iterations this button should SUBMIT instead
     * @param event {@link ActionEvent} info for the submit button call, passed automatically by system.
     */
    @FXML
    void handleButtonSubmit(ActionEvent event) {
        RequestFormState requestFormState = new RequestFormState();
        requestFormState.switchScene(event);
    }
}
