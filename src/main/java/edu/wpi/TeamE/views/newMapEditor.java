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

import edu.wpi.TeamE.algorithms.Edge;
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.algorithms.Path;
import edu.wpi.TeamE.algorithms.pathfinding.*;
import edu.wpi.TeamE.databases.*;

import edu.wpi.TeamE.App;
import javafx.collections.FXCollections;
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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class newMapEditor {

    /*
     * FXML Values
     */


    @FXML // fx:id="zoomSlider"
    private Slider zoomSlider;

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

    @FXML // fx:id="directionsButton"
    private JFXButton directionsButton; // Value injected by FXMLLoader

    @FXML // fx:id="stackPane"
    private StackPane stackPane; // Value injected by FXMLLoader

    @FXML // fx:id="exit"
    private Polygon exit;

    @FXML // fx:id="lowerAnchorPane"
    private AnchorPane lowerAnchorPane; // Value injected by FXMLLoader

    @FXML
    private TreeTableView<Node> treeTable;
    @FXML
    private JFXTextField xCordInput;
    @FXML
    private JFXTextField yCordInput;
    @FXML
    private JFXTextField idInput;
    @FXML
    private JFXComboBox floorInput;
    @FXML
    private JFXComboBox typeInput;
    @FXML
    private JFXComboBox buildingInput;
    //    @FXML private JFXComboBox idDropDown;
    @FXML
    private JFXTextField longNameInput;
    @FXML
    private JFXTextField shortNameInput;

    /*
     * Additional Variables
     */

    private String selectedStartNodeID; // selected starting value's ID

    private String selectedEndNodeID; // selected ending value's ID

    private String currentFloor = "1"; // set based on button presses

    private Path currentFoundPath; // the last found path todo null if no path has been found yet

    ArrayList<String> nodeIDArrayList;

    ArrayList<Node> nodeArrayList;

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
     *
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
     *
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
     *
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


    public void drawMap(String floorNum) {
        makeConnection connection = makeConnection.makeConnection();

        //clear map
        System.out.print("\nCLEARING MAP...");
        pane.getChildren().clear();
        System.out.println(" DONE");

        System.out.println("drawMap() is Finding path for floor " + floorNum);


        //if path is null
        if (NodeDB.getAllNodes() == null) {
            //todo snackbar to say error
            return;
        }
        Group g = new Group(); //create group to contain all the shapes before we add them to the scene

        ArrayList<Node> nodeArray = new ArrayList<Node>();
        nodeArray = NodeDB.getAllNodesByFloor(floorNum);
        ArrayList<Edge> edgeArray = new ArrayList<Edge>();
        edgeArray = EdgeDB.getAllEdges();

        //display all nodes
        scale = imageWidth / imageView.getFitWidth();
        for (int i = 0; i < nodeArray.size(); i++) {
            double xCoord = nodeArray.get(i).getX() / scale;
            double yCoord = nodeArray.get(i).getY() / scale;
            System.out.println(xCoord);
            Circle circle = new Circle(xCoord, yCoord, 2, Color.GREEN);
            System.out.println(circle.toString());
            g.getChildren().add(circle);

        }
        for(int i = 0; i < edgeArray.size(); i++) {
            //edgeArray.get(i).
            Line line = new Line();
            line.setStrokeLineCap(StrokeLineCap.ROUND);
            line.setStrokeWidth(strokeWidth);
            line.setStroke(Color.RED);

            //g.getChildren().addAll(circle, line);
        }
        System.out.println(g.getChildren().toString());
        pane.getChildren().add(g);
    }

        public void setCurrentFloor(String floorNum) {

            //set image
            currentFloor = floorNum;
            Image image = new Image("edu/wpi/TeamE/maps/" + floorNum + ".png");
            imageView.setImage(image);

            //draw path for new floor
            drawMap(currentFloor);

            System.out.println("Current floor set to " + floorNum);
        }

        /**
         * Method called by FXMLLoader when initialization is complete. Propagates initial fields in FXML:
         * Namely, adds FloorMap PNG and fills dropdowns with DB data, sets default floor.
         */
        @FXML
        void initialize() {

            System.out.println("Begin PathFinder Init");

            //get primaryStage
            Stage primaryStage = App.getPrimaryStage();

            //If exit button is clicked, exit app
            exit.setOnMouseClicked(event -> {
                App app = new App();
                app.stop();
            });

            //get dimensions of stage
            stageWidth = primaryStage.getWidth();
            stageHeight = primaryStage.getHeight();

            assert startLocationComboBox != null : "fx:id=\"startLocationComboBox\" was not injected: check your FXML file 'PathFinder.fxml'.";
            assert endLocationComboBox != null : "fx:id=\"endLocationComboBox\" was not injected: check your FXML file 'PathFinder.fxml'.";

            //DB connection
            makeConnection connection = makeConnection.makeConnection();

            //Get longNames & IDs
            System.out.print("Begin Adding to Dropdown List... ");
            //todo here

            longNameArrayList = FXCollections.observableArrayList();
            nodeIDArrayList = new ArrayList<String>();

            nodeArrayList = NodeDB.getAllNodes();
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

            drawMap(currentFloor);
        }


        public void nextFloor(ActionEvent event) {
            //set current floor to one after current
            setCurrentFloor(floorNames[currentFloorNamesIndex]);

            //increment unless at max, then back to 0
            if (currentFloorNamesIndex == 5) {
                currentFloorNamesIndex = 0;
            } else currentFloorNamesIndex++;
        }
    }


