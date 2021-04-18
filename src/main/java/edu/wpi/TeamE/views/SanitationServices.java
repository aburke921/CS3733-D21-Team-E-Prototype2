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
import edu.wpi.TeamE.databases.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;



public class SanitationServices extends ServiceRequestFormComponents {

  @FXML private JFXTextField numInput;
  @FXML private JFXTextField assignedIndividual;
  @FXML private JFXTextField Signature;
  @FXML private JFXTextArea detailedInstructionsInput;


  @FXML private JFXComboBox<String> departmentInput;
  @FXML private JFXComboBox<String> typeInput;
  @FXML private JFXComboBox<String> ServiceTypeinput;
  @FXML private JFXComboBox<String> Severity;
  @FXML private JFXButton cancel;
  @FXML private JFXButton submit;
  public SanitationServicesForm request;
  RequiredFieldValidator validator = new RequiredFieldValidator();
  makeConnection connection = makeConnection.makeConnection();


  /**
   * Sets value of drop down list to the selected value(departments)
   * @param actionEvent
   */




  /**
   * Detects if the user has entered all required fields
   *
   */
  private boolean validateInput(){

      validator.setMessage("Input required");


    departmentInput.getValidators().add(validator);
    ServiceTypeinput.getValidators().add(validator);
    assignedIndividual.getValidators().add(validator);
    numInput.getValidators().add(validator);
    typeInput.getValidators().add(validator);
    detailedInstructionsInput.getValidators().add(validator);
    Signature.getValidators().add(validator);
    Severity.getValidators().add(validator);

    return departmentInput.validate() && typeInput.validate() && numInput.validate() && ServiceTypeinput.validate() && assignedIndividual.validate() && detailedInstructionsInput.validate() && Severity.validate() && Signature.validate();


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
    String details = detailedInstructionsInput.getText();
    String signature = Signature.getText();
    if(validateInput()){
      //String detailedInstructions = sdetailedInstructionsInput.getText();
      //creating the service request

      //System.out.println(request.getAssignmentField());
      //Adding service request to table
      //makeConnection connection = makeConnection.makeConnection();
      //connection.addRequest("sanitationServices", request);

      connection.addSanitationRequest(15,room+num, serviceKind,details,"",signature);
      super.handleButtonSubmit(actionEvent);
      //Setting up all variables to be entered
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
    assert  Severity != null : "fx:id=\"Severity\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/Sanitation.fxml'.";
    ObservableList<String> rating  = FXCollections.observableArrayList();
   rating.setAll("1","2","3","4","5");
   Severity.setItems(rating);






    assert numInput != null : "fx:id=\"numInput\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/Sanitation.fxml'.";
    assert assignedIndividual != null : "fx:id=\"assignedIndividual\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/Sanitation.fxml'.";
    assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/ExternalPatient.fxml'.";
    assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file '/edu/wpi/TeamE/fxml/ExternalPatient.fxml'.";


  }
}
