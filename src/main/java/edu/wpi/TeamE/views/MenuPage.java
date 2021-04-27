package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.TeamE.App;
import edu.wpi.TeamE.views.serviceRequestObjects.AubonPainItem;
import edu.wpi.TeamE.views.serviceRequestObjects.ServiceRequestForm;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
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

    //TODO: Fix this
    public void prepareTable(TreeTableView menuTable) {

        if(menuTable.getRoot() == null) {
            //Establishing some columns that are consistent throughout all the service requests
            //Column 1 - ID
            TreeTableColumn<AubonPainItem, String> formColumn = new TreeTableColumn<>("Item Name");
            formColumn.setPrefWidth(320);
            formColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<AubonPainItem, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getFoodItem()));
            menuTable.getColumns().add(formColumn);
            //Column 2 - Location
            TreeTableColumn<AubonPainItem, String> locationColumn = new TreeTableColumn<>("Calories");
            locationColumn.setPrefWidth(150);
            locationColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<AubonPainItem, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getFoodCalories()));
            menuTable.getColumns().add(locationColumn);
            //Column 3 - Assignee
            TreeTableColumn<AubonPainItem, String> assigneeColumn = new TreeTableColumn<>("Price");
            assigneeColumn.setPrefWidth(150);
            assigneeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<AubonPainItem, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getFoodPrice()));
            menuTable.getColumns().add(assigneeColumn);

        }
        //Establishing root node
        ArrayList<String> foodItems = DB.getAubonPainFeild("foodItem");
        ArrayList<String> foodPrice = DB.getAubonPainFeild("foodPrice");
        ArrayList<String> foodCalories = DB.getAubonPainFeild("foodCalories");

        //COMMENT THIS PART OUT ----------------------
        ArrayList<AubonPainItem> items = DB.getAubonPanItems();
        TreeItem<AubonPainItem> rootNode = new TreeItem<>(new AubonPainItem("Aubon Pain Menu"));

        for(AubonPainItem item : items){
            TreeItem<AubonPainItem> childNode = new TreeItem<>(new AubonPainItem(item.getFoodItem(), item.getFoodCalories(), item.getFoodPrice()));
            TreeItem<AubonPainItem> realDescription = new TreeItem<>(new AubonPainItem(item.getFoodDescription()));
//            TreeItem<AubonPainItem> description = new TreeItem<>(new AubonPainItem(null, item.getFoodItem(), null, null, null));
//            childNode.getChildren().add(description);
//            addToTable(childNode);

            rootNode.getChildren().add(childNode);
            childNode.getChildren().add(realDescription);
        }



        //Adding Root
        menuTable.setRoot(rootNode);
        menuTable.setShowRoot(false);
        //COMMENT UP TO HERE ----------------------

//        TreeItem<AubonPainItem> rootNode = new TreeItem<>(new AubonPainItem("Aubon Pain Menu"));
//        TreeItem<AubonPainItem> inProgress = new TreeItem<>(new AubonPainItem("In Progress"));
//
//        TreeItem<ServiceRequestForm> externalPatientCompleted = new TreeItem<>(new ServiceRequestForm("External Patient Form"));
//        addToTable(inProgress);
//
//        rootNode.getChildren().setAll(inProgress);
//
//        //Adding Root
//        menuTable.setRoot(rootNode);
//        menuTable.setShowRoot(false);
    }

    //TODO: Fix this
    /**
     * This function populates a specific part of the table from the database
     * @param inProgress the TreeItem for the service requests still in progress
     */
    private void addToTable(TreeItem<AubonPainItem> inProgress) {

        ArrayList<AubonPainItem> items = DB.getAubonPanItems();

        ArrayList<String> foodItems = DB.getAubonPainFeild("foodItem");
        ArrayList<String> foodPrice = DB.getAubonPainFeild("foodPrice");
        ArrayList<String> foodCalories = DB.getAubonPainFeild("foodCalories");
        ArrayList<String> foodDescription = DB.getAubonPainFeild("foodDescription");
//        ArrayList<String> idArray = DB.getMyCreatedRequestInfo(tableName, App.userID, "requestID");
//        for(int j = 0; j < idArray.size(); j++) {
//            System.out.println(idArray.get(j));
//        }
//        ArrayList<String> statusArray = DB.getMyCreatedRequestInfo(tableName, App.userID, "requestStatus");
//        ArrayList<String> locationArray = DB.getRequestLocations(tableName, App.userID);
//        ArrayList<String> assigneeArray = DB.getMyCreatedRequestInfo(tableName, App.userID, "assigneeID");
        if(foodItems.size() > 0) {
            System.out.println("Array size" + foodItems.size());
//            if (!inProgress.getChildren().isEmpty()) {
//                removeChildren(inProgress);
//            }


//            if (!completed.getChildren().isEmpty()) {
//                removeChildren(completed);
//            }
//            if (!cancelled.getChildren().isEmpty()) {
//                removeChildren(cancelled);
//            }
            for (int i = 0; i < foodItems.size(); i++) {
                System.out.println("Before");
                TreeItem<AubonPainItem> request = new TreeItem<>(new AubonPainItem(foodDescription.get(i), null, null));
                System.out.println(request.getValue().getFoodItem());
//                if (request.getValue().getStatus().equals("inProgress")) {
                    inProgress.getChildren().add(request);
//                }
//                if (request.getValue().getStatus().equals("complete")) {
//                    completed.getChildren().add(request);
//                }
//                if (request.getValue().getStatus().equals("canceled")) {
//                    cancelled.getChildren().add(request);
//                }
            }
        }
    }

    //TODO: Fix this
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


    /**
     * This function refreshes the page so that it updates the table with any changes
     *
     * @param e an action
     */
    @FXML
    private void refresh(ActionEvent e) {
        prepareTable(menuTable);
    }

    @FXML
    void init() {
        prepareTable(menuTable);
    }

}
