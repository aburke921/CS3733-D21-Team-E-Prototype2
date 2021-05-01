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

        String buttonName = ((Button) event.getSource()).getText();

        if(buttonName.equals("COVID Survey")){
            if (App.userID != 0){
                if (DB.filledCovidSurveyToday(App.userID) && DB.isUserCovidSafe(App.userID)) { // go to pathfinder
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/PathFinder.fxml"));
                        App.setDraggableAndChangeScene(root);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else { // go to covid survey
                    CovidSurvey.plzGoToPathFinder = true;
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/CovidSurvey.fxml"));
                        App.getPrimaryStage().getScene().setRoot(root);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (App.noCleanSurveyYet) { // go to covid survey
                CovidSurvey.plzGoToPathFinder = true;
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/CovidSurvey.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else { // go to pathfinder
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/PathFinder.fxml"));
                    App.setDraggableAndChangeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        else if(buttonName.equals("Path Finder")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/PathFinder.fxml"));
                App.setDraggableAndChangeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Service Requests")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ServiceRequests.fxml"));
                App.setDraggableAndChangeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Map Editor")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/newMapEditor.fxml"));
                App.setDraggableAndChangeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("User Management")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/UserManagement.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
