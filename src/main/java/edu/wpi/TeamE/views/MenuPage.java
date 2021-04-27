package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.TeamE.App;
import edu.wpi.TeamE.views.serviceRequestObjects.ServiceRequestForm;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

import java.util.ArrayList;

public class MenuPage {

    @FXML
    private JFXTreeTableView menuTable;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton refreshButton;

    @FXML
    private JFXButton checkoutItem;

    public void prepareTable(TreeTableView menuTable) {

        if(menuTable.getRoot() == null) {
            //Establishing some columns that are consistent throughout all the service requests
            //Column 1 - ID
            TreeTableColumn<ServiceRequestForm, String> formColumn = new TreeTableColumn<>("Item Name");
            formColumn.setPrefWidth(320);
            formColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<ServiceRequestForm, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getId()));
            menuTable.getColumns().add(formColumn);
            //Column 2 - Location
            TreeTableColumn<ServiceRequestForm, String> locationColumn = new TreeTableColumn<>("Calories");
            locationColumn.setPrefWidth(150);
            locationColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<ServiceRequestForm, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getLocation()));
            menuTable.getColumns().add(locationColumn);
            //Column 3 - Assignee
            TreeTableColumn<ServiceRequestForm, String> assigneeColumn = new TreeTableColumn<>("Price");
            assigneeColumn.setPrefWidth(150);
            assigneeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<ServiceRequestForm, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getAssignee()));
            menuTable.getColumns().add(assigneeColumn);

        }
        //Establishing root node
        TreeItem<ServiceRequestForm> rootNode = new TreeItem<>(new ServiceRequestForm("Service Requests"));
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
        ArrayList<String> assigneeArray = DB.getMyCreatedRequestInfo(tableName, App.userID, "assigneeID");
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

    @FXML
    void cancelRequest() {}

    @FXML
    void checkoutItem() {}

    @FXML
    void toDefault() {}

    @FXML
    void refresh() {}


    @FXML
    void init() {

    }

}
