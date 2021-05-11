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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class PathFinder {

    // Node setting fields

    private static int startNodeIndex = -1;
    private static int endNodeIndex = -1;

    private String startNodeID = null;
    private String endNodeID = null;

    // stuff for directory

    private final String[] typeNames = {"REST", "INFO", "DEPT", "LABS", "RETL", "SERV", "CONF", "EXIT", "ELEV", "STAI", "PARK"}; // array of types
    private HashMap<String, HashMap<String, String>> directory = new HashMap<>();

    private final HashMap<String, String> longTypeNames = new HashMap<String, String>(){{
        put("REST", "Restrooms");
        put("INFO", "Information Desks");
        put("DEPT", "Departments");
        put("LABS", "Laboratories");
        put("RETL", "Retail");
        put("SERV", "Services");
        put("CONF", "Conferences");
        put("EXIT", "Entrances/Exits");
        put("ELEV", "Elevators");
        put("STAI", "Stairs");
        put("PARK", "Parking");
    }};

    private JFXTreeView treeView;
    private StackPane directoryPane;

    @FXML //fx:id="directoryButton"
    private JFXButton directoryButton;

    private final HashMap<String, String> directoryColor = new HashMap<String, String>(){{
        put("Restrooms", "#b00404");
        put("Information Desks", "#dc721c");
        put("Departments", "#74058c");
        put("Laboratories", "#c900ae");
        put("Retail", "#3d4f9d");
        put("Services", "#005cff");
        put("Conferences", "#7f5124");
        put("Entrances/Exits", "#90e430");
        put("Elevators", "#769557");
        put("Stairs", "#007f52");
        put("Parking", "#1299d2");
    }};

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

    @FXML JFXCheckBox rest;
    @FXML JFXCheckBox info;
    @FXML JFXCheckBox dept;
    @FXML JFXCheckBox labs;
    @FXML JFXCheckBox retl;
    @FXML JFXCheckBox serv;
    @FXML JFXCheckBox conf;
    @FXML JFXCheckBox exit;
    @FXML JFXCheckBox elev;
    @FXML JFXCheckBox stai;
    @FXML JFXCheckBox park;
    @FXML JFXCheckBox all;

    private HashMap<String, JFXCheckBox> checkBoxes = new HashMap<>();

    @FXML // fx:id="reverse"
    private JFXButton reverse;


    @FXML // fx:id="minus"
    private JFXButton minus;
    @FXML // fx:id="plus"
    private JFXButton plus;

    /*
     * Additional Variables
     */

    private String selectedStartNodeID; // selected starting value's ID

    private String selectedEndNodeID; // selected ending value's ID

    private String currentFloor = "1"; // set based on button presses

    private Path currentFoundPath; // the last found path todo null if no path has been found yet

    private ArrayList<String> nodeIDArrayList;

    private ArrayList<Node> nodeArrayList;

    private ObservableList<String> longNameArrayList;

    private final String[] floorNames = {"L1", "L2", "G", "1", "2", "3"}; // list of floorNames

    private int currentFloorNamesIndex = 4; //start # should be init floor index + 1 (variable is actually always one beyond current floor)

    private double stageWidth;
    private double stageHeight;

    private double imageWidth;
    private double imageHeight;

    private ScrollPane scrollPane;
    Stage primaryStage;

    private int[] floorVisits = new int[]{0, 0, 0, 0, 0, 0}; // Number of times each floor has been visited, in order: L2, L1, G, 1, 2, 3
    private final HashMap<String, Integer> floorMap = new HashMap<String, Integer>(){{
        put("L2", 0);
        put("L1", 1);
        put("G", 2);
        put("1", 3);
        put("2", 4);
        put("3", 5);
    }};

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
        startNodeID = null;
        reverse.setDisable(startLocationComboBox.getSelectionModel().isEmpty() ||
                endLocationComboBox.getSelectionModel().isEmpty());
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
        endNodeID = null;
        reverse.setDisable(startLocationComboBox.getSelectionModel().isEmpty() ||
                endLocationComboBox.getSelectionModel().isEmpty());
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
        String floor = "Floor " + currentFoundPath.getStart().get("floor");

        TableView tableView = new TableView();

        TableColumn<TextualDirectionStep, Text> column1 = new TableColumn<>();
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

        boolean floorChangeFlag = true;

        String stop = directions.remove(directions.size() - 1);

        for (String dir : directions) {
            if (floorChangeFlag) {
                Text floorHeader = new Text(Character.toString(MaterialDesignIcon.PLAY_CIRCLE.getChar()));
                floorHeader.setStyle("-fx-fill: -fx--primary-dark");

                Text floorText = new Text(floor);
                floorText.setFont(Font.font(null, FontWeight.BOLD, 16));

                tableView.getItems().add(new TextualDirectionStep(floorHeader, floorText));

                floorChangeFlag = false;
            }
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
            tableView.getItems().add(new TextualDirectionStep(text, new Text("   "+ dir)));
            if (dir.contains("Floor")) {
                String s1 = dir.substring(dir.indexOf("Floor"));
                s1 = s1.replace("Floor", "");
                s1 = s1.replaceAll("\\s", "");
                floor = "Floor " + s1;
                floorChangeFlag = true;
            }
        }

        Text stopIcon = new Text(Character.toString(MaterialDesignIcon.ALERT_OCTAGON.getChar()));
        stopIcon.setStyle("-fx-fill: -fx--primary-dark");

        Text stopText = new Text(stop);
        stopText.setFont(Font.font(null, FontWeight.BOLD, 16));

        tableView.getItems().add(new TextualDirectionStep(stopIcon, stopText));

        JFXDialogLayout popup = new JFXDialogLayout();
        Label text = new Label("Path Directions");
        text.setFont(Font.font(null, FontWeight.BOLD, 17));
        popup.setHeading(text);
        popup.setBody(tableView);
        popup.setPrefHeight(USE_COMPUTED_SIZE);
        JFXDialog dialog = new JFXDialog(stackPane, popup, JFXDialog.DialogTransition.CENTER);
        dialog.setMaxWidth(400);
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
        Label text = new Label("Location Selection");
        text.setFont(Font.font(null, FontWeight.BOLD, 17));
        error.setHeading(text);
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
            startNodeID = null;
            dialog.close();

        });
        destination.setOnAction(event -> {
            endLocationComboBox.getSelectionModel().select(index);
            // clear preset node
            endNodeID = null;
            dialog.close();

        });
        bathroom.setOnAction(event -> {
            SearchContext search = new SearchContext();
            startLocationComboBox.getSelectionModel().select(index);
            endLocationComboBox.setValue("");
            Node bathroom1 = search.findNearest(nodeArrayList.get(index),"REST");
            if (bathroom1 == null){
                System.err.println("Nearest Bathroom: Path not found");
            }else {

                int endIndex = 0;
                for (int i = 0; i < nodeArrayList.size(); i++) {
                    if (nodeArrayList.get(i).get("id").equals(bathroom1.get("id"))) {
                        endIndex = i;
                    }

                }
                endLocationComboBox.getSelectionModel().select(endIndex);
            }
            // clear preset nodes
            startNodeID = null;
            endNodeID = null;
            dialog.close();
        });

        if (App.isLockEndPath()) { //if end path is locked don't allow user to set an end-path
            error.setActions(parking,start);
        } else {
            error.setActions(parking,start,bathroom,destination);
        }

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
        currentFoundPath = null; // empty path
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
        floorVisits = new int[]{0, 0, 0, 0, 0, 0};

        System.out.println("\nFINDING PATH...");

        if (directoryPane.isVisible()) {
            closeDirectory();
        }

        if (startNodeID != null) { // if not null, there is a preset start
            selectedStartNodeID = startNodeID;
        } else {
            //get index and ID of selected item in dropdown
            startLocationComboBox.setItems(longNameArrayList);
            int startLocationListSelectedIndex = startLocationComboBox.getSelectionModel().getSelectedIndex();
            selectedStartNodeID = nodeIDArrayList.get(startLocationListSelectedIndex);
            System.out.println("New ID resolution: (index) " + startLocationListSelectedIndex + ", (ID) " + selectedStartNodeID);
        }

        if (endNodeID != null) { // if not null, there is a preset end
            selectedEndNodeID = endNodeID;
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

        updateMarkers();

        //if path is null
        if (fullPath == null) {
            //todo snackbar to say no path set
            return;
        }

        int legNum = 0;

        List<Path> paths = fullPath.splitByFloor();
        for(Path path : paths){
            if(path.getStart().get("floor").equalsIgnoreCase(floorNum)){

                ArrayList<MapMarker> markerList = new ArrayList<>(); // List of markers to place at the end of the drawing of the path

                double markerIconXOffset = -(scale * 3);
                double markerIconYOffset = -(scale / 2);
                String mapMarkerSize = "25";

                Iterator<Node> legItr = path.iterator();
                Iterator<Node> legItrCopy = path.iterator();

                Group g = new Group(); //create group to contain all the shapes before we add them to the scene

                //Use these variables to keep track of the coordinates of the previous node
                double prevXCoord = 0;
                double prevYCoord = 0;

                double distance = 0;

                double dashlength = 10;
                double lineOffset = -20;

                ObservableList<Double> coordsList = FXCollections.observableArrayList();

                boolean firstNode = true;
                String firstID = null;

                //loop through list of nodes and add coordinates to Array List (this will be used to create the polyline)
                while (legItrCopy.hasNext()) {
                    Node node = legItrCopy.next();
                    //Resize the coordinates to match the resized image
                    double xCoord = (double) node.getX() / scale;
                    double yCoord = (double) node.getY() / scale;

                    coordsList.add(xCoord);
                    coordsList.add(yCoord);
                }

                Polyline polyline = new Polyline();
                polyline.getPoints().addAll(coordsList);
                polyline.setStroke(Color.web("#006db3"));
                polyline.setStrokeWidth(2);
                polyline.getStrokeDashArray().setAll(dashlength, dashlength);
                pane.getChildren().addAll(polyline);

                Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(polyline.strokeDashOffsetProperty(), 0)),
                        new KeyFrame(Duration.seconds(1), new KeyValue(polyline.strokeDashOffsetProperty(), lineOffset)));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();

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

                    if (!legItr.hasNext()) { //if current node is the ending node for this floor

                        MarkerType type;

                        if (firstNode) { //if current node is the first node on floor of path leg
                            firstNode = false;

                            if (node.get("id").equals(selectedStartNodeID)) { // start node of entire path
                                //place a dot on the location
                                type = MarkerType.START;
                            } else if (node.get("id").equals(selectedEndNodeID)) { // end node of entire path
                                //place a dot on the location
                                type = MarkerType.END;
                            } else { // end node of just this floor
                                //place a dot on the location
                                type = MarkerType.FIRST;
                            }

                        } else {
                            if (node.get("id").equals(selectedEndNodeID)) { // end node of entire path
                                //place a dot on the location
                                type = MarkerType.END;
                            } else { // end node of just this floor
                                //place a dot on the location
                                type = MarkerType.LAST;
                            }
                        }

                        markerList.add(new MapMarker((xCoord + markerIconXOffset), (yCoord + markerIconYOffset), mapMarkerSize, type));

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

                            floorLabel.setOnMouseClicked(e -> {
                                int num = floorMap.get(floorNum);
                                floorVisits[num] = floorVisits[num] + 1;
                                setCurrentFloor(finalDestFloor);
                            });

                            g.getChildren().addAll(flowPane);
                        }

                    } else if (firstNode) { //if current node is the starting node
                        firstNode = false;

                        MarkerType type;

                        if (node.get("id").equals(selectedStartNodeID)) { // start node of entire path
                            //place a dot on the location
                            type = MarkerType.START;
                        } else if (node.get("id").equals(selectedEndNodeID)) { // end node of entire path
                            //place a dot on the location
                            type = MarkerType.END;
                        } else { // end node of just this floor
                            //place a dot on the location
                            type = MarkerType.FIRST;
                        }

                        markerList.add(new MapMarker((xCoord + markerIconXOffset), (yCoord + markerIconYOffset), mapMarkerSize, type));

                        //update the coordinates for the previous node
                        prevXCoord = xCoord;
                        prevYCoord = yCoord;

                        //else, if current node is not this floors ending node, i.e., path continues
                    } else {
                        //update the coordinates for the previous node
                        prevXCoord = xCoord;
                        prevYCoord = yCoord;
                    }

                }

                for (MapMarker marker : markerList) {
                    g.getChildren().add(marker.makeMarker());
                }

                //add all objects to the scene
                pane.getChildren().addAll(g);

                ArrayList<Double> xCoords = new ArrayList<>();
                ArrayList<Double> yCoords = new ArrayList<>();

                int i = 0;
                for (Double coord : coordsList) {
                    if ((i % 2) == 0) {
                        xCoords.add(coord);
                    } else {
                        yCoords.add(coord);
                    }
                    i++;
                }

                // Set to opposites, for comparison
                double xMin = 5000/scale;
                double xMax = 0;

                for (Double coord : xCoords) {
                    if (coord < xMin) {
                        xMin = coord;
                    }
                    if (coord > xMax) {
                        xMax = coord;
                    }
                }

                // Set to opposites, for comparison
                double yMin = 3400/scale;
                double yMax = 0;

                for (Double coord : yCoords) {
                    if (coord < yMin) {
                        yMin = coord;
                    }
                    if (coord > yMax) {
                        yMax = coord;
                    }
                }

                // Descale, back to "original" coords
                if (legNum == floorVisits[floorMap.get(floorNum)]) {
                    zoomToPath(xMin * scale, xMax * scale, yMin * scale, yMax * scale);
                }
                legNum++;
            } else {
                System.out.println("No path on this floor");
                //todo snackbar to say no nodes on this floor?
            }
        }
    }

    /**
     * Zooms into the Path
     * @param xMin Min X Coordinate
     * @param xMax Max X Coordinate
     * @param yMin Min Y Coordinate
     * @param yMax Max Y Coordinate
     */
    private void zoomToPath(double xMin, double xMax, double yMin, double yMax) {
        double fullScaleX = 5000 * scrollPane.getWidth() / primaryStage.getWidth(); // max number of viewable pixels
        double fullScaleY = 3400 * scrollPane.getHeight() / primaryStage.getHeight();

        double xDist = xMax - xMin; // Distance between the points (sets zoom)
        double yDist = yMax - yMin;

        double fullStage = Math.max((xDist / fullScaleX), (yDist / fullScaleY)) + 0.175;
        double stageAmount = constrain(0.2, 1, fullStage); // Percentage of stage visible
        double zoomAmount = 1 / stageAmount;

        zoomSlider.setValue(zoomAmount);

        double xCenter = xMax - xDist / 2; // Center of points, sets position
        double yCenter = yMax - yDist / 2;

        double centerX = fullScaleX * stageAmount / 2; // Center of viewport
        double centerY = fullScaleY * stageAmount / 2;

        double xOffset = 5000 - fullScaleX * stageAmount; //how many pixels can the map be shifted by
        double yOffset = 3400 - fullScaleY * stageAmount;

        double x = (xCenter - centerX) / xOffset;
        double y = (yCenter - centerY) / yOffset;

        scrollPane.setHvalue(x);
        scrollPane.setVvalue(y);
    }

    /**
     * Constrains a value to within a minimum value and maximum value
     * @param min Minimum value
     * @param max Maximum value
     * @param val The value
     * @return The constrained value
     */
    private double constrain(double min, double max, double val) {
        if (val > max) {
            return max;
        } else if (val < min) {
            return min;
        } else {
            return val;
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
        switchFocusButton(floorNum);
        currentFloor = floorNum;
        currFloor.setText("");
        currFloor.setText(currentFloor);

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

        if(App.guestGoingToPathfinder) {
            App.newJFXDialogPopUp("","Okay","Pathfinding while not signed in is for remote view purpose only", stackPane);
            App.guestGoingToPathfinder = false;
        }


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
        primaryStage = App.getPrimaryStage();

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

        ArrayList<TreeItem> categories = new ArrayList<>();

        for (String type : typeNames) {
            TreeItem category = new TreeItem(longTypeNames.get(type));
            ArrayList<TreeItem> nodes = new ArrayList<>();
            for (Node node : DB.getAllNodesByType(type)) {
                TreeItem item = new TreeItem(node.get("longName"));
                nodes.add(item);
            }
            category.getChildren().addAll(nodes);
            categories.add(category);
        }

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

        currentlySelected = floor1;

        //Sidebar stuff
        minETA.setText("00");
        secETA.setText("00");

        StackPane stackPane = new StackPane(imageView, markerPane, borderPane);

        scrollPane = new ScrollPane(new Group(stackPane));
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

        ArrayList<TreeItem> directoryNodes = build();
        treeView = makeTreeView(directoryNodes);

        directoryPane = new StackPane(treeView);
        directoryPane.setMaxWidth(330);
        directoryPane.prefHeightProperty().bind(primaryStage.heightProperty().subtract(appBarAnchorPane.heightProperty()).subtract(lowerAnchorPane.heightProperty()));
        directoryPane.setVisible(false);

        StackPane outerPane = new StackPane(scrollPane, directoryPane);
        outerPane.setAlignment(Pos.CENTER_LEFT);

        rootBorderPane.setCenter(outerPane);
        rootBorderPane.setPrefWidth(stageWidth);
        rootBorderPane.setPrefHeight(stageHeight);

        //Set up location categories
        marker.populateLocationMarkerMarkerGroup(scale);
        markerPane.getChildren().add(marker.getMarkerGroup());

        System.out.println("Finish PathFinder Init.");

        //don't allow user to select end location if end location is locked
        if (App.isLockEndPath()) {
            //remove all selector
            rest.setManaged(false);
            info.setManaged(false);
            labs.setManaged(false);
            dept.setManaged(false);
            retl.setManaged(false);
            serv.setManaged(false);
            conf.setManaged(false);
            exit.setManaged(false);
            elev.setManaged(false);
            stai.setManaged(false);
            all.setManaged(false);


            //hide all selectors
            rest.setVisible(false);
            info.setVisible(false);
            labs.setVisible(false);
            dept.setVisible(false);
            retl.setVisible(false);
            serv.setVisible(false);
            conf.setVisible(false);
            exit.setVisible(false);
            elev.setVisible(false);
            stai.setVisible(false);
            all.setVisible(false);
        }

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
                //System.out.println(nodeXInt);
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

        Node startNode = App.getStartNode();
        if (startNode != null) {
            startLocationComboBox.setValue(startNode.get("longName"));
            startNodeID = startNode.get("id");
            App.setStartNode(null);
            // Reset so user doesn't get this again
        }

        Node endNode = App.getEndNode();
        if (endNode != null) {
            endLocationComboBox.setValue(endNode.get("longName"));
            endNodeID = endNode.get("id");
            App.setEndNode(null);
            // Reset so user doesn't get this again
        }

        if (App.isToEmergency()) {
            safe.setSelected(true);
            App.setToEmergency(false);
            // Reset so user doesn't get this again
        }

        //if combobox is restricted - dont allow access to changing it.
        endLocationComboBox.setDisable(App.isLockEndPath()); //lock path

        populateCheckboxes();

        // Disable zoom buttons under certain cases
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (zoomSlider.getValue() < 1.01) {
                    minus.setDisable(true);
                    plus.setDisable(false);
                } else if (zoomSlider.getValue() > 4.99) {
                    minus.setDisable(false);
                    plus.setDisable(true);
                } else {
                    minus.setDisable(false);
                    plus.setDisable(false);
                }
            }
        });
    }

    /**
     * Builds the Tree for the directory
     * @return Categorized and sorted directory
     */
    private ArrayList<TreeItem> build() {
        ArrayList<TreeItem> categories = new ArrayList<>();

        for (String type : typeNames) {
            String typeName = longTypeNames.get(type);
            TreeItem category = new TreeItem(typeName);
            ArrayList<TreeItem> nodes = new ArrayList<>();
            HashMap<String, String> nameToID = new HashMap<>();
            ArrayList<Node> nodeList = DB.getAllNodesByType(type);
            Collections.sort(nodeList);
            for (Node node : nodeList) {
                String longName = node.get("longName");
                nameToID.put(longName, node.get("id"));
                TreeItem item = new TreeItem(longName);
                nodes.add(item);
            }
            directory.put(typeName, nameToID);
            category.getChildren().addAll(nodes);
            categories.add(category);
        }

        return categories;
    }

    /**
     * Creates the TreeView
     * @param data The directory infor from build();
     * @return Directory TreeView
     */
    private JFXTreeView makeTreeView(ArrayList<TreeItem> data) {
        JFXTreeView view = new JFXTreeView();
        TreeItem root = new TreeItem("Locations");
        root.getChildren().addAll(data);
        view.setRoot(root);
        view.setShowRoot(false);

        view.setCellFactory(tree -> {
            TreeCell<String> cell = new TreeCell<String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        // Matching directory label color
                        if (directoryColor.containsKey(item)) {
                            String color = directoryColor.get(item);
                            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.SQUARE);
                            icon.setStyle("-fx-fill: " + color);
                            setGraphic(icon);
                            setGraphicTextGap(7);
                        } else {
                            setGraphic(null);
                        }
                    }
                }
            };
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty() && !App.isLockEndPath()) {
                    TreeItem<String> treeItem = cell.getTreeItem();
                    TreeItem<String> parent = treeItem.getParent();
                    if (!parent.equals(root)) { // reject categories, only allow nodes
                        String name = treeItem.getValue();
                        String type = parent.getValue();
                        String nodeID = directory.get(type).get(name);
                        selectNode(name, nodeID);
                    }

                }
            });
            return cell ;
        });

        return view;
    }

    private void selectNode(String name, String id){
        JFXDialogLayout popup = new JFXDialogLayout();
        Label text = new Label("Location Selection");
        text.setFont(Font.font(null, FontWeight.BOLD, 17));
        popup.setHeading(text);
        JFXDialog dialog = new JFXDialog(directoryPane, popup,JFXDialog.DialogTransition.CENTER);

        JFXButton start = new JFXButton("Start");
        JFXButton destination = new JFXButton("Destination");
        start.setOnAction(event -> {
            startNodeID = id;
            startLocationComboBox.setValue(name);
            dialog.close();

        });
        destination.setOnAction(event -> {
            endNodeID = id;
            endLocationComboBox.setValue(name);
            dialog.close();

        });
        popup.setActions(start,destination);

        dialog.setMaxHeight(20);
        dialog.setMaxWidth(250);

        dialog.show();
    }

    /**
     * Opens or closes the directory, based on what needs to happen
     */
    public void changeDirectory() {
        if (directoryPane.visibleProperty().get()) {
            closeDirectory();
        } else {
            openDirectory();
        }
    }

    /**
     * Closes directory
     */
    private void closeDirectory() {
        directoryPane.setVisible(false);
        startLocationComboBox.setDisable(false);
        endLocationComboBox.setDisable(false);
        directoryButton.getStyleClass().remove("directorySelected");
        directoryButton.getStyleClass().add("directory");
    }

    /**
     * Open directory
     */
    private void openDirectory() {
        directoryPane.setVisible(true);
        startLocationComboBox.setDisable(true);
        endLocationComboBox.setDisable(true);
        directoryButton.getStyleClass().remove("directory");
        directoryButton.getStyleClass().add("directorySelected");
    }

    /**
     * Switches visible floor
     * @param e Button click action
     */
    public void chooseFloor(ActionEvent e) {
        Button button = ((Button) e.getSource());
        String floor = button.getText();
        currFloor.setText(floor);

        setCurrentFloor(floor);
    }

    /**
     * Switch highlighted floor button
     * @param floor Floor to switch to
     */
    private void switchFocusButton(String floor) {
        currentlySelected.getStyleClass().remove("transit-button-selected");
        currentlySelected.getStyleClass().add("transit-button-unselected");
        switch (floor) {
            case "L2":
                currentlySelected = floorL2;
                break;

            case "L1":
                currentlySelected = floorL1;
                break;

            case "G":
                currentlySelected = floorG;
                break;

            case "1":
                currentlySelected = floor1;
                break;

            case "2":
                currentlySelected = floor2;
                break;

            case "3":
                currentlySelected = floor3;
                break;
        }
        currentlySelected.getStyleClass().remove("transit-button-unselected");
        currentlySelected.getStyleClass().add("transit-button-selected");
    }

    @FXML
    public void sortNodesByType(ActionEvent event) {
        String selectedType =((CheckBox) event.getSource()).getId().toUpperCase();

        if (((CheckBox) event.getSource()).isSelected()) {
            System.out.println(selectedType);

            if (!selectedType.equals("ALL")) {
                //create hashcode string for hashmap
                String typeAndFloorString = selectedType + currentFloor;
                //Get the nodes with the current floor and type
                ArrayList<Node> nodeList = marker.getTypeAndFloorNode().get(typeAndFloorString);

                marker.getSelectedCheckBox().replace(((CheckBox) event.getSource()).getId().toUpperCase(), 1);

                for (Node node : nodeList) {
                    NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
                    Rectangle r = nM.getRectangle();
                    r.setVisible(true);
                    currentMarkers.add(node);
                }
            } else {
                rest.setSelected(true);
                info.setSelected(true);
                dept.setSelected(true);
                labs.setSelected(true);
                retl.setSelected(true);
                serv.setSelected(true);
                conf.setSelected(true);
                exit.setSelected(true);
                elev.setSelected(true);
                stai.setSelected(true);
                park.setSelected(true);

                for(String type : typeNames) {
                    marker.getSelectedCheckBox().replace(type.toUpperCase(), 1);

                    String typeAndFloorString = type + currentFloor;
                    ArrayList<Node> nodeList = marker.getTypeAndFloorNode().get(typeAndFloorString);

                    for(Node node : nodeList) {
                        NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
                        Rectangle r = nM.getRectangle();
                        r.setVisible(true);
                        currentMarkers.add(DB.getNodeInfo(node.get("id")));
                    }
                }
            }
        } else {
            System.out.println(selectedType);

            if(!selectedType.equals("ALL")) {
                //create hashcode string for hashmap
                String typeAndFloorString = selectedType + currentFloor;
                //Get the nodes with the current floor and type
                ArrayList<Node> nodeList = marker.getTypeAndFloorNode().get(typeAndFloorString);

                marker.getSelectedCheckBox().replace(((CheckBox) event.getSource()).getId().toUpperCase(), 0);

                for (Node node : nodeList) {
                    NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
                    nM.getRectangle().setVisible(false);
                    currentMarkers.remove(node);
                }
            } else {
                rest.setSelected(false);
                info.setSelected(false);
                dept.setSelected(false);
                labs.setSelected(false);
                retl.setSelected(false);
                serv.setSelected(false);
                conf.setSelected(false);
                exit.setSelected(false);
                elev.setSelected(false);
                stai.setSelected(false);
                park.setSelected(false);

                for(String type : typeNames) {
                    marker.getSelectedCheckBox().replace(type.toUpperCase(), 0);

                    String typeAndFloorString = type + currentFloor;
                    ArrayList<Node> nodeList = marker.getTypeAndFloorNode().get(typeAndFloorString);

                    for(Node node : nodeList) {
                        NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
                        nM.getRectangle().setVisible(false);
                        currentMarkers.remove(DB.getNodeInfo(node.get("id")));
                    }
                }
            }
        }

        if(allSelected()) {
            all.setSelected(true);
        } else {
            all.setSelected(false);
        }
    }

    public boolean allSelected() {
        boolean allSelected = true;
        for (String type : typeNames) {
            if (marker.getSelectedCheckBox().get(type) == 0) {
                allSelected = false;
            }
        }
        return allSelected;
    }

    public void startQRScanning(ActionEvent event) {
        String result = QRCode.scanQR();
        String nodeID = result.substring(result.lastIndexOf('/') + 1, result.lastIndexOf('.'));
        System.out.println("Scanned nodeID: " + nodeID);
        Node startNode = DB.getNodeInfo(nodeID);
        startNodeID = startNode.get("id");
        startLocationComboBox.setValue(startNode.get("longName"));
    }

    public void swap(ActionEvent actionEvent) {
        String tempName = startLocationComboBox.getValue();
        String tempID = startNodeID;
        startLocationComboBox.setValue(endLocationComboBox.getValue());
        startNodeID = endNodeID;
        endLocationComboBox.setValue(tempName);
        endNodeID = tempID;
    }

    /**
     * Updates the current floor with already selected markers
     */
    private void updateMarkers() {
        //clear current floor of markers
        for (Node node : currentMarkers) {
            NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
            nM.getRectangle().setVisible(false);
        }

        currentMarkers.clear();

        ArrayList<String> selectedTypes = new ArrayList<>();

        if (all.isSelected()) {
            selectedTypes.addAll(Arrays.asList(typeNames));
        } else {
            for (String type : typeNames) {
                if (checkBoxes.get(type).isSelected()) {
                    selectedTypes.add(type);
                }
            }
        }

        for (String type : selectedTypes) {
            String typeAndFloorString = type + currentFloor;
            //Get the nodes with the current floor and type
            ArrayList<Node> nodeList = marker.getTypeAndFloorNode().get(typeAndFloorString);
            for (Node node : nodeList) {
                NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
                Rectangle r = nM.getRectangle();
                r.setVisible(true);
                currentMarkers.add(node);
            }
        }
    }

    /**
     * Populates the checkboxes hashmap post FXML loading
     */
    private void populateCheckboxes() {
        checkBoxes.put("REST", rest);
        checkBoxes.put("INFO", info);
        checkBoxes.put("DEPT", dept);
        checkBoxes.put("LABS", labs);
        checkBoxes.put("RETL", retl);
        checkBoxes.put("SERV", serv);
        checkBoxes.put("CONF", conf);
        checkBoxes.put("EXIT", exit);
        checkBoxes.put("ELEV", elev);
        checkBoxes.put("STAI", stai);
        checkBoxes.put("PARK", park);
    }

    /**
     * Zoom Button handling
     */
    public void zoom(ActionEvent e) {
        Button button = ((Button) e.getSource());
        if (button.getId().equals("plus")) {
            zoomSlider.setValue(zoomSlider.getValue() + 0.5);
        } else {
            zoomSlider.setValue(zoomSlider.getValue() - 0.5);
        }
    }
}
