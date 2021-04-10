package edu.wpi.TeamE.algorithms.pathfinding;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Abstract Seacher Class for Pathfinding API
 * On creation can be initialized to A* or DFS (already implemented) or others that can be added later
 */
public abstract class Searcher {
    /**
     * Generic Search method for UI
     * Seraches between Start and End Nodes for path
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
    Path reconstructPath(HashMap<Node, Node> cameFrom, Node end) {
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