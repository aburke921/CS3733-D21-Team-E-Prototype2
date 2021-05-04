package edu.wpi.cs3733.D21.teamE.pathfinding.algorithms;

import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.map.Path;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class BestFirstSearcher extends Searcher {

    private String type;
    /**
     * constructor
     */


    public BestFirstSearcher(String _type){
        super();
        this.type = _type;

    }

    @Override
    public Path search(Node start, Node end){

        System.out.println("search not of BFS is called");

        List<Node> visited = new LinkedList<>();

        Queue<Node> potentials = new LinkedList<>();
        HashMap<Node, Node> cameFrom = new HashMap<>();

        potentials.add(start);

        while(!potentials.isEmpty()){

            Node current = potentials.poll();

            System.out.println("current: " + current);

            if(current.equals(end)){
                //success case
                Path path = new Path();
                path.add(start);

                System.out.println("Build the path");
                path.add(reconstructPath(cameFrom, current));

                System.out.println("Path FOUND!");
                return path;
            } else if(isExcluded(current)){
                continue;
            }

            List<String> neighbors = current.getNeighbors();

            for(String neighborId : neighbors){
                Node neighbor = getNode(neighborId);
                if(!potentials.contains(neighbor) && /*!visited.contains(neighbor) &&*/ current.compareTo(neighbor) != 0){
                    //visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    potentials.add(neighbor);
                }
            }
        }

        System.out.println("PATH NOT found!");

        //failure case
        return null;
    }
}
