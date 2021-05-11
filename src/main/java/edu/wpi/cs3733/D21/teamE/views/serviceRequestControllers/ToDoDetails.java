package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.base.IFXValidatableControl;
import com.jfoenix.validation.IntegerValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.Date;
import edu.wpi.cs3733.D21.teamE.Time;
import edu.wpi.cs3733.D21.teamE.database.appointmentDB;
import edu.wpi.cs3733.D21.teamE.email.SheetsAndJava;
import edu.wpi.cs3733.D21.teamE.email.sendEmail;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.scheduler.Schedule;
import edu.wpi.cs3733.D21.teamE.scheduler.ToDo;
import edu.wpi.cs3733.D21.teamE.states.ServiceRequestState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ToDoDetails {

    private ToDo todo = null;

    private RequiredFieldValidator validator = new RequiredFieldValidator();
    private ObservableList<String> userNames;
    private ArrayList<Integer> userIDList;

    private ObservableList<String> longNameList;
    private ArrayList<String> nodeIDList;

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

    @FXML // fx:id="selfAssign"
    private JFXCheckBox selfAssign;
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
    private JFXCheckBox sendNotification; // Value injected by FXMLLoader
    @FXML // fx:id="notificationDate"
    private JFXDatePicker notificationDateInput; // Value injected by FXMLLoader
    @FXML // fx:id="notificationTime"
    private JFXTimePicker notificationTimeInput; // Value injected by FXMLLoader



    @FXML // fx:id="background"
    private ImageView background;

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
        background.fitWidthProperty().bind(primaryStage.widthProperty());

        String userType = DB.getUserType(App.userID);
        userIDList = DB.getAssigneeIDs(userType);
        userNames = DB.getAssigneeNames(userType);

        longNameList = FXCollections.observableArrayList();
        nodeIDList = new ArrayList<>();

        ArrayList<Node> nodeArrayList = DB.getAllNodes();
        for (int i = 0; i < nodeArrayList.size(); i++) {
            Node node = nodeArrayList.get(i);
            if (node.get("type").equalsIgnoreCase("HALL") || node.get("type").equalsIgnoreCase("WALK")) {

            } else {
                longNameList.add(node.get("longName"));
                nodeIDList.add(node.get("id"));
            }
        }

        ObservableList<String> statusOptions = FXCollections.observableArrayList();
        statusOptions.add("Ongoing");
        statusOptions.add("Completed");

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
        todo = App.getToDo();

        if(todo != null) {
            populateDetails();
        } else {
            statusInput.setValue("Ongoing");
            priorityInput.setValue("None");

            selfAssign.setSelected(true);
            userIDInput.setManaged(false);
        }

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

    public void initToDo(ToDo passedTodo){
        todo = passedTodo;
    }

    @FXML
    private void populateDetails() {
        String title = todo.getTitle();
        int userID = todo.getUserID();
        int status = todo.getStatus();
        int priority = todo.getPriority();

        Node location = todo.getLocation();
        Date scheduledDate = todo.getScheduledDate();
        Time startTime = todo.getStartTime();
        Time endTime = todo.getEndTime();
        String detail = todo.getDetail();
        Date notificationDate = todo.getNotificationDate();
        Time notificationTime = todo.getNotificationTime();

        if(title != null) {
            titleInput.setText(title);
        }
        if((Integer) userID != null) {
            if (userID == App.userID) {
                selfAssign.setSelected(true);
                userIDInput.setManaged(false);
                userIDInput.setVisible(false);
            } else {
                int index = -1;
                for (int i = 0; i < userIDList.toArray().length; i++) {
                    if (userIDList.get(i) == userID) {
                        index = i;
                    }
                }
                userIDInput.getSelectionModel().select(index);
            }
        }

        if((Integer) status != null) {
            if (status == 1) {
                statusInput.setValue("Ongoing");
            } else if (status == 10) {
                statusInput.setValue("Completed");
            } else {
                statusInput.setValue("Deleted");
            }
        }

        if((Integer) priority != null) {
            if (priority == 0) {
                priorityInput.setValue("None");
            } else if (priority == 1) {
                priorityInput.setValue("Low");
            } else if (priority == 2) {
                priorityInput.setValue("Medium");
            } else {
                priorityInput.setValue("High");
            }
        }

        if(location != null) {
            int index = -1;
            for(int i = 0; i < nodeIDList.toArray().length; i++) {
                if (nodeIDList.get(i).equals(location.get("id"))) {
                    index = i;
                }
            }
            locationInput.getSelectionModel().select(index);
        }

        if(detail != null) {
            additionalNotesInput.setText(detail);
        }
        if(!scheduledDate.isEmpty()) {
            LocalDate date = scheduledDate.toLocalDate();
            dateInput.setValue(date);
        }
        if(!startTime.isEmpty()) {
            LocalTime time = startTime.toLocalTime();
            startTimeInput.setValue(time);
        }
        if(!endTime.isEmpty()) {
            LocalTime time = endTime.toLocalTime();
            endTimeInput.setValue(time);
        }
        if(!notificationDate.isEmpty()) {
            LocalDate date = notificationDate.toLocalDate();
            notificationDateInput.setValue(date);
        }
        if(!notificationTime.isEmpty()) {
            LocalTime time = notificationTime.toLocalTime();
            notificationTimeInput.setValue(time);
        }
    }

    /**
     * records inputs from user into a series of String variables and returns to the main page
     */
    @FXML
    private void saveData() throws MessagingException, IOException, GeneralSecurityException {

        if (validateInput() && validateTimeConflict()) {

            String title = titleInput.getText();
            if(title != null) {
                System.out.println("title " + title);
            }
            String date = null;
            if(dateInput.getValue() != null) {
                date = dateInput.getValue().toString();
                System.out.println("date " + date);
            }
            String startTime = null;
            if(startTimeInput.getValue() != null) {
                startTime = startTimeInput.getValue().toString();
                System.out.println("startTime " + startTime);
            }
            String endTime = null;
            if(endTimeInput.getValue() != null) {
                endTime = endTimeInput.getValue().toString();
                System.out.println("endTime " + endTime);
            }

            String status = statusInput.getValue();
            int statusInt;
            if(status.equals("Ongoing")) {
                statusInt = 1;
            } else {
                statusInt = 10;
            }

            System.out.println("status " + status + " " + statusInt);

            String priority = priorityInput.getValue();
            int priorityInt;
            if(priority.equals("None")) {
                priorityInt = 0;
            } else if (priority.equals("Low")) {
                priorityInt = 1;
            } else if (priority.equals("Medium")) {
                priorityInt = 2;
            } else {
                priorityInt = 3;
            }
            System.out.println("priority " + priority + " " + priorityInt);

            String nodeID = null;
            if(locationInput.getSelectionModel().getSelectedItem() != null) {
                int locationIndex = locationInput.getSelectionModel().getSelectedIndex();
                System.out.println("locationIndex " + locationIndex);
                nodeID = nodeIDList.get(locationIndex);
            }

            int userID = App.userID;
            if(!selfAssign.isSelected()) {
                int userIndex = userIDInput.getSelectionModel().getSelectedIndex();
                System.out.println("userIndex " + userIndex);
                Integer userIDFromList = userIDList.get(userIndex);
                userID = userIDFromList;
            }

            String additionalNotes = additionalNotesInput.getText();
            if(additionalNotes != null) {
                System.out.println("additionalNotes " + additionalNotes);
            }

            String notificationTime = null;
            if (notificationTimeInput.getValue() != null) {
                notificationTime = notificationTimeInput.getValue().toString();
                System.out.println("notificationTime " + notificationTime);
            }
            String notificationDate = null;
            if (notificationDateInput.getValue() != null) {
                notificationDate = notificationDateInput.getValue().toString();
                System.out.println("notificationDate " + notificationDate);
            }

            int todoID;
            if(todo == null){
                todoID = DB.addCustomToDo(userID, title);
            } else {
                todoID = todo.getTodoID();
            }
            DB.updateToDo(todoID, title, userID, statusInt, priorityInt, nodeID, date, startTime, endTime,
                    additionalNotes, notificationDate, notificationTime);


            if(sendNotification.isSelected()) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Please Login To Our Gmail Account To Verify This App");
                alert.setHeaderText("Unfortunately, to receive a reminder for your ToDo, you will need to approve this app on your local machine.");
                alert.setContentText("Would you like to log in to our Software Engineering Gmail Account?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK){
                    // user chose OK
                    String email = DB.getEmail(userID);
                    String fullName = DB.getUserName(userID);
                    int position = fullName.indexOf(" ");

                    String firstName = fullName.substring(0, position);
                    String lastName = fullName.substring(position);
                    Node node = DB.getNodeInfo(nodeID);
                    String locationName = "";
                    if (node != null) {
                        locationName = node.get("longName");
                    }
                    String taskStartDateAndTime = date + " " + startTime;
                    String taskEndDateAndTime = date + " " + endTime;
                    String notificationDateAndTime = notificationDate + " " + notificationTime;

                    SheetsAndJava.addTodoToSheet(todoID, blankIfNull(title), email, firstName, lastName, blankIfNull(locationName), blankIfNull(taskStartDateAndTime), blankIfNull(taskEndDateAndTime), blankIfNull(notificationDateAndTime));

                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ScheduleList.fxml"));
                    App.changeScene(root);

                }
            }
            else{
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ScheduleList.fxml"));
                App.changeScene(root);
            }

            }

            todo = null;

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ScheduleList.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    }


    private String blankIfNull(String s) {
        return s == null ? "" : s;
    }

    /**
     * Detects if the user has entered all required fields
     */
    private boolean validateInput() {

        boolean valid = true;
        validator.setMessage("Input required");
        List<IFXValidatableControl> validatedControls = new LinkedList<IFXValidatableControl>(){
            @Override
            public boolean add(IFXValidatableControl control) {
                if(super.add(control)){
                    control.getValidators().add(validator);
                    return true;
                } else {
                    return false;
                }
            }
        };
        validatedControls.add(titleInput);
        validatedControls.add(statusInput);
        validatedControls.add(priorityInput);

        if(!selfAssign.isSelected()){
            validatedControls.add(userIDInput);
        }

        if(sendNotification.isSelected()){
            validatedControls.add(notificationDateInput);
            validatedControls.add(notificationTimeInput);
        }

        if(endTimeInput.getValue() != null){
            validatedControls.add(startTimeInput);
        }

        for(IFXValidatableControl control : validatedControls){
            valid &= control.validate();
        }

        return valid;
    }

    /**
     * Check if there are any time conflicts
     */
    private boolean validateTimeConflict() {
        boolean valid = true;

        int assignedUserID;
        if(!selfAssign.isSelected()){
            int userIndex = userIDInput.getSelectionModel().getSelectedIndex();
            Integer userIDFromList = userIDList.get(userIndex);
            assignedUserID = userIDFromList;
        } else {
            assignedUserID = App.userID;
        }

        Date date = new Date(dateInput.getValue());
        Time startTime = new Time(startTimeInput.getValue());
        Time endTime = new Time(endTimeInput.getValue());

        String errorMessage = "";

        if(!(startTime.isEmpty() || date.isEmpty())){
            valid &= startTime.compareTo(endTime) < 0;

            Schedule schedule = DB.getSchedule(assignedUserID, 1, date.toString());
            boolean noScheduleConflict = true;
            if(todo != null){
                //editing, check to see if existing can change
                noScheduleConflict = schedule.canChange(todo, startTime, endTime);

                if(noScheduleConflict == false) {
                    errorMessage += schedule.canChangeErrorMessage(todo, startTime, endTime);
                }

            } else {
                //adding, check to see if new can be placed
                noScheduleConflict = schedule.canAdd(startTime, endTime);

                if(noScheduleConflict == false) {
                    errorMessage += schedule.canAddErrorMessage(startTime, endTime);
                }
            }

            valid &= noScheduleConflict;
        }
        if(valid == false) {
            App.newJFXDialogPopUp("Time Conflict", "Try Again", errorMessage, stackPane);
        }
        return valid;
    }

    @FXML
    private void selfAssign(ActionEvent event) {
       if(((JFXCheckBox) event.getSource()).isSelected()) {
           userIDInput.setManaged(false);
           userIDInput.setVisible(false);
       } else {
           userIDInput.setManaged(true);
           userIDInput.setVisible(true);
       }
    }

    @FXML
    private void sendNotification(ActionEvent event) {
        if(((JFXCheckBox) event.getSource()).isSelected()) {
            notificationDateInput.setVisible(true);
            notificationTimeInput.setVisible(true);
        } else {
            notificationDateInput.setVisible(false);
            notificationTimeInput.setVisible(false);
        }
    }

    /**
     * Switch to a different scene
     * @param e tells which button was pressed
     */
    @FXML
    private void switchScene(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ScheduleList.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
