package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class InternalPatient extends ServiceRequestFormComponents{
    @FXML
    private JFXComboBox<String> pickupInput;
    @FXML
    private JFXComboBox<String> dropoffInput;
    @FXML
    private JFXTextField departmentInput;
    @FXML
    private JFXComboBox<String> severityInput;
    @FXML
    private JFXTextField patientIdInput;
    @FXML
    private JFXTextField assignedPersonnel;
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

    public void saveData(ActionEvent actionEvent) {


    }

    @FXML
    void initialize() {
        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false); // show help or not
            App.setShowLogin(true); // show login or not
            App.setPageTitle("Internal Patient Transportation (Shane Donahue)"); //set AppBar title
            App.setHelpText(""); //set help text
            App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList<String> locations = DB.getAllNodeLongNames();
        pickupInput.setItems(locations);
        dropoffInput.setItems(locations);
    }
}
