package edu.wpi.TeamE.algorithms.database;

import edu.wpi.TeamE.algorithms.pathfinding.Node;
import org.apache.derby.iapi.services.io.FormatableHashtable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class algoCSV {

    private static FormatableHashtable nodes;

    private static HashMap<String, Node> dbNodes;
    private static ArrayList<String[]> dbEdges;

    public static void ReadCSVs() throws FileNotFoundException {
        dbNodes = new HashMap<>();
        dbEdges = new ArrayList();
        ClassLoader csvLoader = algoCSV.class.getClassLoader();
        Scanner nodesScanner = new Scanner(new File("src/main/resources/edu/wpi/TeamE/csv/MapENodes.csv"));

        nodesScanner.nextLine(); //header line

        while (nodesScanner.hasNext()) {

            String[] nodeData = nodesScanner.nextLine().split(",");

            //extract data for node
            String nodeID = nodeData[0];
            int xcoord = Integer.parseInt(nodeData[1]);
            int ycoord = Integer.parseInt(nodeData[2]);
            String floor = nodeData[3];
            String building = nodeData[4];
            String nodeType = nodeData[5];
            String longName = nodeData[6];
            String shortName = nodeData[7];

            //add node to HashMap
            dbNodes.put(nodeID, new Node(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName));
        }

        Scanner edgesScanner = new Scanner(new File("src/main/resources/edu/wpi/TeamE/csv/MapEEdges.csv"));

        edgesScanner.nextLine(); //header line

        while (edgesScanner.hasNext()) {

            String[] edgeData = edgesScanner.nextLine().split(",");

            //add edge to ArrayList
            dbEdges.add(edgeData);
        }
    }

    /**
     * query the Node table to get the nodes field data
     * i.e. xCoord, yCoord, floor, etc
     * @return an array of strings containing all of these fields
     *         key is the field type, value is the data
     */
    public static Node getNode(String nodeId){
        if (dbNodes.containsKey(nodeId)) {
            return dbNodes.get(nodeId);
        } else {
            return null;
        }
    }

    /**
     * query the Edge table to get every node which shares an edge with nodeId
     * @return an string array containing each neighbors nodeId
     */
    public static HashMap<String, Double> getNeighbors(String nodeId){
        HashMap<String, Double> out = new HashMap<>();
        for (String[] edge : dbEdges) {
            if (edge[1].equals(nodeId)) {
                out.put(edge[2], 0.0);
            } else if (edge[2].equals(nodeId)) {
                out.put(edge[1], 0.0);
            }
        }
        return out;
    }

}