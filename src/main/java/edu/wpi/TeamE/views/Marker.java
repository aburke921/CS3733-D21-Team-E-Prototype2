package edu.wpi.TeamE.views;

import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.databases.NodeDB;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

public class Marker {

    //Hashmap mapping NodeID to NodeMarker
    private HashMap<String, NodeMarker> locationMarker;
    //Hashmap mapping Node type to integer(0 or 1)
    private HashMap<String, Integer> checkBoxSelected;
    //Hashmap mapping Node type to color
    private HashMap<String, Color> typeColor;

    Integer sideLength = 5;

    public Marker() {
        locationMarker = new HashMap<String, NodeMarker>(800);
        checkBoxSelected = new HashMap<String, Integer>(20);
        typeColor = new HashMap<String, Color>(20);

        ArrayList<Node> nodeArrayList = NodeDB.getAllNodes();
        for (int i = 0; i < nodeArrayList.size(); i++) {
            Rectangle rectangle = new Rectangle(0, 0, sideLength, sideLength);
            NodeMarker nodeMarker = new NodeMarker(nodeArrayList.get(i), rectangle);
            locationMarker.put(nodeArrayList.get(i).get("id"), nodeMarker);
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
        typeColor.put("CONF", Color.ORANGE);
        typeColor.put("DEPT", Color.web("#690080"));
        typeColor.put("ELEV", Color.web("#769557"));
        typeColor.put("INFO", Color.web("#b46e25"));
        typeColor.put("LABS", Color.web("#a40289"));
        typeColor.put("REST", Color.web("#b00404"));
        typeColor.put("RETL", Color.web("#3d4f9d"));
        typeColor.put("STAI", Color.web("#007f52"));
        typeColor.put("SERV", Color.web("#4800ff"));
        typeColor.put("EXIT", Color.YELLOWGREEN);
        typeColor.put("PARK", Color.web("#159bd2"));
        typeColor.put("WALK", Color.BLACK);
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

}
