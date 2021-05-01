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
      int nodeIDIndex = locationInput.getSelectionModel().getSelectedIndex();
      int userIndex = assignedIndividual.getSelectionModel().getSelectedIndex();

      String serviceKind = ServiceTypeinput.getValue();
      int assigneeID = userID.get(userIndex);
      String details = detailedInstructionsInput.getText();
      String severity = Severity.getValue();
      String signature = Signature.getText();
      String node = nodeID.get(nodeIDIndex);
      DB.addSanitationRequest(App.userID,assigneeID,node, serviceKind,details,severity,signature);

      super.handleButtonSubmit(event);

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
    assert  locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";
    assert Severity != null : "fx:id=\"Severity\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";
    assert assignedIndividual != null : "fx:id=\"assignedIndividual\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";

  }
}
