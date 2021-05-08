package edu.wpi.cs3733.D21.teamE.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ToDoDB {

	static Connection connection = makeConnection.makeConnection().connection;

	public static int createToDoTable() {
		try (PreparedStatement prepState = connection.prepareStatement("Create Table ToDo " +
				"( " +
				"ToDoID           int Primary Key, " +
				"userID           int References userAccount Not Null," +
				"title            varchar(63) Not Null," +
				"status           int Not Null Default 1," + // normal/complete/archived/deleted/...
				"priority         int Not Null Default 0," + // default 0 (none), 1/2/3 (low/mid/high)
				//optional:
				"scheduledDate    Varchar(31) Default Null," + // format:
				"scheduledTime    Varchar(31) Default Null," + // format: 23:17:00
				"nodeID           varchar(31) References node," +
				"detail           varchar(1023)," +
				"expectedLength   Varchar(31)," + // how long it would take, format: 23:17:00
				"notificationDate Varchar(31)," + // eg. remind me 2 days before this (send email)
				"notificationTime Varchar(31)" + // format: 23:17:00 eg. remind me 30 mins before this (send email)
				")")) {
			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("error creating ToDo table");
		}
	}
}
