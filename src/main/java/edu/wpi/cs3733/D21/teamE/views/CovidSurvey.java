package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.states.CovidSurveyState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

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

	/**
	 * Creates a popup if the user indicates any symptoms.
	 */
	@FXML
	void popUp() {
		App.newJFXDialogPopUp("","Exit","Based on your response you should go home.",stackPane);
	}

	/**
	 * Detects if the user has entered all required fields
	 */
	private void validateInput() {
		App.newJFXDialogPopUp("","Done","Please select at least one checkbox.",stackPane);
	}

	@FXML
	void submitButton(ActionEvent actionEvent) {
		//TODO: make a covidSurveyObject and instead of ratings, set the parameters to true or false given user's selection
		int rating = 0;
		if (noSymptoms.isSelected()) {
			rating += 1;
		}
		if (quarantine.isSelected()) {
			rating += 10;
		}
		if (closeContact.isSelected()) {
			rating += 100;
		}
		if (symptoms.isSelected()) {
			rating += 1000;
		}
		if (positiveTest.isSelected()) {
			rating += 10000;
		}

		if (rating == 0) {
			validateInput();
		} else if (App.userID != 0) {
			//TODO: pass that covidSurveyObject here! and delete the one below
			CovidSurveyObj cv = null;
			if (DB.submitCovidSurvey(cv, App.userID)) {
				System.out.println("user's covid survey of " + rating + " was submitted");
			} else {
				System.err.println("user's covid survey of " + rating + " was not submitted");
				popUp();
			}
			if (DB.isUserCovidSafe(App.userID)) {
				if (plzGoToPathFinder){
					plzGoToPathFinder = false;
					try {
						Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/PathFinder.fxml"));
						App.changeScene(root);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}else {
					CovidSurveyState covidSurveyState = new CovidSurveyState();
					covidSurveyState.switchScene(actionEvent);
				}
			} else {
				popUp();
			}
		} else {
			if (rating < 10) {
				App.noCleanSurveyYet = false;
				if (plzGoToPathFinder) {
					plzGoToPathFinder = false;
					try {
						Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/PathFinder.fxml"));
						App.changeScene(root);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				} else {
					CovidSurveyState covidSurveyState = new CovidSurveyState();
					covidSurveyState.switchScene(actionEvent);
				}
			} else {
				App.noCleanSurveyYet = true;
				popUp();
			}
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
