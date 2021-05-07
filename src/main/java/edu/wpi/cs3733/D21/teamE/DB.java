package edu.wpi.cs3733.D21.teamE;

import edu.wpi.cs3733.D21.teamE.database.*;
import edu.wpi.cs3733.D21.teamE.map.Edge;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.views.CovidSurveyObj;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.AubonPainItem;
import edu.wpi.cs3733.D21.teamE.database.*;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DB {

	// AppointmentDB:

	public static void createAllTables() {
		NodeDB.createNodeTable();
		EdgeDB.createEdgeTable();
		UserAccountDB.createUserAccountTable();
		RequestsDB.createRequestsTable();
		RequestsDB.createFloralRequestsTable();
		RequestsDB.createSanitationTable();
		RequestsDB.createExtTransportTable();
		RequestsDB.createMedDeliveryTable();
		RequestsDB.createSecurityServTable();
		appointmentDB.createAppointmentTable();
		RequestsDB.createLanguageRequestTable();
		RequestsDB.createLaundryRequestTable();
		RequestsDB.createMaintenanceRequestTable();
		RequestsDB.createFoodDeliveryTable();
		RequestsDB.createInternalPatientRequest();
		RequestsDB.createAubonPainMenuTable();
		RequestsDB.createReligionRequestTable();
		RequestsDB.createEntryRequestTable();
	}

	public static void createNodeTable() {
		NodeDB.createNodeTable();
	}

	/**
	 * creates an appointment and adds to the appointmentDB table
	 * @param patientID is the ID of the patient making the appointment
	 * @param startTime is when the appointment starts
	 * @param doctorID  is the doctor assigned to the appointment
	 * @return an int (0 if add fails, 1 if add succeeded)
	 */
	public static int addAppointment(int patientID, String startTime, String date, Integer doctorID) {
		return appointmentDB.addAppointment(patientID, startTime, date, doctorID);
	}

	/**
	 * increments appointmentID by 1 each time an appointment is made
	 * @return 0 if ID cannot be incremented, 1 if ID is incremented correctly
	 */
	public static int getMaxAppointmentID() {
		return appointmentDB.getMaxAppointmentID();
	}

	/**
	 * edits an appointment
	 * @param appointmentID is the ID of the appointment
	 * @param newStartTime  is the new start time of the appointment
	 * @param newDoctorID   is the new doctor assigned
	 * @return an int (0 if add fails, 1 if add succeeded)
	 */
	public static int editAppointment(int appointmentID, String newStartTime, String date, Integer newDoctorID) {
		return appointmentDB.editAppointment(appointmentID, newStartTime, date, newDoctorID);
	}

	/**
	 * cancels an appointment given the appointmentID
	 * @param appointmentID is the ID of the appointment
	 * @return an int (0 if add fails, 1 if add succeeded)
	 */
	public static int cancelAppointment(int appointmentID) {
		return appointmentDB.cancelAppointment(appointmentID);
	}

	// csvDB:

	/**
	 * Reads csv & inserts into table
	 * @param tableName name of the table that needs to be populated
	 * @param file      file that is going to be read
	 */
	public static void populateTable(String tableName, File file) {
		csvDB.populateTable(tableName, file);
	}

	/**
	 * Translates a table into a csv file which can be later returned to the user.
	 * @param tableName this is the table/data/information that will be translated into a csv file
	 */
	public static void getNewCSVFile(String tableName) {
		csvDB.getNewCSVFile(tableName);
	}

	/**
	 * saves patient information (called before deleting a patient and their history)
	 * @param patientID is the userID of the patient
	 */
	public static void addRemovedPatientAppointmentHistory(int patientID) {
		csvDB.addRemovedPatientAppointmentHistory(patientID);
	}

	// EdgeDB:

	/**
	 * Uses executes the SQL statements required to create the hasEdge table.
	 * This table has the attributes:
	 * - edgeID: this is a unique identifier for the edge connection. Every edge must contain an edgeID.
	 * - startNode: this is a nodeID in which the edge connection starts.
	 * - endNode: this is a nodeID in which the edge connection ends.
	 */
	public static void createEdgeTable() {
		EdgeDB.createEdgeTable();
	}

	public static void deleteEdgeTable() {
		EdgeDB.deleteEdgeTable();
	}

	/**
	 * Deletes edge(s) between the given two nodes, they can be in any order
	 * @return the amount of rows affected by executing this statement, should be 1 in this case, if there are two edges it returns 2
	 * if count == 1 || count == 2, edges have been deleted
	 * else no edges exist with inputted nodes
	 */
	public static int deleteEdge(String nodeID1, String nodeID2) {
		return EdgeDB.deleteEdge(nodeID1, nodeID2);
	}

	/**
	 * Acts as a trigger and calculates the length between two nodes that form an edge and add the value to the edge table
	 * @param startNode the node ID for the starting node in the edge
	 * @param endNode   the node ID for the ending node in the edge
	 */
	public static void addLength(String startNode, String endNode) {
		EdgeDB.addLength(startNode, endNode);
	}

	/**
	 * Adds an edge with said data to the database. Both startNode and endNode has to already exist in node table
	 * @param edgeID    this is a unique identifier for the edge connection. Every edge must contain an edgeID.
	 * @param startNode this is a nodeID in which the edge connection starts.
	 * @param endNode   this is a nodeID in which the edge connection ends.
	 * @return the amount of rows affected by executing this statement, should be 1 in this case
	 */
	public static int addEdge(String edgeID, String startNode, String endNode) {
		return EdgeDB.addEdge(edgeID, startNode, endNode);
	}

	/**
	 * modifies an edge, updating the DB, returning 0 or 1 depending on whether operation was successful
	 * @return int (0 if node couldn't be added, 1 if the node was added successfully)
	 * need to check that both startNode and endNode already exist in node table
	 */
	public static int modifyEdge(String edgeID, String startNode, String endNode) {
		return EdgeDB.modifyEdge(edgeID, startNode, endNode);
	}

	/**
	 * gets all edges and each edge's attribute
	 * @return ArrayList<Edge>
	 */
	public static ArrayList<Edge> getAllEdges() {
		return EdgeDB.getAllEdges();
	}

	/**
	 * gets edge information for all edges containing given nodeID
	 * @return Pair<Integer, String> map with edge information
	 */
	public static ArrayList<Pair<Float, String>> getEdgeInfo(String nodeID) {
		return EdgeDB.getEdgeInfo(nodeID);
	}

	// NodeDB:


	/**
	 * matches the nodeID to a node and deletes it from DB
	 * @return the amount of rows affected by executing this statement, should be 1 in this case
	 */
	public static int deleteNode(String nodeID) {
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
	 * @return int (0 if node couldn't be added, 1 if the node was added successfully)
	 */
	public static int modifyNode(String nodeID, Integer xCoord, Integer yCoord, String floor, String building, String nodeType, String longName, String shortName) {
		return NodeDB.modifyNode(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName);
	}

	public static ArrayList<Node> getAllNodesByType(String type) {
		return NodeDB.getAllNodesByType(type);
	}

	/**
	 * gets all Nodes (each row in node table)
	 * @return ArrayList of Node objects
	 * need Node constructor from UI/UX team
	 */
	public static ArrayList<Node> getAllNodes() {
		return NodeDB.getAllNodes();
	}

	/**
	 * gets a node's all attributes given nodeID
	 * @return a Node object with the matching nodeID
	 */
	public static Node getNodeInfo(String nodeID) {
		return NodeDB.getNodeInfo(nodeID);
	}

	/**
	 * todo
	 */
	public static ObservableList<String> getAllNodeLongNames() {
		return NodeDB.getAllNodeLongNames();
	}

	/**
	 * counts the number of nodes already in the database of the given type and on the given floor that starts with the given teamNum
	 * @param teamNum be a String like "E"
	 * @param Floor   is a String like "L2"
	 * @param Type    is the nodeType of the node, like "ELEV"
	 * @return is how many nodes are already in the database given the params
	 */
	public static int countNodeTypeOnFloor(String teamNum, String Floor, String Type) {
		return NodeDB.countNodeTypeOnFloor(teamNum, Floor, Type);
	}

	/**
	 * gets list of nodeIDS
	 * @return String[] of nodeIDs
	 */
	public static ArrayList<String> getListOfNodeIDS() {
		return NodeDB.getListOfNodeIDS();
	}

	public static void deleteNodeTable() {
		NodeDB.deleteNodeTable();
	}


	/**
	 * Given a NodeID, gives the xCoord, yCoord, Floor and Type of that node from Nodes
	 * @param nodeID is the nodeID of the nodes you want info from
	 * @return a Node with only xCoord, yCoord, floor and nodeType not null
	 */
	public static Node getNodeLite(String nodeID) {
		return NodeDB.getNodeLite(nodeID);
	}

	/**
	 * Gets all node long names for a specified FLOOR column value.
	 * @param floorName the value to check for in FLOOR column
	 * @return ObservableList of node long names.
	 */
	public static ObservableList<String> getAllNodeLongNamesByFloor(String floorName) {
		return NodeDB.getAllNodeLongNamesByFloor(floorName);
	}

	/**
	 * Gets a list of the nodeIDs of all of the nodes that are on the given floor
	 * @param floorName the name of the floor that the nodes will be selected on
	 */
	public static ArrayList<String> getListOfNodeIDSByFloor(String floorName) {
		return NodeDB.getListOfNodeIDSByFloor(floorName);
	}

	/**
	 * gets all Nodes that have the given FLOOR value
	 * @param floorName the value to check for in FLOOR column
	 * @return ArrayList of Node objects
	 */
	public static ArrayList<Node> getAllNodesByFloor(String floorName) {
		return NodeDB.getAllNodesByFloor(floorName);
	}


	// RequestDB:

	// Creating Tables:


	/**
	 * This parses through the Abon Pain website at BH and adds each item, its image, calories, price, and
	 * description to the aubonPainMenu table
	 * The link to the website being read is: https://order.aubonpain.com/menu/brigham-womens-hospital
	 */
	public static void populateAbonPainTable() {
		RequestsDB.populateAbonPainTable();
	}


	// Adding To Tables:

	public static void addRequest(int userID, int assigneeID, String requestType) {
		RequestsDB.addRequest(userID, assigneeID, requestType);
	}

	/**
	 * This adds a sanitation services form to the table specific for it
	 * //@param form this is the form being added to the table
	 */
	public static void addSanitationRequest(int userID, int assigneeID, String roomID, String sanitationType, String description, String urgency, String signature) {
		RequestsDB.addSanitationRequest(userID, assigneeID, roomID, sanitationType, description, urgency, signature);
	}

	/**
	 * This function needs to add a external patient form to the table for external patient forms
	 * //@param form this is the form that we will create and send to the database
	 */
	public static void addExternalPatientRequest(int userID, int assigneeID, String roomID, String requestType, String severity, String patientID, String ETA, String bloodPressure, String temperature, String oxygenLevel, String description) {
		RequestsDB.addExternalPatientRequest(userID, assigneeID, roomID, requestType, severity, patientID, ETA, bloodPressure, temperature, oxygenLevel, description);
	}

	/**
	 * This adds a floral request to the database that the user is making
	 * @param userID        this is the username that the user uses to log into the account
	 * @param assigneeID    this is the ID of the assigned user
	 * @param RoomNodeID    this is the nodeID/room the user is sending the request to
	 * @param recipientName this is the name of the individual they want the flowers to be addressed to
	 * @param flowerType    this is the type of flowers that the user wants to request
	 * @param flowerAmount  this the number/quantity of flowers that the user is requesting
	 * @param vaseType      this is the type of vase the user wants the flowers to be delivered in
	 * @param message       this is a specific detailed message that the user can have delivered with the flowers or an instruction message
	 */
	public static void addFloralRequest(int userID, int assigneeID, String RoomNodeID, String recipientName, String flowerType, int flowerAmount, String vaseType, String arrangement, String stuffedAnimal, String chocolate, String message) {
		RequestsDB.addFloralRequest(userID, assigneeID, RoomNodeID, recipientName, flowerType, flowerAmount, vaseType, arrangement, stuffedAnimal, chocolate, message);
	}

	/**
	 * This adds a medicine request form to the table for medicine request forms
	 * // @param form this is the form being added
	 */
	public static void addMedicineRequest(int userID, int assigneeID, String roomID, String medicineName, int quantity, String dosage, String specialInstructions, String signature) {
		RequestsDB.addMedicineRequest(userID, assigneeID, roomID, medicineName, quantity, dosage, specialInstructions, signature);
	}

	/**
	 * This adds a security form to the table for security service form
	 * // @param form this is the form added to the table
	 */
	public static void addSecurityRequest(int userID, int assigneeID, String roomID, String level, String urgency) {
		RequestsDB.addSecurityRequest(userID, assigneeID, roomID, level, urgency);
	}

	/**
	 * @param userID       ID of the user
	 * @param assigneeID   ID of the assigned user who will complete this task
	 * @param roomID       nodeID of the user
	 * @param languageType type of language being requested
	 * @param description  detailed description of request
	 */
	public static void addLanguageRequest(int userID, int assigneeID, String roomID, String languageType, String description) {
		RequestsDB.addLanguageRequest(userID, assigneeID, roomID, languageType, description);
	}

	/**
	 * @param userID         ID of the user
	 * @param roomID         nodeID of the user
	 * @param assigneeID     ID of the assigned user who will complete this task
	 * @param washLoadAmount amount of loads needed to wash
	 * @param dryLoadAmount  amount of loads needed to dry
	 * @param description    detailed description of request
	 */
	public static void addLaundryRequest(int userID, String roomID, int assigneeID, String washLoadAmount, String dryLoadAmount, String description) {
		RequestsDB.addLaundryRequest(userID, roomID, assigneeID, washLoadAmount, dryLoadAmount, description);
	}

	/**
	 * @param userID      ID of the user
	 * @param roomID      nodeID of the user
	 * @param assigneeID  ID of the assigned user who will complete this task
	 * @param type        is the type of maintenance required
	 * @param severity    is how severe the situation is
	 * @param ETA         time taken to complete the request
	 * @param description detailed description of request
	 */
	public static void addMaintenanceRequest(int userID, String roomID, int assigneeID, String type, String severity, String ETA, String description) {
		RequestsDB.addMaintenanceRequest(userID, roomID, assigneeID, type, severity, ETA, description);
	}

	/**
	 * adds a request for food delivery
	 * @param userID           ID of the user
	 * @param roomID           nodeID of the user
	 * @param assigneeID       ID of the assigned user who will complete this task
	 * @param dietRestrictions any restrictions the user has diet wise
	 * @param allergies        any allergies the user has
	 * @param foodItem         the food item choice made by the user
	 * @param foodQuantity     the quantity of the food item the user wants
	 * @param beverageItem     the beverage item choice made by the user
	 * @param beverageQuantity the quantity of the beverage item the user wants
	 */
	public static void addFoodDeliveryRequest(int userID, String roomID, int assigneeID, String dietRestrictions, String allergies, String foodItem, int foodQuantity, String beverageItem, int beverageQuantity) {
		RequestsDB.addFoodDeliveryRequest(userID, roomID, assigneeID, dietRestrictions, allergies, foodItem, foodQuantity, beverageItem, beverageQuantity);
	}

	public static void addInternalPatientRequest(int userID, String pickUpLocation, String dropOffLocation, int assigneeID, int patientID, String department, String severity, String description) {
		RequestsDB.addInternalPatientRequest(userID, pickUpLocation, dropOffLocation, assigneeID, patientID, department, severity, description);
	}

	/**
	 * adds a menuItem to the aubonPainMenu database table
	 * @param foodImage       this is the url to an image of the item
	 * @param foodItem        this is the item itself (this is unique and is used as an identifier)
	 * @param foodPrice       this is the price of the foodItem
	 * @param foodCalories    this is the number of calories the food item has
	 * @param foodDescription this is a description of the food item
	 */
	public static void addAubonPainMenuItem(String foodImage, String foodItem, String foodPrice, String foodCalories, String foodDescription) {
		RequestsDB.addAubonPainMenuItem(foodImage, foodItem, foodPrice, foodCalories, foodDescription);
	}

	/**
	 * add a religious request
	 * @param roomID       where the request takes place
	 * @param religionType the kind of the religion that request is requesting
	 * @param description  some text to further describe the request
	 */
	public static void addReligiousRequest(int userID, String roomID, int assigneeID, String religionType, String description) {
		RequestsDB.addReligiousRequest(userID, roomID, assigneeID, religionType, description);
	}

	/**
	 * This adds a entry request form to the table
	 */
	public static void addEntryRequest(CovidSurveyObj covidSurveyObj) {

		RequestsDB.addEntryRequest(covidSurveyObj);
	}

	// Editing Tables:

	/**
	 * This edits a Sanitation Services form that is already in the database
	 * @param requestID      the ID that specifies which sanitation form that is being edited
	 * @param description    the new description that the user is using to update their form
	 * @param roomID         the new node/room/location the user is assigning this request to
	 * @param sanitationType the new type of sanitation that the user is changing their request to
	 * @param urgency        the new urgency that the user is changing in their request
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editSanitationRequest(int requestID, String roomID, String sanitationType, String description, String urgency, String signature) {
		return RequestsDB.editSanitationRequest(requestID, roomID, sanitationType, description, urgency, signature);
	}

	/**
	 * This edits a External Transport Services form that is already in the database
	 * @param requestID   the ID that specifies which external transfer form that is being edited
	 * @param roomID      this is the string used to update the hospital field
	 * @param requestType this is the string used to update the type
	 * @param severity    this is the string used to update the severity
	 * @param patientID   this is the string used to update patientID
	 * @param description this is the string used to update the description
	 * @param ETA         this is the string used to update the eta
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editExternalPatientRequest(int requestID, String roomID, String requestType, String severity, String patientID, String ETA, String bloodPressure, String temperature, String oxygenLevel, String description) {
		return RequestsDB.editExternalPatientRequest(requestID, roomID, requestType, severity, patientID, ETA, bloodPressure, temperature, oxygenLevel, description);
	}

	/**
	 * This edits a floral request form that is already in the database
	 * @param requestID    the ID that specifies which external transfer form that is being edited
	 * @param roomID       the new node/room/location the user is assigning this request to
	 * @param flowerType   the type of flower the user wants to change their request to
	 * @param flowerAmount the new quantity of flowers the user wants to change their request to
	 * @param vaseType     the new vase type the user wants to change their request to
	 * @param message      the new message containing either instructions or to the recipient the user wants to change
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editFloralRequest(int requestID, String roomID, String recipientName, String flowerType, Integer flowerAmount, String vaseType, String arrangement, String stuffedAnimal, String chocolate, String message) {
		return RequestsDB.editFloralRequest(requestID, roomID, recipientName, flowerType, flowerAmount, vaseType, arrangement, stuffedAnimal, chocolate, message);
	}

	/**
	 * This function edits a current request for medicine delivery with the information below for a request already in the database
	 * @param requestID           the ID that specifies which external transfer form that is being edited
	 * @param roomID              the new node/room/location the user is assigning this request to
	 * @param medicineName        this is the name of the medicine the user is changing the request to
	 * @param quantity            this is the number of pills the user is changing the request to
	 * @param dosage              this is the dosage (ml or mg) the user is changing the request to
	 * @param specialInstructions this is the new special instructions the user is requesting
	 * @param assigneeID          this is the userID of the a new employee or administrator that will be fulfilling the request.
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editMedicineRequest(int requestID, String roomID, String medicineName, Integer quantity, String dosage, String specialInstructions, int assigneeID) {
		return RequestsDB.editMedicineRequest(requestID, roomID, medicineName, quantity, dosage, specialInstructions, assigneeID);
	}

	/**
	 * This edits a security form already within the database
	 * @param requestID the ID that specifies which external transfer form that is being edited
	 * @param roomID    the new node/room/location the user is assigning this request to
	 * @param level     this is the info to update levelOfSecurity
	 * @param urgency   this is the info to update levelOfUrgency
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editSecurityRequest(int requestID, String roomID, String level, String urgency) {
		return RequestsDB.editSecurityRequest(requestID, roomID, level, urgency);
	}


	/**
	 * Can change the assigneeID or the request status to any request
	 * @param requestID     is the generated ID of the request
	 * @param assigneeID    is the assignee's ID that you want to change it to
	 * @param requestStatus is the status that you want to change it to
	 * @return a 1 if one line changed successfully, and 0 or other numbers for failure
	 */
	public static int editRequests(int requestID, int assigneeID, String requestStatus) {
		return RequestsDB.editRequests(requestID, assigneeID, requestStatus);
	}

	/**
	 * @param requestID    is the generated ID of the request
	 * @param roomID       the new node/room/location the user is assigning this request to
	 * @param languageType is the new language type being requested by the user
	 * @param description  is an edited detailed description
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editLanguageRequest(int requestID, String roomID, String languageType, String description) {
		return RequestsDB.editLanguageRequest(requestID, roomID, languageType, description);
	}

	/**
	 * @param requestID      is the generated ID of the request
	 * @param roomID         the new node/room/location the user is assigning this request to
	 * @param washLoadAmount is new amount of loads to be washed
	 * @param dryLoadAmount  is new amount of loads to be dried
	 * @param description    is an edited detailed description
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editLaundryRequest(int requestID, String roomID, String washLoadAmount, String dryLoadAmount, String description) {
		return RequestsDB.editLaundryRequest(requestID, roomID, washLoadAmount, dryLoadAmount, description);
	}

	/**
	 * @param requestID   is the generated ID of the request
	 * @param roomID      the new node/room/location the user is assigning this request to
	 * @param type        is the new type of maintenance request
	 * @param severity    is the new severity of the situation
	 * @param ETA         is the new estimated time
	 * @param description is an edited detailed description
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editMaintenanceRequest(int requestID, String roomID, String type, String severity, String ETA, String description) {
		return RequestsDB.editMaintenanceRequest(requestID, roomID, type, severity, ETA, description);
	}

	/**
	 * @param requestID        is the generated ID of the request
	 * @param roomID           the new node/room/location the user is assigning this request to
	 * @param dietRestrictions is the edited restrictions of the user in terms of diet
	 * @param allergies        is the edited allergies the user has
	 * @param food             is the new food the user requests
	 * @param beverage         is the new beverage the user requests
	 * @param description      is an edited detailed description
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editFoodDeliveryRequest(int requestID, String roomID, String dietRestrictions, String allergies, String food, String beverage, String description) {
		return RequestsDB.editFoodDeliveryRequest(requestID, roomID, dietRestrictions, allergies, food, beverage, description);
	}

	public static int editInternalPatientRequest(int requestID, String pickUpLocation, String dropOffLocation, int patientID, String department, String severity, String description) {
		return RequestsDB.editInternalPatientRequest(requestID, pickUpLocation, dropOffLocation, patientID, department, severity, description);
	}

	/**
	 * edit a religious request
	 * @param roomID       where the request takes place
	 * @param religionType the kind of the religion that request is requesting
	 * @param description  some text to further describe the request
	 */
	public static int editReligiousRequest(int requestID, String roomID, String religionType, String description) {
		return RequestsDB.editReligiousRequest(requestID, roomID, religionType, description);
	}


	public static int editEntryRequest(CovidSurveyObj covidSurveyObj) {
		return RequestsDB.editEntryRequest(covidSurveyObj);
	}

		// Querying Tables:

	/**
	 * Gets a list of all the "assigneeIDs", "requestIDs", or "requestStatus" from the requests with the given type done by the given userID
	 * @param tableName this is the name of the table that we are getting the info from
	 * @param userID    this is the ID of the user who made the request
	 * @param infoType  this is the type of information that is being retrieved
	 * @return an ArrayList<String> with the desired info
	 */
	public static ArrayList<String> getMyCreatedRequestInfo(String tableName, int userID, String infoType) {
		return RequestsDB.getMyCreatedRequestInfo(tableName, userID, infoType);
	}

	/**
	 * Gets a list of all the "creatorIDs", "requestIDs", or "requestStatus" from the requests with the given type assigned to the given userID
	 * @param tableName this is the name of the table that we are getting the info from
	 * @param userID    this is the ID of the user who made the request
	 * @param infoType  this is the type of information that is being retrieved
	 * @return an ArrayList<String> with the desired info
	 */
	public static ArrayList<String> getMyAssignedRequestInfo(String tableName, int userID, String infoType) {
		return RequestsDB.getMyAssignedRequestInfo(tableName, userID, infoType);
	}

	/**
	 * Gets a list of all the longNames for the location from the given tableName
	 * @param tableType this is the name of the table that we are getting the requestIDs from
	 * @return a list of all longNames for the location for all of the requests
	 */
	public static ArrayList<String> getRequestLocations(String tableType, int userID) {
		return RequestsDB.getRequestLocations(tableType, userID);
	}

	/**
	 * gets a list of available assignees based on the userType given
	 * @param givenUserType is type of user (i.e. "nurse", "EMT", "doctor", etc.)
	 * @return a HashMap of possible assignees for the specific request type
	 */
	public static HashMap<Integer, String> getSelectableAssignees(String givenUserType) {
		return RequestsDB.getSelectableAssignees(givenUserType);
	}

	/**
	 * Gets a lits of all the menu items from aubon pain
	 * @return list of AubonPainItem that are in the aubonPainMenu database table
	 */
	public static ArrayList<AubonPainItem> getAubonPanItems() {
		return RequestsDB.getAubonPanItems();
	}

	/**
	 * Used to get a list of info from a given column name in the aubonPainMenu table
	 * @param column this is the name of the column the information is extracted from
	 * @return a list of the given information
	 */
	public static ArrayList<String> getAubonPainFeild(String column) {
		return RequestsDB.getAubonPainFeild(column);
	}






	// UserAccountDB:


	/**
	 * This is allows a visitor to create a user account giving them more access to the certain requests available
	 * @param email     this is the user's email that is connected to the account the are trying to create
	 * @param password  this is a password that the user will use to log into the account
	 * @param firstName this is the user's first name that is associated with the account
	 * @param lastName  this is the user's last name that is associated with the account
	 * // TODO: 4/27/21 Return success or fail status?
	 */
	public static void addUserAccount(String email, String password, String firstName, String lastName) {
		UserAccountDB.addUserAccount(email, password, firstName, lastName);
	}

	/**
	 * This is allows an administrator or someone with access to the database to be able to create a user account
	 * with more permissions giving them more access to the certain requests available. This is function should not
	 * be used while the app is being run. This function can not be used to add a working admin account.
	 * @param email     this is the user's email that is connected to the account the are trying to create
	 * @param password  this is a password that the user will use to log into the account
	 * @param userType  this is the type of account that the individual is being assigned to
	 * @param firstName this is the user's first name that is associated with the account
	 * @param lastName  this is the user's last name that is associated with the account
	 */
	public static void addSpecialUserType(String email, String password, String userType, String firstName, String lastName) {
		UserAccountDB.addSpecialUserType(email, password, userType, firstName, lastName);
	}

	/**
	 * This is allows an administrator or someone with access to the database to be able to edit a user account
	 * with more permissions giving them more access to the certain requests available. This is function should not
	 * be used while the app is being run.
	 * @param email     this is the user's email that is connected to the account the are trying to edit
	 * @param password  this is a password that the user will use to log into the account
	 * @param userType  this is the type of account that the individual is being assigned to
	 * @param firstName this is the user's first name that is associated with the account
	 * @param lastName  this is the user's last name that is associated with the account
	 * // TODO: 4/27/21 JavaDoc return explanation, also, consider bool return?
	 */
	public static int editUserAccount(int userID, String email, String password, String userType, String firstName, String lastName) {
		return UserAccountDB.editUserAccount(userID, email, password, userType, firstName, lastName);
	}

	/**
	 * This is allows an administrator or someone with access to the database to be able to edit a user account
	 * with more permissions giving them more access to the certain requests available. This is function should not
	 * be used while the app is being run.
	 * @param userID this is the ID of the user the admin is trying to delete
	 */
	public static int deleteUserAccount(int userID) {
		csvDB.addRemovedPatientAppointmentHistory(userID);
		return UserAccountDB.deleteUserAccount(userID);
	}

	/**
	 * Function for logging a user in with their unique email and password
	 * @param email    is the user's entered email
	 * @param password is the user's entered password
	 * @return 0 when the credentials does not match with any user in the database, and returns the userID otherwise
	 */
	public static int userLogin(String email, String password) {
		return UserAccountDB.userLogin(email, password);
	}

	public static String getUserType(int userID) {
		return UserAccountDB.getUserType(userID);
	}

	public static ObservableList<String> getAssigneeNames(String givenUserType) {
		return RequestsDB.getAssigneeNames(givenUserType);
	}

	/**
	 * Submits a Covid Survey to the server
	 * @param covidSurveyObj is the result int that we are submitting
	 * @param userID        is the user's ID that we are submitting
	 * @return true if successfully changed one row, false otherwise
	 */
	public static boolean submitCovidSurvey(CovidSurveyObj covidSurveyObj, int userID) {
		return UserAccountDB.submitCovidSurvey(covidSurveyObj, userID);
	}

	public static ArrayList<Integer> getAssigneeIDs(String givenUserType) {
		return RequestsDB.getAssigneeIDs(givenUserType);
	}

	/**
	 * Checks if a user have a unsafe Covid survey
	 * @param userID is the user's ID that we are checking
	 * @return true if user has a safe survey, false if user has a dangerous survey
	 */
	public static boolean isUserCovidSafe(int userID) {
		return UserAccountDB.isUserCovidSafe(userID);
	}

	public static String getEmail(int userID) {
		return RequestsDB.getEmail(userID);
	}
	/**
	 * Checks if a user have filled their COVID survey today
	 * @param userID is the user's ID that we are checking
	 * @return true if user has filled a survey today, false if user did not fill a survey today
	 */
	public static boolean filledCovidSurveyToday(int userID) {
		return UserAccountDB.filledCovidSurveyToday(userID);
	}

	public static String getUserName(int userID) {
		return UserAccountDB.getUserName(userID);
	}
	/**
	 * Submits a Parking Slot to the server
	 * @param nodeID is the result nodeID that we are submitting
	 * @param userID is the user's ID that we are submitting
	 * @return true if successfully changed one row, false otherwise
	 */
	public static boolean submitParkingSlot(String nodeID, int userID) {
		return UserAccountDB.submitParkingSlot(nodeID, userID);
	}

	/**
	 * Get where the user parked
	 * @param userID is the user's ID that we are checking
	 * @return the node where the user parked, null if not exist
	 */
	public static String whereDidIPark(int userID) {
		return UserAccountDB.whereDidIPark(userID);
	}

	//This should return the list of information within the table for covid surveys as a list of covid survey objects
	public static ArrayList<CovidSurveyObj> getCovidSurveys() {
		return RequestsDB.getCovidSurveys();
	}

	//This should mark a survey within the table as safe for entry
	public static int markAsCovidSafe(int formNumber) {
		return RequestsDB.markAsCovidSafe(formNumber);
	}

	//This should mark a survey within the table as unsafe for entry
	public static int markAsCovidRisk(int formNumber) {
		return RequestsDB.markAsCovidRisk(formNumber);
	}

	public static int updateUserAccountCovidStatus(int userID, String status) {
		return RequestsDB.updateUserAccountCovidStatus(userID, status);
	}

	public static boolean isUserCovidRisk(int userID) {
		return UserAccountDB.isUserCovidRisk(userID);
	}

	public static boolean isUserCovidUnmarked(int userID) {
		return UserAccountDB.isUserCovidUnmarked(userID);
	}

}