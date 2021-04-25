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

import java.util.ArrayList;

public class LanguageInterpreter extends ServiceRequestFormComponents {

	ObservableList<String> locations;
	ArrayList<String> nodeID;

	@FXML
	private Rectangle fullscreen;

	@FXML
	private Circle hide;

	@FXML
	private Polygon exit;

	@FXML
	private JFXComboBox<String> locationInput;

	@FXML
	private JFXComboBox<String> languageSelection;

	@FXML
	private JFXComboBox<String> assignedPersonnel;

	@FXML
	private JFXTextArea descriptionInput;

	@FXML
	private JFXButton cancel;

	@FXML
	private JFXButton submit;

	@FXML
	void handleButtonCancel(ActionEvent event) {
		super.handleButtonSubmit(event);
	}

	@FXML
	void saveData(ActionEvent event) {
		int index = locationInput.getSelectionModel().getSelectedIndex();
		String node = nodeID.get(index);
		String assignee = assignedPersonnel.getSelectionModel().getSelectedItem();
		String descrip = descriptionInput.getText();
		String language = languageSelection.getSelectionModel().getSelectedItem();

		super.handleButtonSubmit(event);
	}

	@FXML
		// This method is called by the FXMLLoader when initialization is complete

	void initialize() { ;
//		ObservableList<String> employees = SomethingDB.getAllEmployeeNames();
//		assignedPersonnel.setItems(employees);
		locations = NodeDB.getAllNodeLongNames();
		locationInput.setItems(locations);
	}

}
