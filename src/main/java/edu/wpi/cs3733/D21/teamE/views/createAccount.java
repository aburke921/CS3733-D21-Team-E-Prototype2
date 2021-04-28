package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.io.IOException;

public class createAccount {

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

	@FXML // fx:id="exit"
	private Polygon exit;

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
		//If exit button is clicked, exit app
		exit.setOnMouseClicked(event -> {
			App app = new App();
			app.stop();
		});
	}

	public void createAccountButton() {
		int userID = 0;

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
			userID = DB.userLogin(email.getText(), password.getText());
		}
		if (userID != 0) {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
				App.getPrimaryStage().getScene().setRoot(root);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@FXML
	public void getHelpDefault(ActionEvent actionEvent) {
	}

	/**
	 * Terminate application
	 * @param e
	 */
	@FXML
	private void shutdown(ActionEvent e) {
		App app = new App();
		app.stop();
	}

	@FXML
	private void errorPopup(String errorMessage) {
		JFXDialogLayout error = new JFXDialogLayout();
		error.setHeading(new Text("Error!"));
		error.setBody(new Text(errorMessage));
		JFXDialog dialog = new JFXDialog(stackPane, error, JFXDialog.DialogTransition.CENTER);
		JFXButton okay = new JFXButton("Okay");
		okay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dialog.close();

			}
		});
		error.setActions(okay);
		dialog.show();
	}

	@FXML
	public void toLogin(ActionEvent e) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Login.fxml"));
			App.getPrimaryStage().getScene().setRoot(root);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


}
