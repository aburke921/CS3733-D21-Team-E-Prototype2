package edu.wpi.TeamE.views.serviceRequestControllers;

import edu.wpi.TeamE.views.serviceRequestControllers.ServiceRequestFormComponents;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
