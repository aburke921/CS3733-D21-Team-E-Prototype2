package edu.wpi.cs3733.D21.teamE.views;

import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.database.NodeDB;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Marker {

    //Hashmap mapping NodeID to NodeMarker
    private HashMap<String, NodeMarker> locationMarker;
    //Hashmap mapping Node type to integer(0 or 1)
    private HashMap<String, Integer> checkBoxSelected;
    //Hashmap mapping Node type to color
    private HashMap<String, Color> typeColor;
    //Hashmap mapping String(node type + floor) to an array holding the nodes with that node type and floor
    private HashMap<String, ArrayList<Node>> typeAndFloorNode;

    //Group containing all the rectangles
    private Group markerGroup = new Group();

    private Integer sideLength = 7;
    private Integer numTypes = 13;
    private Integer numFloors = 6;
    private String[] types = {"HALL", "CONF", "DEPT", "ELEV", "INFO", "LABS", "REST","RETL", "STAI", "SERV", "EXIT", "PARK", "WALK"};
    private String[] floors = {"L1", "L2", "G", "1", "2", "3"};

    public Marker() {
        locationMarker = new HashMap<String, NodeMarker>(800);
        checkBoxSelected = new HashMap<String, Integer>(20);
        typeColor = new HashMap<String, Color>(20);
        typeAndFloorNode = new HashMap<String, ArrayList<Node>>(15);

        for (int i = 0; i < numTypes; i++) {
            ArrayList<Node> typeArrayList = NodeDB.getAllNodesByType(types[i]);
            for (int j = 0; j < numFloors; j++) {
                ArrayList<Node> floorArrayList = NodeDB.getAllNodesByFloor(floors[j]);

                List<Node> intersection = floorArrayList.stream()
                        .filter(type -> typeArrayList.stream()
                                .anyMatch(floor ->
                                        type.get("id").equals(floor.get("id"))))
                        .collect(Collectors.toList());

                String typeAndFloorString = types[i] + floors[j];
                typeAndFloorNode.put(typeAndFloorString, (ArrayList<Node>) intersection);
            }
        }

        checkBoxSelected.put("HALL", 0);
        checkBoxSelected.put("CONF", 0);
        checkBoxSelected.put("DEPT", 0);
        checkBoxSelected.put("ELEV", 0);
        checkBoxSelected.put("INFO", 0);
        checkBoxSelected.put("LABS", 0);
        checkBoxSelected.put("REST", 0);
        checkBoxSelected.put("RETL", 0);
        checkBoxSelected.put("STAI", 0);
        checkBoxSelected.put("SERV", 0);
        checkBoxSelected.put("EXIT", 0);
        checkBoxSelected.put("PARK", 0);
        checkBoxSelected.put("WALK", 0);

        typeColor.put("HALL", Color.web("#7c7c7c"));
        typeColor.put("CONF", Color.web("#7f5124"));
        typeColor.put("DEPT", Color.web("#74058c"));
        typeColor.put("ELEV", Color.web("#769557"));
        typeColor.put("INFO", Color.web("#dc721c"));
        typeColor.put("LABS", Color.web("#c900ae"));
        typeColor.put("REST", Color.web("#b00404"));
        typeColor.put("RETL", Color.web("#3d4f9d"));
        typeColor.put("STAI", Color.web("#007f52"));
        typeColor.put("SERV", Color.web("#005cff"));
        typeColor.put("EXIT", Color.web("#90e430"));
        typeColor.put("PARK", Color.web("#1299d2"));
        typeColor.put("WALK", Color.BLACK);
    }

    public void populateLocationMarkerMarkerGroup(Double scale) {

        ArrayList<Node> nodeArrayList = NodeDB.getAllNodes();

        for (int i = 0; i < nodeArrayList.size(); i++) {
            Double xCoord = nodeArrayList.get(i).getX() / scale - (sideLength * 0.5);
            Double yCoord = nodeArrayList.get(i).getY() / scale - (sideLength * 0.5);
            Rectangle rectangle = new Rectangle(xCoord, yCoord, sideLength, sideLength);
            rectangle.setStroke(Color.BLACK);
            rectangle.setFill(typeColor.get(nodeArrayList.get(i).get("type")));
            rectangle.setVisible(false);
            //rectangle.setOpacity(0.25);

            NodeMarker nodeMarker = new NodeMarker(nodeArrayList.get(i), rectangle);

            this.getLocationMarker().put(nodeArrayList.get(i).get("id"), nodeMarker);
            markerGroup.getChildren().add(rectangle);

        }
    }

    public HashMap<String, Integer> getSelectedCheckBox() {
        return this.checkBoxSelected;
    }

    public HashMap<String, NodeMarker> getLocationMarker() {
        return this.locationMarker;
    }

    public HashMap<String, Color> getTypeColor() {
        return this.typeColor;
    }

    public HashMap<String, ArrayList<Node>>  getTypeAndFloorNode() {
        return this.typeAndFloorNode;
    }

    public Group getMarkerGroup() {
        return this.markerGroup;
    }

}
