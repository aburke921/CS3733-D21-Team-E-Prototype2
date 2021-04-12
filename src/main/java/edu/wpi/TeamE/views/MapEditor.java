package edu.wpi.TeamE.views;
import com.jfoenix.controls.JFXTextField;
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
    @FXML private JFXTextField xCordInput;
    @FXML private JFXTextField yCordInput;
    @FXML private JFXTextField idInput;
    @FXML private JFXTextField floorInput;
    @FXML private JFXTextField typeInput;
    @FXML private JFXTextField buildingInput;
    @FXML private JFXTextField longNameInput;
    @FXML private JFXTextField shortNameInput;


    /**
     * brings user to the map editor navigation page
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

    /**
     * brings user to the help page
     * @param e
     */
    public void getHelpDefault(ActionEvent e) {
    }

    /**
     * When the table is empty (aka no root), create the proper columns
     * Go through the array of Nodes and create a treeItem for each one,
     * add each one to the treeTable
     * @param array
     * @param table
     */
    public void prepareNodes(ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Node> array, TreeTableView<edu.wpi.TeamE.algorithms.pathfinding.Node> table) {
        if (table.getRoot() == null) {
            edu.wpi.TeamE.algorithms.pathfinding.Node node0 = new
                    edu.wpi.TeamE.algorithms.pathfinding.Node("ID",
                    0, 0, "Floor", "Building",
                    "Node Type", "Long Name", "Short Name");
            final TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Node> rootNode = new TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Node>(node0);
            table.setRoot(rootNode);
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Node, String> column = new TreeTableColumn<>("Location");
            column.setPrefWidth(320);
            column.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("longName")));
            table.getColumns().add(column);
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
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Node, String> column4 = new TreeTableColumn<>("ID");
            column4.setPrefWidth(150);
            column4.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("id")));
            table.getColumns().add(column4);
            //column 5
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Node, String> column5 = new TreeTableColumn<>("Floor");
            column5.setPrefWidth(150);
            column5.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("floor")));
            table.getColumns().add(column5);
            //column 6
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Node, String> column6 = new TreeTableColumn<>("Building");
            column6.setPrefWidth(150);
            column6.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("building")));
            table.getColumns().add(column6);
            //column 7
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Node, String> column7 = new TreeTableColumn<>("Short Name");
            column7.setPrefWidth(150);
            column7.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("shortName")));
            table.getColumns().add(column7);
            //column 8
            TreeTableColumn<edu.wpi.TeamE.algorithms.pathfinding.Node, String> column8 = new TreeTableColumn<>("Type");
            column8.setPrefWidth(150);
            column8.setCellValueFactory((CellDataFeatures<edu.wpi.TeamE.algorithms.pathfinding.Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("type")));
            table.getColumns().add(column8);
        }
        if(table.getRoot().getChildren().isEmpty() == false) {
            table.getRoot().getChildren().remove(0, array.size());
        }
            for (int i = 0; i < array.size(); i++) {
                edu.wpi.TeamE.algorithms.pathfinding.Node s = array.get(i);
                //int n = array.get(i).getX();
                final TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Node> node = new TreeItem<edu.wpi.TeamE.algorithms.pathfinding.Node>(s);
                table.getRoot().getChildren().add(node);
            }
        }


    /**
     * looks at each field that the user could input into, whichever ones are not empty
     * the information is extracted and the node that the user selected is edited using
     * database's edit node fcn
     * @param array
     * @param table
     */
    public void editNode(ArrayList<edu.wpi.TeamE.algorithms.pathfinding.Node> array, TreeTableView<Node> table) {
        TreeItem<Node> node = table.getSelectionModel().getSelectedItem();
        if (table.getSelectionModel().getSelectedItem() != null) {
            String nodeID = table.getSelectionModel().getSelectedItem().getValue().get("id");
            //TODO add in database editNode fcn using all variables
            if(floorInput.getText() != null) {
                String floor = floorInput.getText();
            } else {
                String floor = null;
            }
            if(longNameInput.getText() != null) {
                String longName = longNameInput.getText();
            } else {
                String longName = null;
            }
            if(shortNameInput.getText() != null) {
                String shortName = shortNameInput.getText();
            } else {
                String shortName = null;
            }
            if(typeInput.getText() != null) {
                String type = typeInput.getText();
            } else {
                String type = null;
            }
            if(buildingInput.getText() != null) {
                String building = buildingInput.getText();
            } else {
                String building = null;
            }

                }
            }


    /**
     * retrieves all the inputted info from the user, creates a new node and adds it to database
     * using database's addNode fcn
     * @return
     */
    public int addNode() {
        int i = 0;
        //TODO figure out what to do with array
        ArrayList<Node> array = new ArrayList<Node>();
        //TODO will be in init
       //will be in init
        File file = new File("L1Nodes.csv");
        makeConnection connection = new makeConnection();
        connection.deleteAllTables();
        connection.createTables();
        //connection.populateTable("node", file);

        int xVal = Integer.parseInt(xCordInput.getText());
        int yVal = Integer.parseInt(yCordInput.getText());
        i = connection.addNode(idInput.getText(), xVal, yVal, floorInput.getText(), buildingInput.getText(), typeInput.getText(), longNameInput.getText(), shortNameInput.getText());
        System.out.println(i);
        if(i == 1) {
            Node n = new Node(idInput.getText(), xVal, yVal, floorInput.getText(), buildingInput.getText(), typeInput.getText(), longNameInput.getText(), shortNameInput.getText());
            array.add(n);
            prepareNodes(array, treeTable);
        } else {
            System.out.println("Error");
        }
        return i;
    }

    /**
     * calls the addNode fcn when the add node button is pressed
     * @param e
     */
    public void addNodeButton(ActionEvent e) {
        //TODO figure out what array input to use
        addNode();
        //prepareNodes(array, treeTable);
    }

    /**
     * retrieves the ID of the selected item in the table, passes that into deleteNode fcn from database
     * @param array
     * @param table
     */

    public void deleteNode(ArrayList<Node> array, TreeTableView<Node> table) {
        TreeItem<Node> node = table.getSelectionModel().getSelectedItem();
        if (table.getSelectionModel().getSelectedItem() != null) {
            System.out.println(table.getSelectionModel().getSelectedItem().getValue().get("id"));
            for(int i = 0; i < array.size(); i++) {
                if(array.get(i).get("id") == table.getSelectionModel().getSelectedItem().getValue().get("id")) {
                    //TODO get connection stuff
                    //connection.deleteNode(array.get(i).get("id"));

                }

            }
        }
    }

    /**
     * calls the deleteNode fcn when the delete button is clicked
     * @param e
     */

    public void deleteNodeButton(ActionEvent e) {
        //TODO figure out what array input to use
        ArrayList<Node> array = new ArrayList<Node>();
        deleteNode(array, treeTable);
        //prepareNodes(array, treeTable);
    }

    /**
     * when refresh button is clicked, retrieves the arrayList of Nodes,
     * calls the function to display data using the array (prepareNodes)
     * @param actionEvent
     */

    public void startTableButton(ActionEvent actionEvent) {
        //creating the root for the array
        edu.wpi.TeamE.algorithms.pathfinding.Node node0 = new
                edu.wpi.TeamE.algorithms.pathfinding.Node("ID",
                0, 0, "Floor", "Building",
                "Node Type", "Long Name", "Short Name");
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





