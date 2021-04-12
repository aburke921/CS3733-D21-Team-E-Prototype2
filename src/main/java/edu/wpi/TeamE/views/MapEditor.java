package edu.wpi.TeamE.views;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Group;
import javafx.scene.Node;
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

    @FXML
    private TreeTableView<NodeTest> treeTable;
    @FXML
    private FlowPane theStage;


    @FXML
    private void toDefault(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
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

    public void prepareNodes(ArrayList<NodeTest> array, TreeTableView<NodeTest> table) {
        if (table.getRoot() == null) {
            NodeTest root = new NodeTest("Location", 10);
            final TreeItem<NodeTest> rootNode = new TreeItem<NodeTest>(root);
            table.setRoot(rootNode);
            TreeTableColumn<NodeTest, String> column = new TreeTableColumn<>("Location");
            column.setPrefWidth(150);
            column.setCellValueFactory((CellDataFeatures<NodeTest, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().name));
            table.getColumns().add(column);
            //column 2
            TreeTableColumn<NodeTest, Number> column2 = new TreeTableColumn<>("X-Cord");
            column2.setPrefWidth(150);
            column2.setCellValueFactory((CellDataFeatures<NodeTest, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().num));
            table.getColumns().add(column2);
            //column 3
            TreeTableColumn<NodeTest, Number> column3 = new TreeTableColumn<>("Y-Cord");
            column3.setPrefWidth(150);
            column3.setCellValueFactory((CellDataFeatures<NodeTest, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().num));
            table.getColumns().add(column3);
            //column 4
            TreeTableColumn<NodeTest, Number> column4 = new TreeTableColumn<>("ID");
            column4.setPrefWidth(150);
            column4.setCellValueFactory((CellDataFeatures<NodeTest, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().num));
            table.getColumns().add(column4);
            for (int i = 0; i < array.size(); i++) {
                NodeTest s = array.get(i);
                int n = array.get(i).num;
                final TreeItem<NodeTest> node = new TreeItem<NodeTest>(s);
                table.getRoot().getChildren().add(node);
            }
        }
    }

    public void editNode(ArrayList<NodeTest> array, TreeTableView<String> table) {
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

    public void addNode(ArrayList<NodeTest> array, TreeTableView<String> table) {
        //function that takes in users inputted info, updates database
    }

    public void deleteNode(ArrayList<NodeTest> array, TreeTableView<String> table) {
        TreeItem<String> node = table.getSelectionModel().getSelectedItem();
        if (table.getSelectionModel().getSelectedItem() != null) {
            //function that takes in identifier of selected node, deletes the node
        }
    }

        /*public void start(Stage stage) {
            stage.setTitle("Tree Table View Samples");
            final Scene scene = new Scene(new Group(), 200, 400);
            Group sceneRoot = (Group)scene.getRoot();

            /* CODE
            //creating the nodes for the array
            NodeTest node0 = new NodeTest("Bathroom", 5);
            NodeTest node1 = new NodeTest("Bathroom2", 5);
            //creating array
            ArrayList<NodeTest> array = new ArrayList<NodeTest>();
            //adding nodes
            array.add(node0);
            array.add(node1);
            //creating root node
            final TreeItem<NodeTest> test = new TreeItem<NodeTest>(node0);
            test.setExpanded(true);
            //running fcn
            //prepareNodes(array, test);
            /*END


            //Creating a column for name
            TreeTableColumn<NodeTest, String> column = new TreeTableColumn<>("Location");
            column.setPrefWidth(150);

            //Defining cell content
            column.setCellValueFactory((CellDataFeatures<NodeTest, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().name));

            //creating a column for num
            TreeTableColumn<NodeTest, Number> column2 = new TreeTableColumn<>("Floor");
            column2.setPrefWidth(150);

            //Defining cell content
            column2.setCellValueFactory((CellDataFeatures<NodeTest, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().num));


            //Creating a tree table view
            final TreeTableView<NodeTest> treeTableView = new TreeTableView<NodeTest>(test);
            treeTableView.getColumns().add(column);
            treeTableView.getColumns().add(column2);
            treeTableView.setPrefWidth(1000);
            treeTableView.setShowRoot(true);
            sceneRoot.getChildren().add(treeTableView);
            stage.setScene(scene);
            stage.show();
        } */

    public void startTableButton(ActionEvent actionEvent) {
        //creating the nodes for the array
        NodeTest node0 = new NodeTest("Bathroom", 5);
        NodeTest node1 = new NodeTest("Bathroom2", 5);
        //creating array
        ArrayList<NodeTest> array = new ArrayList<NodeTest>();
        //adding nodes
        array.add(node0);
        array.add(node1);
        //creating root node
        final TreeItem<NodeTest> test = new TreeItem<NodeTest>(node0);
        test.setExpanded(true);
        //running fcn
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



    public void fileOpener(final ActionEvent e) {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(App.getPrimaryStage());
        if (file != null) {
            openFile(file);
        }
    }

    private Desktop desktop = Desktop.getDesktop();

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





