package edu.wpi.TeamE.algorithms.database;

import edu.wpi.TeamE.algorithms.pathfinding.Node;

import java.util.HashMap;

public class makeConnection {

    /**
     * query the Node table to get the nodes field data
     * i.e. xCoord, yCoord, floor, etc
     * @return an array of strings containing all of these fields
     *         key is the field type, value is the data
     */
    public Node getNodeInfo(String nodeID){
        return null;
    }

    /**
     * query the Edge table to get every node which shares an edge with nodeId
     * @return an string array containing each neighbors nodeId
     */
    public HashMap<String, Double> getNeighbors(String nodeId){
        return null;
    }

}