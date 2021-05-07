package edu.wpi.cs3733.D21.teamE;

import edu.wpi.cs3733.D21.teamE.database.RequestsDB;
import edu.wpi.cs3733.D21.teamE.database.UserAccountDB;
import edu.wpi.cs3733.D21.teamE.database.makeConnection;
import edu.wpi.cs3733.D21.teamE.map.Edge;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.views.CovidSurveyObj;
import edu.wpi.cs3733.D21.teamE.views.UserManagement;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.AubonPainItem;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.ExternalPatientObj;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.FloralObj;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.LanguageInterpreterObj;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.ReligiousRequestObj;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import org.junit.jupiter.api.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
			DB.createAllTables();
		} catch (Exception e) {
			DB.createAllTables();
		}

	}

	@Test
	public void testing() {

	}

	@Test
	@DisplayName("testGetNodeInfo")
	public void testGetNodeInfo() {
		DB.addNode("test123", 12, 13, "1", "45 Francis", "PARK", "longName", "shortName");

		Node testNode1 = new Node("test123", 12, 13, "1", "45 Francis", "PARK", "longName", "shortName");

		Node testNode2 = DB.getNodeInfo("test123");

		assertTrue(testNode1.equals(testNode2));
	}


	@Test
	@DisplayName("GetNodeInfo for a node with null values")
	public void testGetNodeInfo2() {
		DB.addNode("test123", 12, 13, "1", "BTM", "PARK", "long", null);

		Node testNode1 = new Node("test123", 12, 13, "1", "BTM", "PARK", "long", null);

		Node testNode2 = DB.getNodeInfo("test123");

		assertTrue(testNode1.equals(testNode2));
	}


	@Test
	@DisplayName("testGetEdgeInfo")
	public void testGetEdgeInfo() {
		DB.addNode("testEdge1", 121, 122, "1", "BTM", "EXIT", "String", "String");
		DB.addNode("testEdge2", 12, 15, "1", "BTM", "EXIT", "String", "String");
		DB.addNode("testEdge3", 122, 123, "1", "BTM", "EXIT", "String", "String");
		DB.addNode("testEdge4", 124, 153, "1", "BTM", "EXIT", "String", "String");

		DB.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");
		DB.addEdge("testEdge3_testEdge4", "testEdge3", "testEdge4");
		DB.addEdge("testEdge2_testEdge4", "testEdge2", "testEdge4");

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

		resultEdgeInfo = DB.getEdgeInfo("testEdge4");

		assertEquals(resultEdgeInfo, listOfEdgeInfo);

	}


	@Test
	@DisplayName("testGetAllNodes")
	public void testGetAllNodes() {


		DB.addNode("testEdge1", 121, 122, "1", "BTM", "EXIT", "String", "String");
		DB.addNode("testEdge2", 12, 15, "1", "BTM", "EXIT", "String", "String");
		DB.addNode("testEdge3", 122, 123, "1", "BTM", "EXIT", "String", "String");
		DB.addNode("testEdge4", 124, 153, "1", "BTM", "EXIT", "String", "String");


		Node n1 = new Node("testEdge1", 121, 122, "1", "BTM", "EXIT", "String", "String");
		Node n2 = new Node("testEdge2", 12, 15, "1", "BTM", "EXIT", "String", "String");
		Node n3 = new Node("testEdge3", 122, 123, "1", "BTM", "EXIT", "String", "String");
		Node n4 = new Node("testEdge4", 124, 153, "1", "BTM", "EXIT", "String", "String");

		ArrayList<Node> testNodeArray = new ArrayList<>();
		testNodeArray.add(n1);
		testNodeArray.add(n2);
		testNodeArray.add(n3);
		testNodeArray.add(n4);

		ArrayList<Node> nodeArray = DB.getAllNodes();

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
	@DisplayName("testGetAllEdges")
	public void testGetAllEdges() {
		DB.addNode("test1", 0, 0, "1", "Tower", "ELEV", "test", "test");
		DB.addNode("test2", 2, 2, "1", "Tower", "ELEV", "test", "test");
		DB.addEdge("test1_test2", "test1", "test2");

		ArrayList<Edge> listOfEdges = new ArrayList<>();


		double length1 = Math.pow((-2), 2);
		double length2 = Math.pow((-2), 2);

		double length12 = Math.sqrt((length1 + length2));

		Edge edge1 = new Edge("test1_test2", "test1", "test2", length12);

		listOfEdges.add(edge1);

		ArrayList<Edge> resultListOfEdges = DB.getAllEdges();

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
		DB.addNode("test1", 0, 0, "1", "Tower", "ELEV", "test", "test");
		DB.addNode("test2", 2, 2, "1", "Tower", "ELEV", "test", "test");

		ArrayList<Edge> listOfEdges = DB.getAllEdges();

		assertEquals(0, listOfEdges.size());
	}

	@Test
	@DisplayName("testAddNode")
	public void testAddNode() {
		int testResult;

		// if this works, testResult should be 1
		testResult = DB.addNode("testNode", 111, 222, "1", "Tower", "ELEV", "String", "String");

		assertEquals(1, testResult);
	}

	@Test
	@DisplayName("testAddNode: the node added already exists")
	public void testAddNode2() {
		DB.addNode("testNode12", 121, 222, "1", "Tower", "ELEV", "String", "String");

		int testResult = DB.addNode("testNode12", 121, 222, "1", "Tower", "ELEV", "String", "String");

		assertEquals(0, testResult);
	}

	@Test
	@DisplayName("testGetNodeLite")
	public void testGetNodeLite() {
		DB.addNode("test233", 22, 33, "1", "BTM", "WALK", "long", null);

		Node testNode1 = new Node("test233", 22, 33, "1", "BTM", "WALK", "long", null);

		Node testNode2 = DB.getNodeLite("test233");

		assertTrue(testNode1.equals(testNode2));
	}


	@Test
	@DisplayName("testGetAllNodesByFloor: 2 nodes with the given floor")
	public void testGetAllNodesByFloor() {
		DB.addNode("nodeID1", 0, 0, "1", "Tower", "PARK", "longName1", "shortName1");
		DB.addNode("nodeID2", 1, 0, "1", "Tower", "PARK", "longName2", "shortName2");

		ArrayList<Node> nodes = DB.getAllNodesByFloor("1");

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
		DB.addNode("nodeID1", 0, 0, "1", "Tower", "ELEV", "longName1", "shortName1");
		DB.addNode("nodeID2", 1, 0, "1", "Tower", "ELEV", "longName2", "shortName2");
		DB.addNode("nodeID3", 3, 0, "1", "Tower", "ELEV", "longName3", "shortName3");

		ArrayList<Node> nodes = DB.getAllNodesByFloor("3");

		assertEquals(0, nodes.size());
	}


	@Test
	@DisplayName("testGetAllNodeLongNamesByFloor: 2 nodes with the given floor")
	public void testGetAllNodeLongNamesByFloor() {
		DB.addNode("nodeID1", 0, 0, "1", "Tower", "ELEV", "longName1", "shortName1");
		DB.addNode("nodeID2", 1, 0, "1", "Tower", "ELEV", "longName2", "shortName2");

		ObservableList<String> longNames = DB.getAllNodeLongNamesByFloor("1");

		ObservableList<String> correctLongNames = FXCollections.observableArrayList();

		correctLongNames.add("longName1");
		correctLongNames.add("longName2");

		assertEquals(longNames, correctLongNames);

	}

	@Test
	@DisplayName("testGetAllNodeLongNamesByFloor: no nodes with the given floor")
	public void testGetAllNodeLongNamesByFloor2() {
		DB.addNode("nodeID1", 0, 0, "1", "Tower", "ELEV", "longName1", "shortName1");
		DB.addNode("nodeID2", 1, 0, "1", "Tower", "ELEV", "longName2", "shortName2");
		DB.addNode("nodeID3", 0, 0, "1", "Tower", "ELEV", "longName3", "shortName3");

		ObservableList<String> longNames = DB.getAllNodeLongNamesByFloor("3");

		assertEquals(0, longNames.size());
	}

	@Test
	@DisplayName("testGetListOfNodeIDSByFloor: 2 nodes with the given floor")
	public void testGetListOfNodeIDSByFloor() {
		DB.addNode("nodeID1", 0, 0, "1", "Tower", "ELEV", "longName1", "shortName1");
		DB.addNode("nodeID2", 1, 0, "1", "Tower", "ELEV", "longName2", "shortName2");

		ArrayList<String> nodeIDs = DB.getListOfNodeIDSByFloor("1");

		ArrayList<String> correctNodeIDs = new ArrayList<>();

		correctNodeIDs.add("nodeID1");
		correctNodeIDs.add("nodeID2");

		assertEquals(nodeIDs, correctNodeIDs);
	}

	@Test
	@DisplayName("testGetListOfNodeIDSByFloor: no nodes with the given floor")
	public void testGetListOfNodeIDSByFloor2() {
		DB.addNode("nodeID1", 0, 0, "1", "Tower", "ELEV", "longName1", "shortName1");
		DB.addNode("nodeID2", 1, 0, "1", "Tower", "ELEV", "longName2", "shortName2");
		DB.addNode("nodeID3", 3, 0, "1", "Tower", "ELEV", "longName3", "shortName3");

		ArrayList<String> nodeIDs = DB.getListOfNodeIDSByFloor("3");


		assertEquals(0, nodeIDs.size());
	}


	@Test
	@DisplayName("testAddEdge")
	public void testAddEdge() {

		int testResult;

		DB.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		DB.addNode("testEdge2", 12, 15, "1", "Tower", "ELEV", "String", "String");

		// if this works, testResult should be 1
		testResult = DB.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");

		assertEquals(1, testResult);

	}

	@Test
	@DisplayName("testAddEdge: the edgeID already exists")
	public void testAddEdge2() {
		DB.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		DB.addNode("testEdge2", 12, 15, "1", "Tower", "ELEV", "String", "String");

		DB.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");

		int testResult = DB.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");

		assertEquals(0, testResult);
	}

	@Test
	@DisplayName("testAddEdge: the startNode does not exist")
	public void testAddEdge3() {
		DB.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		DB.addNode("testEdge2", 12, 15, "1", "Tower", "ELEV", "String", "String");

		// if this works, testResult should be 1
		int testResult = DB.addEdge("testEdge1_testEdge2", "testEdge3", "testEdge2");

		assertEquals(0, testResult);
	}

	@Test
	@DisplayName("testAddEdge: the endNode does not exist")
	public void testAddEdge4() {
		DB.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		DB.addNode("testEdge2", 12, 15, "1", "Tower", "ELEV", "String", "String");

		// if this works, testResult should be 1
		int testResult = DB.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge3");

		assertEquals(0, testResult);
	}

	@Test
	@DisplayName("testModifyNode")
	public void testModifyNode() {

		int testResult;

		DB.addNode("originalNode", 121, 122, "1", "Tower", "ELEV", "String", "String");

		testResult = DB.modifyNode("originalNode", 100, null, null, null, null, null, null);


		assertEquals(1, testResult);
	}

	@Test
	@DisplayName("testDeleteEdge")
	public void testDeleteEdge() {

		int testResult;

		DB.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		DB.addNode("testEdge2", 12, 15, "1", "Tower", "ELEV", "String", "String");

		DB.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");

		testResult = DB.deleteEdge("testEdge1", "testEdge2");

//		System.out.println(testResult);

		assertEquals(0, testResult);
	}

	@Test
	@DisplayName("testDeleteNode")
	public void testDeleteNode() {

		int testResult;

		DB.addNode("testEdge1", 121, 122, "1", "Tower", "ELEV", "String", "String");
		testResult = DB.deleteNode("testEdge1");

		assertEquals(1, testResult);
	}

	@Test
	@DisplayName("testGetListOfNodeIDS")
	public void testGetListOfNodeIDS() {

		DB.addNode("test1", 0, 0, "1", "Tower", "ELEV", "test", "test");
		DB.addNode("test2", 2, 2, "1", "Tower", "ELEV", "test", "test");
		DB.addNode("test3", 3, 3, "1", "Tower", "ELEV", "test", "test");
		DB.addNode("test4", 4, 4, "1", "Tower", "ELEV", "test", "test");

		ArrayList<String> listOfNodeIDs = new ArrayList<>();

		listOfNodeIDs.add("test1");
		listOfNodeIDs.add("test2");
		listOfNodeIDs.add("test3");
		listOfNodeIDs.add("test4");

		assertEquals(listOfNodeIDs, DB.getListOfNodeIDS());
	}

	@Test
	@DisplayName("Testing countNodeTypeOnFloor()")
	public void testCountNodeTypeOnFloor() {
		DB.addNode("test1", 534, 0, "1", "Tower", "INFO", "long", "asd");
		DB.addNode("Test2", 54, 2, "1", "Tower", "ELEV", "name", "test");
		DB.addNode("test3", 43, 3, "1", "Tower", "ELEV", "test", "hert");
		DB.addNode("test4", 544, 4, "1", "Tower", "ELEV", "fun", "test");
		DB.addNode("Test5", 4350, 0, "1", "Tower", "ELEV", "long", "asd");
		DB.addNode("test6", 5342, 2, "1", "Tower", "ELEV", "name", "test");
		DB.addNode("test7", 433, 3, "1", "Tower", "ELEV", "test", "hert");
		DB.addNode("test8", 344, 4, "1", "Tower", "ELEV", "fun", "test");
		DB.addNode("test9", 430, 0, "1", "Tower", "ELEV", "long", "asd");
		DB.addNode("test10", 432, 2, "1", "Tower", "ELEV", "name", "test");

		int returned = DB.countNodeTypeOnFloor("t", "1", "INFO");
		assertEquals(1, returned);
		int returned2 = DB.countNodeTypeOnFloor("t", "1", "PARK");
		assertEquals(0, returned2);
	}


	@Test
	@DisplayName("testCreateUserAccountTable")
	public void testCreateUserAccountTable() {

	}

	@Test
	@DisplayName("testAddSanitationRequest")
	public void testAddSanitationRequest() {

		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("custodian@gmail.com", "testPass", "custodian", "drew", "Shukla");

		DB.addSanitationRequest(new SanitationServiceObj(0, 1, 2, "test", "Urine Cleanup", "description here", "Low"));
	}

	@Test
	@DisplayName("testAddExternalPatientRequest")
	public void testAddExternalPatientRequest() {
		//('visitor', 'patient', 'doctor', 'admin', 'nurse', 'EMT', 'floralPerson', 'pharmacist', 'security', 'electrician', 'custodian')))";
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("EMT@gmail.com", "testPass", "EMT", "drew", "Shukla");

		DB.addExternalPatientRequest(new ExternalPatientObj(1, 1, 2, "test", "severe", "Ambulance", "123", "High", "Low", "Low", "They do not feel good"));
	}

	@Test
	@DisplayName("testAddMedicineRequest")
	public void testAddMedicineRequest() {
		//('visitor', 'patient', 'doctor', 'admin', 'nurse', 'EMT', 'floralPerson', 'pharmacist', 'security', 'electrician', 'custodian')))";
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("pharmacist@gmail.com", "testPass", "pharmacist", "drew", "Shukla");


		DB.addMedicineRequest(new MedicineDeliveryObj(1, 1, 2, "test", "drugs", 2, 100, "take once a day", "Nupur"));
	}

	@Test
	@DisplayName("testSecurityRequest")
	public void testAddSecurityRequest() {

		//('visitor', 'patient', 'doctor', 'admin', 'nurse', 'EMT', 'floralPerson', 'pharmacist', 'security', 'electrician', 'custodian')))";
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("security@gmail.com", "testPass", "security", "drew", "Shukla");

		DB.addSecurityRequest(new SecurityServiceObj(0, 1, 2, "test", "low", "Low", "reason"));
	}

	@Test
	@DisplayName("testAddFloralRequest")
	public void testAddFloralRequest() {
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("floralPerson1@gmail.com", "testPass", "floralPerson", "drew", "Shukla");

//		FloralObj: (String nodeID, int assigneeID, int userID, String flower, int count, String vase, String recipient, String message, String arrangement, String stuffedAnimal, String chocolate))

		FloralObj request = new FloralObj(0,1, 2, "test", "Nupur", "Roses", 1, "Tall", "do not Include arrangement", "do not Include stuffed Animal", "Include Chocolate", "feel better");

		DB.addFloralRequest(request);

	}

	@Test
	@DisplayName("testAddSpecialUserType")
	public void testAddSpecialUserType() {
		DB.addSpecialUserType("test1@gmail.com", "testPassword", "patient", "patientFirstName", "patientLastName");
		DB.addSpecialUserType("test2@gmail.com", "testPassword1", "admin", "adminFirstName", "adminLastName");
		DB.addSpecialUserType("test3@gmail.com", "testPassword2", "doctor", "doctorFirstName", "doctorLastName");
	}

	@Test
	@DisplayName("testEditUserAccount")
	public void testEditUserAccount() {
		DB.addSpecialUserType("test1@gmail.com", "testPassword", "patient", "patientFirstName", "patientLastName");
		DB.addSpecialUserType("test2@gmail.com", "testPassword1", "admin", "adminFirstName", "adminLastName");
		DB.addSpecialUserType("test3@gmail.com", "testPassword2", "doctor", "doctorFirstName", "doctorLastName");

		int affectedRow = DB.editUserAccount(1, "edited@gmail.com", null, null, "newFirstName", "newLastName");
		int affectedRow1 = DB.editUserAccount(3, null, "editedPassword", "patient", null, null);

		int totalRowsAffected = affectedRow + affectedRow1;

		assertEquals(2, totalRowsAffected);
	}

	@Test
	@DisplayName("testDeleteUserAccount")
	public void testDeleteUserAccount() {
		DB.addSpecialUserType("test1@gmail.com", "testPassword", "patient", "patientFirstName", "patientLastName");
		DB.addSpecialUserType("test2@gmail.com", "testPassword1", "admin", "adminFirstName", "adminLastName");
		DB.addSpecialUserType("test3@gmail.com", "testPassword2", "doctor", "doctorFirstName", "doctorLastName");

		int affectedRow = DB.deleteUserAccount(1);
		int affectedRow1 = DB.deleteUserAccount(3);

		int totalRowsAffected = affectedRow + affectedRow1;

		assertEquals(2, totalRowsAffected);
	}


	@Test
	@DisplayName("testGetRequestStatus")
	public void testGetRequestStatus() {

		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");

		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");

		DB.addSpecialUserType("EMT1@gmail.com", "testPass", "EMT", "bob", "Shukla");
		DB.addSpecialUserType("floralPerson1@gmail.com", "testPass", "floralPerson", "drew", "Shukla");


		DB.addExternalPatientRequest(new ExternalPatientObj(1, 1, 3, "test", "severe", "Ambulance", "123",  "High", "Low", "Low", "headache"));
		DB.addExternalPatientRequest(new ExternalPatientObj(2, 1, 3, "test", "severe", "Ambulance", "123",  "High", "Low", "Low", "migraine"));
		DB.addExternalPatientRequest(new ExternalPatientObj(3, 2, 3, "test", "severe", "Ambulance", "123",  "High", "Low", "Low", "migraine"));

		FloralObj request = new FloralObj(4,1, 4, "test", "Nupur", "Roses", 1, "Tall", "do not Include arrangement", "do not Include stuffed Animal", "Include Chocolate", "feel better");

		DB.addFloralRequest(request);

		ArrayList<String> returnedStatus;
		ArrayList<String> correctStatus = new ArrayList<>();

		correctStatus.add("inProgress");
		correctStatus.add("inProgress");

		returnedStatus = DB.getMyCreatedRequestInfo("extTransport", 1, "requestStatus");
		assertEquals(correctStatus, returnedStatus);
	}

	@Test
	@DisplayName("testGetRequestStatus2 : Admin")
	public void testGetRequestStatus2() {
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");

		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");

		DB.addSpecialUserType("EMT1@gmail.com", "testPass", "EMT", "bob", "Shukla");
		DB.addSpecialUserType("floralPerson1@gmail.com", "testPass", "floralPerson", "drew", "Shukla");

		DB.addExternalPatientRequest(new ExternalPatientObj(1, 1, 3, "test", "severe", "Ambulance", "123", "High", "Low", "Low", "headache"));
		DB.addExternalPatientRequest(new ExternalPatientObj(2, 1, 3, "test", "severe", "Ambulance", "123",  "High", "Low", "Low", "migraine"));
		DB.addExternalPatientRequest(new ExternalPatientObj(3, 2, 3, "test", "severe", "Ambulance", "123",  "High", "Low", "Low", "migraine"));

		FloralObj request = new FloralObj(0,1, 4, "test", "Nupur", "Roses", 1, "Tall", "do not Include arrangement", "do not Include stuffed Animal", "Include Chocolate", "feel better");

		DB.addFloralRequest(request);

		DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");
		ArrayList<String> returnedStatus;
		ArrayList<String> correctStatus = new ArrayList<>();
		correctStatus.add("inProgress");
		correctStatus.add("inProgress");
		correctStatus.add("inProgress");
		returnedStatus = DB.getMyCreatedRequestInfo("extTransport", 5, "requestStatus");
		assertEquals(correctStatus, returnedStatus);
	}

	@Test
	@DisplayName("testGetRequestStatus3 : No Data")
	public void testGetRequestStatus3() {
		DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");
		ArrayList<String> returnedStatus;
		returnedStatus = DB.getMyCreatedRequestInfo("extTransport", 1, "requestStatus");
		assertEquals(0, returnedStatus.size());
	}


	@Test
	@DisplayName("testGetRequestIDs")
	public void testGetRequestIDs() {

		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");

		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");

		DB.addSpecialUserType("EMT1@gmail.com", "testPass", "EMT", "bob", "Shukla");
		DB.addSpecialUserType("floralPerson1@gmail.com", "testPass", "floralPerson", "drew", "Shukla");


		DB.addExternalPatientRequest(new ExternalPatientObj(1, 1, 3, "test", "severe", "Ambulance", "123",  "High", "Low", "Low", "headache"));
		DB.addExternalPatientRequest(new ExternalPatientObj(2, 2, 3, "test", "severe", "Ambulance", "123",  "High", "Low", "Low", "migraine"));

		FloralObj request = new FloralObj(0,1, 4, "test", "Nupur", "Roses", 1, "Tall", "do not Include arrangement", "do not Include stuffed Animal", "Include Chocolate", "feel better");

		DB.addFloralRequest(request);

		ArrayList<String> returnedIDs;
		ArrayList<String> correctIDs = new ArrayList<>();
		correctIDs.add("1");
		returnedIDs = DB.getMyCreatedRequestInfo("extTransport", 1, "requestID");
		assertEquals(correctIDs, returnedIDs);
	}

	@Test
	@DisplayName("testGetRequestIDs2 : Admin")
	public void testGetRequestIDs2() {


		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");

		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");

		DB.addSpecialUserType("EMT1@gmail.com", "testPass", "EMT", "bob", "Shukla");
		DB.addSpecialUserType("floralPerson1@gmail.com", "testPass", "floralPerson", "drew", "Shukla");


		DB.addExternalPatientRequest(new ExternalPatientObj(1, 1, 3, "test", "severe", "Ambulance", "123",  "High", "Low", "Low", "headache"));
		DB.addExternalPatientRequest(new ExternalPatientObj(2, 2, 3, "test", "severe", "Ambulance", "123",  "High", "Low", "Low", "migraine"));

		FloralObj request = new FloralObj(0,1, 4, "test", "Nupur", "Roses", 1, "Tall", "High", "Low", "Low", "feel better");
		DB.addFloralRequest(request);



		DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");
		ArrayList<String> returnedIDs;
		ArrayList<String> correctIDs = new ArrayList<>();
		correctIDs.add("1");
		correctIDs.add("2");
		returnedIDs = DB.getMyCreatedRequestInfo("extTransport", 5, "requestID");
		assertEquals(correctIDs, returnedIDs);
	}

	@Test
	@DisplayName("testGetRequestIDs3: No Data")
	public void testGetRequestIDs3() {
		DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");
		ArrayList<String> returnedIDs;
		returnedIDs = DB.getMyCreatedRequestInfo("extTransport", 1, "requestID");
		assertEquals(0, returnedIDs.size());
	}


	@Test
	@DisplayName("testGetRequestAssignees")
	public void testGetRequestAssignees() {


		DB.addNode("test1", 0, 0, "2", "Tower", "INFO", "long name #1", "shortName");
		DB.addNode("test2", 0, 0, "L1", "Tower", "INFO", "long name #2", "shortName");
		DB.addNode("test3", 0, 0, "3", "Tower", "INFO", "long name #3", "shortName");

		DB.addUserAccount("test1@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");

		DB.addSpecialUserType("pharmacist1@gmail.com", "testPass", "pharmacist", "bob", "Shukla");
		DB.addSpecialUserType("pharmacist2@gmail.com", "testPass", "pharmacist", "kim", "Shukla");
		DB.addSpecialUserType("pharmacist3@gmail.com", "testPass", "pharmacist", "dell", "Shukla");

		DB.addMedicineRequest(new MedicineDeliveryObj(1, 1, 3, "test1", "drugs", 2, 100, "take once a day", "Nupur"));
		DB.addMedicineRequest(new MedicineDeliveryObj(2, 1, 4, "test2", "drugs2", 3, 10, "take once a day", "Nupur"));
		DB.addMedicineRequest(new MedicineDeliveryObj(3, 2, 5, "test3", "drugs3", 4, 1, "take once a day", "Nupur"));

		ArrayList<String> returnedAssignees = DB.getMyCreatedRequestInfo("medDelivery", 1, "assigneeID");
		ArrayList<String> correctAssignees = new ArrayList<>();
		correctAssignees.add("3");
		correctAssignees.add("4");
		assertEquals(correctAssignees, returnedAssignees);
	}

	@Test
	@DisplayName("testGetRequestAssignees2 : Admin")
	public void testGetRequestAssignees2() {

		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "long name #1", "shortName");
		DB.addNode("test3", 0, 0, "L1", "Tower", "INFO", "long name #2", "shortName");
		DB.addNode("test2", 0, 0, "3", "Tower", "INFO", "long name #3", "shortName");

		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addUserAccount("test2@gmail.com", "testPass", "Ben", "Shukla");

		DB.addSpecialUserType("pharmacist1@gmail.com", "testPass", "pharmacist", "bob", "Shukla");
		DB.addSpecialUserType("pharmacist2@gmail.com", "testPass", "pharmacist", "kim", "Shukla");
		DB.addSpecialUserType("pharmacist3@gmail.com", "testPass", "pharmacist", "dell", "Shukla");

		DB.addMedicineRequest(new MedicineDeliveryObj(1, 1, 3, "test", "drugs", 2, 100, "take once a day", "Nupur"));
		DB.addMedicineRequest(new MedicineDeliveryObj(2, 1, 4, "test2", "drugs2", 3, 10, "take once a day", "Nupur"));
		DB.addMedicineRequest(new MedicineDeliveryObj(3, 2, 5, "test3", "drugs3", 4, 1, "take once a day", "Nupur"));


		DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");
		ArrayList<String> returnedAssignees = DB.getMyCreatedRequestInfo("medDelivery", 6, "assigneeID");
		ArrayList<String> correctAssignees = new ArrayList<>();
		correctAssignees.add("3");
		correctAssignees.add("4");
		correctAssignees.add("5");
		assertEquals(correctAssignees, returnedAssignees);
	}

	@Test
	@DisplayName("testGetRequestAssignees3: no data")
	public void testGetRequestAssignees3() {
		DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");
		ArrayList<String> returnedAssignees = DB.getMyCreatedRequestInfo("medDelivery", 1, "assigneeID");
		assertEquals(0, returnedAssignees.size());
	}


	@Test
	@DisplayName("testGetRequestLocations")
	public void testGetRequestLocations() {

		DB.addNode("test1", 0, 0, "2", "Tower", "INFO", "long name #1", "shortName");
		DB.addNode("test2", 0, 0, "L1", "Tower", "INFO", "long name #2", "shortName");
		DB.addNode("test3", 0, 0, "3", "Tower", "INFO", "long name #3", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");

		DB.addSpecialUserType("pharmacist1@gmail.com", "testPass", "pharmacist", "bob", "Shukla");
		DB.addSpecialUserType("pharmacist2@gmail.com", "testPass", "pharmacist", "kim", "Shukla");

		DB.addMedicineRequest(new MedicineDeliveryObj(1, 1, 3, "test1", "drugs", 2, 100, "take once a day", "Nupur"));
		DB.addMedicineRequest(new MedicineDeliveryObj(2, 1, 4, "test2", "drugs2", 3, 10, "take once a day", "Nupur"));
		DB.addMedicineRequest(new MedicineDeliveryObj(3, 2, 3, "test3", "drugs3", 4, 1, "take once a day", "Nupur"));

		ArrayList<String> returnedLocations = DB.getRequestLocations("medDelivery", 1);
		ArrayList<String> correctLocations = new ArrayList<>();
		correctLocations.add("long name #1");
		correctLocations.add("long name #2");
		assertEquals(correctLocations, returnedLocations);
	}

	@Test
	@DisplayName("testGetRequestLocations2 : Admin")
	public void testGetRequestLocations2() {

		DB.addNode("test1", 0, 0, "2", "Tower", "INFO", "long name #1", "shortName");
		DB.addNode("test2", 0, 0, "L1", "Tower", "INFO", "long name #2", "shortName");
		DB.addNode("test3", 0, 0, "3", "Tower", "INFO", "long name #3", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");

		DB.addSpecialUserType("pharmacist1@gmail.com", "testPass", "pharmacist", "bob", "Shukla");
		DB.addSpecialUserType("pharmacist2@gmail.com", "testPass", "pharmacist", "kim", "Shukla");

		DB.addMedicineRequest(new MedicineDeliveryObj(1, 1, 3, "test1", "drugs", 2, 100, "take once a day", "Nupur"));
		DB.addMedicineRequest(new MedicineDeliveryObj(2, 1, 4, "test2", "drugs2", 3, 10, "take once a day", "Nupur"));
		DB.addMedicineRequest(new MedicineDeliveryObj(3, 2, 3, "test3", "drugs3", 4, 1, "take once a day", "Nupur"));

		DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");
		ArrayList<String> returnedLocations = DB.getRequestLocations("medDelivery", 5);
		ArrayList<String> correctLocations = new ArrayList<>();
		correctLocations.add("long name #1");
		correctLocations.add("long name #2");
		correctLocations.add("long name #3");
		assertEquals(correctLocations, returnedLocations);
	}

	@Test
	@DisplayName("testGetRequestLocations3 : No Data")
	public void testGetRequestLocations3() {
		DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");
		ArrayList<String> returnedLocations = DB.getRequestLocations("medDelivery", 1);
		assertEquals(0, returnedLocations.size());
	}


	@Test
	@DisplayName("testEditSanitationRequest")
	public void testEditSanitationRequest() {
		DB.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");
		DB.addSpecialUserType("custodian@gmail.com", "testPass", "custodian", "bob", "Shukla");

		DB.addSanitationRequest(new SanitationServiceObj(0, 1, 2, "test", "Urine Cleanup", "description here", "Low"));

		assertEquals(1, DB.editSanitationRequest(new SanitationServiceObj(1, 1,0, "test", null, null, null)));
	}

	@Test
	@DisplayName("testEditExternalPatientRequest")
	public void testEditExternalPatientRequest() {

		DB.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");
		DB.addSpecialUserType("EMT@gmail.com", "testPass", "EMT", "bob", "Shukla");

		DB.addExternalPatientRequest(new ExternalPatientObj(1, 1, 2, "test", "severe", "Ambulance", "123",  "High", "Low", "Low", "headache"));
		ExternalPatientObj changedRequest = new ExternalPatientObj(1, 0, 0, "test", "severe", "Plane", "145", "Low", "High", null, null);

		assertEquals(1, DB.editExternalPatientRequest(changedRequest));
	}

	@Test
	@DisplayName("testEditMedicineRequest")
	public void testEditMedicineRequest() {

		DB.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");
		DB.addSpecialUserType("pharmacist@gmail.com", "testPass", "pharmacist", "bob", "Shukla");

		DB.addMedicineRequest(new MedicineDeliveryObj(1, 1, 2, "test", "drugs", 2, 100, "take once a day", "Nupur"));

		assertEquals(1, DB.editMedicineRequest(new MedicineDeliveryObj(1, 1, 2, "test", "Tylenol", 100, 2, "Take twice everyday", null)));
	}

	@Test
	@DisplayName("testEditFloralRequest")
	public void testEditFloralRequest() {

		DB.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");
		DB.addSpecialUserType("floralPerson@gmail.com", "testPass", "floralPerson", "bob", "Shukla");

		FloralObj request = new FloralObj(0,1, 2, "test", "Nupur", "Roses", 1, "Tall", "do not Include arrangement", "do not Include stuffed Animal", "Include Chocolate", "feel better");

		DB.addFloralRequest(request);
		FloralObj changedRequest = new FloralObj(1, 0, 0, "test", "Ashley", "Tulips", 0, "Round", "Include arrangement", "stuffed Animal", "Do not Include Chocolate", null);

		assertEquals(1, DB.editFloralRequest(changedRequest));
	}

	@Test
	@DisplayName("testEditSecurityRequest")
	public void testEditSecurityRequest() {

		DB.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");
		DB.addSpecialUserType("security@gmail.com", "testPass", "security", "bob", "Shukla");

		DB.addSecurityRequest(new SecurityServiceObj(0, 1, 2, "test", "low", "Low", "reason"));

		assertEquals(1, DB.editSecurityRequest(new SecurityServiceObj(1, 1, 1, "test", "High", null, null)));
	}

	@Test
	@DisplayName("testChangeRequestStatus")
	public void testChangeRequestStatus() {

		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("pharmacist@gmail.com", "testPass", "pharmacist", "bob", "Shukla");

		DB.addMedicineRequest(new MedicineDeliveryObj(1, 1, 2, "test", "drugs", 2, 100, "take once a day", "Nupur"));
		DB.addMedicineRequest(new MedicineDeliveryObj(2, 1, 2, "test", "drugs2", 3, 10, "take once a day", "Nupur"));

		assertEquals(1, DB.editRequests(1, 0, "complete"));
	}

	@Test
	@DisplayName("testDataForPresentation")
	public void testDataForPresentation() {

		//Floral Delivery Nodes:
		DB.addNode("ADEPT00101", 1401, 2628, "1", "BTM", "DEPT", "Neuroscience Waiting Room", "Neuro Waiting Room");
		DB.addNode("ADEPT00102", 1395, 2674, "2", "BTM", "DEPT", "Orthopedics and Rhemutalogy", "Orthopedics and Rhemutalogy");
		DB.addNode("ADEPT00201", 1720, 2847, "1", "BTM", "DEPT", "MS Waiting", "MS Waiting");
		DB.addNode("ADEPT00301", 986, 2852, "1", "BTM", "DEPT", "CART Waiting", "CART Waiting");
		DB.addNode("DDEPT00102", 4330, 700, "2", "15 Francis", "DEPT", "Chest Diseases Floor 2", "Chest Diseases");


		//Sanitation Nodes:
		DB.addNode("AREST00101", 1556, 2604, "1", "BTM", "REST", "Restroom S elevator 1st floor", "Restroom");
		DB.addNode("AREST00103", 1552, 2854, "3", "BTM", "REST", "Restroom BTM conference center 3rd floor", "Restroom");
		DB.addNode("ARETL00101", 1619, 2522, "1", "BTM", "RETL", "Cafe", "Cafe");
		DB.addNode("IREST00103", 2255, 1255, "3", "45 Francis", "REST", "Restroom 1 - Family", "R1");
		DB.addNode("IREST00203", 2570, 1257, "3", "45 Francis", "REST", "Restroom 2", "R2");
		DB.addNode("IREST00303", 2745, 1147, "3", "45 Francis", "REST", "Restroom 3", "R3");
		DB.addNode("IREST00403", 2300, 1018, "3", "45 Francis", "REST", "Restroom 4 - M wheelchair", "R4");
		DB.addNode("HRETL00102", 1935, 860, "2", "Tower", "RETL", "Garden Cafe", "Garden Cafe");


		//med delivery Nodes:
		DB.addNode("BLABS00102", 2246, 1350, "2", "45 Francis", "LABS", "Vascular Diagnostic Lab", "Labs B0102");
		DB.addNode("BLABS00202", 2945, 995, "2", "45 Francis", "LABS", "Outpatient Specimen Collection", "Labs B0202");
		DB.addNode("IDEPT00103", 2323, 1328, "3", "45 Francis", "DEPT", "Center for Infertility and Reproductive Surgery", "D1");
		DB.addNode("IDEPT00203", 2448, 1328, "3", "45 Francis", "DEPT", "Gynecology Oncology MIGS", "D2");
		DB.addNode("IDEPT00303", 2730, 1315, "3", "45 Francis", "DEPT", "General Surgical Specialties Suite A", "D3");
		DB.addNode("IDEPT00403", 2738, 1227, "3", "45 Francis", "DEPT", "General Surgical Specialties Suite B", "D4");
		DB.addNode("IDEPT00503", 2868, 1075, "3", "45 Francis", "DEPT", "Urology", "D5");
		DB.addNode("IDEPT00603", 2333, 764, "3", "45 Francis", "DEPT", "Maternal Fetal Practice", "D6");
		DB.addNode("IDEPT00703", 2400, 764, "3", "45 Francis", "DEPT", "Obstetrics", "D7");
		DB.addNode("IDEPT00803", 2492, 887, "3", "45 Francis", "DEPT", "Fetal Med & Genetics", "D8");
		DB.addNode("IDEPT00903", 2631, 851, "3", "45 Francis", "DEPT", "Gynecology", "D9");


		//Security Nodes:
		DB.addNode("HDEPT00203", 1690, 830, "3", "Tower", "DEPT", "MICU 3BC Waiting Room", "MICU 3BC WR");
		DB.addNode("WELEV00E01", 3265, 830, "1", "45 Francis", "ELEV", "Elevator E Floor 1", "Elevator E1");
		DB.addNode("ePARK00101", 381, 1725, "1", "Parking", "PARK", "Left Parking Lot Spot 001", "Parking Left 001");
		DB.addNode("ePARK00201", 406, 1725, "1", "Parking", "PARK", "Left Parking Lot Spot 002", "Parking Left 002");
		DB.addNode("eWALK00701", 1730, 1544, "1", "Parking", "WALK", "Entrance Sidewalk", "Walkway");
		DB.addNode("BDEPT00302", 2385, 753, "2", "45 Francis", "DEPT", "Lee Bell Breast Center", "DEPT B0302");
		DB.addNode("BDEPT00402", 2439, 902, "2", "45 Francis", "DEPT", "Jen Center for Primary Care", "DEPT B0402");
		DB.addNode("CCONF002L1", 2665, 1043, "L1", "45 Francis", "CONF", "Medical Records Conference Room Floor L1", "Conf C002L1");

		//External transport:
		DB.addNode("FDEPT00501", 2128, 1300, "1", "Tower", "DEPT", "Emergency Department", "Emergency");
		DB.addNode("EEXIT00101", 2275, 785, "1", "45 Francis", "EXIT", "Ambulance Parking Exit Floor 1", "AmbExit 1");

		connection.addDataForPresentation();
	}


	@Test
	@DisplayName("testFunction")
	public void testFunction() {
		//DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");

		//Visitors:
		// - have access to floral Delivery
		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addUserAccount("terry_reilly123@yahoo.com", "visitor2", "Terry", "Reilly");
		DB.addUserAccount("smiddle@outlook.com", "visitor3", "Sharon", "Middleton");
		DB.addUserAccount("catherinehop12@gmail.com", "visitor4", "Catherine", "Hopkins");
		DB.addUserAccount("mbernard@wpi.edu", "visitor5", "Michelle", "Bernard");
		DB.addUserAccount("mccoy.meghan@hotmail.com", "visitor6", "Meghan", "Mccoy");
		DB.addUserAccount("harry89owens@gmail.com", "visitor7", "Harry", "Owens");
		DB.addUserAccount("hugowh@gmail.com", "visitor8", "Hugo", "Whitehouse");
		DB.addUserAccount("spenrodg@yahoo.com", "visitor9", "Spencer", "Rodgers");
		DB.addUserAccount("thomasemail@gmail.com", "visitor10", "Thomas", "Mendez");
		DB.addUserAccount("claytonmurray@gmail.com", "visitor11", "Clayton", "Murray");
		DB.addUserAccount("lawrencekhalid@yahoo.com", "visitor12", "Khalid", "Lawrence");

		//Patients:
		//13 - 19
		DB.addSpecialUserType("adamj@gmail.com", "patient1", "patient", "Adam", "Jenkins");
		DB.addSpecialUserType("abbym@yahoo.com", "patient2", "patient", "Abby", "Mohamed");
		DB.addSpecialUserType("wesleya@gmail.com", "patient3", "patient", "Wesley", "Armstrong");
		DB.addSpecialUserType("travisc@yahoo.com", "patient4", "patient", "Travis", "Cook");
		DB.addSpecialUserType("gabriellar@gmail.com", "patient5", "patient", "Gabriella", "Reyes");
		DB.addSpecialUserType("troyo@yahoo.com", "patient6", "patient", "Troy", "Olson");
		DB.addSpecialUserType("anat@gmail.com", "patient7", "patient", "Ana", "Turner");

		//Doctors:
		//20-27
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		DB.addSpecialUserType("ameliak@yahoo.com", "doctor02", "doctor", "Amelia", "Knight");
		DB.addSpecialUserType("simond@gmail.com", "doctor03", "doctor", "Simon", "Daniel");
		DB.addSpecialUserType("victoriae@yahoo.com", "doctor04", "doctor", "Victoria", "Erickson");
		DB.addSpecialUserType("taylorr@gmail.com", "doctor05", "doctor", "Taylor", "Ramos");
		DB.addSpecialUserType("rosas@yahoo.com", "doctor06", "doctor", "Rosa", "Smith");
		DB.addSpecialUserType("declanp@gmail.com", "doctor07", "doctor", "Declan", "Patel");
		DB.addSpecialUserType("laurenb@yahoo.com", "doctor08", "doctor", "Lauren", "Bolton");

		//Admin:
		//28 - 30
		DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");
		DB.addSpecialUserType("andrewg@yahoo.com", "admin002", "admin", "Andrew", "Guerrero");
		DB.addSpecialUserType("aleshah@gmail.com", "admin003", "admin", "Alesha", "Harris");

		//External transport:
		DB.addNode("FDEPT00501", 2128, 1300, "1", "Tower", "DEPT", "Emergency Department", "Emergency");
		DB.addNode("EEXIT00101", 2275, 785, "1", "45 Francis", "EXIT", "Ambulance Parking Exit Floor 1", "AmbExit 1");

		// EMT:
		// 31- 39
		DB.addSpecialUserType("ciarang@gmail.com", "admin001", "EMT", "Ciaran", "Goodwin");
		DB.addSpecialUserType("lolab@gmail.com", "admin001", "EMT", "Lola", "Bond");
		DB.addSpecialUserType("samanthar@gmail.com", "admin001", "EMT", "Samantha", "Russell");
		DB.addSpecialUserType("calebc@gmail.com", "admin001", "EMT", "Caleb", "Chapman");
		DB.addSpecialUserType("dalec@gmail.com", "admin001", "EMT", "Dale", "Coates");
		DB.addSpecialUserType("jerrym@gmail.com", "admin001", "EMT", "Jerry", "Myers");
		DB.addSpecialUserType("bettyw@gmail.com", "admin001", "EMT", "Betty", "Warren");
		DB.addSpecialUserType("maximr@gmail.com", "admin001", "EMT", "Maxim", "Rawlings");
		DB.addSpecialUserType("alans@gmail.com", "admin001", "EMT", "Alan", "Singh");


		DB.addExternalPatientRequest(new ExternalPatientObj(1, 27, 31, "EEXIT00101", "High Severity", "Ambulance", "12334567",  "Low", "High", "Low", "Patient dropped down into a state of unconsciousness randomly at the store. Patient is still unconscious and unresponsive but has a pulse. No friends or family around during the incident. "));
		DB.addExternalPatientRequest(new ExternalPatientObj(2, 30, 32, "EEXIT00101", "High Severity", "Ambulance", "4093380", "High", "Low", "Low", "Patient coming in with cut on right hand. Needs stitches. Bleeding is stable."));
		DB.addExternalPatientRequest(new ExternalPatientObj(3, 22, 33, "FDEPT00501", "High Severity", "Helicopter", "92017693", "Low", "Low", "High", "Car crash on the highway. 7 year old child in the backseat with no seatbelt on in critical condition. Blood pressure is low and has major trauma to the head."));
		DB.addExternalPatientRequest(new ExternalPatientObj(4, 20, 34, "FDEPT00501", "High Severity", "Helicopter", "93754789",  "High", "Low", "Low", "Skier hit tree and lost consciousness. Has been unconscious for 30 minutes. Still has a pulse."));
		DB.addExternalPatientRequest(new ExternalPatientObj(5, 24, 35, "EEXIT00101", "High Severity", "Ambulance", "417592", "High", "High", "Low", "Smoke inhalation due to a fire. No burns but difficult time breathing."));
		DB.addExternalPatientRequest(new ExternalPatientObj(6, 28, 36, "FDEPT00501", "High Severity", "Helicopter", "44888936",  "Low", "Low", "High", "Major car crash on highway. Middle aged woman ejected from the passenger's seat. Awake and unresponsive and in critical condition"));
		DB.addExternalPatientRequest(new ExternalPatientObj(7, 24, 37, "EEXIT00101", "High Severity", "Ambulance", "33337861", "High", "Low", "Low", "Patient passed out for 30 seconds. Is responsive and aware of their surroundings. Has no history of passing out."));
		DB.addExternalPatientRequest(new ExternalPatientObj(8, 27, 38, "FDEPT00501", "High Severity", "Ambulance", "40003829", "High", "Low", "Low", "Relocating a patient with lung cancer from Mt.Auburn Hospital."));
		DB.addExternalPatientRequest(new ExternalPatientObj(9, 24, 39, "FDEPT00501", "High Severity", "Plane", "38739983", "Low", "High", "Low", "Heart transplant organ in route"));


		ArrayList<String> correctLongNames = new ArrayList<>();
		correctLongNames.add("Ambulance Parking Exit Floor 1");
		correctLongNames.add("Emergency Department");



		ArrayList<String> locationArray = DB.getRequestLocations("extTransport", 27);

		assertEquals(correctLongNames, locationArray);
	}

	@Test
	@DisplayName("testAddAppointment")
	public void testAddAppointment() {

		DB.addNode("ADEPT00101", 1401, 2628, "1", "BTM", "DEPT", "Neuroscience Waiting Room", "Neuro Waiting Room");

		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		int rowsAffected1 = DB.addAppointment(1, "4:00", "02/12/21", 2);
		int rowsAffected2 = DB.addAppointment(2, "8:00",  "02/12/21",2);

		int totalRowsAffected = rowsAffected1 + rowsAffected2;
		assertEquals(2, totalRowsAffected);
	}


	@Test
	@DisplayName("testEditAppointment")
	public void testEditAppointment() {

		DB.addNode("ADEPT00101", 1401, 2628, "1", "BTM", "DEPT", "Neuroscience Waiting Room", "Neuro Waiting Room");

		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		DB.addSpecialUserType("ameliak@yahoo.com", "doctor02", "doctor", "Amelia", "Knight");

		DB.addAppointment(1, "4:00",  "02/12/21",2);
		DB.addAppointment(2, "8:00", "02/12/21",2);


		int rowAffected = DB.editAppointment(1, "6:00", "7:00", null);
		int rowAffected2 = DB.editAppointment(2, "9:00", "10:00", 3);

		int totalRowAffected = rowAffected + rowAffected2;

		assertEquals(2, totalRowAffected);
	}


	@Test
	@DisplayName("testAddAppointment")
	public void testAddRemovedPatientAppointmentHistory() {

		DB.addNode("ADEPT00101", 1401, 2628, "1", "BTM", "DEPT", "Neuroscience Waiting Room", "Neuro Waiting Room");

		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		DB.addAppointment(1, "8:00", "02/12/21",2);
		DB.addAppointment(2, "8:00",  "02/12/21",2);

		DB.addRemovedPatientAppointmentHistory(1);

	}

	@Test
	@DisplayName("testCancelAppointment")
	public void testCancelAppointment() {
		DB.addNode("ADEPT00101", 1401, 2628, "1", "BTM", "DEPT", "Neuroscience Waiting Room", "Neuro Waiting Room");

		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");

		DB.addAppointment(1, "8:00",  "02/12/21",2);

		int rowsAffected = DB.cancelAppointment(1);

		assertEquals(1, rowsAffected);
	}


	@Test
	@DisplayName("testCancelAppointment : multiple appointments")
	public void testCancelAppointment2() {
		DB.addNode("ADEPT00101", 1401, 2628, "1", "BTM", "DEPT", "Neuroscience Waiting Room", "Neuro Waiting Room");

		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");

		DB.addAppointment(1, "8:00", "02/12/21",2);
		DB.addAppointment(1, "8:00",  "02/13/21",2);
		DB.addAppointment(1, "8:00",  "02/14/21",2);

		int rowsAffected = DB.cancelAppointment(1);

		assertEquals(1, rowsAffected);
	}


	@Test
	@DisplayName("testGetSelectableAssignees")
	public void testGetSelectableAssignees() {
//		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
//
//		DB.addSpecialUserType("billb@gmail.com","doctor01","doctor","Bill", "Byrd");
//		DB.addSpecialUserType("billb@gmail.com","doctor01","doctor","Bill", "Byrd");
//		DB.addSpecialUserType("billb@gmail.com","doctor01","doctor","Bill", "Byrd");
//		DB.addSpecialUserType("billb@gmail.com","doctor01","doctor","Bill", "Byrd");
//		DB.addSpecialUserType("billb@gmail.com","doctor01","doctor","Bill", "Byrd");
//
//	//('visitor', 'patient', 'doctor', 'admin', 'nurse', 'EMT', 'floralPerson', 'pharmacist', 'security', 'electrician', 'custodian')))";
//
//		DB.addMedicineRequest(20, "Clara Bryan", "BLABS00102", "Atorvastatin", 30, "20mg", "Once a day by mouth", "Bill Byrd");
//		DB.addMedicineRequest(20, "Jennifer Cunningham", "BLABS00202", "Lisinopril", 90, "20mg", "Once a day by mouth", "Bill Byrd");
//
//		DB.addSecurityRequest(20, "James O'Moore","HDEPT00203", "Low", "Low");
//		DB.addSecurityRequest(22, "Russell Armstrong","WELEV00E01", "Medium", "Medium");
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
	@Test
	@DisplayName("userLoginTest")
	public void userLoginTest() {

		//Visitors:
		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addUserAccount("terry_reilly123@yahoo.com", "visitor2", "Terry", "Reilly");
		//Patients:
		DB.addSpecialUserType("adamj@gmail.com", "patient1", "patient", "Adam", "Jenkins");
		DB.addSpecialUserType("abbym@yahoo.com", "patient2", "patient", "Abby", "Mohamed");
		//Doctors:
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		DB.addSpecialUserType("ameliak@yahoo.com", "doctor02", "doctor", "Amelia", "Knight");
		//Fake Admin:
		DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");
		DB.addSpecialUserType("andrewg@yahoo.com", "admin002", "admin", "Andrew", "Guerrero");
		DB.addSpecialUserType("aleshah@gmail.com", "admin003", "admin", "Alesha", "Harris");
		//floralPerson:
		DB.addSpecialUserType("amyw@gmail.com", "floralPerson1", "floralPerson", "Amy", "Castaneda");
		DB.addSpecialUserType("elsaf@gmail.com", "floralPerson2", "floralPerson", "Elsa", "Figueroa");
		//custodian:
		DB.addSpecialUserType("crystalh@gmail.com", "custodian1", "custodian", "Crystal", "Harvey");
		DB.addSpecialUserType("minnien@gmail.com", "custodian2", "custodian", "Minnie", "Newman");
		//pharmacist:
		DB.addSpecialUserType("clarab@gmail.com", "pharmacist1", "pharmacist", "Clara", "Bryan");
		DB.addSpecialUserType("jenniferc@gmail.com", "pharmacist2", "pharmacist", "Jennifer", "Cunningham");
		//security
		DB.addSpecialUserType("jameso@gmail.com", "security1", "security", "James", "O'Moore");
		DB.addSpecialUserType("russella@gmail.com", "security2", "security", "Russell", "Armstrong");
		//EMT:
		DB.addSpecialUserType("ciarang@gmail.com", "EMT000001", "EMT", "Ciaran", "Goodwin");
		DB.addSpecialUserType("lolab@gmail.com", "EMT000002", "EMT", "Lola", "Bond");
		//Real Admins:
		ArrayList<String> insertUsers = new ArrayList<>();
		insertUsers.add("Insert Into userAccount Values (-1, 'superAdmin', 'superAdmin999', 'admin', 'Super', 'Admin', Current Timestamp, '', Null, Null)");
		insertUsers.add("Insert Into userAccount Values (-99, 'admin', 'admin', 'admin', 'admin', 'admin', Current Timestamp, '', Null, Null)");
		insertUsers.add("Insert Into userAccount Values (99999, 'staff', 'staff', 'doctor', 'staff', 'staff', Current Timestamp, '', Null, Null)");
		insertUsers.add("Insert Into userAccount Values (10000, 'guest', 'guest', 'patient', 'guest', 'visitor', Current Timestamp, '', Null, Null)");

		for (String insertUser : insertUsers) {
			try (PreparedStatement prepState = makeConnection.makeConnection().connection.prepareStatement(insertUser)) {
				prepState.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				//showError("This email already has an account");
				System.err.println("Error inserting into userAccount inside function userLoginTest()");
			}
		}

		//Visitors:
		assertEquals(1, DB.userLogin("bellag@gmail.com", "visitor1"));
		assertEquals(2, DB.userLogin("terry_reilly123@yahoo.com", "visitor2"));
		//Patients:
		assertEquals(3, DB.userLogin("adamj@gmail.com", "patient1"));
		assertEquals(4, DB.userLogin("abbym@yahoo.com", "patient2"));
		//Doctors:
		assertEquals(5, DB.userLogin("billb@gmail.com", "doctor01"));
		assertEquals(6, DB.userLogin("ameliak@yahoo.com", "doctor02"));
		//Fake Admin:
		assertEquals(7, DB.userLogin("abbyw@gmail.com", "admin001"));
		assertEquals(8, DB.userLogin("andrewg@yahoo.com", "admin002"));
		assertEquals(9, DB.userLogin("aleshah@gmail.com", "admin003"));
		//floralPerson:
		assertEquals(10, DB.userLogin("amyw@gmail.com", "floralPerson1"));
		assertEquals(11, DB.userLogin("elsaf@gmail.com", "floralPerson2"));
		//custodian:
		assertEquals(12, DB.userLogin("crystalh@gmail.com", "custodian1"));
		assertEquals(13, DB.userLogin("minnien@gmail.com", "custodian2"));
		//pharmacist:
		assertEquals(14, DB.userLogin("clarab@gmail.com", "pharmacist1"));
		assertEquals(15, DB.userLogin("jenniferc@gmail.com", "pharmacist2"));
		//security
		assertEquals(16, DB.userLogin("jameso@gmail.com", "security1"));
		assertEquals(17, DB.userLogin("russella@gmail.com", "security2"));
		//EMT:
		assertEquals(18, DB.userLogin("ciarang@gmail.com", "EMT000001"));
		assertEquals(19, DB.userLogin("lolab@gmail.com", "EMT000002"));
		//Real Admin:
		assertEquals(-1, DB.userLogin("superAdmin", "superAdmin999"));
		assertEquals(-99, DB.userLogin("admin", "admin"));
		assertEquals(99999, DB.userLogin("staff", "staff"));
		assertEquals(10000, DB.userLogin("guest", "guest"));
	}


	@Test
	@DisplayName("testGetUserType")
	public void testGetUserType() {
		DB.addSpecialUserType("email@gmail.com", "12345678", "admin", "firstName", "lastName");
		String returnedUserType = DB.getUserType(1);
		assertEquals("admin", returnedUserType);
	}


	@Test
	@DisplayName("testAddLanguageRequest")
	public void testAddLanguageRequest() {

		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test1@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("interpreter@gmail.com", "testPass", "interpreter", "drew", "Shukla");

		LanguageInterpreterObj request = new LanguageInterpreterObj(0,1, 2, "test", "Hindi", "I need help translating");
		DB.addLanguageRequest(request);
	}

	@Test
	@DisplayName("testAddLaundryRequest")
	public void testAddLaundryRequest() {

		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("interpreter@gmail.com", "testPass", "interpreter", "drew", "Shukla");

		LaundryObj request = new LaundryObj(0, "test", 2, 1,"2", "2", "I haven't done laundry in 2 weeks");
		DB.addLaundryRequest(request);
	}

	@Test
	@DisplayName("testAddMaintenanceRequest")
	public void testAddMaintenanceRequest() {

		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("interpreter@gmail.com", "testPass", "electrician", "drew", "Shukla");

		DB.addMaintenanceRequest(new MaintenanceObj(1, 1, "test", 2, "electrical", "not very severe", "light switch not working"));
	}

// this is where testAddFoodDeliveryRequest() can go

	@Test
	@DisplayName("testEditLanguageRequest")
	public void testEditLanguageRequest() {
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test1@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("interpreter@gmail.com", "testPass", "interpreter", "drew", "Shukla");

		LanguageInterpreterObj request = new LanguageInterpreterObj(0,1, 2, "test", "Hindi", "I need help translating");

		DB.addLanguageRequest(request);


		LanguageInterpreterObj infoToChange = new LanguageInterpreterObj(1,1, 0,"test", "Korean", null);
		assertEquals(1, DB.editLanguageRequest(infoToChange));
	}

	@Test
	@DisplayName("testEditLaundryRequest")
	public void testEditLaundryRequest() {
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("interpreter@gmail.com", "testPass", "interpreter", "drew", "Shukla");

		LaundryObj request = new LaundryObj(0,  "test", 1, 2, "2", "2", "I haven't done laundry in 2 weeks");
		DB.addLaundryRequest(request);

		assertEquals(1, DB.editLaundryRequest(new LaundryObj(1, "test", 0, 0, "3", "3", null)));
	}

	@Test
	@DisplayName("testEditMaintenanceRequest")
	public void testEditMaintenanceRequest() {
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("interpreter@gmail.com", "testPass", "electrician", "drew", "Shukla");

		DB.addMaintenanceRequest(new MaintenanceObj(1, 1, "test", 2, "electrical", "not very severe", "light switch not working"));

		assertEquals(1, DB.editMaintenanceRequest(new MaintenanceObj(1, 1, null, 2, null, "very severe","wires are loose everywhere!")));
	}


	@Test
	@DisplayName("testAddAubonPainMenuItem")
	public void testAddAubonPainMenuItem() {
		DB.addAubonPainMenuItem(new AubonPainItem("foodImage", "foodItem", "$56.00", "23 Calories", "foodDescription"));
	}

	@Test
	@DisplayName("testPopulateAbonPainTable")
	public void testPopulateAbonPainTable() {
		DB.populateAbonPainTable();
	}

	@Test
	@DisplayName("testGetAubonPanItems")
	public void testGetAubonPanItems() {



		AubonPainItem item1 = new AubonPainItem("foodImageURL", "foodItem", "$4732.23", "243 calories", "food description");
		AubonPainItem item2 = new AubonPainItem("URLLLLL", "pancakes", "$23.45", "3290 calories", "yummmyyy");
		AubonPainItem item3 = new AubonPainItem("foooooodddd URL", "soup", "$92.44", "3 calories", "goood");

		DB.addAubonPainMenuItem(item1);
		DB.addAubonPainMenuItem(item2);
		DB.addAubonPainMenuItem(item3);

		ArrayList<AubonPainItem> correctItems = new ArrayList<>();
		correctItems.add(item1);
		correctItems.add(item2);
		correctItems.add(item3);

		ArrayList<AubonPainItem> returnedItems = DB.getAubonPanItems();


		boolean allCorrect = true;
		boolean foodImage = false;
		boolean foodItem = false;
		boolean foodPrice = false;
		boolean foodCalories = false;
		boolean foodDescription = false;


		if (returnedItems.size() == correctItems.size()) {
			for (int item = 0; item < correctItems.size(); item++) {
				AubonPainItem returnedItem = returnedItems.get(item);
				AubonPainItem correctItem = correctItems.get(item);
				if (returnedItem.getImageURL().equals(correctItem.getImageURL())) {
					foodImage = true;
				}
				if (returnedItem.getFoodItem().equals(correctItem.getFoodItem())) {
					foodItem = true;
				}
				if (returnedItem.getFoodPrice().equals(correctItem.getFoodPrice())) {
					foodPrice = true;
				}
				if (returnedItem.getFoodCalories().equals(correctItem.getFoodCalories())) {
					foodCalories = true;
				}
				if (returnedItem.getFoodDescription().equals(correctItem.getFoodDescription())) {
					foodDescription = true;
				}
				if (!(foodImage && foodItem && foodPrice && foodCalories && foodDescription)) {
					allCorrect = false;
				}
			}
		} else {
			allCorrect = false;
		}
		assertTrue(allCorrect);

	}

	@Test
	@DisplayName("testAddInternalPatientRequest")
	public void testAddInternalPatientRequest() {
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addNode("test2", 1, 1, "3", "Tower", "INFO", "longName1", "shortName2");

		DB.addUserAccount("test243@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("interpreter123@gmail.com", "testPass", "EMT", "drew", "Shukla");
		DB.addSpecialUserType("helloherh@gmail.com", "testPass", "patient", "nupi", "Shukla");

		DB.addInternalPatientRequest(new InternalPatientObj(0, 1, "test", "test2", 2, 3, "department", "not severe", "she is in pain"));
	}

	@Test
	@DisplayName("testEditInternalPatientRequest")
	public void testEditInternalPatientRequest() {
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addNode("test2", 1, 1, "3", "Tower", "INFO", "longName1", "shortName2");

		DB.addUserAccount("test243@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("interpreter123@gmail.com", "testPass", "EMT", "drew", "Shukla");
		DB.addSpecialUserType("helloherh@gmail.com", "testPass", "patient", "nupi", "Shukla");
		DB.addSpecialUserType("idk@gmail.com", "testPass", "patient", "notme", "Shukla");

		DB.addInternalPatientRequest(new InternalPatientObj(0, 1, "test", "test2", 2, 3, "department", "not severe", "she is in pain"));
		int result = DB.editInternalPatientRequest(new InternalPatientObj(1, 1, null, "test", 4, 0, null, "hellloooooo", null));

		assertEquals(1, result);

	}

	@Test
	@DisplayName("testAddReligiousRequest")
	public void testAddReligiousRequest() {

		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test123@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("religiousPerson@gmail.com", "testPass", "religiousPerson", "drew", "Shukla");

		ReligiousRequestObj request = new ReligiousRequestObj(0,1, "test", 2, "Religion1", "Flying Spaghetti Monster");

		DB.addReligiousRequest(request);
	}

	@Test
	@DisplayName("testEditReligiousRequest")
	public void testEditReligiousRequest() {
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test2@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("interpreter@gmail.com", "testPass", "religiousPerson", "drew", "Shukla");

		ReligiousRequestObj request = new ReligiousRequestObj(0, 1, "test", 2, "Religion1", "Flying Spaghetti Monster");
		DB.addReligiousRequest(request);

		ReligiousRequestObj infoToChange = new ReligiousRequestObj(1,1, "test", 0,"Religion2", "description");
		assertEquals(1, DB.editReligiousRequest(infoToChange));
	}

	@Test
	@DisplayName("testGetAllNodes")
	public void testGetAllUsers() {


		DB.addSpecialUserType("adamj@gmail.com", "patient1", "patient", "Adam", "Jenkins");
		DB.addSpecialUserType("abbym@yahoo.com", "patient2", "patient", "Abby", "Mohamed");
		DB.addSpecialUserType("wesleya@gmail.com", "patient3", "patient", "Wesley", "Armstrong");
		DB.addSpecialUserType("travisc@yahoo.com", "patient4", "patient", "Travis", "Cook");
		DB.addSpecialUserType("gabriellar@gmail.com", "patient5", "patient", "Gabriella", "Reyes");


		UserManagement.User u1 = new UserManagement.User("patient", 1, "Adam", "Jenkins", "adamj@gmail.com");
		UserManagement.User u2 = new UserManagement.User("patient", 2, "Abby", "Mohamed", "abbym@yahoo.com");
		UserManagement.User u3 = new UserManagement.User("patient", 3, "Wesley", "Armstrong", "wesleya@gmail.com");
		UserManagement.User u4 = new UserManagement.User("patient", 4, "Travis", "Cook", "travisc@yahoo.com");
		UserManagement.User u5 = new UserManagement.User("patient", 5, "Gabriella", "Reyes", "gabriellar@gmail.com");


		ArrayList<UserManagement.User> testUserArray = new ArrayList<>();
		testUserArray.add(u1);
		testUserArray.add(u2);
		testUserArray.add(u3);
		testUserArray.add(u4);
		testUserArray.add(u5);

		ArrayList<UserManagement.User> userArray = UserAccountDB.getAllUsers();

		boolean allCorrect = true;
		boolean firstName = false;
		boolean lastName = false;
		boolean userID = false;
		boolean userType = false;
		boolean email = false;


		if (testUserArray.size() == userArray.size()) {
			for (int user = 0; user < userArray.size(); user++) {
				UserManagement.User returnedUser = userArray.get(user);
				UserManagement.User correctUser = testUserArray.get(user);
				if (returnedUser.getUserType().equals(correctUser.getUserType())) {
					userType = true;
				}
				if (returnedUser.getUserID() == correctUser.getUserID()) {
					userID = true;
				}
				if (returnedUser.getFirstName().equals(correctUser.getFirstName())) {
					firstName = true;
				}
				if (returnedUser.getLastName().equals(correctUser.getLastName())) {
					lastName = true;
				}
				if (returnedUser.getEmail().equals(correctUser.getEmail())) {
					email = true;
				}
				if (userType && userID && firstName && lastName && email) {
					allCorrect = false;
				}
			}
		} else {
			allCorrect = false;
		}

		assertTrue(allCorrect);

	}

	@Test
	@DisplayName("testGetAubonPainFeild")
	public void testGetAubonPainFeild() {
		DB.addAubonPainMenuItem(new AubonPainItem("foodImageURL", "foodItem", "$4732.23", "243 calories", "food description"));
		DB.addAubonPainMenuItem(new AubonPainItem("URLLLLL", "pancakes", "$23.45", "3290 calories", "yummmyyy"));
		DB.addAubonPainMenuItem(new AubonPainItem("foooooodddd URL", "soup", "$92.44", "3 calories", "goood"));

		ArrayList<String> correctFoodImages = new ArrayList<>();
		correctFoodImages.add("foodImageURL");
		correctFoodImages.add("URLLLLL");
		correctFoodImages.add("foooooodddd URL");

		ArrayList<String> returnedFoodImages = DB.getAubonPainFeild("foodImage");

		assertEquals(correctFoodImages, returnedFoodImages);
	}

	@Test
	@DisplayName("testGetAubonPainFeild2")
	public void testGetAubonPainFeild2() {
		DB.addAubonPainMenuItem(new AubonPainItem("foodImageURL", "foodItem", "$4732.23", "243 calories", "food description"));
		DB.addAubonPainMenuItem(new AubonPainItem(null, "pancakes", "$23.45", "3290 calories", "yummmyyy"));
		DB.addAubonPainMenuItem(new AubonPainItem("foooooodddd URL", "soup", "$92.44", "3 calories", "goood"));

		ArrayList<String> correctFoodImages = new ArrayList<>();
		correctFoodImages.add("foodImageURL");
		correctFoodImages.add(null);
		correctFoodImages.add("foooooodddd URL");

		ArrayList<String> returnedFoodImages = DB.getAubonPainFeild("foodImage");

		assertEquals(correctFoodImages, returnedFoodImages);
	}

	@Test
	@DisplayName("testGetAssigneeNames")
	public void testGetAssigneeNames() {

		DB.addSpecialUserType("adamj@gmail.com", "patient1", "patient", "Adam", "Jenkins");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		DB.addSpecialUserType("amyw@gmail.com", "floralPerson1", "floralPerson", "Amy", "Castaneda");

		ObservableList<String> assigneeNamesPatient = FXCollections.observableArrayList();
		assigneeNamesPatient.add("Adam Jenkins");

		ObservableList<String> assigneeNamesDoctor = FXCollections.observableArrayList();
		assigneeNamesDoctor.add("Bill Byrd");

		ObservableList<String> assigneeNamesFloral = FXCollections.observableArrayList();
		assigneeNamesFloral.add("Amy Castaneda");

		ObservableList<String> returnedPatient = RequestsDB.getAssigneeNames("patient");
		ObservableList<String> returnedDoctor = RequestsDB.getAssigneeNames("doctor");
		ObservableList<String> returnedFloral = RequestsDB.getAssigneeNames("floralPerson");

		assertEquals(assigneeNamesPatient, returnedPatient);
		assertEquals(assigneeNamesDoctor, returnedDoctor);
		assertEquals(assigneeNamesFloral, returnedFloral);
	}

	@Test
	@DisplayName("testGetAssigneeIDs")
	public void testGetAssigneeIDs() {

		DB.addSpecialUserType("adamj@gmail.com", "patient1", "patient", "Adam", "Jenkins");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		DB.addSpecialUserType("amyw@gmail.com", "floralPerson1", "floralPerson", "Amy", "Castaneda");

		ArrayList<Integer> assigneeIDsPatient = new ArrayList<>();
		assigneeIDsPatient.add(1);

		ArrayList<Integer> assigneeIDsDoctor = new ArrayList<>();
		assigneeIDsDoctor.add(2);

		ArrayList<Integer> assigneeIDsFloral = new ArrayList<>();
		assigneeIDsFloral.add(3);

		ArrayList<Integer> returnedPatientIDs = RequestsDB.getAssigneeIDs("patient");
		ArrayList<Integer> returnedDoctorIDs = RequestsDB.getAssigneeIDs("doctor");
		ArrayList<Integer> returnedFloralIDs = RequestsDB.getAssigneeIDs("floralPerson");

		assertEquals(assigneeIDsPatient, returnedPatientIDs);
		assertEquals(assigneeIDsDoctor, returnedDoctorIDs);
		assertEquals(assigneeIDsFloral, returnedFloralIDs);
	}

	@Test
	@DisplayName("COVIDTests")
	public void COVIDTests() {
		System.out.println("This is where all the COVID related database tests are");

		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addUserAccount("terry_reilly123@yahoo.com", "visitor2", "Terry", "Reilly");

		CovidSurveyObj covidSurveyObjSafe = new CovidSurveyObj(1, 1, false, false, false, false, true, "Safe");
		CovidSurveyObj covidSurveyObjNotSafe = new CovidSurveyObj(2, 2, true, true, true, true, false, "Unsafe");

		assertFalse(DB.isUserCovidSafe(1));
		assertFalse(DB.isUserCovidSafe(2));

		assertFalse(DB.filledCovidSurveyToday(1));
		assertFalse(DB.filledCovidSurveyToday(2));

		assertTrue(DB.submitCovidSurvey(covidSurveyObjSafe, 1));
		assertTrue(DB.submitCovidSurvey(covidSurveyObjNotSafe, 2));

		assertTrue(DB.filledCovidSurveyToday(1));
		assertTrue(DB.filledCovidSurveyToday(2));

		assertTrue(DB.isUserCovidSafe(1));
		assertFalse(DB.isUserCovidSafe(2));
	}

	@Test
	@DisplayName("ParkingTests")
	public void ParkingTests() {
		System.out.println("This is where all the Parking related database tests are");

		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addUserAccount("terry_reilly123@yahoo.com", "visitor2", "Terry", "Reilly");
		DB.addNode("ePARK00101", 381, 1725, "1", "Parking", "PARK", "Left Parking Lot Spot 001", "Parking Left 001");
		DB.addNode("test233", 22, 33, "1", "BTM", "WALK", "long", "short");

		assertNull(DB.whereDidIPark(1));
		assertNull(DB.whereDidIPark(2));

		assertTrue(DB.submitParkingSlot("ePARK00101", 1));
		assertTrue(DB.submitParkingSlot("test233", 2));   // node is not of type PARK, stores null in table

		assertEquals("ePARK00101", DB.whereDidIPark(1));
		assertNull(DB.whereDidIPark(2));
	}
	@Test
	@DisplayName("testGetEmail")
	public void testGetEmail() {
		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");

		String email = DB.getEmail(1);

		assertEquals("bellag@gmail.com", email);

	}


	@Test
	@DisplayName("testAddEntryRequest")
	public void testAddEntryRequest() {
		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");

		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");

		CovidSurveyObj covidSurveyObjSafe = new CovidSurveyObj(1, 1, false, false, false, false, true, "Needs to be reviewed");

		DB.addEntryRequest(covidSurveyObjSafe);

	}

	@Test
	@DisplayName("testEditEntryRequest")
	public void testEditEntryRequest() {
		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		CovidSurveyObj covidSurveyObjUnsafe = new CovidSurveyObj(1, 1, true, true, true, true, false, "Unsafe");

		DB.addEntryRequest(covidSurveyObjUnsafe);
		covidSurveyObjUnsafe = new CovidSurveyObj(1, 1, true, true, true, true, false, "Safe");

		DB.editEntryRequest(covidSurveyObjUnsafe);
	}

	@Test
	@DisplayName("testMarkAsCovidSafe")
	public void testMarkAsCovidSafe() {
		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");

		CovidSurveyObj covidSurveyObjSafe = new CovidSurveyObj(1, 1, true, true, true, true, false, "Unsafe");

		DB.addEntryRequest(covidSurveyObjSafe);

		int result = DB.markAsCovidSafe(1);

		assertEquals(1, result);

	}

	@Test
	@DisplayName("testMarkAsCovidRisk")
	public void testMarkAsCovidRisk() {
		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		CovidSurveyObj covidSurveyObjSafe = new CovidSurveyObj(1, 1, true, true, true, true, false, "Safe");

		DB.addEntryRequest(covidSurveyObjSafe);


		int result = DB.markAsCovidRisk(1);

		assertEquals(1, result);
	}

	@Test
	@DisplayName("testGetCovidSurveys")
	public void testGetCovidSurveys() {
		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addUserAccount("terry_reilly123@yahoo.com", "visitor2", "Terry", "Reilly");
		DB.addUserAccount("smiddle@outlook.com", "visitor3", "Sharon", "Middleton");

		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		DB.addSpecialUserType("ameliak@yahoo.com", "doctor02", "doctor", "Amelia", "Knight");
		DB.addSpecialUserType("simond@gmail.com", "doctor03", "doctor", "Simon", "Daniel");

		CovidSurveyObj covidSurveyObjSafe1 = new CovidSurveyObj(1, 4, false, false, false, false, true, "Needs to be reviewed");
		CovidSurveyObj covidSurveyObjSafe2 = new CovidSurveyObj(2, 5, false, false, false, false, true, "Needs to be reviewed");
		CovidSurveyObj covidSurveyObjSafe3 = new CovidSurveyObj(3, 6, false, false, false, false, true, "Needs to be reviewed");

		DB.addEntryRequest(covidSurveyObjSafe1);
		DB.addEntryRequest(covidSurveyObjSafe2);
		DB.addEntryRequest(covidSurveyObjSafe3);


//		ArrayList<String> result1 = new ArrayList<>();
//		result1.add("10101");
//		result1.add("1");
//		assertEquals(result1, DB.getMyAssignedRequestInfo("entryRequest", 3, "surveyResult"));

		ArrayList<CovidSurveyObj> resultCovidSurveys = DB.getCovidSurveys();

		ArrayList<CovidSurveyObj> actualCovidSurveys = new ArrayList<>();

		actualCovidSurveys.add(new CovidSurveyObj(1, 4, false, false, false, false, true, "Needs to be reviewed"));
		actualCovidSurveys.add(new CovidSurveyObj(2, 5, false, false, false, false, true, "Needs to be reviewed"));
		actualCovidSurveys.add(new CovidSurveyObj(3, 6, false, false, false, false, true, "Needs to be reviewed"));

		boolean allCorrect = true;
		boolean userID = false;
		boolean formNumber = false;
		boolean positiveTest = false;
		boolean symptoms = false;
		boolean closeContact = false;
		boolean quarantine = false;
		boolean noSymptoms = false;
		boolean status = false;

		if (resultCovidSurveys.size() == actualCovidSurveys.size()) {
			for (int survey = 0; survey < resultCovidSurveys.size(); survey++) {
				CovidSurveyObj returnedCovidSurveyObj = resultCovidSurveys.get(survey);
				CovidSurveyObj correctCovidSurveyObj = actualCovidSurveys.get(survey);
				if (returnedCovidSurveyObj.getUser().equals(correctCovidSurveyObj.getUser())) {
					userID = true;
				}
				if (returnedCovidSurveyObj.getFormNumber().equals(correctCovidSurveyObj.getFormNumber())) {
					formNumber = true;
				}
				if (returnedCovidSurveyObj.getPositiveTest() == correctCovidSurveyObj.getPositiveTest()) {
					positiveTest = true;
				}
				if (returnedCovidSurveyObj.getSymptoms() == correctCovidSurveyObj.getSymptoms()) {
					symptoms = true;
				}
				if (returnedCovidSurveyObj.getCloseContact() == correctCovidSurveyObj.getCloseContact()) {
					closeContact = true;
				}
				if (returnedCovidSurveyObj.getQuarantine() == correctCovidSurveyObj.getQuarantine()) {
					quarantine = true;
				}
				if (returnedCovidSurveyObj.getNoSymptoms() == correctCovidSurveyObj.getNoSymptoms()) {
					noSymptoms = true;
				}
				if (returnedCovidSurveyObj.getStatus().equals(correctCovidSurveyObj.getStatus())) {
					status = true;
				}
				if (userID && formNumber && positiveTest && symptoms && closeContact && quarantine && noSymptoms && !status) {
					allCorrect = false;
				}
			}
		} else {
			allCorrect = false;
		}

		assertTrue(allCorrect);
	}



}