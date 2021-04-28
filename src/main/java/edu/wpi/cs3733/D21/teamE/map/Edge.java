package edu.wpi.cs3733.D21.teamE.map;

/**
 * Edge Class
 * Handles specific details about edges
 */
public class Edge{
    private String id;
    private String startNodeId;
    private String endNodeId;
    private Double length;

    /**
     * 'start' and 'end' are slight misnomers, the edge is undirected
     * @param _id This is the id of the edge
     * @param _startNode this is the "start" node
     * @param _endNode This is the "end" node
     */
    public Edge(String _id, String _startNode, String _endNode, Double _length){
        id = _id;
        startNodeId = _startNode;
        endNodeId = _endNode;
        length = _length;
    }

    /**
     * @param node String representation of which node is requested
     *             should have values of 'start' or 'end'
     * @return returns the id of the requested node
     */
    public String getNode(int node) {
        if (node == 0) {
            return startNodeId;
        } else if (node == 1) {
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

    public Double getLength(){
        return length;
    }

    public String getStartNodeId() { return startNodeId; }

    public String getEndNodeId() { return endNodeId; }
}