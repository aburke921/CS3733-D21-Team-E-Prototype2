package edu.wpi.cs3733.D21.teamE.database;

import edu.wpi.cs3733.D21.teamE.views.CovidSurveyObj;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers.MedicineDelivery;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

public class RequestsDB2 {

	static Connection connection = makeConnection.makeConnection().connection;

	//ALL REQUEST STUFF:

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

	/**
	 * adds a request to the requests table in the databse
	 * @param userID this is the userID of the person filling out the request
	 * @param assigneeID this is the userID of the person who is assigned to this request
	 * @param requestType this is the type of request that is being created
	 */
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

	/**
	 * Gets the largest requestID, which can be used to increment and make a the next one
	 * @return the largest requestID in the request table
	 */
	public static int getMaxRequestID() {
		int maxID = 0;

		String query = "Select Max(requestID) As maxRequestID From food";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();

			if (rset.next()) {
				maxID = rset.getInt("maxRequestID");
			}

			prepState.close();
			return maxID;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error in getMaxRequestID()");
			return 0;
		}
	}

	/**
	 * Can change the assigneeID or the request status to any request
	 * @param requestID     is the generated ID of the request
	 * @param assigneeID    is the assignee's ID that you want to change it to
	 * @param requestStatus is the status that you want to change it to
	 * @return a 1 if one line changed successfully, and 0 or other numbers for failure
	 */
	public static int editRequests(int requestID, int assigneeID, String requestStatus) {

		boolean added = false;
		String query = "Update requests ";

		if (assigneeID != 0) {
			query = query + "Set assigneeID = '" + assigneeID + "'";

			added = true;
		}
		if (requestStatus != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "Set requestStatus = '" + requestStatus + "'";

		}

		query = query + " Where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating request table");
			return 0;
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
	 * @param request this is all of the information needed, in a floral request object.
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
	 * @return 1 if the update was successful, 0 if it failed
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





	//LANGUAGE REQUEST STUFF:

	/**
	 * Uses executes the SQL statements required to create a languageRequest table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - languageType: this is the type of language the user is requesting
	 * - description: detailed description of request
	 */
	public static void createLanguageRequestTable() {

		String query = "Create Table languageRequest " +
				"( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    roomID    varchar(31) Not Null References node On Delete Cascade, " +
				"    languageType     varchar(31) Not Null, " +
				"    description   varchar(5000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating languageRequest table");
		}

	}

	/**
	 * adds a language request to the languageRequest table
	 * @param request this is all of the information needed, in a language request object.
	 */
	public static void addLanguageRequest(LanguageInterpreterObj request) {
//		addRequest(userID, assigneeID, "languageRequest");
		addRequest(request.getUserID(), request.getAssigneeID(), "languageRequest");

		String insertLanguageReq = "Insert Into languageRequest Values ((Select Count(*) From requests), ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertLanguageReq)) {
			prepState.setString(1, request.getNodeID());
			prepState.setString(2, request.getLanguage());
			prepState.setString(3, request.getDescription());

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into languageRequest inside function addLanguageRequest()");
		}

	}

	/**
	 * This edits a language request form that is already in the database
	 * @param request this the information that the user wants to change stored in a language request object. (If int = 0 --> do not change, If String = null --> do not change)
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editLanguageRequest(LanguageInterpreterObj request) {
		boolean added = false;
		String query = "Update languageRequest Set ";

		if (request.getNodeID() != null) {
			query = query + " roomID = '" + request.getNodeID() + "'";
			added = true;
		}
		if (request.getLanguage() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "languageType = '" + request.getLanguage() + "'";
			added = true;
		}
		if (request.getDescription() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + request.getDescription() + "'";
		}

		query = query + " where requestID = " + request.getRequestID();
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating languageRequest");
			return 0;
		}

	}






	// RELIGIOUS REQUESTS STUFF:

	/**
	 * Uses executes the SQL statements required to create a languageRequest table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - religionID: is the type of maintenance required
	 * - description: detailed description of request
	 * - religionType: religion
	 */
	public static void createReligionRequestTable() {

		String query = "Create Table religiousRequest " +
				"( " +
				"requestID     int Primary Key References requests (requestID) On Delete Cascade, " +
				"roomID        varchar(31)  Not Null References node (nodeID) On Delete Cascade, " +
				"religionType  varchar(31)  Not Null, " +
				"description   varchar(5000) Not Null, " +
				"Constraint religionTypeLimit Check (religionType In ('Religion1', 'Religion2', 'Religion3', 'Religion4')) " +
				")";
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating religiousRequests table");
		}
	}

	/**
	 * adds a language request to the religiousRequest table
	 * @param request this is all of the information needed, in a religious request object.
	 */
	public static void addReligiousRequest(ReligiousRequestObj request) {
		addRequest(request.getUserID(), request.getAssigneeID(), "religiousRequest");

		String insertMaintenanceReq = "Insert Into religiousRequest Values ((Select Count(*) From requests), ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertMaintenanceReq)) {
			prepState.setString(1, request.getNodeID());
			prepState.setString(2, request.getReligion());
			prepState.setString(3, request.getDescription());

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into religiousRequest inside function addReligiousRequest()");
		}
	}


	/**
	 * This edits a religious request form that is already in the database
	 * @param request this the information that the user wants to change stored in a religious request object. (If int = 0 --> do not change, If String = null --> do not change)
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editReligiousRequest(ReligiousRequestObj request) {
		boolean added = false;
		String query = "Update religiousRequest Set ";

		if (request.getNodeID() != null) {
			query = query + "roomID = '" + request.getNodeID() + "'";
			added = true;
		}
		if (request.getReligion() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "religionType = '" + request.getReligion() + "'";
			added = true;
		}
		if (request.getDescription() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + request.getDescription() + "'";
		}

		query = query + " where requestID = " + request.getRequestID();
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating religiousRequest");
			return 0;
		}
	}





	//EXTERNAL PATIENT REQUEST STUFF:

	/**
	 * Uses executes the SQL statements required to create a extTransport table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user is sending the request to.
	 * - requestType: this the mode of transportation that the request is being made for. The valid options are: 'Ambulance', 'Helicopter', 'Plane'
	 * - severity: this is how sever the patient is who the user/first responders are transporting.
	 * - patientID: this is the ID of the patient that is being transported.
	 * - ETA: this is the estimated time the patient will arrive.
	 * - description: this is a detailed description of request that generally includes what happened to the patient and their current situation.
	 */
	public static void createExtTransportTable() {

		String query = "Create Table extTransport( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    roomID varchar(31) Not Null References node On Delete Cascade, " +
				"    requestType varchar(100) Not Null, " +
				"    severity varchar(30) Not Null, " +
				"    patientID varchar(31) Not Null, " +
				"    bloodPressure varchar(31), " +
				"    temperature varchar(31), " +
				"    oxygenLevel varchar(31), " +
				"    description varchar(5000)," +
				"    Constraint requestTypeLimitExtTrans Check (requestType In ('Ambulance', 'Helicopter', 'Plane'))" +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating extTransport table");
		}
	}

	/**
	 * This function needs to add a external patient form to the table for external patient forms
	 * //@param form this is the form that we will create and send to the database
	 */
	public static void addExternalPatientRequest(ExternalPatientObj externalPatientObj) {
		// int userID, int assigneeID, String roomID, String requestType, String severity, String patientID, String ETA, String bloodPressure, String temperature, String oxygenLevel, String description

		int userID = externalPatientObj.getUserID();
		int assigneeID = externalPatientObj.getAssigneeID();
		String roomID = externalPatientObj.getNodeID();
		String requestType = externalPatientObj.getRequestType();
		String severity = externalPatientObj.getSeverity();
		String patientID = externalPatientObj.getPatientID();
		String bloodPressure = externalPatientObj.getBloodPressure();
		String temperature = externalPatientObj.getTemperature();
		String oxygenLevel = externalPatientObj.getOxygenLevel();
		String description = externalPatientObj.getDetails();

		addRequest(userID, assigneeID, "extTransport");

		String insertExtTransport = "Insert Into exttransport " +
				"Values ((Select Count(*) " +
				"         From requests), ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertExtTransport)) {
			prepState.setString(1, roomID);
			prepState.setString(2, requestType);
			prepState.setString(3, severity);
			prepState.setString(4, patientID);
			prepState.setString(5, bloodPressure);
			prepState.setString(6, temperature);
			prepState.setString(7, oxygenLevel);
			prepState.setString(8, description);

			prepState.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into extTransport inside function addExternalPatientRequest()");
		}

	}


	/**
	 * This edits a External Transport Services form that is already in the database
	 * @param externalPatientObj takes in an External Patient Object
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editExternalPatientRequest(ExternalPatientObj externalPatientObj) {

		//int requestID, String roomID, String requestType, String severity, String patientID, String description, String ETA, String bloodPressure, String temperature, String oxygenLevel
		String roomID = externalPatientObj.getNodeID();
		String requestType = externalPatientObj.getRequestType();
		String severity = externalPatientObj.getSeverity();
		String patientID = externalPatientObj.getPatientID();
		String bloodPressure = externalPatientObj.getBloodPressure();
		String temperature = externalPatientObj.getTemperature();
		String oxygenLevel = externalPatientObj.getOxygenLevel();
		String description = externalPatientObj.getDetails();

		boolean added = false;
		String query = "Update extTransport Set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";

			added = true;
		}
		if (requestType != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " requestType = '" + requestType + "'";
			added = true;
		}
		if (severity != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " severity = '" + severity + "'";
			added = true;
		}
		if (patientID != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " patientID = '" + patientID + "'";
			added = true;
		}
		if (description != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " description = '" + description + "'";
			added = true;
		}
		if (bloodPressure != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " bloodPressure = '" + bloodPressure + "'";
			added = true;
		}
		if (temperature != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " temperature = '" + temperature + "'";
			added = true;
		}
		if (oxygenLevel != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " oxygenLevel = '" + oxygenLevel + "'";
		}

		query = query + " where requestID = " + externalPatientObj.getRequestID();

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error in updating external transport request");
			return 0;
		}
	}





	//LAUNDRY REQUEST STUFF:

	/**
	 * Uses executes the SQL statements required to create a languageRequest table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - washLoadAmount: amount of loads needed to wash
	 * - dryLoadAmount: amount of loads needed to dry
	 * - description:  detailed description of request
	 */
	public static void createLaundryRequestTable() {

		String query = "Create Table laundryRequest " +
				"( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    roomID    varchar(31) Not Null References node On Delete Cascade, " +
				"    washLoadAmount   varchar(31) Not Null, " +
				"    dryLoadAmount   varchar(31) Not Null, " +
				"    description varchar(5000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating laundryRequest table");
		}

	}

	/**
	 * adds a laundry request to the laundryRequest table
	 * @param request this is all of the information needed, in a religious request object.
	 */
	public static void addLaundryRequest(LaundryObj request) {
//		addRequest(userID, assigneeID, "laundryRequest");
		addRequest(request.getUserID(), request.getAssigneeID(), "laundryRequest");

		String insertLaundryReq = "Insert Into laundryRequest Values ((Select Count(*) From requests), ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertLaundryReq)) {
//			prepState.setString(1, roomID);
//			prepState.setString(2, washLoadAmount);
//			prepState.setString(3, dryLoadAmount);
//			prepState.setString(4, description);
			prepState.setString(1, request.getNodeID());
			prepState.setString(2, request.getWashLoadAmount());
			prepState.setString(3, request.getDryLoadAmount());
			prepState.setString(4, request.getDescription());

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into laundryRequest inside function addLanguageRequest()");
		}
	}

	/**
	 * This edits a laundry request form that is already in the database
	 * @param request this the information that the user wants to change stored in a laundry request object. (If int = 0 --> do not change, If String = null --> do not change)
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editLaundryRequest(LaundryObj request) {
		boolean added = false;
		String query = "Update laundryRequest Set ";

		if (request.getNodeID() != null) {
			query = query + " roomID = '" + request.getNodeID() + "'";
			added = true;
		}
		if (request.getWashLoadAmount() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "washLoadAmount = '" + request.getWashLoadAmount() + "'";
			added = true;
		}
		if (request.getDryLoadAmount() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "dryLoadAmount = '" + request.getDryLoadAmount() + "'";
			added = true;
		}
		if (request.getDescription() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + request.getDescription() + "'";
		}

		query = query + " where requestID = " + request.getRequestID();
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating laundryRequest");
			return 0;
		}
	}



	//SECURITY REQUEST STUFF:

	/**
	 * Uses executes the SQL statements required to create a languageRequest table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - type: is the type of maintenance required
	 * - severity: is how severe the situation is
	 * - ETA: time taken to complete the request
	 * - description: detailed description of request
	 */
	public static void createMaintenanceRequestTable() {

		String query = "Create Table maintenanceRequest " +
				"( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    roomID    varchar(31) Not Null References node On Delete Cascade, " +
				"    type varchar(31), " +
				"    severity    varchar(31)  Not Null, " +
				"    description varchar(5000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating maintenanceRequest table");
		}

	}

	/**
	 * adds a maintenance request to the maintenanceRequest table
	 * @param maintenanceObj this is all of the information needed, in a maintenance request object.
	 */
	public static void addMaintenanceRequest(MaintenanceObj maintenanceObj) {

		int userID = maintenanceObj.getUserID();
		int assigneeID = maintenanceObj.getAssigneeID();
		String roomID = maintenanceObj.getNodeID();
		String type = maintenanceObj.getRequest();
		String severity = maintenanceObj.getSeverity();
		String description = maintenanceObj.getDescription();


		addRequest(userID, assigneeID, "maintenanceRequest");

		String insertMaintenanceReq = "Insert Into maintenanceRequest Values ((Select Count(*) From requests), ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertMaintenanceReq)) {
			prepState.setString(1, roomID);
			prepState.setString(2, type);
			prepState.setString(3, severity);
			prepState.setString(4, description);

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into maintenanceRequest inside function addMaintenanceRequest()");
		}
	}

	/**
	 * This edits a maintenance request which is already in the db
	 * @param maintenanceObj this is all of the information needed, in a maintenance request object.
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editMaintenanceRequest(MaintenanceObj maintenanceObj) {

		String roomID = maintenanceObj.getNodeID();
		String type = maintenanceObj.getRequest();
		String severity = maintenanceObj.getSeverity();
		String description = maintenanceObj.getDescription();


		boolean added = false;
		String query = "Update maintenanceRequest Set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";
			added = true;
		}
		if (type != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "type = '" + type + "'";
			added = true;
		}
		if (severity != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "severity = '" + severity + "'";
			added = true;
		}
		if (description != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + description + "'";
			added = true;
		}

		query = query + " where requestID = " + maintenanceObj.getRequestID();
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating maintenanceRequest");
			return 0;
		}
	}




	//SECURITY REQUEST STUFF:

	/**
	 * Uses executes the SQL statements required to create a medDelivery table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - level: this is the security level that is needed
	 * - urgency: this is how urgent it is for security to arrive or for the request to be filled. The valid options are: 'Low', 'Medium', 'High', 'Critical'
	 */
	public static void createSecurityServTable() {

		String query = "Create Table securityServ ( " +
				"requestID  int Primary Key References requests On Delete Cascade," +
				"roomID     varchar(31) Not Null References node On Delete Cascade," +
				"level     varchar(31)," +
				"urgency   varchar(31) Not Null," +
				"reason     varchar(3000)," +
				"Constraint urgencyTypeLimitServ Check (urgency In ('Low', 'Medium', 'High', 'Critical')) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating securityServ table");
		}
	}

	/**
	 * adds a security request to the securityServ table
	 * @param request this is all of the information needed, in a security request object.
	 */
	public static void addSecurityRequest(SecurityServiceObj request) {
		addRequest(request.getUserID(), request.getAssigneeID(), "security");

		String insertSecurityRequest = "Insert Into securityServ Values ((Select Count(*) From requests), ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertSecurityRequest)) {
			prepState.setString(1, request.getNodeID());
			prepState.setString(2, request.getSecurityLevel());
			prepState.setString(3, request.getUrgencyLevel());
			prepState.setString(4, request.getReason());

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into securityRequest inside function addSecurityRequest()");
		}
	}

	/**
	 * This edits a security request form that is already in the database
	 * @param request this the information that the user wants to change stored in a security request object. (If int = 0 --> do not change, If String = null --> do not change)
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editSecurityRequest(SecurityServiceObj request) {
		boolean added = false;
		String query = "Update securityServ Set ";

		if (request.getNodeID() != null) {
			query = query + " roomID = '" + request.getNodeID() + "'";

			added = true;
		}
		if (request.getSecurityLevel() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "level = '" + request.getSecurityLevel() + "'";
			added = true;
		}
		if (request.getUrgencyLevel() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "urgency = '" + request.getUrgencyLevel() + "'";
			added = true;
		}
		if (request.getReason() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "reason = '" + request.getReason() + "'";
		}

		query = query + " where requestID = " + request.getRequestID();
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating security request");
			return 0;
		}
	}






	//SANITATION REQUEST STUFF:

	/**
	 * Uses executes the SQL statements required to create a sanitationRequest table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user is sending the request to.
	 * - signature: this the signature (name in print) of the user who is filling out the request
	 * - description: this is any description the user who is filling out the request wants to provide for the person who will be completing the request
	 * - sanitationType: this is the type of sanitation/cleanup the user is requesting to be delt with. The valid options are: "Urine Cleanup",
	 * "Feces Cleanup", "Preparation Cleanup", "Trash Removal"
	 * - urgency: this is how urgent the request is and helpful for prioritizing. The valid options are: "Low", "Medium", "High", "Critical"
	 */
	public static void createSanitationTable() {

		String query = "Create Table sanitationRequest( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    roomID varchar(31) Not Null References node On Delete Cascade, " +
				"    description varchar(5000), " +
				"    sanitationType varchar(31), " +
				"    urgency varchar(31) Not Null, " +
				"    Constraint sanitationTypeLimit Check (sanitationType In ('Urine Cleanup', 'Feces Cleanup', 'Preparation Cleanup', 'Trash Removal'))," +
				"    Constraint urgencyTypeLimit Check (urgency In ('Low', 'Medium', 'High', 'Critical')) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating sanitationRequest table");
		}
	}


	/**
	 * adds a sanitation request to the sanitationRequest table
	 * @param request this is all of the information needed, in a sanitation request object.
	 */
	public static void addSanitationRequest(SanitationServiceObj request) {
		addRequest(request.getUserID(), request.getAssigneeID(), "sanitation");

		String insertSanitationRequest = "Insert Into sanitationRequest " +
				"Values ((Select Count(*) " +
				"         From requests), ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertSanitationRequest)) {
			prepState.setString(1, request.getNodeID());
			prepState.setString(2, request.getDetail());
			prepState.setString(3, request.getService());
			prepState.setString(4, request.getSeverity());

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into sanitationRequest inside function addSanitationRequest()");
		}

	}


	/**
	 * This edits a sanitation request form that is already in the database
	 * @param request this the information that the user wants to change stored in a sanitation request object. (If int = 0 --> do not change, If String = null --> do not change)
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editSanitationRequest(SanitationServiceObj request) {

		boolean added = false;
		String query = "update sanitationRequest set";

		if (request.getNodeID() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " roomID = '" + request.getNodeID() + "'";
			added = true;
		}
		if (request.getService() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " sanitationType = '" + request.getService() + "'";
			added = true;
		}
		if (request.getDetail() != null) {
			query = query + " description = '" + request.getDetail() + "'";

			added = true;
		}
		if (request.getSeverity() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " urgency = '" + request.getSeverity() + "'";
			added = true;
		}


		query = query + " where requestID = " + request.getRequestID();

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating sanitation request");
			return 0;
		}


	}







	//MEDICINE REQUEST STUFF:

	/**
	 * Uses executes the SQL statements required to create a medDelivery table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants the medecine delivered to.
	 * - medicineName: this is the drug that the user is ordering/requesting
	 * - quantity: the is the supply or the number of pills ordered
	 * - dosage: this is the mg or ml quantity for a medication
	 * - specialInstructions: this is any special details or instructions the user wants to give to who ever is processing the request.
	 * - signature: this the signature (name in print) of the user who is filling out the request
	 */
	public static void createMedDeliveryTable() {
		String query = "Create Table medDelivery ( " +
				"requestID  int Primary Key References requests On Delete Cascade," +
				"roomID     varchar(31) Not Null References node On Delete Cascade," +
				"medicineName        varchar(31) Not Null," +
				"quantity            int         Not Null," +
				"dosage              int Not Null," +
				"specialInstructions varchar(5000)," +
				"signature           varchar(31) Not Null" + ")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating medDelivery table");
		}
	}

	/**
	 * This adds a medicine request form to the table for medicine request forms
	 * @param medicineDeliveryObj object holding medicine req. fields
	 */
	public static void addMedicineRequest(MedicineDeliveryObj medicineDeliveryObj) {

		int userID = medicineDeliveryObj.getUserID();
		int assigneeID = medicineDeliveryObj.getAssigneeID();
		String roomID = medicineDeliveryObj.getNodeID();
		String medicineName = medicineDeliveryObj.getMedicineName();
		int quantity = medicineDeliveryObj.getDoseQuantity();
		int dosage = medicineDeliveryObj.getDoseMeasure();
		String specialInstructions = medicineDeliveryObj.getSpecialInstructions();
		String signature = medicineDeliveryObj.getSignature();

		addRequest(userID, assigneeID, "medDelivery");

		String insertMedRequest = "Insert Into meddelivery Values ((Select Count(*) From requests), ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertMedRequest)) {
			prepState.setString(1, roomID);
			prepState.setString(2, medicineName);
			prepState.setInt(3, quantity);
			prepState.setInt(4, dosage);
			prepState.setString(5, specialInstructions);
			prepState.setString(6, signature);

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into medicineRequest inside function addMedicineRequest()");
		}
	}

	/**
	 * edits medicine request which is already in DB
	 * @param medicineDeliveryObj object holding medicine req. fields
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editMedicineRequest(MedicineDeliveryObj medicineDeliveryObj) {

		int requestID = medicineDeliveryObj.getRequestID();
		int assigneeID = medicineDeliveryObj.getAssigneeID();
		String roomID = medicineDeliveryObj.getNodeID();
		String medicineName = medicineDeliveryObj.getMedicineName();
		int quantity = medicineDeliveryObj.getDoseQuantity();
		Integer quantityI = quantity;
		int dosage = medicineDeliveryObj.getDoseMeasure();
		Integer dosageI = quantity;
		String specialInstructions = medicineDeliveryObj.getSpecialInstructions();
		String signature = medicineDeliveryObj.getSignature();

		boolean added = false;
		String query = "Update medDelivery Set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";

			added = true;
		}
		if (medicineName != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " medicineName = '" + medicineName + "'";
			added = true;
		}
		if (quantityI != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " quantity = " + quantity;
			added = true;
		}
		if (dosageI != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " dosage = " + dosage;
			added = true;
		}
		if (specialInstructions != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " specialInstructions = '" + specialInstructions + "'";
			added = true;
		}
		if (signature != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " signature = '" + signature + "'";
			added = true;
		}


		query = query + " where requestID = " + requestID;

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error in updating medicine request");
			return 0;
		}
	}




	//FOOD REQUEST STUFF:

	/**
	 * Uses executes the SQL statements required to create a foodDelivery table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - allergy: this is an food allergy that the user might have
	 * - dietRestriction: this is any dietary restrictions that the person fulfilling the request might need to know about
	 * - beverage: the drink the user is ordering
	 * - comments: any comments the user wants to leave for the person fulfilling the request
	 */
	public static void createFoodDeliveryTable() {

		String query = "Create Table foodDelivery ( " +
				"    requestID int Primary Key References requests (requestID) On Delete Cascade, " +
				"    roomID varchar(31) Not Null References node (nodeID) On Delete Cascade, " +
				"    deliveryService varchar(50), " +
				"    orderNumber varchar(50), " +
				"    description varchar(3000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating foodDelivery table");
		}


	}

	//TODO: Not tested
	/**
	 * adds a food delivery request to the foodDelivery table
	 * @param request this is all of the information needed, in a food delivery request object.
	 */
	public static void addFoodDeliveryRequest(FoodDeliveryObj request) {
		addRequest(request.getUserID(), request.getAssigneeID(), "foodDeliveryRequest");

		int requestID = getMaxRequestID();

		String insertFoodDeliveryReq = "Insert Into foodDelivery Values (?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertFoodDeliveryReq)) {
			prepState.setInt(1, requestID);
			prepState.setString(2, request.getNodeID());
			prepState.setString(3, request.getDeliveryService());
			prepState.setString(4, request.getOrderNumber());
			prepState.setString(5, request.getDescription());
			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into foodDeliveryRequest inside function addFoodDeliveryRequest()");
		}
	}


	//TODO: Not tested
	/**
	 * edits food delivery request which is already in DB
	 * @param request this the information that the user wants to change stored in a food delivery request object. (If int = 0 --> do not change, If String = null --> do not change)
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editFoodDeliveryRequest(FoodDeliveryObj request) {
		boolean added = false;
		String query = "Update foodDeliveryRequest Set ";

		if (request.getNodeID() != null) {
			query = query + " roomID = '" + request.getNodeID() + "'";
			added = true;
		}
		if (request.getDeliveryService() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "deliveryService = '" + request.getDeliveryService() + "'";
			added = true;
		}
		if (request.getOrderNumber() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "orderNumber = '" + request.getOrderNumber() + "'";
			added = true;
		}
		if (request.getDescription() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + request.getDescription() + "'";
		}

		query = query + " where requestID = " + request.getRequestID();
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating foodDeliveryRequest");
			return 0;
		}
	}


	/**
	 * Uses executes the SQL statements required to create a aubonPainMenu table.
	 * This table has attributes:
	 * - foodImage: this is the url to an image of the item
	 * - foodItem: this is the item itself (this is unique and is used as an identifier)
	 * - foodPrice: this is the price of the foodItem
	 * - foodCalories: this is the number of calories the food item has
	 * - foodDescription: this is a description of the food item
	 */
	public static void createAubonPainMenuTable() {
		String query = "Create Table aubonPainMenu( " +
				"    foodImage varchar(600), " +
				"    foodItem varchar(100) Primary Key, " +
				"    foodPrice varchar(50), " +
				"    foodCalories varchar(50), " +
				"    foodDescription varchar(3000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating aubonPainMenu table");
		}
	}

	/**
	 * adds a menuItem to the aubonPainMenu database table
	 * @param menuItem this is all of the information needed, in a security request object.
	 */
	public static void addAubonPainMenuItem(AubonPainItem menuItem){

		String query = "Insert Into aubonPainMenu Values(?,?,?,?,?) ";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.setString(1, menuItem.getImageURL());
			prepState.setString(2, menuItem.getFoodItem());
			prepState.setString(3, menuItem.getFoodPrice());
			prepState.setString(4, menuItem.getFoodCalories());
			prepState.setString(5, menuItem.getFoodDescription());
			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("error inserting addAubonPainMenuItem() ");
		}

	}

	/**
	 * This parses through the Abon Pain website at BH and adds each item, its image, calories, price, and
	 * description to the aubonPainMenu table
	 * The link to the website being read is: https://order.aubonpain.com/menu/brigham-womens-hospital
	 */
	public static void populateAbonPainTable() {

		try {
			Document doc = Jsoup.connect("https://order.aubonpain.com/menu/brigham-womens-hospital").get();
			Set<String> classes = doc.classNames();

			Elements elements = doc.body().select("*");

			ArrayList<String> foodImage = new ArrayList<>();
			ArrayList<String> foodItems = new ArrayList<>();
			ArrayList<String> foodPrice = new ArrayList<>();
			ArrayList<String> foodCalories = new ArrayList<>();
			ArrayList<String> foodDescription = new ArrayList<>();

			int count = 0;
			for (Element element : elements) {
				if (element.ownText().equals("Breakfast ON THE GO")) {
					count++;
				}
				if (count == 2) {
					if (element.normalName().equals("img")) {
						foodImage.add(element.attr("abs:src"));
					}

					if (element.className().equals("product-name product__name")) {
						foodItems.add(element.ownText());
					}
					if (element.className().equals("product__attribute product__attribute--price")) {
						int numOfEmptySpacesToAdd = foodItems.size() - foodPrice.size() - 1;
						for (int i = 0; i < numOfEmptySpacesToAdd; i++) {
							foodPrice.add(null);
						}
						foodPrice.add(element.ownText());
					}
					if (element.className().equals("product__attribute product__attribute--calorie-label")) {
						int numOfEmptySpacesToAdd = foodItems.size() - foodCalories.size() - 1;
						for (int i = 0; i < numOfEmptySpacesToAdd; i++) {
							foodCalories.add(null);
						}
						foodCalories.add(element.ownText());
					}
					if (element.className().equals("product__description")) {
						int numOfEmptySpacesToAdd = foodItems.size() - foodDescription.size() - 1;
						for (int i = 0; i < numOfEmptySpacesToAdd; i++) {
							foodDescription.add(null);
						}
						foodDescription.add(element.ownText());
					}
				}

			}

			for (int i = 0; i < foodImage.size(); i++) {
				String image = null;
				String price = null;
				String calories = null;
				if (i < foodImage.size()) {
					image = foodImage.get(i);
				}
				if (i < foodPrice.size()) {
					price = foodPrice.get(i);
				}
				if (i < foodCalories.size()) {
					calories = foodCalories.get(i);
				}

				addAubonPainMenuItem(new AubonPainItem(image, foodItems.get(i), price, calories, foodDescription.get(i)));
			}


		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("error reading in Aubon Pain website in addAbonPainTable()");
		}

	}









	//TODO: uncomment this when Internal Patient Object is completed
	// fix anything which needs to be fixed and write tests
	// add this table to deleteAllTables and createAllTables

	//INTERNAL PATIENT REQUEST STUFF:


	public static void createInternalPatientRequest() {
		String query = "Create Table internalPatientRequest " +
				"( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    patientID int References userAccount(userID) On Delete Cascade, " +
				"    pickUpLocation varchar(31) Not Null References node On Delete Cascade, " +
				"    dropOffLocation varchar(31) Not Null References node On Delete Cascade, " +
				"    department varchar(31), " +
				"    severity varchar(31), " +
				"    description varchar(5000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating internalPatientRequest table");
		}
	}

	/**
	 * adds a internal patient transport to the internalPatientRequest database table
	 * @param request object holding internal patient transport req. fields
	 */
 	public static void addInternalPatientRequest(InternalPatientObj request) {
		addRequest(request.getUserID(), request.getAssigneeID(), "internalPatientRequest");

		String insertInternalPatientReq = "Insert Into internalPatientRequest Values ((Select Count(*) From requests), ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertInternalPatientReq)) {
			prepState.setInt(1, request.getPatientID());
			prepState.setString(2, request.getNodeID());
			prepState.setString(3, request.getDropOffNodeID());
			prepState.setString(4, request.getDepartment());
			prepState.setString(5, request.getSeverity());
			prepState.setString(6, request.getDescription());
			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into internalPatientRequest inside function addInternalPatientRequest()");
		}
	}

	/**
	 * edits internal patient transport delivery request which is already in DB
	 * @param request this the information that the user wants to change stored in a food delivery request object. (If int = 0 --> do not change, If String = null --> do not change)
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editInternalPatientRequest(InternalPatientObj request) {
		boolean added = false;
		String query = "Update internalPatientRequest Set ";

		if (request.getNodeID() != null) {
			query = query + " pickUpLocation = '" + request.getNodeID() + "'";
			added = true;
		}
		if (request.getDropOffNodeID() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "dropOffLocation = '" + request.getDropOffNodeID() + "'";
			added = true;
		}
		if (request.getPatientID() != 0) {
			if (added) {
				query = query + ", ";
			}
			query = query + "patientID = " + request.getPatientID();
			added = true;
		}
		if (request.getDepartment() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "department = '" + request.getDepartment() + "'";
			added = true;
		}
		if (request.getSeverity() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "severity = '" + request.getSeverity() + "'";
			added = true;
		}
		if (request.getDescription() != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + request.getDescription() + "'";
		}

		query = query + " where requestID = " + request.getRequestID();
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error in editInternalPatientRequest()");
			return 0;
		}
	}








//	public static void addInternalPatientRequest(InternalPatientObj internalPatientObj) {
//
//		int userID = internalPatientObj.getUserID();
//		int assigneeID = internalPatientObj.getAssigneeID();
//		String roomID = internalPatientObj.getNodeID();
//		String dropOffLocation = internalPatientObj.getNodeID();
//		String department = internalPatientObj.getDepartment();
//		String severity = internalPatientObj.getSeverity();
//		String description = internalPatientObj.getDescription();
//
//		addRequest(userID, assigneeID, "internalPatient");
//
//		String insertInternPatient = "Insert Into internalPatient Values ((Select Count(*) From requests), ?, ?, ?, ?, ?)";
//
//		try (PreparedStatement prepState = connection.prepareStatement(insertInternPatient)) {
//			prepState.setString(1, roomID);
//			prepState.setString(2, dropOffLocation);
//			prepState.setString(3, department);
//			prepState.setString(4, severity);
//			prepState.setString(5, description);
//
//			prepState.execute();
//		} catch (SQLException e) {
//			//e.printStackTrace();
//			System.err.println("Error inserting into internalPatient inside function addInternalPatientRequest()");
//		}
//	}
//
//
//	public static int editInternalPatient(InternalPatientObj internalPatientObj) {
//
//		int requestID = internalPatientObj.getRequestID();
//		String roomID = internalPatientObj.getNodeID();
//		String dropOffLocation = internalPatientObj.getNodeID();
//		String department = internalPatientObj.getDepartment();
//		String severity = internalPatientObj.getSeverity();
//		String description = internalPatientObj.getDescription();
//
//		boolean added = false;
//		String query = "Update internalPatient Set ";
//
//		if (roomID != null) {
//			query = query + " roomID = '" + roomID + "'";
//
//			added = true;
//		}
//		if (dropOffLocation != null) {
//			if (added) {
//				query = query + ", ";
//			}
//			query = query + " dropOffLocation = '" + dropOffLocation + "'";
//			added = true;
//		}
//		if (department != null) {
//			if (added) {
//				query = query + ", ";
//			}
//			query = query + " quantity = '" + department + "'";
//			added = true;
//		}
//		if (severity != null) {
//			if (added) {
//				query = query + ", ";
//			}
//			query = query + " dosage = '" + severity + "'";
//			added = true;
//		}
//		if (description != null) {
//			if (added) {
//				query = query + ", ";
//			}
//			query = query + " specialInstructions = '" + description + "'";
//			added = true;
//		}
//
//		query = query + " where requestID = " + requestID;
//
//		try (PreparedStatement prepState = connection.prepareStatement(query)) {
//			prepState.executeUpdate();
//			prepState.close();
//			return 1;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			System.err.println("Error in updating internalPatient request");
//			return 0;
//		}
//	}




	// ENTRY REQUEST STUFF:









	//COVID SURVEY STUFF:

	/**
	 * create the entry request table
	 * decision: 0 for not filled, 1 for allow, 2 for ER, 3 for block
	 */
	public static void createEntryRequestTable() {
		// CovidSurveyObject has the following fields:
		// Integer user, Integer formNumber, Boolean positiveTest, Boolean symptoms, Boolean closeContact, Boolean quarantine, Boolean noSymptoms
		String query = "Create Table entryRequest " +
				"( " +
				"entryrequestID     int Primary Key, " +
				"positiveTest boolean Not Null, " +
				"symptoms     boolean Not Null, " +
				"closeContact     boolean Not Null, " +
				"quarantine     boolean Not Null, " +
				"noSymptoms     boolean Not Null, " +
				"status varchar(31) Not Null" +
				")";
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating entryRequest table");
		}
	}

	/**
	 * This adds a entry request form to the table
	 * each time a new entry is added, status is automatically set as "Needs to be reviewed"
	 */
	public static void addEntryRequest(CovidSurveyObj covidSurveyObj) {
		boolean positiveTest = covidSurveyObj.getPositiveTest();
		boolean symptoms = covidSurveyObj.getSymptoms();
		boolean closeContact = covidSurveyObj.getCloseContact();
		boolean quarantine = covidSurveyObj.getQuarantine();
		boolean noSymptoms = covidSurveyObj.getNoSymptoms();
		int userID = covidSurveyObj.getUser();

		//addRequest(userID, assigneeID, "entryRequest");

		String insertEntryRequest = "Insert Into entryRequest Values ((Select Count(*) From entryRequest) + 1, ?, ?, ?, ?, ?, 'Needs to be reviewed')";

		try (PreparedStatement prepState = connection.prepareStatement(insertEntryRequest)) {
			prepState.setBoolean(1, positiveTest);
			prepState.setBoolean(2, symptoms);
			prepState.setBoolean(3, closeContact);
			prepState.setBoolean(4, quarantine);
			prepState.setBoolean(5, noSymptoms);

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into entryRequest inside function addEntryRequest()");
		}
	}

	/**
	 *
	 * @param covidSurveyObj
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editEntryRequest(CovidSurveyObj covidSurveyObj) {
		//int requestID, boolean positiveTest, boolean symptoms, boolean closeContact, boolean quarantine, boolean noSymptoms, String status
		int formNumber = covidSurveyObj.getFormNumber();
		Boolean positiveTestB = covidSurveyObj.getPositiveTest();
		Boolean symptomsB = covidSurveyObj.getSymptoms();
		Boolean closeContactB = covidSurveyObj.getCloseContact();
		Boolean quarantineB = covidSurveyObj.getQuarantine();
		Boolean noSymptomsB = covidSurveyObj.getNoSymptoms();
		String status = covidSurveyObj.getStatus();

		boolean added = false;
		String query = "Update entryRequest Set";

		if (positiveTestB != null) {
			query = query + " positiveTest = " + positiveTestB;
			added = true;
		}
		if (symptomsB != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " symptoms = " + symptomsB;
			added = true;
		}
		if (closeContactB != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " closeContact = " + closeContactB;
			added = true;
		}
		if (quarantineB != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " quarantine = " + quarantineB;
			added = true;
		}
		if (noSymptomsB != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " noSymptoms = " + noSymptomsB;
			added = true;
		}
		if (status != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " status = '" + status + "'";
			added = true;
		}

		query = query + " Where entryRequestID = " + formNumber;

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating entryRequest");
			return 0;
		}


	}


}
