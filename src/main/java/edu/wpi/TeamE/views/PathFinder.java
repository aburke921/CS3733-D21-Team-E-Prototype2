package edu.wpi.TeamE.views;

import com.jfoenix.controls.*;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.FileNotFoundException;

import java.io.IOException;

import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.algorithms.Path;
import edu.wpi.TeamE.algorithms.pathfinding.*;

import edu.wpi.TeamE.App;
import edu.wpi.cs3733.D21.teamE.QRCode;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Group;
import javafx.scene.Parent;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;

import javafx.stage.Stage;



import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class PathFinder {

    /*
     * FXML Values
     */

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane; // Value injected by FXMLLoader

    @FXML // fix:id="findPathButton"
    public JFXButton findPathButton; // Value injected by FXMLLoader

    @FXML // fix:id="backButton"
    public Button backButton; // Value injected by FXMLLoader

    @FXML // fx:id="startLocationList"
    private JFXComboBox<String> startLocationComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="endLocationList"
    private JFXComboBox<String> endLocationComboBox; // Value injected by FXMLLoader
    @FXML
    private JFXToggleButton handicap;

    @FXML
    private JFXToggleButton safe;

    @FXML // fx:id="imageView"
    private ImageView imageView = new ImageView();

    @FXML // fx:id="pane"
    private Pane pane = new Pane();

    @FXML // fx:id="pane"
    private Pane markerPane = new Pane();

    @FXML // fx:id="scrollPane"
    private BorderPane rootBorderPane;

    @FXML // fx:id="zoomSlider"
    private Slider zoomSlider;

    @FXML // fx:id="directionsButton"
    private JFXButton directionsButton; // Value injected by FXMLLoader

    @FXML // fx:id="stackPane"
    private StackPane stackPane; // Value injected by FXMLLoader

//    @FXML // fx:id="exit"
//    private Polygon exit;

    @FXML // fx:id="lowerAnchorPane"
    private AnchorPane lowerAnchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="minETA"
    private Label minETA;
    @FXML // fx:id="secETA"
    private Label secETA;

    @FXML // fx:id="dist"
    private Label dist;

    @FXML // fx:id="currFloor"
    private Label currFloor;

    @FXML // fx:id="sideBar"
    private VBox sideBar;
    @FXML // fx:id="ETA"
    private HBox ETA;
    @FXML // fx:id="clearPath"
    private Button clearPath;

    /*
     * Additional Variables
     */

    private String selectedStartNodeID; // selected starting value's ID

    private String selectedEndNodeID; // selected ending value's ID

    private String currentFloor = "1"; // set based on button presses

    private Path currentFoundPath; // the last found path todo null if no path has been found yet

    private ArrayList<String> nodeIDArrayList;

    private ArrayList<Node> nodeArrayList;

    private final String[] floorNames = {"L1", "L2", "G", "1", "2", "3"}; //list of floorNames

    private int currentFloorNamesIndex = 4; //start # should be init floor index + 1 (variable is actually always one beyond current floor)

    private ObservableList<String> longNameArrayList;

    private double stageWidth;
    private double stageHeight;

    private double imageWidth;
    private double imageHeight;

    private double scale;

    private double radius = 6;
    private double strokeWidth = 3;
    private int selection = 0;

    private Marker marker = new Marker();

    private ArrayList<Node> currentMarkers = new ArrayList<Node>();

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
        if (currentFoundPath == null) return;

        List<String> directions = currentFoundPath.makeDirectionsWithDist();
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(directions);
        listView.setPrefHeight(USE_COMPUTED_SIZE);

        JFXDialogLayout error = new JFXDialogLayout();
        error.setHeading(new Text("Detailed Path Directions"));
        error.setBody(listView);
        error.setPrefHeight(USE_COMPUTED_SIZE);
        JFXDialog dialog = new JFXDialog(stackPane, error, JFXDialog.DialogTransition.CENTER);
        dialog.setMaxWidth(350);
        int fullSize = listView.getItems().size() * 35 + 120;
        if (fullSize > 425) {
            dialog.setMaxHeight(425);
        } else {
            dialog.setMaxHeight(fullSize);
        }
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
    @FXML
    void clickOnNode( String longName){
        JFXDialogLayout error = new JFXDialogLayout();
        error.setHeading(new Text("Location selection"));
        JFXDialog dialog = new JFXDialog(stackPane, error,JFXDialog.DialogTransition.CENTER);
        JFXButton start = new JFXButton("Start");
        JFXButton destination = new JFXButton("Destination");


       start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startLocationComboBox.setValue(longName);
                dialog.close();

            }
        });
        destination.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                endLocationComboBox.setValue(longName);
                dialog.close();

            }
        });
        error.setActions(start,destination);




        dialog.show();
    }


    /**
     * Clears the path and closes the sidebar
     *
     * @param event the calling event's info
     */
    @FXML
    void pathClear(ActionEvent event) {

        //clear map
        System.out.print("\nCLEARING MAP...");
        pane.getChildren().clear();
        minETA.setText("00");
        secETA.setText("00");
        dist.setText("");
        System.out.println(" DONE");
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

        //Define A* Search
        System.out.println("A* Search with startNodeID of " + selectedStartNodeID + ", and endNodeID of " + selectedEndNodeID + "\n");
        SearchContext aStar = new SearchContext();

        //set constrains
        if (handicap.isSelected()) {
            System.out.println("Yay Handicap");
            aStar.addConstraint("HANDICAP");
        }
        if (safe.isSelected()) {
            System.out.println("Yay Safe =)");
            aStar.addConstraint("SAFE");
        }

        //Check if starting and ending node are the same
        if (selectedStartNodeID.equals(selectedEndNodeID)) { //error
            //Print error message and don't allow the program to call the path search function
            System.out.println("Cannot choose the same starting and ending location. Try again");
            //SnackBar popup
            JFXSnackbar bar = new JFXSnackbar(lowerAnchorPane);
            bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Cannot choose the same starting and ending location. Try again")));

            findPathButton.setDisable(true);
        } else { // run search
            //Call the path search function
            Path foundPath = aStar.search(selectedStartNodeID, selectedEndNodeID);

            //draw map, unless path is null
            if (foundPath == null) { //path is null

                //remove drawn line
                pane.getChildren().clear();

                //SnackBar popup
                JFXSnackbar bar = new JFXSnackbar(lowerAnchorPane);
                bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Sorry, something has gone wrong. Please try again.")));

            } else { //path is not null

                currentFoundPath = null;
                // Set map image to starting floor
                String startFloor = foundPath.getStart().get("floor");
                setCurrentFloor(startFloor);

                minETA.setText(Integer.toString(foundPath.getETA().getMin()));
                secETA.setText(String.format("%02d", (foundPath.getETA().getSec())));
                int len = (int) Math.round(foundPath.getPathLengthFeet());
                dist.setText(Integer.toString(len) + " Feet");

                //save found path for when floors are switched
                currentFoundPath = foundPath;

                //draw the map for the current floor
                drawMap(foundPath, currentFloor);

                System.out.println();
                List<String> directions = foundPath.makeDirections();
                for (String dir : directions) {
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
     * @param fullPath the path to be drawn on the map.
     */
    public void drawMap(Path fullPath, String floorNum) {

        //clear map
        System.out.print("\nCLEARING MAP...");
        pane.getChildren().clear();
        System.out.println(" DONE");

        System.out.println("drawMap() is Finding path for floor " + floorNum);


        //if path is null
        if (fullPath == null) {
            //todo snackbar to say no path set
            return;
        }

        List<Path> paths = fullPath.splitByFloor();
        for(Path path : paths){
            if(path.getStart().get("floor").equalsIgnoreCase(floorNum)){

                Iterator<Node> legItr = path.iterator();
                Group g = new Group(); //create group to contain all the shapes before we add them to the scene

                //Use these variables to keep track of the coordinates of the previous node
                double prevXCoord = 0;
                double prevYCoord = 0;

                int firstNode = 1;
                String firstID = null;
                while (legItr.hasNext()) { //loop through list
                    //this iterator will return a Node object
                    //which is just a container for all the node info like its coordinates
                    Node node = legItr.next();

                    //Resize the coordinates to match the resized image
                    double xCoord = (double) node.getX() / scale;
                    double yCoord = (double) node.getY() / scale;

                    if (firstNode == 1) { //if current node is the starting node
                        if (!(prevYCoord < 1) || !(prevXCoord < 1)) {
                            //technically second node, here to prevent circle from being "under" path line, prev will be fist node
                            firstNode = 0;
                            Circle circle;
                            if (firstID.equalsIgnoreCase(selectedStartNodeID)) {
                                // True first node
                                circle = new Circle(prevXCoord, prevYCoord, radius, Color.GREEN);
                            } else {
                                // First on floor
                                circle = new Circle(prevXCoord, prevYCoord, radius, Color.RED);
                            }


                            //create a line between this node and the previous node
                            Line line = new Line(prevXCoord, prevYCoord, xCoord, yCoord);
                            line.setStrokeLineCap(StrokeLineCap.ROUND);
                            line.setStrokeWidth(strokeWidth);
                            line.setStroke(Color.RED);

                            g.getChildren().addAll(line, circle);
                        } else {
                            //Track true first node's ID, for node color issue
                            firstID = node.get("id");
                        }
                        if (!legItr.hasNext()) { //if current node is the ending node for this floor, e.g. last node is also first node on floor

                            Circle circle;

                            if (node.get("id").equals(selectedStartNodeID)) { // start node of entire path
                                //place a dot on the location
                                circle = new Circle(xCoord, yCoord, radius, Color.GREEN);
                            } else if (node.get("id").equals(selectedEndNodeID)) { // end node of entire path
                                //place a dot on the location
                                circle = new Circle(xCoord, yCoord, radius, Color.BLACK);
                            } else { // end node of just this floor
                                //place a dot on the location
                                circle = new Circle(xCoord, yCoord, radius, Color.RED);
                            }

                            g.getChildren().addAll(circle);
                        }
                        //update the coordinates for the previous node
                        prevXCoord = xCoord;
                        prevYCoord = yCoord;


                    } else if (!legItr.hasNext()) { //if current node is the ending node for this floor

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

                        g.getChildren().addAll(line, circle);
                    } else {
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
            } else {
                System.out.println("No path on this floor");
                //todo snackback to say no nodes on this floor?
            }
        }
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
            } else System.out.println(".....NOT node: " + node.get("longName"));
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
        currFloor.setText(currentFloor);


        //Get a list of types that are currently selected
        ArrayList<String> currentlyViewableTypes = new ArrayList<String>();
        for (String key : marker.getSelectedCheckBox().keySet()) {
            if (marker.getSelectedCheckBox().get(key) == 1) {
                currentlyViewableTypes.add(key);
            }
        }

        ArrayList<Node> currentlyViewableNodes = new ArrayList<Node>();

        //Iterate through all the types that are currently selected
        for (String currViewType : currentlyViewableTypes) {
            String typeAndFloorString = currViewType + currentFloor;
            //Get the nodes with the current floor and type
            currentlyViewableNodes = marker.getTypeAndFloorNode().get(typeAndFloorString);

            //For every node, set it to visible
            for (Node node : currentlyViewableNodes) {
                NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
                Rectangle r = nM.getRectangle();
                r.setVisible(true);
                r.setFill(marker.getTypeColor().get(currViewType));
                currentMarkers.add(node);
            }
        }

        //draw path for new floor
        drawMap(currentFoundPath,currentFloor);

        System.out.println("Current floor set to " + floorNum);
    }

    /**
     * Method called by FXMLLoader when initialization is complete. Propagates initial fields in FXML:
     * Namely, adds FloorMap PNG and fills dropdowns with DB data, sets default floor.
     */
    @FXML
    void initialize() {

        System.out.println("Begin PathFinder Page Init");

        //todo remove in future iterations when default app size is changed
        if (App.getPrimaryStage().getHeight() <= 775) { //if height less than minimum for seeing all contents,
            App.getPrimaryStage().setHeight(775); //note, does not stop user from shrinking
        }

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setPageTitle("Path Finder"); //set AppBar title
            App.setHelpText("To use the pathfinder, first select a starting location and end location you would like " +
                    "to find the paths to.\n You may search to find what you are looking for as well. " +
                    "\n..."); //todo add help text for pathfinding
            App.setStackPane(stackPane);
            App.setShowHelp(true);
            App.setShowLogin(true);
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's sideBarVBox element
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get primaryStage
        Stage primaryStage = App.getPrimaryStage();

        //get dimensions of stage
        stageWidth = primaryStage.getWidth();
        stageHeight = primaryStage.getHeight();

        assert startLocationComboBox != null : "fx:id=\"startLocationComboBox\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert endLocationComboBox != null : "fx:id=\"endLocationComboBox\" was not injected: check your FXML file 'PathFinder.fxml'.";

        //DB connection


        //Get longNames & IDs
        System.out.print("Begin Adding to Dropdown List... ");
        //todo here

        longNameArrayList = FXCollections.observableArrayList();
        nodeIDArrayList = new ArrayList<String>();

        nodeArrayList = DB.getAllNodes();
        for (int i = 0; i < nodeArrayList.size(); i++) {
            longNameArrayList.add(nodeArrayList.get(i).get("longName"));
            nodeIDArrayList.add(nodeArrayList.get(i).get("id"));
        }
//        longNameArrayList = connection.getAllNodeLongNames();
//        nodeIDArrayList = connection.getListOfNodeIDS();

        //add ObservableLists to dropdowns
        startLocationComboBox.setItems(longNameArrayList);
        endLocationComboBox.setItems(longNameArrayList);

        System.out.println("done");


        new AutoCompleteComboBoxListener<>(startLocationComboBox);
        new AutoCompleteComboBoxListener<>(endLocationComboBox);

        final ArrayList<Node> array = DB.getAllNodes();

        //Set up zoomable and pannable panes
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);

        //set default/initial floor for map
        Image image = new Image("edu/wpi/TeamE/maps/1.png");
        currFloor.setText(currentFloor);
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        imageView.setImage(image);

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(primaryStage.getWidth());

        scale = imageWidth / imageView.getFitWidth();

        //Sidebar stuff
        minETA.setText("00");
        secETA.setText("00");

        StackPane stackPane = new StackPane(imageView, markerPane, borderPane);

        ScrollPane scrollPane = new ScrollPane(new Group(stackPane));
        //stackPane.prefWidthProperty().bind(primaryStage.widthProperty());
        //stackPane.prefHeightProperty().bind(primaryStage.heightProperty());

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

        //Set up location categories
        marker.populateLocationMarkerMarkerGroup(scale);
        markerPane.getChildren().add(marker.getMarkerGroup());

        System.out.println("Finish PathFinder Init.");
        pane.setOnMouseClicked(e -> {
            /*double xCoord = e.getX();
            double yCoord = e.getY();
            Circle circle = new Circle(xCoord, yCoord, 2, Color.GREEN);
            g.getChildren().add(circle);
             */
            scale = imageWidth / imageView.getFitWidth();
            System.out.println("click!");
            double X = e.getX();
            int xInt = (int)X;
            double Y = e.getY();
            int yInt = (int)Y;
            /*System.out.println(xInt);
            System.out.println(yInt);*/

//            for(int i = 0; i < array.size(); i++) {
//                double nodeX = array.get(i).getX() / scale;
//                int nodeXInt = (int)nodeX;
//                double nodeY = array.get(i).getY() / scale;
//                int nodeYInt = (int)nodeY;
//                System.out.println(nodeXInt);
//
//                if(Math.abs(nodeXInt - xInt) <= 2 && Math.abs(nodeYInt - yInt) <= 2){
//                    clickOnNode(array.get(i).get("longName"));
//                    //selection++;
//                    /*if(selection == 1) {
//                        startLocationComboBox.setValue(array.get(i).get("longName"));
//                    }else if(selection == 2){
//                        endLocationComboBox.setValue(array.get(i).get("longName"));
//                        selection = 0;
//                    }*/
//                    System.out.println(array.get(i).get("longName"));
//
//                }
//            }
        });
    }

    public void nextFloor(ActionEvent event) {
        //clear current floor of markers
        for (Node node : currentMarkers) {
            NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
            nM.getRectangle().setVisible(false);
        }
        currentMarkers.clear();
        //set current floor to one after current
        setCurrentFloor(floorNames[currentFloorNamesIndex]);

        //increment unless at max, then back to 0
        if (currentFloorNamesIndex == 5) {
            currentFloorNamesIndex = 0;
        } else currentFloorNamesIndex++;
        currFloor.setText(currentFloor);
    }

    public void sortNodesByType(ActionEvent event) {
        String currentType =((CheckBox) event.getSource()).getId().toUpperCase();
        //create hashcode string for hashmap
        String typeAndFloorString = currentType + currentFloor;
        //Get the nodes with the current floor and type
        ArrayList<Node> nodeList = marker.getTypeAndFloorNode().get(typeAndFloorString);

        if (((CheckBox) event.getSource()).isSelected()) {
            System.out.println(((CheckBox) event.getSource()).getId().toUpperCase());
            marker.getSelectedCheckBox().replace(((CheckBox) event.getSource()).getId().toUpperCase(), 1);

            for (Node node : nodeList) {
                NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
                Rectangle r = nM.getRectangle();
                r.setVisible(true);
                r.setFill(marker.getTypeColor().get(currentType));
                currentMarkers.add(node);
            }
        } else {
            System.out.println(((CheckBox) event.getSource()).getId().toUpperCase());
            marker.getSelectedCheckBox().replace(((CheckBox) event.getSource()).getId().toUpperCase(), 0);

            for (Node node : nodeList) {
                NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
                nM.getRectangle().setVisible(false);
                currentMarkers.remove(node);
            }
        }
    }

    public void startQRScanning(ActionEvent event) {

        QRCode qrCode = new QRCode();
        String nodeID = qrCode.scanQR();

        if(nodeID != null) {
            int index = 0;
            for(int i = 0; i < nodeArrayList.size();i++){
                if(nodeArrayList.get(i).get("id").equals(nodeID)){
                    index = i;
                }
            }
            startLocationComboBox.getSelectionModel().select(index);
        }

    }
}
