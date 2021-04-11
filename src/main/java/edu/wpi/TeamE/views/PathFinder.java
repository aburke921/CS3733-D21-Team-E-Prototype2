package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.TeamE.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class PathFinder {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="startLocationList"
    private JFXComboBox<String> startLocationList; // Value injected by FXMLLoader

    @FXML // fx:id="bathroomList"
    private JFXComboBox<String> endLocationList; // Value injected by FXMLLoader

    @FXML
    private void toDefault(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void getHelpDefault(ActionEvent event) {

    }

    @FXML
    void selectStartNode(ActionEvent event) {
        String nodeID = ((JFXComboBox) event.getSource()).getValue().toString();

        /*
        String longName = getLongName(nodeID); //Given by database
        startLocationList.setValue(longName);
        */

    }

    @FXML
    void selectEndNode(ActionEvent event) {
        String nodeID = ((JFXComboBox) event.getSource()).getValue().toString();
        /*
        String longName = getLongName(nodeID); //Given by database
        endLocationList.setValue(longName);
        */
    }

    @FXML
    public void findPath(ActionEvent actionEvent) {
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert startLocationList != null : "fx:id=\"bathroomList\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert endLocationList != null : "fx:id=\"bathroomList\" was not injected: check your FXML file 'PathFinder.fxml'.";

        ObservableList<String> list = FXCollections.observableArrayList("eEXIT00101","eEXIT00201","ePARK00101","ePARK00201", "ePARK00301"); //todo pull item list from CSV?
        startLocationList.setItems(list);
        endLocationList.setItems(list);
    }
}
