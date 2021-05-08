package edu.wpi.cs3733.D21.teamE.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ToDoDB {

	static Connection connection = makeConnection.makeConnection().connection;

	public static void createToDoTable() {
		String query = "Create Table ToDo " +
				"( " +
				"ToDoID           int Primary Key, " +
				"userID           int References userAccount Not Null, " +
				"title            varchar(31)                Not Null, " +
				"status           int                        Not Null, " +
				"priority         int                        Not Null, " +
				"scheduledDate    Varchar(31), " +
				"scheduledTime    Varchar(31), " +
				"nodeID           varchar(31) References node, " +
				"detail           varchar(255), " +
				"expectedLength   Time, " +
				"notificationDate Varchar(31), " +
				"notificationTime Varchar(31) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("error creating ToDo table");
		}
	}
}
