package edu.wpi.TeamE.algorithms.pathfinding;

import java.util.Iterator;

public class Examples {
    public static void main(String[] args){

        //getting these are outside the scope of this program
        String startNode = "eWALK01801";
        String endNode = "ePARK00501";

        //these are the objects you can use to search
        DFSSearch dfs = new DFSSearch();
        Searcher aStar = new AStarSearch();

        //the search returns a Path object
        //which is LinkedList of Nodes
        Path path = dfs.search(startNode, endNode);
        Path optimalPath = aStar.search(startNode, endNode);

        //you can print a path
        //you can specify which labels you want to see for each node
        //unfortunately this does not include coordinates
        path.print("id", "longName");
        //or leave it blank and it will default to all of them
        optimalPath.print();

        //you can iterate through a path like this
        Iterator<Node> nodeIter1 = path.iterator();
        while(nodeIter1.hasNext()){
            //this iterator will return a Node object
            //which is just a container for all the node info like id, floor, building, etc
            Node node = nodeIter1.next();
            String id = node.get("id");
            String floor = node.get("floor");
            String building = node.get("building");
            String type = node.get("type");
            String longName = node.get("longName");
            String shortName = node.get("shortName");
            //coordinates are ints so they have to be stored separate
            int xCoord = node.getX();
            int yCoord = node.getY();
        }
    }
}
