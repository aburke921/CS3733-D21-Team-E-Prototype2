/**
 * Sample Skeleton for 'UserManagement.fxml' Controller Class
 */

package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;

public class UserManagement {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane; // Value injected by FXMLLoader

    @FXML
    private JFXTextField userNameInput;

    @FXML
    private JFXTextField userIDInput;

    @FXML
    private JFXTextField userTypeInput;

    @FXML // fx:id="treeTableView"
    private TreeTableView<User> treeTableView; // Value injected by FXMLLoader

//    @FXML // fx:id="userIDEditableColumn"
//    private JFXTreeTableColumn<User, ?> userIDColumn; // Value injected by FXMLLoader
//
//    @FXML // fx:id="fullNameEditableColumn"
//    private JFXTreeTableColumn<User, ?> fullNameColumn; // Value injected by FXMLLoader
//
//    @FXML
//    private JFXTreeTableColumn<User, ?> userTypeEditableColumn;

    @FXML // fx:id="backButton"
    private JFXButton backButton; // Value injected by FXMLLoader

    @FXML
    void addUserButton(ActionEvent event) {

    }

    @FXML
    void deleteUserButton(ActionEvent event) {

    }

    @FXML
    void editUserButton(ActionEvent event) {

    }

    @FXML
    void startTableButton(ActionEvent event) {

    }

    @FXML
    void toNavigation(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert appBarAnchorPane != null : "fx:id=\"appBarAnchorPane\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert treeTableView != null : "fx:id=\"treeTableView\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'UserManagement.fxml'.";

        //todo fill Table with user data

        //get user data
        HashMap<Integer, String> visitorHashMap = DB.getSelectableAssignees("visitor");
        HashMap<Integer, String> doctorHashMap = DB.getSelectableAssignees("doctor");
        HashMap<Integer, String> patientHashMap = DB.getSelectableAssignees("patient");
        HashMap<Integer, String> adminHashMap = DB.getSelectableAssignees("admin");
        System.out.println(visitorHashMap);

        //add user data to userlist
//        ObservableList<User> users = FXCollections.observableArrayList();
        ArrayList<User> users = new ArrayList<>(); //todo maybe use above ObservableList?
        visitorHashMap.forEach((type, id) -> {
            //User(name, type, id)
            users.add(new User("visitor", type, id));
        });
        doctorHashMap.forEach((type, id) -> {
            //User(name, type, id)
            users.add(new User("doctor", type, id));
        });
        patientHashMap.forEach((type, id) -> {
            //User(name, type, id)
            users.add(new User("patient", type, id));
        });
        adminHashMap.forEach((type, id) -> {
            //User(name, type, id)
            users.add(new User("admin", type, id));
        });
        System.out.println("userslist: " + users);

        //todo build tree
        //add users to table
        prepareUsers(treeTableView, users);

        /* todo
            addSpecialUserType()
            editUserAccount()
            deleteUserAccount()
         */
    }

    private void editUser() {
        int id = Integer.parseInt(userIDInput.getText());
        String username = userNameInput.getText();
        String userType = userTypeInput.getText();

        DB.addUserAccount(username,);

    }

    private void addUser() {
        
    }

    private void deleteUser() {

    }




    //User class
    private static final class User {
        final StringProperty userName;
        final IntegerProperty userID;
        final StringProperty userType;

        public String getUserName() {
            return userName.get();
        }

        public StringProperty userNameProperty() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName.set(userName);
        }

        public int getUserID() {
            return userID.get();
        }

        public IntegerProperty userIDProperty() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID.set(userID);
        }

        public String getUserType() {
            return userType.get();
        }

        public StringProperty userTypeProperty() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType.set(userType);
        }

        User(String userType, int userID, String userName) {
            this.userType = new SimpleStringProperty(userType);
            this.userID = new SimpleIntegerProperty(userID);
            this.userName = new SimpleStringProperty(userName);
        }
    }

    public void prepareUsers(TreeTableView<User> table, ArrayList<User> array) {
        if (table.getRoot() == null) {
            User user0 = new User("", 0, "");
            final TreeItem<User> rootUser = new TreeItem<>(user0);
            table.setRoot(rootUser);

            //column 1 - ID
            TreeTableColumn<User, String> column1 = new TreeTableColumn<>("User Name");
            column1.setPrefWidth(320);
            column1.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getUserName()));
            table.getColumns().add(column1);

            //column 2 - Start Node
            TreeTableColumn<User, Number> column2 = new TreeTableColumn<>("ID");
            column2.setPrefWidth(320);
            column2.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getUserID()));
            table.getColumns().add(column2);

            //column 3 - End Node
            TreeTableColumn<User, String> column3 = new TreeTableColumn<>("User Type");
            column3.setPrefWidth(320);
            column3.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getUserType()));
            table.getColumns().add(column3);
        }
        table.setShowRoot(false);
        for (int i = 0; i < array.size(); i++) {
            User s = array.get(i);
            //int n = array.get(i).getX();
            final TreeItem<User> user = new TreeItem<>(s);
            table.getRoot().getChildren().add(user);
        }
    }

}


