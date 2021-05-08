package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.email.sendEmail;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.ExternalPatientObj;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import javax.mail.MessagingException;

public class ExternalPatient extends ServiceRequestFormComponents {

	ObservableList<String> locations;
	ArrayList<String> nodeID = new ArrayList<>();
	ObservableList<String> userNames;
	ArrayList<Integer> userID = new ArrayList<>();

	@FXML // fx:id="background"
	private ImageView background;
	@FXML // fx:id="locationInput"
	private JFXComboBox<String> locationInput; // Value injected by FXMLLoader
	@FXML // fx:id="requestTypeInput"
	private JFXComboBox<String> requestTypeInput; // Value injected by FXMLLoader
	@FXML // fx:id="severityInput"
	private JFXComboBox<String> severityInput; // Value injected by FXMLLoader
	@FXML // fx:id="patientIdInput"
	private JFXTextField patientIdInput; // Value injected by FXMLLoader
	@FXML // fx:id="assignedPersonnel"
	private JFXComboBox<String> assignedPersonnel; // Value injected by FXMLLoader
	@FXML // fx:id="descriptionInput"
	private JFXTextArea descriptionInput; // Value injected by FXMLLoader
	@FXML // fx:id="cancel"
	private JFXButton cancel; // Value injected by FXMLLoader
	@FXML // fx:id="submit"
	private JFXButton submit; // Value injected by FXMLLoader
	@FXML // fx:id="exit"
	private Polygon exit;

	@FXML
	private JFXTextField oxygenInput;

	@FXML
	private JFXTextField bloodPressureInput;

	@FXML
	private JFXTextField temperatureInput;

	@FXML
	public AnchorPane appBarAnchorPane;

	@FXML
	private StackPane stackPane;

	/**
	 * Detects if the user has entered all required fields
	 */
	private boolean validateInput() {

		RequiredFieldValidator validator = new RequiredFieldValidator();

		validator.setMessage("Input required");

		locationInput.getValidators().add(validator);
		requestTypeInput.getValidators().add(validator);
		severityInput.getValidators().add(validator);
		patientIdInput.getValidators().add(validator);
		descriptionInput.getValidators().add(validator);
		assignedPersonnel.getValidators().add(validator);
		oxygenInput.getValidators().add(validator);
		bloodPressureInput.getValidators().add(validator);
		temperatureInput.getValidators().add(validator);

		return locationInput.validate() && requestTypeInput.validate() && severityInput.validate() &&
				patientIdInput.validate() && assignedPersonnel.validate() && descriptionInput.validate() &&
				oxygenInput.validate() && bloodPressureInput.validate() && temperatureInput.validate();
	}

	/**
	 * records inputs from user into a series of String variables and returns to the main page
	 * @param actionEvent
	 */
	@FXML
	private void saveData(ActionEvent actionEvent) throws MessagingException {
		//validate all fields have been filled first
		if (validateInput()) {
			//getting index for the chosen node and user
			int assigneeIDIndex = assignedPersonnel.getSelectionModel().getSelectedIndex();
			int nodeIDIndex = locationInput.getSelectionModel().getSelectedIndex();

			//setting up all fields to be put into object and sent to database
			String type = requestTypeInput.getSelectionModel().getSelectedItem();
			String severity = severityInput.getSelectionModel().getSelectedItem();
			String patientID = patientIdInput.getText();
			String bloodPressure = bloodPressureInput.getText();
			String temperature = temperatureInput.getText();
			String oxygenLevel = oxygenInput.getText();
			String details = descriptionInput.getText();
			int assigneeID = userID.get(assigneeIDIndex);
			String id = nodeID.get(nodeIDIndex);

			//Creating object to go to database and running DB function on it to add iot to database
			ExternalPatientObj externalPatientObj = new ExternalPatientObj(0, App.userID, assigneeID, id, type, severity, patientID, bloodPressure, temperature, oxygenLevel, details);
			DB.addExternalPatientRequest(externalPatientObj);

			//email stuff
			String email = DB.getEmail(App.userID);
			String fullName = DB.getUserName(App.userID);
			String assigneeName = userNames.get(assigneeIDIndex);
			String locationName = locations.get(nodeIDIndex);
			String body = "Hello " + fullName + ", \n\n" + "Thank you for making an External Patient Transport request." +
					"Here is the summary of your request: \n\n" +
					" - Type: " + type + "\n" +
					" - Severity: " + severity + "\n" +
					" - PatientID: " + patientID + "\n" +
					" - Blood Pressure: " + bloodPressure + "\n" +
					" - Temperature: " + temperature + "\n" +
					" - Oxygen Level: " + oxygenLevel + "\n" +
					" - Details: " + details + "\n" +
					" - Assignee Name: " + assigneeName + "\n" +
					" - Location: " + locationName + "\n\n" +
					"If you need to edit any details, please visit our app to do so. We look forward to seeing you soon!\n\n" +
					"- Emerald Emus BWH";

					sendEmail.sendRequestConfirmation(email, body);

			super.handleButtonSubmit(actionEvent);
		}
	}

	@FXML
		// This method is called by the FXMLLoader when initialization is complete

	void initialize() {

		Stage primaryStage = App.getPrimaryStage();
		Image backgroundImg = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
		Image backgroundImage = backgroundImg;
		background.setImage(backgroundImage);
		background.setEffect(new GaussianBlur());

		//background.setPreserveRatio(true);
		background.fitWidthProperty().bind(primaryStage.widthProperty());
		//background.fitHeightProperty().bind(primaryStage.heightProperty());

		//Populating lists for location dropdown and id
		nodeID = DB.getListOfNodeIDS();
		locations = DB.getAllNodeLongNames();
		locationInput.setItems(locations);

		//Populating lists for user dropdown and id
		userID = DB.getAssigneeIDs("EMT");
		userNames = DB.getAssigneeNames("EMT");
		assignedPersonnel.setItems(userNames);

		//asserting that they are not null
		assert locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert requestTypeInput != null : "fx:id=\"requestTypeInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert severityInput != null : "fx:id=\"severityInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert patientIdInput != null : "fx:id=\"patientIdInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert assignedPersonnel != null : "fx:id=\"assignedPersonnel\" was not injected: check your FXML file 'ExternalPatient.fxml'.";

		//init appBar
		javafx.scene.Node appBarComponent = null;
		try {
			App.setShowHelp(false); // show help or not
			App.setShowLogin(true); // show login or not
			App.setPageTitle("External Patient Request (Cole Manning)"); //set AppBar title
			App.setHelpText(""); //set help text
			App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
			appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
			appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
