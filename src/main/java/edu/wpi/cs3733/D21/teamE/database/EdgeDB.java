package edu.wpi.cs3733.D21.teamE.database;

import edu.wpi.cs3733.D21.teamE.map.Edge;
import javafx.util.Pair;
import java.sql.*;
import java.util.ArrayList;

public class EdgeDB {

	static Connection connection = makeConnection.makeConnection().getConnection();

	/**
	 * Uses executes the SQL statements required to create the hasEdge table.
	 * This table has the attributes:
	 * - edgeID: this is a unique identifier for the edge connection. Every edge must contain an edgeID.
	 * - startNode: this is a nodeID in which the edge connection starts.
	 * - endNode: this is a nodeID in which the edge connection ends.
	 */
	public static void createEdgeTable() {

		String query = "Create Table hasEdge"
				+ "("
				+ "    edgeID    varchar(63) Primary Key,"
				+ "    startNode varchar(31) Not Null References node (nodeid) On Delete Cascade,"
				+ "    endNode   varchar(31) Not Null References node (nodeid) On Delete Cascade, "
				+ "    length    float, "
				+ "    Unique (startNode, endNode)"
				+ ")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating hasEdge table");
		}
	}

	public static void deleteEdgeTable() {

		try {
			Statement stmt = connection.createStatement();
			stmt.execute("Drop Table hasedge");
			stmt.close();
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("deleteEdgeTable() not working");
		}

	}


	/**
	 * Deletes edge(s) between the given two nodes, they can be in any order
	 * @return the amount of rows affected by executing this statement, should be 1 in this case, if there are two edges it returns 2
	 * if count == 1 || count == 2, edges have been deleted
	 * else no edges exist with inputted nodes
	 */
	public static int deleteEdge(String nodeID1, String nodeID2) {

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
	 * Acts as a trigger and calculates the length between two nodes that form an edge and add the value to the edge table
	 * @param startNode the node ID for the starting node in the edge
	 * @param endNode   the node ID for the ending node in the edge
	 */
	public static void addLength(String startNode, String endNode) {

		String sqlQuery;
		int startX = 0;
		int startY = 0;
		int endX = 0;
		int endY = 0;

		ResultSet rset;
		Statement stmt;

		try {
			stmt = connection.createStatement();
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
			stmt = connection.createStatement();
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


			stmt = connection.createStatement();

			sqlQuery = "UPDATE hasEdge SET length = " + length + " WHERE startNode = '" + startNode + "' AND endNode = '" + endNode + "'";

			//executes the SQL insert statement (inserts the data into the table)
			stmt.executeUpdate(sqlQuery);

			stmt.close();

		} catch (SQLException e) {
			System.err.println("UPDATE try/catch failed");
		}


	}



	/**
	 * Adds an edge with said data to the database. Both startNode and endNode has to already exist in node table
	 * @param edgeID    this is a unique identifier for the edge connection. Every edge must contain an edgeID.
	 * @param startNode this is a nodeID in which the edge connection starts.
	 * @param endNode   this is a nodeID in which the edge connection ends.
	 * @return the amount of rows affected by executing this statement, should be 1 in this case
	 */
	public static int addEdge(String edgeID, String startNode, String endNode) {
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
	 * modifies an edge, updating the DB, returning 0 or 1 depending on whether operation was successful
	 * @param edgeID
	 * @param startNode
	 * @param endNode
	 * @return int (0 if node couldn't be added, 1 if the node was added successfully)
	 * need to check that both startNode and endNode already exist in node table
	 */
	public static int modifyEdge(String edgeID, String startNode, String endNode) {
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
	 * gets all edges and each edge's attribute
	 * @return ArrayList<Edge>
	 */
	public static ArrayList<Edge> getAllEdges() {
		ArrayList<Edge> edgesArray = new ArrayList<>();
		try {
			Statement stmt = connection.createStatement();
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
	 * gets edge information for all edges containing given nodeID
	 * @param nodeID
	 * @return Pair<Integer, String> map with edge information
	 */
	public static ArrayList<Pair<Float, String>> getEdgeInfo(String nodeID) {

		ArrayList<Pair<Float, String>> pair = new ArrayList<Pair<Float, String>>();

		try {
			Statement stmt = connection.createStatement();
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
			Statement stmt = connection.createStatement();
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


}
