package edu.wpi.TeamE.algorithms.pathfinding;

import edu.wpi.TeamE.algorithms.database.algoCSV;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Testing {

    public static void main(String[] args){
        try {
            algoCSV.ReadCSVs();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        boolean cont = false;

        do {
            Scanner io = new Scanner(System.in);
            System.out.print("Please enter the Origin Node: ");
            String startNode = io.nextLine();

            System.out.print("Please enter the Destination Node: ");
            String endNode = io.nextLine();

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
            System.out.print("\nDFS ");
            path.print("id", "longName");
            //or leave it blank and it will default to all of them
            System.out.print("Optimal ");
            optimalPath.print("id");

            System.out.print("\nWould you like to test another path?\n");
            String answer = io.nextLine();

            if(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                cont = true;
                System.out.print("\n");
            } else {
                cont = false;
            }
        } while (cont);

    }
}
