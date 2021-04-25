package edu.wpi.TeamE.views;
import com.jfoenix.controls.*;
import edu.wpi.TeamE.App;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


import java.io.IOException;

public class CovidSurvey extends ServiceRequests  {
    @FXML JFXCheckBox positiveTest;
    @FXML JFXCheckBox symptoms;
    @FXML JFXCheckBox closeContact;
    @FXML JFXCheckBox quarantine;
    @FXML JFXCheckBox noSymptoms;



    @FXML
    void submitButton(ActionEvent actionEvent){
        boolean safe = !(positiveTest.isSelected() || symptoms.isSelected() || closeContact.isSelected() || quarantine.isSelected()) && noSymptoms.isSelected();
        if(safe){

            System.out.println("Yay no COVID");
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
                App.setDraggableAndChangeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else{
            System.out.println("Oh no maybe COVID so sad =(");
        }
    }

    @FXML
    public void initialize() {
        assert  positiveTest  != null : "fx:id=\"positiveTest\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert  symptoms  != null : "fx:id=\"symptoms\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert closeContact  != null : "fx:id=\"closeContact\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert  quarantine != null : "fx:id=\"quarantine\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert  noSymptoms  != null : "fx:id=\"noSymptoms\" was not injected: check your FXML file 'PathFinder.fxml'.";
    }
}
