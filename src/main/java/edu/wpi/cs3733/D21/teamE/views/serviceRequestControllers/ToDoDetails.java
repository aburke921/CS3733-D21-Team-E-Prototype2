package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.database.appointmentDB;
import edu.wpi.cs3733.D21.teamE.email.SheetsAndJava;
import edu.wpi.cs3733.D21.teamE.email.sendEmail;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.states.ServiceRequestState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
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
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ToDoDetails extends ServiceRequestFormComponents{

    RequiredFieldValidator validator = new RequiredFieldValidator();
    ObservableList<String> userNames;
    ArrayList<Integer> userIDList;

    ObservableList<String> longNameList;
    ArrayList<String> nodeIDList;

    @FXML // fx:id="background"
    private ImageView background;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="fullscreen"
    private Rectangle fullscreen; // Value injected by FXMLLoader

    @FXML // fx:id="hide"
    private Circle hide; // Value injected by FXMLLoader

    @FXML // fx:id="exit"
    private Polygon exit; // Value injected by FXMLLoader



    @FXML // fx:id="dateInput"
    private JFXDatePicker dateInput; // Value injected by FXMLLoader
    @FXML // fx:id="startTimeInput"
    private JFXTimePicker startTimeInput; // Value injected by FXMLLoader
    @FXML // fx:id="endTimeInput"
    private JFXTimePicker endTimeInput; // Value injected by FXMLLoader

    @FXML // fx:id="userIDInput"
    private JFXComboBox<String> userIDInput; // Value injected by FXMLLoader
    @FXML // fx:id="additionalNotesInput"
    private JFXTextArea additionalNotesInput; // Value injected by FXMLLoader
    @FXML // fx:id="titleInput"
    private JFXTextField titleInput;
    @FXML // fx:id="locationInput"
    private JFXComboBox<String> locationInput; // Value injected by FXMLLoader
    @FXML // fx:id="statusInput"
    private JFXComboBox<String> statusInput; // Value injected by FXMLLoader
    @FXML // fx:id="priorityInput"
    private JFXComboBox<String> priorityInput; // Value injected by FXMLLoader

    @FXML // fx:id="notificationDate"
    private JFXDatePicker notificationDateInput; // Value injected by FXMLLoader
    @FXML // fx:id="notificationTime"
    private JFXTimePicker notificationTimeInput; // Value injected by FXMLLoader

    @FXML
    public AnchorPane appBarAnchorPane;

    @FXML
    private StackPane stackPane;

    @FXML // fx:id="cancel"
    private JFXButton cancel; // Value injected by FXMLLoader

    @FXML // fx:id="submit"
    private JFXButton submit; // Value injected by FXMLLoader


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

        //ObservableList<String> userNames;
        //ArrayList<Integer> userID = new ArrayList<>();

        userIDList = DB.getAssigneeIDs("doctor");
        userNames = DB.getAssigneeNames("doctor");

        longNameList = FXCollections.observableArrayList();
        nodeIDList = new ArrayList<>();

        ArrayList<Node> nodeArrayList = DB.getAllNodes();
        for (int i = 0; i < nodeArrayList.size(); i++) {
            Node node = nodeArrayList.get(i);
            if (node.get("type").equalsIgnoreCase("HALL") || node.get("type").equalsIgnoreCase("WALK")) {
                nodeArrayList.remove(i--);
            } else {
                longNameList.add(node.get("longName"));
                nodeIDList.add(node.get("id"));
            }
        }

        ObservableList<String> statusOptions = FXCollections.observableArrayList();
        statusOptions.add("Ongoing");
        statusOptions.add("Completed");
        statusOptions.add("Deleted");

        ObservableList<String> priorityOptions = FXCollections.observableArrayList();
        priorityOptions.add("None");
        priorityOptions.add("Low");
        priorityOptions.add("Medium");
        priorityOptions.add("High");

        assert fullscreen != null : "fx:id=\"fullscreen\" was not injected: check your FXML file 'ToDoDetails.fxml'.";
        assert hide != null : "fx:id=\"hide\" was not injected: check your FXML file 'ToDoDetails.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'ToDoDetails.fxml'.";
        assert userIDInput != null : "fx:id=\"userIDInput\" was not injected: check your FXML file 'ToDoDetails.fxml'.";
        assert locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file 'ToDoDetails.fxml'.";
        assert statusInput != null : "fx:id=\"statusInput\" was not injected: check your FXML file 'ToDoDetails.fxml'.";
        assert priorityInput != null : "fx:id=\"priorityInput\" was not injected: check your FXML file 'ToDoDetails.fxml'.";

        userIDInput.setItems(userNames);
        locationInput.setItems(longNameList);
        statusInput.setItems(statusOptions);
        priorityInput.setItems(priorityOptions);

        assert additionalNotesInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'ToDoDetails.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'ToDoDetails.fxml'.";
        assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'ToDoDetails.fxml'.";

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false); // show help or not
            App.setShowLogin(true); // show login or not
            App.setPageTitle("Details"); //set AppBar title
            App.setHelpText(""); //set help text
            App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * records inputs from user into a series of String variables and returns to the main page
     * @param actionEvent
     */
    @FXML
    private void saveData(ActionEvent actionEvent) throws MessagingException, IOException, GeneralSecurityException {


        if (validateInput()) {

            String title = titleInput.getText();
            System.out.println("title " + title);
            String date = dateInput.getValue().toString();
            System.out.println("date " + date);
            String startTime = startTimeInput.getValue().toString();
            System.out.println("startTime " + startTime);
            String endTime = endTimeInput.getValue().toString();
            System.out.println("endTime " + endTime);

            String status = statusInput.getSelectionModel().getSelectedItem();
            int statusInt;
            if(status.equals("Ongoing")) {
                statusInt = 1;
            } else if (status.equals("Completed")) {
                statusInt = 10;
            } else {
                statusInt = 0;
            }
            System.out.println("status " + status + " " + statusInt);

            String priority = priorityInput.getSelectionModel().getSelectedItem();
            int priorityInt;
            if(status.equals("None")) {
                priorityInt = 0;
            } else if (status.equals("Low")) {
                priorityInt = 1;
            } else if (status.equals("Medium")) {
                priorityInt = 2;
            } else {
                priorityInt = 3;
            }
            System.out.println("priority " + priority + " " + priorityInt);

            int locationIndex = locationInput.getSelectionModel().getSelectedIndex();
            System.out.println("locationIndex " + locationIndex);
            String nodeID = nodeIDList.get(locationIndex);

            int userIndex = userIDInput.getSelectionModel().getSelectedIndex();
            System.out.println("userIndex " + userIndex);
            Integer userID = userIDList.get(userIndex);

            String additionalNotes = additionalNotesInput.getText();
            System.out.println("additionalNotes " + additionalNotes);

            String notificationTime = notificationTimeInput.getValue().toString();
            System.out.println("notificationTime " + notificationTime);
            String notificationDate = notificationDateInput.getValue().toString();
            System.out.println("date " + date);

//            DB.addAppointment(App.userID, startTime, date, doctorID);

            String email = DB.getEmail(App.userID);
            String fullName = DB.getUserName(App.userID);
//            sendEmail.sendAppointmentConfirmation(email, startTime, fullName);
            int position = fullName.indexOf(" ");

            String firstName = fullName.substring(0, position);
            String lastName = fullName.substring(position);
            String taskDateAndTime = date + " " + startTime;
            String notificationDateAndTime = notificationDate + " " + notificationTime;
//            int appointmentID = appointmentDB.getAppointmentID(App.userID, startTime, date);

            int todoID = 0;

            //SheetsAndJava.addTodoToSheet(todoID, title, email, firstName, lastName, );

            super.handleButtonSubmit(actionEvent);
        }
    }

    /**
     * Detects if the user has entered all required fields
     */
    private boolean validateInput() {

        validator.setMessage("Input required");

        titleInput.getValidators().add(validator);
        userIDInput.getValidators().add(validator);
        dateInput.getValidators().add(validator);
        startTimeInput.getValidators().add(validator);
        endTimeInput.getValidators().add(validator);
        locationInput.getValidators().add(validator);
        additionalNotesInput.getValidators().add(validator);
        statusInput.getValidators().add(validator);
        priorityInput.getValidators().add(validator);
        notificationDateInput.getValidators().add(validator);
        notificationTimeInput.getValidators().add(validator);

        return titleInput.validate() && userIDInput.validate() && dateInput.validate()
                && startTimeInput.validate() && endTimeInput.validate() && locationInput.validate()
                && additionalNotesInput.validate() && statusInput.validate() && priorityInput.validate()
                && notificationDateInput.validate() && notificationTimeInput.validate() ;
    }

    /**
     * Switch to a different scene
     * @param e tells which button was pressed
     */
    @FXML
    private void switchScene(ActionEvent e) {
        ServiceRequestState serviceRequestState = new ServiceRequestState();
        serviceRequestState.switchScene(e);
    }

}
