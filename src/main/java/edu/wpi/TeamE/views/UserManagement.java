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
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.database.RequestsDB;
import edu.wpi.cs3733.D21.teamE.database.UserAccountDB;
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
        if (treeTableView.getSelectionModel().getSelectedItem() != null) { //if User has been selected...
            User userForDeletion = treeTableView.getSelectionModel().getSelectedItem().getValue();
            //todo delete from DB
            System.out.println("Deleting user " + userForDeletion);
            //todo refresh TreeTable
        } else { //nothing selected in TTV
            System.out.println("Deletion failed - No item was selected"); //todo popup?
        }
    }

    @FXML
    void editUserButton(ActionEvent event) {
        int id = Integer.parseInt(userIDInput.getText());
        String username = userNameInput.getText();
        String userType = userTypeInput.getText();

//        DB.addUserAccount(username,);
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

        //fill Table with user data
        prepareUsers(treeTableView, UserAccountDB.getAllUsers());

        /* todo
            addSpecialUserType()
            editUserAccount()
            deleteUserAccount()
         */

    }


    //User class

    /**
     * todo, temp user class, should be moved elsewhere
     * firstName, lastName, userID, userType, email
     */
    public static final class User {
        final StringProperty firstName;
        final StringProperty lastName;
        final IntegerProperty userID;
        final StringProperty userType;
        final StringProperty email;

        public String getFirstName() {
            return firstName.get();
        }

        public StringProperty firstNameProperty() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName.set(firstName);
        }

        public String getLastName() {
            return lastName.get();
        }

        public StringProperty lastNameProperty() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName.set(lastName);
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

        public String getEmail() {
            return email.get();
        }

        public StringProperty emailProperty() {
            return email;
        }

        public void setEmail(String email) {
            this.email.set(email);
        }

        public User(String userType, int userID, String firstName, String lastName, String email) {

            this.userType = new SimpleStringProperty(userType);
            this.userID = new SimpleIntegerProperty(userID);
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.email = new SimpleStringProperty(email);
        }
    }

    public void prepareUsers(TreeTableView<User> table, ArrayList<User> array) {
        if (table.getRoot() == null) {
            User user0 = new User("", 0, "", "", "");
            final TreeItem<User> rootUser = new TreeItem<>(user0);
            table.setRoot(rootUser);

            //column 1 - first Name
            TreeTableColumn<User, String> column1 = new TreeTableColumn<>("First Name");
            column1.setPrefWidth(100);
            column1.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getFirstName()));
            table.getColumns().add(column1);

            //column 2 - lastname
            TreeTableColumn<User, String> column2 = new TreeTableColumn<>("Last Name");
            column2.setPrefWidth(100);
            column2.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getLastName()));
            table.getColumns().add(column2);


            //column 3 - ID
            TreeTableColumn<User, Number> column3 = new TreeTableColumn<>("ID");
            column3.setPrefWidth(50);
            column3.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getUserID()));
            table.getColumns().add(column3);

            //column 4 - User Type
            TreeTableColumn<User, String> column4 = new TreeTableColumn<>("User Type");
            column4.setPrefWidth(100);
            column4.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getUserType()));
            table.getColumns().add(column4);

            //column 5 - Email
            TreeTableColumn<User, String> column5 = new TreeTableColumn<>("Email");
            column5.setPrefWidth(300);
            column5.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getEmail()));
            table.getColumns().add(column5);
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


