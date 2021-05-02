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
				"title            varchar(31)                        Not Null, " +
				"status           int                                Not Null, " + // normal/complete/archived/deleted/...
				"priority         int                                Not Null, " + // 1/2/3/...
				"scheduledTime    Time, " + // nullable, edited frequently
				"nodeID           varchar(31) References node, " + // nullable
				"requestID        int References requests, " + // nullable, auto-add to list when assigned
				"detail           varchar(255), " +
				"expectedTime     Time, " + // how long it would take
				"notificationTime Time  " + // eg. remind me 30 mins before this (send email)
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("error creating ToDo table");
		}
	}
}
