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
	 * @param request this is all of the information needed in a floral request object.
	 */
	public static void addFloralRequest(FloralObj request) {
		addRequest(request.getUserID(), request.getAssigneeID(), "floral");

		String insertFloralRequest = "Insert Into floralrequests Values ((Select Count(*) From requests), ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertFloralRequest)) {
			prepState.setString(1, request.getNodeID());
			prepState.setString(2, request.getRecipient());
			prepState.setString(3, request.getFlower());
			prepState.setInt(4, request.getCount());
			prepState.setString(5, request.getVase());
			prepState.setString(6, request.getArrangement());
			prepState.setString(7, request.getStuffedAnimal());
			prepState.setString(8, request.getChocolate());
			prepState.setString(9, request.getMessage());

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into floralRequests inside function addFloralRequest()");
		}
	}


	/**
	 * This edits a floral request form that is already in the database
	 * @param request this the information that the user wants to change stored in a floral request object. (If int = 0 --> do not change, If String = null --> do not change)
	 * @return
	 */
	public static int editFloralRequest(FloralObj request) {

		boolean added = false;
		String query = "Update floralRequests Set ";

		if (request.getRecipient() != null) {
			query = query + "recipientName = '" + request.getRecipient() + "'";

			added = true;
		}
		if (request.getNodeID() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " roomID = '" + request.getNodeID() + "'";
			added = true;
		}
		if (request.getFlower() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " flowerType = '" + request.getFlower() + "'";
			added = true;
		}

		//TODO: changed this line from "request.getCount() != null" because it threw an error. Need make sure that I changed it ok
		if (request.getCount() > 0) {
			if (added) {
				query = query + ", ";
			}
			query = query + " flowerAmount = " + request.getCount();
			added = true;
		}
		if (request.getVase() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " vaseType = '" + request.getVase() + "'";
			added = true;
		}
		if (request.getArrangement() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " arrangement = '" + request.getArrangement() + "'";
			added = true;
		}
		if (request.getStuffedAnimal() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " stuffedAnimal = '" + request.getStuffedAnimal() + "'";
			added = true;
		}
		if (request.getChocolate() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " chocolate = '" + request.getChocolate() + "'";
			added = true;
		}
		if (request.getMessage() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " message = '" + request.getMessage() + "'";
			added = true;
		}

		query = query + " where requestID = " + request.getRequestID();
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating floral request");
			return 0;
		}
	}





}
