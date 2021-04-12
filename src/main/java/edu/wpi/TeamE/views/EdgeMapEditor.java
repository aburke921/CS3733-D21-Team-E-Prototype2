package edu.wpi.TeamE.views;
import edu.wpi.TeamE.algorithms.pathfinding.Edge;
import edu.wpi.TeamE.algorithms.pathfinding.Edge;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import java.util.ArrayList;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;




public class EdgeMapEditor {

    @FXML private TreeTableView<edu.wpi.TeamE.algorithms.pathfinding.Edge> treeTable;
    @FXML private FlowPane theStage;


    @FXML
    private void toNavigation(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/MapEditorNavigation.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void getHelpDefault(ActionEvent e) {
    }

       /* public static void main(String[] args) {
            Application.launch(args);
        }

        should be 'id', 'floor', 'building', 'type', 'longName', 'shortName'
        */

    public void prepareEdges(ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Edge> array, TreeTableView<edu.wpi.TeamE.algorithms.pathfinding.Edge> table) {
        if (table.getRoot() == null) {
            edu.wpi.TeamE.algorithms.pathfinding.Edge edge0 = new
                    edu.wpi.TeamE.algorithms.pathfinding.Edge("ID", "0", "1");
            final TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Edge> rootEdge = new TreeItem<>(edge0);
            table.setRoot(rootEdge);
            //column 1 - ID
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Edge, String> column1 = new TreeTableColumn<>("ID");
            column1.setPrefWidth(320);
            column1.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Edge, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getId()));
            table.getColumns().add(column1);
            //column 2 - Start Node
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Edge, String> column2 = new TreeTableColumn<>("Start Node ID");
            column2.setPrefWidth(320);
            column2.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Edge, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getStartNodeId()));
            table.getColumns().add(column2);
            //column 3 - End Node
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Edge, String> column3 = new TreeTableColumn<>("End Node ID");
            column3.setPrefWidth(320);
            column3.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Edge, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getEndNodeId()));
            table.getColumns().add(column3);
        }
    }

    public void editEdge(ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Edge> array, TreeTableView<String> table) {
        TreeItem<String> edge = table.getSelectionModel().getSelectedItem();
        if (table.getSelectionModel().getSelectedItem() != null) {
            //function that takes in string (?) which will call to edge that wants to be edited and
            //inputted info from user, returns void
            /*for(int i = 0; i < array.size(); i++) {
                if(edge.equals(array.get(i).name)) {

                }
            }*/
        }
    }

    public void addEdge(ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Edge> array, TreeTableView<String> table) {
        //function that takes in users inputted info, updates database
    }

    public void deleteEdge(ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Edge> array, TreeTableView<String> table) {
        TreeItem<String> edge = table.getSelectionModel().getSelectedItem();
        if (table.getSelectionModel().getSelectedItem() != null) {
            //function that takes in identifier of selected edge, deletes the edge
        }
    }


    public void startTableButton(ActionEvent actionEvent) {
        //creating the root for the array
        edu.wpi.TeamE.algorithms.pathfinding.Edge edge0 = new
                edu.wpi.TeamE.algorithms.pathfinding.Edge("ID", "0", "0");
        //creating array
        ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Edge> array = new ArrayList<>();
        //creating root edge
        final TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Edge> test = new TreeItem<>(edge0);
        test.setExpanded(true);

        //will be in init later
        File file = new File("L1Edges.csv");
        makeConnection connection = new makeConnection();
        connection.deleteAllTables();
        connection.createTables();
        connection.populateTable("edge", file);
        //will be in init later

//        array = connection.getAllEdges();
//        prepareEdges(array, treeTable);
    }
    @FXML
    public void toFileUpload() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/EdgeFileUpload.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     *opens the file explorer on user's device, allows user to select CSV file,
     * uploads file to database, refreshes page
     * @param e actionevent
     */
    public void fileOpener(final ActionEvent e) {

        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(App.getPrimaryStage());
        if (file != null) {
            //openFile(file);
            //file.getAbsolutePath();
            makeConnection connection = new makeConnection();
            connection.deleteAllTables();
            // TODO: delete edge table only plzzzzzzzzzzzzz
            connection.createTables();
            // TODO: create edge table only plzzzzzzzzzzzzz
            connection.populateTable("edge", file);
            //database's fcn for file uploading goes here, param is file
            //when submit button is pressed, update/refresh?
        }
    }

    private Desktop desktop = Desktop.getDesktop();

    /**
     * opens the file chosen by user
     * @param file File
     */
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    MapEditor.class.getName()).log(
                    Level.SEVERE, null, ex
            );

        }
    }
}





