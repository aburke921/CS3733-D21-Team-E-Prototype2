package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
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

    @FXML // fx:id="endLocationList"
    private JFXComboBox<String> endLocationList; // Value injected by FXMLLoader

    @FXML // fx:id="imageView"
    private ImageView imageView;

    @FXML // fx:id="pane"
    private Pane pane;

    private String startNodeID; // selected starting value's ID

    private String endNodeID; // selected ending value's ID

    ObservableList<String> listOfId = FXCollections.observableArrayList(); //declaration

    ArrayList<String> nodeIDArrayList;

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
     * @param event calling function's (Find Path Button) event info.
     */
    @FXML
    public void findPath(ActionEvent event) {

        int startLocationListSelectedIndex = startLocationList.getSelectionModel().getSelectedIndex();
        startNodeID = nodeIDArrayList.get(startLocationListSelectedIndex);
        System.out.println("New ID resolution: (index) " + startLocationListSelectedIndex + ", (ID) " + startNodeID);

        int endLocationListSelectedIndex = endLocationList.getSelectionModel().getSelectedIndex();
        endNodeID = nodeIDArrayList.get(endLocationListSelectedIndex);
        System.out.println("New ID resolution: (index) " + endLocationListSelectedIndex + ", (ID) " + endNodeID);

        System.out.println("\nFINDING PATH...");

        //Check if starting and ending node are the same
        if(startNodeID.equals(endNodeID)) {
            System.out.println("Cannot choose the same starting and ending location. Try again");

            //remove drawn line
            pane.getChildren().clear();

            //SnackBar popup
            JFXSnackbar bar = new JFXSnackbar(pane);
            bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Cannot choose the same starting and ending location.\nTry again.")));
        }
        else {
            //Execute A* Search
            System.out.print("A* Search with startNodeID of " + startNodeID + ", and endNodeID of " + endNodeID + "\n");
            Searcher aStar = new AStarSearch();

            //Call the path search function
            Path foundPath = aStar.search(startNodeID, endNodeID);
            drawMap(foundPath);
        }
    }

    /**
     * Draws map path given a complete {@link Path}.
     * @param path the path to be drawn on the map.
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
//            System.out.println("xCoord: " + xCoord + "\nyCoord:" + yCoord + "\n---");
        }

        //add all objects to the scene
        pane.getChildren().add(g);
    }

    public void TEMPORARYCreateDB() { //todo
        makeConnection connection = makeConnection.makeConnection();
        System.out.println("STARTING UP!!!");
        try {
            connection.deleteAllTables();
            System.out.println("1.");
            connection.createTables();
            System.out.println("2.");
            File nodes = new File("src/main/resources/edu/wpi/TeamE/csv/bwEnodes.csv");
            File edges = new File("src/main/resources/edu/wpi/TeamE/csv/bwEedges.csv");
            connection.populateTable("node", nodes);
            connection.populateTable("hasEdge", edges);
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Nothing to delete");
        }
    }

    /**
     * Method called by FXMLLoader when initialization is complete. Propagates initial fields in FXML:
     * Namely, adds FloorMap PNG and fills dropdowns with DB data.
     */
    @FXML
    void initialize() {
        System.out.println("Begin PathFinder Init");

        assert startLocationList != null : "fx:id=\"startLocationList\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert endLocationList != null : "fx:id=\"endLocationList\" was not injected: check your FXML file 'PathFinder.fxml'.";

        //load image
        Image image = new Image("edu/wpi/TeamE/FirstFloorMap.png");
        imageView.setImage(image);

//        //todo, temp db connect
//        TEMPORARYCreateDB();

        //DB connection todo set up w/ new fcn DB is making?
        makeConnection connection = new makeConnection();

        //Get longNames & IDs
        System.out.print("Begin Adding to Dropdown List... ");
        ObservableList<String> longNameArrayList = connection.getAllNodeLongNamesByFloor("1");
        nodeIDArrayList = connection.getListOfNodeIDSByFloor("1");
        listOfId.addAll(nodeIDArrayList);

        //add ObservableLists to dropdowns
        startLocationList.setItems(longNameArrayList);
        endLocationList.setItems(longNameArrayList);
        System.out.println("done");


        System.out.println("Finish PathFinder Init.");
    }

}
