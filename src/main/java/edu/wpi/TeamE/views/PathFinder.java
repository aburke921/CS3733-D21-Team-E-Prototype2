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
import javafx.scene.Group;
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
     * @param event calling event info
     */
    @FXML
    void selectStartNode(ActionEvent event) {
        String dropdownSelected = ((JFXComboBox) event.getSource()).getValue().toString();
        System.out.println("\nselected start node name: " + dropdownSelected);
        startNodeID = resolveID(dropdownSelected);
        System.out.println("node ID resolved to: " + startNodeID);

        if (startLocationList.getSelectionModel().isEmpty() ||
                endLocationList.getSelectionModel().isEmpty()) {
            findPathButton.setDisable(true);
        } else {
            findPathButton.setDisable(false);
        }
    }

    /**
     * Gets selected item from {@link #endLocationList} dropdown.
     * @param event calling event info
     */
    @FXML
    void selectEndNode(ActionEvent event) {
        String dropdownSelected = ((JFXComboBox) event.getSource()).getValue().toString();
        System.out.println("\nselected end node name: " + dropdownSelected);
        endNodeID = resolveID(dropdownSelected);
        System.out.println("node ID resolved to: " + endNodeID);

        if (startLocationList.getSelectionModel().isEmpty() ||
                endLocationList.getSelectionModel().isEmpty()) {
            findPathButton.setDisable(true);
        } else {
            findPathButton.setDisable(false);
        }
    }

    /**
     * todo Uses {@link Searcher}'s search() function to find the best path,
     * given the two current start and end positions ({@link #startNodeID} and {@link #endNodeID}).
     * @param event calling function's (Find Path Button) event info
     */
    @FXML
    public void findPath(ActionEvent event) {

        System.out.print("\nFINDING PATH...");

        //get selected nodes
        String startNode = startNodeID;
        String endNode = endNodeID;
        assert startNodeID != null && endNodeID != null; //todo, actual button validation

        //Execute A* Search
        System.out.print("A* Search with startNodeID of " + startNode + ", and endNodeID of " + endNodeID + "\n");
        Searcher aStar = new AStarSearch();
        Path foundPath = aStar.search(startNode, endNode); //todo error here - this is a question for Algo.

        drawMap(foundPath);
    }

    /**
     * Draws map path given....
     * todo... would be called by findPath()?
     */
    public void drawMap(Path path) {

        System.out.print("\nCLEARING MAP...");

        pane.getChildren().clear();

        //build path
        Iterator<Node> nodeIterator = path.iterator(); //create iterable list

        Group g = new Group(); //create group to contain all the shapes before we add them to the scene

        //Use these variables to keep track of the coordinates of the previous node
        double prevXCoord = 0;
        double prevYCoord = 0;

        while(nodeIterator.hasNext()){ //loop through list
            //this iterator will return a Node object
            //which is just a container for all the node info like its coordinates
            Node node = nodeIterator.next();

            //Resize the coordinates to match the resized image
            double xCoord = (double) node.getX() / 10;
            double yCoord = (double) node.getY() / 10;

            if (node.get("id").equals(startNodeID)) { //if current node is the starting node
                prevXCoord = xCoord;
                prevYCoord = yCoord;

                //place a red dot on the location
                Circle circle = new Circle(xCoord, yCoord, 2, Color.RED);
                g.getChildren().add(circle);
            }
            else if (node.get("id").equals(endNodeID)) { //if current node is the ending node
                //place a red dot on the location
                Circle circle = new Circle(xCoord, yCoord, 2, Color.RED);
                //create a line between this node and the previous node
                Line line = new Line(prevXCoord, prevYCoord, xCoord, yCoord);
                line.setStroke(Color.RED);

                g.getChildren().addAll(circle, line);
            }
            else {
                //create a line between this node and the previous node
                Line line = new Line(prevXCoord, prevYCoord, xCoord, yCoord);
                line.setStroke(Color.RED);

                g.getChildren().add(line);

                //update the coordinates for the previous node
                prevXCoord = xCoord;
                prevYCoord = yCoord;
            }
            //print info
            System.out.println("xCoord: " + xCoord + "\nyCoord:" + yCoord + "\n---");
        }

        //add all objects to the scene
        pane.getChildren().add(g);
    }

    /**
     * Method called by FXMLLoader when initialization is complete. Propagates initial fields in FXML:
     * Namely, adds FloorMap PNG and fills dropdowns with DB data
     */
    @FXML
    void initialize() {
        assert startLocationList != null : "fx:id=\"startLocationList\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert endLocationList != null : "fx:id=\"endLocationList\" was not injected: check your FXML file 'PathFinder.fxml'.";

        Image image = new Image("edu/wpi/TeamE/FirstFloorMap.png");
        imageView.setImage(image);


        //DB connection todo set up w/ new fcn DB is making
        makeConnection connection = new makeConnection();

        //get All nodes
        ArrayList<Node> nodeArrayList = connection.getAllNodes();

        //add to Observable List
        System.out.println("Begin Adding to Dropdown List...");
        for (Node node : nodeArrayList) { //loop through list todo, remove unnecessary code once implementation is more final.
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
