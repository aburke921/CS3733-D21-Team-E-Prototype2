package edu.wpi.TeamE.databases;

import edu.wpi.TeamE.algorithms.Edge;
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.views.MapEditor;
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
	private MapEditor mapEditor = new MapEditor();

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
	 * Tables Created: node, hasEdge, userAccount, requests, floralRequests, sanitationRequest, extTransport, medDelivery, securityServ
	 * Views Created (which are like tables): visitorAccount, patientAccount, doctorAccount, adminAccount
	 */
	public void createTables() {
		createNodeTable();
		createEdgeTable();
		createUserAccountTable();
		createRequestsTable();
		createFloralRequestsTable();
		createExtTransportTable();
		createSanitationTable();
		createMedDeliveryTable();
		createSecurityServTable();
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
					"Create Table node( "
							+ "    nodeID    varchar(31) Primary Key,"
							+ "    xCoord    int Not Null,"
							+ "    yCoord    int Not Null,"
							+ "    floor     varchar(5) Not Null,"
							+ "    building  varchar(20),"
							+ "    nodeType  varchar(10),"
							+ "    longName  varchar(100),"
							+ "    shortName varchar(100),"
							+ "    Unique (xCoord, yCoord, floor),"
							+ "    Constraint floorLimit Check (floor In ('1', '2', '3', 'L1', 'L2')), "
							+ "    Constraint buildingLimit Check (building In ('BTM', '45 Francis', 'Tower', '15 Francis', 'Shapiro', 'Parking')), "
							+ "    Constraint nodeTypeLimit Check (nodeType In ('PARK', 'EXIT', 'WALK', 'HALL', 'CONF', 'DEPT', 'ELEV', 'INFO', 'LABS', 'REST', 'RETL', 'STAI', 'SERV', 'ELEV', 'BATH'))"
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
	public void createUserAccountTable() {
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

	/**
	 * Uses executes the SQL statements required to create views for different types of users. The views created
	 * are: visitorAccount, patientAccount, doctorAccount, adminAccount.
	 */
	public void createUserAccountTypeViews() {
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View visitorAccount As " +
					"Select * " +
					"From useraccount " +
					"Where usertype = 'visitor'";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating visitorAccount view");
		}
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View patientAccount As " +
					"Select * " +
					"From useraccount " +
					"Where usertype = 'patient'";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating patientAccount view");
		}
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View doctorAccount As " +
					"Select * " +
					"From useraccount " +
					"Where usertype = 'doctor'";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating doctorAccount view");
		}

		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View adminAccount As " +
					"Select * " +
					"From useraccount " +
					"Where usertype = 'admin'";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating adminAccount view");
		}

	}


	//createRequestsTable --> added assignee



	/**
	 * Uses executes the SQL statements required to create the requests table.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - creatorID: this is the username of the user who created the request.
	 * - creationTime: this is a time stamp that is added to the request at the moment it is made.
	 * - requestType: this is the type of request that the user is making. The valid options are: "floral", "medDelivery", "sanitation", "security", "extTransport".
	 * - requestStatus: this is the state in which the request is being processed. The valid options are: "complete", "canceled", "inProgress".
	 * - assignee: this is the person who is assigned to the request
	 */
	public void createRequestsTable() {
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create Table requests(" +
					"requestID    int Primary Key,\n" +
					"creatorID    int References useraccount On Delete Cascade," +
					"creationTime timestamp,\n" +
					"requestType  varchar(31),\n" +
					"requestStatus varchar(10),\n" +
					"assignee varchar(31)," +
					"Constraint requestTypeLimit Check (requestType In ('floral', 'medDelivery', 'sanitation', 'security', 'extTransport')), " +
					"Constraint requestStatusLimit Check (requestStatus In ('complete', 'canceled', 'inProgress')))";
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
	 * - flowerType: this is the type of flowers that the user wants to request. The valid options are: 'Roses', 'Tulips', 'Carnations', 'Assortment'
	 * - flowerAmount: this the number/quantity of flowers that the user is requesting. The valid options are: 1, 6, 12
	 * - vaseType: this is the type of vase the user wants the flowers to be delivered in. The valid options are: 'Round', 'Square', 'Tall', 'None'
	 * - message: this is a specific detailed message that the user can have delivered with the flowers or an instruction message
	 * *                for whoever is fufilling the request
	 */
	public void createFloralRequestsTable() {
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
	public void createSanitationTable() {
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create Table sanitationRequest(\n" +
					"    requestID int Primary Key References requests On Delete Cascade,\n" +
					"    roomID varchar(31) Not Null References node On Delete Cascade,\n" +
					"    signature varchar(31) Not Null,\n" +
					"    description varchar(5000),\n" +
					"    sanitationType varchar(31),\n" +
					"    urgency varchar(31) Not Null,\n" +
					"    Constraint sanitationTypeLimit Check (sanitationType In ('Urine Cleanup', 'Feces Cleanup', 'Preparation Cleanup', 'Trash Removal'))," +
					"    Constraint urgencyTypeLimit Check (urgency In ('Low', 'Medium', 'High', 'Critical'))\n" +
					")";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating sanitationRequest table");
		}
	}

	/**
	 * Uses executes the SQL statements required to create a extTransport table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - hospitalLocation: this is the location of the hospital that the user/first responders are going to.
	 * - requestType: this the mode of transportation that the request is being made for. The valid options are: 'Ambulance', 'Helicopter', 'Plane'
	 * - severity: this is how sever the patient is who the user/first responders are transporting.
	 * - patientID: this is the ID of the patient that is being transported.
	 * - ETA: this is the estimated time the patient will arrive.
	 * - description: this is a detailed description of request that generally includes what happened to the patient and their current situation.
	 */
	public void createExtTransportTable() {
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create Table extTransport(\n" +
					"    requestID int Primary Key References requests On Delete Cascade,\n" +
					"    hospitalLocation varchar(100) Not Null,\n" +
					"    requestType varchar(100) Not Null, " +
					"    severity varchar(30) Not Null,\n" +
					"    patientID int Not Null,\n" +
					"    ETA varchar(100),\n" +
					"    description varchar(5000)," +
					"    Constraint requestTypeLimitExtTrans Check (requestType In ('Ambulance', 'Helicopter', 'Plane'))" +
					")";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			 e.printStackTrace();
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
	public void createMedDeliveryTable() {
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create Table medDelivery (\n" +
					"requestID  int Primary Key References requests On Delete Cascade," +
					"roomID     varchar(31) Not Null References node On Delete Cascade," +
					"medicineName        varchar(31) Not Null," +
					"quantity            int         Not Null," +
					"dosage              varchar(31) Not Null," +
					"specialInstructions varchar(5000)," +
					"signature           varchar(31) Not Null" + ")";

			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
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
	public void createSecurityServTable() {
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create Table securityServ (\n" +
					"requestID  int Primary Key References requests On Delete Cascade," +
					"roomID     varchar(31) Not Null References node On Delete Cascade," +
					"level     varchar(31)," +
					"urgency   varchar(31) Not Null," +
					"Constraint urgencyTypeLimitServ Check (urgency In ('Low', 'Medium', 'High', 'Critical'))\n" +
					")";

			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("error creating securityServ table");
		}
	}






	/**
	 * Deletes node,hasEdge, userAccount, requests, floralRequests, sanitationRequest and extTransport tables.
	 * Also deletes adminAccount, doctorAccount, patientAccount, visitorAccount views
	 * try/catch phrase set up in case the tables all ready do not exist
	 */
	public void deleteAllTables() {

		try {
			Statement stmt = this.connection.createStatement();
			stmt.execute("Drop Table securityServ");
			stmt.execute("Drop Table medDelivery");
			stmt.execute("Drop Table extTransport");
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
	 * Reads csv & inserts into table
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

	/**
	 * Acts as a trigger and calculates the length between two nodes that form an edge and add the value to the edge table
	 * @param startNode the node ID for the starting node in the edge
	 * @param endNode   the node ID for the ending node in the edge
	 */
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
	 * Adds a node with said data to the database
	 * @param nodeID this is a unique identifier for the each node. Every node must contain a nodeID.
	 * @param xCoord this is the X-Coordinate/pixel location of the node on the map of the hospital.
	 * @param yCoord this is the Y-Coordinate/pixel location of the node on the map of the hospital.
	 * @param floor this is the floor of the hospital that the node is located on. The available options are: "1", "2", "3", "L1", "L2"
	 * @param building this is the building of the hospital that the node is located in. The available options are: "BTM", "45 Francis", "Tower",
	 *                   "15 Francis", "Shapiro", "Parking".
	 * @param nodeType this is the type room/location that the node is specifying. The available options are: "PARK" (parking), "EXIT" (exit),
	 *                    "WALK" (sidewalk/out door walkway), "HALL' (indoor walkway), "CONF" (conference room), "DEPT" (department room), "ELEV" (elevator),
	 *                    "INFO" (information), "LABS" (lab testing/results room), "REST" (rest areas/sitting areas), "RETL" (retail/food and shopping),
	 *                    "STAI" (stairs), "SERV" (services), "BATH" (bathrooms).
	 * @param longName this is the long version/more descriptive name of the node/location/room
	 * @param shortName this is the short/nickname of the node/location/room
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
				//mapEditor.errorPopup("Error in updating node");
				System.err.println("addNode Result = 0, probably bad cuz no rows was affected");
			} else if (addNodeRS != 1) {
				//mapEditor.errorPopup("Error in updating node");
				System.err.println("addNode Result =" + addNodeRS + ", probably bad cuz " + addNodeRS + " rows was affected");
			}
			return addNodeRS; // addNodeRS = x means the statement executed affected x rows, should be 1 in this case.
		} catch (SQLException e) {
			//e.printStackTrace();
			//mapEditor.errorPopup("There is already a node here or this node already exists");
			return 0;
		}
	}

	/**
	 * Adds an edge with said data to the database. Both startNode and endNode has to already exist in node table
	 * @param edgeID this is a unique identifier for the edge connection. Every edge must contain an edgeID.
	 * @param startNode this is a nodeID in which the edge connection starts.
	 * @param endNode this is a nodeID in which the edge connection ends.
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
			//e.printStackTrace();
			System.err.println("addEdge() error in the try/catch");
			return 0;
		}
	}

	/**
	 * This is allows a visitor to create a user account giving them more access to the certain requests available
	 * @param email this is the user's email that is connected to the account the are trying to create
	 * @param password this is a password that the user will use to log into the account
	 * @param firstName this is the user's first name that is associated with the account
	 * @param lastName this is the user's last name that is associated with the account
	 */
	public void addUserAccount(String email, String password, String firstName, String lastName) {
		String insertUser = "Insert Into useraccount Values ((Select Count(*) From useraccount) + 1, ?, ?, 'visitor', ?, ?)";
		try (PreparedStatement prepState = connection.prepareStatement(insertUser)) {
			prepState.setString(1, email);
			prepState.setString(2, password);
			prepState.setString(3, firstName);
			prepState.setString(4, lastName);
			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			//mapEditor.errorPopup("This email all ready has an account");
			System.err.println("Error inserting into userAccount inside function insertUserAccount()");
		}
	}

	/**
	 * This is allows an administrator or someone with access to the database to be able to create a user account
	 * with more permissions giving them more access to the certain requests available. This is function should not
	 * be used while the app is being run.
	 * @param email this is the user's email that is connected to the account the are trying to create
	 * @param password this is a password that the user will use to log into the account
	 * @param userType this is the type of account that the individual is being assigned to
	 * @param firstName this is the user's first name that is associated with the account
	 * @param lastName this is the user's last name that is associated with the account
	 */
	public void addSpecialUserType(String email, String password, String userType, String firstName, String lastName) {
		String insertUser = "Insert Into useraccount Values ((Select Count(*) From useraccount) + 1, ?, ?, ?, ?, ?)";
		try (PreparedStatement prepState = connection.prepareStatement(insertUser)) {
			prepState.setString(1, email);
			prepState.setString(2, password);
			prepState.setString(3, userType);
			prepState.setString(4, firstName);
			prepState.setString(5, lastName);
			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			//showError("This email all ready has an account");
			System.err.println("Error inserting into userAccount inside function insertUserAccount()");
		}
	}

	/**
	 * This adds a sanitation services form to the table specific for it
	 *
	 * @param //form this is the form being added to the table
	 */
	public void addSanitationRequest(int userID, String asignee, String roomID, String sanitationType, String description, String urgency, String signature) {
		String insertRequest = "Insert Into requests\n" +
				"Values ((Select Count(*)\n" +
				"         From requests) + 1, ?, Current Timestamp, 'sanitation', 'inProgress', ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);
			prepState.setString(2, asignee);
			prepState.execute();
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error inserting into requests inside function addSanitationRequest()");
		}

		String insertSanitationRequest = "Insert Into sanitationrequest\n" +
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
	 * This function needs to add a external patient form to the table for external patient forms
	 *
	 * @param //form this is the form that we will create and send to the database
	 */
	public void addExternalPatientRequest(int userID, String asignee, String hospitalLocation, String requestType, String severity, String patientID, String ETA, String description) {

		String insertRequest = "Insert Into requests\n" +
				"Values ((Select Count(*)\n" +
				"         From requests) + 1, ?, Current Timestamp, 'extTransport', 'inProgress', ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);
			prepState.setString(2, asignee);

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into requests inside function addExternalPatientRequest()");
		}

		String insertExtTransport = "Insert Into exttransport\n" +
				"Values ((Select Count(*)\n" +
				"         From requests), ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertExtTransport)) {
			prepState.setString(1, hospitalLocation);
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
	 *
	 * @param userID        this is the username that the user uses to log into the account
	 * @param RoomNodeID    this is the nodeID/room the user is sending the request to
	 * @param recipientName this is the name of the individual they want the flowers to be addressed to
	 * @param flowerType    this is the type of flowers that the user wants to request
	 * @param flowerAmount  this the number/quantity of flowers that the user is requesting
	 * @param vaseType      this is the type of vase the user wants the flowers to be delivered in
	 * @param message       this is a specific detailed message that the user can have delivered with the flowers or an instruction message
	 *                      for whoever is fufilling the request
	 */
	public void addFloralRequest(int userID, String asignee, String RoomNodeID, String recipientName, String flowerType, int flowerAmount, String vaseType, String message) {
		String insertRequest = "Insert Into requests\n" +
				"Values ((Select Count(*)\n" +
				"         From requests) + 1, ?, Current Timestamp, 'floral', 'inProgress', ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);
			prepState.setString(2, asignee);

			prepState.execute();
		} catch (SQLException e) {
//			e.printStackTrace();
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

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
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
	 * This adds a medicine request form to the table for medicine request forms
	 *
	 * @param //form this is the form being added
	 */
	public void addMedicineRequest(int userID, String asignee, String roomID, String medicineName, int quantity, String dosage, String specialInstructions, String signature) {

		String insertRequest = "Insert Into requests\n" +
				"Values ((Select Count(*)\n" +
				"         From requests) + 1, ?, Current Timestamp, 'medDelivery', 'inProgress', ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);
			prepState.setString(2, asignee);

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into requests inside function addMedicineRequest()");
		}


		String insertMedRequest = "Insert Into medDelivery\n" +
				"Values ((Select Count(*)\n" +
				"         From requests), ?, ?, ?, ?, ?, ?)";

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
	 *
	 * @param //form this is the form added to the table
	 */
	public void addSecurityRequest(int userID, String asignee, String roomID, String level, String urgency) {

		String insertRequest = "Insert Into requests\n" +
				"Values ((Select Count(*)\n" +
				"         From requests) + 1, ?, Current Timestamp, 'security', 'inProgress', ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);
			prepState.setString(2, asignee);
			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into requests inside function addSecurityRequest()");
		}

		String insertSecurityRequest = "Insert Into securityServ\n" +
				"Values ((Select Count(*)\n" +
				"         From requests), ?, ?, ?)";

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

	public void addDataForPresentation(){
		//Visitors:
		// - have access to floral Delivery
		addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		addUserAccount("terry_reilly123@yahoo.com", "visitor2", "Terry", "Reilly");
		addUserAccount("smiddle@outlook.com", "visitor3", "Sharon", "Middleton");
		addUserAccount("catherinehop12@gmail.com", "visitor4", "Catherine", "Hopkins");
		addUserAccount("mbernard@wpi.edu", "visitor5", "Michelle", "Bernard");
		addUserAccount("mccoy.meghan@hotmail.com", "visitor6", "Meghan", "Mccoy");
		addUserAccount("harry89owens@gmail.com", "visitor7", "Harry", "Owens");
		addUserAccount("hugowh@gmail.com", "visitor8", "Hugo", "Whitehouse");
		addUserAccount("spenrodg@yahoo.com", "visitor9", "Spencer", "Rodgers");
		addUserAccount("thomasemail@gmail.com", "visitor10", "Thomas", "Mendez");
		addUserAccount("claytonmurray@gmail.com", "visitor11", "Clayton", "Murray");
		addUserAccount("lawrencekhalid@yahoo.com", "visitor12", "Khalid", "Lawrence");

		//Patients:
		//13 - 19
		addSpecialUserType("adamj@gmail.com","patient1","patient","Adam", "Jenkins");
		addSpecialUserType("abbym@yahoo.com","patient2","patient","Abby", "Mohamed");
		addSpecialUserType("wesleya@gmail.com","patient3","patient","Wesley", "Armstrong");
		addSpecialUserType("travisc@yahoo.com","patient4","patient","Travis", "Cook");
		addSpecialUserType("gabriellar@gmail.com","patient5","patient","Gabriella", "Reyes");
		addSpecialUserType("troyo@yahoo.com","patient6","patient","Troy", "Olson");
		addSpecialUserType("anat@gmail.com","patient7","patient","Ana", "Turner");

		//Doctors:
		//20-27
		addSpecialUserType("billb@gmail.com","doctor1","doctor","Bill", "Byrd");
		addSpecialUserType("ameliak@yahoo.com","doctor2","doctor","Amelia", "Knight");
		addSpecialUserType("simond@gmail.com","doctor3","doctor","Simon", "Daniel");
		addSpecialUserType("victoriae@yahoo.com","doctor4","doctor","Victoria", "Erickson");
		addSpecialUserType("taylorr@gmail.com","doctor5","doctor","Taylor", "Ramos");
		addSpecialUserType("rosas@yahoo.com","doctor6","doctor","Rosa", "Smith");
		addSpecialUserType("declanp@gmail.com","doctor7","doctor","Declan", "Patel");
		addSpecialUserType("laurenb@yahoo.com","doctor8","doctor","Lauren", "Bolton");

		//Admin:
		//28 - 30
		addSpecialUserType("abbyw@gmail.com","admin1","admin","Abby", "Williams");
		addSpecialUserType("andrewg@yahoo.com","admin2","admin","Andrew", "Guerrero");
		addSpecialUserType("aleshah@gmail.com","admin3","admin","Alesha", "Harris");


		//Floral Requests:
		addFloralRequest(13,"Amy Castaneda", "ADEPT00101", "Adam", "Roses", 1, "None", "Hi Adam, I am so sorry to hear about your accident. Please get better soon!");
		addFloralRequest(13,"Elsa Figueroa", "ADEPT00102", "Abraham", "Tulips", 6, "Round", "Dear Abraham, hope these flowers help you feel better. The team really misses you and hope you will be ready to go by the championship");
		addFloralRequest(14,"Caroline Sutton", "ADEPT00102", "Xavier", "Carnations", 12, "Square", "Get well soon");
		addFloralRequest(15,"Miles Long", "ADEPT00301", "Nikki", "Assortment", 1, "None", "");
		addFloralRequest(15,"Hasan Perry", "ADEPT00101", "Monica", "Roses", 6, "Tall", "Love and miss you!!");
		addFloralRequest(17,"Caroline Richardson", "DDEPT00102", "Amy", "Tulips", 12, "Square", "Enjoy the flowers");
		addFloralRequest(17,"Miles Carroll", "ADEPT00102", "Alfred", "Carnations", 1, "Tall", "Miss you!");
		addFloralRequest(19,"Seth Warner", "ADEPT00101", "Caroline", "Assortment", 6, "Round", "Sorry I forgot to warn you about the slippery stairs, I hope these flowers can make you feel better!");
		addFloralRequest(19,"Darren Rossi", "ADEPT00301", "Carrie", "Assortment", 12, "Round", "");

		//Sanitation Requests:
		addSanitationRequest(20,"Crystal Harvey", "AREST00101", "Urine Cleanup", "Restroom with urine on the floor", "Medium", "Bill Byrd");
		addSanitationRequest(20,"Minnie Newman", "AREST00103", "Urine Cleanup", "Restroom with urine on the toilet seet", "Medium", "Bill Byrd");
		addSanitationRequest(24,"Ayla Black", "AREST00103", "Feces Cleanup", "Feces smeared on toilet seats", "High", "Taylor Ramos");
		addSanitationRequest(25,"Lenard Jacobs", "ARETL00101", "Trash Removal", "Trash can full, starting to smell", "Medium", "Rosa Smith");
		addSanitationRequest(28,"Juan Williams", "IREST00103", "Feces Cleanup", "Just outside of the bathroom there is a pile of feces. Someone did not make it in time.", "Critical", "Abby Williams");
		addSanitationRequest(30,"May Jimenez", "IREST00203", "Trash Removal", "Trash can smells bad", "Medium", "Alesha Harris");
		addSanitationRequest(29,"Herman Bull", "IREST00303", "Trash Removal", "Trash can full. Another one is available so don't rush.", "Low", "Andrew Guerrero");
		addSanitationRequest(22,"Umar Rojas", "HRETL00102", "Urine Cleanup", "Liquid on the floor. Unclear if it is urine. Not a whole lot of it.", "Low", "Simon Daniel");
		addSanitationRequest(23,"Reuben", "IREST00403", "Trash Removal", "", "Low", "Victoria Erickson");

		//Medicine Delivery Request
		addMedicineRequest(20, "Clara Bryan", "BLABS00102", "Atorvastatin", 30, "20mg", "Once a day by mouth", "Bill Byrd");
		addMedicineRequest(20, "Jennifer Cunningham", "BLABS00202", "Lisinopril", 90, "20mg", "Once a day by mouth", "Bill Byrd");
		addMedicineRequest(21, "Jak Bishop", "IDEPT00103", "Levothyroxine", 90, "12.5mcg", "Once a day my bouth", "Amelia Knight");
		addMedicineRequest(24, "Ben Coles", "BLABS00102", "Metformin", 30, "850mg", "Twice a day by mouth", "Taylor Ramos");
		addMedicineRequest(27, "Gloria Webster", "IDEPT00803", "Amlodipine", 30, "5mg", "Once a day by mouth", "Lauren Bolton");
		addMedicineRequest(26, "Robbie Turner", "IDEPT00603", "Metoprolol", 90, "400mg", "Once a day by mouth", "Declan Patel");
		addMedicineRequest(23, "Lucas Whittaker", "IDEPT00403", "Omeprazole", 90, "40mg", "Three times a day by mouth before a meal", "Victoria Erickson");
		addMedicineRequest(24, "Alec Rees", "IDEPT00703", "Simvastatin", 30, "10mg", "Once a day by mouth", "Taylor Ramos");
		addMedicineRequest(27, "Francesca Ferguson", "IDEPT00903", "Losartan", 90, "100mg", "Once daily by mouth", "Lauren Bolton");
		addMedicineRequest(21, "Josie Pittman", "IDEPT00203", "Albuterol", 30, "0.63mg", "3 times a day via nebulizer. 4 times a day if needed.", "Amelia Knight");
		addMedicineRequest(20, "Will Ford", "BLABS00202", "Metformin", 30, "8.5mL", "Once daily with meals.", "Bill Byrd");
		addMedicineRequest(23, "Billy Gomez", "BLABS00102", "Metformin", 30, "5mL", "Twice a day with meals.", "Victoria Erickson");

		//Security Requests:
		addSecurityRequest(20, "James O'Moore","HDEPT00203", "Low", "Low");
		addSecurityRequest(22, "Russell Armstrong","WELEV00E01", "Medium", "Medium");
		addSecurityRequest(30, "Lillian Peters","HDEPT00203", "Low", "Low");
		addSecurityRequest(27, "Clara Dixon","ePARK00101", "Medium", "High");
		addSecurityRequest(24, "Herbert Ortega","BDEPT00402", "Medium", "Medium");
		addSecurityRequest(20, "Caleb Carr","BDEPT00302", "Low", "Low");
		addSecurityRequest(25, "Jasper Miller","CCONF002L1", "High", "Critical");
		addSecurityRequest(29, "Jennifer Brewer","eWALK00701", "Medium", "Medium");

		addExternalPatientRequest(27,"Ciaran Goodwin","Brigham & Women's Hospital - Boston MA", "Ambulance", "High Severity", "0093438901", "5 minutes", "Patient dropped down into a state of unconsciousness randomly at the store. Patient is still unconscious and unresponsive but has a pulse. No friends or family around during the incident. ");
		addExternalPatientRequest(30,"Lola Bond","Brigham & Women's Hospital - Boston MA", "Ambulance","Low Severity", "0093380235", "20 minutes", "Patient coming in with cut on right hand. Needs stitches. Bleeding is stable.");
		addExternalPatientRequest(22,"Samantha Russell","Brigham & Women's Hospital - Boston MA", "Helicopter","High Severity", "9201769382", "10 minutes", "Car crash on the highway. 7 year old child in the backseat with no seatbelt on in critical condition. Blood pressure is low and has major trauma to the head.");
		addExternalPatientRequest(20,"Caleb Chapman","Brigham & Women's Hospital - Boston MA", "Helicopter","High Severity", "9375478921", "20 minutes", "Skier hit tree and lost consciousness. Has been unconscious for 30 minutes. Still has a pulse.");
		addExternalPatientRequest(24,"Dale Coates","Brigham & Women's Hospital - Boston MA", "Ambulance","Medium Severity", "0175926583", "10 minutes", "Smoke inhalation due to a fire. No burns but difficult time breathing.");
		addExternalPatientRequest(28,"Jerry Myers","Brigham & Women's Hospital - Boston MA", "Helicopter", "High Severity", "0488893687", "15 minutes", "Major car crash on highway. Middle aged woman ejected from the passenger's seat. Awake and unresponsive and in critical condition");
		addExternalPatientRequest(24,"Betty Warren","Brigham & Women's Hospital - Boston MA", "Ambulance","Medium Severity", "3333786190", "7 minutes", "Patient passed out for 30 seconds. Is responsive and aware of their surroundings. Has no history of passing out.");
		addExternalPatientRequest(27,"Maxim Rawlings","Brigham & Women's Hospital - Boston MA", "Ambulance","Low Severity", "0000382947", "10 minutes", "Relocating a patient with lung cancer from Mt.Auburn Hospital.");
		addExternalPatientRequest(24,"Alan Singh","Brigham & Women's Hospital - Boston MA", "Plane","High Severity", "3873998366", "12 hours", "Heart transplant organ in route");



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
	public int modifyNode(String nodeID,  Integer xCoord, Integer yCoord, String floor, String building, String nodeType, String longName, String shortName) {
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
	 * This edits a Sanitation Services form that is already in the database
	 * @param requestID the ID that specifies which sanitation form that is being edited
	 * @param description the new description that the user is using to update their form
	 * @param roomID the new node/room/location the user is assigning this request to
	 * @param sanitationType the new type of sanitation that the user is changing their request to
	 * @param urgency the new urgency that the user is changing in their request
	 * @param assignee this is the userID of the a new employee or administrator that will be fulfilling the request.
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public int editSanitationRequest(int requestID, String description, String roomID, String sanitationType, String urgency, String assignee) {

		boolean added = false;
		String query = "update sanitationRequest set ";

		if (description!= null) {
			query = query + " description = '" + description + "'";

			added = true;
		}
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
		if (urgency != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " urgency = '" + urgency + "'";
			added = true;
		}
		if (assignee != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " assignee = '" + assignee + "'";
			added = true;
		}

		query = query + " where requestID = '" + requestID + "'";
		try {
			Statement stmt = this.connection.createStatement();
			System.out.println(query);
			stmt.executeUpdate(query);
			stmt.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error in updating sanitation request");
			return 0;
		}



	}

	/**
	 * This edits a External Transport Services form that is already in the database
	 * @param requestID the ID that specifies which external transfer form that is being edited
	 * @param hospitalName this is the string used to update the hospital field
	 * @param requestType this is the string used to update the type
	 * @param severity this is the string used to update the severity
	 * @param patientID this is the string used to update patientID
	 * @param description this is the string used to update the description
	 * @param ETA this is the string used to update the eta
	 * @param assignee this is the userID of the a new employee or administrator that will be fulfilling the request.
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public int editExternalPatientRequest(int requestID, String hospitalName, String requestType, String severity, String patientID, String description, String ETA, String assignee) {

		boolean added = false;
		String query = "update sanitationRequest set ";

		if (hospitalName!= null) {
			query = query + " hospitalName = '" + hospitalName + "'";

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
		if (ETA != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " assignee = '" + assignee + "'";
			added = true;
		}

		query = query + " where requestID = '" + requestID + "'";
		try {
			Statement stmt = this.connection.createStatement();
			System.out.println(query);
			stmt.executeUpdate(query);
			stmt.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error in updating external transport request");
			return 0;
		}
	}

	/**
	 * This edits a floral request form that is already in the database
	 * @param requestID the ID that specifies which external transfer form that is being edited
	 * @param roomID the new node/room/location the user is assigning this request to
	 * @param flowerType the type of flower the user wants to change their request to
	 * @param flowerAmount the new quantity of flowers the user wants to change their request to
	 * @param vaseType the new vase type the user wants to change their request to
	 * @param message the new message containing either instructions or to the recipient the user wants to change
	 * @param assignee this is the userID of the a new employee or administrator that will be fulfilling the request.
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public int editFloralRequest(int requestID, String roomID, String recipientName, String flowerType, Integer flowerAmount, String vaseType, String message, String assignee) {

		boolean added = false;
		String query = "update sanitationRequest set ";

		if (recipientName!= null) {
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
		if (assignee != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " assignee = '" + assignee + "'";
			added = true;
		}

		query = query + " where requestID = '" + requestID + "'";

		try {
			Statement stmt = this.connection.createStatement();
			System.out.println(query);
			stmt.executeUpdate(query);
			stmt.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error in updating floral request");
			return 0;
		}

	}

	/**
	 * This function edits a current request for medicine delivery with the information below for a request already in the database
	 * @param requestID the ID that specifies which external transfer form that is being edited
	 * @param roomID the new node/room/location the user is assigning this request to
	 * @param medicineName this is the name of the medicine the user is changing the request to
	 * @param quantity this is the number of pills the user is changing the request to
	 * @param dosage this is the dosage (ml or mg) the user is changing the request to
	 * @param specialInstructions this is the new special instructions the user is requesting
	 * @param assignee this is the userID of the a new employee or administrator that will be fulfilling the request.
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public int editMedicineRequest(int requestID, String roomID, String medicineName, Integer quantity, String dosage, String specialInstructions, String assignee) {

		boolean added = false;
		String query = "update sanitationRequest set ";

		if (roomID!= null) {
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
		if (assignee != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " assignee = '" + assignee + "'";
			added = true;
		}

		query = query + " where requestID = '" + requestID + "'";

		try {
			Statement stmt = this.connection.createStatement();
			System.out.println(query);
			stmt.executeUpdate(query);
			stmt.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error in updating floral request");
			return 0;
		}
	}

	/**
	 * This edits a security form already within the database
	 * @param requestID the ID that specifies which external transfer form that is being edited
	 * @param roomID the new node/room/location the user is assigning this request to
	 * @param level this is the info to update levelOfSecurity
	 * @param urgency  this is the info to update levelOfUrgency
	 * @param assignee this is the userID of the a new employee or administrator that will be fulfilling the request.
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public int editSecurityRequest(int requestID, String roomID, String level, String urgency, String assignee) {
		boolean added = false;
		String query = "update sanitationRequest set ";

		if (roomID!= null) {
			query = query + " roomID = '" + roomID + "'";

			added = true;
		}
		if (level != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " level = '" + level + "'";
			added = true;
		}
		if (urgency != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " urgency = " + urgency;
			added = true;
		}
		if (assignee != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + " assignee = '" + assignee + "'";
			added = true;
		}

		query = query + " where requestID = '" + requestID + "'";

		try {
			Statement stmt = this.connection.createStatement();
			System.out.println(query);
			stmt.executeUpdate(query);
			stmt.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error in updating floral request");
			return 0;
		}
	}

	/**
	 * change a request's status to complete, canceled or inProgress
	 *
	 * @param requestID is the generated ID of the request
	 * @param status    is the status that you want to change it to
	 * @return a 1 if one line changed successfully, and 0 or other numbers for failure
	 */
	public int changeRequestStatus(int requestID, String status) {
		String changeRequestStatusS = "Update requests Set requeststatus = ? Where requestid = ?";
		try (PreparedStatement changeRequestStatusPS = connection.prepareStatement(changeRequestStatusS)) {
			changeRequestStatusPS.setString(1, status);
			changeRequestStatusPS.setInt(2, requestID);

			int rowsChanged = changeRequestStatusPS.executeUpdate();

			changeRequestStatusPS.close();

			return rowsChanged;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Unable to change requests status");
		}
		return 0;
	}








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
	 * todo
	 * @return
	 */
	public ObservableList<String> getAllNodeLongNames() {
		ObservableList<String> listOfNodeLongNames =  FXCollections.observableArrayList();

		String deleteNodeS = "SELECT LONGNAME FROM node";
		try (PreparedStatement deleteNodePS = connection.prepareStatement(deleteNodeS)) {

			ResultSet rset = deleteNodePS.executeQuery();

			while (rset.next()) {
				String nodeID = rset.getString("LONGNAME");
				listOfNodeLongNames.add(nodeID);
			}
			rset.close();
			deleteNodePS.close();

			return listOfNodeLongNames;
		}catch(SQLException e){
			e.printStackTrace();
			System.err.println("getListofNodeIDS error try/catch");
			return listOfNodeLongNames;
		}

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
			System.err.println("getListOfNodeIDS error try/catch");
			return listOfNodeIDs;
		}
	}

	/**
	 * Gets a list of the nodeIDs of all of the nodes that are on the given floor
	 * @param floorName the name of the floor that the nodes will be selected on
	 * @return
	 */
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
			System.err.println("getListOfNodeIDS error try/catch");
			return listOfNodeIDs;
		}

	}

	/**
	 * gets all edges and each edge's attribute
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
	 * gets list of nodeIDS
	 * @return String[] of nodeIDs
	 */
	public ArrayList<String> getListOfNodeIDS() {
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
			System.err.println("getListOfNodeIDS error try/catch");
			return listOfNodeIDs;
		}

	}

	/**
	 * Translates a table into a csv file which can be later returned to the user.
	 * @param tableName this is the table/data/information that will be translated into a csv file
	 */
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



	// User System Stuff

	/**
	 * Function for logging a user in with their unique email and password, currently can only check for if correct or not
	 *
	 * @param email is the user's entered email
	 * @param password is the user's entered password
	 * @return true when the credentials match with a user in the database, and false otherwise
	 */
	public boolean userLogin(String email, String password) {
		String userLoginS = "Select Count(*) As verification From useraccount Where email = ? And password = ?";
		try (PreparedStatement userLoginPS = connection.prepareStatement(userLoginS)) {
			userLoginPS.setString(1, email);
			userLoginPS.setString(2, password);
			ResultSet userLoginRS = userLoginPS.executeQuery();
			int verification = 0;
			if (userLoginRS.next()) {
				verification = userLoginRS.getInt("verification");
			}
			userLoginRS.close();
			if(verification == 0){
				//mapEditor.errorPopup("Invalid username or password!");
			}
			return verification == 1;
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("countNodeTypeOnFloor() error");
		}
		return false;
	}

	/**
	 * Gets a list of all the requestIDs from the given tableName
	 * @param tableName this is the name of the table that we are getting the requestIDs from
	 * @return a list of all the requestIDs
	 */
	public ArrayList<String> getRequestIDs(String tableName){

		ArrayList<String> listOfIDs = new ArrayList<String>();

		try  {
			Statement stmt = connection.createStatement();
			String requestID = "Select requestID From " + tableName;

			ResultSet rset = stmt.executeQuery(requestID);

			while(rset.next()){
				String ID = rset.getString("requestID");
				listOfIDs.add(ID);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("getRequestStatus() error in the try/catch");

		}

		return listOfIDs;
	}

	/**
	 * Gets a list of all the statuses from the given tableName
	 * @param tableName this is the name of the table that we are getting the requestIDs from
	 * @return a list of all the statuses of the requests
	 */
	public ArrayList<String> getRequestStatus(String tableName){

		ArrayList<String> listOfStatus = new ArrayList<String>();

		try  {
			Statement stmt = connection.createStatement();
			String requestStatus = "Select requests.requestStatus From requests, " + tableName + " Where requests.requestID = " + tableName +".requestID";

			ResultSet rset = stmt.executeQuery(requestStatus);

			while(rset.next()){
				String status = rset.getString("requestStatus");
				listOfStatus.add(status);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("getRequestStatus() error in the try/catch");

		}

		return listOfStatus;
	}

	/**
	 * Gets a list of all the assignees from the given tableName
	 * @param tableName this is the name of the table that we are getting the requestIDs from
	 * @return a list of all assignees for all of the requests
	 */
	public ArrayList<String> getRequestAssignees(String tableName){

		ArrayList<String> listOfAssignees = new ArrayList<String>();

		try  {
			Statement stmt = connection.createStatement();
			String requestAssignee = "Select requests.assignee From requests, " + tableName + " Where requests.requestID = " + tableName +".requestID";

			ResultSet rset = stmt.executeQuery(requestAssignee);

			while(rset.next()){
				String status = rset.getString("assignee");
				listOfAssignees.add(status);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("getRequestAssignee() error in the try/catch");

		}

		return listOfAssignees;

	}

	/**
	 * Gets a list of all the longNames for the location from the given tableName
	 * @param tableName this is the name of the table that we are getting the requestIDs from
	 * @return a list of all longNames for the location for all of the requests
	 */
	public ArrayList<String> getRequestLocations(String tableName){

		ArrayList<String> listOfLongNames = new ArrayList<String>();

		try  {
			Statement stmt = connection.createStatement();
			String requestLongNames = "Select longName From node, " + tableName + " where node.nodeID = " + tableName + ".roomID";

			ResultSet rset = stmt.executeQuery(requestLongNames);

			while(rset.next()){
				String status = rset.getString("longName");
				listOfLongNames.add(status);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("getRequestLocations() error in the try/catch");

		}

		return listOfLongNames;
	}

// Duplicate node
// LongName too long

}
