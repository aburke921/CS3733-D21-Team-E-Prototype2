package edu.wpi.TeamE.databases;


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
                System.err.println("error with the DriverManager");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("error with the EmbeddedDriver class.forName thing");
        }

    }

    /**
     * Creates node and hasEdge tables
     * try/catch phrase set up in case the tables all ready exist
     */
    public void createTables() {

        try {
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
            System.err.println("error creating node table");
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
            System.err.println("error creating hasEdge table");
        }
    }


    /**
     * Deletes node and hasEdges table
     * try/catch phrase set up in case the tables all ready do not exist
     */
    public void deleteAllTables() {

        try {
            Statement stmt = this.connection.createStatement();
            stmt.execute("drop table hasEdge");
            stmt.execute("drop table node");
            stmt.close();
        } catch (SQLException e) {
            // e.printStackTrace();
            System.err.println("deleteAllTables() not working");
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
//                    e.printStackTrace();
                    System.err.println("populateTable() inner try/catch error");
                }
            }

            // closes the BufferedReader
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("populateTable() outer try/catch error");
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

            stmt = this.connection.createStatement();

            sqlQuery = "SELECT xCoord, yCoord FROM node WHERE nodeID = '" + startNode + "'";

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
