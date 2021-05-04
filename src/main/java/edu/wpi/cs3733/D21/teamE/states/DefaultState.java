package edu.wpi.cs3733.D21.teamE.states;

import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.views.ChangeScene;
import edu.wpi.cs3733.D21.teamE.views.CovidSurvey;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class DefaultState {

    public void switchScene(ActionEvent event) {

        String buttonName = ((Button) event.getSource()).getId();

        switch (buttonName) {
            case "covidSurvey":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/CovidSurvey.fxml"));
                    App.changeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "pathFinderButton":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/PathFinder.fxml"));
                    App.changeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "serviceRequestButton":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ServiceRequests.fxml"));
                    App.changeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "mapEditorButton":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/newMapEditor.fxml"));
                    App.changeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "userManagementButton":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/UserManagement.fxml"));
                    App.changeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "directions":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Directions.fxml"));
                    App.changeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "scheduleAppointmentButton":
                try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/Appointment.fxml"));
                App.changeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "covidSurveyStatusButton":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/CovidSurveyStatus.fxml"));
                    App.changeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            }
        }
    }