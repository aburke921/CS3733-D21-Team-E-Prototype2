package edu.wpi.TeamE.views.serviceRequestControllers;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
//import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.databases.makeConnection;
import edu.wpi.TeamE.views.forms.ServiceRequestForm;
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
import sun.reflect.generics.tree.Tree;

import java.io.IOException;
import java.util.Collections;
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
    JFXButton refreshButton;

    @FXML
    private void toDefault(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void cancelRequest(ActionEvent e) {
        cancel(serviceRequestTable);
        prepareTable(serviceRequestTable);
    }

    private void cancel(TreeTableView<ServiceRequestForm> table) {
        makeConnection connection = makeConnection.makeConnection();
        if(table.getSelectionModel().getSelectedItem() != null) {
            int id = Integer.valueOf(table.getSelectionModel().getSelectedItem().getValue().getId());
            connection.changeRequestStatus(id, "canceled");
            System.out.println("The request was cancelled");
        }
    }

    @FXML
    private void completeRequest(ActionEvent e) {
        complete(serviceRequestTable);
        prepareTable(serviceRequestTable);
    }

    private void complete(TreeTableView<ServiceRequestForm> table) {
        makeConnection connection = makeConnection.makeConnection();
        if(table.getSelectionModel().getSelectedItem() != null) {
            int id = Integer.valueOf(table.getSelectionModel().getSelectedItem().getValue().getId());
            connection.changeRequestStatus(id,"complete");
            System.out.println("The request was completed");
        }

    }

    /**
     * This function refreshes the page so that it updates the table with any changes
     *
     * @param e an action
     */
    @FXML
    private void refresh(ActionEvent e) {
        prepareTable(serviceRequestTable);
    }

    /**
     * This function populates a specific part of the table from the database
     *
     * @param tableName the name of the table the data is coming from
     * @param inProgress the TreeItem for the service requests still in progress
     * @param completed the TreeItem for the service requests that have been completed
     * @param cancelled the TreeItem for the service requests that were cancelled
     */
    private void addToTable(String tableName, TreeItem<ServiceRequestForm> inProgress, TreeItem<ServiceRequestForm> completed, TreeItem<ServiceRequestForm> cancelled) {
        makeConnection connection = makeConnection.makeConnection();
        ArrayList<String> idArray = connection.getRequestIDs(tableName, -1);
//        for(int j = 0; j < idArray.size(); j++) {
//            System.out.println(idArray.get(j));
//        }
        ArrayList<String> statusArray = connection.getRequestStatus(tableName, -1);
        ArrayList<String> locationArray = connection.getRequestLocations(tableName, -1);
        ArrayList<String> assigneeArray = connection.getRequestAssignees(tableName, -1);
        if(idArray.size() > 0) {
            System.out.println("Array size" + idArray.size());
            if (!inProgress.getChildren().isEmpty()) {
                removeChildren(inProgress);
            }
            if (!completed.getChildren().isEmpty()) {
                removeChildren(completed);
            }
            if (!cancelled.getChildren().isEmpty()) {
                removeChildren(cancelled);
            }
            for (int i = 0; i < idArray.size(); i++) {
                System.out.println("Before");
                TreeItem<ServiceRequestForm> request = new TreeItem<>(new ServiceRequestForm(idArray.get(i), locationArray.get(i), assigneeArray.get(i), statusArray.get(i)));
                System.out.println(request.getValue().getId());
                if (request.getValue().getStatus().equals("inProgress")) {
                    inProgress.getChildren().add(request);
                }
                if (request.getValue().getStatus().equals("complete")) {
                    completed.getChildren().add(request);
                }
                if (request.getValue().getStatus().equals("canceled")) {
                    cancelled.getChildren().add(request);
                }
            }
        }
    }

    public void removeChildren(TreeItem<ServiceRequestForm> treeItem) {
       int removal = treeItem.getChildren().size();
        System.out.println(removal);
        if(treeItem.getChildren().size() != 0) {
            treeItem.getChildren().remove(0,removal);
        }
        TreeItem<ServiceRequestForm> test = treeItem;
        System.out.println(test.getChildren().size());
    }

    public void checkoutRequest(TreeTableView<ServiceRequestForm> table) {

        String typeOfForm = table.getSelectionModel().getSelectedItem().getParent().getValue().getId();

        if(typeOfForm.equals("External Patient Form")) {

        }
        if(typeOfForm.equals("Floral Form")) {

        }
        if(typeOfForm.equals("Medicine Delivery Form")) {

        }
        if(typeOfForm.equals("Sanitation Service Form")) {

        }
        if(typeOfForm.equals("Security Services Form")) {

        }
    }

    public void checkRequestButton(ActionEvent e) {
        checkoutRequest(serviceRequestTable);
    }


    public void prepareTable(TreeTableView serviceRequestTable) {
        if(serviceRequestTable.getRoot() == null) {
            //Establishing some columns that are consistent throughout all the service requests
            //Column 1 - ID
            TreeTableColumn<ServiceRequestForm, String> formColumn = new TreeTableColumn<>("Form");
            formColumn.setPrefWidth(320);
            formColumn.setCellValueFactory((CellDataFeatures<ServiceRequestForm, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getId()));
            serviceRequestTable.getColumns().add(formColumn);
            //Column 2 - Location
            TreeTableColumn<ServiceRequestForm, String> locationColumn = new TreeTableColumn<>("Location");
            locationColumn.setPrefWidth(150);
            locationColumn.setCellValueFactory((CellDataFeatures<ServiceRequestForm, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getLocation()));
            serviceRequestTable.getColumns().add(locationColumn);
            //Column 3 - Assignee
            TreeTableColumn<ServiceRequestForm, String> assigneeColumn = new TreeTableColumn<>("Assignee");
            assigneeColumn.setPrefWidth(150);
            assigneeColumn.setCellValueFactory((CellDataFeatures<ServiceRequestForm, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getAssignee()));
            serviceRequestTable.getColumns().add(assigneeColumn);

        }

        //Establishing root node
        TreeItem<ServiceRequestForm> rootNode = new TreeItem<>(new ServiceRequestForm("Service Requests"));

        //Setting up sub-root nodes
        TreeItem<ServiceRequestForm> inProgress = new TreeItem<>(new ServiceRequestForm("In Progress"));
        TreeItem<ServiceRequestForm> completed = new TreeItem<>(new ServiceRequestForm("Completed"));
        TreeItem<ServiceRequestForm> cancelled = new TreeItem<>(new ServiceRequestForm("Cancelled"));

        //Setting up children of sub-root nodes
        TreeItem<ServiceRequestForm> externalPatientCompleted = new TreeItem<>(new ServiceRequestForm("External Patient Form"));
        TreeItem<ServiceRequestForm> floralFormCompleted = new TreeItem<>(new ServiceRequestForm("Floral Form"));
        TreeItem<ServiceRequestForm> medicineDeliveryCompleted = new TreeItem<>(new ServiceRequestForm("Medicine Delivery Form"));
        TreeItem<ServiceRequestForm> sanitationServicesCompleted = new TreeItem<>(new ServiceRequestForm("Sanitation Services Form"));
        TreeItem<ServiceRequestForm> securityServiceCompleted = new TreeItem<>(new ServiceRequestForm("Security Service Form"));
        TreeItem<ServiceRequestForm> externalPatientInProgress = new TreeItem<>(new ServiceRequestForm("External Patient Form"));
        TreeItem<ServiceRequestForm> floralFormInProgress = new TreeItem<>(new ServiceRequestForm("Floral Form"));
        TreeItem<ServiceRequestForm> medicineDeliveryInProgress = new TreeItem<>(new ServiceRequestForm("Medicine Delivery Form"));
        TreeItem<ServiceRequestForm> sanitationServicesInProgress = new TreeItem<>(new ServiceRequestForm("Sanitation Services Form"));
        TreeItem<ServiceRequestForm> securityServiceInProgress = new TreeItem<>(new ServiceRequestForm("Security Services Form"));
        TreeItem<ServiceRequestForm> externalPatientCancelled = new TreeItem<>(new ServiceRequestForm("External Patient Form"));
        TreeItem<ServiceRequestForm> floralFormCancelled = new TreeItem<>(new ServiceRequestForm("Floral Form"));
        TreeItem<ServiceRequestForm> medicineDeliveryCancelled = new TreeItem<>(new ServiceRequestForm("Medicine Delivery Form"));
        TreeItem<ServiceRequestForm> sanitationServicesCancelled = new TreeItem<>(new ServiceRequestForm("Sanitation Services Form"));
        TreeItem<ServiceRequestForm> securityServiceCancelled = new TreeItem<>(new ServiceRequestForm("Security Services Form"));

        //Adding request forms
        addToTable("security", securityServiceInProgress, securityServiceCompleted, securityServiceCancelled);
        addToTable("extTransport", externalPatientInProgress, externalPatientCompleted, externalPatientCancelled);
        addToTable("floral", floralFormInProgress, floralFormCompleted, floralFormCancelled);
        addToTable("sanitation", sanitationServicesInProgress, sanitationServicesCompleted, sanitationServicesCancelled);
        addToTable("medDelivery", medicineDeliveryInProgress, medicineDeliveryCompleted, medicineDeliveryCancelled);

        //Adding children to sub-root nodes
        inProgress.getChildren().setAll(externalPatientInProgress, floralFormInProgress, medicineDeliveryInProgress, sanitationServicesInProgress, securityServiceInProgress);
        completed.getChildren().setAll(externalPatientCompleted, floralFormCompleted, medicineDeliveryCompleted, sanitationServicesCompleted, securityServiceCompleted);
        cancelled.getChildren().setAll(externalPatientCancelled, floralFormCancelled, medicineDeliveryCancelled, sanitationServicesCancelled, securityServiceCancelled);

        //Adding sub-roots to root node
        rootNode.getChildren().setAll(inProgress, completed, cancelled);

        //Adding Root
        serviceRequestTable.setRoot(rootNode);
        serviceRequestTable.setShowRoot(false);
    }

    @FXML
    void initialize() {

        prepareTable(serviceRequestTable);

    }
}
