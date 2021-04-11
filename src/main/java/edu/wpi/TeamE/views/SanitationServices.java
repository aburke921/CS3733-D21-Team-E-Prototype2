package edu.wpi.TeamE.views;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;

public class SanitationServices extends ServiceRequestFormComponents {


  @FXML
  private TextField roomNumInput;
  @FXML private TextField mnumInput;
  @FXML private TextField mess;
  @FXML private TextField doseMeasureInput;
  @FXML private TextArea sdetailedInstructionsInput;
  @FXML private TextField signatureInput;

  @FXML private MenuButton departmentInput;
  @FXML private MenuButton typeInput;
  @FXML private MenuButton ServiceTypeinput;

  /**
   * Sets value of drop down list to the selected value(departments)
   * @param actionEvent
   */
  @FXML
  private void selectDepartment(ActionEvent actionEvent) {
    String department = ((MenuItem) actionEvent.getSource()).getText();
    departmentInput.setText(department);
  }
  /**
   * Sets value of drop down list to the selected value(Service Types)
   * @param actionEvent
   */
  @FXML
  private void selectServiceType(ActionEvent actionEvent) {
    String service = ((MenuItem) actionEvent.getSource()).getText();
    ServiceTypeinput.setText(service);
  }
  /**
   * Sets value of drop down list to the selected value(Room/Hallway)
   * @param actionEvent
   */
  @FXML
  private void SelectType(ActionEvent actionEvent) {
    String type = ((MenuItem) actionEvent.getSource()).getText();
    typeInput.setText(type);
  }
}