package edu.wpi.TeamE.algorithms.pathfinding;

import java.util.*;

import edu.wpi.TeamE.algorithms.*;
import edu.wpi.TeamE.algorithms.pathfinding.constraints.SearchConstraint;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.database.makeConnection;

/**
 * Abstract Searcher Class for Pathfinding API
 * On creation can be initialized to A* or DFS (already implemented) or others that can be added later
 */
class Searcher {

    private SearchConstraint type;

    //this is a local cache of nodes which have been worked with in the past
    private HashMap<String, Node> graph;

    private makeConnection con;

    /**
     * Super constructor, initializes cache
     */
    public Searcher(){
        graph = new HashMap<>();
        con = makeConnection.makeConnection();

        refreshGraph();
    }

    public void setType(SearchConstraint _type){
        type = _type;
    }

    public boolean isExcluded(Node node){
        return type.isExcluded(node);
    }

    public void refreshGraph(){
        ArrayList<Edge> edges = DB.getAllEdges();
        ArrayList<Node> nodes = DB.getAllNodes();

        for(Node node : nodes){
            graph.put(node.get("id"), node);
        }

        for(Edge edge : edges){
            graph.get(edge.getNode(0)).addNeighbor(edge.getNode(1));
            graph.get(edge.getNode(1)).addNeighbor(edge.getNode(0));
        }
    }

    /**
     * @param nodeId the Id of the node you wish to receive
     * @return the requested node
     */
    public Node getNode(String nodeId){
        return graph.get(nodeId);
    }

    /**
     * @param nodeId The Id of the node you want to get the neighbors of
     * @return the neighbors of that node
     */
    public List<String> getNeighbors(String nodeId){
        return graph.get(nodeId).getNeighbors();
    }

    public Path search(String startId, String endId){
        Node start = getNode(startId);
        Node end = getNode(endId);
        return search(start, end);
    }

    /**
     * Generic Search method for UI
     * Searches between Start and End Nodes for path
     * Searching algorithm can be A* or DFS (more can be added in the future)
     *
     * @param start The NodeID of the start node as a string
     * @param end   The NodeID of the end node as a string
     * @return The path from Start to End as a Path (basically a LinkedList)
     */
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

            List<String> neighbors = getNeighbors(current.get("id"));

            for(String neighborId : neighbors){
                Node neighbor = getNode(neighborId);
                Double neighborCost = prevCost.get(current) + current.dist(neighbor);
                if(!prevCost.containsKey(neighbor) || neighborCost < prevCost.get(neighbor)){
                    prevCost.put(neighbor, neighborCost);
                    cameFrom.put(neighbor, current);
                    neighbor.setCost(neighborCost + neighbor.dist(end));

                    //remove and re insert because value has been updated
                    potentials.remove(neighbor);
                    potentials.add(neighbor);
                }
            }
        }

        //failure case
        return null;
    }

    public Path search(Object start, Object end){
        if(start instanceof String && end instanceof String){
            return search((String)start, (String)end);
        } else if(start instanceof Node && end instanceof Node){
            return search((Node)start, (Node)end);
        } else {
            return null;
        }
    }

    public Path search(List stops){
        Path fullPath = new Path();
        Iterator itr = stops.iterator();

        if(itr.hasNext()){
            Object prev = itr.next();
            while(itr.hasNext()){
                Object current = itr.next();
                Path leg = search(prev, current);
                if(fullPath.getEnd().equals(leg.getStart())){
                    leg.pop();
                }
                fullPath.add(leg);
                prev = current;
            }
            return fullPath;
        } else {
            return null;
        }
    }

    public Path searchAlongPath(Path route, String stopType){
        List<Node> stops = DB.getAllNodesByType(stopType);
        Node start = route.getStart();
        Node end = route.getEnd();

        Path shortestSF = new Path();
        for(Node stop : stops){
            Path path = search(start.get("id"), stop.get("id"));
            Path leg2 = search(stop.get("id"), end.get("id"));
            leg2.pop();
            path.add(leg2);

            if(shortestSF.isEmpty() || path.getPathLength() < shortestSF.getPathLength()){
                shortestSF = path;
            }
        }

        System.out.printf("Added %f length\n", shortestSF.getPathLength() - route.getPathLength());

        return shortestSF;
    }

    public Node findNearest(Node location, String stopType){
        List<Node> stops = DB.getAllNodesByType(stopType);
        Node nearestSF = null;
        Path shortestSF = new Path();
        for(Node stop : stops){
            Path p = search(location, stop);
            if(shortestSF.isEmpty() || p.getPathLength() < shortestSF.getPathLength()){
                shortestSF = p;
                nearestSF = stop;
            }
        }
        return nearestSF;
    }



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