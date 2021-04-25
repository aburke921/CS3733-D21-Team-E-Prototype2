package edu.wpi.cs3733d18.teamE.database;

import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class makeConnection {

	private static makeConnection con;
	public static Connection connection;


	private makeConnection() {

		System.out.println("Starting connection to Apache Derby");
		try {
			Properties props = new Properties();
			props.put("user", "admin");
			props.put("password", "admin");
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			try {
				connection = DriverManager.getConnection("jdbc:derby:BWDB;create=true", props);
				System.out.println("\nConnected to the DB");
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("Error with the DriverManager");
				System.err.println("\n\n\n---------- Did you connect to database with IntelliJ? ----------\n\n\n");
				return;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("error with the EmbeddedDriver class");
			return;
		}

		System.out.println("\nFor TESTING only: Deleting all tables");
		int numOfTableDeleted = DDL.allTableDelete();
		int dasd = RequestDB.addFloral();
		int dasda = UpdateDB.addFloral();
		if (numOfTableDeleted == 0 || numOfTableDeleted == 1) {
			System.out.println(numOfTableDeleted + " table Deleted");
		} else {
			System.out.println(numOfTableDeleted + " tables Deleted");
		}

		System.out.println("\nPopulate Tables from csv if table not exist");
		if (DDL.nodeTableCreate() == 1) {
			System.out.println("|--- node Table created, populating...");
			File nodes = new File("CSVs/MapEAllnodes.csv");
			DML.populateTable("node", nodes);
		} else {
			//System.out.println("|--- node table already exist");
		}
		if (DDL.hasEdgeTableCreate() == 1) {
			System.out.println("|--- hasEdge Table created, populating...");
			File edges = new File("CSVs/MapEAlledges.csv");
			DML.populateTable("hasEdge", edges);
		} else {
			//System.out.println("|--- node table already exist");
		}

		System.out.println("\nCreate Tables");
		int numOfTableCreated = DDL.allTableCreate();
		if (numOfTableCreated == 0 || numOfTableCreated == 1) {
			System.out.println(numOfTableCreated + " table Created");
		} else {
			System.out.println(numOfTableCreated + " tables Created");
		}

		//connection.addDemoData();
		System.out.println("Demo data added");
	}

	public static makeConnection getCon() {
		return SingletonHelper.con;
	}

	private static class SingletonHelper {
		private static final makeConnection con = new makeConnection();
	}

}
