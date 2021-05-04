package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.states.DefaultState;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.ServiceRequestForm;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

import java.io.IOException;
import java.util.ArrayList;

public class CovidSurveyStatus {

    @FXML
    private JFXTreeTableView<CovidSurveyObj> covidSurveyTable;

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
        CovidSurveyObj formNumber = covidSurveyTable.getSelectionModel().getSelectedItem().getValue();
        int formNum = formNumber.getFormNumber();
        DB.markAsCovidSafe(formNum);
        DB.updateUserAccountCovidStatus(App.userID, "Safe");
    }

    @FXML
    private void markAsRisk() {
        CovidSurveyObj formNumber = covidSurveyTable.getSelectionModel().getSelectedItem().getValue();
        int formNum = formNumber.getFormNumber();
        DB.markAsCovidRisk(formNum);
        DB.updateUserAccountCovidStatus(App.userID, "Unsafe");
    }

    public void removeChildren(TreeItem<CovidSurveyObj> treeItem) {
        int removal = treeItem.getChildren().size();
        System.out.println(removal);
        if(treeItem.getChildren().size() != 0) {
            treeItem.getChildren().remove(0,removal);
        }
        TreeItem<CovidSurveyObj> test = treeItem;
        System.out.println(test.getChildren().size());
    }

    @FXML
    private void refresh() {
        prepareTable(covidSurveyTable);
    }

    @FXML
    private void goBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addToTable(TreeItem<CovidSurveyObj> unchecked, TreeItem<CovidSurveyObj> markedSafe, TreeItem<CovidSurveyObj> markedUnsafe) {

       ArrayList<CovidSurveyObj> allCovidSurveys = DB.getCovidSurveys();

        if(allCovidSurveys.size() > 0) {
            if(!unchecked.getChildren().isEmpty()) {
                removeChildren(unchecked);
            }
            if(!markedSafe.getChildren().isEmpty()) {
                removeChildren(markedSafe);
            }
            if(!markedUnsafe.getChildren().isEmpty()) {
                removeChildren(markedUnsafe);
            }
            for (int i = 0; i < allCovidSurveys.size(); i++) {
                TreeItem<CovidSurveyObj> survey = new TreeItem<>(allCovidSurveys.get(i));
                if(allCovidSurveys.get(i).getStatus().equals("Needs to be reviewed")) {
                    unchecked.getChildren().add(survey);
                }
                if(allCovidSurveys.get(i).getStatus().equals("Safe")) {
                    markedSafe.getChildren().add(survey);
                }
                if(allCovidSurveys.get(i).getStatus().equals("Unsafe")) {
                    markedUnsafe.getChildren().add(survey);
                }
            }
        }
    }

    @FXML
    private void switchScene(ActionEvent e) {
        DefaultState defaultState = new DefaultState();
        defaultState.switchScene(e);
    }

    public void prepareTable(TreeTableView covidSurveyTable) {
        if(covidSurveyTable.getRoot() == null) {
            //Establishing some columns that are consistent throughout all the service requests
            //Column 1 - User
            TreeTableColumn<CovidSurveyObj, String> userColumn = new TreeTableColumn<>("User");
            userColumn.setPrefWidth(150);
            userColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<CovidSurveyObj, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getUserDisplay()));
            covidSurveyTable.getColumns().add(userColumn);
            //Column 2 - Form Number
            TreeTableColumn<CovidSurveyObj, String> formNumberColumn = new TreeTableColumn<>("Form Number");
            formNumberColumn.setPrefWidth(150);
            formNumberColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<CovidSurveyObj, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getFormNumberDisplay()));
            covidSurveyTable.getColumns().add(formNumberColumn);
            //Column 3 - Positive Test?
            TreeTableColumn<CovidSurveyObj, String> testedColumn = new TreeTableColumn<>("Positive Test?");
            testedColumn.setPrefWidth(150);
            testedColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<CovidSurveyObj, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getPositiveTestDisplay()));
            covidSurveyTable.getColumns().add(testedColumn);
            //Column 4 - Symptoms?
            TreeTableColumn<CovidSurveyObj, String> symptomsColumn = new TreeTableColumn<>("Any Symptoms?");
            symptomsColumn.setPrefWidth(150);
            symptomsColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<CovidSurveyObj, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getSymptomsDisplay()));
            covidSurveyTable.getColumns().add(symptomsColumn);
            //Column 5 - Close Contacts?
            TreeTableColumn<CovidSurveyObj, String> closeContactColumn = new TreeTableColumn<>("Any Close Contacts?");
            closeContactColumn.setPrefWidth(150);
            closeContactColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<CovidSurveyObj, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getCloseContactDisplay()));
            covidSurveyTable.getColumns().add(closeContactColumn);
            //Column 6 - Quarantine?
            TreeTableColumn<CovidSurveyObj, String> quarantineColumn = new TreeTableColumn<>("Has to be in Quarantine?");
            quarantineColumn.setPrefWidth(150);
            quarantineColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<CovidSurveyObj, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getQuarantineDisplay()));
            covidSurveyTable.getColumns().add(quarantineColumn);
            //Column 7 - Feels Good?
            TreeTableColumn<CovidSurveyObj, String> allGoodColumn = new TreeTableColumn<>("Feels Good?");
            allGoodColumn.setPrefWidth(150);
            allGoodColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<CovidSurveyObj, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getNoSymptomsDisplay()));
            covidSurveyTable.getColumns().add(allGoodColumn);
        }

        //Establishing root node
        TreeItem<CovidSurveyObj> rootNode = new TreeItem<>(new CovidSurveyObj("Service Requests"));

        //Setting up sub-root nodes
        TreeItem<CovidSurveyObj> unChecked = new TreeItem<>(new CovidSurveyObj("To Be Reviewed"));
        TreeItem<CovidSurveyObj> markedAsRisk = new TreeItem<>(new CovidSurveyObj("Marked as Risk"));
        TreeItem<CovidSurveyObj> markedAsSafe = new TreeItem<>(new CovidSurveyObj("Marked as Safe"));

        //Adding sub-roots to nodes
        rootNode.getChildren().addAll(unChecked,markedAsRisk,markedAsSafe);
        covidSurveyTable.setRoot(rootNode);
        covidSurveyTable.setShowRoot(false);

        //Adding requests to table
        addToTable(unChecked,markedAsSafe,markedAsRisk);
    }

    @FXML
    void initialize() {
        prepareTable(covidSurveyTable);
    }

}
