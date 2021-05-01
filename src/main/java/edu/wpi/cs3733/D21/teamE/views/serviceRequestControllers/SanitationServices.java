package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;

import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.ArrayList;

public class SanitationServices extends ServiceRequestFormComponents {


  ObservableList<String> locations;
  ArrayList<String> nodeID = new ArrayList<>();
  ObservableList<String> names;
  ArrayList<Integer> userID = new ArrayList<>();


  @FXML private JFXComboBox<String> assignedIndividual;
  @FXML private JFXTextField Signature;
  @FXML private JFXTextArea detailedInstructionsInput;


  @FXML private JFXComboBox<String> locationInput;

  @FXML private JFXComboBox<String> ServiceTypeinput;
  @FXML private JFXComboBox<String> Severity;
  @FXML private JFXButton cancel;
  @FXML private JFXButton submit;


  @FXML private RequiredFieldValidator validator = new RequiredFieldValidator();




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

    ServiceTypeinput.getValidators().add(validator);
    assignedIndividual.getValidators().add(validator);
    locationInput.getValidators().add(validator);
    detailedInstructionsInput.getValidators().add(validator);
    Signature.getValidators().add(validator);
    Severity.getValidators().add(validator);

    return locationInput.validate() && ServiceTypeinput.validate() && assignedIndividual.validate() && detailedInstructionsInput.validate() && Severity.validate() && Signature.validate();

  }


  /**
   * records inputs from user into a series of String variables and returns to the main page
   * @param event
   */
  @FXML
  private void saveData(ActionEvent event){

    if(validateInput()){
      ArrayList<String> nodeIDS = DB.getListOfNodeIDS();
      String serviceKind = ServiceTypeinput.getValue();
      int assigneeID = 99999;
      String details = detailedInstructionsInput.getText();
      String severity = Severity.getValue();
      String signature = Signature.getText();
      int nodeIDIndex = locationInput.getSelectionModel().getSelectedIndex();
      String nodeID = nodeIDS.get(nodeIDIndex);
      DB.addSanitationRequest(App.userID,assigneeID,nodeID, serviceKind,details,severity,signature);
      //DB changed the assignee in the function call to an int (not string) --> we need the assignee's userID
      System.out.println(serviceKind);

      super.handleButtonSubmit(event);
      //Setting up all variables to be entered
    }

  }


  @FXML
  void initialize(){

    locations = DB.getAllNodeLongNames();
    nodeID = DB.getListOfNodeIDS();
    //TODO add user type
    names = DB.getAssigneeNames("Add user type here");
    userID = DB.getAssigneeIDs("Add user type here");

    locationInput.setItems(locations);
    assignedIndividual.setItems(names);

    assert ServiceTypeinput != null : "fx:id=\"ServiceTypeinput\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";
    //TODO clean these up
    ObservableList<String> Services  = FXCollections.observableArrayList();
    Services.setAll("Urine Cleanup","Feces Cleanup","Trash Removal");

    //ServiceTypeinput.setItems(Services);

    assert  locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";
    assert Severity != null : "fx:id=\"Severity\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";
    ObservableList<String> rating  = FXCollections.observableArrayList();
    rating.setAll("Low","Medium","High","Critical");
    Severity.setItems(rating);

    assert assignedIndividual != null : "fx:id=\"assignedIndividual\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";


  }
}
