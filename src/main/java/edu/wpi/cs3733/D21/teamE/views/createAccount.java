package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.states.CovidSurveyState;
import edu.wpi.cs3733.D21.teamE.states.CreateAccountState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class createAccount {

	@FXML
	public Button submitAccountButton;
	@FXML
	private JFXTextField firstName;
	@FXML
	private JFXTextField lastName;
	@FXML
	private JFXTextField email;
	@FXML
	private JFXTextField password;
	@FXML
	private StackPane stackPane;
	@FXML
	private AnchorPane appBarAnchorPane;

	private static boolean checkString(String str) {
		char ch;
		boolean capitalFlag = false;
		boolean lowerCaseFlag = false;
		boolean numberFlag = false;
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if (Character.isDigit(ch)) {
				numberFlag = true;
			} else if (Character.isUpperCase(ch)) {
				capitalFlag = true;
			} else if (Character.isLowerCase(ch)) {
				lowerCaseFlag = true;
			}
			if (numberFlag && capitalFlag && lowerCaseFlag)
				return true;
		}
		return false;
	}

	public void initialize() {
		//init appBar
		javafx.scene.Node appBarComponent = null;
		try {
			App.setShowHelp(false); // show help or not
			App.setShowLogin(false); // show login or not
			App.setPageTitle("Create Account"); //set AppBar title
			App.setHelpText(""); //set help text
			App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
			appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
			appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createAccountButton(ActionEvent event) {
		App.userID = 0;

		if (email.getText().isEmpty()) {
			errorPopup("Must input an email");
			return;
		}
		if (password.getText().isEmpty()) {
			errorPopup("Must input password");
			return;
		}
		if (firstName.getText().isEmpty()) {
			errorPopup("Must input a First Name");
			return;
		}
		if (lastName.getText().isEmpty()) {
			errorPopup("Must input Last Name");
			return;
		}
		if (!email.getText().contains("@") || !email.getText().contains(".")) {
			errorPopup("Invalid email");
			return;
		}
		if (password.getText().length() < 8) {
			errorPopup("Password must be longer than 8 characters");
			return;
		}
		if (!password.getText().contains("$") && !password.getText().contains("@")
				&& !password.getText().contains("!") && !password.getText().contains("#")
				&& !password.getText().contains("%")) {
			errorPopup("Password must include a special character");
			return;
		}
		if (!checkString(password.getText())) {
			errorPopup("Password must include a capital letter and a number");
			return;
		}
		if (firstName != null && lastName != null && email != null && password != null) {
			DB.addUserAccount(email.getText(), password.getText(), firstName.getText(), lastName.getText());
			App.userID = DB.userLogin(email.getText(), password.getText());
		}
		if (App.userID != 0) {
			CreateAccountState createAccountState = new CreateAccountState();
			createAccountState.switchScene(event);
		}
	}

	@FXML
	private void errorPopup(String errorMessage) {
		App.newJFXDialogPopUp("Error!", "Okay", errorMessage, stackPane);
	}

	/**
	 * Switch to a different scene
	 * @param e tells which button was pressed
	 */
	@FXML
	public void switchScene(ActionEvent e) {
		CreateAccountState createAccountState = new CreateAccountState();
		createAccountState.switchScene(e);
	}


}
