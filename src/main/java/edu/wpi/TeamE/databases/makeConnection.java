package edu.wpi.TeamE.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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


}
