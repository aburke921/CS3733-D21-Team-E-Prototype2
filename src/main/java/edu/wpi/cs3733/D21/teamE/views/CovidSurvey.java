package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.Time;
import edu.wpi.cs3733.D21.teamE.states.CovidSurveyState;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class CovidSurvey extends ServiceRequests {

	public static boolean plzGoToPathFinder = false;

	@FXML
	private JFXCheckBox positiveTest;
	@FXML
	private JFXCheckBox symptoms;
	@FXML
	private JFXCheckBox closeContact;
	@FXML
	private JFXCheckBox quarantine;
	@FXML
	private JFXCheckBox noSymptoms;
	@FXML
	private AnchorPane appBarAnchorPane;
	@FXML
	private StackPane stackPane;

	public static void exitPopUp(String heading, String button, String message, StackPane stackPane) {
		System.out.println("DialogBox Posted");
		JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
		jfxDialogLayout.setHeading(new Text(heading));
		jfxDialogLayout.setBody(new Text(message));
		JFXDialog dialog = new JFXDialog(stackPane, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
		JFXButton okay = new JFXButton(button);
		okay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
					App.changeScene(root);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		jfxDialogLayout.setActions(okay);
		dialog.show();
	}

	private void exit() {
		exitPopUp("","Exit","Thank you for filling out the form, please wait for it to be reviewed before entry.", stackPane);
	}

	/**
	 * Detects if the user has entered all required fields
	 */
	private void validateInput() {
		App.newJFXDialogPopUp("","Done","Please select at least one checkbox.",stackPane);
	}

	private boolean validateInputVersion() {
		return symptoms.isSelected() || positiveTest.isSelected() || closeContact.isSelected()
				|| quarantine.isSelected() || noSymptoms.isSelected();
	}

	@FXML
	private void submitButton(ActionEvent event) {
		if(validateInputVersion()) {
			boolean positiveTestBool = false;
			boolean symptomsBool = false;
			boolean closeContactBool = false;
			boolean quarantineBool = false;
			boolean noSymptomsBool = false;
			if (positiveTest.isSelected()) {
				positiveTestBool = true;
			}
			if (symptoms.isSelected()) {
				symptomsBool = true;
			}
			if (closeContact.isSelected()) {
				closeContactBool = true;
			}
			if (quarantine.isSelected()) {
				quarantineBool = true;
			}
			if (noSymptoms.isSelected()) {
				noSymptomsBool = true;
			}

			//TODO: based on what user has selected, set the string to either "Needs to be reviewed", "Safe", "Unsafe"

			CovidSurveyObj newSurvey = new CovidSurveyObj(App.userID, 0, positiveTestBool, symptomsBool, closeContactBool, quarantineBool, noSymptomsBool, "Needs to Be Reviewed");
			DB.submitCovidSurvey(newSurvey, App.userID);
			DB.addEntryRequest(newSurvey);
			DB.updateUserAccountCovidStatus(App.userID, "Needs to Be Reviewed");
			exit();
		} else {
			validateInput();
		}
	}

	/**
	 * Switch to a different scene
	 * @param event tells which button was pressed
	 */
	@FXML
	void switchScene(ActionEvent event) {
		CovidSurveyState covidSurveyState = new CovidSurveyState();
		covidSurveyState.switchScene(event);
	}

	@FXML
	public void initialize() {

		//init appBar
		javafx.scene.Node appBarComponent = null;
		try {
			App.setShowLogin(true);
			App.setShowHelp(true);
			App.setPageTitle("Covid Survey"); //set AppBar title
			App.setHelpText(""); //set help text todo, fill in this field
			App.setStackPane(stackPane); // required for dialog boxes
			appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
			appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
		} catch (IOException e) {
			e.printStackTrace();
		}

		assert positiveTest != null : "fx:id=\"positiveTest\" was not injected: check your FXML file 'PathFinder.fxml'.";
		assert symptoms != null : "fx:id=\"symptoms\" was not injected: check your FXML file 'PathFinder.fxml'.";
		assert closeContact != null : "fx:id=\"closeContact\" was not injected: check your FXML file 'PathFinder.fxml'.";
		assert quarantine != null : "fx:id=\"quarantine\" was not injected: check your FXML file 'PathFinder.fxml'.";
		assert noSymptoms != null : "fx:id=\"noSymptoms\" was not injected: check your FXML file 'PathFinder.fxml'.";
	}
}
