package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.email.sendEmail;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;

public class LanguageInterpreter extends ServiceRequestFormComponents {

	ObservableList<String> locations;
	ArrayList<String> nodeID;

	@FXML // fx:id="background"
	private ImageView background;

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
	public AnchorPane appBarAnchorPane;

	@FXML
	private StackPane stackPane;


	@FXML
	void handleButtonCancel(ActionEvent event) {
		super.handleButtonCancel(event);
	}

	@FXML
	void saveData(ActionEvent event) throws MessagingException {
		int index = locationInput.getSelectionModel().getSelectedIndex();
		String node = nodeID.get(index);
		String assignee = assignedPersonnel.getSelectionModel().getSelectedItem();
		String descrip = descriptionInput.getText();
		String language = languageSelection.getSelectionModel().getSelectedItem();

		super.handleButtonSubmit(event);

		//For email implementation later
//		String email = DB.getEmail(App.userID);
//		String fullName = DB.getUserName(App.userID);
////		String assigneeName = userNames.get(assigneeIDIndex);
//		String locationName = locations.get(index);
//		String body = "Hello " + fullName + ", \n\n" + "Thank you for making a Language Interpretation request." +
//				"Here is the summary of your request: \n\n" +
//				" - Location: " + locationName + "\n" +
//				" - Assignee Name: " + assignee + "\n\n" +
//				" - descrip: " + descrip + "\n" +
//				" - language: " + language + "\n\n" +
//				"If you need to edit any details, please visit our app to do so. We look forward to seeing you soon!\n\n" +
//				"- Emerald Emus BWH";
//
//		sendEmail.sendRequestConfirmation(email, body);
	}

	@FXML
	void initialize() {

		Stage primaryStage = App.getPrimaryStage();
		Image backgroundImg = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
		Image backgroundImage = backgroundImg;
		background.setImage(backgroundImage);
		background.setEffect(new GaussianBlur());

		//background.setPreserveRatio(true);
		background.fitWidthProperty().bind(primaryStage.widthProperty());
		//background.fitHeightProperty().bind(primaryStage.heightProperty());

		//init appBar
		javafx.scene.Node appBarComponent = null;
		try {
			App.setShowHelp(false); // show help or not
			App.setShowLogin(true); // show login or not
			App.setPageTitle("Language Interpreter (Yihong Xu)"); //set AppBar title
			App.setHelpText(""); //set help text
			App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
			appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
			appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
		} catch (IOException e) {
			e.printStackTrace();
		}

		locations = DB.getAllNodeLongNames();
		locationInput.setItems(locations);
	}

}
