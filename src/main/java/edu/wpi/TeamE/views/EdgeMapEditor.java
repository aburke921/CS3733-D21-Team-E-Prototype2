package edu.wpi.TeamE.views;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamE.algorithms.Edge;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;


public class EdgeMapEditor {

    @FXML private TreeTableView<Edge> treeTable;
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

    @FXML
    public void getHelpDefault(ActionEvent e) {
    }

    /**
     *
     * @param table this is the TreeTableView that is editing
     */
    public void prepareEdges(TreeTableView<Edge> table) {
        makeConnection connection = makeConnection.makeConnection();
        ArrayList<Edge> array = connection.getAllEdges();
        if (table.getRoot() == null) {
            Edge edge0 = new
                    Edge("ID", "0", "1", 0.00);
            final TreeItem<Edge> rootEdge = new TreeItem<>(edge0);
            table.setRoot(rootEdge);
            //column 1 - ID
            TreeTableColumn<Edge, String> column1 = new TreeTableColumn<>("ID");
            column1.setPrefWidth(320);
            column1.setCellValueFactory((CellDataFeatures<Edge, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getId()));
            table.getColumns().add(column1);
            //column 2 - Start Node
            TreeTableColumn<Edge, String> column2 = new TreeTableColumn<>("Start Node ID");
            column2.setPrefWidth(320);
            column2.setCellValueFactory((CellDataFeatures<Edge, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getStartNodeId()));
            table.getColumns().add(column2);
            //column 3 - End Node
            TreeTableColumn<Edge, String> column3 = new TreeTableColumn<>("End Node ID");
            column3.setPrefWidth(320);
            column3.setCellValueFactory((CellDataFeatures<Edge, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getEndNodeId()));
            table.getColumns().add(column3);
        }
        for (int i = 0; i < array.size(); i++) {
            Edge s = array.get(i);
            //int n = array.get(i).getX();
            final TreeItem<Edge> edge = new TreeItem<>(s);
            table.getRoot().getChildren().add(edge);
        }
    }

    /**
     *
     * @param table this is the table it is grabbing the edge to edit
     */
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

    @FXML
    public void editEdgeButton(ActionEvent e) {
        editEdge(treeTable);
    }

    //Function that take information after button press to add edge
    public void addEdge() {
        makeConnection connection = makeConnection.makeConnection();
        connection.addEdge(idInput.getText(), startNodeIDInput.getText(), endNodeIDInput.getText());
        System.out.println("This happened");
    }

    @FXML
    public void addEdgeButton(ActionEvent e) {
        addEdge();
    }

    /**
     *
     * @param table this is the table of edges that it is deleting from
     */
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

    @FXML
    public void deleteEdgeButton(ActionEvent e) {
        deleteEdge(treeTable);
    }

    @FXML
    public void startTableButton(ActionEvent actionEvent) {

        //creating the root for the array
        Edge edge0 = new
                Edge("ID","StartNode","EndNode",0.00);
        //creating array
        ArrayList<Edge> array = new ArrayList<>();
        //creating root node
        final TreeItem<Edge> test = new TreeItem<>(edge0);
        test.setExpanded(true);

        prepareEdges(treeTable);
    }

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
       /* assert treeTable != null : "fx:id=\"treeTable\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        assert startNodeIDInput != null : "fx:id=\"xCordInput\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        assert endNodeIDInput != null : "fx:id=\"longNameInput\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        assert idInput != null : "fx:id=\"idInput\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        */
        prepareEdges(treeTable);

    }


}





