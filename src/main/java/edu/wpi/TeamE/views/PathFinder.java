package edu.wpi.TeamE.views;

import com.jfoenix.controls.*;

import java.awt.geom.RoundRectangle2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.algorithms.Path;
import edu.wpi.TeamE.algorithms.pathfinding.*;
import edu.wpi.TeamE.databases.*;

import edu.wpi.TeamE.App;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.stage.Screen;
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
    private JFXComboBox<String> startLocationComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="endLocationList"
    private JFXComboBox<String> endLocationComboBox; // Value injected by FXMLLoader

    //@FXML // fx:id="imageView"
    private ImageView imageView = new ImageView();

    @FXML // fx:id="pane"
    private Pane pane = new Pane();

    @FXML // fx:id="scrollPane"
    private BorderPane rootBorderPane;

    @FXML // fx:id="zoomSlider"
    private Slider zoomSlider;

    @FXML // fx:id="directionsButton"
    private JFXButton directionsButton; // Value injected by FXMLLoader

    @FXML // fx:id="stackPane"
    private StackPane stackPane; // Value injected by FXMLLoader

    /*
     * Additional Variables
     */

    private String selectedStartNodeID; // selected starting value's ID

    private String selectedEndNodeID; // selected ending value's ID

    private String currentFloor = "1"; // set based on button presses

    private Path currentFoundPath; // the last found path todo null if no path has been found yet

    ArrayList<String> nodeIDArrayList;

    private final String[] floorNames = {"L1", "L2", "G", "1", "2", "3"}; //list of floorNames

    private int currentFloorNamesIndex = 4; //start # should be init floor index + 1 (variable is actually always one beyond current floor)

    ObservableList<String> longNameArrayList;

    private double stageWidth;
    private double stageHeight;

    private double imageWidth;
    private double imageHeight;

    private double scale;

    private double radius = 6;
    private double strokeWidth = 3;

    /**
     * Returns to {@link edu.wpi.TeamE.views.Default} page.
     * @param event calling event info.
     */
    @FXML
    private void toDefault(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void getHelpDefault(ActionEvent event) {
        //todo, create help modal (refactor name, this was taken from Default page, hence the name)
    }

    /**
     * Gets the currently selected item from {@link #startLocationComboBox} dropdown.
     * @param event calling event info.
     */
    @FXML
    void selectStartNode(ActionEvent event) {
        // findPath button validation
        if (startLocationComboBox.getSelectionModel().isEmpty() ||
                endLocationComboBox.getSelectionModel().isEmpty()) {
            findPathButton.setDisable(true);
        } else {
            findPathButton.setDisable(false);
        }
    }

    /**
     * Gets the currently selected item from {@link #endLocationComboBox} dropdown.
     * @param event calling event info.
     */
    @FXML
    void selectEndNode(ActionEvent event) {
        // findPath button validation
        if (startLocationComboBox.getSelectionModel().isEmpty() ||
                endLocationComboBox.getSelectionModel().isEmpty()) {
            findPathButton.setDisable(true);
        } else {
            findPathButton.setDisable(false);
        }
    }

    /**
     * Get textual directions from {@link Path#makeDirections()}, and prints them out onto
     * a popup dialog.
     * @param event the calling event's info
     */
    @FXML
    void showDirections(ActionEvent event) {
        //get directions
        List<String> directions = currentFoundPath.makeDirections();
        StringBuilder directionsStringBuilder = new StringBuilder();
        for (String dir: directions) {
            System.out.println(dir);
            directionsStringBuilder.append(dir + ".\n"); //todo make scrollable
        }
        //make popup
        JFXDialogLayout error = new JFXDialogLayout();
        error.setHeading(new Text("Detailed Path Directions"));
        error.setBody(new Text(directionsStringBuilder.toString()));
        JFXDialog dialog = new JFXDialog(stackPane, error, JFXDialog.DialogTransition.CENTER);
        JFXButton okay = new JFXButton("Done");
        okay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();

            }
        });
        error.setActions(okay);
        dialog.show();
    }

    /**
     * Uses {@link Searcher}'s search() function to find the best path,
     * given the two current start and end positions ({@link #selectedStartNodeID} and {@link #selectedEndNodeID}).
     * Then calls {@link #drawMap(Path, String)}.
     * Sets {@link #currentFoundPath}. Returns a SnackBar when path is null.
     * @param event calling function's (Find Path Button) event info.
     */
    @FXML
    public void findPath(ActionEvent event) {

        System.out.println("\nFINDING PATH...");

        //get index and ID of selected item in dropdown
        startLocationComboBox.setItems(longNameArrayList);
        int startLocationListSelectedIndex = startLocationComboBox.getSelectionModel().getSelectedIndex();
        selectedStartNodeID = nodeIDArrayList.get(startLocationListSelectedIndex);
        System.out.println("New ID resolution: (index) " + startLocationListSelectedIndex + ", (ID) " + selectedStartNodeID);

        //get index of selected item in dropdown
        endLocationComboBox.setItems(longNameArrayList);
        int endLocationListSelectedIndex = endLocationComboBox.getSelectionModel().getSelectedIndex();
        selectedEndNodeID = nodeIDArrayList.get(endLocationListSelectedIndex);
        System.out.println("New ID resolution: (index) " + endLocationListSelectedIndex + ", (ID) " + selectedEndNodeID);

        //Execute A* Search
        System.out.println("A* Search with startNodeID of " + selectedStartNodeID + ", and endNodeID of " + selectedEndNodeID + "\n");
        SearchContext aStar = new SearchContext();
        //aStar.setConstraint("HANDICAP");

        //Check if starting and ending node are the same
        if(selectedStartNodeID.equals(selectedEndNodeID)) { //error
            //Print error message and don't allow the program to call the path search function
            System.out.println("Cannot choose the same starting and ending location. Try again");
            //SnackBar popup
            JFXSnackbar bar = new JFXSnackbar(pane);
            bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Cannot choose the same starting and ending location. Try again")));

            findPathButton.setDisable(true);
        }
        else { // run search
            //Call the path search function
            Path foundPath = aStar.search(selectedStartNodeID, selectedEndNodeID);

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

                //todo Display algo's directions
                System.out.println();
                List<String> directions = foundPath.makeDirections();
                for (String dir: directions) {
                    System.out.println(dir);
                }

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
            //todo snackbar to say no nodes on this floor?
            return;
        }

        //make iterator out of the parsed path
        Iterator<Node> nodeIteratorThisFloorOnly = finalNodeList.iterator();

        Group g = new Group(); //create group to contain all the shapes before we add them to the scene

        //Use these variables to keep track of the coordinates of the previous node
        double prevXCoord = 0;
        double prevYCoord = 0;
        scale = imageWidth / imageView.getFitWidth();

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

                if (node.get("id").equals(selectedStartNodeID)) { // start node of entire path

                    //place a dot on the location
                    Circle circle = new Circle(xCoord, yCoord, radius, Color.GREEN);
                    g.getChildren().add(circle);

                } else { // start node of just this floor

                    //place a red dot on the location
                    Circle circle = new Circle(xCoord, yCoord, radius, Color.RED);
                    g.getChildren().add(circle);
                }


            } else if (!nodeIteratorThisFloorOnly.hasNext()) { //if current node is the ending node for this floor

                Circle circle;

                if (node.get("id").equals(selectedEndNodeID)) { // end node of entire path
                    //place a dot on the location
                    circle = new Circle(xCoord, yCoord, radius, Color.BLACK);
                } else { // end node of just this floor
                    //place a dot on the location
                    circle = new Circle(xCoord, yCoord, radius, Color.RED);
                }

                //create a line between this node and the previous node
                Line line = new Line(prevXCoord, prevYCoord, xCoord, yCoord);
                line.setStrokeLineCap(StrokeLineCap.ROUND);
                line.setStrokeWidth(strokeWidth);
                line.setStroke(Color.RED);

                g.getChildren().addAll(circle, line);
            }
            else {
                //create a line between this node and the previous node
                Line line = new Line(prevXCoord, prevYCoord, xCoord, yCoord);
                line.setStrokeLineCap(StrokeLineCap.ROUND);
                line.setStrokeWidth(strokeWidth);
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

        stageWidth = primaryStage.getWidth();
        stageHeight = primaryStage.getHeight();

        System.out.println("Begin PathFinder Init");

        assert startLocationComboBox != null : "fx:id=\"startLocationComboBox\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert endLocationComboBox != null : "fx:id=\"endLocationComboBox\" was not injected: check your FXML file 'PathFinder.fxml'.";

        //DB connection
        makeConnection connection = makeConnection.makeConnection();

        //Get longNames & IDs
        System.out.print("Begin Adding to Dropdown List... ");
        longNameArrayList = connection.getAllNodeLongNames();
        nodeIDArrayList = connection.getListOfNodeIDS();

        //add ObservableLists to dropdowns
        startLocationComboBox.setItems(longNameArrayList);
        endLocationComboBox.setItems(longNameArrayList);
        System.out.println("done");

        new AutoCompleteComboBoxListener<>(startLocationComboBox);
        new AutoCompleteComboBoxListener<>(endLocationComboBox);

        //Set up zoomable and pannable panes
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);

        //set default/initial floor for map
        Image image = new Image("edu/wpi/TeamE/maps/1.png");
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        imageView.setImage(image);

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(primaryStage.getWidth());

        StackPane stackPane = new StackPane(imageView, borderPane);
        ScrollPane scrollPane = new ScrollPane(new Group(stackPane));

        //make scroll pane pannable
        scrollPane.setPannable(true);

        //get rid of side scroll bars
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //bind the zoom slider to the map
        stackPane.scaleXProperty().bind(zoomSlider.valueProperty());
        stackPane.scaleYProperty().bind(zoomSlider.valueProperty());

        rootBorderPane.setCenter(scrollPane);
        rootBorderPane.setPrefWidth(stageWidth);
        rootBorderPane.setPrefHeight(stageHeight);

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
