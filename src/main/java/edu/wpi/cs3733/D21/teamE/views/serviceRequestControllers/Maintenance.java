package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.lang.String;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Maintenance extends ServiceRequestFormComponents {


    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();
    ObservableList<String> names;
    ArrayList<Integer> userID = new ArrayList<>();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="fullscreen"
    private Rectangle fullscreen; // Value injected by FXMLLoader

    @FXML // fx:id="hide"
    private Circle hide; // Value injected by FXMLLoader

    @FXML // fx:id="exit"
    private Polygon exit; // Value injected by FXMLLoader

    @FXML // fx:id="locationInput"
    private JFXComboBox<String> locationInput; // Value injected by FXMLLoader

    @FXML // fx:id="requestTypeInput"
    private JFXComboBox<String> requestTypeInput; // Value injected by FXMLLoader

    @FXML // fx:id="severityInput"
    private JFXComboBox<String> severityInput; // Value injected by FXMLLoader

    @FXML // fx:id="assignedPersonnelInput"
    private JFXComboBox<String> assignedPersonnelInput; // Value injected by FXMLLoader

    @FXML // fx:id="authorInput"
    private JFXTextField authorInput; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionInput"
    private JFXTextArea descriptionInput; // Value injected by FXMLLoader

    @FXML // fx:id="ETAInput"
    private JFXTextField ETAInput; // Value injected by FXMLLoader

    @FXML // fx:id="cancel"
    private JFXButton cancel; // Value injected by FXMLLoader

    @FXML // fx:id="submit"
    private JFXButton submit; // Value injected by FXMLLoader

    @FXML
    void handleButtonCancel(ActionEvent event) {
        super.handleButtonCancel(event);
    }

    @FXML
    void saveData(ActionEvent event) {
        super.handleButtonSubmit(event);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        locations = DB.getAllNodeLongNames();
        nodeID = DB.getListOfNodeIDS();
        //TODO add user type
        names = DB.getAssigneeNames("Add user type here");
        userID = DB.getAssigneeIDs("Add user type here");

        locationInput.setItems(locations);
        assignedPersonnelInput.setItems(names);

        assert fullscreen != null : "fx:id=\"fullscreen\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert hide != null : "fx:id=\"hide\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert requestTypeInput != null : "fx:id=\"requestTypeInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert severityInput != null : "fx:id=\"severityInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert assignedPersonnelInput != null : "fx:id=\"assignedPersonnelInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert authorInput != null : "fx:id=\"authorInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert ETAInput != null : "fx:id=\"ETAInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";

    }

}
