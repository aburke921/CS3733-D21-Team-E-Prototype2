package edu.wpi.TeamE.algorithms.pathfinding;


/**
 * Edge Class
 * Handles specific details about edges
 */
public class Edge{
    private String id;
    private String startNodeId;
    private String endNodeId;

    /**
     * 'start' and 'end' are slight misnomers, the edge is undirected
     * @param _id This is the id of the edge
     * @param _startNode this is the "start" node
     * @param _endNode This is the "end" node
     */
    public Edge(String _id, String _startNode, String _endNode){
        id = _id;
        startNodeId = _startNode;
        endNodeId = _endNode;
    }

    /**
     * @param node String representation of which node is requested
     *             should have values of 'start' or 'end'
     * @return returns the id of the requested node
     */
    public String getNode(String node) {
        if (node.equalsIgnoreCase("START")) {
            return startNodeId;
        } else if (node.equalsIgnoreCase("END")) {
            return endNodeId;
        } else {
            return null;
        }
    }

    /**
     * @return returns the id of the edge
     */
    public String getId(){
        return id;
    }
}