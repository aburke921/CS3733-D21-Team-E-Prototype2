package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.email.sendEmail;
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

public class MedicineDelivery extends ServiceRequestFormComponents {

    ObservableList<String> locations;
    ArrayList<String> nodeIDs;

    @FXML // fx:id="background"
    private ImageView background;

    @FXML
    private JFXComboBox<String> locationInput;

    @FXML
    private JFXTextField medicineNameInput;

    @FXML
    private JFXTextField doseQuantityInput;

    @FXML
    private JFXTextField doseMeasureInput;

    @FXML
    private JFXTextField assignee;

    @FXML
    private JFXTextArea specialInstructInput;

    @FXML
    private JFXTextField signatureInput;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton submit;

    @FXML
    public AnchorPane appBarAnchorPane;

    @FXML
    private StackPane stackPane;

    private boolean validateInput() {

        RequiredFieldValidator validator = new RequiredFieldValidator();

        validator.setMessage("Input required");

        locationInput.getValidators().add(validator);
        medicineNameInput.getValidators().add(validator);
        doseMeasureInput.getValidators().add(validator);
        doseQuantityInput.getValidators().add(validator);
        assignee.getValidators().add(validator);
        specialInstructInput.getValidators().add(validator);
        signatureInput.getValidators().add(validator);

        return  locationInput.validate() && medicineNameInput.validate() && doseMeasureInput.validate() && doseQuantityInput.validate() && assignee.validate() && specialInstructInput.validate() && signatureInput.validate();

    }

    @FXML
    private void saveData(ActionEvent e) throws MessagingException {

        int index = locationInput.getSelectionModel().getSelectedIndex();

        String location = nodeIDs.get(index);
        String name = medicineNameInput.getText();
        String doseMeasure = doseMeasureInput.getText();
        int doseQuantity = Integer.parseInt(doseQuantityInput.getText());
        int assigned = Integer.parseInt( assignee.getText());
        String specialInstructions = specialInstructInput.getText();
        String signature = signatureInput.getText();

        DB.addMedicineRequest(App.userID, assigned, location, name, doseQuantity, doseMeasure, specialInstructions, signature);

        //For email implementation later
//        String email = DB.getEmail(App.userID);
//        String fullName = DB.getUserName(App.userID);
////        String assigneeName = userNames.get(assigned);
////        String locationName = locations.get(nodeIDIndex);
//        String body = "Hello " + fullName + ", \n\n" + "Thank you for making an External Patient Transport request." +
//                "Here is the summary of your request: \n\n" +
//                " - Location: " + location + "\n" +
//                " - Medicine Name: " + name + "\n" +
//                " - Medicine Dosage: " + doseMeasure + "\n" +
//                " - Does Quantity: " + doseQuantity + "\n" +
//                " - Assignee Name: " + assigned + "\n" +
//                " - Special Instructions: " + specialInstructions + "\n" +
//                " - Signature: " + signatureInput + "\n\n" +
//                "If you need to edit any details, please visit our app to do so. We look forward to seeing you soon!\n\n" +
//                "- Emerald Emus BWH";
//
//        sendEmail.sendRequestConfirmation(email, body);

        super.handleButtonSubmit(e);
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

        locations = DB.getAllNodeLongNames();
        nodeIDs = DB.getListOfNodeIDS();

        locationInput.setItems(locations);

        assert locationInput != null;
        assert medicineNameInput != null;
        assert doseQuantityInput != null;
        assert doseMeasureInput != null;
        assert specialInstructInput != null;
        assert signatureInput != null;
        assert submit != null;
        assert cancel != null;

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false); // show help or not
            App.setShowLogin(true); // show login or not
            App.setPageTitle("Medicine Delivery (Shannen Lin)"); //set AppBar title
            App.setHelpText(""); //set help text
            App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
