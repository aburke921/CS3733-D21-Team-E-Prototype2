package edu.wpi.cs3733.D21.teamE.database;

import edu.wpi.cs3733.D21.teamE.map.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;


public class NodeDB {

	static Connection connection = makeConnection.makeConnection().connection;


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
	public static int createNodeTable() {
		String nodeTableCreateS = "Create Table node " +
				"( " +
				"nodeID    varchar(31) Primary Key, " +
				"xCoord    int        Not Null, " +
				"yCoord    int        Not Null, " +
				"floor     varchar(5) Not Null, " +
				"building  varchar(20), " +
				"nodeType  varchar(10), " +
				"longName  varchar(100), " +
				"shortName varchar(100), " +
				"Unique (xCoord, yCoord, floor), " +
				"Constraint floorLimit Check (floor In ('1', '2', '3', 'L1', 'L2', 'G')), " +
				"Constraint buildingLimit Check (building In ('BTM', '45 Francis', 'Tower', '15 Francis', 'Shapiro', 'Parking')), " +
				"Constraint nodeTypeLimit Check (nodeType In " +
				"                                ('PARK', 'EXIT', 'WALK', 'HALL', 'CONF', 'DEPT', 'ELEV', 'INFO', 'LABS', 'REST', " +
				"                                 'RETL', 'STAI', 'SERV', 'ELEV', 'BATH')) " +
				")";
		try (PreparedStatement nodeTableCreatePS = connection.prepareStatement(nodeTableCreateS)) {
			nodeTableCreatePS.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("|--- Failed to create node table");
			return 0;
		}
		return 1;
	}


	/**
	 * matches the nodeID to a node and deletes it from DB
	 * @return the amount of rows affected by executing this statement, should be 1 in this case
	 */
	public static int deleteNode(String nodeID) {
		String query = "Delete From node Where nodeid = ?";
		try (PreparedStatement prepStat = connection.prepareStatement(query)) {
			prepStat.setString(1, nodeID);
			// We might encounter issues if on delete cascade didn't work
			int numOfDeletedRows = prepStat.executeUpdate();
			if (numOfDeletedRows == 0) {
				System.err.println("deleteNode Result = 0, probably bad cuz no rows was affected");
			} else if (numOfDeletedRows != 1) {
				System.err.println("deleteNode Result =" + numOfDeletedRows + ", probably bad cuz " + numOfDeletedRows + " rows was affected");
			}
			return numOfDeletedRows;
			// deleteNode = x means the statement executed affected x rows, should be 1 in this case.
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Adds a node with said data to the database
	 * @param nodeID    this is a unique identifier for the each node. Every node must contain a nodeID.
	 * @param xCoord    this is the X-Coordinate/pixel location of the node on the map of the hospital.
	 * @param yCoord    this is the Y-Coordinate/pixel location of the node on the map of the hospital.
	 * @param floor     this is the floor of the hospital that the node is located on. The available options are: "1", "2", "3", "L1", "L2"
	 * @param building  this is the building of the hospital that the node is located in. The available options are: "BTM", "45 Francis", "Tower",
	 *                  "15 Francis", "Shapiro", "Parking".
	 * @param nodeType  this is the type room/location that the node is specifying. The available options are: "PARK" (parking), "EXIT" (exit),
	 *                  "WALK" (sidewalk/out door walkway), "HALL' (indoor walkway), "CONF" (conference room), "DEPT" (department room), "ELEV" (elevator),
	 *                  "INFO" (information), "LABS" (lab testing/results room), "REST" (rest areas/sitting areas), "RETL" (retail/food and shopping),
	 *                  "STAI" (stairs), "SERV" (services), "BATH" (bathrooms).
	 * @param longName  this is the long version/more descriptive name of the node/location/room
	 * @param shortName this is the short/nickname of the node/location/room
	 * @return the amount of rows affected by executing this statement, should be 1 in this case
	 */
	public static int addNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName) {
		String query = "Insert Into node Values (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement addNodePS = connection.prepareStatement(query)) {
			addNodePS.setString(1, nodeID);
			addNodePS.setInt(2, xCoord);
			addNodePS.setInt(3, yCoord);
			addNodePS.setString(4, floor);
			addNodePS.setString(5, building);
			addNodePS.setString(6, nodeType);
			addNodePS.setString(7, longName);
			addNodePS.setString(8, shortName);
			int numberOfRowsAdded = addNodePS.executeUpdate();
			if (numberOfRowsAdded == 0) {
				//mapEditor.errorPopup("Error in updating node");
				System.err.println("addNode Result = 0, probably bad cuz no rows was affected");
			} else if (numberOfRowsAdded != 1) {
				//mapEditor.errorPopup("Error in updating node");
				System.err.println("addNode Result =" + numberOfRowsAdded + ", probably bad cuz " + numberOfRowsAdded + " rows was affected");
			}
			addNodePS.close();
			return numberOfRowsAdded; // addNodeRS = x means the statement executed affected x rows, should be 1 in this case.
		} catch (SQLException e) {
			//e.printStackTrace();
			//mapEditor.errorPopup("There is already a node here or this node already exists");
			return 0;
		}
	}

	/**
	 * modifies a node, updating the DB, returning 0 or 1 depending on whether operation was successful
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
	public static int modifyNode(String nodeID, Integer xCoord, Integer yCoord, String floor, String building, String nodeType, String longName, String shortName) {

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

		try (PreparedStatement prepStat = connection.prepareStatement(query)) {

			prepStat.executeUpdate();
			return 1;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error in updating node");
			return 0;
		}
	}


	//POTENTIALLY COMBINED:

	public static ArrayList<Node> getAllNodesByType(String type) {
		ArrayList<Node> nodesArray = new ArrayList<>();

		String query = "select * from node WHERE '" + type + "' = nodeType Order By longName Asc";

		try (PreparedStatement prepStat = connection.prepareStatement(query)) {

			ResultSet rset = prepStat.executeQuery();

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
		} catch (SQLException e) {
			System.err.println("getAllNodes Error : " + e);
		}
		return nodesArray;
	}

	/**
	 * gets all Nodes (each row in node table)
	 * @return ArrayList of Node objects
	 * need Node constructor from UI/UX team
	 */
	public static ArrayList<Node> getAllNodes() {
		ArrayList<Node> nodesArray = new ArrayList<>();
//observable list -- UI


		String query = "Select * From node Order By longName Asc";
		try (PreparedStatement prepStat = connection.prepareStatement(query)) {

			ResultSet rset = prepStat.executeQuery();

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

		} catch (SQLException e) {
			System.err.println("getAllNodes Error : " + e);
		}
		return nodesArray;
	}


	/**
	 * gets a node's all attributes given nodeID
	 * @return a Node object with the matching nodeID
	 */
	public static Node getNodeInfo(String nodeID) {
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


	//POTENTIALLY COMBINED:

	/**
	 * todo
	 * @return
	 */
	public static ObservableList<String> getAllNodeLongNames() {
		ObservableList<String> listOfNodeLongNames = FXCollections.observableArrayList();

		String deleteNodeS = "Select longname From node";
		try (PreparedStatement deleteNodePS = connection.prepareStatement(deleteNodeS)) {

			ResultSet rset = deleteNodePS.executeQuery();

			while (rset.next()) {
				String nodeID = rset.getString("LONGNAME");
				listOfNodeLongNames.add(nodeID);
			}
			rset.close();
			deleteNodePS.close();

			return listOfNodeLongNames;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("getListofNodeIDS error try/catch");
			return listOfNodeLongNames;
		}

	}


	/**
	 * counts the number of nodes already in the database of the given type and on the given floor that starts with the given teamNum
	 * @param teamNum be a String like "E"
	 * @param Floor   is a String like "L2"
	 * @param Type    is the nodeType of the node, like "ELEV"
	 * @return is how many nodes are already in the database given the params
	 */
	public static int countNodeTypeOnFloor(String teamNum, String Floor, String Type) {
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
			e.printStackTrace();
			System.err.println("countNodeTypeOnFloor() error");
		}
		return -1;
	}


	//POTENTIALLY COMBINED:

	/**
	 * gets list of nodeIDS
	 * @return String[] of nodeIDs
	 */
	public static ArrayList<String> getListOfNodeIDS() {
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


	// previously deleted but they are back!:

	public static void deleteNodeTable() {

		try {
			Statement stmt = connection.createStatement();
			stmt.execute("Drop Table node");
			stmt.close();
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("deleteNodeTable() not working");
		}

	}

	/**
	 * Given a NodeID, gives the xCoord, yCoord, Floor and Type of that node from Nodes
	 * @param nodeID is the nodeID of the nodes you want info from
	 * @return a Node with only xCoord, yCoord, floor and nodeType not null
	 */
	public static Node getNodeLite(String nodeID) {
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
	 * Gets all node long names for a specified FLOOR column value.
	 * @param floorName the value to check for in FLOOR column
	 * @return ObservableList of node long names.
	 */
	public static ObservableList<String> getAllNodeLongNamesByFloor(String floorName) {
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
	public static ArrayList<String> getListOfNodeIDSByFloor(String floorName) {
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
	 * gets all Nodes that have the given FLOOR value
	 * @param floorName the value to check for in FLOOR column
	 * @return ArrayList of Node objects
	 */
	public static ArrayList<Node> getAllNodesByFloor(String floorName) {
		ArrayList<Node> nodesArray = new ArrayList<>();
		try {
			Statement stmt = connection.createStatement();
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


}
