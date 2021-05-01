package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
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

    private boolean validateInput() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Input required");

        pickupInput.getValidators().add(validator);
        dropoffInput.getValidators().add(validator);
        departmentInput.getValidators().add(validator);
        severityInput.getValidators().add(validator);
        patientIdInput.getValidators().add(validator);
        assignedPersonnel.getValidators().add(validator);
        descriptionInput.getValidators().add(validator);

        return pickupInput.validate() && dropoffInput.validate() && departmentInput.validate()
                && severityInput.validate() && patientIdInput.validate() && assignedPersonnel.validate()
                && descriptionInput.validate();
    }

    @FXML
    public void saveData(ActionEvent event) {

        if(validateInput()) {
            int pickupIndex = pickupInput.getSelectionModel().getSelectedIndex();
            int dropOffIndex = dropoffInput.getSelectionModel().getSelectedIndex();
            int userIndex = assignedPersonnel.getSelectionModel().getSelectedIndex();
            String pickup = nodeID.get(pickupIndex);
            String dropOff = nodeID.get(dropOffIndex);
            int assign = userID.get(userIndex);
            String dept = departmentInput.getText();
            String sever = severityInput.getSelectionModel().getSelectedItem();
            String patient = patientIdInput.getText();
            int patientID = 0;
            String desc = descriptionInput.getText();

            DB.addInternalPatientRequest(App.userID,pickup,dropOff,assign, patientID, dept, sever, desc);
        }

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
