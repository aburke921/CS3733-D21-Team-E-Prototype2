package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class CovidSurvey extends ServiceRequests {

	public static boolean plzGoToPathFinder = false;

	@FXML
	JFXCheckBox positiveTest;
	@FXML
	JFXCheckBox symptoms;
	@FXML
	JFXCheckBox closeContact;
	@FXML
	JFXCheckBox quarantine;
	@FXML
	JFXCheckBox noSymptoms;
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
			if (DB.submitCovidSurvey(rating, App.userID)) {
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
						App.setDraggableAndChangeScene(root);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}else {
					try {
						Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
						App.setDraggableAndChangeScene(root);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
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
						App.setDraggableAndChangeScene(root);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				} else {
					try {
						Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
						App.setDraggableAndChangeScene(root);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			} else {
				App.noCleanSurveyYet = true;
				popUp();
			}
		}
	}

	/**
	 * Returns to the service request page
	 * @param event {@link ActionEvent} info for the cancel button call, passed automatically by system.
	 */
	@FXML
	void handleButtonCancel(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
			App.setDraggableAndChangeScene(root);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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
