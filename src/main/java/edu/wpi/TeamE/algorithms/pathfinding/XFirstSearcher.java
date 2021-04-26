package edu.wpi.TeamE.algorithms.pathfinding;

import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.algorithms.Path;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * DF Search Implementation
 * Contains specific implementation of DFS
 */
class XFirstSearcher extends Searcher {

    private String type;

    /**
     * @param _type has to equal "DFS" or "BFS"
     */
    public XFirstSearcher(String _type){
        super();
        type = _type;
    }

    /**
     * Searches for a Path from startId to endId
     * Utilizing the Depth First Search Algorithm
     * i.e. will continue along a path until it hits a dead end or finds its goal
     * @param startId the Node to begin Path at
     * @param endId the Node to end Path at
     * @return Path object representing the route from startId to endId
     */
    @Override
    public Path search(String startId, String endId){

        //get node info from database
        Node start = getNode(startId);
        Node end = getNode(endId);

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

            Node current;
            if(type.equalsIgnoreCase("BFS")){
                //this will make it do BFS
                current = potentials.removeFirst();
            } else {
                //this will make it do DFS (i think)
                current = potentials.removeLast();
            }

            if(current.equals(end)){
                //success case
                Path path = new Path();
                path.add(start);
                path.add(reconstructPath(cameFrom, current));
                return path;
            } else if (isExcluded(current)){
                continue;
            }

            //move to current (pop off stack and add to visited)
            visited.add(current);

            List<String> neighbors = getNeighbors(current.get("id"));
            //for each neighbor, add to potentials if
            //it hasnt been visited and isn't already a potential
            for(String neighborId : neighbors){
                Node neighbor = getNode(neighborId);
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