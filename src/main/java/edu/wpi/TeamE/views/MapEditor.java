package edu.wpi.TeamE.views;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;


public class MapEditor {

    @FXML
    private TreeTableView<Node> treeTable;
    @FXML
    private JFXTextField xCordInput;
    @FXML
    private JFXTextField yCordInput;
    @FXML
    private JFXTextField idInput;
    @FXML
    private JFXTextField floorInput;
    @FXML
    private JFXTextField typeInput;
    @FXML
    private JFXTextField buildingInput;
    @FXML
    private JFXTextField longNameInput;
    @FXML
    private JFXTextField shortNameInput;

    /**
     * when page loaded, displays the data
     */
    @FXML
    void initialize() {
        assert treeTable != null : "fx:id=\"treeTable\" was not injected: check your FXML file 'MapEditor.fxml'.";
        assert xCordInput != null : "fx:id=\"xCordInput\" was not injected: check your FXML file 'MapEditor.fxml'.";
        assert longNameInput != null : "fx:id=\"longNameInput\" was not injected: check your FXML file 'MapEditor.fxml'.";
        assert yCordInput != null : "fx:id=\"yCordInput\" was not injected: check your FXML file 'MapEditor.fxml'.";
        assert idInput != null : "fx:id=\"idInput\" was not injected: check your FXML file 'MapEditor.fxml'.";
        assert shortNameInput != null : "fx:id=\"shortNameInput\" was not injected: check your FXML file 'MapEditor.fxml'.";
        assert floorInput != null : "fx:id=\"floorInput\" was not injected: check your FXML file 'MapEditor.fxml'.";
        assert typeInput != null : "fx:id=\"typeInput\" was not injected: check your FXML file 'MapEditor.fxml'.";
        assert buildingInput != null : "fx:id=\"buildingInput\" was not injected: check your FXML file 'MapEditor.fxml'.";
        //assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'MapEditor.fxml'.";
        prepareNodes(treeTable);
    }


    /**
     * brings user to the map editor navigation page
     *
     * @param e
     */
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
     * When the table is empty (aka no root), create the proper columns
     * Go through the array of Nodes and create a treeItem for each one,
     * add each one to the treeTable
     *
     * @param table this is the table being prepared with the nodes
     */
    public void prepareNodes(TreeTableView<Node> table) {
        makeConnection connection = makeConnection.makeConnection();
        ArrayList<Node> array = connection.getAllNodes();
        if (table.getRoot() == null) {
            Node node0 = new
                    Node("ID",
                    0, 0, "Floor", "Building",
                    "Node Type", "Long Name", "Short Name");
            final TreeItem<Node> rootNode = new TreeItem<Node>(node0);
            table.setRoot(rootNode);
            //Column 1 - Location
            TreeTableColumn<Node, String> column = new TreeTableColumn<>("Location");
            column.setPrefWidth(320);
            column.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("longName")));
            table.getColumns().add(column);
            //Column 2 - X Coordinate
            TreeTableColumn<Node, Number> column2 = new TreeTableColumn<>("X-Cord");
            column2.setPrefWidth(150);
            column2.setCellValueFactory((CellDataFeatures<Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getX()));
            table.getColumns().add(column2);
            //Column 3 - Y Coordinate
            TreeTableColumn<Node, Number> column3 = new TreeTableColumn<>("Y-Cord");
            column3.setPrefWidth(150);
            column3.setCellValueFactory((CellDataFeatures<Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getY()));
            table.getColumns().add(column3);
            //Column 4 - Node ID
            TreeTableColumn<Node, String> column4 = new TreeTableColumn<>("ID");
            column4.setPrefWidth(150);
            column4.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("id")));
            table.getColumns().add(column4);
            //Column 5 - Floor
            TreeTableColumn<Node, String> column5 = new TreeTableColumn<>("Floor");
            column5.setPrefWidth(150);
            column5.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("floor")));
            table.getColumns().add(column5);
            //Column 6 - Building
            TreeTableColumn<Node, String> column6 = new TreeTableColumn<>("Building");
            column6.setPrefWidth(150);
            column6.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("building")));
            table.getColumns().add(column6);
            //Column 7 - Short Name
            TreeTableColumn<Node, String> column7 = new TreeTableColumn<>("Short Name");
            column7.setPrefWidth(150);
            column7.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("shortName")));
            table.getColumns().add(column7);
            //Column 8 - Type of Node
            TreeTableColumn<Node, String> column8 = new TreeTableColumn<>("Type");
            column8.setPrefWidth(150);
            column8.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("type")));
            table.getColumns().add(column8);
        }
        if (table.getRoot().getChildren().isEmpty() == false && array.size() > 0) {
            table.getRoot().getChildren().remove(0, array.size() - 1);
        }
        for (int i = 0; i < array.size(); i++) {
            Node s = array.get(i);
            //int n = array.get(i).getX();
            final TreeItem<Node> node = new TreeItem<Node>(s);
            table.getRoot().getChildren().add(node);
        }
    }

    @FXML
    public void editNodeButton(ActionEvent e) {
        editNode(treeTable);
    }


    /**
     * looks at each field that the user could input into, whichever ones are not empty
     * the information is extracted and the node that the user selected is edited using
     * database's edit node fcn
     *
     * @param table this is the table of nodes that is having a node edited
     */
    public void editNode(TreeTableView<Node> table) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            String nodeID = table.getSelectionModel().getSelectedItem().getValue().get("id");
            Integer xVal = null;
            Integer yVal = null;
            String floor = null;
            String longName = null;
            String shortName = null;
            String type = null;
            String building = null;
            if (!floorInput.getText().equals("")) {
                floor = floorInput.getText();
            }
            if (!longNameInput.getText().equals("")) {
                longName = longNameInput.getText();
            }
            if (!shortNameInput.getText().equals("")) {
                shortName = shortNameInput.getText();
            }
            if (!typeInput.getText().equals("")) {
                type = typeInput.getText();
            }
            if (!buildingInput.getText().equals("")) {
                building = buildingInput.getText();
            }
            if (!xCordInput.getText().equals("")) {
                System.out.println(xCordInput.getText());
                System.out.println("inside xcord");
                xVal = Integer.parseInt(xCordInput.getText());
                xVal = Integer.valueOf(xVal);
            }
            if (!yCordInput.getText().equals("")) {
                yVal = Integer.parseInt(yCordInput.getText());
                yVal = Integer.valueOf(yVal);
            }
            makeConnection connection = makeConnection.makeConnection();
            connection.modifyNode(nodeID, xVal, yVal, floor, building, type, longName, shortName);
        }
    }

    /**
     * Autogenerate NodeIDs
     * Elevators need to have the format `Elevator X xxxxx`
     * @param type The type of Node this is
     * @param floor The floor this Node is on
     * @param longName The longName of the node
     * @return The NodeID of the given Node
     */
    public String genNodeID(String type, String floor, String longName){
        StringBuilder SB = new StringBuilder("e");
        SB.append(type);

        if (type.equalsIgnoreCase("ELEV")) {
            SB.append("00");
            SB.append(longName.charAt(9));
            //Elevator names need to start with 'Elevator X xxxxx"
        } else {
            makeConnection connection = makeConnection.makeConnection();
            int instance = connection.countNodeTypeOnFloor("e", floor, type) + 1;
            SB.append(String.format("%03d", instance));
        }

        try{
            int num = Integer.parseInt(floor);
            SB.append("0").append(num);
        } catch (NumberFormatException e) {
            if (floor.equalsIgnoreCase("G") || floor.equalsIgnoreCase("GG")){
                SB.append("GG");
            } else {
                SB.append(floor);
            }
        }

        return SB.toString();
    }

    /**
     * retrieves all the inputted info from the user, creates a new node and adds it to database
     * using database's addNode fcn
     * @return
     */
    public int addNode() {
        int i = -1;
        makeConnection connection = makeConnection.makeConnection();
        int xVal = Integer.parseInt(xCordInput.getText());
        int yVal = Integer.parseInt(yCordInput.getText());
        i = connection.addNode(idInput.getText(), xVal, yVal, floorInput.getText(), buildingInput.getText(), typeInput.getText(), longNameInput.getText(), shortNameInput.getText());
        return i;
    }

    @FXML
    public void addNodeButton(ActionEvent e) {
        addNode();
    }

    /**
     * retrieves the ID of the selected item in the table, passes that into deleteNode fcn from database
     * @param table
     */
    public int deleteNode(TreeTableView<Node> table) {
        int s = -1;
        TreeItem<Node> node = table.getSelectionModel().getSelectedItem();
        makeConnection connection = makeConnection.makeConnection();
        ArrayList<Node> array = connection.getAllNodes();
        if (table.getSelectionModel().getSelectedItem() != null) {
            System.out.println(table.getSelectionModel().getSelectedItem().getValue().get("id"));
            for(int i = 0; i < array.size(); i++) {
                if(array.get(i).get("id").equals(table.getSelectionModel().getSelectedItem().getValue().get("id"))) {
                    s = connection.deleteNode(array.get(i).get("id"));
                }
            }
        }
        return s;
    }

    @FXML
    public void deleteNodeButton(ActionEvent e) {
        deleteNode(treeTable);
    }

    @FXML
    public void startTableButton(ActionEvent actionEvent) {
        //creating the root for the array
        Node node0 = new
                Node("ID",
                0, 0, "Floor", "Building",
                "Node Type", "Long Name", "Short Name");
        //creating root node
        final TreeItem<Node> test = new TreeItem<Node>(node0);
        test.setExpanded(true);
        prepareNodes(treeTable);
    }

    @FXML
    public void fileOpener(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(App.getPrimaryStage());
        makeConnection connection = makeConnection.makeConnection();
        if (file != null) {
            //Have to save edge table so we can get it back after deleting
            connection.getNewCSVFile("hasEdge");
            File saveEdges = new File("src/main/resources/edu/wpi/TeamE/output/outputEdge.csv");

            //This is where tables are cleared and refilled
            connection.deleteAllTables();
            try {
                connection.createTables();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            connection.populateTable("node", file);
            connection.populateTable("hasEdge", saveEdges);
            System.out.println("Success");
        }
    }

    @FXML
    private void openFile(ActionEvent e) throws IOException {
        makeConnection connection = makeConnection.makeConnection();
        connection.getNewCSVFile("node");
        File file = new File("src/main/resources/edu/wpi/TeamE/output/outputNode.csv");
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);
    }

}