package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
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

//    @FXML // fx:id="fullscreen"
//    private Rectangle fullscreen; // Value injected by FXMLLoader
//
//    @FXML // fx:id="hide"
//    private Circle hide; // Value injected by FXMLLoader
//
//    @FXML // fx:id="exit"
//    private Polygon exit; // Value injected by FXMLLoader

    @FXML // fx:id="emailInput"
    private JFXTextField emailInput; // Value injected by FXMLLoader

    @FXML // fx:id="passwordInput"
    private JFXPasswordField passwordInput; // Value injected by FXMLLoader

    @FXML // fx:id="createAccountButton"
    private Button createAccountButton; // Value injected by FXMLLoader

    @FXML // fx:id="submitLoginButton"
//    private Button submitLoginButton; // Value injected by FXMLLoader
    public Button submitLoginButton; //todo is this intentionally public?

    @FXML // fx:id="guestLoginButton"
    private Button guestLoginButton; // Value injected by FXMLLoader

    @FXML private StackPane stackPane;

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
		hospitalImageView.setFitHeight(primaryStage.getHeight());
		hospitalImageView.fitHeightProperty().bind(primaryStage.heightProperty());

		imageAnchorPane.prefWidthProperty().bind(primaryStage.widthProperty());
		imageAnchorPane.prefHeightProperty().bind(primaryStage.heightProperty());

		Rectangle2D viewport = new Rectangle2D(100, 0, hospital.getWidth(), hospital.getHeight());
		hospitalImageView.setViewport(viewport);

		Image logo = new Image("edu/wpi/cs3733/D21/teamE/logo.png");
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
	}

	public void submitLogin() {
		int userID = 0;

		if (emailInput != null && passwordInput != null) {
			userID = DB.userLogin(emailInput.getText(), passwordInput.getText());
			App.userID = userID; // app will be logged in as guest if userID = 0
		}
		if (userID != 0) {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
				App.getPrimaryStage().getScene().setRoot(root);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
		    App.newJFXDialogPopUp("User Not Found", "Try Again","This user cannot be found in the system.",stackPane);
			System.out.println("User not in System");
		}

//    public void submitLogin() {
//        int userID = 0;
//        makeConnection connection = makeConnection.makeConnection();
//        if(emailInput.getText().isEmpty()) {
//            errorPopup("Must input an email");
//            return;
//        }
//        if(passwordInput.getText().isEmpty()) {
//            errorPopup("Must input password");
//            return;
//        }
//        if(emailInput != null && passwordInput != null) {
//            userID = connection.userLogin(emailInput.getText(), passwordInput.getText());
//        } if(userID != 0) {
//            try {
//                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
//                App.getPrimaryStage().getScene().setRoot(root);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        } else {
//            errorPopup("Incorrect Email or Password");
//        }

	}

	public void toNewAccount(ActionEvent actionEvent) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/createAccount.fxml"));
			App.getPrimaryStage().getScene().setRoot(root);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	public void getHelpDefault(ActionEvent actionEvent) {
	}

	@FXML
	public void guestLogin(ActionEvent e) {
    	App.noCleanSurveyYet = true;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
			App.getPrimaryStage().getScene().setRoot(root);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	private void errorPopup(String errorMessage) {
		System.out.println("errorMessage: " + errorMessage);
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
}



