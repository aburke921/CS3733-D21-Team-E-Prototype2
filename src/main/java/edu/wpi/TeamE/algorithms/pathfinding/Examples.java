package edu.wpi.TeamE.algorithms.pathfinding;

import edu.wpi.TeamE.algorithms.Path;
import edu.wpi.TeamE.databases.makeConnection;

import java.io.File;

public class Examples {

    public static void main(String[] args){
        System.out.println("STARTING UP!!!");
        makeConnection connection = makeConnection.makeConnection();
        System.out.println("Connected to the DB");
        File nodes = new File("src/main/resources/edu/wpi/TeamE/csv/bwEnodes.csv");
        File edges = new File("src/main/resources/edu/wpi/TeamE/csv/bwEedges.csv");
        try {
            // connection.deleteAllTables();
            connection.createTables();
            connection.populateTable("node", nodes);
            connection.populateTable("hasEdge", edges);
            System.out.println("Tables were created");
        } catch (Exception e) {
            System.out.println("Tables already there");
//			connection.createTables();
//			connection.populateTable("node", nodes);
//			connection.populateTable("hasEdge", edges);
//			System.out.println("Tables were created and populated");
        }


        String startNode = "ePARK00101";
        String endNode = "FEXIT00201";

        //the pathfinding API is now entirely wrapped up in the SearchContext class
        //this will allow you to flexibly configure what kind of search you wish to execute
        //if you want vanilla a* you can instantiate like this
        SearchContext search = new SearchContext();
        //or like this (these lines (^v) are equivalent)
        search = new SearchContext(new Searcher(), "VANILLA");
        //safe search will avoid the emergency room
        search = new SearchContext(new DFSSearcher(), "SAFE");
        //handicap search will avoid stairs
        //also nothing is case sensitive
        search = new SearchContext(new Searcher(), "handicap");

        //if you don't want to instantiate a new one every time (recommended, better for memory)
        //you can specify a new algorithm or new conditions like this
        search.setAlgo(new DFSSearcher());
        search.setAlgo(new Searcher());

        search.setConstraint("HANDICAP");
        search.setConstraint("HaNdICap");
        search.setConstraint("SAFE");
        search.setConstraint("VANILLA");

        //searching for a new path is the same
        Path p = search.search(startNode, endNode);
        p.print("id");
        for (String dir : p.makeDirectionsWithDist()) {
            System.out.println(dir);
        }
        System.out.println();

        p = search.search("ePARK00101", "eWALK00101");
        p.print("id");
        for (String dir : p.makeDirectionsWithDist()) {
            System.out.println(dir);
        }
        System.out.println();

        // Directions Testing
        p = search.search("ARETL00101", "ADEPT00102");
        p.print("id");
        for (String dir : p.makeDirectionsWithDist()) {
            System.out.println(dir);
        }
        System.out.println();

        search.setConstraint("HANDICAP");
        p = search.search("ARETL00101", "ADEPT00102");
        p.print("id");
        for (String dir : p.makeDirectionsWithDist()) {
            System.out.println(dir);
        }


        //however it should be mentioned that search algorithms
        //use a local copy of the db, and this local copy does not update when the db is edited
        //but it pulls in fresh data on every instantiation of Searcher or DFSSearch
        //so when using these objects make sure the algorithm instantiations aren't too persistent
        //or you have the option of saying
        search.refresh();
        //if you're not sure if a change has been made to the db
        //this method pulls in the whole node table and the whole edge table
        //so should be used only if necessary

        //additionally it should be noted that using search with constraints leads
        //to a higher likelihood of search returning null (i.e. no path being found)
    }
}
