package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.lang.String;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
	@FXML // fx:id="ambulance"
	private String ambulance; // Value injected by FXMLLoader
	@FXML // fx:id="helicopter"
	private String helicopter; // Value injected by FXMLLoader
	@FXML // fx:id="plane"
	private String plane; // Value injected by FXMLLoader
	@FXML // fx:id="severityInput"
	private JFXComboBox<String> severityInput; // Value injected by FXMLLoader
	@FXML // fx:id="high_severity"
	private String high_severity; // Value injected by FXMLLoader
	@FXML // fx:id="medium_severity"
	private String medium_severity; // Value injected by FXMLLoader
	@FXML // fx:id="low_severity"
	private String low_severity; // Value injected by FXMLLoader
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

	/**
	 * todo This function will cause a pop-up modal to appear with help information for this form's fields
	 * @param event {@link ActionEvent} info for the help button call, passed automatically by system.
	 */
	@FXML
	void getHelpExternalPatient(ActionEvent event) {

	}

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

	/**
	 * records inputs from user into a series of String variables and returns to the main page
	 * @param actionEvent
	 */
	@FXML
	private void saveData(ActionEvent actionEvent) {


		if (validateInput()) {


			String type = requestTypeInput.getSelectionModel().getSelectedItem().toString();
			String severity = severityInput.getSelectionModel().getSelectedItem().toString();
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
			System.out.println(nodeID + " " + type + " " + severity + " " + patientID + " " + ETA + " " + details + " " + assigneeID);
			DB.addExternalPatientRequest(App.userID, assigneeID, id, type, severity, patientID, ETA, bloodPressure, temperature, oxygenLevel, details);

			super.handleButtonSubmit(actionEvent);

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
		assert ambulance != null : "fx:id=\"ambulance\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert helicopter != null : "fx:id=\"helicopter\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert plane != null : "fx:id=\"plane\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert severityInput != null : "fx:id=\"severityInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert high_severity != null : "fx:id=\"high_severity\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert medium_severity != null : "fx:id=\"medium_severity\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert low_severity != null : "fx:id=\"low_severity\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert patientIdInput != null : "fx:id=\"patientIdInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert ETAInput != null : "fx:id=\"ETAInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		assert assignedPersonnel != null : "fx:id=\"assignedPersonnel\" was not injected: check your FXML file 'ExternalPatient.fxml'.";

		exit.setOnMouseClicked(event -> {
			App app = new App();
			app.stop();
		});

	}
}
