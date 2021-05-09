package edu.wpi.cs3733.D21.teamE.database;

import edu.wpi.cs3733.D21.teamE.scheduler.ToDo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

	public static int getMaxToDoID() {
		try (PreparedStatement prepState = connection.prepareStatement("Select Max(ToDoID) As maxToDoID From ToDo")) {
			ResultSet rSet = prepState.executeQuery();
			if (rSet.next()) {
				return rSet.getInt("maxToDoID");
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Did not get maxToDoID in getMaxToDoID()");
		}
		return 0;
	}

	/**
	 * adds a ToDo_row to the ToDo_table in the database
	 * @param userID this is the userID of the person associated with the ToDo_item
	 * @param title  this is the todo_name of the custom todo_item
	 * @return the ToDoID, 0 if failed
	 */
	public static int addCustomToDo(int userID, String title) {
		try (PreparedStatement preparedStatement = connection.prepareStatement("Insert Into ToDo " +
				"(ToDoID, userID, Title) Values (?, ?, ?)")) {
			preparedStatement.setInt(1, getMaxToDoID() + 1);
			preparedStatement.setInt(2, userID);
			preparedStatement.setString(3, title);
			if (preparedStatement.executeUpdate() == 1) {
				return getMaxToDoID();
			} else return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into ToDo table in addCustomToDo()");
			return 0;
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
	 * @param scheduledDate    format: 2021-05-08
	 * @param startTime        format: 23:17
	 * @param endTime          format: 01:30
	 * @param nodeID           has to exist in the node table
	 * @param detail           maximum 1023 characters
	 * @param notificationDate format: 2021-05-08 eg. remind me 2 days before this (send email)
	 * @param notificationTime format: 23:17 eg. remind me 30 mins before this (send email)
	 * @return true if one line changed successfully, false otherwise
	 */
	public static boolean updateToDo(int ToDoID, int userID, String title, int status, int priority, String scheduledDate, String startTime, String endTime, String nodeID, String detail, String notificationDate, String notificationTime) {
		String statusString = null;
		String sql = "Update ToDo Set userID = ?";
		if (title != null) {
			sql += ", title = ?";
		}
		if (status != -1) {
			sql += ", status = ?";
			if (getToDoType(ToDoID) == 1) {
				switch (status) {
					case 1:  // normal
						statusString = "inProgress";
					case 10: // complete
						statusString = "complete";
					case 0:  // delete
						statusString = "canceled";
						break;
					default:
						throw new IllegalStateException("Unexpected value: " + status);
				}
			}
		}
		if (priority != -1) {
			sql += ", priority = ?";
		}
		if (scheduledDate != null) {
			sql += ", scheduledDate = ?";
		}
		if (startTime != null) {
			sql += ", scheduledTime = ?";
		}
		if (endTime != null) {
			sql += ", expectedLength = ?";
		}
		if (nodeID != null) {
			sql += ", nodeID = ?";
		}
		if (detail != null) {
			sql += ", detail = ?";
		}
		if (notificationDate != null) {
			sql += ", notificationDate = ?";
		}
		if (notificationTime != null) {
			sql += ", notificationTime = ?";
		}
		sql += " Where ToDoID = ?";

		if (RequestsDB2.editRequests(ToDoID, userID, statusString) != 1) {
			return false;
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, userID);
			int i = 1;
			if (title != null) {
				i++;
				preparedStatement.setString(i, title);
			}
			if (status != -1) {
				i++;
				preparedStatement.setInt(i, status);
			}
			if (priority != -1) {
				i++;
				preparedStatement.setInt(i, priority);
			}
			if (scheduledDate != null) {
				i++;
				preparedStatement.setString(i, scheduledDate);
			}
			if (startTime != null) {
				i++;
				preparedStatement.setString(i, startTime);
			}
			if (endTime != null) {
				i++;
				preparedStatement.setString(i, endTime);
			}
			if (nodeID != null) {
				i++;
				preparedStatement.setString(i, nodeID);
			}
			if (detail != null) {
				i++;
				preparedStatement.setString(i, detail);
			}
			if (notificationDate != null) {
				i++;
				preparedStatement.setString(i, notificationDate);
			}
			if (notificationTime != null) {
				i++;
				preparedStatement.setString(i, notificationTime);
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

	/**
	 * Checks which type is the ToDoID from
	 * @return 0 for pure, 1 for requests, 2 for appointment, -1 for error
	 */
	public static int getToDoType(int ToDoID) {
		try (PreparedStatement preparedStatement = connection.prepareStatement("Select Count(*) As Count From ToDo, requests Where ToDoID = requestID And ToDoID = ?")) {
			preparedStatement.setInt(1, ToDoID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (resultSet.getInt("Count") == 1) {
					return 1;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error querying from ToDo and requests table in getToDoType()");
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement("Select Count(*) As Count From ToDo, appointment Where ToDoID = appointmentID And ToDoID = ?")) {
			preparedStatement.setInt(1, ToDoID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (resultSet.getInt("Count") == 1) {
					return 2;
				} else return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error querying from ToDo and appointment table in getToDoType()");
		}
		return -1;
	}

	/**
	 * @param date   enter "" for undated ToDos, enter "everything" for all ToDos(including undated ones)
	 * @param status enter -1 for get all regardless of status, or enter needed status number
	 * @return a List of ToDo_items
	 */
	public static List<ToDo> getToDoList(int userID, int status, String date) {
		List<ToDo> toDoList = new ArrayList<>();
		String sql = "Select * From ToDo Where userID = ?";

		if (status != -1) {
			sql += " And status = ?";
		}
		switch (date) {
			case "everything": // all ToDos(including undated ones)
				break;
			case "": // undated ToDos
				sql += " And scheduledDate Is Null";
				break;
			default:
				sql += " And scheduledDate = ?";
				break;
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setInt(1, userID);
			int i = 1;
			if (status != -1) {
				i++;
				preparedStatement.setInt(i, status);
			}
			if (!date.equals("everything") && !date.equals("")) {
				i++;
				preparedStatement.setString(i, date);
			}
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				toDoList.add(new ToDo(
						resultSet.getInt("ToDoID"),
						resultSet.getString("title"),
						resultSet.getInt("userID"),
						resultSet.getInt("status"),
						resultSet.getInt("priority"),
						NodeDB.getNodeInfo(resultSet.getString("nodeID")),
						resultSet.getString("scheduledDate"),
						resultSet.getString("startTime"),
						resultSet.getString("endTime"),
						resultSet.getString("detail"),
						resultSet.getString("notificationDate"),
						resultSet.getString("notificationTime")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("SQL error in getToDoList()");
		}
		return toDoList;
	}
}
