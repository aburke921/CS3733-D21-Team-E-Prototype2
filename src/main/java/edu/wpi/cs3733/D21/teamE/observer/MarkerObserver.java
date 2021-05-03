package edu.wpi.cs3733.D21.teamE.observer;

import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.views.Marker;
import edu.wpi.cs3733.D21.teamE.views.NodeMarker;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MarkerObserver extends Observer {

    private Pane pane;
    private Marker marker;
    private ArrayList<Node> currentMarkers;

    public MarkerObserver(Subject subject, Pane pane, Marker marker, ArrayList<Node> currentMarkers){
        this.subject = subject;
        this.pane = pane;
        this.marker = marker;
        this.currentMarkers = currentMarkers;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println("MarkerObserver");
        String floorNum = subject.getState();
        if (floorNum.equals("")) {return;}

        //Get a list of types that are currently selected
        ArrayList<String> currentlyViewableTypes = new ArrayList<String>();
        for (String key : marker.getSelectedCheckBox().keySet()) {
            if (marker.getSelectedCheckBox().get(key) == 1) {
                currentlyViewableTypes.add(key);
            }
        }

        for (String currViewType : currentlyViewableTypes) {
            String typeAndFloorString = currViewType + floorNum;
            //Get the nodes with the current floor and type
            ArrayList<Node> currentlyViewableNodes = marker.getTypeAndFloorNode().get(typeAndFloorString);

            //For every node, set it to visible
            for (Node node : currentlyViewableNodes) {
                NodeMarker nM = marker.getLocationMarker().get(node.get("id"));
                Rectangle r = nM.getRectangle();
                r.setVisible(true);
                r.setFill(marker.getTypeColor().get(currViewType));
                currentMarkers.add(node);
            }
        }

    }
}
