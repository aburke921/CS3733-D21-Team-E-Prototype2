package edu.wpi.TeamE.databases;

import edu.wpi.TeamE.algorithms.pathfinding.Node;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class makeConnection {

	Connection connection;

	//Node Node = new Node();

    /*

    EASY COPY AND PAST CODE----------------



    try {
        Statement stmt = this.connection.createStatement();

        String sqlQuery = "";

        ResultSet rset = stmt.executeQuery(sqlQuery);

        while (rset.next()) {

            int startX = rset.getInt("xCoord");
            String startY = rset.getString("nodeID");
        }
        rset.close();
        stmt.close();
    }catch (SQLException e){
        System.err.println("Error");
    }

    */


	/**
	 * Constructor makes the database connection
	 */
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
				this.connection = DriverManager.getConnection("jdbc:derby:BWDB;create=true", props);
			} catch (SQLException e) {
				// e.printStackTrace();
				System.err.println("error with the DriverManager");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("error with the EmbeddedDriver class.forName thing");
		}

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
							+ "    startNode varchar(31) not null references node (nodeID),"
							+ "    endNode   varchar(31) not null references node (nodeID), "
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
	 * gets given nodeID's attributes
	 * @param nodeID
	 * @return a Node object with the matching nodeID
	 * need Node constructor from Algorithms team
	 */
//    public Node getNodeInfo(String nodeID) {
//
//    }

	/**
	 * Given a NodeID, gives the xCoord, yCoord, Floor and Type of that node from Nodes
	 *
	 * @param nodeID is the nodeID of the nodes you want info from
	 * @return a Node with only xCoord, yCoord, floor and nodeType not null
	 */
	public Node getNodeLite(String nodeID) {
		String getNodeLiteS = "select xCoord, yCoord, floor, nodeType from node where nodeID = ?";
		try (PreparedStatement getNodeLitePS = connection.prepareStatement(getNodeLiteS)) {
			connection.setAutoCommit(false);
			getNodeLitePS.setString(1, nodeID);
			ResultSet getNodeLiteRS = getNodeLitePS.executeQuery();
			int xCoord = getNodeLiteRS.getInt("xCoord");
			int yCoord = getNodeLiteRS.getInt("yCoord");
			String floor = getNodeLiteRS.getString("floor");
			String nodeType = getNodeLiteRS.getString("nodeType");
			getNodeLiteRS.close();
			return new Node(nodeID, xCoord, yCoord, floor, null, nodeType, null, null);
		} catch (SQLException e) {
			e.printStackTrace();
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

		ArrayList<Pair<Float, String>> pair = null;

		try {
			Statement stmt = this.connection.createStatement();
			String query = "select startNode from hasEdge where endNode = " + nodeID;
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
			String query = "select endNode from hasEdge where startNode = " + nodeID;
			ResultSet rset = stmt.executeQuery(query);

			while (rset.next()) {
				float length = rset.getFloat("length");
				String endNodeID = rset.getString("endNode");

				pair.add(new Pair<>(length, endNodeID));
			}

			rset.close();
			stmt.close();
		} catch (SQLException e) {
			System.err.println("startNode Error");
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

		try {
			Statement stmt = this.connection.createStatement();
			String query = "select * from node";
			ResultSet rset = stmt.executeQuery(query);

			while (rset.next()) {
				String NodeID = rset.getString("NodeID");
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
	 * adds a node with said data to the database
	 *
	 * @return int (the amount of rows affected by executing this statement, should be 1 in this case)
	 */

	public int addNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName) {
		String addNodeS = "insert into node values (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement addNodePS = connection.prepareStatement(addNodeS)) {
			connection.setAutoCommit(false);
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
		}
		return 0;
	}

	/**
	 * adds an edge with said data to the database
	 * both startNode and endNode has to already exist in node table
	 *
	 * @return int (the amount of rows affected by executing this statement, should be 1 in this case)
	 */

	public int addEdge(String edgeID, String startNode, String endNode) {
		String addEdgeS = "insert into hasEdge values (?, ?, ?)";
		try (PreparedStatement addEdgePS = connection.prepareStatement(addEdgeS)) {
			connection.setAutoCommit(false);
			addEdgePS.setString(1, edgeID);
			addEdgePS.setString(2, startNode);
			addEdgePS.setString(3, endNode);
			// TODO: make a coordinate look-up so we can calculate and input the length of the edge
			// addEdgePS.setFloat(4, (Math.sqrt(Math.pow(startNode.getX() - endNode.getX(), 2) + Math.pow(startNode.getY() - endNode.getY(), 2))));
			int addEdgeRS = addEdgePS.executeUpdate();
			if (addEdgeRS == 0) {
				System.err.println("addNode Result = 0, probably bad cuz no rows was affected");
			} else if (addEdgeRS != 1) {
				System.err.println("addNode Result =" + addEdgeRS + ", probably bad cuz " + addEdgeRS + " rows was affected");
			}
			return addEdgeRS; // addEdgeRS = x means the statement executed affected x rows, should be 1 in this case.
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
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
	public int modifyNode(String nodeID, int xCoord, int yCoord, String floor, String building, String nodeType, String longName, String shortName) {
		return 0;
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
		return 0;
	}


	/**
	 * any edge containing the given nodeID (startNode or endNode) is deleted
	 *
	 * @param nodeID
	 * @return int (0 if node couldn't be added, 1 if the node was added successfully)
	 */
	public int deleteEdge(String nodeID) {

		try {
			Statement stmt = this.connection.createStatement();

			String sqlQuery = "DELETE FROM hasEdge WHERE startNode = '" + nodeID + "' OR endNode = '" + nodeID + "'";

			stmt.executeUpdate(sqlQuery);
			stmt.close();

			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error");
			return 0;
		}
	}


	/**
	 * matches the nodeID to a node and deletes it from DB, returning 0 or 1 depending on whether operation was successful
	 *
	 * @param nodeID
	 * @return int (0 if node couldn't be added, 1 if the node was added successfully)
	 * make sure the edges containing this node are deleted
	 */
	public int deleteNode(String nodeID) {
		// has to call deleteEdge(nodeID) before deleting the node
		return 0;
	}

	/**
	 * gets list of nodeIDS
	 *
	 * @return String[] of nodeIDs
	 */
	public String[] getListofNodeIDS() {
		String[] array = {};
		return array;
	}


	public static void main(String[] args) {

		makeConnection connection = new makeConnection();

		System.out.println("made it here!");
		connection.deleteAllTables();
		connection.createTables();
		File nodes = new File("src/main/resources/edu/wpi/TeamE/csv/bwEnodes.csv");
		File edges = new File("src/main/resources/edu/wpi/TeamE/csv/bwEedges.csv");
		connection.populateTable("node", nodes);
		connection.populateTable("hasEdge", edges);

		// getsAllNodes (returns ArrayList<Node>)
		connection.getAllNodes();

		//connection.populateTable("node", "L1Nodes.csv");
		//connection.populateTable("hasEdge", "L1Edges.csv");
		//System.out.println(connection.deleteEdge("CCONF002L1"));
	}
}

