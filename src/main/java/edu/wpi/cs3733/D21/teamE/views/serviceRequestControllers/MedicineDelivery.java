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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.ArrayList;

public class MedicineDelivery extends ServiceRequestFormComponents {

    ObservableList<String> locations;
    ArrayList<String> nodeIDs;

    @FXML
    private JFXComboBox<String> locationInput;

    @FXML
    private JFXTextField medicineNameInput;

    @FXML
    private JFXTextField doseQuantityInput;

    @FXML
    private JFXTextField doseMeasureInput;

    @FXML
    private JFXTextField assignee;

    @FXML
    private JFXTextArea specialInstructInput;

    @FXML
    private JFXTextField signatureInput;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton submit;

    private boolean validateInput() {

        RequiredFieldValidator validator = new RequiredFieldValidator();

        validator.setMessage("Input required");

        locationInput.getValidators().add(validator);
        medicineNameInput.getValidators().add(validator);
        doseMeasureInput.getValidators().add(validator);
        doseQuantityInput.getValidators().add(validator);
        assignee.getValidators().add(validator);
        specialInstructInput.getValidators().add(validator);
        signatureInput.getValidators().add(validator);

        return  locationInput.validate() && medicineNameInput.validate() && doseMeasureInput.validate() && doseQuantityInput.validate() && assignee.validate() && specialInstructInput.validate() && signatureInput.validate();

    }

    @FXML
    private void saveData(ActionEvent e) {

        int index = locationInput.getSelectionModel().getSelectedIndex();

        String location = nodeIDs.get(index);
        String name = medicineNameInput.getText();
        String doseMeasure = doseMeasureInput.getText();
        int doseQuantity = Integer.parseInt(doseQuantityInput.getText());
        int assigned = Integer.parseInt( assignee.getText());
        String specialInstructions = specialInstructInput.getText();
        String signature = signatureInput.getText();

        DB.addMedicineRequest(App.userID, assigned, location, name, doseQuantity, doseMeasure, specialInstructions, signature);
    }

    @FXML
    void handleButtonSubmit(ActionEvent event) {
        if (validateInput()) {
            try {
                saveData(event);
                System.out.println(event); //Print the ActionEvent to console
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() {

        locations = DB.getAllNodeLongNames();
        nodeIDs = DB.getListOfNodeIDS();

        locationInput.setItems(locations);

        assert locationInput != null;
        assert medicineNameInput != null;
        assert doseQuantityInput != null;
        assert doseMeasureInput != null;
        assert specialInstructInput != null;
        assert signatureInput != null;
        assert submit != null;
        assert cancel != null;
    }
}
