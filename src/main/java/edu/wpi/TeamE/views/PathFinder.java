package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import edu.wpi.TeamE.algorithms.pathfinding.*;
import edu.wpi.TeamE.databases.*;

import edu.wpi.TeamE.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class PathFinder {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fix:id="findPathButton"
    public JFXButton findPathButton; // Value injected by FXMLLoader

    @FXML // fix:id="backButton"
    public Button backButton; // Value injected by FXMLLoader

    @FXML // fx:id="startLocationList"
    private JFXComboBox<String> startLocationList; // Value injected by FXMLLoader

    @FXML // fx:id="bathroomList"
    private JFXComboBox<String> endLocationList; // Value injected by FXMLLoader

    @FXML // fx:id="imageView"
    private ImageView imageView;

    @FXML // fx:id="pane"
    private Pane pane;

    private String startNodeID; // selected starting value's ID

    private String endNodeID; // selected ending value's ID

    ObservableList<String> list = FXCollections.observableArrayList(); //declaration

    ObservableList<String> listOfId = FXCollections.observableArrayList(); //declaration

    /**
     * Returns to {@link edu.wpi.TeamE.views.Default} page.
     * @param event calling event info
     */
    @FXML
    private void toDefault(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void getHelpDefault(ActionEvent event) {
        //todo, create help modal (refactor name, this was taken from Default page, hence the name)
    }

    /**
     * Gets selected item from {@link #startLocationList} dropdown.
     * todo, judging from comments, this will get full node info from DB? For now, I am using my own resolveID
     * @param event calling event info
     */
    @FXML
    void selectStartNode(ActionEvent event) {
        String dropdownSelected = ((JFXComboBox) event.getSource()).getValue().toString();
        System.out.println("\nselected start node name: " + dropdownSelected);
        startNodeID = resolveID(dropdownSelected);
        System.out.println("node ID resolved to: " + startNodeID);

//        String longName = getLongName(dropdownSelected); //Given by database
//        startLocationList.setValue(longName);

    }

    /**
     * Gets selected item from {@link #endLocationList} dropdown.
     * todo, judging from comments, this will get full node info from DB? For now, I am using my own resolveID
     * @param event calling event info
     */
    @FXML
    void selectEndNode(ActionEvent event) {
        String dropdownSelected = ((JFXComboBox) event.getSource()).getValue().toString();
        System.out.println("\nselected end node name: " + dropdownSelected);
        endNodeID = resolveID(dropdownSelected);
        System.out.println("node ID resolved to: " + startNodeID);

//        String longName = getLongName(dropdownSelected); //Given by database
//        endLocationList.setValue(longName);
    }

    /**
     * todo
     * @param event calling fcn's (Find Path Button) event info
     */
    @FXML
    public void findPath(ActionEvent event) {

        //todo commented section here serves as good example for how shapes will be made
//        double xcoord = (double) 1748 / 10;
//        double ycoord = (double) 1336 / 10;
//
//        double xcoord2 = (double) 1910 / 10;
//        double ycoord2 = (double) 1465 / 10;
//
//        Circle circle = new Circle(xcoord, ycoord, 2, Color.RED);
//        Circle circle2 = new Circle(xcoord2, ycoord2, 2, Color.RED);
//        Line line = new Line(xcoord, ycoord, xcoord2, ycoord2);
//        line.setStroke(Color.RED);
//
//        pane.getChildren().addAll(circle, circle2, line);


        System.out.print("\nFINDING PATH...");

        //get selected nodes
        String startNode = startNodeID;
        String endNode = endNodeID;
        assert startNodeID != null && endNodeID != null; //todo, actual button validation

        //Execute A* Search
        System.out.print("A* Search with startNodeID of " + startNode + ", and endNodeID of " + endNodeID + "\n");
        Searcher aStar = new AStarSearch();
        Path foundPath = aStar.search(startNode, endNode); //todo error here - this is a question for Algo.

        //build path
        Iterator<edu.wpi.TeamE.algorithms.pathfinding.Node> nodeIterator = foundPath.iterator(); //create iterable list
        while(nodeIterator.hasNext()){ //loop through list
            //this iterator will return a Node object
            //which is just a container for all the node info like id, floor, building, etc
            Node node = nodeIterator.next();
            String id = node.get("id");
            String floor = node.get("floor");
            String building = node.get("building");
            String type = node.get("type");
            String longName = node.get("longName");
            String shortName = node.get("shortName");
            //coordinates are ints so they have to be stored separate
            int xCoord = node.getX();
            int yCoord = node.getY();

            //print info
            System.out.println("Node ID:" + id + "\nxCoord: " + xCoord + "\nyCoord:" + yCoord + "\n---");
            //todo, potentially add this data to table - need to get startNode & endNode first, so tests can be run
            //todo build Map Shapes of of given path
        }
    }

    /**
     * Method called by FXMLLoader when initialization is complete. Propagates initial fields in FXML.
     * Namely, adds FloorMap PNG, fills dropdowns with DB data
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert startLocationList != null : "fx:id=\"bathroomList\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert endLocationList != null : "fx:id=\"bathroomList\" was not injected: check your FXML file 'PathFinder.fxml'.";

        Image image = new Image("edu/wpi/TeamE/FirstFloorMap.png");
        imageView.setImage(image);


        //DB connection todo doesnt always work after Map Editor has muddled the DB?
        makeConnection connection = new makeConnection();

        //get All nodes
        ArrayList<Node> nodeArrayList = connection.getAllNodes();

        //add to Observable List
        System.out.println("Begin Adding to Dropdown List...");
        for (Node node : nodeArrayList) { //loop through list
            //this iterator will return a Node object
            //which is just a container for all the node info like id, floor, building, etc
            String id = node.get("id");
            String floor = node.get("floor");
            String building = node.get("building");
            String type = node.get("type");
            String longName = node.get("longName");
            String shortName = node.get("shortName");
            //coordinates are ints so they have to be stored separate
            int xCoord = node.getX();
            int yCoord = node.getY();

            //print info
            System.out.println("    Node ID:" + id + "\n    xCoord: " + xCoord + "\n    yCoord:" + yCoord + "\n  ---");

            //add to list
            list.add(longName);
            listOfId.add(id); //for ID lookups later todo maybe just use nodeArrayList?
        }
        System.out.println("...Finished Adding to Dropdown List");

        //add ObservableList to dropdowns
        startLocationList.setItems(list);
        endLocationList.setItems(list);

        System.out.println("PathFinder Init Finished.");
    }

    /**
     * Gets a node's ID from it's longName
     *
     * Implementation: Searches through "list" until longName is found,
     *  Then gets "listOfId"'s value at this index. Both lists must be initialised
     *  for proper functioning
     *
     * @param longName "longName" of node to be resolved
     * @return Node ID
     */
    private String resolveID(String longName) {
        assert list.size() == listOfId.size();
        for (int i = 0; i < list.size(); i++) {
            if (longName.equals(list.get(i))) {
                System.out.println("Correlating index is " + i);
                return listOfId.get(i); //found correlating index
            }
        }
        return null;
    }
}
