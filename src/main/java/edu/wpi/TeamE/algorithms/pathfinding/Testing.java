package edu.wpi.TeamE.algorithms.pathfinding;

import edu.wpi.TeamE.algorithms.database.algoCSV;
import edu.wpi.TeamE.databases.makeConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Testing {

    public static void main(String[] args){
        System.out.println("STARTING UP!!!");
        makeConnection connection = makeConnection.makeConnection();
        System.out.println("Connected to the DB");
        File nodes = new File("src/main/resources/edu/wpi/TeamE/csv/bwEnodes.csv");
        File edges = new File("src/main/resources/edu/wpi/TeamE/csv/bwEedges.csv");
        try {
            connection.deleteAllTables();
            connection.createTables();
            connection.populateTable("node", nodes);
            connection.populateTable("hasEdge", edges);
            System.out.println("Tables were reset");
        } catch (Exception e) {
            connection.createTables();
            connection.populateTable("node", nodes);
            connection.populateTable("hasEdge", edges);
            System.out.println("Tables were created and populated");
        }


        boolean con;
        do {
            //eWALK01801
            //ePARK00101

            Scanner io = new Scanner(System.in);
            System.out.print("Please enter the Origin Node: ");
            String startNode = io.nextLine();

            System.out.print("Please enter the Destination Node: ");
            String endNode = io.nextLine();

            DFSSearch dfs = new DFSSearch();
            Searcher aStar = new AStarSearch();
            Path path = dfs.search(startNode, endNode);
            path.print("id");
            Path optimalPath = dfs.search(startNode, endNode);
            optimalPath.print("id");

            System.out.print("Search Another Path? [y/n]: ");

            con = io.nextLine().equalsIgnoreCase("y");
        } while (con);
    }
}
