package edu.wpi.TeamE.views;
import com.jfoenix.controls.*;
import edu.wpi.TeamE.App;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;


import java.io.IOException;

public class CovidSurvey extends ServiceRequests  {

    @FXML private AnchorPane appBarAnchorPane;
    @FXML private StackPane stackPane;
    @FXML JFXCheckBox positiveTest;
    @FXML JFXCheckBox symptoms;
    @FXML JFXCheckBox closeContact;
    @FXML JFXCheckBox quarantine;
    @FXML JFXCheckBox noSymptoms;



    @FXML
    void submitButton(ActionEvent actionEvent){
        boolean safe = !(positiveTest.isSelected() || symptoms.isSelected() || closeContact.isSelected() || quarantine.isSelected()) && noSymptoms.isSelected();
        int rating = 0;
        if(noSymptoms.isSelected()){

            rating = 1;
            System.out.print(rating);

        }else {
            if(symptoms.isSelected()){
            rating = 2;
            }if(closeContact.isSelected()){
                rating = 3;
            }if(quarantine.isSelected()){
                rating = 4;
            }if(positiveTest.isSelected()){
                rating = 5;
            }
        }
        System.out.print(rating);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Returns to the service request page
     * @param event {@link ActionEvent} info for the cancel button call, passed automatically by system.
     */
    @FXML
    void handleButtonCancel(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize() {

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowLogin(true);
            App.setShowHelp(true);
            App.setPageTitle("Covid Survey"); //set AppBar title
            App.setHelpText(""); //set help text todo, fill in this field
            App.setStackPane(stackPane); // required for dialog boxes
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert  positiveTest  != null : "fx:id=\"positiveTest\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert  symptoms  != null : "fx:id=\"symptoms\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert closeContact  != null : "fx:id=\"closeContact\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert  quarantine != null : "fx:id=\"quarantine\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert  noSymptoms  != null : "fx:id=\"noSymptoms\" was not injected: check your FXML file 'PathFinder.fxml'.";
    }
}
