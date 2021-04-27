package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.TeamE.views.serviceRequestObjects.ServiceRequestForm;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class MenuPage {

    @FXML
    private JFXTreeTableView<> menuTable;

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


    @FXML
    void init() {

    }

}
