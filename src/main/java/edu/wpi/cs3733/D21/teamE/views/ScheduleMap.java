package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.Date;
import edu.wpi.cs3733.D21.teamE.map.Edge;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.map.Path;
import edu.wpi.cs3733.D21.teamE.pathfinding.SearchContext;
import edu.wpi.cs3733.D21.teamE.scheduler.Schedule;
import edu.wpi.cs3733.D21.teamE.scheduler.ToDo;
import edu.wpi.cs3733.D21.teamE.states.ToDoState;
import javafx.animation.PathTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ScheduleMap {

    // fx:id="imageView"
    private ImageView imageView = new ImageView();
    @FXML // fx:id="pane"
    private Pane pane = new Pane();
    @FXML // fx:id="zoomSlider"
    private Slider zoomSlider;
    @FXML // fx:id="scrollPane"
    private BorderPane rootBorderPane;
    @FXML
    private AnchorPane lowerAnchorPane;
    @FXML // fx:id="minETA"
    private Label minETA;
    @FXML // fx:id="secETA"
    private Label secETA;

    @FXML // fx:id="dist"
    private Label dist;

    @FXML // fx:id="datePicker"
    private JFXDatePicker datePicker;

    @FXML // fx:id="dateLabel"
    private Label dateLabel;

    @FXML // fx:id="goBackDay"
    private MaterialDesignIconView goBackDay;

    @FXML // fx:id="goForwardDay"
    private MaterialDesignIconView goForwardDay;

    //private Schedule;

    private Node startNode = null;
    private Node endNode = null;

    private double stageWidth;
    private double stageHeight;

    private double imageWidth;
    private double imageHeight;

    private double scale;

    private int[] floorVisits = new int[]{0, 0, 0, 0, 0, 0}; // Number of times each floor has been visited, in order: L2, L1, G, 1, 2, 3

    private String selectedStartNodeID; // selected starting value's ID

    private String selectedEndNodeID; // selected ending value's ID

    private String currentFloor = "1"; // set based on button presses

    private Path currentFoundPath; // the last found path

    private double strokeWidth = 3;

    private Date currentDate = new Date(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());

    private HashMap<String, Integer> floorMap = new HashMap<String, Integer>(){{
        put("L2", 0);
        put("L1", 1);
        put("G", 2);
        put("1", 3);
        put("2", 4);
        put("3", 5);
    }};

    /**
     * Uses {@link SearchContext}'s search() function to find the best path,
     * given the two current start and end positions ({@link #selectedStartNodeID} and {@link #selectedEndNodeID}).
     * Then calls {@link #drawMap(Path, String)}.
     * Sets {@link #currentFoundPath}. Returns a SnackBar when path is null.
     * @param event calling function's (Find Path Button) event info.
     */
    @FXML
    public void findPath() {
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


                //draw the map for the current floor
                drawMap(foundPath, currentFloor);
            }
        }
    }

    /**
     * Switches visible floor
     * @param e Button click action
     */
    public void chooseFloor(ActionEvent e) {
        //clear current floor of markers
        /*for (Node node : currentMarkers) {
            NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
            nM.getRectangle().setVisible(false);
        }*/
        Button button = ((Button) e.getSource());
        //currentMarkers.clear();
        String floor = button.getText();

        setCurrentFloor(floor);
        //drawMap(currentFoundPath, currentFloor);

        System.out.println("Current floor set to " + floor);
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


    public void drawMap(Path fullPath, String floorNum) {

        //clear map
        System.out.print("\nCLEARING MAP...");
        pane.getChildren().clear();
        System.out.println(" DONE");

        System.out.println("drawMap() is Finding path for floor " + floorNum);


        //if path is null
        if (fullPath == null) {
            System.out.println("NULL PATH");
            //todo snackbar to say no path set
            return;
        }

        int legNum = 0;

        List<Path> paths = fullPath.splitByFloor();
        for(Path path : paths){
            if(path.getStart().get("floor").equalsIgnoreCase(floorNum)){

                double markerIconXOffset = -(scale * 3);
                double markerIconYOffset = -(scale / 2);
                String mapMarkerSize = "25";

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
                            MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.MAP_MARKER);
                            icon.setSize(mapMarkerSize);
                            icon.setLayoutX(prevXCoord + markerIconXOffset);
                            icon.setLayoutY(prevYCoord + markerIconYOffset);

                            if (firstID.equalsIgnoreCase(selectedStartNodeID)) {
                                System.out.println("SCALE: " + scale);
                                // True first node
                                icon.setId("submission-icon");

                                //circle = new Circle(prevXCoord, prevYCoord, radius, Color.GREEN);
                            } else {
                                // First on floor
                                icon.setId("red-icon");

                                //circle = new Circle(prevXCoord, prevYCoord, radius, Color.RED);
                            }


                            //create a line between this node and the previous node
                            Line line = new Line(prevXCoord, prevYCoord, xCoord, yCoord);
                            line.setStrokeLineCap(StrokeLineCap.ROUND);
                            line.setStrokeWidth(strokeWidth);
                            line.setStroke(Color.RED);

                            g.getChildren().addAll(line, icon);
                        } else {
                            //Track true first node's ID, for node color issue
                            firstID = node.get("id");
                        }
                        if (!legItr.hasNext()) { //if current node is the ending node for this floor, e.g. last node is also first node on floor

                            MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.MAP_MARKER);
                            icon.setSize(mapMarkerSize);
                            icon.setLayoutX(xCoord + markerIconXOffset);
                            icon.setLayoutY(yCoord + markerIconYOffset);

                            if (node.get("id").equals(selectedStartNodeID)) { // start node of entire path
                                //place a dot on the location
                                icon.setId("submission-icon");
                            } else if (node.get("id").equals(selectedEndNodeID)) { // end node of entire path
                                //place a dot on the location
                                icon.setId("black-icon");
                            } else { // end node of just this floor
                                //place a dot on the location
                                icon.setId("red-icon");
                            }

                            g.getChildren().addAll(icon);
                        }
                        //update the coordinates for the previous node
                        prevXCoord = xCoord;
                        prevYCoord = yCoord;


                    } else if (!legItr.hasNext()) { //if current node is the ending node for this floor

                        MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.MAP_MARKER);
                        icon.setSize(mapMarkerSize);
                        icon.setLayoutX(xCoord + markerIconXOffset);
                        icon.setLayoutY(yCoord + markerIconYOffset);

                        if (node.get("id").equals(selectedEndNodeID)) { // end node of entire path
                            //place a dot on the location
                            icon.setId("black-icon");
                        } else { // end node of just this floor
                            //place a dot on the location
                            icon.setId("red-icon");
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

                            floorLabel.setOnMouseClicked(e -> {
                                int num = floorMap.get(floorNum);
                                floorVisits[num] = floorVisits[num] + 1;
                                setCurrentFloor(finalDestFloor);
                            });

                            g.getChildren().addAll(line, icon, flowPane);
                        } else {
                            //otherwise, only add the line and node circle
                            g.getChildren().addAll(line, icon);
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
                    //zoomToPath(xMin * scale, xMax * scale, yMin * scale, yMax * scale);
                }
                legNum++;
            } else {
                System.out.println("No path on this floor");
                //todo snackbar to say no nodes on this floor?
            }
        }
    }

    @FXML
    private void setDateLabel(Date date) {
        int monthInt = date.getMonth();
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (monthInt >= 0 && monthInt <= 11 ) {
            month = months[--monthInt];
        }

        String day = Integer.toString(date.getDay());
        String year = Integer.toString(date.getYear());
        String dateFormat = month + " " + day+ ", " + year;
        dateLabel.setText(dateFormat);
    }

    /**
     * Changes the displayed map, and path; sets {@link #currentFloor}.
     * @param floorNum floor to change to
     */
    public void setCurrentFloor(String floorNum) {
        currentFloor = floorNum;

        //switchFocusButton(floorNum);

        //draw path for new floor
        drawMap(currentFoundPath,currentFloor);

        System.out.println("Current floor set to " + floorNum);
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

        scale = imageWidth / imageView.getFitWidth();

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

        datePicker.setValue(LocalDate.now());
        Date date = new Date(datePicker.getValue());
        setDateLabel(date);

        //set up icons for moving foward and backward a day
        goBackDay.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                LocalDate currDate = datePicker.getValue();
                datePicker.setValue(currDate.minusDays(1));
                setDateLabel(new Date(datePicker.getValue()));
                //prepareToDoTable(treeTableView, datePicker.getValue().toString());
            }
        });

        goForwardDay.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                LocalDate currDate = datePicker.getValue();
                datePicker.setValue(currDate.plusDays(1));
                setDateLabel(new Date(datePicker.getValue()));
                //prepareToDoTable(treeTableView, datePicker.getValue().toString());
            }
        });

        Schedule scheduleOngoing = DB.getSchedule(App.userID, 1, datePicker.getValue().toString());
        Schedule scheduleCompleted = DB.getSchedule(App.userID, 10, datePicker.getValue().toString());


        ArrayList<Node> nodeArray = DB.getAllNodes();
        List<ToDo> array = scheduleOngoing.getTodoList();
            for(int j = 0; j < nodeArray.size(); j++) {
                if(array.get(0).getLocationString().equals(nodeArray.get(j).get("longName"))) {
                    startNode = nodeArray.get(j);
                }
                if(array.get(array.size()-1).getLocationString().equals(nodeArray.get(j).get("longName"))) {
                    endNode = nodeArray.get(j);
                }
            }
            findPath();
    }


    @FXML
    private void changeDate(ActionEvent event) {
        setDateLabel(new Date(datePicker.getValue()));
        drawMap(currentFoundPath, datePicker.getValue().toString());
    }



    @FXML
    private void switchScene(ActionEvent event) {
        //ToDoState toDoState = new ToDoState();
        //toDoState.switchScene(event);
    }

}
