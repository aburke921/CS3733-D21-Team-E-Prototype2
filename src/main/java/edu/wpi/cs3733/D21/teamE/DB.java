package edu.wpi.cs3733.D21.teamE;

import edu.wpi.TeamE.algorithms.Edge;
import edu.wpi.TeamE.algorithms.Node;
//import edu.wpi.TeamE.databases.EdgeDB;
//import edu.wpi.TeamE.databases.NodeDB;
//import edu.wpi.TeamE.databases.NodeDB;

import edu.wpi.cs3733.D21.teamE.database.NodeDB;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.util.ArrayList;

public class DB {
	public static void main(String[] args) {

		makeConnection BWDB;
//		BWDB = new CarSystem("Ford Focus", "KABC");
//		BWDB.drive();
//		BWDB.radio();
//		BWDB.turn("left");
//		BWDB.turn("right");
//		BWDB.MP3();
//		BWDB.stop();
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
	public static void createNodeTable(){
		NodeDB.createNodeTable();
	}

	/**
	 * matches the nodeID to a node and deletes it from DB
	 * @return the amount of rows affected by executing this statement, should be 1 in this case
	 */
	public static int deleteNode(String nodeID){
		return NodeDB.deleteNode(nodeID);
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
		return NodeDB.addNode(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName);
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
	public static int modifyNode(String nodeID,  Integer xCoord, Integer yCoord, String floor, String building, String nodeType, String longName, String shortName){
		return NodeDB.modifyNode(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName);
	}


	public static ArrayList<Node> getAllNodesByType(String type){
		return NodeDB.getAllNodesByType(type);
	}

	/**
	 * gets all Nodes (each row in node table)
	 * @return ArrayList of Node objects
	 * need Node constructor from UI/UX team
	 */
	public static ArrayList<Node> getAllNodes(){
		return NodeDB.getAllNodes();
	}

	/**
	 * gets a node's all attributes given nodeID
	 * @return a Node object with the matching nodeID
	 */
	public static Node getNodeInfo(String nodeID){
		return NodeDB.getNodeInfo(nodeID);
	}

	/**
	 * todo
	 * @return
	 */
	public static ObservableList<String> getAllNodeLongNames(){
		return NodeDB.getAllNodeLongNames();
	}

	/**
	 * counts the number of nodes already in the database of the given type and on the given floor that starts with the given teamNum
	 * @param teamNum be a String like "E"
	 * @param Floor   is a String like "L2"
	 * @param Type    is the nodeType of the node, like "ELEV"
	 * @return is how many nodes are already in the database given the params
	 */
	public static int countNodeTypeOnFloor(String teamNum, String Floor, String Type){
		return NodeDB.countNodeTypeOnFloor(teamNum, Floor, Type);
	}

	/**
	 * gets list of nodeIDS
	 * @return String[] of nodeIDs
	 */
	public static ArrayList<String> getListOfNodeIDS(){
		return NodeDB.getListOfNodeIDS();
	}


	/**
	 * Uses executes the SQL statements required to create the hasEdge table.
	 * This table has the attributes:
	 * - edgeID: this is a unique identifier for the edge connection. Every edge must contain an edgeID.
	 * - startNode: this is a nodeID in which the edge connection starts.
	 * - endNode: this is a nodeID in which the edge connection ends.
	 */
	public static void createEdgeTable(){
		EdgeDB.createEdgeTable();
	}

	public static void deleteEdgeTable(){
		EdgeDB.deleteEdgeTable();
	}

	/**
	 * Deletes edge(s) between the given two nodes, they can be in any order
	 * @return the amount of rows affected by executing this statement, should be 1 in this case, if there are two edges it returns 2
	 * if count == 1 || count == 2, edges have been deleted
	 * else no edges exist with inputted nodes
	 */
	public static int deleteEdge(String nodeID1, String nodeID2){
		return EdgeDB.deleteEdge(nodeID1, nodeID2);
	}

	/**
	 * Acts as a trigger and calculates the length between two nodes that form an edge and add the value to the edge table
	 * @param startNode the node ID for the starting node in the edge
	 * @param endNode   the node ID for the ending node in the edge
	 */
	public static void addLength(String startNode, String endNode){
		EdgeDB.addLength(startNode, endNode);
	}

	/**
	 * Adds an edge with said data to the database. Both startNode and endNode has to already exist in node table
	 * @param edgeID    this is a unique identifier for the edge connection. Every edge must contain an edgeID.
	 * @param startNode this is a nodeID in which the edge connection starts.
	 * @param endNode   this is a nodeID in which the edge connection ends.
	 * @return the amount of rows affected by executing this statement, should be 1 in this case
	 */
	public static int addEdge(String edgeID, String startNode, String endNode){
		return EdgeDB.addEdge(edgeID, startNode, endNode);
	}

	/**
	 * modifies an edge, updating the DB, returning 0 or 1 depending on whether operation was successful
	 * @param edgeID
	 * @param startNode
	 * @param endNode
	 * @return int (0 if node couldn't be added, 1 if the node was added successfully)
	 * need to check that both startNode and endNode already exist in node table
	 */
	public static int modifyEdge(String edgeID, String startNode, String endNode){
		return EdgeDB.modifyEdge(edgeID, startNode, endNode);
	}

	/**
	 * gets all edges and each edge's attribute
	 * @return ArrayList<Edge>
	 */
	public static ArrayList<Edge> getAllEdges(){
		return EdgeDB.getAllEdges();
	}

	/**
	 * gets edge information for all edges containing given nodeID
	 * @param nodeID
	 * @return Pair<Integer, String> map with edge information
	 */
	public static ArrayList<Pair<Float, String>> getEdgeInfo(String nodeID){
		return EdgeDB.getEdgeInfo(nodeID);
	}








}