package edu.wpi.TeamE.views;

import edu.wpi.TeamE.algorithms.Node;
import javafx.scene.shape.Circle;
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
