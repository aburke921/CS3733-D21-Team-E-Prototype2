package edu.wpi.TeamE.views;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamE.algorithms.pathfinding.Edge;
import edu.wpi.TeamE.algorithms.pathfinding.Edge;
import edu.wpi.TeamE.algorithms.pathfinding.Node;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleWrapper;
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
    @FXML private JFXTextField idInput;
    @FXML private JFXTextField startNodeIDInput;
    @FXML private  JFXTextField endNodeIDInput;


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

    public void prepareEdges(TreeTableView<edu.wpi.TeamE.algorithms.pathfinding.Edge> table) {
        makeConnection connection = makeConnection.makeConnection();
        ArrayList<Edge> array = connection.getAllEdges();
        if (table.getRoot() == null) {
            edu.wpi.TeamE.algorithms.pathfinding.Edge edge0 = new
                    edu.wpi.TeamE.algorithms.pathfinding.Edge("ID", "0", "1", 0.00);
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
//            //column 4 - Length
//            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Edge, Double> column4 = new TreeTableColumn<>("Length");
//            column4.setPrefWidth(320);
//            column4.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Edge, Double> p) ->
//                    new ReadOnlyDoubleWrapper(p.getValue().getValue().getLength())
//            table.getColumns().add(column4);
        }
//        if(table.getRoot().getChildren().isEmpty() == false) {
//            table.getRoot().getChildren().remove(0, array.size());
//        }
        for (int i = 0; i < array.size(); i++) {
            edu.wpi.TeamE.algorithms.pathfinding.Edge s = array.get(i);
            //int n = array.get(i).getX();
            final TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Edge> edge = new TreeItem<>(s);
            table.getRoot().getChildren().add(edge);
        }
    }

    public void editEdge(TreeTableView<Edge> table) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            String edgeID = table.getSelectionModel().getSelectedItem().getValue().getId();
            String startID = null;
            String endID = null;
            if(!startNodeIDInput.getText().isEmpty()) {
                startID = startNodeIDInput.getText();
            }
            if(!endNodeIDInput.getText().isEmpty()) {
                endID = endNodeIDInput.getText();
            }
            makeConnection connection = makeConnection.makeConnection();
            connection.modifyEdge(edgeID, startID, endID);
            connection.deleteEdge(table.getSelectionModel().getSelectedItem().getValue().getStartNodeId(), table.getSelectionModel().getSelectedItem().getValue().getEndNodeId());
        }
    }

    public void editEdgeButton(ActionEvent e) {
        editEdge(treeTable);
    }

    public void addEdge(TreeTableView<Edge> table) {
        //function that takes in users inputted info, updates database
        makeConnection connection = makeConnection.makeConnection();
        ArrayList<Edge> array = connection.getAllEdges();
        connection.addEdge(idInput.getText(), startNodeIDInput.getText(), endNodeIDInput.getText());
        System.out.println("This happened");
    }

    public void addEdgeButton(ActionEvent e) {
        addEdge(treeTable);
    }



    public void deleteEdge(TreeTableView<Edge> table) {
        Edge edge = table.getSelectionModel().getSelectedItem().getValue();
        makeConnection connection = makeConnection.makeConnection();
        ArrayList<Edge> array = connection.getAllEdges();
        if(table.getSelectionModel().getSelectedItem().getValue().getId() != null) {
            for(int i = 0; i < array.size(); i++) {
                if(array.get(i).getId().equals(edge.getId())) {
                    System.out.println("This lies between " + edge.getStartNodeId() + " and " + edge.getEndNodeId());
                    connection.deleteEdge(edge.getStartNodeId(), edge.getEndNodeId());
                }
            }
        }
    }

    public void deleteEdgeButton(ActionEvent e) {
        deleteEdge(treeTable);
    }


    public void startTableButton(ActionEvent actionEvent) {

        //creating the root for the array
        edu.wpi.TeamE.algorithms.pathfinding.Edge edge0 = new
                edu.wpi.TeamE.algorithms.pathfinding.Edge("ID","StartNode","EndNode",0.00);
        //creating array
        ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Edge> array = new ArrayList<>();
        //creating root node
        final TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Edge> test = new TreeItem<>(edge0);
        test.setExpanded(true);


        prepareEdges(treeTable);
    }
//    @FXML
//    public void toFileUpload() {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/EdgeFileUpload.fxml"));
//            App.getPrimaryStage().getScene().setRoot(root);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }


    @FXML
    public void fileOpener(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(App.getPrimaryStage());
        makeConnection connection = makeConnection.makeConnection();
        if (file != null) {
            connection.deleteEdgeTable();
            connection.createEdgeTable();
            connection.populateTable("hasEdge", file);
            System.out.println("Success");
        }
    }

    final private Desktop desktop = Desktop.getDesktop();

    @FXML
    private void openFile(ActionEvent e) throws IOException {
        makeConnection connection = makeConnection.makeConnection();
        connection.getNewCSVFile("hasEdge");
        File file = new File("src/main/resources/edu/wpi/TeamE/output/outputEdge.csv");
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);
    }

    @FXML
    void initialize() {
        assert treeTable != null : "fx:id=\"treeTable\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        assert startNodeIDInput != null : "fx:id=\"xCordInput\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        assert endNodeIDInput != null : "fx:id=\"longNameInput\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        assert idInput != null : "fx:id=\"idInput\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        prepareEdges(treeTable);
    }


}





