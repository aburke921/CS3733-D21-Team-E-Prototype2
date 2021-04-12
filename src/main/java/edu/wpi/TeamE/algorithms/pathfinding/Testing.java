package edu.wpi.TeamE.algorithms.pathfinding;

import edu.wpi.TeamE.algorithms.database.algoCSV;

import java.io.FileNotFoundException;

public class Testing {

    public static void main(String[] args){
        try {
            algoCSV.ReadCSVs();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        String startNode = "eEXIT00101";
        String endNode = "ePARK00101";

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

    }
}
