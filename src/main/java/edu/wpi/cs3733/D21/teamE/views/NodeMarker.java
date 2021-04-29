package edu.wpi.cs3733.D21.teamE.views;

import edu.wpi.cs3733.D21.teamE.map.Node;
import javafx.scene.shape.Rectangle;

public class NodeMarker{

    private Node node;
    private Rectangle rectangle;

    public NodeMarker(Node node, Rectangle rectangle) {
        this.node = node;
        this.rectangle = rectangle;
    }

    public Node getNode(){
        return this.node;
    }

    public Rectangle getRectangle(){
        return this.rectangle;
    }

}
