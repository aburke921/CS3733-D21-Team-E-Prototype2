/**
 * Sample Skeleton for 'UserManagement.fxml' Controller Class
 */

package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.TeamE.App;
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.database.RequestsDB;
import edu.wpi.cs3733.D21.teamE.database.UserAccountDB;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;

public class UserManagement {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private Button addUser;

    @FXML
    private Button editUser;

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane;

    @FXML
    private JFXTextField userNameInput;

    @FXML
    private JFXComboBox<String> userTypeInput;

    @FXML
    private JFXTextField userPassword;

    @FXML
    private JFXTextField userEmail;

    @FXML // fx:id="treeTableView"
    private TreeTableView<User> treeTableView;

    @FXML
    private JFXButton backButton;

    private boolean addingUser = false;

    private boolean editingUser = false;

    private User currentlyEditing;

    @FXML
    void addUserButton(ActionEvent event) {
        if (addingUser) { //admin is submitting new user
            addingUser = false;

            //add user
            String[] firstAndLast = userNameInput.getText().split(" "); //get first and last name
            if (firstAndLast.length == 2) { //validate userNameInput field
                System.out.println("Adding User Account:" +
                        "\n...email: " + userEmail.getText() +
                        "\n...password: " + userPassword.getText() +
                        "\n...first: " + firstAndLast[0] +
                        "\n...last: " + firstAndLast[1]);
                DB.addUserAccount(userEmail.getText(),userPassword.getText(),firstAndLast[0],firstAndLast[1]); //add to DB
//            DB.addUserAccount("cmanning@wpi.edu","aConformingPassword$#1","First","Last"); //add to DB

                //todo confirmation popup

            } else {
                //todo pop-up, tell user it must be first and last name
            }



            //cleanup
            addUser.setText("Add User");
            showFields(false);
            clearFieldContent();
            //todo refresh

        } else { //admin would like to start adding user
            addingUser = true;
            showFields(true);
            addUser.setText("Confirm Adding User");
        }
    }

    @FXML
    void deleteUserButton(ActionEvent event) {
        if (treeTableView.getSelectionModel().getSelectedItem() != null) { //if User has been selected...

            //get selected user
            User userForDeletion = treeTableView.getSelectionModel().getSelectedItem().getValue();

            //delete from DB
            System.out.print("Deleting user " + userForDeletion.getUserID() + ", "+ userForDeletion.getEmail() +"");
            int removeDB = UserAccountDB.deleteUserAccount(userForDeletion.getUserID());
            if (removeDB == 1) {
                System.out.print("...deleted from db");
            } else System.out.println("...Could not delete user from DB");

            //delete from TTV
            TreeItem c = treeTableView.getSelectionModel().getSelectedItem();
            boolean removeTTV = c.getParent().getChildren().remove(c);
            if (removeTTV) {
                System.out.println("...deleted from TTV");
            } else System.out.println("...Could not delete user from table");

        } else { //nothing selected in TTV
            System.out.println("Deletion failed - No item was selected"); //todo popup?
        }
    }

    @FXML
    void editUserButton(ActionEvent event) {
        if (editingUser) {
            //user is being edited, clicks button to submit

            //todo check if fields are valid

            String[] firstAndLast = userNameInput.getText().split(" ");
            if (firstAndLast.length == 2) { //validate userNameInput field
                DB.editUserAccount(currentlyEditing.getUserID(),userEmail.getText(),userPassword.getText(),userTypeInput.getValue(),firstAndLast[0], firstAndLast[1]);

                //todo confirmation popup?
            } else {
                //todo popup, invalid first and last
            }

            //cleanup
            showFields(false);
            editingUser = false;
            editUser.setText("Edit User");
            currentlyEditing = null;
            clearFieldContent();
            //todo refresh
        } else { //no edit is in progress

            if (treeTableView.getSelectionModel().getSelectedItem() != null) {
                currentlyEditing = treeTableView.getSelectionModel().getSelectedItem().getValue(); //get selected user
                editUser.setText("Confirm User Edit"); //change button text
                showFields(true); //show form fields
                editingUser = true; //flag start of edit

                //set fields
                userEmail.setText(currentlyEditing.getEmail());
                userNameInput.setText(currentlyEditing.getFirstName()+ " " + currentlyEditing.getLastName());
                userTypeInput.getSelectionModel().select(currentlyEditing.getUserType());
            } else {
                //todo popup?
                System.out.println("cannot edit, nothing selected");
            }

        }



    }

    @FXML
    void startTableButton(ActionEvent event) {
        //todo refresh table?
    }

    /**
     * Returns to {@link edu.wpi.TeamE.views.Default} page.
     * @param event calling event info.
     */
    @FXML
    void toDefault(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert appBarAnchorPane != null : "fx:id=\"appBarAnchorPane\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert treeTableView != null : "fx:id=\"treeTableView\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'UserManagement.fxml'.";

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setPageTitle("User Management"); //set AppBar title
            App.setShowHelp(false);
            App.setShowLogin(true);
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's sideBarVBox element
        } catch (IOException e) {
            e.printStackTrace();
        }

        //fill Table with user data
        prepareUsers(treeTableView, UserAccountDB.getAllUsers());

        //hide form values until editing or adding
        showFields(false);

        //add items to comboBox
        userTypeInput.getItems().addAll("visitor","patient","doctor","admin");
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

    /**
     * Shows and hides the form fields
     * @param status
     */
    private void showFields(boolean status) {
        userNameInput.setVisible(status);
        userTypeInput.setVisible(status);
        userPassword.setVisible(status);
        userEmail.setVisible(status);
    }

    /**
     * @// TODO: 4/27/21
     */
    private void clearFieldContent() {
        userNameInput.setText(null);
        userTypeInput.getSelectionModel().select(null);
        userPassword.setText(null);
        userEmail.setText(null);
    }

}


