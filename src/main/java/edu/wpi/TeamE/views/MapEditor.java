package edu.wpi.TeamE.views;
import edu.wpi.TeamE.algorithms.pathfinding.Node;
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



public class MapEditor {

    @FXML private TreeTableView<edu.wpi.TeamE.algorithms.pathfinding.Node> treeTable;
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
        }*/

    public void prepareNodes(ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Node> array, TreeTableView<edu.wpi.TeamE.algorithms.pathfinding.Node> table) {
        if (table.getRoot() == null) {
            edu.wpi.TeamE.algorithms.pathfinding.Node root = new edu.wpi.TeamE.algorithms.pathfinding.Node("Location", 2, 2, "location", "location", "location", "location", "location");
            final TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Node> rootNode = new TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Node>(root);
            table.setRoot(rootNode);
            /*TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Node, String> column = new TreeTableColumn<>("Location");
            column.setPrefWidth(150);
            column.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getX()));
            table.getColumns().add(column);*/
            //column 2
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Node, Number> column2 = new TreeTableColumn<>("X-Cord");
            column2.setPrefWidth(150);
            column2.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getX()));
            table.getColumns().add(column2);
            //column 3
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Node, Number> column3 = new TreeTableColumn<>("Y-Cord");
            column3.setPrefWidth(150);
            column3.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getY()));
            table.getColumns().add(column3);
            //column 4
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Node, Number> column4 = new TreeTableColumn<>("ID");
            column4.setPrefWidth(150);
            column4.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getX()));
            table.getColumns().add(column4);
            //column 5
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Node, Number> column5 = new TreeTableColumn<>("Floor");
            column5.setPrefWidth(150);
            column5.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getY()));
            table.getColumns().add(column5);
            for (int i = 0; i < array.size(); i++) {
                edu.wpi.TeamE.algorithms.pathfinding.Node s = array.get(i);
                int n = array.get(i).getX();
                final TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Node> node = new TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Node>(s);
                table.getRoot().getChildren().add(node);
            }
        }
    }

    public void editNode(ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Node> array, TreeTableView<String> table) {
        TreeItem<String> node = table.getSelectionModel().getSelectedItem();
        if (table.getSelectionModel().getSelectedItem() != null) {
            //function that takes in string (?) which will call to node that wants to be edited and
            //inputted info from user, returns void
            /*for(int i = 0; i < array.size(); i++) {
                if(node.equals(array.get(i).name)) {

                }
            }*/
        }
    }

    public void addNode(ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Node> array, TreeTableView<String> table) {
        //function that takes in users inputted info, updates database
    }

    public void deleteNode(ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Node> array, TreeTableView<String> table) {
        TreeItem<String> node = table.getSelectionModel().getSelectedItem();
        if (table.getSelectionModel().getSelectedItem() != null) {
            //function that takes in identifier of selected node, deletes the node
        }
    }


    public void startTableButton(ActionEvent actionEvent) {
        //creating the root for the array
        edu.wpi.TeamE.algorithms.pathfinding.Node node0 = new
                edu.wpi.TeamE.algorithms.pathfinding.Node("Location",
                2, 2, "location", "location",
                "location", "location", "location");
        //creating array
        ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Node> array = new ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Node>();
        //creating root node
        final TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Node> test = new TreeItem<Node>(node0);
        test.setExpanded(true);

        //will be in init later
        File file = new File("L1Nodes.csv");
        makeConnection connection = new makeConnection();
        connection.deleteAllTables();
        connection.createTables();
        connection.populateTable("node", file);
        //will be in init later

        array = connection.getAllNodes();
        prepareNodes(array, treeTable);
    }
    @FXML
    public void toFileUpload() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/MapFileUpload.fxml"));
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
            // TODO: delete node table only plzzzzzzzzzzzzz
            connection.createTables();
            // TODO: create node table only plzzzzzzzzzzzzz
            connection.populateTable("node", file);
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





