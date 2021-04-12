package edu.wpi.TeamE.algorithms.pathfinding;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Node Class, lots of specific Node implementation stuff
 * Handles node info
 */
public class Node implements Comparable<Node>, Iterable<Node> {
    private int xCoord, yCoord;
    //mapping of node information
    private HashMap<String, String> nodeInfo;

    //if this is part of a Path, next will be the next Node in path
    //otherwise null
    private Node next;

    //an array containing the ids of this node's neighbors
    private HashMap<String, Double> neighbors;

    //cost estimate for A*
    private Double costEst;

    /**
     * Default constructor only initializes the neighbors list
     * used for sentinel nodes in Path
     */
    public Node(){
        next = null;
        costEst = Double.MAX_VALUE;
    }

    /**
     * Overloaded Constructor for Nodes, keeps lots of info
     */
    public Node(String _id, int _x, int _y,
                String _floor, String _building,
                String _type, String _longName,
                String _shortName){
        this();
        xCoord = _x;
        yCoord = _y;
        nodeInfo = new HashMap<>(6);
        nodeInfo.put("id", _id);
        nodeInfo.put("floor", _floor);
        nodeInfo.put("building", _building);
        nodeInfo.put("type", _type);
        nodeInfo.put("longName", _longName);
        nodeInfo.put("shortName", _shortName);
    }

    /**
     * @param info String representation of what info is requested
     *             should be 'id', 'floor', 'building', 'type', 'longName', 'shortName'
     * @return returns the requested information
     */
    public String get(String info){
        return nodeInfo.get(info);
    }

    /**
     * @return The X Coordinate
     */
    public int getX(){
        return xCoord;
    }

    /**
     * @return The Y Coordinate
     */
    public int getY(){
        return yCoord;
    }

    /**
     * cost is used in pathfinding to evaluate if this node is worth being part of the path
     * should always call setCost before adding node to a PriorityQueue
     * so as to wipe out any previous calculations
     * @param cost set the cost associated with this node
     */
    public void setCost(Double cost){
        costEst = cost;
    }

    /**
     * @param _next node which is next in the Path
     */
    public void setNext(Node _next){
        next = _next;
    }

    /**
     * @return the next node in Path
     */
    public Node getNext(){
        return next;
    }

    public void setNeighborIds(HashMap<String, Double> _neighbors){
        neighbors = _neighbors;
    }

    public HashMap<String, Double> getNeighborIds(){
        return neighbors;
    }

    /**
     * @param n node to compare with this
     * @return true if the two nodes 'id's  are equal
     */
    public boolean equals(Node n){
        return get("id").equalsIgnoreCase(n.get("id"));
    }

    /**
     * This is used by the PriorityQueue as its ordering function
     * @param n Node to compare with this
     * @return comparison of node's costEst
     */
    @Override
    public int compareTo(Node n){
        return costEst.compareTo(n.costEst);
    }

    /**
     * defined for use in HashMap
     * @return hashCode of 'id'
     */
    @Override
    public int hashCode(){
        return get("id").hashCode();
    }

    /**
     * @return an iterator for iterating through Paths
     */
    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator(this);
    }

    private class NodeIterator implements Iterator<Node> {
        Node cursor;
        private NodeIterator(Node _cursor){
            cursor = _cursor;
        }

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public Node next() {
            Node tmp = cursor;
            cursor = cursor.getNext();
            return tmp;
        }
    }

}
