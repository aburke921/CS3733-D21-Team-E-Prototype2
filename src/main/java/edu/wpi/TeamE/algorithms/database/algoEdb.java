package edu.wpi.TeamE.algorithms.database;

import java.util.Map;

public class algoEdb {

    /**
     * query the Node table to get the nodes field data
     * i.e. xCoord, yCoord, floor, etc
     * @return a map of strings containing all of these fields
     *         key is the field type, value is the data
     */
    public static Map<String, String> getNode(String nodeId){
        return null;
    }

    /**
     * query the Edge table to get every node which shares an edge with nodeId
     * @return an string array containing each neighbors nodeId
     */
    public static String[] getNeighbors(String nodeId){
        return null;
    }

}