package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;


import javafx.event.ActionEvent;

import java.util.ArrayList;

public class ReligiousRequest extends ServiceRequestFormComponents {


    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();
    ObservableList<String> names;
    ArrayList<Integer> userID = new ArrayList<>();

    @FXML
    private JFXComboBox<String> locationInput;

    @FXML
    private JFXComboBox<String> religionInput;

    @FXML
    private JFXTextField assignedPersonnel;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXButton submit;

    @FXML
    private JFXButton cancel;

    @FXML
    void handleButtonSubmit(ActionEvent event) {
        super.handleButtonSubmit(event);
    }

    @FXML
    void handleButtonCancel(ActionEvent event) {
        super.handleButtonCancel(event);
    }

    @FXML
    void initialize() {
        locations = DB.getAllNodeLongNames();
        nodeID = DB.getListOfNodeIDS();
        //TODO add user type
        names = DB.getAssigneeNames("Add user type here");
        userID = DB.getAssigneeIDs("Add user type here");
    }

}
