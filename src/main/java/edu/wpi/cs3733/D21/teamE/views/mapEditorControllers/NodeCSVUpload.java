package edu.wpi.cs3733.D21.teamE.views.mapEditorControllers;

import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.map.Node;

import edu.wpi.cs3733.D21.teamE.DB;

import edu.wpi.cs3733.D21.teamE.database.makeConnection;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NodeCSVUpload {

    @FXML
    private TreeTableView<Node> treeTable;

    @FXML // fx:id="exit"
    private Polygon exit;

    /**
     * When the table is empty (aka no root), create the proper columns
     * Go through the array of Nodes and create a treeItem for each one,
     * add each one to the treeTable
     *
     * @param table this is the table being prepared with the nodes
     */
    public void prepareNodes(TreeTableView<Node> table) {

        ArrayList<Node> array = DB.getAllNodes();
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
            column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("longName")));
            table.getColumns().add(column);
            //Column 2 - X Coordinate
            TreeTableColumn<Node, Number> column2 = new TreeTableColumn<>("X-Cord");
            column2.setPrefWidth(150);
            column2.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getX()));
            table.getColumns().add(column2);
            //Column 3 - Y Coordinate
            TreeTableColumn<Node, Number> column3 = new TreeTableColumn<>("Y-Cord");
            column3.setPrefWidth(150);
            column3.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getY()));
            table.getColumns().add(column3);
            //Column 4 - Node ID
            TreeTableColumn<Node, String> column4 = new TreeTableColumn<>("ID");
            column4.setPrefWidth(150);
            column4.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("id")));
            table.getColumns().add(column4);
            //Column 5 - Floor
            TreeTableColumn<Node, String> column5 = new TreeTableColumn<>("Floor");
            column5.setPrefWidth(150);
            column5.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("floor")));
            table.getColumns().add(column5);
            //Column 6 - Building
            TreeTableColumn<Node, String> column6 = new TreeTableColumn<>("Building");
            column6.setPrefWidth(150);
            column6.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("building")));
            table.getColumns().add(column6);
            //Column 7 - Short Name
            TreeTableColumn<Node, String> column7 = new TreeTableColumn<>("Short Name");
            column7.setPrefWidth(150);
            column7.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("shortName")));
            table.getColumns().add(column7);
            //Column 8 - Type of Node
            TreeTableColumn<Node, String> column8 = new TreeTableColumn<>("Type");
            column8.setPrefWidth(150);
            column8.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("type")));
            table.getColumns().add(column8);
        }
        treeTable.setShowRoot(false);
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
    public void fileOpener(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(App.getPrimaryStage());
        makeConnection connection = makeConnection.makeConnection();
        if (file != null) {
            //Have to save edge table so we can get it back after deleting
            DB.getNewCSVFile("hasEdge");
            File saveEdges = new File("CSVs/outputEdge.csv");

            //This is where tables are cleared and refilled
            connection.deleteAllTables();
            DB.createAllTables();
            DB.populateTable("node", file);
            DB.populateTable("hasEdge", saveEdges);
            System.out.println("Some edges might be removed because their nodes are no longer here");
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
        DB.getNewCSVFile("node");
        File file = new File("src/main/resources/edu/wpi/cs3733/D21/teamE/output/outputNode.csv");
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);
    }

    @FXML
    public void getHelpDefault(ActionEvent actionEvent) {
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

    /**
     * brings user to the map editor navigation page
     *
     * @param e
     */
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
    void initialize() {
        prepareNodes(treeTable);

        exit.setOnMouseClicked(event -> {
            App app = new App();
            app.stop();
        });
    }


}

