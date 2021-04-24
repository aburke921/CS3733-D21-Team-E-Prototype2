package edu.wpi.TeamE.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class RequestsDB {

	static Connection connection = makeConnection.makeConnection().getConnection();


	//CREATING TABLES:::

	/**
	 * Uses executes the SQL statements required to create the requests table.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - creatorID: this is the username of the user who created the request.
	 * - creationTime: this is a time stamp that is added to the request at the moment it is made.
	 * - requestType: this is the type of request that the user is making. The valid options are: "floral", "medDelivery", "sanitation", "security", "extTransport".
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
				"Constraint requestTypeLimit Check (requestType In ('floral', 'medDelivery', 'sanitation', 'security', 'extTransport')), " +
				"Constraint requestStatusLimit Check (requestStatus In ('complete', 'canceled', 'inProgress')))";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating requests table");
		}
	}

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
				"message       varchar(5000), " +
				"Constraint flowerTypeLimit Check (flowerType In ('Roses', 'Tulips', 'Carnations', 'Assortment')), " +
				"Constraint flowerAmountLimit Check (flowerAmount In (1, 6, 12)), " +
				"Constraint vaseTypeLimit Check (vaseType In ('Round', 'Square', 'Tall', 'None')))";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("error creating floralRequests table");
		}
	}

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
				"    signature varchar(31) Not Null, " +
				"    description varchar(5000), " +
				"    sanitationType varchar(31), " +
				"    urgency varchar(31) Not Null, " +
				"    Constraint sanitationTypeLimit Check (sanitationType In ('Urine Cleanup', 'Feces Cleanup', 'Preparation Cleanup', 'Trash Removal'))," +
				"    Constraint urgencyTypeLimit Check (urgency In ('Low', 'Medium', 'High', 'Critical')) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("error creating sanitationRequest table");
		}
	}

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
				"    ETA varchar(100), " +
				"    description varchar(5000)," +
				"    Constraint requestTypeLimitExtTrans Check (requestType In ('Ambulance', 'Helicopter', 'Plane'))" +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("error creating extTransport table");
		}
	}

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
				"dosage              varchar(31) Not Null," +
				"specialInstructions varchar(5000)," +
				"signature           varchar(31) Not Null" + ")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("error creating medDelivery table");
		}
	}

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
				"Constraint urgencyTypeLimitServ Check (urgency In ('Low', 'Medium', 'High', 'Critical')) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("error creating securityServ table");
		}
	}

// ADDING TO TABLES::::
// ADDING TO TABLES::::
// ADDING TO TABLES::::
// ADDING TO TABLES::::
// ADDING TO TABLES::::
// ADDING TO TABLES::::
// ADDING TO TABLES::::

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
	 * This adds a sanitation services form to the table specific for it
	 * @param //form this is the form being added to the table
	 */
	public static void addSanitationRequest(int userID, int assigneeID, String roomID, String sanitationType, String description, String urgency, String signature) {
		addRequest(userID, assigneeID, "sanitation");

		String insertSanitationRequest = "Insert Into sanitationrequest " +
				"Values ((Select Count(*) " +
				"         From requests), ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertSanitationRequest)) {
			prepState.setString(1, roomID);
			prepState.setString(2, signature);
			prepState.setString(3, description);
			prepState.setString(4, sanitationType);
			prepState.setString(5, urgency);

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into sanitationRequest inside function addSanitationRequest()");
		}

	}

	/**
	 * This function needs to add a external patient form to the table for external patient forms
	 * //@param form this is the form that we will create and send to the database
	 */
	public static void addExternalPatientRequest(int userID, int assigneeID, String roomID, String requestType, String severity, String patientID, String ETA, String description) {

		addRequest(userID, assigneeID, "extTransport");

		String insertExtTransport = "Insert Into exttransport " +
				"Values ((Select Count(*) " +
				"         From requests), ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertExtTransport)) {
			prepState.setString(1, roomID);
			prepState.setString(2, requestType);
			prepState.setString(3, severity);
			prepState.setString(4, patientID);
			prepState.setString(5, ETA);
			prepState.setString(6, description);

			prepState.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into extTransport inside function addExternalPatientRequest()");
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
	public static void addFloralRequest(int userID, int assigneeID, String RoomNodeID, String recipientName, String flowerType, int flowerAmount, String vaseType, String message) {
		addRequest(userID, assigneeID, "floral");

		String insertFloralRequest = "Insert Into floralrequests Values ((Select Count(*) From requests), ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertFloralRequest)) {
			prepState.setString(1, RoomNodeID);
			prepState.setString(2, recipientName);
			prepState.setString(3, flowerType);
			prepState.setInt(4, flowerAmount);
			prepState.setString(5, vaseType);
			prepState.setString(6, message);

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into floralRequests inside function addFloralRequest()");
		}
	}

	/**
	 * This adds a medicine request form to the table for medicine request forms
	 * // @param form this is the form being added
	 */
	public static void addMedicineRequest(int userID, int assigneeID, String roomID, String medicineName, int quantity, String dosage, String specialInstructions, String signature) {
		addRequest(userID, assigneeID, "medDelivery");

		String insertMedRequest = "Insert Into meddelivery Values ((Select Count(*) From requests), ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertMedRequest)) {
			prepState.setString(1, roomID);
			prepState.setString(2, medicineName);
			prepState.setInt(3, quantity);
			prepState.setString(4, dosage);
			prepState.setString(5, specialInstructions);
			prepState.setString(6, signature);

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into medicineRequest inside function addMedicineRequest()");
		}

	}

	/**
	 * This adds a security form to the table for security service form
	 * // @param form this is the form added to the table
	 */
	public static void addSecurityRequest(int userID, int assigneeID, String roomID, String level, String urgency) {
		addRequest(userID, assigneeID, "security");

		String insertSecurityRequest = "Insert Into securityserv Values ((Select Count(*) From requests), ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertSecurityRequest)) {
			prepState.setString(1, roomID);
			prepState.setString(2, level);
			prepState.setString(3, urgency);

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into securityRequest inside function addSecurityRequest()");
		}


	}

// EDITING TABLES::::
// EDITING TABLES::::
// EDITING TABLES::::
// EDITING TABLES::::
// EDITING TABLES::::
// EDITING TABLES::::
// EDITING TABLES::::

	/**
	 * This edits a Sanitation Services form that is already in the database
	 * @param requestID      the ID that specifies which sanitation form that is being edited
	 * @param description    the new description that the user is using to update their form
	 * @param roomID         the new node/room/location the user is assigning this request to
	 * @param sanitationType the new type of sanitation that the user is changing their request to
	 * @param urgency        the new urgency that the user is changing in their request
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editSanitationRequest(int requestID, String roomID, String sanitationType, String description, String urgency, String signature) {

		boolean added = false;
		String query = "update sanitationRequest set";

		if (roomID != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " roomID = '" + roomID + "'";
			added = true;
		}
		if (sanitationType != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " sanitationType = '" + sanitationType + "'";
			added = true;
		}
		if (description != null) {
			query = query + " description = '" + description + "'";

			added = true;
		}
		if (urgency != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " urgency = '" + urgency + "'";
			added = true;
		}
		if (signature != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + "signature = '" + signature + "'";
			added = true;
		}


		query = query + " where requestID = " + requestID;

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error in updating sanitation request");
			return 0;
		}


	}

	/**
	 * This edits a External Transport Services form that is already in the database
	 * @param requestID   the ID that specifies which external transfer form that is being edited
	 * @param roomID      this is the string used to update the hospital field
	 * @param requestType this is the string used to update the type
	 * @param severity    this is the string used to update the severity
	 * @param patientID   this is the string used to update patientID
	 * @param description this is the string used to update the description
	 * @param ETA         this is the string used to update the eta
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editExternalPatientRequest(int requestID, String roomID, String requestType, String severity, String patientID, String description, String ETA) {

		boolean added = false;
		String query = "update extTransport set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";

			added = true;
		}
		if (requestType != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " requestType = '" + requestType + "'";
			added = true;
		}
		if (severity != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " severity = '" + severity + "'";
			added = true;
		}
		if (patientID != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " patientID = '" + patientID + "'";
			added = true;
		}
		if (description != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " description = '" + description + "'";
			added = true;
		}
		if (ETA != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " ETA = '" + ETA + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error in updating external transport request");
			return 0;
		}
	}

	/**
	 * This edits a floral request form that is already in the database
	 * @param requestID    the ID that specifies which external transfer form that is being edited
	 * @param roomID       the new node/room/location the user is assigning this request to
	 * @param flowerType   the type of flower the user wants to change their request to
	 * @param flowerAmount the new quantity of flowers the user wants to change their request to
	 * @param vaseType     the new vase type the user wants to change their request to
	 * @param message      the new message containing either instructions or to the recipient the user wants to change
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editFloralRequest(int requestID, String roomID, String recipientName, String flowerType, Integer flowerAmount, String vaseType, String message) {

		boolean added = false;
		String query = "update floralRequests set ";

		if (recipientName != null) {
			query = query + " recipientName = '" + recipientName + "'";

			added = true;
		}
		if (roomID != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " roomID = '" + roomID + "'";
			added = true;
		}
		if (flowerType != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " flowerType = '" + flowerType + "'";
			added = true;
		}
		if (flowerAmount != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " flowerAmount = " + flowerAmount;
			added = true;
		}
		if (vaseType != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " vaseType = '" + vaseType + "'";
			added = true;
		}
		if (message != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " message = '" + message + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error in updating floral request");
			return 0;
		}
	}

	/**
	 * This function edits a current request for medicine delivery with the information below for a request already in the database
	 * @param requestID           the ID that specifies which external transfer form that is being edited
	 * @param roomID              the new node/room/location the user is assigning this request to
	 * @param medicineName        this is the name of the medicine the user is changing the request to
	 * @param quantity            this is the number of pills the user is changing the request to
	 * @param dosage              this is the dosage (ml or mg) the user is changing the request to
	 * @param specialInstructions this is the new special instructions the user is requesting
	 * @param assigneeID          this is the userID of the a new employee or administrator that will be fulfilling the request.
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editMedicineRequest(int requestID, String roomID, String medicineName, Integer quantity, String dosage, String specialInstructions, int assigneeID) {

		boolean added = false;
		String query = "update medDelivery set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";

			added = true;
		}
		if (medicineName != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " medicineName = '" + medicineName + "'";
			added = true;
		}
		if (quantity != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " quantity = " + quantity;
			added = true;
		}
		if (dosage != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " dosage = '" + dosage + "'";
			added = true;
		}
		if (specialInstructions != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " specialInstructions = '" + specialInstructions + "'";
			added = true;
		}
		if (assigneeID != 0) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " assigneeID = '" + assigneeID + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error in updating medicine request");
			return 0;
		}
	}

	/**
	 * This edits a security form already within the database
	 * @param requestID the ID that specifies which external transfer form that is being edited
	 * @param roomID    the new node/room/location the user is assigning this request to
	 * @param level     this is the info to update levelOfSecurity
	 * @param urgency   this is the info to update levelOfUrgency
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editSecurityRequest(int requestID, String roomID, String level, String urgency) {
		boolean added = false;
		String query = "update securityServ set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";

			added = true;
		}
		if (level != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + "level = '" + level + "'";
			added = true;
		}
		if (urgency != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + "urgency = '" + urgency + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error in updating security request");
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
			if (added == true) {
				query = query + ", ";
			}
			query = query + "Set requestStatus = '" + requestStatus + "'";

		}

		query = query + " where requestID = " + requestID;
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

// QUERYING TABLES::::
// QUERYING TABLES::::
// QUERYING TABLES::::
// QUERYING TABLES::::
// QUERYING TABLES::::
// QUERYING TABLES::::
// QUERYING TABLES::::

	/**
	 * Gets a list of all the "assigneeIDs", "requestIDs", or "requestStatus" from the requests with the given type done by the given userID
	 * @param tableName this is the name of the table that we are getting the info from
	 * @param userID    this is the ID of the user who made the request
	 * @param infoType  this is the type of information that is being retrieved
	 * @return an ArrayList<String> with the desired info
	 */
	public static ArrayList<String> getMyCreatedRequestInfo(String tableName, int userID, String infoType) {

		ArrayList<String> listOfInfo = new ArrayList<>();

		String query;
		if (userID >= -1) {
			query = "Select " + infoType + " From requests Natural Join " + tableName + " Where creatorID = " + userID;
		} else {
			query = "Select " + infoType + " From requests Natural Join " + tableName;
		}

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();

			while (rset.next()) {
				if (infoType.equals("AssigneeID")) {
					int ID = rset.getInt(infoType);
					listOfInfo.add(String.valueOf(ID));
				} else {
					String ID = rset.getString(infoType); // potential issue // -TO-DO-: won't work with int AssigneeIDs? Fixed by translating IDs to String, should it return a pair of Assignee ID and name?
					listOfInfo.add(ID);
				}
			}
			rset.close();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("getRequestInfo() got a SQLException");
		}
		return listOfInfo;
	}

	/**
	 * Gets a list of all the "creatorIDs", "requestIDs", or "requestStatus" from the requests with the given type assigned to the given userID
	 * @param tableType this is the name of the table that we are getting the info from
	 * @param userID    this is the ID of the user who made the request
	 * @param infoType  this is the type of information that is being retrieved
	 * @return an ArrayList<String> with the desired info
	 */
	public static ArrayList<String> getMyAssignedRequestInfo(String tableType, int userID, String infoType) {

		ArrayList<String> listOfInfo = new ArrayList<>();

		String query;
		if (userID >= -1) {
			query = "Select " + infoType + " From requests Natural Join " + tableType + " Where assigneeID = " + userID;
		} else {
			query = "Select " + infoType + " From requests Natural Join " + tableType;
		}

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();

			while (rset.next()) {
				String ID = rset.getString(infoType); //potential issue
				listOfInfo.add(ID);
			}
			rset.close();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("getRequestInfo() got a SQLException");
		}

		return listOfInfo;
	}

	/**
	 * Gets a list of all the longNames for the location from the given tableName
	 * @param tableType this is the name of the table that we are getting the requestIDs from
	 * @return a list of all longNames for the location for all of the requests
	 */
	public static ArrayList<String> getRequestLocations(String tableType, int userID) {
		ArrayList<String> listOfLongNames = new ArrayList<String>();

		String tableName = "";
		switch (tableType) {
			case "floral":
				tableName = "floralRequests";
				break;
			case "medDelivery":
				tableName = "medDelivery";
				break;
			case "sanitation":
				tableName = "sanitationRequest";
				break;
			case "security":
				tableName = "securityServ";
				break;
			case "extTransport":
				tableName = "extTransport";
				break;
		}

		String query;

		if (userID >= -1) {
			query = "Select longName from node, (Select roomID From " + tableName + ", (Select requestID from requests Where requestType = '" + tableType + "' and creatorID = " + userID + ") correctType where correctType.requestID = " + tableName + ".requestID) correctStuff where correctStuff.roomID = node.nodeID";
		} else {
			query = "Select requestID, longname From node,(Select requestid, roomid From " + tableName + ") correctTable Where node.nodeid = correctTable.roomID Order By requestID";
		}
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();
			while (rset.next()) {
				String status = rset.getString("longName");
				listOfLongNames.add(status);
			}

			rset.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("getRequestLocations() got a SQLException");
		}
		return listOfLongNames;
	}

	/**
	 * gets a list of available assignees based on the userType given
	 * @param givenUserType is type of user (i.e. "nurse", "EMT", "doctor", etc.)
	 * @return a HashMap of possible assignees for the specific request type
	 */
	public static HashMap<Integer, String> getSelectableAssignees(String givenUserType) {
		HashMap<Integer, String> listOfAssignees = new HashMap<>();

		String query = "Select firstName, lastName, userID from userAccount where userType = '" + givenUserType + "'";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();
			while (rset.next()) {
				String firstName = rset.getString("firstName");
				String lastName = rset.getString("lastName");
				int assigneeID = rset.getInt("userID");
				String fullName = firstName + " " + lastName;
				listOfAssignees.put(assigneeID, fullName);
			}

			rset.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("getAvailableAssignees() got a SQLException");
		}
		return listOfAssignees;
	}
}
