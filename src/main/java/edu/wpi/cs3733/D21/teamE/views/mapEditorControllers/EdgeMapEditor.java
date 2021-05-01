package edu.wpi.cs3733.D21.teamE.views.mapEditorControllers;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D21.teamE.map.Edge;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;


public class EdgeMapEditor {

    @FXML private TreeTableView<Edge> treeTable;
    @FXML private FlowPane theStage;
    @FXML private JFXComboBox idDropDown;
    @FXML private JFXComboBox startEdge;
    @FXML private JFXComboBox endEdge;

    @FXML private ImageView imageView;
    @FXML private Pane pane;

    @FXML // fx:id="exit"
    private Polygon exit;

    @FXML
    private void toNavigation(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/MapEditorNavigation.fxml"));
            App.changeScene(root);
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

        ArrayList<Edge> array = DB.getAllEdges();
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
        treeTable.setShowRoot(false);
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
            String edgeID = null;
            String startID = null;
            String endID = null;
            if(idDropDown.getValue() != null) {
                edgeID = idDropDown.getValue().toString();
            }
            if(startEdge.getValue() != null) {
                startID = startEdge.getValue().toString();
            }
            if(endEdge.getValue() != null) {
                endID = endEdge.getValue().toString();
            }

            DB.modifyEdge(edgeID, startID, endID);
            DB.deleteEdge(table.getSelectionModel().getSelectedItem().getValue().getStartNodeId(), table.getSelectionModel().getSelectedItem().getValue().getEndNodeId());
        }
    }

    @FXML
    public void editEdgeButton(ActionEvent e) {
        editEdge(treeTable);
    }

    //Function that take information after button press to add edge
    public void addEdge() {

        if(startEdge.getValue() != null && endEdge.getValue() != null) {
            String ID = startEdge.getValue().toString() + "_" + endEdge.getValue().toString();
            DB.addEdge(ID, startEdge.getValue().toString(), endEdge.getValue().toString());
            System.out.println("This happened");
        }
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

        ArrayList<Edge> array = DB.getAllEdges();
        if(idDropDown.getValue() != null && startEdge.getValue() != null && endEdge.getValue() != null) {
            for(int i = 0; i < array.size(); i++) {
                if(array.get(i).getId().equals(idDropDown.getValue().toString())) {
                    System.out.println("This lies between " + startEdge.getValue().toString() + " and " + endEdge.getValue().toString());
                    DB.deleteEdge(startEdge.getValue().toString(), endEdge.getValue().toString());
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

//    @FXML
//    public void fileOpener(ActionEvent e) {
//        FileChooser fileChooser = new FileChooser();
//        File file = fileChooser.showOpenDialog(App.getPrimaryStage());
//        makeConnection connection = makeConnection.makeConnection();
//        if (file != null) {
//            connection.deleteEdgeTable();
//            connection.createEdgeTable();
//            connection.populateTable("hasEdge", file);
//            System.out.println("Success");
//        }
//    }
//
//    @FXML
//    private void openFile(ActionEvent e) throws IOException {
//
//        makeConnection connection = makeConnection.makeConnection();
//        connection.getNewCSVFile("hasEdge");
//        File file = new File("CSVs/outputEdge.csv");
//        Desktop desktop = Desktop.getDesktop();
//        desktop.open(file);
//
//    }



    @FXML
    void initialize() {
       /* assert treeTable != null : "fx:id=\"treeTable\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        assert startNodeIDInput != null : "fx:id=\"xCordInput\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        assert endNodeIDInput != null : "fx:id=\"longNameInput\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        assert idInput != null : "fx:id=\"idInput\" was not injected: check your FXML file 'EdgeMapEditor.fxml'.";
        */

        prepareEdges(treeTable);

        ArrayList<String> nodeIDArrayList = new ArrayList<String>();
        ArrayList< Node > nodeArray = DB.getAllNodes();
        for (int i = 0; i < nodeArray.size(); i++) {
            Node s = nodeArray.get(i);
            final TreeItem<Node> node = new TreeItem<Node>(s);
            nodeIDArrayList.add(s.get("id"));
        }
        ObservableList<String> listOfIDS = FXCollections.observableArrayList();
        listOfIDS.addAll(nodeIDArrayList);

        ArrayList<Edge> array = new ArrayList<Edge>();
        ArrayList<String> edgeIDS = new ArrayList<String>();
        array = DB.getAllEdges();
        for(int i = 0; i < array.size(); i++) {
            edgeIDS.add(array.get(i).getId());

        }
        ObservableList<String> listOfEdges = FXCollections.observableArrayList();
        listOfEdges.addAll(edgeIDS);
        //add ObservableLists to dropdowns
        idDropDown.setItems(listOfEdges);
        startEdge.setItems(listOfIDS);
        endEdge.setItems(listOfIDS);

        exit.setOnMouseClicked(event -> {
            App app = new App();
            app.stop();
        });

        //set image to map
        javafx.scene.image.Image image = new Image("edu/wpi/cs3733/D21/teamE/maps/1.png");
        imageView.setImage(image);

        //when tree table is clicked
        treeTable.setOnMouseClicked(event -> {
            //make sure that a edge is actually selected
            if (treeTable.getSelectionModel().getSelectedItem() != null) {
                Edge edge = treeTable.getSelectionModel().getSelectedItem().getValue();
                if (edge.getId() == null) {
                    return;
                }
                //clear the map
                pane.getChildren().clear();
                //calculate scaling based on image and imageView size
                double scale = image.getWidth() / imageView.getFitWidth();
                //connect to database


                //Retrieve the x and y coordinates of the nodes connected to the edge
                double xCoordStart = (double) DB.getNodeInfo(edge.getStartNodeId()).getX() / scale;
                double yCoordStart = (double) DB.getNodeInfo(edge.getStartNodeId()).getY() / scale;
                double xCoordEnd = (double) DB.getNodeInfo(edge.getEndNodeId()).getX() / scale;
                double yCoordEnd = (double) DB.getNodeInfo(edge.getEndNodeId()).getY() / scale;


                //Create a line using those coordinates
                Line line = new Line(xCoordStart, yCoordStart, xCoordEnd, yCoordEnd);
                line.setStrokeLineCap(StrokeLineCap.ROUND);
                line.setStrokeWidth(3);
                line.setStroke(Color.RED);

                //display the line on the map
                Group g = new Group(line);
                pane.getChildren().add(g);
            }
        });

    }


}





