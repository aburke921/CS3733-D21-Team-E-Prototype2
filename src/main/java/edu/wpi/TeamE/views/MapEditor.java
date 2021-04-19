package edu.wpi.TeamE.views;
import com.jfoenix.controls.*;
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
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
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;



public class MapEditor {

    @FXML private TreeTableView<Node> treeTable;
    @FXML private JFXTextField xCordInput;
    @FXML private JFXTextField yCordInput;
    @FXML private JFXComboBox floorInput;
    @FXML private JFXComboBox typeInput;
    @FXML private JFXComboBox buildingInput;
    @FXML private JFXTextField longNameInput;
    @FXML private JFXTextField shortNameInput;
    @FXML private StackPane stackPane;
    @FXML private FlowPane flowPane;

    @FXML // fx:id="exit"
    private Polygon exit;

    /**
     * when page loaded, displays the data
     */
    @FXML
    void initialize() {
        prepareNodes(treeTable);
        //Creating Type dropdown
        ArrayList<String> nodeTypeArrayList = new ArrayList<String>();
        nodeTypeArrayList.add("HALL");
        nodeTypeArrayList.add("CONF");
        nodeTypeArrayList.add("DEPT");
        nodeTypeArrayList.add("HALL");
        nodeTypeArrayList.add("ELEV");
        nodeTypeArrayList.add("INFO");
        nodeTypeArrayList.add("LABS");
        nodeTypeArrayList.add("REST");
        nodeTypeArrayList.add("RETL");
        nodeTypeArrayList.add("STAI");
        nodeTypeArrayList.add("SERV");
        nodeTypeArrayList.add("EXIT");
        nodeTypeArrayList.add("BATH");
        ObservableList<String> listOfType = FXCollections.observableArrayList();
        listOfType.addAll(nodeTypeArrayList);

        //Creating Floor Dropdown
        ArrayList<String> nodeFloorArrayList = new ArrayList<String>();
        nodeFloorArrayList.add("L1");
        nodeFloorArrayList.add("L2");
        nodeFloorArrayList.add("1");
        nodeFloorArrayList.add("2");
        nodeFloorArrayList.add("3");
        ObservableList<String> listOfFloors = FXCollections.observableArrayList();
        listOfFloors.addAll(nodeFloorArrayList);
        //Creating Building Dropdown
        ArrayList<String> nodeBuildingArrayList = new ArrayList<String>();
        nodeBuildingArrayList.add("BTM");
        nodeBuildingArrayList.add("45 Francis");
        nodeBuildingArrayList.add("15 Francis");
        nodeBuildingArrayList.add("Tower");
        nodeBuildingArrayList.add("Shapiro");
        ObservableList<String> listOfBuildings = FXCollections.observableArrayList();
        listOfBuildings.addAll(nodeBuildingArrayList);

        //add ObservableLists to dropdowns
        typeInput.setItems(listOfType);
        floorInput.setItems(listOfFloors);
        buildingInput.setItems(listOfBuildings);

        exit.setOnMouseClicked(event -> {
            App app = new App();
            app.stop();
        });

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
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * brings user to the help page
     *
     * @param e
     */
    @FXML
    public void getHelpDefault(ActionEvent e) {
    }

    /**
     * When the table is empty (aka no root), create the proper columns
     * Go through the array of Nodes and create a treeItem for each one,
     * add each one to the treeTable
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

    /**
     * Runs editNode fcn when edit node button is pressed
     * @param e
     */
    @FXML
    public void editNodeButton(ActionEvent e) {
        editNode(treeTable);
    }


    /**
     * looks at each field that the user could input into, whichever ones are not empty
     * the information is extracted and the node that the user selected is edited using
     * database's edit node fcn
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
            if (!floorInput.getValue().toString().equals("")) {
                floor = floorInput.getValue().toString();
            }
            if (longNameInput.getText().equals("")) {
                errorPopup("Must input Long Name");
                return;
            }
            if (!shortNameInput.getText().equals("")) {
                shortName = shortNameInput.getText();
            }
            if (!typeInput.getSelectionModel().equals("")) {
                type = typeInput.getValue().toString();
            }
            if (!buildingInput.getValue().toString().equals("")) {
                building = buildingInput.getValue().toString();
            }
            if (xCordInput.getText().equals("")) {
                errorPopup("Must input X Coordinate");
                return;
            }
            if (yCordInput.getText().equals("")) {
                errorPopup("Must input Y Coordinate");
                return;
            }
            longName = longNameInput.getText();

            xVal = Integer.parseInt(xCordInput.getText());
            xVal = Integer.valueOf(xVal);

            yVal = Integer.parseInt(yCordInput.getText());
            yVal = Integer.valueOf(yVal);

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
        if (floorInput.getValue().toString().equals("")) {
            errorPopup("Must input Floor");
            return i;
        }
        if (longNameInput.getText().equals("")) {
            errorPopup("Must input Long Name");
            return i;
        }
        if (shortNameInput.getText().equals("")) {
            errorPopup("Must input Short Name");
            return i;
        }
        if (typeInput.getSelectionModel().equals("")) {
            errorPopup("Must input Type");
            return i;
        }
        if (buildingInput.getValue().toString().equals("")) {
            errorPopup("Must input Building");
            return i;
        }
        if (xCordInput.getText().equals("")) {
            errorPopup("Must input X Coordinate");
            return i;
        }
        if (yCordInput.getText().equals("")) {
            errorPopup("Must input Y Coordinate");
            return i;
        }
        int xVal = Integer.parseInt(xCordInput.getText());
        int yVal = Integer.parseInt(yCordInput.getText());
        i = connection.addNode(genNodeID(typeInput.getValue().toString(), floorInput.getValue().toString(), longNameInput.getText()), xVal, yVal, floorInput.getValue().toString(), buildingInput.getValue().toString(), typeInput.getValue().toString(), longNameInput.getText(), shortNameInput.getText());
        return i;
    }


    /**
     * calls the addNode fcn when the add node button is pressed
     * @param e
     */
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
        if (table.getSelectionModel().getSelectedItem() == null) {
            errorPopup("Must select Node to delete");
            return s;
        } else {
            System.out.println(table.getSelectionModel().getSelectedItem().getValue().get("id"));
            for (int i = 0; i < array.size(); i++) {
                if (array.get(i).get("id").equals(table.getSelectionModel().getSelectedItem().getValue().get("id"))) {
                    s = connection.deleteNode(array.get(i).get("id"));
                }
            }
        }
        return s;
    }



    /**
     * calls the deleteNode fcn when the delete button is clicked
     * @param e
     */
    @FXML
    public void deleteNodeButton(ActionEvent e) {
        deleteNode(treeTable);
    }

    /**
     * when refresh button is clicked, retrieves the arrayList of Nodes,
     * calls the function to display data using the array (prepareNodes)
     * @param actionEvent
     */
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
            connection.createTables();
            connection.populateTable("node", file);
            connection.populateTable("hasEdge", saveEdges);
            System.out.println("Success");
        }
    }



    /**
     *opens the file explorer on user's device, allows user to select CSV file,
     * uploads file to database, refreshes page
     * @param e actionevent
     */
    @FXML
    private void openFile(ActionEvent e) throws IOException {
        makeConnection connection = makeConnection.makeConnection();
        connection.getNewCSVFile("node");
        File file = new File("src/main/resources/edu/wpi/TeamE/output/outputNode.csv");
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);
    }

    @FXML
    private void errorPopup(String errorMessage) {
        JFXDialogLayout error = new JFXDialogLayout();
        error.setHeading(new Text("Error!"));
        error.setBody(new Text(errorMessage));
        JFXDialog dialog = new JFXDialog(stackPane, error, JFXDialog.DialogTransition.CENTER);
        JFXButton okay = new JFXButton("Okay");
        okay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();

            }
        });
        error.setActions(okay);
        dialog.show();
    }

}