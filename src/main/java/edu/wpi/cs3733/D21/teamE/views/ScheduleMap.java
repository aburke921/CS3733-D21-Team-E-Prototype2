package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.Date;
import edu.wpi.cs3733.D21.teamE.map.Edge;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.map.Path;
import edu.wpi.cs3733.D21.teamE.pathfinding.SearchContext;
import edu.wpi.cs3733.D21.teamE.scheduler.Schedule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;

public class ScheduleMap {

    // fx:id="imageView"
    private ImageView imageView = new ImageView();
    @FXML // fx:id="pane"
    private Pane pane = new Pane();
    @FXML // fx:id="zoomSlider"
    private Slider zoomSlider;
    @FXML // fx:id="scrollPane"
    private BorderPane rootBorderPane;

    //private Schedule;

    private double stageWidth;
    private double stageHeight;

    private double imageWidth;
    private double imageHeight;

    private double scale;

    private String selectedStartNodeID; // selected starting value's ID

    private String selectedEndNodeID; // selected ending value's ID

    private String currentFloor = "1"; // set based on button presses

    private Path currentFoundPath; // the last found path

    private LocalDate currentDate = LocalDate.now();

    /**
     * Uses {@link SearchContext}'s search() function to find the best path,
     * given the two current start and end positions ({@link #selectedStartNodeID} and {@link #selectedEndNodeID}).
     * Then calls {@link #drawMap(Path, String)}.
     * Sets {@link #currentFoundPath}. Returns a SnackBar when path is null.
     * @param event calling function's (Find Path Button) event info.
     */
    /*@FXML
    public void findPath(ActionEvent event) {
        floorVisits = new int[]{0, 0, 0, 0, 0, 0};

        System.out.println("\nFINDING PATH...");

        if (startNode != null) { // if not null, there is a preset start
            selectedStartNodeID = startNode.get("id");
        }

        if (endNode != null) { // if not null, there is a preset end
            selectedEndNodeID = endNode.get("id");
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
    */

    /**
     * displays map of current floor
     * @param floorNum current floor number
     */
    public void drawMap(String floorNum) {

        //clear map
        System.out.print("\nCLEARING MAP...");
        pane.getChildren().clear();
        System.out.println(" DONE");

        //create group to contain all the shapes before we add them to the scene
        Group g = new Group();

        //retrieves nodes and edges from DB
        ArrayList<Node> nodeArray = new ArrayList<Node>();
        //nodeArray = DB.getToDoList()
        ArrayList<Edge> edgeArray = new ArrayList<Edge>();
        edgeArray = DB.getAllEdges();

        //display all edges
        scale = imageWidth / imageView.getFitWidth();
        ArrayList<String> lineList = new ArrayList<String>();
        //display all edges
        for(int i = 0; i < edgeArray.size(); i++) {
            double startX = -1;
            double startY = -1;
            double endX = -1;
            double endY = -1;
            //get start and end node for each edge
            String start = edgeArray.get(i).getStartNodeId();
            String end = edgeArray.get(i).getEndNodeId();
            //parse through nodes, when you reach ones that match start and end of this
            //edge, retrieve coordinates
            for (int j = 0; j < nodeArray.size(); j++) {
                if (nodeArray.get(j).get("floor").equals(floorNum)) {
                    if (nodeArray.get(j).get("id").equals(start)) {
                        startX = nodeArray.get(j).getX() / scale;
                        startY = nodeArray.get(j).getY() / scale;
                    }
                    if (nodeArray.get(j).get("id").equals(end)) {
                        endX = nodeArray.get(j).getX() / scale;
                        endY = nodeArray.get(j).getY() / scale;
                    }
                }
                //if you've retrieved the edge, create a line
                if (startX != -1 && startY != -1 && endX != -1 && endY != -1) {
                    Line line = new Line(startX, startY, endX, endY);
                    line.setStroke(Color.color(1,0,0,0.4));
                    //don't add same edge twice
                    if(!lineList.contains(line.toString())) {
                        line.setStrokeLineCap(StrokeLineCap.ROUND);
                        line.setStrokeWidth(1);
                        g.getChildren().add(line);
                        lineList.add(line.toString());
                    }

                }
            }
        }
        //display all nodes
        for (int i = 0; i < nodeArray.size(); i++) {
            double xCoord = nodeArray.get(i).getX() / scale;
            double yCoord = nodeArray.get(i).getY() / scale;
            Circle circle = new Circle(xCoord, yCoord, 2, Color.BLACK);
            g.getChildren().add(circle);
        }
        pane.getChildren().add(g);
    }

    @FXML
    void initialize() {

        //get primaryStage
        Stage primaryStage = App.getPrimaryStage();

        //get dimensions of stage
        stageWidth = primaryStage.getWidth();
        stageHeight = primaryStage.getHeight();

        //Set up zoomable and pannable panes
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);

        //set default/initial floor for map
        Image image = new Image("edu/wpi/cs3733/D21/teamE/maps/1.png");
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
    }



}
