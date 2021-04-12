package edu.wpi.TeamE.databases;

import edu.wpi.TeamE.algorithms.pathfinding.Node;
import javafx.util.Pair;

import java.sql.*;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class functions {


	public static Connection makeConnection() {

		// Initialize connection to DB
		System.out.println("Starting connection to Apache Derby\n");
		Connection connection;
		try {

			Properties props = new Properties();
			props.put("user", "admin");
			props.put("password", "admin");

			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

			// remove :memory to have persistent database
			try {
				connection = DriverManager.getConnection("jdbc:derby:BWDB;create=true", props);
				return connection;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Given a NodeID, gives the xCoord, yCoord, Floor and Type of that node from Nodes
	 *
	 * @param con    is just the DB connection for this run
	 * @param nodeID is the nodeID of the nodes you want info from
	 */
	public static Node getNodeLite(Connection con, String nodeID) {
		String selectString = "select xCoord, yCoord, floor, nodeType from node where nodeID = ?";
		try (PreparedStatement selectNode = con.prepareStatement(selectString)) {
			con.setAutoCommit(false);
			selectNode.setString(1, nodeID);
			ResultSet resultSet = selectNode.executeQuery();
			int xCoord = resultSet.getInt("xCoord");
			int yCoord = resultSet.getInt("yCoord");
			String floor = resultSet.getString("floor");
			String nodeType = resultSet.getString("nodeType");
			resultSet.close();
			Node node = new Node(nodeID, xCoord, yCoord, floor, null, nodeType, null, null);
			return node;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Node node = new Node();
		return node;
	}

	/**
	 * Given a NodeID, gives the xCoord, yCoord, Floor and Type of that node from Nodes
	 *
	 * @param con    is just the DB connection for this run
	 * @param nodeID is the nodeID of the nodes you want info from
	 */
	/*public static Node getNodeLiteUnprepared(Connection con, int nodeID) {
		try {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery("select xCoord, yCoord, floor, nodeType from node where nodeID = " + nodeID);
			while (resultSet.next()) {
				int xCoord = resultSet.getInt("xCoord");
				int yCoord = resultSet.getInt("yCoord");
				String floor = resultSet.getString("floor");
				String nodeType = resultSet.getString("nodeType");
			}
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Node node = new Node();
		return node;
	}
*/
	/**
	 * Given a NodeID, gives the xCoord, yCoord, Floor and Type of that node from Nodes
	 *
	 * @param con    is just the DB connection for this run
	 * @param nodeID is the nodeID of the nodes you want info from
	 */
	/*public static void getNeighbors(Connection con, int nodeID) {
		String getNeighborS = "select startNode as neighborID, length from edgeLength where endNode = ? union select endNode as neighborID, length from edgeLength where startNode = ?";
		try (PreparedStatement getNeighborPS = con.prepareStatement(getNeighborS)) {
			con.setAutoCommit(false);
			getNeighborPS.setInt(1, nodeID);
			getNeighborPS.setInt(2, nodeID);
			ResultSet resultSet = getNeighborPS.executeQuery();
			List<Map.Entry<Float, Short>> pairList;
			Pair<Integer, Float> integerFloatPairs;
			while (resultSet.next()) {
				pairList.add(new AbstractMap.SimpleEntry<>(resultSet.getInt("neighborID"), resultSet.getInt("neighborID")));
				Pair<Integer, Float> pair = new Pair<>(resultSet.getInt("neighborID"), resultSet.getFloat("length"));
				int neighborID = resultSet.getInt("neighborID");
			}
			resultSet.close();
			//Node node = new Node(nodeID, xCoord, yCoord, floor, null, nodeType, null, null);
			//return node;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/

}
