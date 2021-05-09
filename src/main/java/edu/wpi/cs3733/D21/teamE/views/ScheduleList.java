package edu.wpi.cs3733.D21.teamE.views;

import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.states.DefaultState;
import edu.wpi.cs3733.D21.teamE.states.ToDoState;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers.ToDoDetails;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ScheduleList {

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setPageTitle("User Management"); //set AppBar title
            App.setShowHelp(false);
            App.setShowLogin(true);
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's sideBarVBox element
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editToDo (ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/ToDoDetails.fxml"));
            ToDoDetails controller = loader.getController();
            Stage primaryStage =  App.getPrimaryStage();
            primaryStage.setScene(new Scene(loader.load()));
            //controller.initToDo();
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void addToDo (ActionEvent event) {
        try {
            System.out.println("here");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/ToDoDetails.fxml"));
            ToDoDetails controller = loader.getController();
            Stage primaryStage =  App.getPrimaryStage();
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void switchScene(ActionEvent event) {
        ToDoState toDoState = new ToDoState();
        toDoState.switchScene(event);
    }

    @FXML
    private void changeDate(ActionEvent event) {

    }

    @FXML
    private void markComplete(ActionEvent event) {

    }

}
