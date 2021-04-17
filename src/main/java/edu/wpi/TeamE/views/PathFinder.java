package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class PathFinder {

    /*
     * FXML Values
     */

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

    @FXML // fx:id="endLocationList"
    private JFXComboBox<String> endLocationList; // Value injected by FXMLLoader

    @FXML // fx:id="imageView"
    private ImageView imageView;

    @FXML // fx:id="pane"
    private Pane pane;

    @FXML // fx:id="floorChanger"
    private JFXButton floorChanger; // Value injected by FXMLLoader

    /*
     * Additional Variables
     */

    double scale = (double) 3.45528; //how much to scale the map by

    private String startNodeID; // selected starting value's ID

    private String endNodeID; // selected ending value's ID

    private String currentFloor = "1"; // set based on button presses

    private Path currentFoundPath; // the last found path todo null if no path has been found yet

    ArrayList<String> nodeIDArrayList;

    private final String[] floorNames = {"L1", "L2", "G", "1", "2", "3"}; //list of floorNames

    private int currentFloorNamesIndex = 4; //start # should be init floor index + 1 (variable is actually always one beyond current floor)

    /**
     * Returns to {@link edu.wpi.TeamE.views.Default} page.
     * @param event calling event info.
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
     * Gets the currently selected item from {@link #startLocationList} dropdown.
     * @param event calling event info.
     */
    @FXML
    void selectStartNode(ActionEvent event) {
        // findPath button validation
        if (startLocationList.getSelectionModel().isEmpty() ||
                endLocationList.getSelectionModel().isEmpty()) {
            findPathButton.setDisable(true);
        } else {
            findPathButton.setDisable(false);
        }
    }

    /**
     * Gets the currently selected item from {@link #endLocationList} dropdown.
     * @param event calling event info.
     */
    @FXML
    void selectEndNode(ActionEvent event) {
        // findPath button validation
        if (startLocationList.getSelectionModel().isEmpty() ||
                endLocationList.getSelectionModel().isEmpty()) {
            findPathButton.setDisable(true);
        } else {
            findPathButton.setDisable(false);
        }
    }

    /**
     * Uses {@link Searcher}'s search() function to find the best path,
     * given the two current start and end positions ({@link #startNodeID} and {@link #endNodeID}).
     * Then calls {@link #drawMap(Path, String)}.
     * Sets {@link #currentFoundPath}. Returns a SnackBar when path is null.
     * @param event calling function's (Find Path Button) event info.
     */
    @FXML
    public void findPath(ActionEvent event) {

        System.out.println("\nFINDING PATH...");

        //get index and ID of selected item in dropdown
        int startLocationListSelectedIndex = startLocationList.getSelectionModel().getSelectedIndex();
        startNodeID = nodeIDArrayList.get(startLocationListSelectedIndex);
        System.out.println("New ID resolution: (index) " + startLocationListSelectedIndex + ", (ID) " + startNodeID);

        //get index of selected item in dropdown
        int endLocationListSelectedIndex = endLocationList.getSelectionModel().getSelectedIndex();
        endNodeID = nodeIDArrayList.get(endLocationListSelectedIndex);
        System.out.println("New ID resolution: (index) " + endLocationListSelectedIndex + ", (ID) " + endNodeID);

        //Execute A* Search
        System.out.println("A* Search with startNodeID of " + startNodeID + ", and endNodeID of " + endNodeID + "\n");
        Searcher aStar = new AStarSearch();

        //Check if starting and ending node are the same
        if(startNodeID.equals(endNodeID)) { //error
            //Print error message and don't allow the program to call the path search function
            System.out.println("Cannot choose the same starting and ending location. Try again");
            //SnackBar popup
            JFXSnackbar bar = new JFXSnackbar(pane);
            bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Cannot choose the same starting and ending location. Try again")));

            findPathButton.setDisable(true);
        }
        else { // run search
            //Call the path search function
            Path foundPath = aStar.search(startNodeID, endNodeID);

            //draw map, unless path is null
            if (foundPath == null) { //path is null

                //remove drawn line
                pane.getChildren().clear();

                //SnackBar popup
                JFXSnackbar bar = new JFXSnackbar(pane);
                bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Sorry, something has gone wrong. Please try again.")));

            } else { //path is not null

                //save found path for when floors are switched
                currentFoundPath = foundPath;

                //draw the map for the current floor
                drawMap(foundPath, currentFloor);

                //todo also run getNodesOnFloorFromPath() for the other floors in advance?
            }
        }
    }

    /**
     * Draws map path given a complete {@link Path}.
     * RED - Start & End for floor only.
     * GREEN - start of entire path.
     * BLACK - end node of entire path.
     * @param path the path to be drawn on the map.
     */
    public void drawMap(Path path, String floorNum) {

        //clear map
        System.out.print("\nCLEARING MAP...");
        pane.getChildren().clear();
        System.out.println(" DONE");

        //parse out nodes that are not on specified floor.
        LinkedList<Node> finalNodeList = getNodesOnFloorFromPath(path, floorNum);

        //if there are no nodes on this floor
        if (finalNodeList == null) {
            //todo snackbar to say no nodes on this floor
            return;
        }

        //make iterator out of the parsed path
        Iterator<Node> nodeIteratorThisFloorOnly = finalNodeList.iterator();

        Group g = new Group(); //create group to contain all the shapes before we add them to the scene

        //Use these variables to keep track of the coordinates of the previous node
        double prevXCoord = 0;
        double prevYCoord = 0;

        int firstNode = 1;
        while(nodeIteratorThisFloorOnly.hasNext()){ //loop through list
            //this iterator will return a Node object
            //which is just a container for all the node info like its coordinates
            Node node = nodeIteratorThisFloorOnly.next();

            //Resize the coordinates to match the resized image
            double xCoord = (double) node.getX() / scale;
            double yCoord = (double) node.getY() / scale;

            if (firstNode == 1) { //if current node is the starting node
                firstNode = 0;
                prevXCoord = xCoord;
                prevYCoord = yCoord;

                if (node.get("id").equals(startNodeID)) { // start node of entire path

                    //place a dot on the location
                    Circle circle = new Circle(xCoord, yCoord, 5, Color.GREEN);
                    g.getChildren().add(circle);

                } else { // start node of just this floor

                    //place a red dot on the location
                    Circle circle = new Circle(xCoord, yCoord, 5, Color.RED);
                    g.getChildren().add(circle);
                }


            } else if (!nodeIteratorThisFloorOnly.hasNext()) { //if current node is the ending node for this floor

                Circle circle;

                if (node.get("id").equals(endNodeID)) { // end node of entire path
                    //place a dot on the location
                    circle = new Circle(xCoord, yCoord, 5, Color.BLACK);
                } else { // end node of just this floor
                    //place a dot on the location
                    circle = new Circle(xCoord, yCoord, 5, Color.RED);
                }

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
        }

        //add all objects to the scene
        pane.getChildren().add(g);
    }

    /**
     * Looks through path and returns only nodes on the specified floor
     * @param path {@link Path} to parse
     * @param floorNum floor name to look for
     * @return a linkedList of nodes on the floor
     */
    private LinkedList<Node> getNodesOnFloorFromPath(Path path, String floorNum) {

        System.out.println("Parsing node list...");

        //null check
        if (path == null) {
            System.out.println(".....NO NODES ON THIS FLOOR");
            return null;
        }

        //build path
        Iterator<Node> nodeIterator = path.iterator(); //create iterable list
        LinkedList<Node> finalNodeList = new LinkedList<>(); // list to be returned

        //for each node on path, add to finalNodeList if it is on floor floorNum
        while (nodeIterator.hasNext()) { //loop through path
            Node node = nodeIterator.next();
            if (node.get("floor").equals(floorNum)) { //if the node IS on the current floor
                //add node to list
                System.out.println(".....adding node: " + node.get("longName"));
                finalNodeList.add(node);
            } else System.out.println(".....NOT node: "  + node.get("longName"));
        }
        System.out.println("Done Parsing node list");
        return finalNodeList;
    }

    /**
     * Changes the displayed map, and path; sets {@link #currentFloor}.
     * @param floorNum floor to change to
     */
    public void setCurrentFloor(String floorNum) {

        //set image
        currentFloor = floorNum;
        Image image = new Image("edu/wpi/TeamE/maps/" + floorNum + ".png");
        imageView.setImage(image);

        //draw path
        drawMap(currentFoundPath,currentFloor);

        System.out.println("Current floor set to " + floorNum);
    }

    /**
     * Method called by FXMLLoader when initialization is complete. Propagates initial fields in FXML:
     * Namely, adds FloorMap PNG and fills dropdowns with DB data, sets default floor.
     */
    @FXML
    void initialize() {
        //todo remove when all sizes are same
        //set Stage size
        Stage primaryStage = App.getPrimaryStage();
        primaryStage.setWidth(1920);
        primaryStage.setHeight(1080);

        System.out.println("Begin PathFinder Init");

        assert startLocationList != null : "fx:id=\"startLocationList\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert endLocationList != null : "fx:id=\"endLocationList\" was not injected: check your FXML file 'PathFinder.fxml'.";

        //set default/initial floor
        setCurrentFloor("1");

        //DB connection
        makeConnection connection = makeConnection.makeConnection();

        //Get longNames & IDs
        System.out.print("Begin Adding to Dropdown List... ");
        ObservableList<String> longNameArrayList = connection.getAllNodeLongNames();
        nodeIDArrayList = connection.getListofNodeIDS();

        //add ObservableLists to dropdowns
        startLocationList.setItems(longNameArrayList);
        endLocationList.setItems(longNameArrayList);
        System.out.println("done");

        //new AutoCompleteComboBoxListener<>(startLocationList);
        //new AutoCompleteComboBoxListener<>(endLocationList);

        System.out.println("Finish PathFinder Init.");
    }


    public void nextFloor(ActionEvent event) {
        //set current floor to one after current
        setCurrentFloor(floorNames[currentFloorNamesIndex]);

        //increment unless at max, then back to 0
        if (currentFloorNamesIndex == 4) {
            currentFloorNamesIndex = 0;
        } else currentFloorNamesIndex++;
    }
}
