package edu.wpi.TeamE.algorithms.pathfinding;

import edu.wpi.TeamE.algorithms.database.algoEdb;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * DF Search Implementation
 * Contains specific implementation of DFS
 */
public class DFSSearch extends Searcher {

    /**
     * Searches for a Path from startId to endId
     * Utilizing the Depth First Search Algorithm
     * i.e. will continue along a path until it hits a dead end or finds its goal
     * @param startId the Node to begin Path at
     * @param endId the Node to end Path at
     * @return Path object representing the route from startId to endId
     */
    public Path search(String startId, String endId){

        //get node info from database
        Node start = algoEdb.getNode(startId);
        Node end = algoEdb.getNode(endId);

        //stack (rep by linkedlist) of nodes to search next
        LinkedList<Node> potentials = new LinkedList<>();

        //set of nodes which have already been visited
        HashSet<Node> visited = new HashSet<>();

        //map of what nodes came from where
        HashMap<Node, Node> cameFrom = new HashMap<>();

        //start searching at start node
        potentials.push(start);

        //while there are still potential nodes to search
        while(!potentials.isEmpty()){
            Node current = potentials.peek();

            if(current.equals(end)){
                //success case
                Path path = new Path();
                path.add(start);
                path.add(reconstructPath(cameFrom, current));
                return path;
            }

            //move to current (pop off stack and add to visited)
            current = potentials.pop();
            visited.add(current);

            String[] neighborIds = algoEdb.getNeighbors(current.get("id"));

            //for each neighbor, add to potentials if
            //it hasnt been visited and isn't already a potential
            for(String neighborId : neighborIds){
                Node neighbor = algoEdb.getNode(neighborId);
                if(!visited.contains(neighbor) && !potentials.contains(neighbor)){
                    cameFrom.put(neighbor, current);
                    potentials.push(neighbor);
                }
            }
        }
        //failure case
        //end not found
        return null;
    }
}