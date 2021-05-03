package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.CovidSurveyObj;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class CovidSurveyStatus {

    @FXML
    private JFXTreeTableView covidSurveyTable;

    @FXML
    private JFXButton markAsRiskButton;

    @FXML
    private JFXButton markAsSafeButton;

    @FXML
    private JFXButton refreshButton;

    @FXML
    private JFXButton backButton;

    @FXML
    private void markAsSafe() {

    }

    @FXML
    private void markAsRisk() {

    }

    @FXML
    private void refresh() {
        prepareTable(covidSurveyTable);
    }

    public void prepareTable(TreeTableView covidSurveyTable) {
        if(covidSurveyTable.getRoot() == null) {
            //Establishing some columns that are consistent throughout all the service requests
            //Column 1 - ID
            TreeTableColumn<CovidSurveyObj, String> userColumn = new TreeTableColumn<>("User");
            userColumn.setPrefWidth(320);
            userColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<CovidSurveyObj, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getUserDisplay()));
            covidSurveyTable.getColumns().add(userColumn);
            //Column 2 - Location
            TreeTableColumn<CovidSurveyObj, String> formNumberColumn = new TreeTableColumn<>("Location");
            formNumberColumn.setPrefWidth(150);
            formNumberColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<CovidSurveyObj, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getFormNumberDisplay()));
            covidSurveyTable.getColumns().add(formNumberColumn);
            //Column 3 - Assignee
            TreeTableColumn<CovidSurveyObj, String> testedColumn = new TreeTableColumn<>("Assignee");
            testedColumn.setPrefWidth(150);
            testedColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<CovidSurveyObj, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getAssignee()));
            covidSurveyTable.getColumns().add(testedColumn);

        }

        //Establishing root node
        TreeItem<CovidSurveyObj> rootNode = new TreeItem<>(new CovidSurveyObj("Service Requests"));

        //Setting up sub-root nodes
        TreeItem<CovidSurveyObj> inProgress = new TreeItem<>(new CovidSurveyObj("In Progress"));
        TreeItem<CovidSurveyObj> completed = new TreeItem<>(new CovidSurveyObj("Marked as Risk"));
        TreeItem<CovidSurveyObj> cancelled = new TreeItem<>(new CovidSurveyObj("Marked as Safe"));
    }



}
