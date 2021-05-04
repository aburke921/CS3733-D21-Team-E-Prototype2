package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;

import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.email.sendEmail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SanitationServices extends ServiceRequestFormComponents {

  @FXML // fx:id="background"
  private ImageView background;

  @FXML private JFXTextField assignedIndividual;
  @FXML private JFXTextField Signature;
  @FXML private JFXTextArea detailedInstructionsInput;
  @FXML private JFXComboBox<String> locationInput;
  @FXML private JFXComboBox<String> ServiceTypeinput;
  @FXML private JFXComboBox<String> Severity;
  @FXML private JFXButton cancel;
  @FXML private JFXButton submit;
  @FXML public AnchorPane appBarAnchorPane;
  @FXML private StackPane stackPane;


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
   * @param actionEvent
   */
  @FXML
  private void saveData(ActionEvent actionEvent){


    if(validateInput()){
      //String detailedInstructions = sdetailedInstructionsInput.getText();
      //creating the service request

      //System.out.println(request.getAssignmentField());
      //Adding service request to table
      //makeConnection connection = makeConnection.makeConnection();
      //connection.addRequest("sanitationServices", request);
      ArrayList<String> nodeIDS = DB.getListOfNodeIDS();
      String serviceKind = ServiceTypeinput.getValue();
      int assigneeID = 99999;
      String details = detailedInstructionsInput.getText();
      String severity = Severity.getValue();
      String signature = Signature.getText();
      int nodeIDIndex = locationInput.getSelectionModel().getSelectedIndex();
      String nodeID = nodeIDS.get(nodeIDIndex);
      DB.addSanitationRequest(15,assigneeID,nodeID, serviceKind,details,severity,signature);
      //DB changed the assignee in the function call to an int (not string) --> we need the assignee's userID
      System.out.println(serviceKind);

      super.handleButtonSubmit(actionEvent);

      //For email implementation later
//      String email = DB.getEmail(App.userID);
//      String fullName = DB.getUserName(App.userID);
////      String assigneeName = userNames.get(assigneeIDIndex);
//      String locationName = locations.get(nodeID);
//      String body = "Hello " + fullName + ", \n\n" + "Thank you for making an External Patient Transport request." +
//              "Here is the summary of your request: \n\n" +
//              " - Type: " + type + "\n" +
//              " - Severity: " + severity + "\n" +
//              " - PatientID: " + patientID + "\n" +
//              " - ETA: " + ETA + "\n" +
//              " - Blood Pressure: " + bloodPressure + "\n" +
//              " - Temperature: " + temperature + "\n" +
//              " - Oxygen Level: " + oxygenLevel + "\n" +
//              " - Details: " + details + "\n" +
//              " - Assignee Name: " + assigneeName + "\n" +
//              " - Location: " + locationName + "\n\n" +
//              "If you need to edit any details, please visit our app to do so. We look forward to seeing you soon!\n\n" +
//              "- Emerald Emus BWH";
//
//      sendEmail.sendRequestConfirmation(email, body);

      //Setting up all variables to be entered
    }




  }


  @FXML
  void initialize(){

    Stage primaryStage = App.getPrimaryStage();
    Image backgroundImg = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
    Image backgroundImage = backgroundImg;
    background.setImage(backgroundImage);
    background.setEffect(new GaussianBlur());

    //background.setPreserveRatio(true);
    background.fitWidthProperty().bind(primaryStage.widthProperty());
    //background.fitHeightProperty().bind(primaryStage.heightProperty());

    assert ServiceTypeinput != null : "fx:id=\"ServiceTypeinput\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";
    assert  locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";
    assert Severity != null : "fx:id=\"Severity\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";
    assert assignedIndividual != null : "fx:id=\"assignedIndividual\" was not injected: check your FXML file '/edu/wpi/cs3733/D21/teamE/fxml/Sanitation.fxml'.";

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


    ObservableList<String> Services  = FXCollections.observableArrayList();
    Services.setAll("Urine Cleanup","Feces Cleanup","Trash Removal");

    //ServiceTypeinput.setItems(Services);

    ObservableList<String> locations  = DB.getAllNodeLongNames();

    locationInput.setItems(locations);
    ObservableList<String> rating  = FXCollections.observableArrayList();
    rating.setAll("Low","Medium","High","Critical");
    Severity.setItems(rating);



  }
}
