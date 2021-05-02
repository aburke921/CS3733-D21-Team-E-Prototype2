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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;

public class SanitationServices extends ServiceRequestFormComponents {


  ObservableList<String> locations;
  ArrayList<String> nodeID = new ArrayList<>();
  ObservableList<String> names;
  ArrayList<Integer> userID = new ArrayList<>();


  @FXML
  private JFXComboBox<String> assignee;
  @FXML
  private JFXTextField Signature;
  @FXML
  private JFXTextArea detailedInstructionsInput;
  @FXML
  private JFXComboBox<String> locationInput;
  @FXML
  private JFXComboBox<String> requestTypeInput;
  @FXML
  private JFXComboBox<String> Severity;
  @FXML
  private JFXButton cancel;
  @FXML
  private JFXButton submit;
  @FXML
  private final RequiredFieldValidator validator = new RequiredFieldValidator();
  @FXML
  public AnchorPane appBarAnchorPane;
  @FXML
  private StackPane stackPane;

  /**
   * Detects if the user has entered all required fields
   *
   */
  private boolean validateInput(){

    validator.setMessage("Input required");

    requestTypeInput.getValidators().add(validator);
    assignee.getValidators().add(validator);
    locationInput.getValidators().add(validator);
    detailedInstructionsInput.getValidators().add(validator);
    Signature.getValidators().add(validator);
    Severity.getValidators().add(validator);

    return locationInput.validate() && requestTypeInput.validate() && assignee.validate() && detailedInstructionsInput.validate() && Severity.validate() && Signature.validate();

  }


  /**
   * records inputs from user into a series of String variables and returns to the main page
   * @param event
   */
  @FXML
  private void saveData(ActionEvent event){

    if(validateInput()){
      int nodeIDIndex = locationInput.getSelectionModel().getSelectedIndex();
      int userIndex = assignee.getSelectionModel().getSelectedIndex();

      String serviceKind = requestTypeInput.getValue();
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
  void initialize() {

    //init appBar
    javafx.scene.Node appBarComponent = null;
    try {
      App.setShowHelp(false); // show help or not
      App.setShowLogin(true); // show login or not
      App.setPageTitle("Sanitation Request (Yveder Joseph)"); //set AppBar title
      App.setHelpText(""); //set help text
      App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
      appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
      appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
    } catch (IOException e) {
      e.printStackTrace();
    }


    locations = DB.getAllNodeLongNames();
    nodeID = DB.getListOfNodeIDS();
    //TODO add user type
    names = DB.getAssigneeNames("custodian");
    userID = DB.getAssigneeIDs("custodian");

    locationInput.setItems(locations);
    assignee.setItems(names);

    assert requestTypeInput != null : "fx:id=\"requestTypeInput\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";
    assert  locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";
    assert Severity != null : "fx:id=\"Severity\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";
    assert assignee != null : "fx:id=\"assignee\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";

  }
}
