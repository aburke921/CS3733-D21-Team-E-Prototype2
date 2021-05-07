package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.states.CreateAccountState;
import edu.wpi.cs3733.D21.teamE.states.LoginState;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="emailInput"
    private JFXTextField emailInput; // Value injected by FXMLLoader

    @FXML // fx:id="passwordInput"
    private JFXPasswordField passwordInput; // Value injected by FXMLLoader

    @FXML // fx:id="createAccountButton"
    private Button createAccountButton; // Value injected by FXMLLoader

    @FXML // fx:id="submitLoginButton"
    private Button submitLoginButton; // Value injected by FXMLLoader

    @FXML // fx:id="guestLoginButton"
    private Button guestLoginButton; // Value injected by FXMLLoader

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

	@FXML // fx:id="createAccountLabel"
	private Label createAccountLabel;

    @FXML
    public void initialize() {

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(true); //show help button
            App.setPageTitle(null); //set AppBar title
            App.setShowLogin(false); //dont show login button
            App.setHelpText("Continue as a guest or login to an existing account. If you have no account... "); //todo set help text
            App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }

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

		createAccountLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/createAccount.fxml"));
					App.getPrimaryStage().getScene().setRoot(root);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});


		//set submit as default (will submit on an "ENTER" keypress)
		submitLoginButton.setDefaultButton(true);

		//text validators
		RequiredFieldValidator notEmptyValidator = new RequiredFieldValidator();
//		notEmptyValidator.setMessage("Input Required"); //todo decide whether to increase spacing and include valid msg or not.
		emailInput.getValidators().add(notEmptyValidator); //email validator
		emailInput.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				emailInput.validate();
			}
		});
		passwordInput.getValidators().add(notEmptyValidator); //password validator
		passwordInput.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal) {
				passwordInput.validate();
			}
		});
	}

	@FXML
	public void submitLogin(ActionEvent actionEvent) {
		int userID = 0;

		if (emailInput != null && passwordInput != null) {
			userID = DB.userLogin(emailInput.getText(), passwordInput.getText());
			App.userID = userID; // app will be logged in as guest if userID = 0
		}
		if (userID != 0) {
			LoginState loginState = new LoginState();
			loginState.switchScene(actionEvent);
		} else {
		    App.newJFXDialogPopUp("User Not Found", "Try Again","This user cannot be found in the system.",stackPane);
			System.out.println("User not in System");
		}
	}

	@FXML
	public void toNewAccount(ActionEvent actionEvent) {
		LoginState loginState = new LoginState();
		loginState.switchScene(actionEvent);
	}

	@FXML
	public void guestLogin(ActionEvent e) {
    	App.noCleanSurveyYet = true;
		LoginState loginState = new LoginState();
		loginState.switchScene(e);
	}
}



