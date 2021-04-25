package edu.wpi.TeamE;

import edu.wpi.TeamE.algorithms.Edge;
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.database.makeConnection;
import javafx.util.Pair;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
			//System.out.println("Tables were reset");
		} catch (Exception e) {
			//e.printStackTrace();
		}
		DB.createNodeTable();
		DB.createEdgeTable();
		DB.createUserAccountTable();
		DB.createRequestsTable();
		DB.createFloralRequestsTable();
		DB.createSanitationTable();
		DB.createExtTransportTable();
		DB.createMedDeliveryTable();
		DB.createSecurityServTable();
		DB.createAppointmentTable();
		//System.out.println("Tables were created");
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

		DB.addSanitationRequest(1, 2, "test", "Urine Cleanup", "description here", "Low", "Nupur Shukla");
	}

	@Test
	@DisplayName("testAddExternalPatientRequest")
	public void testAddExternalPatientRequest() {
		//('visitor', 'patient', 'doctor', 'admin', 'nurse', 'EMT', 'floralPerson', 'pharmacist', 'security', 'electrician', 'custodian')))";
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("EMT@gmail.com", "testPass", "EMT", "drew", "Shukla");

		DB.addExternalPatientRequest(1, 2, "test", "Ambulance", "severe", "123", "15 mins", "headache");
	}

	@Test
	@DisplayName("testAddMedicineRequest")
	public void testAddMedicineRequest() {
		//('visitor', 'patient', 'doctor', 'admin', 'nurse', 'EMT', 'floralPerson', 'pharmacist', 'security', 'electrician', 'custodian')))";
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("pharmacist@gmail.com", "testPass", "pharmacist", "drew", "Shukla");


		DB.addMedicineRequest(1, 2, "test", "drugs", 2, "100ml", "take once a day", "Nupur");
	}

	@Test
	@DisplayName("testSecurityRequest")
	public void testAddSecurityRequest() {

		//('visitor', 'patient', 'doctor', 'admin', 'nurse', 'EMT', 'floralPerson', 'pharmacist', 'security', 'electrician', 'custodian')))";
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("security@gmail.com", "testPass", "security", "drew", "Shukla");

		DB.addSecurityRequest(1, 2, "test", "low", "Low");
	}

	@Test
	@DisplayName("testAddFloralRequest")
	public void testAddFloralRequest() {
		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("floralPerson1@gmail.com", "testPass", "floralPerson", "drew", "Shukla");

		DB.addFloralRequest(1, 2, "test", "Nupur", "Roses", 1, "Tall", "feel better");

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


		DB.addExternalPatientRequest(1, 3, "test", "Ambulance", "severe", "123", "15 mins", "headache");
		DB.addExternalPatientRequest(1, 3, "test", "Ambulance", "severe", "123", "15 mins", "migraine");
		DB.addExternalPatientRequest(2, 3, "test", "Ambulance", "severe", "123", "15 mins", "migraine");
		DB.addFloralRequest(1, 4, "test", "Nupur", "Roses", 1, "Tall", "feel better");

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


		DB.addExternalPatientRequest(1, 3, "test", "Ambulance", "severe", "123", "15 mins", "headache");
		DB.addExternalPatientRequest(1, 3, "test", "Ambulance", "severe", "123", "15 mins", "migraine");
		DB.addExternalPatientRequest(2, 3, "test", "Ambulance", "severe", "123", "15 mins", "migraine");
		DB.addFloralRequest(1, 4, "test", "Nupur", "Roses", 1, "Tall", "feel better");

		ArrayList<String> returnedStatus;
		ArrayList<String> correctStatus = new ArrayList<>();
		correctStatus.add("inProgress");
		correctStatus.add("inProgress");
		correctStatus.add("inProgress");
		returnedStatus = DB.getMyCreatedRequestInfo("extTransport", -1, "requestStatus");
		assertEquals(correctStatus, returnedStatus);
	}

	@Test
	@DisplayName("testGetRequestStatus3 : No Data")
	public void testGetRequestStatus3() {

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


		DB.addExternalPatientRequest(1, 3, "test", "Ambulance", "severe", "123", "15 mins", "headache");
		DB.addExternalPatientRequest(2, 3, "test", "Ambulance", "severe", "123", "15 mins", "migraine");
		DB.addFloralRequest(1, 4, "test", "Nupur", "Roses", 1, "Tall", "feel better");

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


		DB.addExternalPatientRequest(1, 3, "test", "Ambulance", "severe", "123", "15 mins", "headache");
		DB.addExternalPatientRequest(2, 3, "test", "Ambulance", "severe", "123", "15 mins", "migraine");
		DB.addFloralRequest(1, 4, "test", "Nupur", "Roses", 1, "Tall", "feel better");

		ArrayList<String> returnedIDs;
		ArrayList<String> correctIDs = new ArrayList<>();
		correctIDs.add("1");
		correctIDs.add("2");
		returnedIDs = DB.getMyCreatedRequestInfo("extTransport", -55, "requestID");
		assertEquals(correctIDs, returnedIDs);
	}

	@Test
	@DisplayName("testGetRequestIDs3: No Data")
	public void testGetRequestIDs3() {

		ArrayList<String> returnedIDs;
		returnedIDs = DB.getMyCreatedRequestInfo("extTransport", -99, "requestID");
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

		DB.addMedicineRequest(1, 3, "test1", "drugs", 2, "100ml", "take once a day", "Nupur");
		DB.addMedicineRequest(1, 4, "test2", "drugs2", 3, "10ml", "take once a day", "Nupur");
		DB.addMedicineRequest(2, 5, "test3", "drugs3", 4, "1ml", "take once a day", "Nupur");

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

		DB.addMedicineRequest(1, 3, "test", "drugs", 2, "100ml", "take once a day", "Nupur");
		DB.addMedicineRequest(1, 4, "test2", "drugs2", 3, "10ml", "take once a day", "Nupur");
		DB.addMedicineRequest(2, 5, "test3", "drugs3", 4, "1ml", "take once a day", "Nupur");


		ArrayList<String> returnedAssignees = DB.getMyCreatedRequestInfo("medDelivery", -1, "assigneeID");
		ArrayList<String> correctAssignees = new ArrayList<>();
		correctAssignees.add("3");
		correctAssignees.add("4");
		correctAssignees.add("5");
		assertEquals(correctAssignees, returnedAssignees);
	}

	@Test
	@DisplayName("testGetRequestAssignees3: no data")
	public void testGetRequestAssignees3() {
		ArrayList<String> returnedAssignees = DB.getMyCreatedRequestInfo("medDelivery", -1, "assigneeID");
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

		DB.addMedicineRequest(1, 3, "test1", "drugs", 2, "100ml", "take once a day", "Nupur");
		DB.addMedicineRequest(1, 4, "test2", "drugs2", 3, "10ml", "take once a day", "Nupur");
		DB.addMedicineRequest(2, 3, "test3", "drugs3", 4, "1ml", "take once a day", "Nupur");

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

		DB.addMedicineRequest(1, 3, "test1", "drugs", 2, "100ml", "take once a day", "Nupur");
		DB.addMedicineRequest(1, 4, "test2", "drugs2", 3, "10ml", "take once a day", "Nupur");
		DB.addMedicineRequest(2, 3, "test3", "drugs3", 4, "1ml", "take once a day", "Nupur");

		ArrayList<String> returnedLocations = DB.getRequestLocations("medDelivery", -50);
		ArrayList<String> correctLocations = new ArrayList<>();
		correctLocations.add("long name #1");
		correctLocations.add("long name #2");
		correctLocations.add("long name #3");
		assertEquals(correctLocations, returnedLocations);
	}

	@Test
	@DisplayName("testGetRequestLocations3 : No Data")
	public void testGetRequestLocations3() {

		ArrayList<String> returnedLocations = DB.getRequestLocations("medDelivery", -1);
		assertEquals(0, returnedLocations.size());
	}


	@Test
	@DisplayName("testEditSanitationRequest")
	public void testEditSanitationRequest() {
		DB.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");
		DB.addSpecialUserType("custodian@gmail.com", "testPass", "custodian", "bob", "Shukla");

		DB.addSanitationRequest(1, 2, "test", "Urine Cleanup", "description here", "Low", "Nupur Shukla");

		assertEquals(1, DB.editSanitationRequest(1, "test", null, null, null, "hello test"));
	}

	@Test
	@DisplayName("testEditExternalPatientRequest")
	public void testEditExternalPatientRequest() {

		DB.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");
		DB.addSpecialUserType("EMT@gmail.com", "testPass", "EMT", "bob", "Shukla");

		DB.addExternalPatientRequest(1, 2, "test", "Ambulance", "severe", "123", "15 mins", "headache");

		assertEquals(1, DB.editExternalPatientRequest(1, "test", null, null, null, null, "15 mins"));
	}

	@Test
	@DisplayName("testEditMedicineRequest")
	public void testEditMedicineRequest() {

		DB.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");
		DB.addSpecialUserType("pharmacist@gmail.com", "testPass", "pharmacist", "bob", "Shukla");

		DB.addMedicineRequest(1, 2, "test", "drugs", 2, "100ml", "take once a day", "Nupur");

		assertEquals(1, DB.editMedicineRequest(1, "test", "Tylenol", null, null, "Take twice everyday", 0));
	}

	@Test
	@DisplayName("testEditFloralRequest")
	public void testEditFloralRequest() {

		DB.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");
		DB.addSpecialUserType("floralPerson@gmail.com", "testPass", "floralPerson", "bob", "Shukla");

		DB.addFloralRequest(1, 2, "test", "Nupur", "Roses", 1, "Tall", "feel better");

		assertEquals(1, DB.editFloralRequest(1, "test", "Ashley", "Tulips", null, null, null));
	}

	@Test
	@DisplayName("testEditSecurityRequest")
	public void testEditSecurityRequest() {

		DB.addNode("test", 0, 0, "1", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@email.com", "testPassword", "Testing", "Queen");
		DB.addSpecialUserType("security@gmail.com", "testPass", "security", "bob", "Shukla");

		DB.addSecurityRequest(1, 2, "test", "low", "Low");

		assertEquals(1, DB.editSecurityRequest(1, null, "high", "High"));
	}

	@Test
	@DisplayName("testChangeRequestStatus")
	public void testChangeRequestStatus() {

		DB.addNode("test", 0, 0, "2", "Tower", "INFO", "longName", "shortName");
		DB.addUserAccount("test@gmail.com", "testPass", "Nubia", "Shukla");
		DB.addSpecialUserType("pharmacist@gmail.com", "testPass", "pharmacist", "bob", "Shukla");

		DB.addMedicineRequest(1, 2, "test", "drugs", 2, "100ml", "take once a day", "Nupur");
		DB.addMedicineRequest(1, 2, "test", "drugs2", 3, "10ml", "take once a day", "Nupur");

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
	@DisplayName("testGetRequestLocations")
	public void testFunction() {

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


		DB.addExternalPatientRequest(27, 31, "EEXIT00101", "Ambulance", "High Severity", "12334567", "5 minutes", "Patient dropped down into a state of unconsciousness randomly at the store. Patient is still unconscious and unresponsive but has a pulse. No friends or family around during the incident. ");
		DB.addExternalPatientRequest(30, 32, "EEXIT00101", "Ambulance", "Low Severity", "4093380", "20 minutes", "Patient coming in with cut on right hand. Needs stitches. Bleeding is stable.");
		DB.addExternalPatientRequest(22, 33, "FDEPT00501", "Helicopter", "High Severity", "92017693", "10 minutes", "Car crash on the highway. 7 year old child in the backseat with no seatbelt on in critical condition. Blood pressure is low and has major trauma to the head.");
		DB.addExternalPatientRequest(20, 34, "FDEPT00501", "Helicopter", "High Severity", "93754789", "20 minutes", "Skier hit tree and lost consciousness. Has been unconscious for 30 minutes. Still has a pulse.");
		DB.addExternalPatientRequest(24, 35, "EEXIT00101", "Ambulance", "Medium Severity", "417592", "10 minutes", "Smoke inhalation due to a fire. No burns but difficult time breathing.");
		DB.addExternalPatientRequest(28, 36, "FDEPT00501", "Helicopter", "High Severity", "44888936", "15 minutes", "Major car crash on highway. Middle aged woman ejected from the passenger's seat. Awake and unresponsive and in critical condition");
		DB.addExternalPatientRequest(24, 37, "EEXIT00101", "Ambulance", "Medium Severity", "33337861", "7 minutes", "Patient passed out for 30 seconds. Is responsive and aware of their surroundings. Has no history of passing out.");
		DB.addExternalPatientRequest(27, 38, "EEXIT00101", "Ambulance", "Low Severity", "40003829", "10 minutes", "Relocating a patient with lung cancer from Mt.Auburn Hospital.");
		DB.addExternalPatientRequest(24, 39, "FDEPT00501", "Plane", "High Severity", "38739983", "12 hours", "Heart transplant organ in route");


		ArrayList<String> correctLongNames = new ArrayList<>();
		correctLongNames.add("Ambulance Parking Exit Floor 1");
		correctLongNames.add("Ambulance Parking Exit Floor 1");
		correctLongNames.add("Emergency Department");
		correctLongNames.add("Emergency Department");
		correctLongNames.add("Ambulance Parking Exit Floor 1");
		correctLongNames.add("Emergency Department");
		correctLongNames.add("Ambulance Parking Exit Floor 1");
		correctLongNames.add("Ambulance Parking Exit Floor 1");
		correctLongNames.add("Emergency Department");


		ArrayList<String> locationArray = DB.getRequestLocations("extTransport", -1);

		assertEquals(correctLongNames, locationArray);
	}

	@Test
	@DisplayName("testAddAppointment")
	public void testAddAppointment() {

		DB.addNode("ADEPT00101", 1401, 2628, "1", "BTM", "DEPT", "Neuroscience Waiting Room", "Neuro Waiting Room");

		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		int rowsAffected1 = DB.addAppointment(1, 400, 400, 2);
		int rowsAffected2 = DB.addAppointment(2, 400, 400, 2);

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

		DB.addAppointment(1, 400, 400, 2);
		DB.addAppointment(2, 400, 400, 2);


		int rowAffected = DB.editAppointment(1, 700, 700, null);
		int rowAffected2 = DB.editAppointment(2, 500, 500, 3);

		int totalRowAffected = rowAffected + rowAffected2;

		assertEquals(2, totalRowAffected);
	}


	@Test
	@DisplayName("testAddAppointment")
	public void testAddRemovedPatientAppointmentHistory() {

		DB.addNode("ADEPT00101", 1401, 2628, "1", "BTM", "DEPT", "Neuroscience Waiting Room", "Neuro Waiting Room");

		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");
		DB.addAppointment(1, 400, 400, 2);
		DB.addAppointment(2, 400, 400, 2);

		DB.addRemovedPatientAppointmentHistory(1);

	}

	@Test
	@DisplayName("testCancelAppointment")
	public void testCancelAppointment() {
		DB.addNode("ADEPT00101", 1401, 2628, "1", "BTM", "DEPT", "Neuroscience Waiting Room", "Neuro Waiting Room");

		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");

		DB.addAppointment(1, 400, 400, 2);

		int rowsAffected = DB.cancelAppointment(1);

		assertEquals(1, rowsAffected);
	}


	@Test
	@DisplayName("testCancelAppointment : multiple appointments")
	public void testCancelAppointment2() {
		DB.addNode("ADEPT00101", 1401, 2628, "1", "BTM", "DEPT", "Neuroscience Waiting Room", "Neuro Waiting Room");

		DB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		DB.addSpecialUserType("billb@gmail.com", "doctor01", "doctor", "Bill", "Byrd");

		DB.addAppointment(1, 400, 400, 2);
		DB.addAppointment(1, 500, 500, 2);
		DB.addAppointment(1, 600, 600, 2);

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
		String insertUser1 = "Insert Into useraccount Values (-1, 'superAdmin', 'superAdmin999', 'admin', 'Super', 'Admin', CURRENT TIMESTAMP)";
		String insertUser2 = "Insert Into useraccount Values (-99, 'admin', 'admin', 'admin', 'admin', 'admin', CURRENT TIMESTAMP)";
		String insertUser3 = "Insert Into useraccount Values (99999, 'staff', 'staff', 'doctor', 'staff', 'staff', CURRENT TIMESTAMP)";
		String insertUser4 = "Insert Into useraccount Values (10000, 'guest', 'guest', 'patient', 'guest', 'visitor', CURRENT TIMESTAMP)";
		try {
			Statement stmt = makeConnection.makeConnection().connection.createStatement();
			stmt.executeUpdate(insertUser1);
			stmt.executeUpdate(insertUser2);
			stmt.executeUpdate(insertUser3);
			stmt.executeUpdate(insertUser4);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
}