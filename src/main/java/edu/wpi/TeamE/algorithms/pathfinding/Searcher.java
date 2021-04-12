package edu.wpi.TeamE.algorithms.pathfinding;

import java.util.HashMap;
import java.util.LinkedList;

import edu.wpi.TeamE.algorithms.database.makeConnection;

/**
 * Abstract Searcher Class for Pathfinding API
 * On creation can be initialized to A* or DFS (already implemented) or others that can be added later
 */
public abstract class Searcher {

    //this is a local cache of nodes which have been worked with in the past
    private HashMap<String, Node> cache;

    private makeConnection con;

    /**
     * Super constructor, initializes cache
     */
    public Searcher(){
        cache = new HashMap<>();
        con = new makeConnection();
    }


    /**
     * @param nodeId the Id of the node you wish to receive
     * @return the requested node
     */
    public Node getNode(String nodeId){
        if(!cache.containsKey(nodeId)){
            cacheNode(nodeId);
        }
        return cache.get(nodeId);
    }

    /**
     * @param nodeId The Id of the node you want to get the neighbors of
     * @return the neighbors of that node
     */
    public HashMap<String, Double> getNeighbors(String nodeId){
        if(!cache.containsKey(nodeId)){
            cacheNode(nodeId);
        }
        return cache.get(nodeId).getNeighborIds();
    }

    /**
     * @param nodeId the id of the node you wish to cache
     */
    private void cacheNode(String nodeId){
        Node node = con.getNodeInfo(nodeId);
        HashMap<String, Double> neighbors = con.getNeighbors(nodeId);
        node.setNeighborIds(neighbors);
        cache.put(nodeId, node);
    }

    /**
     * Generic Search method for UI
     * Searches between Start and End Nodes for path
     * Searching algorithm can be A* or DFS (more can be added in the future)
     *
     * @param startId The NodeID of the start node as a string
     * @param endId   The NodeID of the end node as a string
     * @return The path from Start to End as a Path (basically a LinkedList)
     */
    abstract public Path search(String startId, String endId);

    /**
     * Once the end node is found, this method is invoked to work back
     * and find the path we took to get there
     * @param cameFrom Map representation of which nodes came from where when discovered by search
     * @param end the ending node
     * @return Path of nodes following start
     * start is not included because it didn't 'comeFrom' any node so it's value in map is null
     */
    protected Path reconstructPath(HashMap<Node, Node> cameFrom, Node end) {
        LinkedList<Node> stack = new LinkedList<>();
        //push onto stack
        for(Node current = end; cameFrom.containsKey(current); current = cameFrom.get(current)){
            stack.push(current);
        }

        Path path = new Path();
        //pop off of stack onto linked list
        while(!stack.isEmpty()){
            path.add(stack.pop());
        }

        return path;
    }
}