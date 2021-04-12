package edu.wpi.TeamE.databases;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
import java.io.*;
import java.sql.*;
import java.util.Properties;

public class makeConnection {

    Connection connection;


    /**
     * Constructor makes the database connection
     */
    public makeConnection(){
        // Initialize DB
        System.out.println("Starting connection to Apache Derby\n");
        try {

            //Makes it so a username and password (hardcoded) is needed to access the database data
            Properties props = new Properties();
            props.put("user", "admin");
            props.put("password", "admin");

            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            try {
                this.connection = DriverManager.getConnection("jdbc:derby:BWDB;create=true", props);
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates node and hasEdge tables
     * try/catch phrase set up in case the tables all ready exist
     */
    public void createTables() {

        try {
        /*
        Query Performed:

        CREATE TABLE node (
            nodeID    varchar(31) primary key,
            xCoord    int not null,
            yCoord    int not null,
            floor     varchar(5) not null,
            building  varchar(20),
            nodeType  varchar(10),
            longName  varchar(50),
            shortName varchar(35),
            unique (xCoord, yCoord, floor));

        CREATE TABLE hasEdge (
            edgeID    varchar(63) primary key,
            startNode varchar(31) not null references node (nodeID),
            endNode   varchar(31) not null references node (nodeID),
            unique (startNode, endNode));

         */

            Statement stmt = this.connection.createStatement();
            stmt.execute(
                    "create table node"
                            + "("
                            + "    nodeID    varchar(31) primary key,"
                            + "    xCoord    int not null,"
                            + "    yCoord    int not null,"
                            + "    floor     varchar(5) not null,"
                            + "    building  varchar(20),"
                            + "    nodeType  varchar(10),"
                            + "    longName  varchar(100),"
                            + "    shortName varchar(100),"
                            + "    unique (xCoord, yCoord, floor)"
                            + ")");

        } catch (SQLException e) {
            // e.printStackTrace();
        }

        try {

            Statement stmt = connection.createStatement();
            stmt.execute(
                    "create table hasEdge"
                            + "("
                            + "    edgeID    varchar(63) primary key,"
                            + "    startNode varchar(31) not null references node (nodeID),"
                            + "    endNode   varchar(31) not null references node (nodeID), "
                            + "    length    float, "
                            + "    unique (startNode, endNode)"
                            + ")");

            // Needs a way to calculate edgeID, either in Java or by a sql trigger
            // Probably in Java since it's a PK

        } catch (SQLException e) {
            // e.printStackTrace();
        }

//        try {
//
//            Statement stmt = connection.createStatement();
//            String sqlQuery = "CREATE TRIGGER calculateLength " +
//                    "AFTER INSERT ON hasEdge " +
//                    "REFERENCING NEW AS newRow FOR EACH ROW MODE DB2SQL" +
//                    "    newRow.length := SELECT sqrt(((miniX - maxiX) * (miniX - maxiX)) + ((miniY - maxiY) * (miniY - maxiY))) " +
//                    "    FROM (SELECT min(xCoord) as miniX, max(xCoord) as maxiX, min(yCoord) as miniY, max(yCoord) as maxiY " +
//                    "        FROM (SELECT nodeID, xCoord, yCoord " +
//                    "        FROM node " +
//                    "        WHERE nodeID = newRow.startNode OR nodeID = newRow.endNode)); ";
//
//
//
//            stmt.execute(sqlQuery);
//
//            // Needs a way to calculate edgeID, either in Java or by a sql trigger
//            // Probably in Java since it's a PK
//
//        } catch (SQLException e) {
//             e.printStackTrace();
//        }


    }


    /**
     * Deletes node and hasEdges table
     * try/catch phrase set up in case the tables all ready do not exist
     */
    public void deleteAllTables() {

        try {
            Statement stmt = this.connection.createStatement();
//            stmt.execute("drop trigger calculateLength");
            stmt.execute("drop table hasEdge");
            stmt.execute("drop table node");
            stmt.close();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

    /**
     * Reads csv & inserts into table
     * @param tableName name of the table that needs to be populated
     * @param csvFileName name of the csv file that is going to be read
     */
    public void populateTable(String tableName, String csvFileName) {

        try {
            // creates a file with the file name we are looking to read
            File file = new File(csvFileName);
            // used to read data from a file
            FileReader fr = new FileReader(file);

            // used to read the text from a character-based input stream.
            BufferedReader br = new BufferedReader(fr);

            String line;
            String[] tempArr;

            // reads first line (this is the header of each file and we don't need it)
            br.readLine();

            // if there is something in the file (after line 1)
            while ((line = br.readLine()) != null) {

                // adds arguments into the array separated by the commas ("," is when it knows the next
                // index)
                tempArr = line.split(",");

                String sqlQuery = "";
                if (tableName.equals("node")) {
                    sqlQuery =
                            "INSERT INTO node VALUES ("
                                    + "'"
                                    + tempArr[0]
                                    + "',"
                                    + Integer.valueOf(tempArr[1])
                                    + ","
                                    + Integer.valueOf(tempArr[2])
                                    + ",'"
                                    + tempArr[3]
                                    + "',"
                                    + " '"
                                    + tempArr[4]
                                    + "',"
                                    + " '"
                                    + tempArr[5]
                                    + "',"
                                    + " '"
                                    + tempArr[6]
                                    + "',"
                                    + " '"
                                    + tempArr[7]
                                    + "')";
                }
                if (tableName.equals("hasEdge")) {
                    sqlQuery =
                            "INSERT INTO hasEdge VALUES ('"
                                    + tempArr[0]
                                    + "', '"
                                    + tempArr[1]
                                    + "', '"
                                    + tempArr[2]
                                    + "', NULL"
                                    + ")";
                }


                try {
                    Statement stmt = this.connection.createStatement();

                    //executes the SQL insert statement (inserts the data into the table)
//                    if(tableName.equals("hasEdge")){
//                        System.out.println("before");
//                    writer.flush();
//                    }
                    stmt.execute(sqlQuery);



//                    System.out.println("after");
//                    writer.flush();

                    if(tableName.equals("hasEdge")){
                        System.out.println("Calling addLength");
                        addLength(tempArr[1], tempArr[2]);
                        System.out.println("after calling addLength");
                    }

                    stmt.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                    //System.err.println("calling addLength try/catch error");
                }
            }

            // closes the BufferedReader
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Calculates edge distances
     * calculates the distance between two nodes.
     * - Given searchType = 1, the method will calculate the distance
     * of all the edges that have the given nodeID as its startNode.
     * - Given searchType = 2, the method will calcualte the distance
     * of all the edges that have the given nodeID as its endNode.
     * - Given searchType = 3, the method will calculate the distance
     * of all the edges in the hasEdge table.
     * @param searchType is the type of query that is needed on the data.
     * @param nodeID is the node the function uses to calculate how far other nodes are to it (other nodes that are its edge pair)
     */
    public void lengthFromEdges(int searchType, String nodeID) {
//create this so it is a trigger
//need another function that actually returns the stuff algo are looking for


        try {

            Statement stmt = this.connection.createStatement();

            String sqlQuery =
                    "SELECT startNode, endNode, sqrt(((startX - endX) * (startX - endX)) + ((startY - endY) * (startY - endY))) AS distance "
                            + "FROM (SELECT startNode, endNode, node.xCoord AS startX, node.yCoord AS startY, endX, endY "
                            + "FROM node,(SELECT startNode, endNode, xCoord AS endX, yCoord AS endY "
                            + "FROM node, (SELECT startNode, endNode "
                            + "FROM hasEdge ";

            String restOfQuery = "";

            // Gets length for the given startNode
            if (searchType == 1) {
                restOfQuery =
                        "WHERE startNode = '"
                                + nodeID
                                + "') wantedEdges "
                                + "WHERE nodeID = startNode) wantedEdgesStartInfo "
                                + "WHERE node.nodeID = wantedEdgesStartInfo.endNode) desiredTable";

                sqlQuery = sqlQuery + restOfQuery;
            }

            //Gets length for the given endNode
            if (searchType == 2) {
                restOfQuery =
                        "WHERE endNode = '"
                                + nodeID
                                + "') wantedEdges "
                                + "WHERE nodeID = startNode) wantedEdgesStartInfo "
                                + "WHERE node.nodeID = wantedEdgesStartInfo.endNode) desiredTable";

                sqlQuery = sqlQuery + restOfQuery;
            }
            //Gets length for the whole table
            if (searchType == 3) {
                restOfQuery =
                        ") wantedEdges "
                                + "WHERE nodeID = startNode) wantedEdgesStartInfo "
                                + "WHERE node.nodeID = wantedEdgesStartInfo.endNode) desiredTable";

                sqlQuery = sqlQuery + restOfQuery;
            }

            ResultSet rset = stmt.executeQuery(sqlQuery);

            while (rset.next()) {

                String startNode = rset.getString("startNode");
                String endNode = rset.getString("endNode");
                double distance = rset.getDouble("distance");

                System.out.println(
                        "startNode: " + startNode + "    endNode: " + endNode + "    distance: " + distance);
                System.out.println();
            }

            rset.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLException e <-- located in the main big try/catch statement of lengthFromEdges");
        }
    }

    public void addLength(String startNode, String endNode) {

        String sqlQuery;
        int startX = 0;
        int startY = 0;
        int endX = 0;
        int endY = 0;

        ResultSet rset;
        Statement stmt;

        try {

//            System.out.println("First try/catch");
            stmt = this.connection.createStatement();

            sqlQuery = "SELECT xCoord, yCoord FROM node WHERE nodeID = '" + startNode + "'";

            //executes the SQL insert statement (inserts the data into the table)
            rset = stmt.executeQuery(sqlQuery);

            while (rset.next()) {

                startX = rset.getInt("xCoord");
                startY = rset.getInt("yCoord");
            }
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            // e.printStackTrace();
            System.err.println("WHERE nodeID = startNode try/catch failed");
        }

        try{
//            System.out.println("Second try/catch");
            stmt = this.connection.createStatement();

            sqlQuery = "SELECT xCoord, yCoord FROM node WHERE nodeID = '" + endNode + "'";

            //executes the SQL insert statement (inserts the data into the table)
            rset = stmt.executeQuery(sqlQuery);

            while (rset.next()) {

                endX = rset.getInt("xCoord");
                endY = rset.getInt("yCoord");
            }
            rset.close();
            stmt.close();

        } catch (SQLException e) {
            // e.printStackTrace();
            System.err.println("WHERE nodeID = endNode try/catch failed");
        }


        try{
//            System.out.println("Third try/catch");
            double length;

            double xLength = Math.pow((startX - endX), 2);
            double yLength = Math.pow((startY - endY), 2);

            length = Math.sqrt(xLength + yLength);


            stmt = this.connection.createStatement();

            sqlQuery = "UPDATE hasEdge SET length = " + length + " WHERE startNode = '" + startNode + "' AND endNode = '" + endNode +"'";

            //executes the SQL insert statement (inserts the data into the table)
            stmt.executeUpdate(sqlQuery);

            stmt.close();

        }catch(SQLException e){
            System.err.println("UPDATE try/catch failed");
        }



    }


    public static void main(String[] args){
        makeConnection connection = new makeConnection();

        System.out.println("made it here!");
        connection.deleteAllTables();
        connection.createTables();
        connection.populateTable("node", "nodes.csv");
        connection.populateTable("hasEdge", "edges.csv");

    }
}
