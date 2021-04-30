package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.ArrayList;

public class Floral extends ServiceRequestFormComponents {

    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();
    ObservableList<String> userNames;
    ArrayList<Integer> userID = new ArrayList<>();

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

    public void getHelpFloralDelivery(ActionEvent actionEvent) {
    }

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
    private void saveData(ActionEvent e) {

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
            DB.addFloralRequest(App.userID, assigned, nodeInfo, receiver, type, count, vase, arrangement, teddy, choc, mess);
        }
    }

    @FXML
    void initialize() {
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

    }
}
