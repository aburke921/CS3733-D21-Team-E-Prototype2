package edu.wpi.TeamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.TeamE.App;
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.databases.makeConnection;
import edu.wpi.TeamE.views.serviceRequestControllers.ServiceRequestFormComponents;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class Floral extends ServiceRequestFormComponents {

    ArrayList<Node> nodes = new ArrayList<>();
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

//    @FXML private TextField patientName;
//    @FXML private TextField roomNum;
//    @FXML private CheckBox roseBox;
//    @FXML private CheckBox tulipBox;
//    @FXML private CheckBox carnationBox;
//    @FXML private CheckBox assortmentBox;
//    @FXML private CheckBox singleBox;
//    @FXML private CheckBox halfDozBox;
//    @FXML private CheckBox dozBox;
//    @FXML private CheckBox roundBox;
//    @FXML private CheckBox squareBox;
//    @FXML private CheckBox tallBox;
//    @FXML private CheckBox noneBox;

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
            makeConnection connection = makeConnection.makeConnection();
            int index = locationInput.getSelectionModel().getSelectedIndex();

            String nodeInfo = nodeID.get(index);
            String type = flowerType.getSelectionModel().getSelectedItem();
            String count = flowerCount.getSelectionModel().getSelectedItem();
            String vase = vaseType.getSelectionModel().getSelectedItem();
            String assigned = assignee.getText();
            String reciever = recipient.getText();
            String mess = message.getText();

            connection.addFloralRequest(App.userID, assigned, nodeInfo, reciever, type, 12, vase, mess);

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

//    public void checkOffRoses(ActionEvent a) {
//        this.roseBox.setSelected(true);
//    }
//
//    public void checkOffTulips(ActionEvent a) {
//        this.tulipBox.setSelected(true);
//    }
//
//    public void checkOffCarnations(ActionEvent a) {
//        this.carnationBox.setSelected(true);
//    }
//
//    public void checkOffAssortment(ActionEvent a) {
//        this.assortmentBox.setSelected(true);
//    }
//
//    public void checkOffSingle(ActionEvent a) {
//        this.singleBox.setSelected(true);
//    }
//
//    public void checkOffHalfDoz(ActionEvent a) {
//        this.halfDozBox.setSelected(true);
//    }
//
//    public void checkOffDoz(ActionEvent a) {
//        this.dozBox.setSelected(true);
//    }
//
//    public void checkOffRound(ActionEvent a) {
//        this.roundBox.setSelected(true);
//    }
//
//    public void checkOffSquare(ActionEvent a) {
//        this.squareBox.setSelected(true);
//    }
//
//    public void checkOffTall(ActionEvent a) {
//        this.tallBox.setSelected(true);
//    }
//
//    public void checkOffNone(ActionEvent a) {
//        this.noneBox.setSelected(true);
//    }

    @FXML
    void initialize() {
        makeConnection connection = makeConnection.makeConnection();
        nodeID = connection.getListOfNodeIDS();
        locations = connection.getAllNodeLongNames();
        locationInput.setItems(locations);
        assert locationInput != null;
        assert flowerType != null;
        assert flowerCount != null;
        assert vaseType != null;
        assert assignee != null;
        assert message != null;

    }
}
