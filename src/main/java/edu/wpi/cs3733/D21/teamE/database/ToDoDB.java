package edu.wpi.cs3733.D21.teamE.database;

import edu.wpi.cs3733.D21.teamE.scheduler.ToDo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ToDoDB {

	static Connection connection = makeConnection.makeConnection().connection;

	public static int createToDoTable() {
		try (PreparedStatement prepState = connection.prepareStatement("Create Table ToDo " +
				"( " +
				"ToDoID           int Primary Key, " +
				"userID           int References userAccount Not Null," +
				"title            varchar(63) Not Null," +
				"status           int Not Null Default 1," + // default 1 (normal), 10/0 (complete/deleted)
				"priority         int Not Null Default 0," + // default 0 (none), 1/2/3 (low/mid/high)
				//optional:
				"scheduledDate    Varchar(31) Default Null," + // format:
				"scheduledTime    Varchar(31) Default Null," + // format: 23:17:00
				"nodeID           varchar(31) References node," +
				"detail           varchar(1023)," +
				"expectedLength   Varchar(31)," + // how long it would take, format: 23:17:00
				"notificationDate Varchar(31)," + // eg. remind me 2 days before this (send email)
				"notificationTime Varchar(31)" +  // format: 23:17:00 eg. remind me 30 mins before this (send email)
				")")) {
			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("error creating ToDo table");
			return 0;
		}
		return 1;
	}

	/**
	 * adds a ToDo_row to the ToDo_table in the database
	 * @param userID this is the userID of the person associated with the ToDo_item
	 * @param title  this is the todo_name of the custom todo_item
	 * @return true if one line changed successfully, false otherwise
	 */
	public static boolean addCustomToDo(int userID, String title) {
		try (PreparedStatement preparedStatement = connection.prepareStatement("Insert Into ToDo " +
				"(ToDoID, userID, Title) " +
				"Values ((Select Max(ToDoID) From ToDo), ?, ?)")) {
			preparedStatement.setInt(1, userID);
			preparedStatement.setString(2, title);
			return preparedStatement.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into ToDo table in addCustomToDo()");
			return false;
		}
	}/*
	@param ToDoType   1 for clean, custom todos with no associated appointment/requests
	                     2 for service requests, 3 for appointments
	switch (ToDoType) {
		case 1: // clean, custom todos with no associated appointment/requests
			break;
		case 2: // service requests
			break;
		case 3: // appointments
			break;
		default:
			System.err.println("Not supported todo_type in addCustomToDo()");
			break;
	}*/

	/**
	 * Updates an entered ToDo_item with the following fields, input null to ignore String attributes and -1 to ignore int attributes
	 * @param ToDoID           mandatory
	 * @param userID           mandatory, changes the owner of the item to this userID, use App.userID if no change
	 * @param status           default 1 (normal), 10/0 (complete/deleted)
	 * @param priority         default 0 (none), 1/2/3 (low/mid/high)
	 * @param scheduledDate    format:
	 * @param scheduledTime    format: 23:17:00
	 * @param nodeID           has to exist in the node table
	 * @param detail           maximum 1023 characters
	 * @param expectedLength   how long it would take, format: 23:17:00
	 * @param notificationDate eg. remind me 2 days before this (send email)
	 * @param notificationTime format: 23:17:00 eg. remind me 30 mins before this (send email)
	 * @return true if one line changed successfully, false otherwise
	 */
	public static boolean updateToDo(int ToDoID, int userID, String title, int status, int priority, String scheduledDate, String scheduledTime, String nodeID, String detail, String expectedLength, String notificationDate, String notificationTime) {
		String sql = "Update ToDo Set userID = ?";
		if (title != null) {
			sql += ", title = ?";
		}
		if (status != -1) {
			sql += ", status = ?";
		}
		sql += " Where ToDoID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			int i = 1;
			if (title != null) {
				i++;
				preparedStatement.setString(i, title);
			}
			if (status != -1) {
				i++;
				preparedStatement.setInt(i, status);
			}
			preparedStatement.setInt(i + 1, ToDoID);
			return preparedStatement.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error updating ToDo ID: " + ToDoID + " in addCustomToDo()\nQuery: ");
			System.err.println(sql);
			return false;
		}
	}

	public static List<ToDo> getToDoList(int userID, String date) {
		return null;
	}
}
