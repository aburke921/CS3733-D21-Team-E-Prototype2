package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
//import edu.wpi.cs3733.D21.teamE.map.Node;


import edu.wpi.cs3733.D21.teamE.states.CreateAccountState;
import edu.wpi.cs3733.D21.teamE.states.ServiceRequestStatusState;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.ServiceRequestForm;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ServiceRequestStatus {

    @FXML
    JFXTreeTableView serviceRequestTable;

    @FXML
    JFXButton completeButton;

    @FXML
    JFXButton cancelButton;

    @FXML
    JFXButton refreshButton;

    /**
     * Switch to a different scene
     * @param e tells which button was pressed
     */
    @FXML
    private void switchScene(ActionEvent e) {
        ServiceRequestStatusState serviceRequestStatusState = new ServiceRequestStatusState();
        serviceRequestStatusState.switchScene(e);
    }

    @FXML
    private void cancelRequest(ActionEvent e) {
        cancel(serviceRequestTable);
        prepareTable(serviceRequestTable);
    }

    private void cancel(TreeTableView<ServiceRequestForm> table) {

        if(table.getSelectionModel().getSelectedItem() != null) {
            int id = Integer.valueOf(table.getSelectionModel().getSelectedItem().getValue().getId());
            DB.editRequests(id, 0, "canceled");
            System.out.println("The request was cancelled");
        }
    }

    @FXML
    private void completeRequest(ActionEvent e) {
        complete(serviceRequestTable);
        prepareTable(serviceRequestTable);
    }

    private void complete(TreeTableView<ServiceRequestForm> table) {
        if(table.getSelectionModel().getSelectedItem() != null) {
            int id = Integer.valueOf(table.getSelectionModel().getSelectedItem().getValue().getId());
            DB.editRequests(id,0, "complete");
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

        ArrayList<String> idArray = DB.getMyCreatedRequestInfo(tableName, App.userID, "requestID");
//        for(int j = 0; j < idArray.size(); j++) {
//            System.out.println(idArray.get(j));
//        }
        ArrayList<String> statusArray = DB.getMyCreatedRequestInfo(tableName, App.userID, "requestStatus");
        ArrayList<String> locationArray = DB.getRequestLocations(tableName, App.userID);
        ArrayList<String> assigneeArray = DB.getMyCreatedRequestInfo(tableName, App.userID, "AssigneeID");
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
        addToTable("securityServ", securityServiceInProgress, securityServiceCompleted, securityServiceCancelled);
        addToTable("extTransport", externalPatientInProgress, externalPatientCompleted, externalPatientCancelled);
        addToTable("floralRequests", floralFormInProgress, floralFormCompleted, floralFormCancelled);
        addToTable("sanitationRequest", sanitationServicesInProgress, sanitationServicesCompleted, sanitationServicesCancelled);
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
