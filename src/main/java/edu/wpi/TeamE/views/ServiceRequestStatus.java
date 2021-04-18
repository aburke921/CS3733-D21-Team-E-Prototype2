package edu.wpi.TeamE.views;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
//import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;

import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceRequestStatus {

    @FXML
    JFXTreeTableView serviceRequestTable;

    @FXML
    JFXButton completeButton;

    @FXML
    JFXButton cancelButton;

    @FXML
    private void toDefault(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void prepareTable(TreeTableView serviceRequestTable) {


        //Setting up root nodes
        TreeItem<String> inProgress = new TreeItem<>("In Progress");
        TreeItem<String> completed = new TreeItem<>("Completed");
        TreeItem<String> cancelled = new TreeItem<>("Cancelled");

        //Setting up sub-root nodes
        TreeItem<String> externalPatientCompleted = new TreeItem<>("External Patient Form");
        TreeItem<String> floralFormCompleted = new TreeItem<>("Floral Form");
        TreeItem<String> medicineDeliveryCompleted = new TreeItem<>("Medicine Delivery Form");
        TreeItem<String> sanitationServicesCompleted = new TreeItem<>("Sanitation Services Form");
        TreeItem<String> securityServiceCompleted = new TreeItem<>("Security Service Form");
        TreeItem<String> externalPatientInProgress = new TreeItem<>("External Patient Form");
        TreeItem<String> floralFormInProgress = new TreeItem<>("Floral Form");
        TreeItem<String> medicineDeliveryInProgress = new TreeItem<>("Medicine Delivery Form");
        TreeItem<String> sanitationServicesInProgress = new TreeItem<>("Sanitation Services Form");
        TreeItem<String> securityServiceInProgress = new TreeItem<>("Security Service Form");
        TreeItem<String> externalPatientCancelled = new TreeItem<>("External Patient Form");
        TreeItem<String> floralFormCancelled = new TreeItem<>("Floral Form");
        TreeItem<String> medicineDeliveryCancelled = new TreeItem<>("Medicine Delivery Form");
        TreeItem<String> sanitationServicesCancelled = new TreeItem<>("Sanitation Services Form");
        TreeItem<String> securityServiceCancelled = new TreeItem<>("Security Service Form");

        //Establishing some columns that are consistent throughout all the service requests
        //Column 1 - Location
        TreeTableColumn<String, String> formColumn = new TreeTableColumn<>("Form");
        formColumn.setPrefWidth(320);
        formColumn.setCellValueFactory(new Callback<CellDataFeatures<String, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<String, String> param) {
                return new SimpleStringProperty(param.getValue().getValue())
            }
        });
        serviceRequestTable.getColumns().add(formColumn);
        //Column 2 - X Coordinate
        TreeTableColumn<Node, Number> locationColumn = new TreeTableColumn<>("Location");
        locationColumn.setPrefWidth(150);
        serviceRequestTable.getColumns().add(locationColumn);
        //Column 3 - Y Coordinate
        TreeTableColumn<String, Number> assigneeColumn = new TreeTableColumn<>("Assignee");
        assigneeColumn.setPrefWidth(150);
        serviceRequestTable.getColumns().add(assigneeColumn);

    }

    @FXML
    void initialize() {

        prepareTable(serviceRequestTable);

    }


}
