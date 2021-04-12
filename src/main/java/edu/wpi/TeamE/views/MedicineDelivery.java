package edu.wpi.TeamE.views;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;

public class MedicineDelivery extends ServiceRequestFormComponents {

    @FXML
    private TextField roomNumInput;
    @FXML private TextField medicineNameInput;
    @FXML private TextField doseQuantityInput;
    @FXML private TextField doseMeasureInput;
    @FXML private TextArea specialInstructInput;
    @FXML private TextField signatureInput;
    @FXML private MenuButton departmentInput;


    /**
     * Sets value of drop down list to the selected value
     * @param actionEvent
     */
    @FXML
    private void selectDepartment(ActionEvent actionEvent) {
        String department = ((MenuItem) actionEvent.getSource()).getText();
        departmentInput.setText(department);
    }
}
