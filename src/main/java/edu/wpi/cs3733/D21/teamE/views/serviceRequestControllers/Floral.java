package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.email.sendEmail;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.FloralObj;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;

public class Floral extends ServiceRequestFormComponents {

    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();
    ObservableList<String> userNames;
    ArrayList<Integer> userID = new ArrayList<>();

    @FXML // fx:id="background"
    private ImageView background;

    @FXML
    private JFXComboBox<String> locationInput;

    @FXML
    private JFXComboBox<String> flowerType;

    @FXML
    private JFXComboBox<String> flowerCount;

    @FXML
    private JFXComboBox<String> vaseType;

    @FXML
    private JFXComboBox assignee;

    @FXML
    private JFXTextField arrangementStyle;

    @FXML
    private JFXComboBox<String> teddyBear;

    @FXML
    private JFXComboBox<String> chocolate;

    @FXML
    private JFXTextField recipient;

    @FXML
    private JFXTextArea message;

    @FXML
    private AnchorPane appBarAnchorPane;

    @FXML
    private StackPane stackPane;

    private boolean validateInput() {

        RequiredFieldValidator validator = new RequiredFieldValidator();

        validator.setMessage("Input required");

        locationInput.getValidators().add(validator);
        flowerType.getValidators().add(validator);
        flowerCount.getValidators().add(validator);
        vaseType.getValidators().add(validator);
        assignee.getValidators().add(validator);
        message.getValidators().add(validator);
        chocolate.getValidators().add(validator);
        teddyBear.getValidators().add(validator);
        arrangementStyle.getValidators().add(validator);

        return  locationInput.validate() && flowerType.validate() && flowerCount.validate() &&
                vaseType.validate() && assignee.validate() && message.validate() && chocolate.validate()
                && teddyBear.validate() && arrangementStyle.validate();
    }

    @FXML
    private void saveData(ActionEvent e) throws MessagingException {

        if(validateInput()) {
            int locationIndex = locationInput.getSelectionModel().getSelectedIndex();
            int userIndex = assignee.getSelectionModel().getSelectedIndex();

            String nodeInfo = nodeID.get(locationIndex);
            String type = flowerType.getSelectionModel().getSelectedItem();
            int count = Integer.parseInt(flowerCount.getSelectionModel().getSelectedItem());
            String vase = vaseType.getSelectionModel().getSelectedItem();
            int assigned = userID.get(userIndex);
            String receiver = recipient.getText();
            String mess = message.getText();
            String arrangement = arrangementStyle.getText();
            String teddy = teddyBear.getSelectionModel().getSelectedItem();
            String choc = chocolate.getSelectionModel().getSelectedItem();
           // assigned is now an integer (userID) so must be changed

            FloralObj request = new FloralObj(0, App.userID, assigned, nodeInfo, receiver, type, count, vase, arrangement, teddy, choc, mess);
            DB.addFloralRequest(request);

            String email = DB.getEmail(App.userID);
            String fullName = DB.getUserName(App.userID);
            String assigneeName = userNames.get(userIndex);
            String locationName = locations.get(locationIndex);
            String body = "Hello " + fullName + ", \n\n" + "Thank you for making an Floral request. " +
                    "Here is the summary of your request: \n\n" +
                    " - Type: " + type + "\n" +
                    " - Flower Count: " + count + "\n" +
                    " - Vase Type: " + vase + "\n" +
                    " - Receiver: " + receiver + "\n" +
                    " - Message: " + mess + "\n" +
                    " - Floral Arrangement: " + arrangement + "\n" +
                    " - Teddy Bear: " + teddy + "\n" +
                    " - Chocolate: " + choc + "\n" +
                    " - Assignee Name: " + assigneeName + "\n" +
                    " - Location: " + locationName + "\n\n" +
                    "If you need to edit any details, please visit our app to do so. We look forward to seeing you soon!\n\n" +
                    "- Emerald Emus BWH";

            sendEmail.sendRequestConfirmation(email, body);

            super.handleButtonSubmit(e);
        }
    }

    @FXML
    void handleButtonSubmit(ActionEvent event) {
        if (validateInput()) {
            try {
                saveData(event);
                System.out.println(event); //Print the ActionEvent to console
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException | MessagingException ex) {
                ex.printStackTrace();
            }
        }
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

        nodeID = DB.getListOfNodeIDS();
        locations = DB.getAllNodeLongNames();
        userID = DB.getAssigneeIDs("floralPerson");
        userNames = DB.getAssigneeNames("floralPerson");
        assignee.setItems(userNames);
        locationInput.setItems(locations);
        assert locationInput != null;
        assert flowerType != null;
        assert flowerCount != null;
        assert vaseType != null;
        assert assignee != null;
        assert message != null;

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false); // show help or not
            App.setShowLogin(true); // show login or not
            App.setPageTitle("Floral (Jillian Wright)"); //set AppBar title
            App.setHelpText(""); //set help text
            App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
