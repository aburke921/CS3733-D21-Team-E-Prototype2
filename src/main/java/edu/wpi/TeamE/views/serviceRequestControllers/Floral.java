package edu.wpi.TeamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.TeamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.TeamE.algorithms.Node;

import edu.wpi.TeamE.views.serviceRequestControllers.ServiceRequestFormComponents;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;

public class Floral extends ServiceRequestFormComponents {

    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();

    @FXML
    private JFXComboBox<String> locationInput;

    @FXML
    private JFXComboBox<String> flowerType;

    @FXML
    private JFXComboBox<String> flowerCount;

    @FXML
    private JFXComboBox<String> vaseType;

    @FXML
    private JFXTextField assignee;

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

        return  locationInput.validate() && flowerType.validate() && flowerCount.validate() && vaseType.validate() && assignee.validate() && message.validate();


    }

    private void saveData(ActionEvent e) {

        if(validateInput()) {
            int index = locationInput.getSelectionModel().getSelectedIndex();

            String nodeInfo = nodeID.get(index);
            String type = flowerType.getSelectionModel().getSelectedItem();
            String count = flowerCount.getSelectionModel().getSelectedItem();
            String vase = vaseType.getSelectionModel().getSelectedItem().toString();
            String assigned = assignee.getText(); //TODO this needs to be an int now (userID)
            String reciever = recipient.getText();
            String mess = message.getText();

            DB.addFloralRequest(App.userID, assigned, nodeInfo, reciever, type, 12, vase, mess);

        }
    }

    @FXML
    void handleButtonSubmit(ActionEvent event) {
        if (validateInput()) {
            try {
                saveData(event);
                System.out.println(event); //Print the ActionEvent to console
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() {
        nodeID = NodeDB.getListOfNodeIDS();
        locations = NodeDB.getAllNodeLongNames();
        locationInput.setItems(locations);
        assert locationInput != null;
        assert flowerType != null;
        assert flowerCount != null;
        assert vaseType != null;
        assert assignee != null;
        assert message != null;

    }
}
