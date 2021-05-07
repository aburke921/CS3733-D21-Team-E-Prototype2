package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;

import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.email.sendEmail;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.SecurityServiceObj;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class SecurityService extends ServiceRequestFormComponents {

    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();
    ObservableList<String> userNames;
    ArrayList<Integer> userID = new ArrayList<>();

    @FXML // fx:id="background"
    private ImageView background;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // fx:id = "locationInput"
    private JFXComboBox<String> locationInput;

    @FXML // fx:id="helpSecurityService"
    private Button helpSecurityService; // Value injected by FXMLLoader

    @FXML // fx:id="levelOfSecurity"
    private JFXComboBox<String> levelOfSecurity; // Value injected by FXMLLoader

    @FXML // fx:id="levelOfUrgency"
    private JFXComboBox<String> levelOfUrgency; // Value injected by FXMLLoader

    @FXML // fx:id = "assignedPersonnel"
    private JFXComboBox<String> assignedPersonnel;

    @FXML // fx:id="reasonForRequest"
    private JFXTextArea reasonForRequest; // Value injected by FXMLLoader

    @FXML // fx:id="cancelSecurityRequest"
    private JFXButton cancelSecurityRequest; // Value injected by FXMLLoader

    @FXML // fx:id="submitSecurityRequest"
    private JFXButton submitSecurityRequest; // Value injected by FXMLLoader

    @FXML // fx:id="exit"
    private Polygon exit;

    @FXML
    public AnchorPane appBarAnchorPane;

    @FXML
    private StackPane stackPane;

    /**
     * Returns to default page
     * todo, in future iterations this button should SUBMIT instead
     * @param event {@link ActionEvent} info for the submit button call, passed automatically by system.
     */
    @FXML
    void handleButtonSubmit(ActionEvent event) {
        if (validateInput()) {
            try {
                saveData(event);
                System.out.println(event); //Print the ActionEvent to console
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean validateInput(){

        RequiredFieldValidator validator = new RequiredFieldValidator();

        validator.setMessage("Input required");

        locationInput.getValidators().add(validator);
        levelOfSecurity.getValidators().add(validator);
        levelOfUrgency.getValidators().add(validator);
        assignedPersonnel.getValidators().add(validator);
        reasonForRequest.getValidators().add(validator);

        return  locationInput.validate() && levelOfSecurity.validate() && levelOfUrgency.validate() && assignedPersonnel.validate() && assignedPersonnel.validate() && reasonForRequest.validate();

    }

    @FXML
    private void saveData(ActionEvent actionEvent){
        if(validateInput()){

            String securityLevel = levelOfSecurity.getSelectionModel().getSelectedItem();
            String urgencyLevel = levelOfUrgency.getSelectionModel().getSelectedItem();
            int assigneeIndex = assignedPersonnel.getSelectionModel().getSelectedIndex();
            int assignee = userID.get(assigneeIndex);
            String reason = reasonForRequest.getText();
            int nodeIDIndex = locationInput.getSelectionModel().getSelectedIndex();
            String node = nodeID.get(nodeIDIndex);

            SecurityServiceObj object = new SecurityServiceObj(0, App.userID, assignee, node, securityLevel, urgencyLevel, reason);
            DB.addSecurityRequest(object);

            //For email implementation later
//            String email = DB.getEmail(App.userID);
//            String fullName = DB.getUserName(App.userID);
//            String assigneeName = userNames.get(assigneeIDIndex);
//            String locationName = locations.get(nodeIDIndex);
//            String body = "Hello " + fullName + ", \n\n" + "Thank you for making an External Patient Transport request." +
//                    "Here is the summary of your request: \n\n" +
//                    " - Type: " + type + "\n" +
//                    " - Severity: " + severity + "\n" +
//                    " - PatientID: " + patientID + "\n" +
//                    " - ETA: " + ETA + "\n" +
//                    " - Blood Pressure: " + bloodPressure + "\n" +
//                    " - Temperature: " + temperature + "\n" +
//                    " - Oxygen Level: " + oxygenLevel + "\n" +
//                    " - Details: " + details + "\n" +
//                    " - Assignee Name: " + assigneeName + "\n" +
//                    " - Location: " + locationName + "\n\n" +
//                    "If you need to edit any details, please visit our app to do so. We look forward to seeing you soon!\n\n" +
//                    "- Emerald Emus BWH";
//
//            sendEmail.sendRequestConfirmation(email, body);
            super.handleButtonSubmit(actionEvent);
        }
    }

    /**
     * todo This function will cause a pop-up modal to appear with help information for this form's fields
     * @param event {@link ActionEvent} info for the help button call, passed automatically by system.
     */
    @FXML
    void getHelpSecurityService(ActionEvent event) {
        System.out.println(event);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        Stage primaryStage = App.getPrimaryStage();
        Image backgroundImg = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
        Image backgroundImage = backgroundImg;
        background.setImage(backgroundImage);
        background.setEffect(new GaussianBlur());

        //background.setPreserveRatio(true);
        background.fitWidthProperty().bind(primaryStage.widthProperty());
        //background.fitHeightProperty().bind(primaryStage.heightProperty());

        locations = DB.getAllNodeLongNames();
        nodeID = DB.getListOfNodeIDS();
        locationInput.setItems(locations);

        userNames = DB.getAssigneeNames("security");
        userID = DB.getAssigneeIDs("security");
        assignedPersonnel.setItems(userNames);

        assert helpSecurityService != null : "fx:id=\"helpSecurityService\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert locationInput != null : "fx:id=\"locationOfDelivery\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert levelOfSecurity != null : "fx:id=\"levelOfSecurity\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert levelOfUrgency != null : "fx:id=\"levelOfUrgency\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert reasonForRequest != null : "fx:id=\"reasonForRequest\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert cancelSecurityRequest != null : "fx:id=\"cancelSecurityRequest\" was not injected: check your FXML file 'SecurityService.fxml'.";
        assert submitSecurityRequest != null : "fx:id=\"submitSecurityRequest\" was not injected: check your FXML file 'SecurityService.fxml'.";

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false); // show help or not
            App.setShowLogin(true); // show login or not
            App.setPageTitle("Security Service (Seamus Sullivan)"); //set AppBar title
            App.setHelpText(""); //set help text
            App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
