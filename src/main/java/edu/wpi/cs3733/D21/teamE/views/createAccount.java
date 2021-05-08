package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.states.CovidSurveyState;
import edu.wpi.cs3733.D21.teamE.states.CreateAccountState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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

	@FXML // fx:id="imageView"
	private ImageView hospitalImageView;

	@FXML // fx:id="imageView"
	private ImageView logoImageView;

	@FXML // fx:id="imageAnchorPane"
	private AnchorPane imageAnchorPane;

	@FXML // fx:id="rightAnchorPane"
	private AnchorPane rightAnchorPane;


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

		//Set up images
		Stage primaryStage = App.getPrimaryStage();

		Image hospital = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
		hospitalImageView.setImage(hospital);
		hospitalImageView.setPreserveRatio(true);

		hospitalImageView.fitHeightProperty().bind(primaryStage.heightProperty());
		hospitalImageView.fitWidthProperty().bind(primaryStage.widthProperty());
		imageAnchorPane.prefWidthProperty().bind(primaryStage.widthProperty());
		imageAnchorPane.prefHeightProperty().bind(primaryStage.heightProperty());

		Image logo = new Image("edu/wpi/cs3733/D21/teamE/fullLogo.png");
		logoImageView.setImage(logo);
		logoImageView.setPreserveRatio(true);
		rightAnchorPane.prefWidthProperty().bind(primaryStage.widthProperty());
		rightAnchorPane.prefHeightProperty().bind(primaryStage.heightProperty());

		//init appBar
		Node appBarComponent = null;
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

		//add field validation
		addValidators();

	}

	private void addValidators() {
		//email validator
		RegexValidator emailValidator = new RegexValidator();
		emailValidator.setRegexPattern("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
		emailValidator.setMessage("Invalid Email");

		email.getValidators().add(emailValidator);
		email.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				email.validate();
			}
		});

		//password validator
		RegexValidator passwordValidator = new RegexValidator();
		passwordValidator.setMessage("Invalid Password");
		passwordValidator.setRegexPattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"); //todo, if a different special char is inputted, will error out. Eg: ~^-+

		password.getValidators().add(passwordValidator);
		password.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				password.validate();
			}
		});

		//First & last Name validators
		RequiredFieldValidator notEmptyValidator = new RequiredFieldValidator();
		notEmptyValidator.setMessage("Input Required");

		firstName.getValidators().add(notEmptyValidator);
		firstName.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				firstName.validate();
			}
		});

		lastName.getValidators().add(notEmptyValidator);
		lastName.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				lastName.validate();
			}
		});
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
