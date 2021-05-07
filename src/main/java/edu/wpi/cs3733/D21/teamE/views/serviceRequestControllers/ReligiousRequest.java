package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.ReligiousRequestObj;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ReligiousRequest extends ServiceRequestFormComponents {

    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();
    ObservableList<String> userNames;
    ArrayList<Integer> userID = new ArrayList<>();

    @FXML // fx:id="background"
    private ImageView background;

    @FXML
    private JFXComboBox<String> locationInput;

    @FXML
    private JFXComboBox<String> religionInput;

    @FXML
    private JFXComboBox<String> assignedPersonnel;

    @FXML
    private JFXTextArea description;

    @FXML
    private JFXButton submit;

    @FXML
    private JFXButton cancel;

    @FXML
    public AnchorPane appBarAnchorPane;

    @FXML
    private StackPane stackPane;

    private boolean validateInput() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Input required");

        locationInput.getValidators().add(validator);
        religionInput.getValidators().add(validator);
        assignedPersonnel.getValidators().add(validator);
        description.getValidators().add(validator);

        return  locationInput.validate() && religionInput.validate() &&
                assignedPersonnel.validate() && description.validate();
    }

    @FXML
    void saveData(ActionEvent event) {
        if(validateInput()) {
            int nodeIndex = locationInput.getSelectionModel().getSelectedIndex();
            int assigneeIndex = assignedPersonnel.getSelectionModel().getSelectedIndex();

            String node = nodeID.get(nodeIndex);
            String religion = religionInput.getSelectionModel().getSelectedItem();
            int user = userID.get(assigneeIndex);
            String desc = description.getText();

            ReligiousRequestObj object = new ReligiousRequestObj(0, App.userID, node, user, religion, desc);
            DB.addReligiousRequest(object);
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


        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false); // show help or not
            App.setShowLogin(true); // show login or not
            App.setPageTitle("Religious Request (Aidan Mulcahey)"); //set AppBar title
            App.setHelpText(""); //set help text
            App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }

        locations = DB.getAllNodeLongNames();
        nodeID = DB.getListOfNodeIDS();
        locationInput.setItems(locations);

        userNames = DB.getAssigneeNames("religious");
        userID = DB.getAssigneeIDs("religious");
        assignedPersonnel.setItems(userNames);
    }

}
