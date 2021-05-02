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
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;

public class ExternalPatient extends ServiceRequestFormComponents {

	ObservableList<String> locations;
	ArrayList<String> nodeID = new ArrayList<>();
	ObservableList<String> userNames;
	ArrayList<Integer> userID = new ArrayList<>();

	RequiredFieldValidator validator = new RequiredFieldValidator();

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;
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
	@FXML // fx:id="ETAInput"
	private JFXTextField ETAInput; // Value injected by FXMLLoader
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

		validator.setMessage("Input required");

		locationInput.getValidators().add(validator);
		requestTypeInput.getValidators().add(validator);
		severityInput.getValidators().add(validator);
		patientIdInput.getValidators().add(validator);
		ETAInput.getValidators().add(validator);
		descriptionInput.getValidators().add(validator);
		assignedPersonnel.getValidators().add(validator);
		oxygenInput.getValidators().add(validator);
		bloodPressureInput.getValidators().add(validator);
		temperatureInput.getValidators().add(validator);

		return locationInput.validate() && requestTypeInput.validate() && severityInput.validate() &&
				patientIdInput.validate() && assignedPersonnel.validate() && descriptionInput.validate() &&
				ETAInput.validate() && oxygenInput.validate() && bloodPressureInput.validate() && temperatureInput.validate();
	}

	@FXML
	private void saveData(ActionEvent event) {

		if (validateInput()) {

			String type = requestTypeInput.getSelectionModel().getSelectedItem();
			String severity = severityInput.getSelectionModel().getSelectedItem();
			String patientID = patientIdInput.getText();
			String ETA = ETAInput.getText();
			String bloodPressure = bloodPressureInput.getText();
			String temperature = temperatureInput.getText();
			String oxygenLevel = oxygenInput.getText();
			String details = descriptionInput.getText();
			int assigneeIDIndex = assignedPersonnel.getSelectionModel().getSelectedIndex();
			int assigneeID = userID.get(assigneeIDIndex);
			int nodeIDIndex = locationInput.getSelectionModel().getSelectedIndex();
			String id = nodeID.get(nodeIDIndex);
			DB.addExternalPatientRequest(App.userID, assigneeID, id, type, severity, patientID, ETA, bloodPressure, temperature, oxygenLevel, details);

			super.handleButtonSubmit(event);
		}
	}

	@FXML
		// This method is called by the FXMLLoader when initialization is complete

	void initialize() {

		nodeID = DB.getListOfNodeIDS();
		locations = DB.getAllNodeLongNames();
		userID = DB.getAssigneeIDs("EMT");
		userNames = DB.getAssigneeNames("EMT");

		assert locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		locationInput.setItems(locations);
		assignedPersonnel.setItems(userNames);
		assert requestTypeInput != null : "fx:id=\"requestTypeInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert severityInput != null : "fx:id=\"severityInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert patientIdInput != null : "fx:id=\"patientIdInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert ETAInput != null : "fx:id=\"ETAInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
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
