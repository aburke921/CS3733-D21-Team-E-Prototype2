package edu.wpi.TeamE.databases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class makeConnection {

    Connection connection;

    //Constructor
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
                connection = DriverManager.getConnection("jdbc:derby:/Users/ashley/Documents/IntelliJ_Projects/Algo2/Database;create=true", props);
            } catch (SQLException e) {
                // e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


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
                            + "    longName  varchar(50),"
                            + "    shortName varchar(35),"
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
                            + "    endNode   varchar(31) not null references node (nodeID),"
                            + "    unique (startNode, endNode)"
                            + ")");

            // Needs a way to calculate edgeID, either in Java or by a sql trigger
            // Probably in Java since it's a PK

        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }


    public void deleteAllTables() {

        try {
            Statement stmt = this.connection.createStatement();
            stmt.execute("drop table hasEdge");
            stmt.execute("drop table node");
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }


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
                if (tableName == "node") {
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
                if (tableName == "hasEdge") {
                    sqlQuery =
                            "INSERT INTO hasEdge VALUES ('"
                                    + tempArr[0]
                                    + "', '"
                                    + tempArr[1]
                                    + "', '"
                                    + tempArr[2]
                                    + "')";
                }


                try {
                    Statement stmt = this.connection.createStatement();

                    //executes the SQL insert statement (inserts the data into the table)
                    stmt.execute(sqlQuery);
                } catch (SQLException e) {
                    // e.printStackTrace();
                }
            }

            // closes the BufferedReader
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }



}
