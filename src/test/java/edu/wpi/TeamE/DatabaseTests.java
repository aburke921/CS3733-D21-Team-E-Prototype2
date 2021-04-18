package edu.wpi.TeamE;

import edu.wpi.TeamE.algorithms.Edge;
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class DatabaseTests {

	makeConnection connection;

	@BeforeAll
	public void setConnection() {
		connection = makeConnection.makeConnection();
	}

    @BeforeEach
    public void setupTables(){
        try {
            connection.deleteAllTables();
	        connection.createNodeTable();
	        connection.createEdgeTable();
	        connection.createUserAccountTable();
	        connection.createRequestsTable();
	        connection.createFloralRequestsTable();
			connection.createSanitationTable();
			connection.createExtTransportTable();
            System.out.println("Tables were reset");
        } catch (Exception e) {
			connection.createNodeTable();
			connection.createEdgeTable();
			connection.createUserAccountTable();
			connection.createRequestsTable();
			connection.createFloralRequestsTable();
			connection.createSanitationTable();
			connection.createExtTransportTable();
			System.out.println("Tables were created");
        }
    }

    @Test
	public void testing(){

	}

	@Test
	@DisplayName("testGetNodeInfo")
	public void testGetNodeInfo() {
		connection.addNode("test123", 12, 13, "1", "45 Francis", "PARK", "longName", "shortName");

		Node testNode1 = new Node("test123", 12, 13, "1", "45 Francis", "PARK", "longName", "shortName");

		Node testNode2 = connection.getNodeInfo("test123");

		assertTrue(testNode1.equals(testNode2));
	}


	@Test
	@DisplayName("GetNodeInfo for a node with null values")
	public void testGetNodeInfo2() {
		connection.addNode("test123", 12, 13, "1", "BTM", "PARK", "long", null);

		Node testNode1 = new Node("test123", 12, 13, "1", "BTM", "PARK", "long", null);

		Node testNode2 = connection.getNodeInfo("test123");

		assertTrue(testNode1.equals(testNode2));
	}


	@Test
	@DisplayName("testGetNodeLite")
	public void testGetNodeLite() {
		connection.addNode("test233", 22, 33, "1", "BTM", "WALK", "long", null);

		Node testNode1 = new Node("test233", 22, 33, "1", "BTM", "WALK", "long", null);

		Node testNode2 = connection.getNodeLite("test233");

		assertTrue(testNode1.equals(testNode2));
	}

	@Test
	@DisplayName("testGetEdgeInfo")
	public void testGetEdgeInfo() {
		connection.addNode("testEdge1", 121, 122, "1", "BTM", "EXIT", "String", "String");
		connection.addNode("testEdge2", 12, 15, "1", "BTM", "EXIT", "String", "String");
		connection.addNode("testEdge3", 122, 123, "1", "BTM", "EXIT", "String", "String");
		connection.addNode("testEdge4", 124, 153, "1", "BTM", "EXIT", "String", "String");

		connection.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");
		connection.addEdge("testEdge3_testEdge4", "testEdge3", "testEdge4");
		connection.addEdge("testEdge2_testEdge4", "testEdge2", "testEdge4");

		ArrayList<Pair<Float, String>> listOfEdgeInfo = new ArrayList<>();
		ArrayList<Pair<Float, String>> resultEdgeInfo;

		double length3 = Math.pow((122 - 124), 2);
		double length4 = Math.pow((123 - 153), 2);

		double length34 = Math.sqrt((length3 + length4));

		double length1 = Math.pow((12 - 124), 2);
		double length2 = Math.pow((15 - 153), 2);

		double length24 = Math.sqrt((length1 + length2));

		listOfEdgeInfo.add(new Pair<>((float) length34, "testEdge3"));
		listOfEdgeInfo.add(new Pair<>((float) length24, "testEdge2"));

		resultEdgeInfo = connection.getEdgeInfo("testEdge4");

		assertEquals(resultEdgeInfo, listOfEdgeInfo);

	}


	@Test
	@DisplayName("testGetAllNodes")
	public void testGetAllNodes() {


		connection.addNode("testEdge1", 121, 122, "1", "BTM", "EXIT", "String", "String");
		connection.addNode("testEdge2", 12, 15, "1", "BTM", "EXIT", "String", "String");
		connection.addNode("testEdge3", 122, 123, "1", "BTM", "EXIT", "String", "String");
		connection.addNode("testEdge4", 124, 153, "1", "BTM", "EXIT", "String", "String");


		Node n1 = new Node("testEdge1", 121, 122, "1", "BTM", "EXIT", "String", "String");
		Node n2 = new Node("testEdge2", 12, 15, "1", "BTM", "EXIT", "String", "String");
		Node n3 = new Node("testEdge3", 122, 123, "1", "BTM", "EXIT", "String", "String");
		Node n4 = new Node("testEdge4", 124, 153, "1", "BTM", "EXIT", "String", "String");

		ArrayList<Node> testNodeArray = new ArrayList<>();
		testNodeArray.add(n1);
		testNodeArray.add(n2);
		testNodeArray.add(n3);
		testNodeArray.add(n4);

		ArrayList<Node> nodeArray = connection.getAllNodes();

		boolean allCorrect = true;
		boolean nodeID = false;
		boolean xCoord = false;
		boolean yCoord = false;
		boolean floor = false;
		boolean building = false;
		boolean nodeType = false;
		boolean longName = false;
		boolean shortName = false;

		if (testNodeArray.size() == nodeArray.size()) {
			for (int node = 0; node < nodeArray.size(); node++) {
				Node returnedNode = nodeArray.get(node);
				Node correctNode = testNodeArray.get(node);
				if (returnedNode.get("id").equals(correctNode.get("id"))) {
					nodeID = true;
				}
				if (returnedNode.getX() == correctNode.getX()) {
					xCoord = true;
				}
				if (returnedNode.getY() == correctNode.getY()) {
					yCoord = true;
				}
				if (returnedNode.get("floor").equals(correctNode.get("floor"))) {
					floor = true;
				}
				if (returnedNode.get("building").equals(correctNode.get("building"))) {
					building = true;
				}
				if (returnedNode.get("type").equals(correctNode.get("type"))) {
					nodeType = true;
				}
				if (returnedNode.get("longName").equals(correctNode.get("longName"))) {
					longName = true;
				}
				if (returnedNode.get("shortName").equals(correctNode.get("shortName"))) {
					shortName = true;
				}
				if (nodeID && xCoord && yCoord && floor && building && nodeType && longName && !shortName) {
					allCorrect = false;
				}
			}
		} else {
			allCorrect = false;
		}

		assertTrue(allCorrect);

	}


	@Test
	@DisplayName("testGetAllNodesByFloor: 2 nodes with the given floor")
	public void testGetAllNodesByFloor() {
		connection.addNode("nodeID1", 0, 0, "1", "Tower", "PARK", "longName1", "shortName1");
		connection.addNode("nodeID2", 1, 0, "1", "Tower", "PARK", "longName2", "shortName2");

		ArrayList<Node> nodes = connection.getAllNodesByFloor("1");

		ArrayList<Node> correctNodes = new ArrayList<>();
		Node node1 = new Node("nodeID1", 0, 0, "1", "Tower", "PARK", "longName1", "shortName1");
		Node node2 = new Node("nodeID2", 1, 0, "1", "Tower", "PARK", "longName2", "shortName2");

		correctNodes.add(node1);
		correctNodes.add(node2);

		boolean allCorrect = true;
		boolean nodeID = false;
		boolean xCoord = false;
		boolean yCoord = false;
		boolean floor = false;
		boolean building = false;
		boolean nodeType = false;
		boolean longName = false;
		boolean shortName = false;

		if (nodes.size() == correctNodes.size()) {
			for (int node = 0; node < nodes.size(); node++) {
				Node returnedNode = nodes.get(node);
				Node correctNode = correctNodes.get(node);
				if (returnedNode.get("id").equals(correctNode.get("id"))) {
					nodeID = true;
				}
				if (returnedNode.getX() == correctNode.getX()) {
					xCoord = true;
				}
				if (returnedNode.getY() == correctNode.getY()) {
					yCoord = true;
				}
				if (returnedNode.get("floor").equals(correctNode.get("floor"))) {
					floor = true;
				}
				if (returnedNode.get("building").equals(correctNode.get("building"))) {
					building = true;
				}
				if (returnedNode.get("type").equals(correctNode.get("type"))) {
					nodeType = true;
				}
				if (returnedNode.get("longName").equals(correctNode.get("longName"))) {
					longName = true;
				}
				if (returnedNode.get("shortName").equals(correctNode.get("shortName"))) {
					shortName = true;
				}
				if (nodeID && xCoord && yCoord && floor && building && nodeType && longName && !shortName) {
					allCorrect = false;
				}
			}
		} else {
			allCorrect = false;
		}

		assertTrue(allCorrect);
	}

	@Test
	@DisplayName("testGetAllNodesByFloor: no nodes with the given floor")
	public void testGetAllNodesByFloor2() {
		connection.addNode("nodeID1", 0, 0, "1", "Tower", "ELEV", "longName1", "shortName1");
		connection.addNode("nodeID2", 1, 0, "1", "Tower", "ELEV", "longName2", "shortName2");
		connection.addNode("nodeID3", 3, 0, "1", "Tower", "ELEV", "longName3", "shortName3");

		ArrayList<Node> nodes = connection.getAllNodesByFloor("3");

		assertEquals(0, nodes.size());
	}


	@Test
	@DisplayName("testGetAllNodeLongNamesByFloor: 2 nodes with the given floor")
	public void testGetAllNodeLongNamesByFloor() {
		connection.addNode("nodeID1", 0, 0, "1", "Tower", "ELEV", "longName1", "shortName1");
		connection.addNode("nodeID2", 1, 0, "1", "Tower", "ELEV", "longName2", "shortName2");

		ObservableList<String> longNames = connection.getAllNodeLongNamesByFloor("1");

		ObservableList<String> correctLongNames = FXCollections.observableArrayList();

		correctLongNames.add("longName1");
		correctLongNames.add("longName2");

        assertEquals(longNames, correctLongNames);

	}

	@Test
	@DisplayName("testGetAllNodeLongNamesByFloor: no nodes with the given floor")
	public void testGetAllNodeLongNamesByFloor2() {
		connection.addNode("nodeID1", 0, 0, "1", "Tower", "ELEV", "longName1", "shortName1");
		connection.addNode("nodeID2", 1, 0, "1", "Tower", "ELEV", "longName2", "shortName2");
		connection.addNode("nodeID3", 0, 0, "1", "Tower", "ELEV","longName3", "shortName3");

		ObservableList<String> longNames = connection.getAllNodeLongNamesByFloor("3");

		assertEquals(0, longNames.size());
	}

	@Test
	@DisplayName("testGetListOfNodeIDSByFloor: 2 nodes with the given floor")
	public void testGetListOfNodeIDSByFloor() {
		connection.addNode("nodeID1", 0, 0, "1", "Tower", "ELEV", "longName1", "shortName1");
		connection.addNode("nodeID2", 1, 0, "1", "Tower", "ELEV", "longName2", "shortName2");

		ArrayList<String> nodeIDs = connection.getListOfNodeIDSByFloor("1");

		ArrayList<String> correctNodeIDs = new ArrayList<>();

		correctNodeIDs.add("nodeID1");
		correctNodeIDs.add("nodeID2");

		assertEquals(nodeIDs, correctNodeIDs);
	}

	@Test
	@DisplayName("testGetListOfNodeIDSByFloor: no nodes with the given floor")
	public void testGetListOfNodeIDSByFloor2() {
		connection.addNode("nodeID1", 0, 0, "1", "Tower", "ELEV", "longName1", "shortName1");
		connection.addNode("nodeID2", 1, 0, "1", "Tower", "ELEV", "longName2", "shortName2");
		connection.addNode("nodeID3", 3, 0, "1", "Tower", "ELEV", "longName3", "shortName3");

		ArrayList<String> nodeIDs = connection.getListOfNodeIDSByFloor("3");


		assertEquals(0, nodeIDs.size());
	}


	@Test
	@DisplayName("testGetAllEdges")
	public void testGetAllEdges() {
		connection.addNode("test1", 0, 0, "1", "Tower", "ELEV", "test", "test");
		connection.addNode("test2", 2, 2, "1", "Tower", "ELEV", "test", "test");
		connection.addEdge("test1_test2", "test1", "test2");

		ArrayList<Edge> listOfEdges = new ArrayList<>();


		double length1 = Math.pow((-2), 2);
		double length2 = Math.pow((-2), 2);

		double length12 = Math.sqrt((length1 + length2));

		Edge edge1 = new Edge("test1_test2", "test1", "test2", length12);

		listOfEdges.add(edge1);

		ArrayList<Edge> resultListOfEdges = connection.getAllEdges();

		boolean allCorrect = true;
		boolean edgeID = false;
		boolean length = false;
		if (listOfEdges.size() == resultListOfEdges.size()) {
			for (int edge = 0; edge < resultListOfEdges.size(); edge++) {
				Edge returnedEdge = resultListOfEdges.get(edge);
				Edge correctEdge = listOfEdges.get(edge);
				if (returnedEdge.getLength().equals(correctEdge.getLength())) {
					length = true;
				}
				if (returnedEdge.getId().equals(correctEdge.getId())) {
					edgeID = true;
				}
				if (length && !edgeID) {
					allCorrect = false;
				}
			}
		} else {
			allCorrect = false;
		}
		assertTrue(allCorrect);
	}


	@Test
	@DisplayName("testGetAllEdges: there are no edges")
	public void testGetAllEdges2() {
		connection.addNode("test1", 0, 0, "1", "Tower", "ELEV", "test", "test");
		connection.addNode("test2", 2, 2, "1", "Tower", "ELEV", "test", "test");

		ArrayList<Edge> listOfEdges = connection.getAllEdges();

		assertEquals(0, listOfEdges.size());
	}

	@Test
	@DisplayName("testAddNode")
	public void testAddNode() {
		int testResult;

		// if this works, testResult should be 1
		testResult = connection.addNode("testNode", 111, 222, "1", "Tower", "ELEV", "String", "String");

		assertEquals(1, testResult);
	}

	@Test
	@DisplayName("testAddNode: the node added already exists")
	public void testAddNode2() {
		connection.addNode("testNode12", 121, 222, "1", "Tower", "ELEV", "String", "String");

		int testResult = connection.addNode("testNode12", 121, 222, "1", "Tower", "ELEV", "String", "String");

		assertEquals(0, testResult);
	}


	@Test
	@DisplayName("testAddEdge")
	public void testAddEdge() {

		int testResult;

		connection.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		connection.addNode("testEdge2", 12, 15, "1", "Tower", "ELEV", "String", "String");

		// if this works, testResult should be 1
		testResult = connection.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");

		assertEquals(1, testResult);

	}

	@Test
	@DisplayName("testAddEdge: the edgeID already exists")
	public void testAddEdge2() {
		connection.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		connection.addNode("testEdge2", 12, 15, "1", "Tower", "ELEV", "String", "String");

		connection.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");

		int testResult = connection.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");

		assertEquals(0, testResult);
	}

	@Test
	@DisplayName("testAddEdge: the startNode does not exist")
	public void testAddEdge3() {
		connection.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		connection.addNode("testEdge2", 12, 15, "1", "Tower", "ELEV", "String", "String");

		// if this works, testResult should be 1
		int testResult = connection.addEdge("testEdge1_testEdge2", "testEdge3", "testEdge2");

		assertEquals(0, testResult);
	}

	@Test
	@DisplayName("testAddEdge: the endNode does not exist")
	public void testAddEdge4() {
		connection.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		connection.addNode("testEdge2", 12, 15, "1", "Tower", "ELEV", "String", "String");

		// if this works, testResult should be 1
		int testResult = connection.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge3");

		assertEquals(0, testResult);
	}

	@Test
	@DisplayName("testModifyNode")
	public void testModifyNode() {

		int testResult;

		connection.addNode("originalNode", 121, 122, "1", "Tower", "ELEV", "String", "String");

		testResult = connection.modifyNode("originalNode", 100, null, null, null, null, null, null);


		assertEquals(1, testResult);
	}

	@Test
	@DisplayName("testDeleteEdge")
	public void testDeleteEdge() {

		int testResult;

		connection.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		connection.addNode("testEdge2", 12, 15, "1", "Tower", "ELEV", "String", "String");

		connection.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");

		testResult = connection.deleteEdge("testEdge1", "testEdge2");

		System.out.println(testResult);

		assertEquals(0, testResult);
	}

	@Test
	@DisplayName("testDeleteNode")
	public void testDeleteNode() {

		int testResult;

		connection.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		testResult = connection.deleteNode("testEdge1");

		assertEquals(1, testResult);
	}

	@Test
	@DisplayName("testGetListOfNodeIDS")
	public void testGetListOfNodeIDS() {

		connection.addNode("test1", 0, 0, "1", "Tower", "ELEV", "test", "test");
		connection.addNode("test2", 2, 2, "1", "Tower", "ELEV", "test", "test");
		connection.addNode("test3", 3, 3, "1", "Tower", "ELEV", "test", "test");
		connection.addNode("test4", 4, 4, "1", "Tower", "ELEV", "test", "test");

		ArrayList<String> listOfNodeIDs = new ArrayList<>();

		listOfNodeIDs.add("test1");
		listOfNodeIDs.add("test2");
		listOfNodeIDs.add("test3");
		listOfNodeIDs.add("test4");

		assertEquals(listOfNodeIDs, connection.getListOfNodeIDS());
	}


	@Test
	@DisplayName("testGetNewCSVFile")
	public void testGetNewCSVFile() {
		connection.addNode("test1", 0, 0, "1", "Tower", "ELEV", "long", "asd");
		connection.addNode("test2", 2, 2, "1", "Tower", "ELEV", "name", "test");
		connection.addNode("test3", 3, 3, "1", "Tower", "ELEV", "test", "hert");
		connection.addNode("test4", 4, 4, "1", "Tower", "ELEV", "fun", "test");

		connection.getNewCSVFile("node");

		File testFile = new File("src/test/resources/edu/wpi/TeamE/outputNodeTest.csv");
		File nodeFile = new File("src/test/resources/edu/wpi/TeamE/outputNodeDB.csv");
		ArrayList<String> testArray = new ArrayList<>();
		ArrayList<String> nodeArray = new ArrayList<>();

		try {
			FileReader fr = new FileReader(testFile);
			BufferedReader br = new BufferedReader(fr);

			String line;

			while ((line = br.readLine()) != null) {
				testArray.add(line);
			}

			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.println("populateTable() outer try/catch error");
		}

		try {
			FileReader fr = new FileReader(nodeFile);
			BufferedReader br = new BufferedReader(fr);

			String line;

			while ((line = br.readLine()) != null) {
				nodeArray.add(line);
			}

			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.println("populateTable() outer try/catch error");
		}

		assertEquals(nodeArray, testArray);
	}

	@Test
	@DisplayName("Testing countNodeTypeOnFloor()")
	public void testCountNodeTypeOnFloor() {
		connection.addNode("test1", 534, 0, "1", "Tower", "INFO", "long", "asd");
		connection.addNode("Test2", 54, 2, "1", "Tower", "ELEV", "name", "test");
		connection.addNode("test3", 43, 3, "1", "Tower", "ELEV", "test", "hert");
		connection.addNode("test4", 544, 4, "1", "Tower", "ELEV", "fun", "test");
		connection.addNode("Test5", 4350, 0, "1", "Tower", "ELEV", "long", "asd");
		connection.addNode("test6", 5342, 2, "1", "Tower", "ELEV", "name", "test");
		connection.addNode("test7", 433, 3, "1", "Tower", "ELEV", "test", "hert");
		connection.addNode("test8", 344, 4, "1", "Tower", "ELEV", "fun", "test");
		connection.addNode("test9", 430, 0, "1", "Tower", "ELEV", "long", "asd");
		connection.addNode("test10", 432, 2, "1", "Tower", "ELEV", "name", "test");

		int returned = connection.countNodeTypeOnFloor("t", "1", "INFO");
		assertEquals(1, returned);
		int returned2 = connection.countNodeTypeOnFloor("t", "1", "PARK");
		assertEquals(0, returned2);
	}


	@Test
	@DisplayName("testCreateUserAccountTable")
	public void testCreateUserAccountTable(){

	}

	@Test
	@DisplayName("testAddSanitationRequest")
	public void testAddSanitationRequest(){
		connection.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");

		connection.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");

		connection.addSanitationRequest(1, "test", "Urine Cleanup", "description here", "Low", "Nupur Shukla");
		connection.addExternalPatientRequest(2, "BW", "severe", "123", "15 mins", "headache");
	}

	@Test
	@DisplayName("testAddSpecialUserType")
	public void testAddSpecialUserType(){
		connection.addSpecialUserType("test1@gmail.com", "testPassword", "patient", "patientFirstName", "patientLastName");
		connection.addSpecialUserType("test2@gmail.com", "testPassword1", "admin", "adminFirstName", "adminLastName");
		connection.addSpecialUserType("test3@gmail.com", "testPassword2", "doctor", "doctorFirstName", "doctorLastName");

	}


}
