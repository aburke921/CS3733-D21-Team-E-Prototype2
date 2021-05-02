package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class LanguageInterpreter extends ServiceRequestFormComponents {

	ObservableList<String> locations;
	ArrayList<String> nodeID = new ArrayList<>();
	ObservableList<String> names;
	ArrayList<Integer> userID = new ArrayList<>();

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

	private boolean validateInput() {
		RequiredFieldValidator validator = new RequiredFieldValidator();

		locationInput.getValidators().add(validator);
		languageSelection.getValidators().add(validator);
		assignedPersonnel.getValidators().add(validator);
		descriptionInput.getValidators().add(validator);

		return  locationInput.validate() && languageSelection.validate()
				&& assignedPersonnel.validate() && descriptionInput.validate();
	}

	@FXML
	void saveData(ActionEvent event) {
		if(validateInput()) {
			int userIndex = assignedPersonnel.getSelectionModel().getSelectedIndex();
			int index = locationInput.getSelectionModel().getSelectedIndex();
			int assignee = userID.get(userIndex);
			String node = nodeID.get(index);
			String descrip = descriptionInput.getText();
			String language = languageSelection.getSelectionModel().getSelectedItem();

			DB.addLanguageRequest(App.userID, assignee, node, language, descrip);
			super.handleButtonSubmit(event);
		}
	}

	@FXML
	void initialize() {
		locations = DB.getAllNodeLongNames();
		nodeID = DB.getListOfNodeIDS();
		//TODO add user type
		names = DB.getAssigneeNames("interpreter");
		userID = DB.getAssigneeIDs("interpreter");

		locationInput.setItems(locations);
		assignedPersonnel.setItems(names);

	}

}
