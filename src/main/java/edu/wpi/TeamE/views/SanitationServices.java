package edu.wpi.TeamE.views;

import edu.wpi.TeamE.App;
import edu.wpi.TeamE.databases.makeConnection;
import edu.wpi.TeamE.views.forms.SanitationServicesForm;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;



public class SanitationServices extends ServiceRequestFormComponents {

  @FXML private TextField numInput;
  @FXML private TextField assignedIndividual;
  @FXML private TextArea sdetailedInstructionsInput;
  @FXML private TextField signatureInput;

  @FXML private MenuButton departmentInput;
  @FXML private MenuButton typeInput;
  @FXML private MenuButton ServiceTypeinput;
  @FXML private Button submit;
  public String assigneeName;
  public String number;
  public String  LocationType;
  public String  serviceType;
  public String department;
  public String signature;
  public String detailedDescription;



  /**
   * Sets value of drop down list to the selected value(departments)
   * @param actionEvent
   */
  @FXML
  private void selectDepartment(ActionEvent actionEvent) {
    String department = ((MenuItem) actionEvent.getSource()).getText();
    this.department = department;
    departmentInput.setText(department);
  }
  /**
   * Sets value of drop down list to the selected value(Service Types)
   * @param actionEvent
   */
  @FXML
  private void selectServiceType(ActionEvent actionEvent) {
    String service = ((MenuItem) actionEvent.getSource()).getText();
    serviceType = service;
    ServiceTypeinput.setText(service);
  }
  /**
   * Sets value of drop down list to the selected value(Room/Hallway)
   * @param actionEvent
   */
  @FXML
  private void SelectType(ActionEvent actionEvent) {
    String type = ((MenuItem) actionEvent.getSource()).getText();
    LocationType = type;
    typeInput.setText(type);
  }
  /**
   * records inputs from user into a series of String variables and returns to the main page
   * @param actionEvent
   */
  @FXML
  private void saveData(ActionEvent actionEvent){

    //Setting up all variables to be entered
    String dep = departmentInput.getText();
    String room = typeInput.getText();
    String num = numInput.getText();
    String serviceKind = ServiceTypeinput.getText();
    String assignee = assignedIndividual.getText();
    //String detailedInstructions = sdetailedInstructionsInput.getText();
    //creating the service request
    SanitationServicesForm request = new SanitationServicesForm(dep, room, num, serviceKind, assignee);

    System.out.println(request.getAssignmentField());
    //Adding service request to table
    //makeConnection connection = makeConnection.makeConnection();
    //connection.addRequest("sanitationServices", request);

    super.handleButtonSubmit(actionEvent);

  }

  @FXML
  void handleButtonSubmit(ActionEvent event) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}