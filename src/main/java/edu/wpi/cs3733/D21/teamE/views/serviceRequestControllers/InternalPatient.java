package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.InternalPatientObj;
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

public class InternalPatient extends ServiceRequestFormComponents{

    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();
    ObservableList<String> userNames;
    ArrayList<Integer> userID = new ArrayList<>();

    @FXML // fx:id="background"
    private ImageView background;
    @FXML
    private JFXComboBox<String> pickupInput;
    @FXML
    private JFXComboBox<String> dropoffInput;
    @FXML
    private JFXTextField departmentInput;
    @FXML
    private JFXComboBox<String> severityInput;
    @FXML
    private JFXTextField patientIdInput;
    @FXML
    private JFXComboBox<String> assignedPersonnel;
    @FXML
    private JFXTextArea descriptionInput;
    @FXML
    private JFXButton cancel;
    @FXML
    private JFXButton submit;
    @FXML
    public AnchorPane appBarAnchorPane;
    @FXML
    private StackPane stackPane;

    private boolean validateInput() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Input required");

        pickupInput.getValidators().add(validator);
        dropoffInput.getValidators().add(validator);
        departmentInput.getValidators().add(validator);
        severityInput.getValidators().add(validator);
        patientIdInput.getValidators().add(validator);
        assignedPersonnel.getValidators().add(validator);
        descriptionInput.getValidators().add(validator);

        return pickupInput.validate() && dropoffInput.validate() && departmentInput.validate()
                && severityInput.validate() && patientIdInput.validate() && assignedPersonnel.validate()
                && descriptionInput.validate();
    }

    @FXML
    public void saveData(ActionEvent actionEvent) {
        if(validateInput()) {
            //Setting up indexes for getting id
            int nodePickupIndex = pickupInput.getSelectionModel().getSelectedIndex();
            int nodeDropOffIndex = dropoffInput.getSelectionModel().getSelectedIndex();
            int userIndex = assignedPersonnel.getSelectionModel().getSelectedIndex();

            //Setting up fields to be added to object
            int user = userID.get(userIndex);
            String pickUp = nodeID.get(nodePickupIndex);
            String dropOff = nodeID.get(nodeDropOffIndex);
            String dept = departmentInput.getText();
            String sever = severityInput.getSelectionModel().getSelectedItem();
            int patientID = Integer.parseInt(patientIdInput.getText());
            String desc = descriptionInput.getText();

            //Setting up object and adding it to database
            InternalPatientObj object = new InternalPatientObj(0, App.userID, pickUp, dropOff, user, patientID, dept, sever, desc);
            DB.addInternalPatientRequest(object);
            super.handleButtonSubmit(actionEvent);
        }
    }

    @FXML
    void initialize() {

        Stage primaryStage = App.getPrimaryStage();
        Image backgroundImg = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
        Image backgroundImage = backgroundImg;
        background.setImage(backgroundImage);
        background.setEffect(new GaussianBlur());

        //background.setPreserveRatio(true);
        background.fitWidthProperty().bind(primaryStage.widthProperty());
        //background.fitHeightProperty().bind(primaryStage.heightProperty());

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false); // show help or not
            App.setShowLogin(true); // show login or not
            App.setPageTitle("Internal Patient Transportation (Shane Donahue)"); //set AppBar title
            App.setHelpText(""); //set help text
            App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }

        locations = DB.getAllNodeLongNames();
        nodeID = DB.getListOfNodeIDS();
        pickupInput.setItems(locations);
        dropoffInput.setItems(locations);

        userNames = DB.getAssigneeNames("nurse");
        userID = DB.getAssigneeIDs("nurse");
        assignedPersonnel.setItems(userNames);
    }
}
