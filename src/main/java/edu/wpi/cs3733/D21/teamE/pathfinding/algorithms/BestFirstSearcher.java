package edu.wpi.cs3733.D21.teamE.pathfinding.algorithms;

import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.map.Path;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BestFirstSearcher extends Searcher {

    /**
     * constructor
     */
    public BestFirstSearcher(){

    }

    @Override
    public Path search(Node start, Node end){

        Queue<Node> potentials = new LinkedList<>();
        HashMap<Node, Node> cameFrom = new HashMap<>();

        potentials.add(start);

        while(!potentials.isEmpty()){
            Node current = potentials.poll();
            if(current.equals(end)){
                //success case
                Path path = new Path();
                path.add(start);
                path.add(reconstructPath(cameFrom, current));
                return path;
            } else if(isExcluded(current)){
                continue;
            }

            List<String> neighbors = getNeighbors(current.get("id"));

            for(String neighborId : neighbors){
                Node neighbor = getNode(neighborId);
                if(!cameFrom.containsKey(neighbor)){
                    cameFrom.put(neighbor, current);
                    potentials.add(neighbor);
                }
            }
        }

        //failure case
        return null;
    }
}
