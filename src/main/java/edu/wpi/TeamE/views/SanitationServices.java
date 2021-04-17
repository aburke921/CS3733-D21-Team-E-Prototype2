package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.TeamE.App;
import edu.wpi.TeamE.databases.makeConnection;
import edu.wpi.TeamE.views.forms.SanitationServicesForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;



public class SanitationServices extends ServiceRequestFormComponents {

  @FXML private JFXTextField numInput;
  @FXML private JFXTextField assignedIndividual;
  @FXML private JFXTextArea detailedInstructionsInput;


  @FXML private JFXComboBox<String> departmentInput;
  @FXML private JFXComboBox<String> typeInput;
  @FXML private JFXComboBox<String> ServiceTypeinput;
  @FXML private JFXButton cancel;
  @FXML private JFXButton submit;
  public SanitationServicesForm request;



  /**
   * Sets value of drop down list to the selected value(departments)
   * @param actionEvent
   */
  RequiredFieldValidator validator = new RequiredFieldValidator();



  /**
   * Detects if the user has entered all required fields
   *
   */
  private boolean valitadeInput(){
    validator.setMessage("Input required");
    numInput.getValidators().add(validator);
    assignedIndividual.getValidators().add(validator);
    typeInput.getValidators().add(validator);
    departmentInput.getValidators().add(validator);
    ServiceTypeinput.getValidators().add(validator);
    return numInput.validate() && assignedIndividual.validate() && typeInput.validate() && departmentInput.validate() && ServiceTypeinput.validate();


  }


  /**
   * records inputs from user into a series of String variables and returns to the main page
   * @param actionEvent
   */
  @FXML
  private void saveData(ActionEvent actionEvent){

    String dep = departmentInput.getSelectionModel().toString();
    String room = typeInput.getSelectionModel().toString();

    String num = numInput.getText();
    String serviceKind = ServiceTypeinput.getSelectionModel().toString();
    String assignee = assignedIndividual.getText();

    if(valitadeInput()){
      //String detailedInstructions = sdetailedInstructionsInput.getText();
      //creating the service request
      request = new SanitationServicesForm(dep, room, num, serviceKind, assignee);

      System.out.println(request.getAssignmentField());
      //Adding service request to table
      //makeConnection connection = makeConnection.makeConnection();
      //connection.addRequest("sanitationServices", request);

      super.handleButtonSubmit(actionEvent);
      //Setting up all variables to be entered
    }




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
  @FXML
  void initialize(){
    assert  ServiceTypeinput != null : "fx:id=\"typeInput\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/Sanitation.fxml'.";
    ObservableList<String> Type  = FXCollections.observableArrayList();
    Type.setAll("Room","Hallway");

    typeInput.setItems(Type);
    assert ServiceTypeinput != null : "fx:id=\"ServiceTypeinput\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/Sanitation.fxml'.";

    ObservableList<String> Services  = FXCollections.observableArrayList();
    Services.setAll("Urine cleanup","Feces cleanup","Trash removal");

    ServiceTypeinput.setItems(Services);

    assert  ServiceTypeinput != null : "fx:id=\"departmentInput\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/Sanitation.fxml'.";
    ObservableList<String> departments  = FXCollections.observableArrayList();
    departments.setAll("Emergency Department","Surgery", "Pediatrics");

    departmentInput.setItems(departments);

    assert numInput != null : "fx:id=\"numInput\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/Sanitation.fxml'.";
    assert assignedIndividual != null : "fx:id=\"assignedIndividual\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/Sanitation.fxml'.";
    assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/ExternalPatient.fxml'.";
    assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/ExternalPatient.fxml'.";


  }
}
