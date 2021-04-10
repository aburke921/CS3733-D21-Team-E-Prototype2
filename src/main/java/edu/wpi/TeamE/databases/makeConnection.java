package edu.wpi.TeamE.databases;

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



}
