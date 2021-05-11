/**
 * Sample Skeleton for 'Laundry.fxml' Controller Class
 */

package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.lang.String;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.LaundryObj;
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

public class Laundry extends ServiceRequestFormComponents {

    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();
    ObservableList<String> userNames;
    ArrayList<Integer> userID = new ArrayList<>();

    @FXML // fx:id="background"
    private ImageView background;

    @FXML // fx:id="locationInput"
    private JFXComboBox<String> locationInput; // Value injected by FXMLLoader

    @FXML // fx:id="washLoadAmountInput"
    private JFXComboBox<String> washLoadAmountInput; // Value injected by FXMLLoader

    @FXML // fx:id="dryLoadAmountInput"
    private JFXComboBox<String> dryLoadAmountInput; // Value injected by FXMLLoader

    @FXML // fx:id="assignedPersonnel"
    private JFXComboBox<String> assignedPersonnel; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionInput"
    private JFXTextArea descriptionInput; // Value injected by FXMLLoader

    @FXML // fx:id="cancel"
    private JFXButton cancel; // Value injected by FXMLLoader

    @FXML // fx:id="submit"
    private JFXButton submit; // Value injected by FXMLLoader

    @FXML
    public AnchorPane appBarAnchorPane;

    @FXML
    private StackPane stackPane;

    private boolean validateInput() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Input required");

        locationInput.getValidators().add(validator);
        washLoadAmountInput.getValidators().add(validator);
        dryLoadAmountInput.getValidators().add(validator);
        assignedPersonnel.getValidators().add(validator);
        descriptionInput.getValidators().add(validator);

        return locationInput.validate() && washLoadAmountInput.validate() &&
                dryLoadAmountInput.validate() && assignedPersonnel.validate() &&
                descriptionInput.validate();
    }

    @FXML
    void saveData(ActionEvent event) {
        if(validateInput()) {
            //setting up indexes
            int nodeIndex = locationInput.getSelectionModel().getSelectedIndex();
            int userIndex = assignedPersonnel.getSelectionModel().getSelectedIndex();

            //Creating data to be added to object
            String node = nodeID.get(nodeIndex);
            int user = userID.get(userIndex);
            String wash = washLoadAmountInput.getSelectionModel().getSelectedItem();
            String dry = dryLoadAmountInput.getSelectionModel().getSelectedItem();
            String desc = descriptionInput.getText();

            //Creating object and passing to database
            LaundryObj object = new LaundryObj(0, node, user, App.userID, wash, dry, desc);
            DB.addLaundryRequest(object);
            super.handleButtonSubmit(event);
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        Stage primaryStage = App.getPrimaryStage();
        Image backgroundImg = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
        Image backgroundImage = backgroundImg;
        background.setImage(backgroundImage);
        background.setEffect(new GaussianBlur());

        //background.setPreserveRatio(true);
        background.fitWidthProperty().bind(primaryStage.widthProperty());
        //background.fitHeightProperty().bind(primaryStage.heightProperty());

        assert locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert washLoadAmountInput != null : "fx:id=\"washLoadAmountInput\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert dryLoadAmountInput != null : "fx:id=\"dryLoadAmountInput\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert assignedPersonnel != null : "fx:id=\"assignedPersonnel\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'Laundry.fxml'.";

        locations = DB.getAllNodeLongNames();
        locationInput.setItems(locations);
        userNames = DB.getAssigneeNames("custodian");
        assignedPersonnel.setItems(userNames);

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false); // show help or not
            App.setShowLogin(true); // show login or not
            App.setPageTitle("Laundry Request (Nupur Shukla)"); //set AppBar title
            App.setHelpText(""); //set help text
            App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
