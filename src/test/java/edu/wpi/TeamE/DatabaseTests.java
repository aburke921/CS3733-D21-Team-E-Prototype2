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
	public void setupTables() {

		try {
			connection.deleteAllTables(/*connection.getTablesToThatExist()*/);
			System.out.println("Tables were reset");
		} catch (Exception e) {
			//e.printStackTrace();
		}
		connection.createNodeTable();
		connection.createEdgeTable();
		connection.createUserAccountTable();
		connection.createRequestsTable();
		connection.createFloralRequestsTable();
		connection.createSanitationTable();
		connection.createExtTransportTable();
		connection.createMedDeliveryTable();
		connection.createSecurityServTable();
		System.out.println("Tables were created");
	}

	@Test
	public void testing() {

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
		connection.addNode("nodeID3", 0, 0, "1", "Tower", "ELEV", "longName3", "shortName3");

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
	public void testCreateUserAccountTable() {

	}

	@Test
	@DisplayName("testAddSanitationRequest")
	public void testAddSanitationRequest() {
		connection.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");

		connection.addSanitationRequest(1, "bob","test", "Urine Cleanup", "description here", "Low", "Nupur Shukla");
	}

	@Test
	@DisplayName("testAddExternalPatientRequest")
	public void testAddExternalPatientRequest() {
		connection.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");

		connection.addExternalPatientRequest(1, "bob", "test", "Ambulance","severe", "123", "15 mins", "headache");
	}

	@Test
	@DisplayName("testAddMedicineRequest")
	public void testAddMedicineRequest() {
		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(1, "bob","test", "drugs", 2, "100ml", "take once a day", "Nupur");
	}

	@Test
	@DisplayName("testSecurityRequest")
	public void testAddSecurityRequest() {
		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");

		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");

		connection.addSecurityRequest(1, "bob", "test", "low", "Low");
	}

	@Test
	@DisplayName("testAddFloralRequest")
	public void testAddFloralRequest() {
		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");

		connection.addFloralRequest(1,"bob", "test", "Nupur", "Roses", 1, "Tall", "feel better");

	}

	@Test
	@DisplayName("testAddSpecialUserType")
	public void testAddSpecialUserType() {
		connection.addSpecialUserType("test1@gmail.com", "testPassword", "patient", "patientFirstName", "patientLastName");
		connection.addSpecialUserType("test2@gmail.com", "testPassword1", "admin", "adminFirstName", "adminLastName");
		connection.addSpecialUserType("test3@gmail.com", "testPassword2", "doctor", "doctorFirstName", "doctorLastName");

	}


	@Test
	@DisplayName("testGetRequestStatus")
	public void testGetRequestStatus() {
		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addExternalPatientRequest(1, "bob","test", "Ambulance", "severe", "123", "15 mins", "headache");
		connection.addExternalPatientRequest(1, "bob","test", "Ambulance", "severe", "123", "15 mins", "migraine");
		connection.addExternalPatientRequest(2, "bob","test", "Ambulance", "severe", "123", "15 mins", "migraine");
		connection.addFloralRequest(1, "bob","test", "Nupur", "Roses", 1, "Tall", "feel better");

		ArrayList<String> returnedStatus = new ArrayList<String>();
		ArrayList<String> correctStatus = new ArrayList<String>();

		correctStatus.add("inProgress");
		correctStatus.add("inProgress");

		returnedStatus = connection.getRequestStatus("extTransport", 1);

		assertTrue(correctStatus.equals(returnedStatus));
	}

	@Test
	@DisplayName("testGetRequestStatus2")
	public void testGetRequestStatus2() {
		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addExternalPatientRequest(1, "bob","test", "Ambulance", "severe", "123", "15 mins", "headache");
		connection.addExternalPatientRequest(1, "bob","test", "Ambulance", "severe", "123", "15 mins", "migraine");
		connection.addExternalPatientRequest(2, "bob","test", "Ambulance", "severe", "123", "15 mins", "migraine");
		connection.addFloralRequest(1, "bob","test", "Nupur", "Roses", 1, "Tall", "feel better");

		ArrayList<String> returnedStatus = new ArrayList<String>();
		ArrayList<String> correctStatus = new ArrayList<String>();

		correctStatus.add("inProgress");
		correctStatus.add("inProgress");
		correctStatus.add("inProgress");

		returnedStatus = connection.getRequestStatus("extTransport", -1);

		assertTrue(correctStatus.equals(returnedStatus));
	}

	@Test
	@DisplayName("testGetRequestStatus3 : there is no data ")
	public void testGetRequestStatu3() {

		ArrayList<String> returnedStatus = new ArrayList<String>();
		returnedStatus = connection.getRequestStatus("extTransport", 1);

		assertTrue(returnedStatus.size() == 0);
	}


	@Test
	@DisplayName("testGetRequestIDs")
	public void testGetRequestIDs() {

		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addExternalPatientRequest(1, "bob","test", "Ambulance", "severe", "123", "15 mins", "headache");
		connection.addExternalPatientRequest(2, "bob","test", "Ambulance", "severe", "123", "15 mins", "migraine");
		connection.addFloralRequest(1, "bob","test", "Nupur", "Roses", 1, "Tall", "feel better");

		ArrayList<String> returnedIDs = new ArrayList<String>();
		ArrayList<String> correctIDs = new ArrayList<String>();

		correctIDs.add("1");

		returnedIDs = connection.getRequestIDs("extTransport", 1);

		assertTrue(correctIDs.equals(returnedIDs));
	}

	@Test
	@DisplayName("testGetRequestIDs2")
	public void testGetRequestIDs2() {

		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addExternalPatientRequest(1, "bob","test", "Ambulance", "severe", "123", "15 mins", "headache");
		connection.addExternalPatientRequest(2, "bob","test", "Ambulance", "severe", "123", "15 mins", "migraine");
		connection.addFloralRequest(1, "bob","test", "Nupur", "Roses", 1, "Tall", "feel better");

		ArrayList<String> returnedIDs = new ArrayList<String>();
		ArrayList<String> correctIDs = new ArrayList<String>();

		correctIDs.add("1");
		correctIDs.add("2");

		returnedIDs = connection.getRequestIDs("extTransport", -1);

		assertTrue(correctIDs.equals(returnedIDs));
	}

	@Test
	@DisplayName("testGetRequestIDs3: there is no data")
	public void testGetRequestIDs3() {

		ArrayList<String> returnedIDs = new ArrayList<String>();
		returnedIDs = connection.getRequestIDs("extTransport", -1);

		assertTrue(returnedIDs.size() == 0);
	}



	@Test
	@DisplayName("testGetRequestAssignees")
	public void testGetRequestAssignees(){

		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "long name #1", "shortName");
		connection.addNode("test3", 0, 0, "L1", "Tower", "INFO", "long name #2", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(1, "bob","test", "drugs", 2, "100ml", "take once a day", "Nupur");
		connection.addMedicineRequest(1, "kim","test3", "drugs2", 3, "10ml", "take once a day", "Nupur");

		connection.addNode("test2", 0, 0, "3", "Tower", "INFO", "long name #3", "shortName");
		connection.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(2, "dell","test2", "drugs2", 3, "10ml", "take once a day", "Nupur");

		ArrayList<String> returnedAssignees = connection.getRequestAssignees("medDelivery", 1);
		ArrayList<String> correctAssignees = new ArrayList<String>();

		correctAssignees.add("bob");
		correctAssignees.add("kim");

		assertTrue(correctAssignees.equals(returnedAssignees));
	}

	@Test
	@DisplayName("testGetRequestAssignees2")
	public void testGetRequestAssignees2(){

		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "long name #1", "shortName");
		connection.addNode("test3", 0, 0, "L1", "Tower", "INFO", "long name #2", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(1, "bob","test", "drugs", 2, "100ml", "take once a day", "Nupur");
		connection.addMedicineRequest(1, "kim","test3", "drugs2", 3, "10ml", "take once a day", "Nupur");

		connection.addNode("test2", 0, 0, "3", "Tower", "INFO", "long name #3", "shortName");
		connection.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(2, "dell","test2", "drugs2", 3, "10ml", "take once a day", "Nupur");

		//"floral", "medDelivery", "sanitation", "security", "extTransport".

		ArrayList<String> returnedAssignees = connection.getRequestAssignees("medDelivery", -1);
		ArrayList<String> correctAssignees = new ArrayList<String>();

		correctAssignees.add("bob");
		correctAssignees.add("kim");
		correctAssignees.add("dell");

		assertTrue(correctAssignees.equals(returnedAssignees));
	}

	@Test
	@DisplayName("testGetRequestAssignees3: no data")
	public void testGetRequestAssignees3(){
		ArrayList<String> returnedAssignees = connection.getRequestAssignees("medDelivery", -1);
		assertTrue(returnedAssignees.size() == 0);
	}



	@Test
	@DisplayName("testGetRequestLocations")
	public void testGetRequestLocations(){

		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "long name #1", "shortName");
		connection.addNode("test3", 0, 0, "L1", "Tower", "INFO", "long name #2", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(1, "bob","test", "drugs", 2, "100ml", "take once a day", "Nupur");
		connection.addMedicineRequest(1, "kim","test3", "drugs2", 3, "10ml", "take once a day", "Nupur");

		connection.addNode("test2", 0, 0, "3", "Tower", "INFO", "long name #3", "shortName");
		connection.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(2, "kim","test2", "drugs2", 3, "10ml", "take once a day", "Nupur");

		ArrayList<String> returnedLocations = connection.getRequestLocations("medDelivery", 1);
		ArrayList<String> correctLocations = new ArrayList<String>();

		correctLocations.add("long name #1");
		correctLocations.add("long name #2");

		for(String e: returnedLocations)
			System.out.println(e);

		assertTrue(correctLocations.equals(returnedLocations));

	}

	@Test
	@DisplayName("testGetRequestLocations2")
	public void testGetRequestLocations2(){

		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "long name #1", "shortName");
		connection.addNode("test3", 0, 0, "L1", "Tower", "INFO", "long name #2", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(1, "bob","test", "drugs", 2, "100ml", "take once a day", "Nupur");
		connection.addMedicineRequest(1, "kim","test3", "drugs2", 3, "10ml", "take once a day", "Nupur");

		connection.addNode("test2", 0, 0, "3", "Tower", "INFO", "long name #3", "shortName");
		connection.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(2, "kim","test2", "drugs2", 3, "10ml", "take once a day", "Nupur");

		ArrayList<String> returnedLocations = connection.getRequestLocations("medDelivery", -1);
		ArrayList<String> correctLocations = new ArrayList<String>();

		correctLocations.add("long name #1");
		correctLocations.add("long name #2");
		correctLocations.add("long name #3");

		for(String e: returnedLocations)
			System.out.println(e);

		assertTrue(correctLocations.equals(returnedLocations));

	}

	@Test
	@DisplayName("testGetRequestLocations3 : no data")
	public void testGetRequestLocations3(){

		ArrayList<String> returnedLocations = connection.getRequestLocations("medDelivery", -1);
		assertTrue(returnedLocations.size() == 0);

	}


	@Test
	@DisplayName("testEditSanitationRequest")
	public void testEditSanitationRequest(){

		connection.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");

		connection.addSanitationRequest(1, "bob","test", "Urine Cleanup", "description here", "Low", "Nupur Shukla");

		int result = 0;
		result = connection.editSanitationRequest(1, "test", null, null, null, "hello test");

		assertTrue(result == 1);
	}

	@Test
	@DisplayName("testEditExternalPatientRequest")
	public void testEditExternalPatientRequest(){

		connection.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");

		connection.addExternalPatientRequest(1, "bob", "test", "Ambulance","severe", "123", "15 mins", "headache");

		int result = 0;
		result = connection.editExternalPatientRequest(1, "test", null, null, null, null, "15 mins");

		assertTrue(result == 1);
	}

	@Test
	@DisplayName("testEditMedicineRequest")
	public void testEditMedicineRequest(){

		connection.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");

		connection.addMedicineRequest(1, "bob","test", "drugs", 2, "100ml", "take once a day", "Nupur");

		int result = 0;
		result = connection.editMedicineRequest(1,"test", "Tylenol", null, null, "Take twice everyday", null);

		assertTrue(result == 1);
	}

	@Test
	@DisplayName("testEditFloralRequest")
	public void testEditFloralRequest(){

		connection.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");

		connection.addFloralRequest(1,"bob", "test", "Nupur", "Roses", 1, "Tall", "feel better");

		int result = 0;
		result = connection.editFloralRequest(1,"test", "Ashley", "Tulips", null, null, null);

		assertTrue(result == 1);
	}

	@Test
	@DisplayName("testEditSecurityRequest")
	public void testEditSecurityRequest(){

		connection.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");

		connection.addSecurityRequest(1, "bob", "test", "low", "Low");

		int result = 0;
		result = connection.editSecurityRequest(1,null, "high", "High");

		assertTrue(result == 1);
	}

	@Test
	@DisplayName("testChangeRequestStatus")
	public void testChangeRequestStatus(){

		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(1, "bob","test", "drugs", 2, "100ml", "take once a day", "Nupur");
		connection.addMedicineRequest(1, "bob1","test", "drugs2", 3, "10ml", "take once a day", "Nupur");

		ArrayList<String> IDS = connection.getRequestIDs("medDelivery", 1);

		int rowsChanged = connection.changeRequestStatus(1, "complete");

		assertEquals(1, rowsChanged);
	}

	@Test
	@DisplayName("testDataForPresentation")
	public void testDataForPresentation(){

		//Floral Delivery Nodes:
		connection.addNode("ADEPT00101",1401,2628,"1","BTM","DEPT","Neuroscience Waiting Room","Neuro Waiting Room");
		connection.addNode("ADEPT00102",1395,2674,"2","BTM","DEPT","Orthopedics and Rhemutalogy","Orthopedics and Rhemutalogy");
		connection.addNode("ADEPT00201",1720,2847,"1","BTM","DEPT","MS Waiting","MS Waiting");
		connection.addNode("ADEPT00301",986,2852,"1","BTM","DEPT","CART Waiting","CART Waiting");
		connection.addNode("DDEPT00102",4330,700,"2","15 Francis","DEPT","Chest Diseases Floor 2","Chest Diseases");


		//Sanitation Nodes:
		connection.addNode("AREST00101",1556,2604,"1","BTM","REST","Restroom S elevator 1st floor","Restroom");
		connection.addNode("AREST00103",1552,2854,"3","BTM","REST","Restroom BTM conference center 3rd floor","Restroom");
		connection.addNode("ARETL00101",1619,2522,"1","BTM","RETL","Cafe","Cafe");
		connection.addNode("IREST00103",2255,1255,"3","45 Francis","REST","Restroom 1 - Family","R1");
		connection.addNode("IREST00203",2570,1257,"3","45 Francis","REST","Restroom 2","R2");
		connection.addNode("IREST00303",2745,1147,"3","45 Francis","REST","Restroom 3","R3");
		connection.addNode("IREST00403",2300,1018,"3","45 Francis","REST","Restroom 4 - M wheelchair","R4");
		connection.addNode("HRETL00102",1935,860,"2","Tower","RETL","Garden Cafe","Garden Cafe");


		//med delivery Nodes:
		connection.addNode("BLABS00102",2246,1350,"2","45 Francis","LABS","Vascular Diagnostic Lab","Labs B0102");
		connection.addNode("BLABS00202",2945,995,"2","45 Francis","LABS","Outpatient Specimen Collection","Labs B0202");
		connection.addNode("IDEPT00103",2323,1328,"3","45 Francis","DEPT","Center for Infertility and Reproductive Surgery","D1");
		connection.addNode("IDEPT00203",2448,1328,"3","45 Francis","DEPT","Gynecology Oncology MIGS","D2");
		connection.addNode("IDEPT00303",2730,1315,"3","45 Francis","DEPT","General Surgical Specialties Suite A","D3");
		connection.addNode("IDEPT00403",2738,1227,"3","45 Francis","DEPT","General Surgical Specialties Suite B","D4");
		connection.addNode("IDEPT00503",2868,1075,"3","45 Francis","DEPT","Urology","D5");
		connection.addNode("IDEPT00603",2333,764,"3","45 Francis","DEPT","Maternal Fetal Practice","D6");
		connection.addNode("IDEPT00703",2400,764,"3","45 Francis","DEPT","Obstetrics","D7");
		connection.addNode("IDEPT00803",2492,887,"3","45 Francis","DEPT","Fetal Med & Genetics","D8");
		connection.addNode("IDEPT00903",2631,851,"3","45 Francis","DEPT","Gynecology","D9");


		//Security Nodes:
		connection.addNode("HDEPT00203",1690,830,"3","Tower","DEPT","MICU 3BC Waiting Room","MICU 3BC WR");
		connection.addNode("WELEV00E01",3265,830,"1","45 Francis","ELEV","Elevator E Floor 1","Elevator E1");
		connection.addNode("ePARK00101",381,1725,"1","Parking","PARK","Left Parking Lot Spot 001","Parking Left 001");
		connection.addNode("ePARK00201",406,1725,"1","Parking","PARK","Left Parking Lot Spot 002","Parking Left 002");
		connection.addNode("eWALK00701",1730,1544,"1","Parking","WALK","Entrance Sidewalk","Walkway");
		connection.addNode("BDEPT00302",2385,753,"2","45 Francis","DEPT","Lee Bell Breast Center","DEPT B0302");
		connection.addNode("BDEPT00402",2439,902,"2","45 Francis","DEPT","Jen Center for Primary Care","DEPT B0402");
		connection.addNode("CCONF002L1",2665,1043,"L1","45 Francis","CONF","Medical Records Conference Room Floor L1","Conf C002L1");

		//External transport:
		connection.addNode("FDEPT00501",2128,1300,"1","Tower","DEPT","Emergency Department","Emergency");
		connection.addNode("EEXIT00101",2275,785,"1","45 Francis","EXIT","Ambulance Parking Exit Floor 1","AmbExit 1");

		connection.addDataForPresentation();
	}

//	@Test
//	@DisplayName("testGetTablesToThatExist")
//	public void testGetTablesToThatExist(){
////		try {
////			connection.deleteAllTables();
////			connection.createNodeTable();
////		} catch (Exception e) {
////			connection.createNodeTable();
////		}
//		ArrayList<String> names = connection.getTablesToThatExist();
//
//		for(String name : names){
//			System.out.println(name);
//		}
//		assertTrue(true);
//
//	}

}