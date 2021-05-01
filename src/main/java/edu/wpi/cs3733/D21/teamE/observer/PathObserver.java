package edu.wpi.cs3733.D21.teamE.observer;

import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.map.Path;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

import java.util.Iterator;
import java.util.List;

public class PathObserver extends Observer {

    private double radius = 6;
    private double strokeWidth = 3;

    private Pane pane;
    private Path fullPath;
    private Double scale;
    private String selectedStartNodeID;
    private String selectedEndNodeID;

    public PathObserver(Subject subject, Pane pane, Path fullPath, Double scale, String selectedStartNodeID, String selectedEndNodeID){
        this.subject = subject;
        this.pane = pane;
        this.fullPath = fullPath;
        this.scale = scale;
        this.selectedStartNodeID = selectedStartNodeID;
        this.selectedEndNodeID = selectedEndNodeID;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println("PathObserver");
        String floorNum = subject.getState();
        if (floorNum.equals("")) {return;}

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
                //todo snackbar to say no nodes on this floor?
            }
        }
    }
}
