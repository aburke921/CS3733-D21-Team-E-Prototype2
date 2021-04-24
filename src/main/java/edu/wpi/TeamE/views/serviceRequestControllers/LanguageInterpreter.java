package edu.wpi.TeamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamE.databases.NodeDB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class LanguageInterpreter extends ServiceRequestFormComponents {

	@FXML
	private Rectangle fullscreen;

	@FXML
	private Circle hide;

	@FXML
	private Polygon exit;

	@FXML
	private JFXComboBox<?> locationInput;

	@FXML
	private JFXComboBox<?> languageSelection;

	@FXML
	private JFXTextField assignedPersonnel;

	@FXML
	private JFXTextArea descriptionInput;

	@FXML
	private JFXButton cancel;

	@FXML
	private JFXButton submit;

	@FXML
	void handleButtonCancel(ActionEvent event) {

	}

	@FXML
	void saveData(ActionEvent event) {

	}

	@FXML
		// This method is called by the FXMLLoader when initialization is complete

	void initialize() {
		assert locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file 'ExternalPatient.fxml'.";
		ObservableList<String> locations = NodeDB.getAllNodeLongNames();
		locationInput.setItems(locations);
	}

}
