package edu.wpi.TeamE.databases;

import edu.wpi.TeamE.algorithms.Edge;
import edu.wpi.TeamE.algorithms.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class makeConnection {


	// static variable singleInstance of type SingleConnection
	public static makeConnection singleInstance = null;

	public Connection connection;

	// private constructor restricted to this class itself
	public makeConnection() {
		// Initialize DB
		System.out.println("Starting connection to Apache Derby\n");
		try {

			//Makes it so a username and password (hardcoded) is needed to access the database data
			Properties props = new Properties();
			props.put("user", "admin");
			props.put("password", "admin");

			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

			try {
				/*
				 * Before making this connectin make sure you're database tab in Intellij
				 * Is not connected to the database! This will cause the DriverManager to
				 * Throw an SQLException and goof a bunch of stuff up!
				 */
				this.connection = DriverManager.getConnection("jdbc:derby:BWDB;create=true", props);
				// this.connection.setAutoCommit(false);
			} catch (SQLException e) {
				// e.printStackTrace();
				System.err.println("error with the DriverManager, check if you have connected to database in IntelliJ");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("error with the EmbeddedDriver class.forName thing");
		}
	}

	// static method to create instance of Singleton class
	public static makeConnection makeConnection() {
		// To ensure only one instance is created
		if (singleInstance == null) {
			singleInstance = new makeConnection();
		}
		return singleInstance;
	}

	public static void main(String[] args) {/*
		System.out.println("STARTING UP!!!");
		File nodes = new File("src/main/resources/edu/wpi/TeamE/csv/bwEnodes.csv");
		File edges = new File("src/main/resources/edu/wpi/TeamE/csv/bwEedges.csv");

		makeConnection connection = makeConnection.makeConnection();
		connection.deleteAllTables();
		try {
			connection.createTables();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.populateTable("node", nodes);
		connection.populateTable("hasEdge", edges);

		connection.getNewCSVFile("node");
		connection.getNewCSVFile("hasEdge");
		connection.addNode("test1", 0, 0,"test", "test", "test", "test", "test");
		connection.addNode("test2", 2, 2,"test", "test", "test", "test", "test");
		connection.addNode("test3", 3, 3,"test", "test", "test", "test", "test");
		connection.addNode("test4", 4, 4,"test", "test", "test", "test", "test");

		connection.addEdge("test1_test2", "test1", "test2");
		connection.addEdge("test2_test3", "test2", "test3");
		connection.addEdge("test1_test3", "test1", "test3");

		//int i = connection.modifyEdge("test1_test2", null, "test3");
		//System.out.println(i);*/
	}

	/**
	 * Calls all of the functions that creates each individual table
	 */
	public void createTables(){
		createNodeTable();
		createEdgeTable();
		createUserAccountTable();
		createRequestsTable();
		createFloralRequestsTable();
		createExtTransportTable();
		createSanitationTable();
	}
	/**
	 * Uses executes the SQL statements required to create the node table.
	 * This table has the attributes:
	 * - nodeID: this is a unique identifier for the each node. Every node must contain a nodeID.
	 * - xCoord: this is the X-Coordinate/pixel location of the node on the map of the hospital.
	 * - yCoord: this is the Y-Coordinate/pixel location of the node on the map of the hospital.
	 * - floor: this is the floor of the hospital that the node is located on. The available options are: "1", "2", "3", "L1", "L2"
	 * - building: this is the building of the hospital that the node is located in. The available options are: "BTM", "45 Francis", "Tower",
	 * "15 Francis", "Shapiro", "Parking".
	 * - nodeType: this is the type room/location that the node is specifying. The available options are: "PARK" (parking), "EXIT" (exit),
	 * "WALK" (sidewalk/out door walkway), "HALL' (indoor walkway), "CONF" (conference room), "DEPT" (department room), "ELEV" (elevator),
	 * "INFO" (information), "LABS" (lab testing/results room), "REST" (rest areas/sitting areas), "RETL" (retail/food and shopping),
	 * "STAI" (stairs), "SERV" (services), "BATH" (bathrooms).
	 * longName: this is the long version/more descriptive name of the node/location/room
	 * shortName: this is the short/nickname of the node/location/room
	 */
	public void createNodeTable() {

		try {
			Statement stmt = this.connection.createStatement();
			stmt.execute(
					"Create Table node"
							+ "("
							+ "    nodeID    varchar(31) Primary Key,"
							+ "    xCoord    int Not Null,"
							+ "    yCoord    int Not Null,"
							+ "    floor     varchar(5) Not Null,"
							+ "    building  varchar(20),"
							+ "    nodeType  varchar(10),"
							+ "    longName  varchar(100),"
							+ "    shortName varchar(100),"
							+ "    Unique (xCoord, yCoord, floor)"
							+ ")");

		} catch (SQLException e) {
			 e.printStackTrace();
			System.err.println("error creating node table");
		}

	}

	/**
	 * Uses executes the SQL statements required to create the hasEdge table.
	 * This table has the attributes:
	 * - edgeID: this is a unique identifier for the edge connection. Every edge must contain an edgeID.
	 * - startNode: this is a nodeID in which the edge connection starts.
	 * - endNode: this is a nodeID in which the edge connection ends.
	 */
	public void createEdgeTable() {
		try {

			Statement stmt = connection.createStatement();
			stmt.execute(
					"Create Table hasEdge"
							+ "("
							+ "    edgeID    varchar(63) Primary Key,"
							+ "    startNode varchar(31) Not Null References node (nodeid) On Delete Cascade,"
							+ "    endNode   varchar(31) Not Null References node (nodeid) On Delete Cascade, "
							+ "    length    float, "
							+ "    Unique (startNode, endNode)"
							+ ")");

			// Needs a way to calculate edgeID, either in Java or by a sql trigger
			// Probably in Java since it's a PK

		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating hasEdge table");
		}
	}

	/**
	 * Uses executes the SQL statements required to create the userAccount table.
	 * This table has the attributes:
	 * - userID: used to identify the individual. Every account must have a unique userID and account must have one.
	 * - email: the email must be under 31 characters long and must be unique.
	 * - password: the password must be under 31 characters long.
	 * - userType: is the type of account the user is enrolled with. The valid options are: "visitor", "patient", "doctor", "admin".
	 * - firstName: the user's first name.
	 * - lastName: the user's last name.
	 */
	public void createUserAccountTable(){
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create Table userAccount(" +
					"userID    int Primary Key, " +
					"email     varchar(31) Unique, " +
					"password  varchar(31), " +
					"userType  varchar(31), " +
					"firstName varchar(31), " +
					"lastName  varchar(31), " +
					"Constraint userTypeLimit Check (userType In ('visitor', 'patient', 'doctor', 'admin')))";
			stmt.execute(sqlQuery);
			createUserAccountTypeViews();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("error creating userAccount table");
		}


	}

	public void addUserAccount(int userID, String email, String password, String userType, String firstName, String lastName) {
		String insertUser = "Insert into userAccount Values (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertUser)) {
			prepState.setInt(1, userID);
			prepState.setString(2, email);
			prepState.setString(3, password);
			prepState.setString(4, userType);
			prepState.setString(5, firstName);
			prepState.setString(6, lastName);

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into userAccount inside function insertUserAccount()");
		}


	}

	/**
	 * Uses executes the SQL statements required to create views for different types of users. The views created
	 * are: visitorAccount, patientAccount, doctorAccount, adminAccount.
	 */
	public void createUserAccountTypeViews(){
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View visitorAccount As " +
					"Select * " +
					"From userAccount " +
					"Where userType = 'visitor'";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating visitorAccount view");
		}
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View patientAccount As " +
					"Select * " +
					"From userAccount " +
					"Where userType = 'patient'";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating patientAccount view");
		}
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View doctorAccount As " +
					"Select * " +
					"From userAccount " +
					"Where userType = 'doctor'";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating doctorAccount view");
		}

		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View adminAccount As " +
					"Select * " +
					"From userAccount " +
					"Where userType = 'admin'";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating adminAccount view");
		}

	}


	/**
	 * Uses executes the SQL statements required to create the requests table.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - creatorID: this is the username of the user who created the request.
	 * - creationTime: this is a time stamp that is added to the request at the moment it is made.
	 * - requestType: this is the type of request that the user is making. The valid options are: "floral", "medDelivery", "sanitation", "security", "extTransport".
	 * - requestState: this is the state in which the request is being processed. The valid options are: "complete", "canceled", "inProgress".
	 */
	public void createRequestsTable(){
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create Table requests(" +
					"requestID    int Primary Key,\n" +
					"creatorID    int References userAccount On Delete Cascade," +
					"creationTime timestamp,\n" +
					"requestType  varchar(31),\n" +
					"requestState varchar(10),\n" +
					"Constraint requestTypeLimit Check (requestType In ('floral', 'medDelivery', 'sanitation', 'security', 'extTransport')), " +
					"Constraint requestStateLimit Check (requestState In ('complete', 'canceled', 'inProgress')))";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating requests table");
		}
	}


	/**
	 * Uses executes the SQL statements required to create a floralRequests table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user is sending the request to.
	 * - recipientName: this is the name of the individual they want the flowers to be addressed to
	 * - flowerType: this is the type of flowers that the user wants to request
	 * - flowerAmount: this the number/quantity of flowers that the user is requesting
	 * - vaseType: this is the type of vase the user wants the flowers to be delivered in
	 * - message: this is a specific detailed message that the user can have delivered with the flowers or an instruction message
	 * 	 *                for whoever is fufilling the request
	 */
	public void createFloralRequestsTable(){
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create Table floralRequests( " +
					"requestID     int Primary Key References requests On Delete Cascade, " +
					"roomID        varchar(31) References node On Delete Cascade, " +
					"recipientName varchar(31), " +
					"flowerType    varchar(31), " +
					"flowerAmount  int, " +
					"vaseType      varchar(31), " +
					"message       varchar(5000), " +
					"Constraint flowerTypeLimit Check (flowerType In ('Roses', 'Tulips', 'Carnations', 'Assortment')), " +
					"Constraint flowerAmountLimit Check (flowerAmount In (1, 6, 12)),\n" +
					"Constraint vaseTypeLimit Check (vaseType In ('Round', 'Square', 'Tall', 'None')))";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating floralRequests table");
		}
	}

	public void createSanitationTable(){
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create Table sanitationRequest(\n" +
					"    userID int Primary Key References requests On Delete Cascade,\n" +
					"    roomID varchar(31) not null References node On Delete Cascade,\n" +
					"    signature varchar(31) NOT NULL,\n" +
					"    description varchar(5000),\n" +
					"    sanitationType varchar(31),\n" +
					"    urgency varchar(31) NOT NULL,\n" +
					"    Constraint sanitationTypeLimit Check (sanitationType In ('Urine Cleanup', 'Feces Cleanup', 'Preparation Cleanup', 'Trash Removal'))\n" +
					")";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating sanitationRequest table");
		}
	}

	public void createExtTransportTable() {
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create Table extTransport(\n" +
					"    requestID int Primary Key References requests On Delete Cascade,\n" +
					"    hospitalLocation varchar(100) NOT NULL,\n" +
					"    severity varchar(30) NOT NULL,\n" +
					"    patientID int NOT NULL,\n" +
					"    ETA varchar(100),\n" +
					"    description varchar(5000)\n" +
					")";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating extTransport table");
		}
	}

	/**
	 * Deletes node,hasEdge, userAccount, requests, and floralRequests tables.
	 * Also deletes adminAccount, doctorAccount, patientAccount, visitorAccount views
	 * try/catch phrase set up in case the tables all ready do not exist
	 */
	public void deleteAllTables() {

		try {
			Statement stmt = this.connection.createStatement();
			stmt.execute("DROP table extTransport");
			stmt.execute("Drop Table sanitationRequest");
			stmt.execute("Drop Table floralRequests");
			stmt.execute("Drop Table requests");
			stmt.execute("Drop View visitorAccount");
			stmt.execute("Drop View patientAccount");
			stmt.execute("Drop View doctorAccount");
			stmt.execute("Drop View adminAccount");
			stmt.execute("Drop Table userAccount");
			stmt.execute("Drop Table hasEdge");
			stmt.execute("Drop Table node");
			stmt.close();
		} catch (SQLException e) {
//			 e.printStackTrace();
			System.err.println("deleteAllTables() not working");
		}
	}

	public void deleteEdgeTable() {

		try {
			Statement stmt = this.connection.createStatement();
			stmt.execute("Drop Table hasedge");
			stmt.close();
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("deleteEdgeTable() not working");
		}

	}

	public void deleteNodeTable() {

		try {
			Statement stmt = this.connection.createStatement();
			stmt.execute("Drop Table node");
			stmt.close();
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("deleteNodeTable() not working");
		}

	}

	/**
	 * Reads csv & inserts into table
	 *
	 * @param tableName name of the table that needs to be populated
	 * @param file      file that is going to be read
	 */
	public void populateTable(String tableName, File file) {

		try {
			// creates a file with the file name we are looking to read
			// File file = new File(csvFileName);
			// used to read data from a file
			FileReader fr = new FileReader(file);

			// used to read the text from a character-based input stream.
			BufferedReader br = new BufferedReader(fr);

			String line;
			String[] tempArr;

			// reads first line (this is the header of each file and we don't need it)
			br.readLine();

			// if there is something in the file (after line 1)
			while ((line = br.readLine()) != null) {

				// adds arguments into the array separated by the commas ("," is when it knows the next
				// index)
				tempArr = line.split(",");

				String sqlQuery = "";
				if (tableName.equals("node")) {
					sqlQuery =
							"INSERT INTO node VALUES ("
									+ "'"
									+ tempArr[0]
									+ "',"
									+ Integer.valueOf(tempArr[1])
									+ ","
									+ Integer.valueOf(tempArr[2])
									+ ",'"
									+ tempArr[3]
									+ "',"
									+ " '"
									+ tempArr[4]
									+ "',"
									+ " '"
									+ tempArr[5]
									+ "',"
									+ " '"
									+ tempArr[6]
									+ "',"
									+ " '"
									+ tempArr[7]
									+ "')";
				}
				if (tableName.equals("hasEdge")) {
					sqlQuery =
							"INSERT INTO hasEdge VALUES ('"
									+ tempArr[0]
									+ "', '"
									+ tempArr[1]
									+ "', '"
									+ tempArr[2]
									+ "', NULL"
									+ ")";
				}


				try {
					Statement stmt = this.connection.createStatement();

					stmt.execute(sqlQuery);

					if (tableName.equals("hasEdge")) {
						//System.out.println("Calling addLength");
						addLength(tempArr[1], tempArr[2]);
						//System.out.println("after calling addLength");
					}

					stmt.close();

				} catch (SQLException e) {
					e.printStackTrace();
					System.err.println("populateTable() inner try/catch error");
				}
			}

			// closes the BufferedReader
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.println("populateTable() outer try/catch error");
		}
	}

	public void addLength(String startNode, String endNode) {

		String sqlQuery;
		int startX = 0;
		int startY = 0;
		int endX = 0;
		int endY = 0;

		ResultSet rset;
		Statement stmt;

		try {
			stmt = this.connection.createStatement();
			sqlQuery = "SELECT xCoord, yCoord FROM node WHERE nodeID = '" + startNode + "'";
			rset = stmt.executeQuery(sqlQuery);

			while (rset.next()) {
				startX = rset.getInt("xCoord");
				startY = rset.getInt("yCoord");
			}
			rset.close();
			stmt.close();

		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("WHERE nodeID = startNode try/catch failed");
		}

		try {
			stmt = this.connection.createStatement();
			sqlQuery = "SELECT xCoord, yCoord FROM node WHERE nodeID = '" + endNode + "'";
			//executes the SQL insert statement (inserts the data into the table)
			rset = stmt.executeQuery(sqlQuery);

			while (rset.next()) {
				endX = rset.getInt("xCoord");
				endY = rset.getInt("yCoord");
			}
			rset.close();
			stmt.close();

		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("WHERE nodeID = endNode try/catch failed");
		}


		try {
			double length;
			double xLength = Math.pow((startX - endX), 2);
			double yLength = Math.pow((startY - endY), 2);

			length = Math.sqrt(xLength + yLength);


			stmt = this.connection.createStatement();

			sqlQuery = "UPDATE hasEdge SET length = " + length + " WHERE startNode = '" + startNode + "' AND endNode = '" + endNode + "'";

			//executes the SQL insert statement (inserts the data into the table)
			stmt.executeUpdate(sqlQuery);

			stmt.close();

		} catch (SQLException e) {
			System.err.println("UPDATE try/catch failed");
		}


	}


	/**
	 * This adds a sanitation services form to the table specific for it
	 * @param //form this is the form being added to the table
	 */
	public void addSanitationRequest(int userID, String roomID, String sanitationType, String description, String urgency, String signature) {
		String insertRequest = "Insert Into requests\n" +
				"Values ((Select Count(*)\n" +
				"         From requests) + 1, ?, current Timestamp, 'floral', 'inProgress')";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);
			prepState.execute();
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error inserting into requests inside function addSanitationRequest()");
		}

		String insertSanitationRequest = "Insert Into sanitationRequest\n" +
				"Values ((Select Count(*)\n" +
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
	 * This edits a Sanitation Services form that is already in the table
	 * @param departmentField this updates the department
	 * @param roomField this updates the room field
	 * @param numberField this updates the number field
	 * @param serviceTypeField this updates the service type field
	 * @param assignee this updates who is assigned to the service request
	 */
	public void editSanitationRequest(String departmentField, String roomField, String numberField, String serviceTypeField, String assignee) {}


	/**
	 * This function needs to add a external patient form to the table for external patient forms
	 * @param //form this is the form that we will create and send to the database
	 */
	public void addExternalPatientRequest(int userID, String hospitalLocation, String severity, String patientID, String ETA, String description) {

		String insertRequest = "Insert Into requests\n" +
				"Values ((Select Count(*)\n" +
				"         From requests) + 1, ?, current Timestamp, 'extTransport', 'inProgress')";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into requests inside function addExternalPatientRequest()");
		}

		String insertExtTransport = "Insert Into extTransport\n" +
				"Values ((Select Count(*)\n" +
				"         From requests), ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertExtTransport)) {
			prepState.setString(1, hospitalLocation);
			prepState.setString(2, severity);
			prepState.setString(3, patientID);
			prepState.setString(4, ETA);
			prepState.setString(5, description);

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into extTransport inside function addExternalPatientRequest()");
		}

	}


	/**
	 *
	 * @param hospital this is the string used to update the hospital field
	 * @param type this is the string used to update the type
	 * @param severity this is the string used to update the severity
	 * @param patientID this is the string used to update patientID
	 * @param description this is the string used to update the description
	 * @param eta this is the string used to update the eta
	 */
	public void editExternalPatientRequest(String hospital, String type, String severity, String patientID, String description, String eta) {}


	/**
	 * This adds a floral request to the database that the user is making
	 * @param userID this is the username that the user uses to log into the account
	 * @param RoomNodeID this is the nodeID/room the user is sending the request to
	 * @param recipientName this is the name of the individual they want the flowers to be addressed to
	 * @param flowerType this is the type of flowers that the user wants to request
	 * @param flowerAmount this the number/quantity of flowers that the user is requesting
	 * @param vaseType this is the type of vase the user wants the flowers to be delivered in
	 * @param message this is a specific detailed message that the user can have delivered with the flowers or an instruction message
	 *                for whoever is fufilling the request
	 */
	public void addFloralRequest(int userID, String RoomNodeID, String recipientName, String flowerType, int flowerAmount, String vaseType, String message){
		String insertRequest = "Insert Into requests\n" +
				"Values ((Select Count(*)\n" +
				"         From requests) + 1, ?, current Timestamp, 'floral', 'inProgress')";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);
			ResultSet rset = prepState.executeQuery();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into requests inside function addFloralRequest()");
		}


		String insertFloralRequest = "Insert Into floralRequests\n" +
				"Values ((Select Count(*)\n" +
				"         From requests), ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertFloralRequest)) {
			prepState.setString(1, RoomNodeID);
			prepState.setString(2, recipientName);
			prepState.setString(3, flowerType);
			prepState.setInt(4, flowerAmount);
			prepState.setString(5, vaseType);
			prepState.setString(6, message);

			ResultSet rset = prepState.executeQuery();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into floralRequests inside function addFloralRequest()");
		}

//			Create Table requests(
	//			requestID    int Primary Key,
	//			userID    int References userAccount On Delete Cascade,
	//			creationTime timestamp,
	//			requestType  varchar(31),
	//			requestState varchar(10),
	//			Constraint requestTypeLimit Check (requestType In ('floral', 'medDelivery', 'sanitation', 'security', 'extTransport')),
	//			Constraint requestStateLimit Check (requestState In ('complete', 'canceled', 'inProgress'))
//          );

//			Create Table floralRequests(
//				requestID     int Primary Key References requests On Delete Cascade,
//			    roomID        varchar(31) References node On Delete Cascade,
//				recipientName varchar(31),
//				flowerType    varchar(31),
//				flowerAmount  int,
//			    vaseType      varchar(31),
//				message       varchar(255),
//				Constraint flowerTypeLimit Check (flowerType In ('Roses', 'Tulips', 'Carnations', 'Assortment')),
//			    Constraint flowerAmountLimit Check (flowerAmount In (1, 6, 12)),
//				Constraint vaseTypeLimit Check (vaseType In ('Round', 'Square', 'Tall', 'None'))
//          );
	}


	/**
	 * This edits a floral request form within the table
	 * @param patient this is the string to update patient info
	 * @param room this is the string to update room info
	 * @param flowerType this is the string to update the flower type
	 * @param flowerAmount this is the string to update the flower amount
	 * @param vaseType this is the string to update the vase type
	 * @param message this is the string to update the message
	 */
	public void editFloralRequest(String patient, String room, String flowerType, String flowerAmount, String vaseType, String message) {}


	/**
	 * This adds a medicine request form to the table for medicine request forms
	 * @param //form this is the form being added
	 */
	public void addMedicineRequest(int userID, String medicineName, String quantity, String dosage, String specialInstructions, String signature) {

		String insertRequest = "Insert Into requests\n" +
				"Values ((Select Count(*)\n" +
				"         From requests) + 1, ?, current Timestamp, 'floral', 'inProgress')";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);
			ResultSet rset = prepState.executeQuery();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into requests inside function addMedicineRequest()");
		}


		String insertMedRequest = "Insert Into medDelivery\n" +
				"Values ((Select Count(*)\n" +
				"         From requests), ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertMedRequest)) {
			prepState.setString(1, medicineName);
			prepState.setString(2, quantity);
			prepState.setString(3, dosage);
			prepState.setString(4, specialInstructions);
			prepState.setString(5, signature);

			ResultSet rset = prepState.executeQuery();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into medicineRequest inside function addMedicineRequest()");
		}

	}


	/**
	 * This function edits a current request for medicine delivery with the information below
	 * @param roomNumber this string updates the room number
	 * @param department this string is used to update the department
	 * @param medicineName this string updates the medicine name
	 * @param quantity this string updates the quantity
	 * @param dosage this string updates the dosage
	 * @param specialInstructions this string updates the special instructions
	 */
	public void editMedicineRequest(String roomNumber, String department, String medicineName, String quantity, String dosage, String specialInstructions) {}


	/**
	 * This adds a security form to the table for security service form
	 * @param //form this is the form added to the table
	 */
	public void addSecurityRequest(int userID, String level, String urgency, String reason, String assignee){

		String insertRequest = "Insert Into requests\n" +
				"Values ((Select Count(*)\n" +
				"         From requests) + 1, ?, current Timestamp, 'security', 'inProgress')";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);
			ResultSet rset = prepState.executeQuery();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into requests inside function addSecurityRequest()");
		}


		String insertSecurityRequest = "Insert Into security\n" +
				"Values ((Select Count(*)\n" +
				"         From requests), ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertSecurityRequest)) {
			prepState.setString(1, level);
			prepState.setString(2, urgency);
			prepState.setString(3, reason);
			prepState.setString(4, assignee);

			ResultSet rset = prepState.executeQuery();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into securityRequest inside function addSecurityRequest()");
		}


	}


	/**
	 * This edits a security form already within the table
	 * @param location this is the info to update location
	 * @param levelOfSecurity this is the info to update levelOfSecurity
	 * @param levelOfUrgency this is the info to update levelOfUrgency
	 * @param assignee this is the info used to update who is assigned
	 */
	public void editSecurityRequest(String location, String levelOfSecurity, String levelOfUrgency, String assignee) {}


	/**
	 * gets a node's all attributes given nodeID
	 *
	 * @return a Node object with the matching nodeID
	 */

	public Node getNodeInfo(String nodeID) {
		String getNodeInfoS = "Select * From node Where nodeid = ?";
		try (PreparedStatement getNodeInfoPS = connection.prepareStatement(getNodeInfoS)) {
			getNodeInfoPS.setString(1, nodeID);

			ResultSet getNodeInfoRS = getNodeInfoPS.executeQuery();

			while (getNodeInfoRS.next()) {
				int xCoord = getNodeInfoRS.getInt("xCoord");
				int yCoord = getNodeInfoRS.getInt("yCoord");
				String floor = getNodeInfoRS.getString("floor");
				String building = getNodeInfoRS.getString("building");
				String nodeType = getNodeInfoRS.getString("nodeType");
				String longName = getNodeInfoRS.getString("longName");
				String shortName = getNodeInfoRS.getString("shortName");
				return new Node(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName);
			}

			getNodeInfoRS.close();
			getNodeInfoPS.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("cannot print out nodeInfo");
			return null;
		}
		return null;
	}

	/**
	 * Given a NodeID, gives the xCoord, yCoord, Floor and Type of that node from Nodes
	 *
	 * @param nodeID is the nodeID of the nodes you want info from
	 * @return a Node with only xCoord, yCoord, floor and nodeType not null
	 */
	public Node getNodeLite(String nodeID) {
		String getNodeLiteS = "Select xcoord, ycoord, floor, nodetype From node Where nodeid = ?";
		try (PreparedStatement getNodeLitePS = connection.prepareStatement(getNodeLiteS)) {
			getNodeLitePS.setString(1, nodeID);
			ResultSet getNodeLiteRS = getNodeLitePS.executeQuery();

			while (getNodeLiteRS.next()) {
				int xCoord = getNodeLiteRS.getInt("xCoord");
				int yCoord = getNodeLiteRS.getInt("yCoord");
				String floor = getNodeLiteRS.getString("floor");
				String nodeType = getNodeLiteRS.getString("nodeType");
				return new Node(nodeID, xCoord, yCoord, floor, null, nodeType, null, null);
			}

			getNodeLiteRS.close();
			getNodeLitePS.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Could not get nodeLite Info");
			return null;
		}
		return null;
	}

	/**
	 * gets edge information for all edges containing given nodeID
	 *
	 * @param nodeID
	 * @return Pair<Integer, String> map with edge information
	 */
	public ArrayList<Pair<Float, String>> getEdgeInfo(String nodeID) {

		ArrayList<Pair<Float, String>> pair = new ArrayList<Pair<Float, String>>();

		try {
			Statement stmt = this.connection.createStatement();
			String query = "select * from hasEdge where endNode = '" + nodeID + "'";
			ResultSet rset = stmt.executeQuery(query);

			while (rset.next()) {
				float length = rset.getFloat("length");
				String startNodeID = rset.getString("startNode");

				pair.add(new Pair<>(length, startNodeID));
			}

			rset.close();
			stmt.close();

		} catch (SQLException e) {
			System.err.println("startNode Error");
		}

		try {
			Statement stmt = this.connection.createStatement();
			String query = "select * from hasEdge where startNode = '" + nodeID + "'";
			ResultSet rset = stmt.executeQuery(query);

			while (rset.next()) {
				float length = rset.getFloat("length");
				String endNodeID = rset.getString("endNode");

				pair.add(new Pair<>(length, endNodeID));
			}

			rset.close();
			stmt.close();
		} catch (SQLException e) {
			System.err.println("endNode Error");
		}

		return pair;
	}

	/**
	 * gets all Nodes (each row in node table)
	 *
	 * @return ArrayList of Node objects
	 * need Node constructor from UI/UX team
	 */

	public ArrayList<Node> getAllNodes() {
		ArrayList<Node> nodesArray = new ArrayList<>();
//observable list -- UI
		try {
			Statement stmt = this.connection.createStatement();
			String query = "Select * From node";
			ResultSet rset = stmt.executeQuery(query);

			while (rset.next()) {
				String NodeID = rset.getString("nodeID");
				int xCoord = rset.getInt("xCoord");
				int yCoord = rset.getInt("yCoord");
				String floor = rset.getString("floor");
				String building = rset.getString("building");
				String nodeType = rset.getString("nodeType");
				String longName = rset.getString("longName");
				String shortName = rset.getString("shortName");

				nodesArray.add(new Node(NodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName));

			}

			rset.close();
			stmt.close();

		} catch (SQLException e) {
			System.err.println("getAllNodes Error : " + e);
		}
		return nodesArray;
	}

	/**
	 * gets all Nodes that have the given FLOOR value
	 *
	 * @param floorName the value to check for in FLOOR column
	 * @return ArrayList of Node objects
	 */
	public ArrayList<Node> getAllNodesByFloor(String floorName) {
		ArrayList<Node> nodesArray = new ArrayList<>();
		try {
			Statement stmt = this.connection.createStatement();
			String query = "select * from node WHERE '" + floorName + "' = FLOOR";
			ResultSet rset = stmt.executeQuery(query);

			while (rset.next()) {
				String NodeID = rset.getString("nodeID");
				int xCoord = rset.getInt("xCoord");
				int yCoord = rset.getInt("yCoord");
				String floor = rset.getString("floor");
				String building = rset.getString("building");
				String nodeType = rset.getString("nodeType");
				String longName = rset.getString("longName");
				String shortName = rset.getString("shortName");

				nodesArray.add(new Node(NodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName));

			}

			rset.close();
			stmt.close();

		} catch (SQLException e) {
			System.err.println("getAllNodes Error : " + e);
		}
		return nodesArray;
	}

	/**
	 * Gets all node long names for a specified FLOOR column value.
	 *
	 * @param floorName the value to check for in FLOOR column
	 * @return ObservableList of node long names.
	 */
	public ObservableList<String> getAllNodeLongNamesByFloor(String floorName) {
		ObservableList<String> listOfNodeIDs = FXCollections.observableArrayList();

		String deleteNodeS = "SELECT LONGNAME FROM node WHERE '" + floorName + "' = FLOOR";
		try (PreparedStatement deleteNodePS = connection.prepareStatement(deleteNodeS)) {

			ResultSet rset = deleteNodePS.executeQuery();

			while (rset.next()) {
				String nodeID = rset.getString("LONGNAME");
				listOfNodeIDs.add(nodeID);
			}
			rset.close();
			deleteNodePS.close();

			return listOfNodeIDs;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("getListofNodeIDS error try/catch");
			return listOfNodeIDs;
		}
	}


	public ArrayList<String> getListOfNodeIDSByFloor(String floorName) {
		ArrayList<String> listOfNodeIDs = new ArrayList<>();

		String deleteNodeS = "SELECT nodeID FROM node WHERE '" + floorName + "' = FLOOR";
		try (PreparedStatement deleteNodePS = connection.prepareStatement(deleteNodeS)) {

			ResultSet rset = deleteNodePS.executeQuery();

			while (rset.next()) {
				String nodeID = rset.getString("nodeID");
				listOfNodeIDs.add(nodeID);
			}
			rset.close();
			deleteNodePS.close();

			return listOfNodeIDs;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("getListofNodeIDS error try/catch");
			return listOfNodeIDs;
		}

	}

	/**
	 * gets all edges and each edge's attribute
	 *
	 * @return ArrayList<Edge>
	 */
	public ArrayList<Edge> getAllEdges() {
		ArrayList<Edge> edgesArray = new ArrayList<>();
		try {
			Statement stmt = this.connection.createStatement();
			String query = "Select * From hasedge";
			ResultSet rset = stmt.executeQuery(query);

			while (rset.next()) {
				String edgeID = rset.getString("edgeID");
				String startNode = rset.getString("startNode");
				String endNode = rset.getString("endNode");
				double length = rset.getDouble("length");
				edgesArray.add(new Edge(edgeID, startNode, endNode, length));
			}

			rset.close();
			stmt.close();

		} catch (SQLException e) {
			System.err.println("getAllEdges Error : " + e);
		}
		return edgesArray;
	}

	/**
	 * counts the number of nodes already in the database of the given type and on the given floor that starts with the given teamNum
	 *
	 * @param teamNum be a String like "E"
	 * @param Floor   is a String like "L2"
	 * @param Type    is the nodeType of the node, like "ELEV"
	 * @return is how many nodes are already in the database given the params
	 */
	public int countNodeTypeOnFloor(String teamNum, String Floor, String Type) {
		String queryS = "select count(*) as countNum from node where nodeID like ('" + teamNum + "%') and floor = ? and nodeType = ?";
		try (PreparedStatement PrepStat = connection.prepareStatement(queryS)) {
			PrepStat.setString(1, Floor);
			PrepStat.setString(2, Type);
			ResultSet rSet = PrepStat.executeQuery();
			int countNum = -1;
			if (rSet.next()) {
				countNum = rSet.getInt("countNum");
			}
			rSet.close();
			return countNum;
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("countNodeTypeOnFloor() error");
		}
		return -1;
	}

	/**
	 * adds a node with said data to the database
	 *
	 * @return the amount of rows affected by executing this statement, should be 1 in this case
	 */
	public int addNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName) {
		String addNodeS = "Insert Into node Values (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement addNodePS = connection.prepareStatement(addNodeS)) {
			addNodePS.setString(1, nodeID);
			addNodePS.setInt(2, xCoord);
			addNodePS.setInt(3, yCoord);
			addNodePS.setString(4, floor);
			addNodePS.setString(5, building);
			addNodePS.setString(6, nodeType);
			addNodePS.setString(7, longName);
			addNodePS.setString(8, shortName);
			int addNodeRS = addNodePS.executeUpdate();
			if (addNodeRS == 0) {
				System.err.println("addNode Result = 0, probably bad cuz no rows was affected");
			} else if (addNodeRS != 1) {
				System.err.println("addNode Result =" + addNodeRS + ", probably bad cuz " + addNodeRS + " rows was affected");
			}
			return addNodeRS; // addNodeRS = x means the statement executed affected x rows, should be 1 in this case.
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * adds an edge with said data to the database
	 * both startNode and endNode has to already exist in node table
	 *
	 * @return the amount of rows affected by executing this statement, should be 1 in this case
	 */
	public int addEdge(String edgeID, String startNode, String endNode) {
		String addEdgeS = "Insert Into hasedge (edgeid, startnode, endnode) Values (?, ?, ?)";
		try (PreparedStatement addEdgePS = connection.prepareStatement(addEdgeS)) {
			addEdgePS.setString(1, edgeID);
			addEdgePS.setString(2, startNode);
			addEdgePS.setString(3, endNode);
			int addEdgeRS = addEdgePS.executeUpdate();
			if (addEdgeRS == 0) {
				System.err.println("addEdge Result = 0, probably bad cuz no rows was affected");
			} else if (addEdgeRS != 1) {
				System.err.println("addEdge Result =" + addEdgeRS + ", probably bad cuz " + addEdgeRS + " rows was affected");
			}
			addLength(startNode, endNode);
			return addEdgeRS; // addEdgeRS = x means the statement executed affected x rows, should be 1 in this case.
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("addEdge() error in the try/catch");
			return 0;
		}
	}

	/**
	 * modifies a node, updating the DB, returning 0 or 1 depending on whether operation was successful
	 *
	 * @param nodeID
	 * @param xCoord
	 * @param yCoord
	 * @param floor
	 * @param building
	 * @param nodeType
	 * @param longName
	 * @param shortName
	 * @return int (0 if node couldn't be added, 1 if the node was added successfully)
	 */
	public int modifyNode(String nodeID, Integer xCoord, Integer yCoord, String floor, String building, String nodeType, String longName, String shortName) {
		//String finalQuery = "update node set ";
		String xCoordUpdate = "";
		String yCoordUpdate = "";
		String floorUpdate = "";
		String buildingUpdate = "";
		String nodeTypeUpdate = "";
		String longNameUpdate = "";
		String shortNameUpdate = "";
		boolean added = false;
		String query = "update node set ";
		if (xCoord != null) {
			query = query + " xCoord = " + xCoord;
			//xCoordUpdate = "xCoord = " + xCoord;
			added = true;
		}
		if (yCoord != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " yCoord = " + yCoord;
			added = true;
		}
		if (floor != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " floor = '" + floor + "'";
			added = true;
		}
		if (building != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " building = '" + building + "'";
			added = true;
		}
		if (nodeType != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " nodeType = '" + nodeType + "'";
			added = true;
		}
		if (longName != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " longName = '" + longName + "'";
			added = true;
		}
		if (shortName != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " shortName = '" + shortName + "'";
			added = true;
		}
		query = query + " where nodeID = '" + nodeID + "'";
		try {
			Statement stmt = this.connection.createStatement();
			System.out.println(query);
			stmt.executeUpdate(query);
			stmt.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error in updating node");
			return 0;
		}
	}

	/**
	 * modifies an edge, updating the DB, returning 0 or 1 depending on whether operation was successful
	 *
	 * @param edgeID
	 * @param startNode
	 * @param endNode
	 * @return int (0 if node couldn't be added, 1 if the node was added successfully)
	 * need to check that both startNode and endNode already exist in node table
	 */
	public int modifyEdge(String edgeID, String startNode, String endNode) {
		boolean correctEdgeID = false;
		boolean correctStartNode = false;
		boolean correctEndNode = false;


		// !!!!!! user is required to type in edgeID !!!!!! //

		//1. check that they gave a valid edgeID
		//2. check that they gave a valid nodeID they want to change to the startNode
		//			or
		//3. check that they gave a valid nodeID they want to change to the endNode

		String checkEdgeID = "Select edgeid From hasedge Where edgeid = ?";
		try (PreparedStatement prepState1 = connection.prepareStatement(checkEdgeID)) {
			prepState1.setString(1, edgeID);
			ResultSet rset1 = prepState1.executeQuery();

			if (rset1.next()) {
				correctEdgeID = true;
			}

		} catch (SQLException e) {
			//e.printStackTrace();
			return 0;
		}

		if (startNode != null) {
			String checkStartNode = "Select nodeid From node Where nodeid = ?";
			try (PreparedStatement prepState2 = connection.prepareStatement(checkStartNode)) {
				prepState2.setString(1, startNode);
				ResultSet rset2 = prepState2.executeQuery();

				if (rset2.next()) {
					correctStartNode = true;
				}

			} catch (SQLException e) {
				System.err.println("given start node does not exist");
				//e.printStackTrace();
				return 0;
			}
		}

		if (endNode != null) {
			String checkEndNode = "Select nodeid From node Where nodeid = ?";
			try (PreparedStatement prepState3 = connection.prepareStatement(checkEndNode)) {
				prepState3.setString(1, endNode);
				ResultSet rset3 = prepState3.executeQuery();

				if (rset3.next()) {
					correctEndNode = true;
				}


			} catch (SQLException e) {
				//e.printStackTrace();
				return 0;
			}
		}


		if ((correctEdgeID && correctStartNode) || (correctEdgeID && correctEndNode) || (correctEdgeID && correctStartNode && correctEndNode)) {
			String[] nodes = edgeID.split("_");
			deleteEdge(nodes[0], nodes[1]);
			if (startNode == null) {
				String newEdgeID = nodes[0] + "_" + endNode;
				addEdge(newEdgeID, nodes[0], endNode);
				return 1;
			}
			if (endNode == null) {
				String newEdgeID = startNode + "_" + nodes[1];
				addEdge(newEdgeID, startNode, nodes[1]);
				return 1;
			} else {
				String newEdgeID = startNode + "_" + endNode;
				addEdge(newEdgeID, startNode, endNode);
				return 1;
			}
		}

		System.err.println("startNode or endNode does not exist but no SQL error");
		return 0;
	}

	/**
	 * Deletes edge(s) between the given two nodes, they can be in any order
	 *
	 * @return the amount of rows affected by executing this statement, should be 1 in this case, if there are two edges it returns 2
	 * if count == 1 || count == 2, edges have been deleted
	 * else no edges exist with inputted nodes
	 */
	public int deleteEdge(String nodeID1, String nodeID2) {

		String deleteEdgeS1 = "Delete From hasedge Where startnode = ? And endnode = ?";
		String deleteEdgeS2 = "Delete From hasedge Where endnode = ? And startnode = ?";

		int count = 0;
		// We might want https://stackoverflow.com/questions/10797794/multiple-queries-executed-in-java-in-single-statement
		try (PreparedStatement deleteEdgePS1 = connection.prepareStatement(deleteEdgeS1)) {
			deleteEdgePS1.setString(1, nodeID1);
			deleteEdgePS1.setString(2, nodeID2);

			int deleteEdgeRS1 = deleteEdgePS1.executeUpdate();

			if (deleteEdgeRS1 == 0) {
				System.err.println("deleteEdge Result = 0, inputted nodes in this order do not share an edge");
			} else if (deleteEdgeRS1 == 2) {
				System.out.println("deleteEdge Result =" + deleteEdgeRS1 + ", it's weird cuz " + deleteEdgeRS1 + " rows was affected");
				count = 1;
			} else if (deleteEdgeRS1 != 1) {
				System.err.println("deleteEdge Result =" + deleteEdgeRS1 + ", just bad because this should never occur");
			}
			System.out.println("Number of rows affected: " + deleteEdgeRS1);

			// deleteEdgeRS1 = x means the statement executed affected x rows, should be 1 in this case, if there are two edges it returns 2.
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("deleteEdge() tyr/catch error");
			count = 0;
		}

		try (PreparedStatement deleteEdgePS2 = connection.prepareStatement(deleteEdgeS2)) {
			deleteEdgePS2.setString(1, nodeID1);
			deleteEdgePS2.setString(2, nodeID2);

			int deleteEdgeRS2 = deleteEdgePS2.executeUpdate();

			if (deleteEdgeRS2 == 0) {
				System.err.println("deleteEdge Result = 0, inputted nodes in this order do not share an edge");
			} else if (deleteEdgeRS2 == 2) {
				System.out.println("deleteEdge Result =" + deleteEdgeRS2 + ", it's weird cuz " + deleteEdgeRS2 + " rows was affected");
				count += count;
			} else if (deleteEdgeRS2 != 1) {
				System.err.println("deleteEdge Result =" + deleteEdgeRS2 + ", just bad because this should never occur");
			}
			System.out.println("Number of rows affected: " + deleteEdgeRS2);

			// deleteEdgeRS2 = x means the statement executed affected x rows, should be 1 in this case, if there are two edges it returns 2.
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("deleteEdge() tyr/catch error");
			if (count == 0) {
				count = 0;
			} else
				count = 1;
		}
		return count;
	}

	/**
	 * matches the nodeID to a node and deletes it from DB
	 *
	 * @return the amount of rows affected by executing this statement, should be 1 in this case
	 */
	public int deleteNode(String nodeID) {
		String deleteNodeS = "Delete From node Where nodeid = ?";
		try (PreparedStatement deleteNodePS = connection.prepareStatement(deleteNodeS)) {
			deleteNodePS.setString(1, nodeID);
			// We might encounter issues if on delete cascade didn't work
			int deleteNodeRS = deleteNodePS.executeUpdate();
			if (deleteNodeRS == 0) {
				System.err.println("deleteNode Result = 0, probably bad cuz no rows was affected");
			} else if (deleteNodeRS != 1) {
				System.err.println("deleteNode Result =" + deleteNodeRS + ", probably bad cuz " + deleteNodeRS + " rows was affected");
			}
			return deleteNodeRS;
			// deleteNode = x means the statement executed affected x rows, should be 1 in this case.
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * gets list of nodeIDS
	 *
	 * @return String[] of nodeIDs
	 */
	public ArrayList<String> getListofNodeIDS() {
		ArrayList<String> listOfNodeIDs = new ArrayList<>();

		String deleteNodeS = "Select nodeid From node";
		try (PreparedStatement deleteNodePS = connection.prepareStatement(deleteNodeS)) {

			ResultSet rset = deleteNodePS.executeQuery();

			while (rset.next()) {
				String nodeID = rset.getString("nodeID");
				listOfNodeIDs.add(nodeID);
			}
			rset.close();
			deleteNodePS.close();

			return listOfNodeIDs;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("getListofNodeIDS error try/catch");
			return listOfNodeIDs;
		}

	}

	public void getNewCSVFile(String tableName) {

		String sqlQuery = "select * from " + tableName;
		try (PreparedStatement prepState = connection.prepareStatement(sqlQuery)) {

			ResultSet rset1 = prepState.executeQuery();

			if (tableName == "node") {

				StringBuilder nodeSB = new StringBuilder();
				while (rset1.next()) {

					//File nodeCSV = new File("src/main/resources/edu/wpi/TeamE/output/outputNode.csv");
					nodeSB.append(rset1.getString("nodeID")).append(",");
					nodeSB.append(rset1.getInt("xCoord")).append(",");
					nodeSB.append(rset1.getInt("yCoord")).append(",");
					nodeSB.append(rset1.getString("floor")).append(",");
					nodeSB.append(rset1.getString("building")).append(",");
					nodeSB.append(rset1.getString("nodeType")).append(",");
					nodeSB.append(rset1.getString("longName")).append(",");
					nodeSB.append(rset1.getString("shortName")).append("\n");

				}

				// create a file writer to write to files
				FileWriter nodeWriter = new FileWriter("src/main/resources/edu/wpi/TeamE/output/outputNode.csv");
				nodeWriter.write("nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName\n");
				nodeWriter.write(String.valueOf(nodeSB));
				nodeWriter.close();
			}

			rset1.close();

			ResultSet rset2 = prepState.executeQuery();

			if (tableName == "hasEdge") {
				StringBuilder edgeSB = new StringBuilder();

				while (rset2.next()) {
					//File edgeCSV = new File("src/main/resources/edu/wpi/TeamE/output/outputEdge.csv");
					edgeSB.append(rset2.getString("edgeID")).append(",");
					edgeSB.append(rset2.getString("startNode")).append(",");
					edgeSB.append(rset2.getString("endNode")).append("\n");
				}

				FileWriter edgeWriter = new FileWriter("src/main/resources/edu/wpi/TeamE/output/outputEdge.csv");
				edgeWriter.write("edgeID, startNode, endNode\n");
				edgeWriter.write(String.valueOf(edgeSB));
				edgeWriter.close();
			}

		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
