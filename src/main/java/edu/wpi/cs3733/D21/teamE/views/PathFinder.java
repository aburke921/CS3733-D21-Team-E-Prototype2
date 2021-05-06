package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.QRCode;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.map.Path;
import edu.wpi.cs3733.D21.teamE.observer.ImageObserver;
import edu.wpi.cs3733.D21.teamE.observer.MarkerObserver;
import edu.wpi.cs3733.D21.teamE.observer.Subject;
import edu.wpi.cs3733.D21.teamE.pathfinding.SearchContext;
import edu.wpi.cs3733.D21.teamE.states.PathFinderState;
import javafx.animation.PathTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class PathFinder {

    private static int startNodeIndex = -1;
    private static int endNodeIndex = -1;

    private Node startNode = null;
    private Node endNode = null;

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
    @FXML private RequiredFieldValidator validator = new RequiredFieldValidator();

    @FXML // fx:id="floorL2"
    private Button floorL2;
    @FXML // fx:id="floorL1"
    private Button floorL1;
    @FXML // fx:id="floorG"
    private Button floorG;
    @FXML // fx:id="floor1"
    private Button floor1;
    @FXML // fx:id="floor2"
    private Button floor2;
    @FXML // fx:id="floor3"
    private Button floor3;
    private Button currentlySelected;

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

    private ArrayList<Node> currentMarkers = new ArrayList<>();

    /**
     * Switch to a different scene
     * @param event tells which button was pressed
     */
    @FXML
    private void switchScene(ActionEvent event) {
        PathFinderState pathFinderState = new PathFinderState();
        pathFinderState.switchScene(event);
    }

    /**
     * Gets the currently selected item from {@link #startLocationComboBox} dropdown.
     * @param event calling event info.
     */
    @FXML
    void selectStartNode(ActionEvent event) {
        // findPath button validation
        findPathButton.setDisable(startLocationComboBox.getSelectionModel().isEmpty() ||
                endLocationComboBox.getSelectionModel().isEmpty());
        // clear preset node
        startNode = null;
    }


    /**
     * Gets the currently selected item from {@link #endLocationComboBox} dropdown.
     * @param event calling event info.
     */
    @FXML
    void selectEndNode(ActionEvent event) {
        // findPath button validation
        findPathButton.setDisable(startLocationComboBox.getSelectionModel().isEmpty() ||
                endLocationComboBox.getSelectionModel().isEmpty());
        // clear preset node
        endNode = null;
    }

    /**
     * Get textual directions from {@link Path#makeDirections()}, and prints them out onto
     * a popup dialog.
     * Populates them with icons corresponding to each step in the directions
     * @param event the calling event's info
     */
    @FXML
    void showDirections(ActionEvent event) {
        //get directions
        if (currentFoundPath == null) return;

        List<String> directions = currentFoundPath.makeDirectionsWithDist();

        TableView tableView = new TableView();

        TableColumn<TextualDirectionStep, FontAwesomeIconView> column1 = new TableColumn<>();
        column1.setCellValueFactory(new PropertyValueFactory<>("icon"));
        column1.getStyleClass().add("iconTable");
        column1.setStyle("-fx-alignment: CENTER-LEFT");
        column1.setPrefWidth(40);
        column1.setMinWidth(40);
        column1.setMaxWidth(40);

        TableColumn<TextualDirectionStep, String> column2 = new TableColumn<>();
        column2.setCellValueFactory(new PropertyValueFactory<>("direction"));
        column2.setStyle("-fx-alignment: CENTER-LEFT");

        tableView.setSelectionModel(null);
        tableView.setPrefHeight(USE_COMPUTED_SIZE);
        tableView.getStyleClass().add("directions");
        tableView.getStyleClass().add("noheader");
        tableView.getStyleClass().add("table-row-cell");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);



        for (String dir : directions) {
            char step;
            Text text;
            int rotate = 0;
            step = MaterialDesignIcon.ARROW_UP_BOLD_CIRCLE_OUTLINE.getChar();
            if (dir.contains("straight")) {
                // no change, but needs to be here for elevator checks
            } else if (dir.contains("Stairs")) {
                step = MaterialDesignIcon.STAIRS.getChar();
            } else if (dir.contains("left")) {
                if (dir.contains("sharp")) {
                    rotate = -135;
                } else if (dir.contains("shallow")){
                    rotate = -45;
                } else if (dir.contains("Bend")){
                    rotate = -25;
                } else {
                    rotate = -90;
                }
            } else if (dir.contains("right")) {
                if (dir.contains("sharp")) {
                    rotate = 135;
                } else if (dir.contains("shallow")){
                    rotate = 45;
                } else if (dir.contains("Bend")){
                    rotate = 25;
                } else {
                    rotate = 90;
                }
            } else { // else is elevator
                step = MaterialDesignIcon.ELEVATOR.getChar();
            }
            text = new Text(Character.toString(step));
            text.setRotate(rotate);
            text.setStyle("-fx-fill: -fx--primary-dark");
            tableView.getItems().add(new TextualDirectionStep(text, dir));
        }


        JFXDialogLayout popup = new JFXDialogLayout();
        popup.setHeading(new Text("Detailed Path DirectionsController"));
        popup.setBody(tableView);
        popup.setPrefHeight(USE_COMPUTED_SIZE);
        JFXDialog dialog = new JFXDialog(stackPane, popup, JFXDialog.DialogTransition.CENTER);
        dialog.setMaxWidth(375);
        int fullSize = tableView.getItems().size() * 41 + 120;
        dialog.setMaxHeight(Math.min(fullSize, 425));
        JFXButton okay = new JFXButton("Done");
        okay.setOnAction(event1 -> dialog.close());
        popup.setActions(okay);
        dialog.show();
    }
    /**
     * finds path between a selected start and end location, or finds path to nearest bathroom from start location.
     * @param index index of the clicked on node
     */
    @FXML
    void clickOnNode(int index){
        JFXDialogLayout error = new JFXDialogLayout();
        error.setHeading(new Text("Location selection"));
        JFXDialog dialog = new JFXDialog(stackPane, error,JFXDialog.DialogTransition.CENTER);
        JFXButton parking = new JFXButton("Set Parking");
        if (App.userID == 0 || !nodeArrayList.get(index).get("type").equals("PARK")) {
            parking.setVisible(false);
        }
        JFXButton start = new JFXButton("Start");
        JFXButton destination = new JFXButton("Destination");
        JFXButton bathroom = new JFXButton("Nearest Bathroom");


        parking.setOnAction(event -> {
            if (DB.submitParkingSlot(nodeArrayList.get(index).get("id"), App.userID)) {
                dialog.close();
            } else {
                JFXDialogLayout error1 = new JFXDialogLayout();
                error1.setHeading(new Text("Error when trying to add parking slot"));
                JFXDialog dialog1 = new JFXDialog(stackPane, error1,JFXDialog.DialogTransition.CENTER);
                JFXButton dismiss = new JFXButton("Dismiss");
                dismiss.setOnAction(event1 -> dialog1.close());
                error1.setActions(dismiss);
                dialog1.show();
            }
        });
        start.setOnAction(event -> {
            startLocationComboBox.getSelectionModel().select(index);
            // clear preset node
            startNode = null;
            dialog.close();

        });
        destination.setOnAction(event -> {
            endLocationComboBox.getSelectionModel().select(index);
            // clear preset node
            endNode = null;
            dialog.close();

        });
        bathroom.setOnAction(event -> {
            SearchContext search = new SearchContext();
            startLocationComboBox.getSelectionModel().select(index);
            Node bathroom1 = search.findNearest(nodeArrayList.get(index),"REST");
            int endIndex = 0;
            for(int i = 0; i < nodeArrayList.size();i++){
                if(nodeArrayList.get(i).get("id").equals(bathroom1.get("id"))){
                    endIndex = i;
                }

            }
            endLocationComboBox.getSelectionModel().select(endIndex);
            // clear preset nodes
            startNode = null;
            endNode = null;
            dialog.close();


        });
        error.setActions(parking,start,bathroom,destination);




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
     * Uses {@link SearchContext}'s search() function to find the best path,
     * given the two current start and end positions ({@link #selectedStartNodeID} and {@link #selectedEndNodeID}).
     * Then calls {@link #drawMap(Path, String)}.
     * Sets {@link #currentFoundPath}. Returns a SnackBar when path is null.
     * @param event calling function's (Find Path Button) event info.
     */
    @FXML
    public void findPath(ActionEvent event) {

        System.out.println("\nFINDING PATH...");

        if (startNode != null) { // if not null, there is a preset start
            selectedStartNodeID = startNode.get("id");
        } else {
            //get index and ID of selected item in dropdown
            startLocationComboBox.setItems(longNameArrayList);
            int startLocationListSelectedIndex = startLocationComboBox.getSelectionModel().getSelectedIndex();
            selectedStartNodeID = nodeIDArrayList.get(startLocationListSelectedIndex);
            System.out.println("New ID resolution: (index) " + startLocationListSelectedIndex + ", (ID) " + selectedStartNodeID);
        }

        if (endNode != null) { // if not null, there is a preset end
            selectedEndNodeID = endNode.get("id");
        } else {
            //get index of selected item in dropdown
            endLocationComboBox.setItems(longNameArrayList);
            int endLocationListSelectedIndex = endLocationComboBox.getSelectionModel().getSelectedIndex();
            selectedEndNodeID = nodeIDArrayList.get(endLocationListSelectedIndex);
            System.out.println("New ID resolution: (index) " + endLocationListSelectedIndex + ", (ID) " + selectedEndNodeID);
        }

        //Define Search
        SearchContext search = new SearchContext();
        String algoType;
        switch (App.getSearchAlgo()) {
            case 1:
                algoType = "DFS";
                break;
            case 2:
                algoType = "BFS";
                break;
            case 3:
                algoType = "Dijkstra";
                break;
            case 4:
                algoType = "Best";
                break;
            default:
                algoType = "A*";
                break;
        }
        search.setAlgo(algoType);
        System.out.println(algoType + " Search with startNodeID of " + selectedStartNodeID + ", and endNodeID of " + selectedEndNodeID + "\n");


        //set constrains
        if (handicap.isSelected()) {
            System.out.println("Yay Handicap");
            search.addConstraint("HANDICAP");
        }
        if (!safe.isSelected()) {
            System.out.println("Yay Safe =)");
            search.addConstraint("SAFE");
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
            Path foundPath = search.search(selectedStartNodeID, selectedEndNodeID);

            //draw map, unless path is null
            if (foundPath == null) { //path is null

                //remove drawn line
                pane.getChildren().clear();

                //SnackBar popup
                JFXSnackbar bar = new JFXSnackbar(lowerAnchorPane);
                bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Sorry, something has gone wrong. Please try again.")));

            } else { //path is not null
                /*
                currentFoundPath = null;

                minETA.setText(Integer.toString(foundPath.getETA().getMin()));
                secETA.setText(String.format("%02d", (foundPath.getETA().getSec())));
                int len = (int) Math.round(foundPath.getPathLengthFeet());
                dist.setText(Integer.toString(len) + " Feet");

                //save found path for when floors are switched
                currentFoundPath = foundPath;

                // Set map image to starting floor
                String startFloor = foundPath.getStart().get("floor");
                currentFloor = startFloor;
                currFloor.setText("");

                currFloor.setText(currentFloor);
                 */

                currentFoundPath = null;
                // Set map image to starting floor
                String startFloor = foundPath.getStart().get("floor");
                setCurrentFloor(startFloor);

                minETA.setText(Integer.toString(foundPath.getETA().getMin()));
                secETA.setText(String.format("%02d", (foundPath.getETA().getSec())));
                int len = (int) Math.round(foundPath.getPathLengthFeet());
                dist.setText(len + " Feet");

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

                double distance = 0;

                ObservableList<Double> coordsList = FXCollections.observableArrayList();

                int firstNode = 1;
                String firstID = null;
                while (legItr.hasNext()) { //loop through list
                    //this iterator will return a Node object
                    //which is just a container for all the node info like its coordinates
                    Node node = legItr.next();

                    //Resize the coordinates to match the resized image
                    double xCoord = (double) node.getX() / scale;
                    double yCoord = (double) node.getY() / scale;

                    coordsList.add(xCoord);
                    coordsList.add(yCoord);

                    if(prevXCoord >= 1 && prevYCoord >= 1) {
                        distance += Math.hypot(xCoord - prevXCoord, yCoord - prevYCoord);
                    }

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

                        Label floorLabel = null;
                        FlowPane flowPane = new FlowPane();
                        String destFloor = "";

                        //if the current node is a stair or an elevator, add a label
                        if (node.get("type").equalsIgnoreCase("STAI") || node.get("type").equalsIgnoreCase("ELEV")) {

                            //iterate through the path
                            Iterator<Node> fullItr = fullPath.iterator();
                            while(fullItr.hasNext()) {

                                Node nodeCopy = fullItr.next();

                                if(node.equals(nodeCopy) && fullItr.hasNext()) {

                                    Node nextNode = fullItr.next();

                                    if(nextNode.get("type").equalsIgnoreCase("STAI") || nextNode.get("type").equalsIgnoreCase("ELEV")) {
                                        //create string for label
                                        String toFloor = "Go to Floor " + nextNode.get("floor");
                                        destFloor = nextNode.get("floor");

                                        //add string to label
                                        floorLabel = new Label(toFloor);

                                        //if current node is on a greater floor than the next
                                        if (Node.calculateZ(node.get("floor")) > Node.calculateZ(nextNode.get("floor"))) {
                                            //add down icon
                                            FontAwesomeIconView iconDown = new FontAwesomeIconView(FontAwesomeIcon.ARROW_CIRCLE_ALT_DOWN);
                                            iconDown.setSize("15");
                                            floorLabel.setGraphic(iconDown);
                                        } else { //current node is on a lower floor than next node
                                            //add up icon
                                            FontAwesomeIconView iconUP = new FontAwesomeIconView(FontAwesomeIcon.ARROW_CIRCLE_ALT_UP);
                                            iconUP.setSize("15");
                                            floorLabel.setGraphic(iconUP);
                                        }

                                        //put the label inside the flowPane
                                        flowPane.getChildren().add(floorLabel);

                                        //position the flowPane next to the node
                                        double xCoordLabel = (nextNode.getX() / scale) + 4;
                                        double yCoordLabel = (nextNode.getY() / scale) - 4;
                                        flowPane.setLayoutX(xCoordLabel);
                                        flowPane.setLayoutY(yCoordLabel);

                                        flowPane.getStyleClass().add("floor-change"); //add floor-change css so the child label disappears on hover
                                        flowPane.setPrefWrapLength(0); //shrink flowPane to be as small as child
                                    }

                                }
                            }
                        }

                    if(floorLabel != null) {
                        //if a floor label was made, line and node circle along with the label and its parent flowPane
                        String finalDestFloor = destFloor;

                        floorLabel.setOnMouseClicked(e -> setCurrentFloor(finalDestFloor));

                        g.getChildren().addAll(line, circle, flowPane);
                    } else {
                        //otherwise, only add the line and node circle
                        g.getChildren().addAll(line, circle);
                    }

                    //else, if current node is not this floors ending node, i.e., path continues
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

                //Add moving ball along path
                Circle ball = new Circle(5, Color.RED);
                g.getChildren().add(ball);

                Polyline polyline = new Polyline();
                polyline.getPoints().addAll(coordsList);

                PathTransition transition = new PathTransition();
                transition.setNode(ball);

                if(distance > 100){
                    double duration = distance / 150;
                    transition.setDuration(Duration.seconds(duration));
                } else {
                    transition.setDuration(Duration.seconds(1));
                }

                transition.setPath(polyline);
                transition.setCycleCount(PathTransition.INDEFINITE);
                transition.play();

                //add all objects to the scene
                pane.getChildren().addAll(g);

            } else {
                System.out.println("No path on this floor");
                //todo snackbar to say no nodes on this floor?
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
        currentFloor = floorNum;
        currFloor.setText("");
        currFloor.setText(currentFloor);

        switchFocusButton(floorNum);

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


        //init appBar
        javafx.scene.Node appBarComponent;
        try {
            App.setPageTitle("Path Finder"); //set AppBar title
            App.setHelpText("To use the pathfinder, first select a starting location and end location you would like " +
                    "to find the paths to.\n You may search to find what you are looking for as well. " +
                    "\n..."); //todo add help text for pathfinding
            App.setStackPane(stackPane);
            App.setShowHelp(true);
            App.setShowLogin(true);
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
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
        nodeIDArrayList = new ArrayList<>();

        nodeArrayList = DB.getAllNodes();
        for (int i = 0; i < nodeArrayList.size(); i++) {
            Node node = nodeArrayList.get(i);
            if (node.get("type").equalsIgnoreCase("HALL") || node.get("type").equalsIgnoreCase("WALK")) {
                nodeArrayList.remove(i--);
            } else {
                longNameArrayList.add(node.get("longName"));
                nodeIDArrayList.add(node.get("id"));
            }
        }
//        longNameArrayList = connection.getAllNodeLongNames();
//        nodeIDArrayList = connection.getListOfNodeIDS();

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
        Image image = new Image("edu/wpi/cs3733/D21/teamE/maps/1.png");
        currFloor.setText(currentFloor);
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        imageView.setImage(image);

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(primaryStage.getWidth());

        scale = imageWidth / imageView.getFitWidth();

        floor1.setStyle("-fx-background-color: -fx--primary");
        floor2.setStyle("-fx-background-color: -fx--primary-light");
        floor3.setStyle("-fx-background-color: -fx--primary-light");
        floorG.setStyle("-fx-background-color: -fx--primary-light");
        floorL1.setStyle("-fx-background-color: -fx--primary-light");
        floorL2.setStyle("-fx-background-color: -fx--primary-light");
        currentlySelected = floor1;

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

        //Check if startNodeIndex has a value, if yes fill startLocationComboBox
        if (startNodeIndex != -1) {
            startLocationComboBox.getSelectionModel().select(startNodeIndex);
            startNodeIndex = -1;
        }

        //Check if startNodeIndex has a value, if yes fill startLocationComboBox
        if (endNodeIndex != -1) {
            endLocationComboBox.getSelectionModel().select(endNodeIndex);
            endNodeIndex = -1;
        }

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

            for(int i = 0; i < nodeArrayList.size(); i++) {
                Node node = nodeArrayList.get(i);
                double nodeX = node.getX() / scale;
                int nodeXInt = (int) nodeX;
                double nodeY = node.getY() / scale;
                int nodeYInt = (int) nodeY;
                System.out.println(nodeXInt);
                if ((Math.abs(nodeXInt - xInt) <= 2 && Math.abs(nodeYInt - yInt) <= 2) && (node.get("floor").equalsIgnoreCase(currentFloor))) {

                    System.out.println(nodeArrayList.get(i).get("longName"));
                    clickOnNode(i);

                }
            }

                    /*if(selection == 1) {
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

        //Observer Design Pattern: update page based on floor change
        Subject subject = new Subject();

        new MarkerObserver(subject, markerPane, marker, currentMarkers);
        new ImageObserver(subject, imageView);
        //new PathObserver(subject, pane, currentFoundPath, scale, selectedStartNodeID, selectedEndNodeID);

        currFloor.textProperty().addListener(observable -> subject.setState(currFloor.getText()));

        startNode = App.getStartNode();
        if (startNode != null) {
            startLocationComboBox.setValue(startNode.get("longName"));
            App.setStartNode(null);
            // Reset so user doesn't get this again
        }

        endNode = App.getEndNode();
        if (endNode != null) {
            endLocationComboBox.setValue(endNode.get("longName"));
            App.setEndNode(null);
            // Reset so user doesn't get this again
        }

        if (App.isToEmergency()) {
            safe.setSelected(true);
            App.setToEmergency(false);
            // Reset so user doesn't get this again
        }
    }

    /**
     * Switches visible floor
     * @param e Button click action
     */
    public void chooseFloor(ActionEvent e) {
        //clear current floor of markers
        for (Node node : currentMarkers) {
            NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
            nM.getRectangle().setVisible(false);
        }
        Button button = ((Button) e.getSource());
        currentMarkers.clear();
        String floor = button.getText();
        currFloor.setText(floor);

        setCurrentFloor(floor);
        //drawMap(currentFoundPath, currentFloor);

        System.out.println("Current floor set to " + floor);
    }

    /**
     * Switch highlighted floor button
     * @param floor Floor to switch to
     */
    private void switchFocusButton(String floor) {
        currentlySelected.setStyle("-fx-background-color: -fx--primary-light");
        Button button = currentlySelected;
        switch (floor) {
            case "L2":
                button = floorL2;
                break;

            case "L1":
                button = floorL1;
                break;

            case "G":
                button = floorG;
                break;

            case "1":
                button = floor1;
                break;

            case "2":
                button = floor2;
                break;

            case "3":
                button = floor3;
                break;
        }
        currentlySelected = button;
        currentlySelected.setStyle("-fx-background-color: -fx--primary");
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
                //r.setFill(marker.getTypeColor().get(currentType));
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
        String result = QRCode.scanQR();
        String nodeID = result.substring(result.lastIndexOf('/') + 1, result.lastIndexOf('.'));
        System.out.println("Scanned nodeID: " + nodeID);
        startNode = DB.getNodeInfo(nodeID);
        startLocationComboBox.setValue(startNode.get("longName"));
    }
}
