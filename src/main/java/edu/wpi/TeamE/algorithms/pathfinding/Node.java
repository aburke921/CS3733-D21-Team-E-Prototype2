package edu.wpi.TeamE.algorithms.pathfinding;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Node Class, lots of specific Node implementation stuff
 * Handles node info
 */
public class Node implements Comparable<Node>, Iterable<Node> {
    //mapping of node information
    private HashMap<String, String> nodeInfo;

    //if this is part of a Path, next will be the next Node in path
    //otherwise null
    private Node next;

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
     * @param _nodeInfo map of node's information
     */
    public Node(Map<String, String> _nodeInfo){
        this();

        nodeInfo = new HashMap<>(_nodeInfo);
    }

    /**
     * Overloaded Constructor for Nodes, keeps lots of info
     */
    public Node(String _id, String _x, String _y,
                String _floor, String _building,
                String _nodeType, String _longName,
                String _shortName){
        this();

        nodeInfo = new HashMap<>(8);
        nodeInfo.put("id", _id);
        nodeInfo.put("xCoord", _x);
        nodeInfo.put("yCoord", _y);
        nodeInfo.put("floor", _floor);
        nodeInfo.put("building", _building);
        nodeInfo.put("type", _nodeType);
        nodeInfo.put("longName", _longName);
        nodeInfo.put("shortName", _shortName);
    }


    /**
     * @param info String representation of what info is requested
     *             should be 'xCoord', 'yCoord', etc
     * @return returns the requested information
     */
    public String get(String info){
        return nodeInfo.get(info);
    }

    /**
     * @return a parsed int of the 'xCoord' key
     */
    public int getX(){
        return Integer.parseInt(nodeInfo.get("xCoord"));
    }

    /**
     * @return a parsed int of the 'yCoord' key
     */
    public int getY(){
        return Integer.parseInt(nodeInfo.get("yCoord"));
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

    /**
     * @param n node to compare with this
     * @return true if the two nodes 'id's  are equal
     */
    public boolean equals(Node n){
        return get("id").equalsIgnoreCase(n.get("id"));
    }

    /**
     * @return string rep. which includes 'id' and 'longName'
     */
    @Override
    public String toString() {
        return "Id : " + get("id") + ",  Name: " + get("longName");
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
