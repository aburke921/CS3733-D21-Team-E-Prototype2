package edu.wpi.TeamE.databases;

import edu.wpi.TeamE.algorithms.pathfinding.Edge;
import edu.wpi.TeamE.algorithms.pathfinding.Node;
import javafx.util.Pair;
import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class makeConnection {


	// static variable singleInstance of type SingleConnection
	public static makeConnection singleInstance = null;

	public Connection connection;

	// private constructor restricted to this class itself
	public makeConnection()
	{
		// Initialize DB
		System.out.println("Starting connection to Apache Derby\n");
		try {

			//Makes it so a username and password (hardcoded) is needed to access the database data
			Properties props = new Properties();
			props.put("user", "admin");
			props.put("password", "admin");

			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

			try {
				this.connection = DriverManager.getConnection("jdbc:derby:BWDB;create=true", props);
				// this.connection.setAutoCommit(false);
			} catch (SQLException e) {
				// e.printStackTrace();
				System.err.println("error with the DriverManager");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("error with the EmbeddedDriver class.forName thing");
		}
	}

	// static method to create instance of Singleton class
	public static makeConnection makeConnection()
	{
		// To ensure only one instance is created
		if (singleInstance == null)
		{
			singleInstance = new makeConnection();
		}
		return singleInstance;
	}

	/**
	 * Creates node and hasEdge tables
	 * try/catch phrase set up in case the tables all ready exist
	 */
	public void createTables() {

		try {
			Statement stmt = this.connection.createStatement();
			stmt.execute(
					"create table node"
							+ "("
							+ "    nodeID    varchar(31) primary key,"
							+ "    xCoord    int not null,"
							+ "    yCoord    int not null,"
							+ "    floor     varchar(5) not null,"
							+ "    building  varchar(20),"
							+ "    nodeType  varchar(10),"
							+ "    longName  varchar(100),"
							+ "    shortName varchar(100),"
							+ "    unique (xCoord, yCoord, floor)"
							+ ")");

		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating node table");
		}

		try {

			Statement stmt = connection.createStatement();
			stmt.execute(
					"create table hasEdge"
							+ "("
							+ "    edgeID    varchar(63) primary key,"
							+ "    startNode varchar(31) not null references node (nodeID) on delete cascade,"
							+ "    endNode   varchar(31) not null references node (nodeID) on delete cascade, "
							+ "    length    float, "
							+ "    unique (startNode, endNode)"
							+ ")");

			// Needs a way to calculate edgeID, either in Java or by a sql trigger
			// Probably in Java since it's a PK

		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating hasEdge table");
		}
	}


	/**
	 * Deletes node and hasEdges table
	 * try/catch phrase set up in case the tables all ready do not exist
	 */
	public void deleteAllTables() {

		try {
			Statement stmt = this.connection.createStatement();
			stmt.execute("drop table hasEdge");
			stmt.execute("drop table node");
			stmt.close();
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("deleteAllTables() not working");
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
	 * gets a node's all attributes given nodeID
	 *
	 * @return a Node object with the matching nodeID
	 */

	public Node getNodeInfo(String nodeID) {
		String getNodeInfoS = "select * from node where nodeID = ?";
		try (PreparedStatement getNodeInfoPS = connection.prepareStatement(getNodeInfoS)) {
			getNodeInfoPS.setString(1, nodeID);

			ResultSet getNodeInfoRS = getNodeInfoPS.executeQuery();

			while(getNodeInfoRS.next()) {
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
		String getNodeLiteS = "select xCoord, yCoord, floor, nodeType from node where nodeID = ?";
		try (PreparedStatement getNodeLitePS = connection.prepareStatement(getNodeLiteS)) {
      getNodeLitePS.setString(1, nodeID);
			ResultSet getNodeLiteRS = getNodeLitePS.executeQuery();

			while(getNodeLiteRS.next()) {
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
	 * @return ArrayList of Node objects
	 * need Node constructor from UI/UX team
	 */

	public ArrayList<Node> getAllNodes() {
		ArrayList<Node> nodesArray = new ArrayList<>();
//observable list -- UI
		try {
			Statement stmt = this.connection.createStatement();
			String query = "select * from node";
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
			System.err.println("getAllNodes Error");
		}
		return nodesArray;
	}

	/**
	 * gets all edges and each edge's attribute
	 * @return ArrayList<Edge>
	 */
	public ArrayList<Edge> getAllEdges() {
		ArrayList<Edge> edgesArray = new ArrayList<>();
		try {
			Statement stmt = this.connection.createStatement();
			String query = "select * from hasEdge";
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
			System.err.println("getAllEdges Error");
		}
		return edgesArray;
	}



	/**
	 * adds a node with said data to the database
	 *
	 * @return the amount of rows affected by executing this statement, should be 1 in this case
	 */
	public int addNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName) {
		String addNodeS = "insert into node values (?, ?, ?, ?, ?, ?, ?, ?)";
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
		String addEdgeS = "insert into hasEdge (edgeID, startNode, endNode) values (?, ?, ?)";
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
		if(yCoord != null) {
			if(added == true) {
				query = query + ", ";
			}
			query = query + " yCoord = " + yCoord;
			added = true;
		}
		if(floor != null) {
			if(added == true) {
				query = query + ", ";
			}
			query = query + " floor = '" + floor + "'";
			added = true;
		}
		if(building != null) {
			if(added == true) {
				query = query + ", ";
			}
			query = query + " building = '" + building + "'";
			added = true;
		}
		if(nodeType != null) {
			if(added == true) {
				query = query + ", ";
			}
			query = query + " nodeType = '" + nodeType + "'";
			added = true;
		}
		if(longName != null) {
			if(added == true) {
				query = query + ", ";
			}
			query = query + " longName = '" + longName + "'";
			added = true;
		}
		if(shortName != null) {
			if(added == true) {
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
		}
		catch (SQLException e) {
			e.printStackTrace();System.err.println("Error in updating node");
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

		String checkEdgeID = "select edgeID from hasEdge where edgeID = ?";
		try (PreparedStatement prepState1 = connection.prepareStatement(checkEdgeID)) {
			prepState1.setString(1, edgeID);
			ResultSet rset1 = prepState1.executeQuery();

			if(rset1.next()){
				correctEdgeID = true;
			}

		}catch(SQLException e){
			//e.printStackTrace();
			return 0;
		}

		if(startNode != null){
			String checkStartNode = "select nodeID from node where nodeID = ?";
			try (PreparedStatement prepState2 = connection.prepareStatement(checkStartNode)) {
				prepState2.setString(1, startNode);
				ResultSet rset2 = prepState2.executeQuery();

				if(rset2.next()) {
					correctStartNode = true;
				}

			}catch(SQLException e) {
				System.err.println("given start node does not exist");
				//e.printStackTrace();
				return 0;
			}
		}

		if(endNode != null){
			String checkEndNode = "select nodeID from node where nodeID = ?";
			try (PreparedStatement prepState3 = connection.prepareStatement(checkEndNode)) {
				prepState3.setString(1, endNode);
				ResultSet rset3 = prepState3.executeQuery();

				if(rset3.next()) {
					correctEndNode = true;
				}


			}catch(SQLException e) {
				//e.printStackTrace();
				return 0;
			}
		}



		if((correctEdgeID && correctStartNode) || (correctEdgeID && correctEndNode) || (correctEdgeID && correctStartNode && correctEndNode)){
			String[] nodes = edgeID.split("_");
			deleteEdge(nodes[0], nodes[1]);
			if(startNode == null){
				String newEdgeID = nodes[0] + "_" + endNode;
				addEdge(newEdgeID, nodes[0], endNode);
				return 1;
			}
			if(endNode == null){
				String newEdgeID = startNode + "_" + nodes[1];
				addEdge(newEdgeID, startNode, nodes[1]);
				return 1;
			}
			else{
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

		String deleteEdgeS1 = "delete from hasEdge where startNode = ? and endNode = ?";
		String deleteEdgeS2 = "delete from hasEdge where endNode = ? and startNode = ?";

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
			} else if (deleteEdgeRS1 != 1) {
				System.err.println("deleteEdge Result =" + deleteEdgeRS1 + ", just bad because this should never occur");
			}
			System.out.println("Number of rows affected: " + deleteEdgeRS1);
			count = 1;
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
			} else if (deleteEdgeRS2 != 1) {
				System.err.println("deleteEdge Result =" + deleteEdgeRS2 + ", just bad because this should never occur");
			}
			System.out.println("Number of rows affected: " + deleteEdgeRS2);
			count += count;
			// deleteEdgeRS2 = x means the statement executed affected x rows, should be 1 in this case, if there are two edges it returns 2.
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("deleteEdge() tyr/catch error");
			if (count == 0) {
				count = 0;
			}
			else
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
		String deleteNodeS = "delete from node where nodeID = ?";
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

		String deleteNodeS = "SELECT nodeID FROM node";
		try (PreparedStatement deleteNodePS = connection.prepareStatement(deleteNodeS)) {

			ResultSet rset = deleteNodePS.executeQuery();

			while (rset.next()) {
				String nodeID = rset.getString("nodeID");
				listOfNodeIDs.add(nodeID);
			}
			rset.close();
			deleteNodePS.close();

			return listOfNodeIDs;
		}catch(SQLException e){
			e.printStackTrace();
			System.err.println("getListofNodeIDS error try/catch");
			return listOfNodeIDs;
		}

	}


	public void getNewCSVFile(String tableName){

		String sqlQuery = "select * from " + tableName;
		try (PreparedStatement prepState = connection.prepareStatement(sqlQuery)) {

			ResultSet rset1 = prepState.executeQuery();

				if(tableName == "node") {

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

				if(tableName == "hasEdge"){
					StringBuilder edgeSB = new StringBuilder();

					while(rset2.next()) {
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

			}
			catch (SQLException throwables) {
			throwables.printStackTrace();
			}
			catch (IOException ioException) {
			ioException.printStackTrace();
			}
	}

	public static void main(String[] args) {
		System.out.println("STARTING UP!!!");
		File nodes = new File("src/main/resources/edu/wpi/TeamE/csv/bwEnodes.csv");
		File edges = new File("src/main/resources/edu/wpi/TeamE/csv/bwEedges.csv");

		makeConnection connection = makeConnection.makeConnection();
		connection.deleteAllTables();
		connection.createTables();
		connection.populateTable("node", nodes);
		connection.populateTable("hasEdge", edges);

		connection.getNewCSVFile("node");
		connection.getNewCSVFile("hasEdge");
//		connection.addNode("test1", 0, 0,"test", "test", "test", "test", "test");
//		connection.addNode("test2", 2, 2,"test", "test", "test", "test", "test");
//		connection.addNode("test3", 3, 3,"test", "test", "test", "test", "test");
//		connection.addNode("test4", 4, 4,"test", "test", "test", "test", "test");
//
//		connection.addEdge("test1_test2", "test1", "test2");
//		connection.addEdge("test2_test3", "test2", "test3");
//		connection.addEdge("test1_test3", "test1", "test3");

		//int i = connection.modifyEdge("test1_test2", null, "test3");
		//System.out.println(i);
	}
}

