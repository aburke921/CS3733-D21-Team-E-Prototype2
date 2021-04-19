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
			connection.deleteAllTables();
			connection.createNodeTable();
			connection.createEdgeTable();
			connection.createUserAccountTable();
			connection.createRequestsTable();
			connection.createFloralRequestsTable();
			connection.createSanitationTable();
			connection.createExtTransportTable();
			connection.createMedDeliveryTable();
			connection.createSecurityServTable();
			System.out.println("Tables were reset");
		} catch (Exception e) {
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

		connection.addExternalPatientRequest(1, "bob", "BW", "Ambulance","severe", 123, "15 mins", "headache");
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
		connection.addExternalPatientRequest(1, "bob","BW", "Ambulance", "severe", 123, "15 mins", "headache");
		connection.addExternalPatientRequest(1, "bob","BW", "Ambulance", "severe", 123, "15 mins", "migraine");
		connection.addFloralRequest(1, "bob","test", "Nupur", "Roses", 1, "Tall", "feel better");

		ArrayList<String> returnedStatus = new ArrayList<String>();
		ArrayList<String> correctStatus = new ArrayList<String>();

		correctStatus.add("inProgress");
		correctStatus.add("inProgress");

		returnedStatus = connection.getRequestStatus("extTransport");

		assertTrue(correctStatus.equals(returnedStatus));
	}


	@Test
	@DisplayName("testGetRequestIDs")
	public void testGetRequestIDs() {

		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addExternalPatientRequest(1, "bob","BW", "Ambulance", "severe", 123, "15 mins", "headache");
		connection.addExternalPatientRequest(1, "bob","BW", "Ambulance", "severe", 123, "15 mins", "migraine");
		connection.addFloralRequest(1, "bob","test", "Nupur", "Roses", 1, "Tall", "feel better");

		ArrayList<String> returnedIDs = new ArrayList<String>();
		ArrayList<String> correctIDs = new ArrayList<String>();

		correctIDs.add("1");
		correctIDs.add("2");

		returnedIDs = connection.getRequestIDs("extTransport");

		assertTrue(correctIDs.equals(returnedIDs));
	}

	@Test
	@DisplayName("testChangeRequestStatus")
	public void testChangeRequestStatus(){

		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(1, "bob","test", "drugs", 2, "100ml", "take once a day", "Nupur");
		connection.addMedicineRequest(1, "bob1","test", "drugs2", 3, "10ml", "take once a day", "Nupur");

		ArrayList<String> IDS = connection.getRequestIDs("medDelivery");

		int rowsChanged = connection.changeRequestStatus(1, "complete");

		assertEquals(1, rowsChanged);
	}


	@Test
	@DisplayName("testGetRequestAssignees")
	public void testGetRequestAssignees(){

		connection.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addMedicineRequest(1, "bob","test", "drugs", 2, "100ml", "take once a day", "Nupur");
		connection.addMedicineRequest(1, "kim","test", "drugs2", 3, "10ml", "take once a day", "Nupur");

		connection.addNode("test2", 0, 0, "3", "Tower", "INFO", "longName", "shortName");
		connection.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		connection.addSecurityRequest(1, "drew", "test", "low", "Low");

		ArrayList<String> returnedAssignees = connection.getRequestAssignees("medDelivery");
		ArrayList<String> correctAssignees = new ArrayList<String>();

		correctAssignees.add("bob");
		correctAssignees.add("kim");

		assertTrue(correctAssignees.equals(returnedAssignees));
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
		connection.addSecurityRequest(1, "drew", "test", "low", "Low");

		ArrayList<String> returnedLocations = connection.getRequestLocations("medDelivery");
		ArrayList<String> correctLocations = new ArrayList<String>();

		correctLocations.add("long name #1");
		correctLocations.add("long name #2");

		assertTrue(correctLocations.equals(returnedLocations));

	}




	@Test
	@DisplayName("data")
	public void data(){

		//Visitors:
		// - have access to floral Delivery
		connection.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		connection.addUserAccount("terry_reilly123@yahoo.com", "visitor2", "Terry", "Reilly");
		connection.addUserAccount("smiddle@outlook.com", "visitor3", "Sharon", "Middleton");
		connection.addUserAccount("catherinehop12@gmail.com", "visitor4", "Catherine", "Hopkins");
		connection.addUserAccount("mbernard@wpi.edu", "visitor5", "Michelle", "Bernard");
		connection.addUserAccount("mccoy.meghan@hotmail.com", "visitor6", "Meghan", "Mccoy");
		connection.addUserAccount("harry89owens@gmail.com", "visitor7", "Harry", "Owens");
		connection.addUserAccount("hugowh@gmail.com", "visitor8", "Hugo", "Whitehouse");
		connection.addUserAccount("spenrodg@yahoo.com", "visitor9", "Spencer", "Rodgers");
		connection.addUserAccount("thomasemail@gmail.com", "visitor10", "Thomas", "Mendez");
		connection.addUserAccount("claytonmurray@gmail.com", "visitor11", "Clayton", "Murray");
		connection.addUserAccount("lawrencekhalid@yahoo.com", "visitor12", "Khalid", "Lawrence");

		//Patients:
		//13 - 19
		connection.addSpecialUserType("adamj@gmail.com","patient1","patient","Adam", "Jenkins");
		connection.addSpecialUserType("abbym@yahoo.com","patient2","patient","Abby", "Mohamed");
		connection.addSpecialUserType("wesleya@gmail.com","patient3","patient","Wesley", "Armstrong");
		connection.addSpecialUserType("travisc@yahoo.com","patient4","patient","Travis", "Cook");
		connection.addSpecialUserType("gabriellar@gmail.com","patient5","patient","Gabriella", "Reyes");
		connection.addSpecialUserType("troyo@yahoo.com","patient6","patient","Troy", "Olson");
		connection.addSpecialUserType("anat@gmail.com","patient7","patient","Ana", "Turner");

		//Doctors:
		//20-27
		connection.addSpecialUserType("billb@gmail.com","doctor1","doctor","Bill", "Byrd");
		connection.addSpecialUserType("ameliak@yahoo.com","doctor2","doctor","Amelia", "Knight");
		connection.addSpecialUserType("simond@gmail.com","doctor3","doctor","Simon", "Daniel");
		connection.addSpecialUserType("victoriae@yahoo.com","doctor4","doctor","Victoria", "Erickson");
		connection.addSpecialUserType("taylorr@gmail.com","doctor5","doctor","Taylor", "Ramos");
		connection.addSpecialUserType("rosas@yahoo.com","doctor6","doctor","Rosa", "Smith");
		connection.addSpecialUserType("declanp@gmail.com","doctor7","doctor","Declan", "Patel");
		connection.addSpecialUserType("laurenb@yahoo.com","doctor8","doctor","Lauren", "Bolton");

		//Admin:
		//28 - 30
		connection.addSpecialUserType("abbyw@gmail.com","admin1","admin","Abby", "Williams");
		connection.addSpecialUserType("andrewg@yahoo.com","admin2","admin","Andrew", "Guerrero");
		connection.addSpecialUserType("aleshah@gmail.com","admin3","admin","Alesha", "Harris");






		//Floral Delivery Nodes:
		connection.addNode("ADEPT00101",1401,2628,"1","BTM","DEPT","Neuroscience Waiting Room","Neuro Waiting Room");
		connection.addNode("ADEPT00102",1395,2674,"2","BTM","DEPT","Orthopedics and Rhemutalogy","Orthopedics and Rhemutalogy");
		connection.addNode("ADEPT00201",1720,2847,"1","BTM","DEPT","MS Waiting","MS Waiting");
		connection.addNode("ADEPT00301",986,2852,"1","BTM","DEPT","CART Waiting","CART Waiting");
		connection.addNode("DDEPT00102",4330,700,"2","15 Francis","DEPT","Chest Diseases Floor 2","Chest Diseases");

		//Floral Requests:
		connection.addFloralRequest(13,"Amy Castaneda", "ADEPT00101", "Adam", "Roses", 1, "None", "Hi Adam, I am so sorry to hear about your accident. Please get better soon!");
		connection.addFloralRequest(13,"Elsa Figueroa", "ADEPT00102", "Abraham", "Tulips", 6, "Round", "Dear Abraham, hope these flowers help you feel better. The team really misses you and hope you will be ready to go by the championship");
		connection.addFloralRequest(14,"Caroline Sutton", "ADEPT00102", "Xavier", "Carnations", 12, "Square", "Get well soon");
		connection.addFloralRequest(15,"Miles Long", "ADEPT00301", "Nikki", "Assortment", 1, "None", "");
		connection.addFloralRequest(15,"Hasan Perry", "ADEPT00101", "Monica", "Roses", 6, "Tall", "Love and miss you!!");
		connection.addFloralRequest(17,"Caroline Richardson", "DDEPT00102", "Amy", "Tulips", 12, "Square", "Enjoy the flowers");
		connection.addFloralRequest(17,"Miles Carroll", "ADEPT00102", "Alfred", "Carnations", 1, "Tall", "Miss you!");
		connection.addFloralRequest(19,"Seth Warner", "ADEPT00101", "Caroline", "Assortment", 6, "Round", "Sorry I forgot to warn you about the slippery stairs, I hope these flowers can make you feel better!");
		connection.addFloralRequest(19,"Darren Rossi", "ADEPT00301", "Carrie", "Assortment", 12, "Round", "");

		//Sanitation Nodes:
		connection.addNode("AREST00101",1556,2604,"1","BTM","REST","Restroom S elevator 1st floor","Restroom");
		connection.addNode("AREST00103",1552,2854,"3","BTM","REST","Restroom BTM conference center 3rd floor","Restroom");
		connection.addNode("ARETL00101",1619,2522,"1","BTM","RETL","Cafe","Cafe");
		connection.addNode("IREST00103",2255,1255,"3","45 Francis","REST","Restroom 1 - Family","R1");
		connection.addNode("IREST00203",2570,1257,"3","45 Francis","REST","Restroom 2","R2");
		connection.addNode("IREST00303",2745,1147,"3","45 Francis","REST","Restroom 3","R3");
		connection.addNode("IREST00403",2300,1018,"3","45 Francis","REST","Restroom 4 - M wheelchair","R4");
		connection.addNode("HRETL00102",1935,860,"2","Tower","RETL","Garden Cafe","Garden Cafe");

		//Sanitation Requests:
		connection.addSanitationRequest(20,"Crystal Harvey", "AREST00101", "Urine Cleanup", "Restroom with urine on the floor", "Medium", "Bill Byrd");
		connection.addSanitationRequest(20,"Minnie Newman", "AREST00103", "Urine Cleanup", "Restroom with urine on the toilet seet", "Medium", "Bill Byrd");
		connection.addSanitationRequest(24,"Ayla Black", "AREST00103", "Feces Cleanup", "Feces smeared on toilet seats", "High", "Taylor Ramos");
		connection.addSanitationRequest(25,"Lenard Jacobs", "ARETL00101", "Trash Removal", "Trash can full, starting to smell", "Medium", "Rosa Smith");
		connection.addSanitationRequest(28,"Juan Williams", "IREST00103", "Feces Cleanup", "Just outside of the bathroom there is a pile of feces. Someone did not make it in time.", "Critical", "Abby Williams");
		connection.addSanitationRequest(30,"May Jimenez", "IREST00203", "Trash Removal", "Trash can smells bad", "Medium", "Alesha Harris");
		connection.addSanitationRequest(29,"Herman Bull", "IREST00303", "Trash Removal", "Trash can full. Another one is available so don't rush.", "Low", "Andrew Guerrero");
		connection.addSanitationRequest(22,"Umar Rojas", "HRETL00102", "Urine Cleanup", "Liquid on the floor. Unclear if it is urine. Not a whole lot of it.", "Low", "Simon Daniel");
		connection.addSanitationRequest(23,"Reuben", "IREST00403", "Trash Removal", "", "Low", "Victoria Erickson");


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

		//Medicine Delivery Request
		connection.addMedicineRequest(20, "Clara Bryan", "BLABS00102", "Atorvastatin", 30, "20mg", "Once a day by mouth", "Bill Byrd");
		connection.addMedicineRequest(20, "Jennifer Cunningham", "BLABS00202", "Lisinopril", 90, "20mg", "Once a day by mouth", "Bill Byrd");
		connection.addMedicineRequest(21, "Jak Bishop", "IDEPT00103", "Levothyroxine", 90, "12.5mcg", "Once a day my bouth", "Amelia Knight");
		connection.addMedicineRequest(24, "Ben Coles", "BLABS00102", "Metformin", 30, "850mg", "Twice a day by mouth", "Taylor Ramos");
		connection.addMedicineRequest(27, "Gloria Webster", "IDEPT00803", "Amlodipine", 30, "5mg", "Once a day by mouth", "Lauren Bolton");
		connection.addMedicineRequest(26, "Robbie Turner", "IDEPT00603", "Metoprolol", 90, "400mg", "Once a day by mouth", "Declan Patel");
		connection.addMedicineRequest(23, "Lucas Whittaker", "IDEPT00403", "Omeprazole", 90, "40mg", "Three times a day by mouth before a meal", "Victoria Erickson");
		connection.addMedicineRequest(24, "Alec Rees", "IDEPT00703", "Simvastatin", 30, "10mg", "Once a day by mouth", "Taylor Ramos");
		connection.addMedicineRequest(27, "Francesca Ferguson", "IDEPT00903", "Losartan", 90, "100mg", "Once daily by mouth", "Lauren Bolton");
		connection.addMedicineRequest(21, "Josie Pittman", "IDEPT00203", "Albuterol", 30, "0.63mg", "3 times a day via nebulizer. 4 times a day if needed.", "Amelia Knight");
		connection.addMedicineRequest(20, "Will Ford", "BLABS00202", "Metformin", 30, "8.5mL", "Once daily with meals.", "Bill Byrd");
		connection.addMedicineRequest(23, "Billy Gomez", "BLABS00102", "Metformin", 30, "5mL", "Twice a day with meals.", "Victoria Erickson");

		//Security Nodes:
		connection.addNode("HDEPT00203",1690,830,"3","Tower","DEPT","MICU 3BC Waiting Room","MICU 3BC WR");
		connection.addNode("WELEV00E01",3265,830,"1","45 Francis","ELEV","Elevator E Floor 1","Elevator E1");
		connection.addNode("ePARK00101",381,1725,"1","Parking","PARK","Left Parking Lot Spot 001","Parking Left 001");
		connection.addNode("ePARK00201",406,1725,"1","Parking","PARK","Left Parking Lot Spot 002","Parking Left 002");
		connection.addNode("eWALK00701",1730,1544,"1","Parking","WALK","Entrance Sidewalk","Walkway");
		connection.addNode("BDEPT00302",2385,753,"2","45 Francis","DEPT","Lee Bell Breast Center","DEPT B0302");
		connection.addNode("BDEPT00402",2439,902,"2","45 Francis","DEPT","Jen Center for Primary Care","DEPT B0402");
		connection.addNode("CCONF002L1",2665,1043,"L1","45 Francis","CONF","Medical Records Conference Room Floor L1","Conf C002L1");


		//Security Requests:
		connection.addSecurityRequest(20, "James O'Moore","HDEPT00203", "Low", "Low");
		connection.addSecurityRequest(22, "Russell Armstrong","WELEV00E01", "Medium", "Medium");
		connection.addSecurityRequest(30, "Lillian Peters","HDEPT00203", "Low", "Low");
		connection.addSecurityRequest(27, "Clara Dixon","ePARK00101", "Medium", "High");
		connection.addSecurityRequest(24, "Herbert Ortega","BDEPT00402", "Medium", "Medium");
		connection.addSecurityRequest(20, "Caleb Carr","BDEPT00302", "Low", "Low");
		connection.addSecurityRequest(25, "Jasper Miller","CCONF002L1", "High", "Critical");
		connection.addSecurityRequest(29, "Jennifer Brewer","eWALK00701", "Medium", "Medium");

//		'Ambulance', 'Helicopter', 'Plane'
		//'High Severity', 'Medium Severity', 'Low Severity'
		//20-30
		connection.addExternalPatientRequest(27,"Ciaran Goodwin","Brigham & Women's Hospital - Boston MA", "Ambulance", "High Severity", 12334567, "5 minutes", "Patient dropped down into a state of unconsciousness randomly at the store. Patient is still unconscious and unresponsive but has a pulse. No friends or family around during the incident. ");
		connection.addExternalPatientRequest(30,"Lola Bond","Brigham & Women's Hospital - Boston MA", "Ambulance","Low Severity", 4093380, "20 minutes", "Patient coming in with cut on right hand. Needs stitches. Bleeding is stable.");
		connection.addExternalPatientRequest(22,"Samantha Russell","Brigham & Women's Hospital - Boston MA", "Helicopter","High Severity", 92017693, "10 minutes", "Car crash on the highway. 7 year old child in the backseat with no seatbelt on in critical condition. Blood pressure is low and has major trauma to the head.");
		connection.addExternalPatientRequest(20,"Caleb Chapman","Brigham & Women's Hospital - Boston MA", "Helicopter","High Severity", 93754789, "20 minutes", "Skier hit tree and lost consciousness. Has been unconscious for 30 minutes. Still has a pulse.");
		connection.addExternalPatientRequest(24,"Dale Coates","Brigham & Women's Hospital - Boston MA", "Ambulance","Medium Severity", 417592, "10 minutes", "Smoke inhalation due to a fire. No burns but difficult time breathing.");
		connection.addExternalPatientRequest(28,"Jerry Myers","Brigham & Women's Hospital - Boston MA", "Helicopter", "High Severity", 44888936, "15 minutes", "Major car crash on highway. Middle aged woman ejected from the passenger's seat. Awake and unresponsive and in critical condition");
		connection.addExternalPatientRequest(24,"Betty Warren","Brigham & Women's Hospital - Boston MA", "Ambulance","Medium Severity", 33337861, "7 minutes", "Patient passed out for 30 seconds. Is responsive and aware of their surroundings. Has no history of passing out.");
		connection.addExternalPatientRequest(27,"Maxim Rawlings","Brigham & Women's Hospital - Boston MA", "Ambulance","Low Severity", 40003829, "10 minutes", "Relocating a patient with lung cancer from Mt.Auburn Hospital.");
		connection.addExternalPatientRequest(24,"Alan Singh","Brigham & Women's Hospital - Boston MA", "Plane","High Severity", 38739983, "12 hours", "Heart transplant organ in route");

/*
		Floral Delivery:
			ADEPT00101,1401,2628,1,BTM,DEPT,Neuroscience Waiting Room,Neuro Waiting Room
			ADEPT00102,1395,2674,2,BTM,DEPT,Orthopedics and Rhemutalogy,Orthopedics and Rhemutalogy
			ADEPT00201,1720,2847,1,BTM,DEPT,MS Waiting,MS Waiting
			ADEPT00301,986,2852,1,BTM,DEPT,CART Waiting,CART Waiting
			DDEPT00102,4330,700,2,15 Francis,DEPT,Chest Diseases Floor 2,Chest Diseases

		Sanitation:
			AREST00101,1556,2604,1,BTM,REST,Restroom S elevator 1st floor,Restroom
			AREST00103,1552,2854,3,BTM,REST,Restroom BTM conference center 3rd floor,Restroom
			ARETL00101,1619,2522,1,BTM,RETL,Cafe,Cafe
			IREST00103,2255,1255,3,45 Francis,REST,Restroom 1 - Family,R1
			IREST00203,2570,1257,3,45 Francis,REST,Restroom 2,R2
			IREST00303,2745,1147,3,45 Francis,REST,Restroom 3,R3
			IREST00403,2300,1018,3,45 Francis,REST,Restroom 4 - M wheelchair,R4
			HRETL00102,1935,860,2,Tower,RETL,Garden Cafe,Garden Cafe

		Med Delivery:
			BLABS00102,2246,1350,2,45 Francis,LABS,Vascular Diagnostic Lab,Labs B0102
			BLABS00202,2945,995,2,45 Francis,LABS,Outpatient Specimen Collection,Labs B0202
			IDEPT00103,2323,1328,3,45 Francis,DEPT,Center for Infertility and Reproductive Surgery,D1
			IDEPT00203,2448,1328,3,45 Francis,DEPT,Gynecology Oncology MIGS,D2
			IDEPT00303,2730,1315,3,45 Francis,DEPT,General Surgical Specialties Suite A,D3
			IDEPT00403,2738,1227,3,45 Francis,DEPT,General Surgical Specialties Suite B,D4
			IDEPT00503,2868,1075,3,45 Francis,DEPT,Urology,D5
			IDEPT00603,2333,764,3,45 Francis,DEPT,Maternal Fetal Practice,D6
			IDEPT00703,2400,764,3,45 Francis,DEPT,Obstetrics,D7
			IDEPT00803,2492,887,3,45 Francis,DEPT,Fetal Med & Genetics,D8
			IDEPT00903,2631,851,3,45 Francis,DEPT,Gynecology,D9

		Security:
			HDEPT00203,1690,830,3,Tower,DEPT,MICU 3BC Waiting Room,MICU 3BC WR
			WELEV00E01,3265,830,1,45 Francis,ELEV,Elevator E Floor 1,Elevator E1
			ePARK00101,381,1725,1,Parking,PARK,Left Parking Lot Spot 001,Parking Left 001
			ePARK00201,406,1725,1,Parking,PARK,Left Parking Lot Spot 002,Parking Left 002
			eWALK00701,1730,1544,1,Parking,WALK,Entrance Sidewalk,Walkway
			BDEPT00302,2385,753,2,45 Francis,DEPT,Lee Bell Breast Center,DEPT B0302
			BDEPT00402,2439,902,2,45 Francis,DEPT,Jen Center for Primary Care,DEPT B0402
			CCONF002L1,2665,1043,L1,45 Francis,CONF,Medical Records Conference Room Floor L1,Conf C002L1



		*/





	}


}