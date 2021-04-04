package edu.wpi.teamname.views;

import edu.wpi.teamname.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;

public class MedicineDelivery {

    @FXML
    private TextField roomNumInput;
    @FXML private TextField medicineNameInput;
    @FXML private TextField doseQuantityInput;
    @FXML private TextField doseMeasureInput;
    @FXML private TextArea specialInstructInput;
    @FXML private TextField signatureInput;

    @FXML private MenuButton departmentInput;


    @FXML
    private void handleButton(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void selectDepartment(ActionEvent actionEvent) {
        String department = ((MenuItem) actionEvent.getSource()).getText();
        departmentInput.setText(department);
    }
}
