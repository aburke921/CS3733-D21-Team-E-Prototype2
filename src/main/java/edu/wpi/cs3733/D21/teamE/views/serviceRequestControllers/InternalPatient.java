package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.ArrayList;

public class InternalPatient extends ServiceRequestFormComponents{

    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();
    ObservableList<String> names;
    ArrayList<Integer> userID = new ArrayList<>();

    @FXML
    private JFXComboBox<String> pickupInput;
    @FXML
    private JFXComboBox<String> dropoffInput;
    @FXML
    private JFXTextField departmentInput;
    @FXML
    private JFXComboBox<String> severityInput;
    @FXML
    private JFXTextField patientIdInput;
    @FXML
    private JFXComboBox<String> assignedPersonnel;
    @FXML
    private JFXTextArea descriptionInput;
    @FXML
    private JFXButton cancel;
    @FXML
    private JFXButton submit;

    public void saveData(ActionEvent actionEvent) {


    }

    @FXML
    void initialize() {
        locations = DB.getAllNodeLongNames();
        nodeID = DB.getListOfNodeIDS();
        //TODO add user type
        names = DB.getAssigneeNames("Add user type here");
        userID = DB.getAssigneeIDs("Add user type here");

        pickupInput.setItems(locations);
        dropoffInput.setItems(locations);
        assignedPersonnel.setItems(names);
    }
}
