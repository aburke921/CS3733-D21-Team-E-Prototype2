/**
 * Sample Skeleton for 'UserManagement.fxml' Controller Class
 */

package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.database.UserAccountDB;
import edu.wpi.cs3733.D21.teamE.database.makeConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;

public class UserManagement {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="treeTable"
    private TreeTableView<?> treeTable; // Value injected by FXMLLoader

    @FXML // fx:id="shortNameInput"
    private JFXTextField shortNameInput; // Value injected by FXMLLoader

    @FXML // fx:id="floorInput"
    private JFXComboBox<?> floorInput; // Value injected by FXMLLoader

    @FXML // fx:id="buildingInput"
    private JFXComboBox<?> buildingInput; // Value injected by FXMLLoader

    @FXML // fx:id="typeInput"
    private JFXComboBox<?> typeInput; // Value injected by FXMLLoader

    @FXML // fx:id="xCordInput"
    private JFXTextField xCordInput; // Value injected by FXMLLoader

    @FXML // fx:id="yCordInput"
    private JFXTextField yCordInput; // Value injected by FXMLLoader

    @FXML // fx:id="longNameInput"
    private JFXTextField longNameInput; // Value injected by FXMLLoader

    @FXML // fx:id="backButton"
    private JFXButton backButton; // Value injected by FXMLLoader

    @FXML
    void addNodeButton(ActionEvent event) {

    }

    @FXML
    void deleteNodeButton(ActionEvent event) {

    }

    @FXML
    void editNodeButton(ActionEvent event) {

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
        assert treeTable != null : "fx:id=\"treeTable\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert shortNameInput != null : "fx:id=\"shortNameInput\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert floorInput != null : "fx:id=\"floorInput\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert buildingInput != null : "fx:id=\"buildingInput\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert typeInput != null : "fx:id=\"typeInput\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert xCordInput != null : "fx:id=\"xCordInput\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert yCordInput != null : "fx:id=\"yCordInput\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert longNameInput != null : "fx:id=\"longNameInput\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'UserManagement.fxml'.";

        //todo use below fcn to fill tree table view with data
//        DB.getSelectableAssignees("VISITOR"); //VISITOR DOCTOR PATIENT ADMIN
        /*todo use https://examples.javacodegeeks.com/desktop-java/javafx/javafx-treetableview-example/#edit
        *  onEditCommit actionEvent to get when table is editable, and call DB user edit function.
        *   Have fields below that can have user data input into them for purposes of adding user to DB,
        *       base this part of of node/edge editor content*/


    }
}
