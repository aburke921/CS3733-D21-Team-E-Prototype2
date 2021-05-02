package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;

public class ReligiousRequest extends ServiceRequestFormComponents {


    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();
    ObservableList<String> names;
    ArrayList<Integer> userID = new ArrayList<>();

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
    private StackPane stackPane;

    @FXML
    private AnchorPane appBarAnchorPane;

    private boolean validateInput() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Input Required");

        locationInput.getValidators().add(validator);
        religionInput.getValidators().add(validator);
        assignedPersonnel.getValidators().add(validator);
        description.getValidators().add(validator);

        return locationInput.validate() && religionInput.validate()
                && assignedPersonnel.validate() && description.validate();
    }

    @FXML
    void saveData(ActionEvent event) {

        if(validateInput()) {
            int locIndex = locationInput.getSelectionModel().getSelectedIndex();
            int userIndex = assignedPersonnel.getSelectionModel().getSelectedIndex();

            String node = nodeID.get(locIndex);
            int user = userID.get(userIndex);
            String religion = religionInput.getSelectionModel().getSelectedItem();
            String desc = description.getText();

            DB.addReligiousRequest(App.userID, node, user, religion, desc);
            super.handleButtonSubmit(event);
        }

    }

    @FXML
    void handleButtonCancel(ActionEvent event) {
        super.handleButtonCancel(event);
    }

    @FXML
    void initialize() {

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
        //TODO add user type
        names = DB.getAssigneeNames("religiousPerson");
        userID = DB.getAssigneeIDs("religiousPerson");

        locationInput.setItems(locations);
        assignedPersonnel.setItems(names);
    }

}
