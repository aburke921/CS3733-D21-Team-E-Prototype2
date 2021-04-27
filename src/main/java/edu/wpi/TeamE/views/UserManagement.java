/**
 * Sample Skeleton for 'UserManagement.fxml' Controller Class
 */

package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import edu.wpi.TeamE.algorithms.Edge;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class UserManagement {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane; // Value injected by FXMLLoader

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

        // build tree
        //todo
    }




    //User class
    private static final class User {
        final StringProperty userName;
        final IntegerProperty userType;
        final StringProperty userID;

        User(String userID, int userType, String userName) {
            this.userID = new SimpleStringProperty(userID);
            this.userType = new SimpleIntegerProperty(userType);
            this.userName = new SimpleStringProperty(userName);
        }
    }
}


