package edu.wpi.cs3733.D21.teamE.map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Node Class, lots of specific Node implementation stuff
 * Handles node info
 */
public class Node implements Comparable<Node> {
    private int xCoord, yCoord, zCoord;
    //mapping of node information
    private HashMap<String, String> nodeInfo;

    //if this is part of a Path, next will be the next Node in path
    //otherwise null
    private Node next;

    //an array containing the ids and distances of this node's neighbors
    private List<String> neighbors;

    //cost estimate for A*
    private Double costEst;

    /**
     * Default constructor only initializes the neighbors list
     * used for sentinel nodes in Path
     */
    public Node(){
        next = null;
        neighbors = new LinkedList<>();
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
        nodeInfo = new HashMap<>(6);
        nodeInfo.put("id", _id);
        nodeInfo.put("floor", _floor);
        nodeInfo.put("building", _building);
        nodeInfo.put("type", _type);
        nodeInfo.put("longName", _longName);
        nodeInfo.put("shortName", _shortName);

        xCoord = _x;
        yCoord = _y;
        zCoord = calculateZ(_floor);
    }
    public static int calculateZ(String floor) {
        //floor HashMap
        HashMap<String, Integer> floorMap = new HashMap<String, Integer>(){{
            put("L2", 0);
            put("L1", 1);
            put("G", 1);
            put("1", 2);
            put("2", 3);
            put("3", 4);
        }};

        int magicNumber = 30;
        Integer level = floorMap.get(floor);
        if(level == null){
            return 0;
        } else {
            return floorMap.get(floor) * magicNumber;
        }
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

    public int getZ(){
        return zCoord;
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

    public List<String> getNeighbors(){
        return neighbors;
    }

    public void addNeighbor(String newNeighborId){
        neighbors.add(newNeighborId);
    }

    public Node copy(){
        return new Node(get("id"), xCoord, yCoord, get("floor"), get("building"), get("type"),
                get("longName"), get("shortName"));
    }

    public boolean isStair(){
        return get("type").equalsIgnoreCase("STAI");
    }

    public boolean isEmergency(){
        return get("longName").contains("Emergency");
    }

    /**
     * @param n node to compare with this
     * @return true if the two nodes 'id's  are equal
     */
    public boolean equals(Node n){
        if(nodeInfo == null && n.nodeInfo == null){
            //two pathHeads, equal
            return true;
        } else if(nodeInfo == null || n.nodeInfo == null){
            //one pathHead but not two, not equal
            return false;
        } else {
            //no pathHeads, equal if ids are equal
            return get("id").equalsIgnoreCase(n.get("id"));
        }
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
     * Calculate the euclidean distance between two nodes
     * Pythagorean theorem
     * @param n Parameter Node
     * @return the distance between two nodes
     */
    public double dist(Node n){
        double xDist = Math.pow(this.getX() - n.getX(), 2);
        double yDist = Math.pow(this.getY() - n.getY(), 2);
        double zDist = Math.pow(this.getZ() - n.getZ(), 2);
        return Math.sqrt(xDist + yDist + zDist);
    }
}
