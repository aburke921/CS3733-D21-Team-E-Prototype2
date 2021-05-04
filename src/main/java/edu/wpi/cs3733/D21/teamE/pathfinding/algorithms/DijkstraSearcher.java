package edu.wpi.cs3733.D21.teamE.pathfinding.algorithms;

import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.map.Path;

import java.util.*;

public class DijkstraSearcher extends Searcher {

    private String type;

    /**
     * constructor
     */
    public DijkstraSearcher(String _type){
        super();
        this.type = _type;
    }

    @Override
    public Path search(Node start, Node end){

        PriorityQueue<Node> potentials = new PriorityQueue<>();
        HashMap<Node, Double> prevCost = new HashMap<>();
        HashMap<Node, Node> cameFrom = new HashMap<>();

        Double zero = Double.valueOf(0);
        prevCost.put(start, zero);
        start.setCost(zero);

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

            List<String> neighbors = current.getNeighbors();

            for(String neighborId : neighbors){
                Node neighbor = getNode(neighborId);
                Double neighborCost = prevCost.get(current) + current.dist(neighbor);
                if(!prevCost.containsKey(neighbor) || neighborCost < prevCost.get(neighbor)){
                    prevCost.put(neighbor, neighborCost);
                    cameFrom.put(neighbor, current);
                    neighbor.setCost(neighborCost);

                    //remove and re insert because value has been updated
                    potentials.remove(neighbor);
                    potentials.add(neighbor);
                }
            }
        }

        //failure case
        return null;
    }
}
