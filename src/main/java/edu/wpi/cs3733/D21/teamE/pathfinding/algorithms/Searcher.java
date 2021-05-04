package edu.wpi.cs3733.D21.teamE.pathfinding.algorithms;

import java.util.*;

import edu.wpi.cs3733.D21.teamE.map.Edge;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.map.Path;
import edu.wpi.cs3733.D21.teamE.pathfinding.constraints.SearchConstraint;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.database.makeConnection;

/**
 * Abstract Searcher Class for Pathfinding API
 * On creation can be initialized to A* or DFS (already implemented) or others that can be added later
 */
public abstract class Searcher {

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

    public Path search(String startId, String endId){
        Node start = getNode(startId);
        Node end = getNode(endId);
        return search(start, end);
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

    /**
     * Generic Search method for UI
     * Searches between Start and End Nodes for path
     * Searching algorithm can be A* or DFS (more can be added in the future)
     *
     * @param start The NodeID of the start node as a string
     * @param end   The NodeID of the end node as a string
     * @return The path from Start to End as a Path (basically a LinkedList)
     */
    public abstract Path search(Node start, Node end);

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
    protected Path reconstructPath(HashMap<Node, Node> cameFrom, Node end) {
        LinkedList<Node> stack = new LinkedList<>();

        //int cur = 0;

        //push onto stack
        for(Node current = end; current != null && cameFrom.containsKey(current);
                current = cameFrom.get(current)){
            stack.push(current);

            System.out.println(current.getX() + "." + current.getY());
//
//            cur++;
//
//            if (cur > 40){
//                break;
//            }
        }

        Path path = new Path();
        //pop off of stack onto linked list
        while(!stack.isEmpty()){
            path.add(stack.pop());
        }

        return path;
    }
}