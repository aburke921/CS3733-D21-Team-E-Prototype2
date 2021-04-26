package edu.wpi.TeamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class InternalPatient extends ServiceRequestFormComponents{
    @FXML
    private JFXComboBox pickupInput;
    @FXML
    private JFXComboBox dropoffInput;
    @FXML
    private JFXTextField departmentInput;
    @FXML
    private JFXComboBox severityInput;
    @FXML
    private JFXTextField patientIdInput;
    @FXML
    private JFXTextField assignedPersonnel;
    @FXML
    private JFXTextArea descriptionInput;
    @FXML
    private JFXButton cancel;
    @FXML
    private JFXButton submit;

    public void saveData(ActionEvent actionEvent) {
    }
}
