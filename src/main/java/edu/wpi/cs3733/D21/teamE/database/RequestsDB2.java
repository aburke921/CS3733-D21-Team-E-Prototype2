package edu.wpi.cs3733.D21.teamE.database;

import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.FloralObj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RequestsDB2 {

	static Connection connection = makeConnection.makeConnection().connection;

	//ALL REQUEST STUFF::::

	/**
	 * Uses executes the SQL statements required to create the requests table.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - creatorID: this is the username of the user who created the request.
	 * - creationTime: this is a time stamp that is added to the request at the moment it is made.
	 * - requestType: this is the type of request that the user is making. The valid options are: "floral", "medDelivery", "sanitation", "security", "extTransport", 'languageRequest' 'laundryRequest' 'maintenanceRequest' 'foodDelivery' 'internalPatientRequest'
	 * - requestStatus: this is the state in which the request is being processed. The valid options are: "complete", "canceled", "inProgress".
	 * - assigneeID: this is the person who is assigned to the request
	 */
	public static void createRequestsTable() {

		String query = "Create Table requests(" +
				"requestID     int Primary Key, " +
				"creatorID     int References useraccount On Delete Cascade," +
				"creationTime  timestamp, " +
				"requestType   varchar(31), " +
				"requestStatus varchar(10), " +
				"assigneeID    int References useraccount On Delete Cascade," +
				"Constraint requestTypeLimit Check (requestType In ('floral', 'medDelivery', 'sanitation', 'security', 'extTransport', 'internalPatientRequest', 'languageRequest', 'laundryRequest', 'maintenanceRequest', 'foodDelivery', 'religiousRequest')), " +
				"Constraint requestStatusLimit Check (requestStatus In ('complete', 'canceled', 'inProgress')))";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating requests table");
		}
	}

	public static void addRequest(int userID, int assigneeID, String requestType) {
		String insertRequest = "Insert Into requests " +
				"Values ((Select Count(*) " +
				"         From requests) + 1, ?, Current Timestamp, ?, 'inProgress', ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);
			prepState.setString(2, requestType);
			prepState.setInt(3, assigneeID);
			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into requests inside function addRequest()");
		}

	}







	//FLORAL REQUEST STUFF:

	/**
	 * Uses executes the SQL statements required to create a floralRequests table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user is sending the request to.
	 * - recipientName: this is the name of the individual they want the flowers to be addressed to
	 * - flowerType: this is the type of flowers that the user wants to request. The valid options are: 'Roses', 'Tulips', 'Carnations', 'Assortment'
	 * - flowerAmount: this the number/quantity of flowers that the user is requesting. The valid options are: 1, 6, 12
	 * - vaseType: this is the type of vase the user wants the flowers to be delivered in. The valid options are: 'Round', 'Square', 'Tall', 'None'
	 * - message: this is a specific detailed message that the user can have delivered with the flowers or an instruction message
	 * *                for whoever is fufilling the request
	 */
	public static void createFloralRequestsTable() {

		String query = "Create Table floralRequests( " +
				"requestID     int Primary Key References requests On Delete Cascade, " +
				"roomID        varchar(31) References node On Delete Cascade, " +
				"recipientName varchar(31), " +
				"flowerType    varchar(31), " +
				"flowerAmount  int, " +
				"vaseType      varchar(31), " +
				"arrangement varchar(31), " +
				"stuffedAnimal varchar(31), " +
				"chocolate varchar(31), " +
				"message       varchar(5000), " +
				"Constraint flowerTypeLimit Check (flowerType In ('Roses', 'Tulips', 'Carnations', 'Assortment')), " +
				"Constraint flowerAmountLimit Check (flowerAmount In (1, 6, 12)), " +
				"Constraint vaseTypeLimit Check (vaseType In ('Round', 'Square', 'Tall', 'None')))";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating floralRequests table");
		}
	}


	/**
	 * This adds a floral request to the database that the user is making
	 * @param userID        this is the username that the user uses to log into the account
	 * @param assigneeID    this is the ID of the assigned user
	 * @param RoomNodeID    this is the nodeID/room the user is sending the request to
	 * @param recipientName this is the name of the individual they want the flowers to be addressed to
	 * @param flowerType    this is the type of flowers that the user wants to request
	 * @param flowerAmount  this the number/quantity of flowers that the user is requesting
	 * @param vaseType      this is the type of vase the user wants the flowers to be delivered in
	 * @param message       this is a specific detailed message that the user can have delivered with the flowers or an instruction message
	 */
	/*
	public static void addFloralRequest(int userID, int assigneeID, String RoomNodeID, String recipientName, String flowerType, int flowerAmount, String vaseType, String arrangement, String stuffedAnimal, String chocolate, String message) {
		addRequest(userID, assigneeID, "floral");

		String insertFloralRequest = "Insert Into floralrequests Values ((Select Count(*) From requests), ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertFloralRequest)) {
			prepState.setString(1, RoomNodeID);
			prepState.setString(2, recipientName);
			prepState.setString(3, flowerType);
			prepState.setInt(4, flowerAmount);
			prepState.setString(5, vaseType);
			prepState.setString(6, arrangement);
			prepState.setString(7, stuffedAnimal);
			prepState.setString(8, chocolate);
			prepState.setString(9, message);

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into floralRequests inside function addFloralRequest()");
		}
	}

*/

	/**
	 * This adds a floral request to the database that the user is making
	 * @param userID        this is the username that the user uses to log into the account
	 * @param assigneeID    this is the ID of the assigned user
	 * @param RoomNodeID    this is the nodeID/room the user is sending the request to
	 * @param recipientName this is the name of the individual they want the flowers to be addressed to
	 * @param flowerType    this is the type of flowers that the user wants to request
	 * @param flowerAmount  this the number/quantity of flowers that the user is requesting
	 * @param vaseType      this is the type of vase the user wants the flowers to be delivered in
	 * @param message       this is a specific detailed message that the user can have delivered with the flowers or an instruction message
	 */
	public static void addFloralRequest(FloralObj request) {
//		addRequest(userID, assigneeID, "floral");
		addRequest(request.getUserID(), request.getAssigneeID(), "floral");

		String insertFloralRequest = "Insert Into floralrequests Values ((Select Count(*) From requests), ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertFloralRequest)) {
//			prepState.setString(1, RoomNodeID);
			prepState.setString(1, request.getNodeID());
//			prepState.setString(2, recipientName);
			prepState.setString(2, request.getRecipient());
//			prepState.setString(3, flowerType);
			prepState.setString(3, request.getFlower());
//			prepState.setInt(4, flowerAmount);
			prepState.setInt(4, request.getCount());
//			prepState.setString(5, vaseType);
			prepState.setString(5, request.getVase());
//			prepState.setString(6, arrangement);
			prepState.setString(6, request.getArrangement());
//			prepState.setString(7, stuffedAnimal);
			prepState.setString(7, request.getStuffedAnimal());
//			prepState.setString(8, chocolate);
			prepState.setString(8, request.getChocolate());
//			prepState.setString(9, message);
			prepState.setString(9, request.getMessage());

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into floralRequests inside function addFloralRequest()");
		}
	}


}
