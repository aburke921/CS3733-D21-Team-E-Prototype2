package edu.wpi.TeamE.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class functions {


    public static Connection makeConnection() {

        // Initialize DB
        System.out.println("Starting connection to Apache Derby\n");
        Connection connection;
        try {

            Properties props = new Properties();
            props.put("user", "admin");
            props.put("password", "admin");

            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            // remove :memory to have persistent database
            try {
                connection = DriverManager.getConnection("jdbc:derby:/Users/ashley/Documents/IntelliJ_Projects/Algo2/Database;create=true", props);

//                refactorTables(connection);

                // deleteAllTables(connection);

                return connection;

            } catch (SQLException e) {
                // e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
