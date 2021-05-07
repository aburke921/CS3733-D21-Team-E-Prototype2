package edu.wpi.cs3733.D21.teamE.database;

import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.views.CovidSurveyObj;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.AubonPainItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class RequestsDB {

	static Connection connection = makeConnection.makeConnection().connection;

// CREATING TABLES:::
// CREATING TABLES:::
// CREATING TABLES:::
// CREATING TABLES:::
// CREATING TABLES:::
// CREATING TABLES:::
// CREATING TABLES:::

	/**
	 * Uses executes the SQL statements required to create the requests table.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - creatorID: this is the username of the user who created the request.
	 * - creationTime: this is a time stamp that is added to the request at the moment it is made.
	 * - requestType: this is the type of request that the user is making. The valid options are: "floral", "medDelivery", "sanitation", "security", "extTransport", 'languageRequest' 'laundryRequest' 'maintenanceRequest' 'foodDelivery' 'internalPatientRequest'
	 * - requestStatus: this is the state in which the request is being processed. The valid options are: "complete", "canceled", "inProgress".
	 * - assigneeID: this is the person who is assigned to the request
	 */
	public static void createRequestsTable() {

		String query = "Create Table requests(" +
				"requestID     int Primary Key, " +
				"creatorID     int References useraccount On Delete Cascade," +
				"creationTime  timestamp, " +
				"requestType   varchar(31), " +
				"requestStatus varchar(10), " +
				"assigneeID    int References useraccount On Delete Cascade," +
				"Constraint requestTypeLimit Check (requestType In ('floral', 'medDelivery', 'sanitation', 'security', 'extTransport', 'internalPatientRequest', 'languageRequest', 'laundryRequest', 'maintenanceRequest', 'foodDelivery', 'religiousRequest', 'entryRequest')), " +
				"Constraint requestStatusLimit Check (requestStatus In ('complete', 'canceled', 'inProgress')))";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating requests table");
		}
	}

	/**
	 * Uses executes the SQL statements required to create a floralRequests table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user is sending the request to.
	 * - recipientName: this is the name of the individual they want the flowers to be addressed to
	 * - flowerType: this is the type of flowers that the user wants to request. The valid options are: 'Roses', 'Tulips', 'Carnations', 'Assortment'
	 * - flowerAmount: this the number/quantity of flowers that the user is requesting. The valid options are: 1, 6, 12
	 * - vaseType: this is the type of vase the user wants the flowers to be delivered in. The valid options are: 'Round', 'Square', 'Tall', 'None'
	 * - message: this is a specific detailed message that the user can have delivered with the flowers or an instruction message
	 * *                for whoever is fufilling the request
	 */
	public static void createFloralRequestsTable() {

		String query = "Create Table floralRequests( " +
				"requestID     int Primary Key References requests On Delete Cascade, " +
				"roomID        varchar(31) References node On Delete Cascade, " +
				"recipientName varchar(31), " +
				"flowerType    varchar(31), " +
				"flowerAmount  int, " +
				"vaseType      varchar(31), " +
				"arrangement varchar(31), " +
				"stuffedAnimal varchar(31), " +
				"chocolate varchar(31), " +
				"message       varchar(5000), " +
				"Constraint flowerTypeLimit Check (flowerType In ('Roses', 'Tulips', 'Carnations', 'Assortment')), " +
				"Constraint flowerAmountLimit Check (flowerAmount In (1, 6, 12)), " +
				"Constraint vaseTypeLimit Check (vaseType In ('Round', 'Square', 'Tall', 'None')))";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating floralRequests table");
		}
	}

	/**
	 * Uses executes the SQL statements required to create a sanitationRequest table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user is sending the request to.
	 * - signature: this the signature (name in print) of the user who is filling out the request
	 * - description: this is any description the user who is filling out the request wants to provide for the person who will be completing the request
	 * - sanitationType: this is the type of sanitation/cleanup the user is requesting to be delt with. The valid options are: "Urine Cleanup",
	 * "Feces Cleanup", "Preparation Cleanup", "Trash Removal"
	 * - urgency: this is how urgent the request is and helpful for prioritizing. The valid options are: "Low", "Medium", "High", "Critical"
	 */
	public static void createSanitationTable() {

		String query = "Create Table sanitationRequest( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    roomID varchar(31) Not Null References node On Delete Cascade, " +
				"    signature varchar(31) Not Null, " +
				"    description varchar(5000), " +
				"    sanitationType varchar(31), " +
				"    urgency varchar(31) Not Null, " +
				"    Constraint sanitationTypeLimit Check (sanitationType In ('Urine Cleanup', 'Feces Cleanup', 'Preparation Cleanup', 'Trash Removal'))," +
				"    Constraint urgencyTypeLimit Check (urgency In ('Low', 'Medium', 'High', 'Critical')) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating sanitationRequest table");
		}
	}

	/**
	 * Uses executes the SQL statements required to create a extTransport table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user is sending the request to.
	 * - requestType: this the mode of transportation that the request is being made for. The valid options are: 'Ambulance', 'Helicopter', 'Plane'
	 * - severity: this is how sever the patient is who the user/first responders are transporting.
	 * - patientID: this is the ID of the patient that is being transported.
	 * - ETA: this is the estimated time the patient will arrive.
	 * - description: this is a detailed description of request that generally includes what happened to the patient and their current situation.
	 */
	public static void createExtTransportTable() {

		String query = "Create Table extTransport( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    roomID varchar(31) Not Null References node On Delete Cascade, " +
				"    requestType varchar(100) Not Null, " +
				"    severity varchar(30) Not Null, " +
				"    patientID varchar(31) Not Null, " +
				"    ETA varchar(100), " +
				"    bloodPressure varchar(31), " +
				"    temperature varchar(31), " +
				"    oxygenLevel varchar(31), " +
				"    description varchar(5000)," +
				"    Constraint requestTypeLimitExtTrans Check (requestType In ('Ambulance', 'Helicopter', 'Plane'))" +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating extTransport table");
		}
	}

	/**
	 * Uses executes the SQL statements required to create a medDelivery table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants the medecine delivered to.
	 * - medicineName: this is the drug that the user is ordering/requesting
	 * - quantity: the is the supply or the number of pills ordered
	 * - dosage: this is the mg or ml quantity for a medication
	 * - specialInstructions: this is any special details or instructions the user wants to give to who ever is processing the request.
	 * - signature: this the signature (name in print) of the user who is filling out the request
	 */
	public static void createMedDeliveryTable() {

		String query = "Create Table medDelivery ( " +
				"requestID  int Primary Key References requests On Delete Cascade," +
				"roomID     varchar(31) Not Null References node On Delete Cascade," +
				"medicineName        varchar(31) Not Null," +
				"quantity            int         Not Null," +
				"dosage              varchar(31) Not Null," +
				"specialInstructions varchar(5000)," +
				"signature           varchar(31) Not Null" + ")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating medDelivery table");
		}
	}

	/**
	 * Uses executes the SQL statements required to create a medDelivery table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - level: this is the security level that is needed
	 * - urgency: this is how urgent it is for security to arrive or for the request to be filled. The valid options are: 'Low', 'Medium', 'High', 'Critical'
	 */
	public static void createSecurityServTable() {

		String query = "Create Table securityServ ( " +
				"requestID  int Primary Key References requests On Delete Cascade," +
				"roomID     varchar(31) Not Null References node On Delete Cascade," +
				"level     varchar(31)," +
				"urgency   varchar(31) Not Null," +
				"Constraint urgencyTypeLimitServ Check (urgency In ('Low', 'Medium', 'High', 'Critical')) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating securityServ table");
		}
	}

	/**
	 * Uses executes the SQL statements required to create a languageRequest table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - languageType: this is the type of language the user is requesting
	 * - description: detailed description of request
	 */
	public static void createLanguageRequestTable() {

		String query = "Create Table languageRequest " +
				"( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    roomID    varchar(31) Not Null References node On Delete Cascade, " +
				"    languageType     varchar(31) Not Null, " +
				"    description   varchar(5000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating languageRequest table");
		}

	}

	/**
	 * Uses executes the SQL statements required to create a languageRequest table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - washLoadAmount: amount of loads needed to wash
	 * - dryLoadAmount: amount of loads needed to dry
	 * - description:  detailed description of request
	 */
	public static void createLaundryRequestTable() {

		String query = "Create Table laundryRequest " +
				"( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    roomID    varchar(31) Not Null References node On Delete Cascade, " +
				"    washLoadAmount   varchar(31) Not Null, " +
				"    dryLoadAmount   varchar(31) Not Null, " +
				"    description varchar(5000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating laundryRequest table");
		}

	}

	/**
	 * Uses executes the SQL statements required to create a languageRequest table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - type: is the type of maintenance required
	 * - severity: is how severe the situation is
	 * - ETA: time taken to complete the request
	 * - description: detailed description of request
	 */
	public static void createMaintenanceRequestTable() {

		String query = "Create Table maintenanceRequest " +
				"( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    roomID    varchar(31) Not Null References node On Delete Cascade, " +
				"    type varchar(31), " +
				"    severity    varchar(31)  Not Null, " +
				"    ETA   varchar(31) Not Null, " +
				"    description varchar(5000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating maintenanceRequest table");
		}

	}

	/**
	 * Uses executes the SQL statements required to create a foodDelivery table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - allergy: this is an food allergy that the user might have
	 * - dietRestriction: this is any dietary restrictions that the person fulfilling the request might need to know about
	 * - beverage: the drink the user is ordering
	 * - comments: any comments the user wants to leave for the person fulfilling the request
	 */
	public static void createFoodDeliveryTable() {

		String query = "Create Table foodDelivery ( " +
				"    requestID int Primary Key References requests (requestID) On Delete Cascade, " +
				"    roomID varchar(31) Not Null References node (nodeID) On Delete Cascade, " +
				"    allergy varchar(50), " +
				"    dietRestriction varchar(50), " +
				"    beverage varchar(50), " +
				"    description varchar(3000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating foodDelivery table");
		}


	}

	public static void createInternalPatientRequest() {
		String query = "Create Table internalPatientRequest " +
				"( " +
				"    requestID int Primary Key References requests On Delete Cascade, " +
				"    patientID int References userAccount(userID) On Delete Cascade, " +
				"    pickUpLocation varchar(31) Not Null References node On Delete Cascade, " +
				"    dropOffLocation varchar(31) Not Null References node On Delete Cascade, " +
				"    department varchar(31), " +
				"    severity varchar(31), " +
				"    description varchar(5000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();


		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating internalPatientRequest table");
		}
	}

	/**
	 * Uses executes the SQL statements required to create a languageRequest table. This is a type of request and share the same requestID.
	 * This table has the attributes:
	 * - requestID: this is used to identify a request. Every request must have one.
	 * - roomID: this is the nodeID/room the user wants security assistance at
	 * - religionID: is the type of maintenance required
	 * - description: detailed description of request
	 * - religionType: religion
	 */
	public static void createReligionRequestTable() {

		String query = "Create Table religiousRequest " +
				"( " +
				"requestID     int Primary Key References requests (requestID) On Delete Cascade, " +
				"roomID        varchar(31)  Not Null References node (nodeID) On Delete Cascade, " +
				"religionType  varchar(31)  Not Null, " +
				"description   varchar(5000) Not Null, " +
				"Constraint religionTypeLimit Check (religionType In ('Religion1', 'Religion2', 'Religion3', 'Religion4')) " +
				")";
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating religiousRequests table");
		}
	}

	/**
	 * create the entry request table
	 * decision: 0 for not filled, 1 for allow, 2 for ER, 3 for block
	 */
	public static void createEntryRequestTable() {
		// CovidSurveyObject has the following fields:
			// Integer user, Integer formNumber, Boolean positiveTest, Boolean symptoms, Boolean closeContact, Boolean quarantine, Boolean noSymptoms
		String query = "Create Table entryRequest " +
				"( " +
				"entryrequestID     int Primary Key, " +
				"positiveTest boolean Not Null, " +
				"symptoms     boolean Not Null, " +
				"closeContact     boolean Not Null, " +
				"quarantine     boolean Not Null, " +
				"noSymptoms     boolean Not Null, " +
				"status varchar(31) Not Null" +
				")";
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating entryRequest table");
		}
	}


	/**
	 * Uses executes the SQL statements required to create a aubonPainMenu table.
	 * This table has attributes:
	 * - foodImage: this is the url to an image of the item
	 * - foodItem: this is the item itself (this is unique and is used as an identifier)
	 * - foodPrice: this is the price of the foodItem
	 * - foodCalories: this is the number of calories the food item has
	 * - foodDescription: this is a description of the food item
	 */
	public static void createAubonPainMenuTable() {
		String query = "Create Table aubonPainMenu( " +
				"    foodImage varchar(600), " +
				"    foodItem varchar(100) Primary Key, " +
				"    foodPrice varchar(50), " +
				"    foodCalories varchar(50), " +
				"    foodDescription varchar(3000) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("error creating aubonPainMenu table");
		}
	}


	/**
	 * This parses through the Abon Pain website at BH and adds each item, its image, calories, price, and
	 * description to the aubonPainMenu table
	 * The link to the website being read is: https://order.aubonpain.com/menu/brigham-womens-hospital
	 */
	public static void populateAbonPainTable() {

		try {
			Document doc = Jsoup.connect("https://order.aubonpain.com/menu/brigham-womens-hospital").get();
			Set<String> classes = doc.classNames();

			Elements elements = doc.body().select("*");

			ArrayList<String> foodImage = new ArrayList<>();
			ArrayList<String> foodItems = new ArrayList<>();
			ArrayList<String> foodPrice = new ArrayList<>();
			ArrayList<String> foodCalories = new ArrayList<>();
			ArrayList<String> foodDescription = new ArrayList<>();

			int count = 0;
			for (Element element : elements) {
				if (element.ownText().equals("Breakfast ON THE GO")) {
					count++;
				}
				if (count == 2) {
					if (element.normalName().equals("img")) {
						foodImage.add(element.attr("abs:src"));
					}

					if (element.className().equals("product-name product__name")) {
						foodItems.add(element.ownText());
					}
					if (element.className().equals("product__attribute product__attribute--price")) {
						int numOfEmptySpacesToAdd = foodItems.size() - foodPrice.size() - 1;
						for (int i = 0; i < numOfEmptySpacesToAdd; i++) {
							foodPrice.add(null);
						}
						foodPrice.add(element.ownText());
					}
					if (element.className().equals("product__attribute product__attribute--calorie-label")) {
						int numOfEmptySpacesToAdd = foodItems.size() - foodCalories.size() - 1;
						for (int i = 0; i < numOfEmptySpacesToAdd; i++) {
							foodCalories.add(null);
						}
						foodCalories.add(element.ownText());
					}
					if (element.className().equals("product__description")) {
						int numOfEmptySpacesToAdd = foodItems.size() - foodDescription.size() - 1;
						for (int i = 0; i < numOfEmptySpacesToAdd; i++) {
							foodDescription.add(null);
						}
						foodDescription.add(element.ownText());
					}
				}

			}

			for (int i = 0; i < foodImage.size(); i++) {
				String image = null;
				String price = null;
				String calories = null;
				if (i < foodImage.size()) {
					image = foodImage.get(i);
				}
				if (i < foodPrice.size()) {
					price = foodPrice.get(i);
				}
				if (i < foodCalories.size()) {
					calories = foodCalories.get(i);
				}

				addAubonPainMenuItem(image, foodItems.get(i), price, calories, foodDescription.get(i));
			}


		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("error reading in Aubon Pain website in addAbonPainTable()");
		}

	}

// ADDING TO TABLES::::
// ADDING TO TABLES::::
// ADDING TO TABLES::::
// ADDING TO TABLES::::
// ADDING TO TABLES::::
// ADDING TO TABLES::::
// ADDING TO TABLES::::

	public static void addRequest(int userID, int assigneeID, String requestType) {
		String insertRequest = "Insert Into requests " +
				"Values ((Select Count(*) " +
				"         From requests) + 1, ?, Current Timestamp, ?, 'inProgress', ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertRequest)) {
			prepState.setInt(1, userID);
			prepState.setString(2, requestType);
			prepState.setInt(3, assigneeID);
			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into requests inside function addRequest()");
		}

	}

	/**
	 * This adds a sanitation services form to the table specific for it
	 * //@param form this is the form being added to the table
	 */
	public static void addSanitationRequest(int userID, int assigneeID, String roomID, String sanitationType, String description, String urgency, String signature) {
		addRequest(userID, assigneeID, "sanitation");

		String insertSanitationRequest = "Insert Into sanitationrequest " +
				"Values ((Select Count(*) " +
				"         From requests), ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertSanitationRequest)) {
			prepState.setString(1, roomID);
			prepState.setString(2, signature);
			prepState.setString(3, description);
			prepState.setString(4, sanitationType);
			prepState.setString(5, urgency);

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into sanitationRequest inside function addSanitationRequest()");
		}

	}

	/**
	 * This function needs to add a external patient form to the table for external patient forms
	 * //@param form this is the form that we will create and send to the database
	 */
	public static void addExternalPatientRequest(int userID, int assigneeID, String roomID, String requestType, String severity, String patientID, String ETA, String bloodPressure, String temperature, String oxygenLevel, String description) {

		addRequest(userID, assigneeID, "extTransport");

		String insertExtTransport = "Insert Into exttransport " +
				"Values ((Select Count(*) " +
				"         From requests), ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertExtTransport)) {
			prepState.setString(1, roomID);
			prepState.setString(2, requestType);
			prepState.setString(3, severity);
			prepState.setString(4, patientID);
			prepState.setString(5, ETA);
			prepState.setString(6, bloodPressure);
			prepState.setString(7, temperature);
			prepState.setString(8, oxygenLevel);
			prepState.setString(9, description);

			prepState.execute();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into extTransport inside function addExternalPatientRequest()");
		}

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
		addRequest(userID, assigneeID, "floral");

		String insertFloralRequest = "Insert Into floralrequests Values ((Select Count(*) From requests), ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertFloralRequest)) {
			prepState.setString(1, RoomNodeID);
			prepState.setString(2, recipientName);
			prepState.setString(3, flowerType);
			prepState.setInt(4, flowerAmount);
			prepState.setString(5, vaseType);
			prepState.setString(6, arrangement);
			prepState.setString(7, stuffedAnimal);
			prepState.setString(8, chocolate);
			prepState.setString(9, message);

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into floralRequests inside function addFloralRequest()");
		}
	}

	/**
	 * This adds a medicine request form to the table for medicine request forms
	 * // @param form this is the form being added
	 */
	public static void addMedicineRequest(int userID, int assigneeID, String roomID, String medicineName, int quantity, String dosage, String specialInstructions, String signature) {
		addRequest(userID, assigneeID, "medDelivery");

		String insertMedRequest = "Insert Into meddelivery Values ((Select Count(*) From requests), ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertMedRequest)) {
			prepState.setString(1, roomID);
			prepState.setString(2, medicineName);
			prepState.setInt(3, quantity);
			prepState.setString(4, dosage);
			prepState.setString(5, specialInstructions);
			prepState.setString(6, signature);

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into medicineRequest inside function addMedicineRequest()");
		}
	}

	/**
	 * This adds a security form to the table for security service form
	 * // @param form this is the form added to the table
	 */
	public static void addSecurityRequest(int userID, int assigneeID, String roomID, String level, String urgency) {
		addRequest(userID, assigneeID, "security");

		String insertSecurityRequest = "Insert Into securityserv Values ((Select Count(*) From requests), ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertSecurityRequest)) {
			prepState.setString(1, roomID);
			prepState.setString(2, level);
			prepState.setString(3, urgency);

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into securityRequest inside function addSecurityRequest()");
		}
	}

	/**
	 * This adds a entry request form to the table
	 * each time a new entry is added, status is automatically set as "Needs to be reviewed"
	 */
	public static void addEntryRequest(CovidSurveyObj covidSurveyObj) {
		boolean positiveTest = covidSurveyObj.getPositiveTest();
		boolean symptoms = covidSurveyObj.getSymptoms();
		boolean closeContact = covidSurveyObj.getCloseContact();
		boolean quarantine = covidSurveyObj.getQuarantine();
		boolean noSymptoms = covidSurveyObj.getNoSymptoms();
		int userID = covidSurveyObj.getUser();

		//addRequest(userID, assigneeID, "entryRequest");

		String insertEntryRequest = "Insert Into entryRequest Values ((Select Count(*) From entryRequest) + 1, ?, ?, ?, ?, ?, 'Needs to be reviewed')";

		try (PreparedStatement prepState = connection.prepareStatement(insertEntryRequest)) {
			prepState.setBoolean(1, positiveTest);
			prepState.setBoolean(2, symptoms);
			prepState.setBoolean(3, closeContact);
			prepState.setBoolean(4, quarantine);
			prepState.setBoolean(5, noSymptoms);

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into entryRequest inside function addEntryRequest()");
		}
	}


	/**
	 * @param userID       ID of the user
	 * @param assigneeID   ID of the assigned user who will complete this task
	 * @param roomID       nodeID of the user
	 * @param languageType type of language being requested
	 * @param description  detailed description of request
	 */
	public static void addLanguageRequest(int userID, int assigneeID, String roomID, String languageType, String description) {
		addRequest(userID, assigneeID, "languageRequest");

		String insertLanguageReq = "Insert Into languageRequest Values ((Select Count(*) From requests), ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertLanguageReq)) {
			prepState.setString(1, roomID);
			prepState.setString(2, languageType);
			prepState.setString(3, description);

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into languageRequest inside function addLanguageRequest()");
		}

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
		addRequest(userID, assigneeID, "laundryRequest");

		String insertLaundryReq = "Insert Into laundryRequest Values ((Select Count(*) From requests), ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertLaundryReq)) {
			prepState.setString(1, roomID);
			prepState.setString(2, washLoadAmount);
			prepState.setString(3, dryLoadAmount);
			prepState.setString(4, description);

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into laundryRequest inside function addLanguageRequest()");
		}
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
		addRequest(userID, assigneeID, "maintenanceRequest");

		String insertMaintenanceReq = "Insert Into maintenanceRequest Values ((Select Count(*) From requests), ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertMaintenanceReq)) {
			prepState.setString(1, roomID);
			prepState.setString(2, type);
			prepState.setString(3, severity);
			prepState.setString(4, ETA);
			prepState.setString(5, description);

			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into maintenanceRequest inside function addMaintenanceRequest()");
		}
	}

	//TODO: Not tested
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
	 *                         TODO: Not tested
	 */
	public static void addFoodDeliveryRequest(int userID, String roomID, int assigneeID, String dietRestrictions, String allergies, String foodItem, int foodQuantity, String beverageItem, int beverageQuantity) {
		addRequest(userID, assigneeID, "foodDeliveryRequest");


		int requestID = getMaxRequestID();

		String insertFoodDeliveryReq = "Insert Into foodDelivery Values (?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertFoodDeliveryReq)) {
			prepState.setInt(1, requestID);
			prepState.setString(2, roomID);
			prepState.setString(3, allergies);
			prepState.setString(4, dietRestrictions);
			prepState.execute();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error inserting into foodDeliveryRequest inside function addFoodDeliveryRequest()");
		}
	}


	/**
	 * Gets the largest requestID, which can be used to increment and make a the next one
	 * @return the largest requestID in the request table
	 */
	public static int getMaxRequestID() {
		int maxID = 0;

		String query = "Select Max(requestID) As maxRequestID From food";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();

			if (rset.next()) {
				maxID = rset.getInt("maxRequestID");
			}

			prepState.close();
			return maxID;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error in getMaxRequestID()");
			return 0;
		}
	}

	/**
	 * adds a menuItem to the aubonPainMenu database table
	 * @param foodImage       this is the url to an image of the item
	 * @param foodItem        this is the item itself (this is unique and is used as an identifier)
	 * @param foodPrice       this is the price of the foodItem
	 * @param foodCalories    this is the number of calories the food item has
	 * @param foodDescription this is a description of the food item
	 */
	public static void addAubonPainMenuItem(String foodImage, String foodItem, String foodPrice, String foodCalories, String foodDescription){

		String query = "Insert Into aubonPainMenu Values(?,?,?,?,?) ";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.setString(1, foodImage);
			prepState.setString(2, foodItem);
			prepState.setString(3, foodPrice);
			prepState.setString(4, foodCalories);
			prepState.setString(5, foodDescription);

			prepState.execute();


		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("error inserting addAubonPainMenuItem() ");
		}


	}


	public static void addInternalPatientRequest(int userID, String pickUpLocation, String dropOffLocation, int assigneeID, int patientID, String department, String severity, String description) {
		addRequest(userID, assigneeID, "internalPatientRequest");

		String insertInternalPatientReq = "Insert Into internalPatientRequest Values ((Select Count(*) From requests), ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertInternalPatientReq)) {
			prepState.setInt(1, patientID);
			prepState.setString(2, pickUpLocation);
			prepState.setString(3, dropOffLocation);
			prepState.setString(4, department);
			prepState.setString(5, severity);
			prepState.setString(6, description);

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into internalPatientRequest inside function addInternalPatientRequest()");
		}
	}

	/**
	 * add a religious request
	 * @param roomID       where the request takes place
	 * @param religionType the kind of the religion that request is requesting
	 * @param description  some text to further describe the request
	 */
	public static void addReligiousRequest(int userID, String roomID, int assigneeID, String religionType, String description) {
		addRequest(userID, assigneeID, "religiousRequest");

		String insertMaintenanceReq = "Insert Into religiousRequest Values ((Select Count(*) From requests), ?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertMaintenanceReq)) {
			prepState.setString(1, roomID);
			prepState.setString(2, religionType);
			prepState.setString(3, description);

			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into religiousRequest inside function addReligiousRequest()");
		}
	}


// EDITING TABLES::::
// EDITING TABLES::::
// EDITING TABLES::::
// EDITING TABLES::::
// EDITING TABLES::::
// EDITING TABLES::::
// EDITING TABLES::::

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

		boolean added = false;
		String query = "update sanitationRequest set";

		if (roomID != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " roomID = '" + roomID + "'";
			added = true;
		}
		if (sanitationType != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " sanitationType = '" + sanitationType + "'";
			added = true;
		}
		if (description != null) {
			query = query + " description = '" + description + "'";

			added = true;
		}
		if (urgency != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " urgency = '" + urgency + "'";
			added = true;
		}
		if (signature != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "signature = '" + signature + "'";
			added = true;
		}


		query = query + " where requestID = " + requestID;

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating sanitation request");
			return 0;
		}


	}


	/**
	 *
	 * @param covidSurveyObj
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editEntryRequest(CovidSurveyObj covidSurveyObj) {
		//int requestID, boolean positiveTest, boolean symptoms, boolean closeContact, boolean quarantine, boolean noSymptoms, String status
		int formNumber = covidSurveyObj.getFormNumber();
		Boolean positiveTestB = covidSurveyObj.getPositiveTest();
		Boolean symptomsB = covidSurveyObj.getSymptoms();
		Boolean closeContactB = covidSurveyObj.getCloseContact();
		Boolean quarantineB = covidSurveyObj.getQuarantine();
		Boolean noSymptomsB = covidSurveyObj.getNoSymptoms();
		String status = covidSurveyObj.getStatus();

		boolean added = false;
		String query = "Update entryRequest Set";

		if (positiveTestB != null) {
			query = query + " positiveTest = " + positiveTestB;
			added = true;
		}
		if (symptomsB != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " symptoms = " + symptomsB;
			added = true;
		}
		if (closeContactB != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " closeContact = " + closeContactB;
			added = true;
		}
		if (quarantineB != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " quarantine = " + quarantineB;
			added = true;
		}
		if (noSymptomsB != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " noSymptoms = " + noSymptomsB;
			added = true;
		}
		if (status != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " status = '" + status + "'";
			added = true;
		}

		query = query + " Where entryRequestID = " + formNumber;

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating entryRequest");
			return 0;
		}


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
	public static int editExternalPatientRequest(int requestID, String roomID, String requestType, String severity, String patientID, String description, String ETA, String bloodPressure, String temperature, String oxygenLevel) {

		boolean added = false;
		String query = "Update extTransport Set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";

			added = true;
		}
		if (requestType != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " requestType = '" + requestType + "'";
			added = true;
		}
		if (severity != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " severity = '" + severity + "'";
			added = true;
		}
		if (patientID != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " patientID = '" + patientID + "'";
			added = true;
		}
		if (description != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " description = '" + description + "'";
			added = true;
		}
		if (ETA != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " ETA = '" + ETA + "'";
			added = true;
		}
		if (bloodPressure != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " bloodPressure = '" + bloodPressure + "'";
			added = true;
		}
		if (temperature != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " temperature = '" + temperature + "'";
			added = true;
		}
		if (oxygenLevel != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " oxygenLevel = '" + oxygenLevel + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating external transport request");
			return 0;
		}
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

		boolean added = false;
		String query = "Update floralRequests Set ";

		if (recipientName != null) {
			query = query + "recipientName = '" + recipientName + "'";

			added = true;
		}
		if (roomID != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " roomID = '" + roomID + "'";
			added = true;
		}
		if (flowerType != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " flowerType = '" + flowerType + "'";
			added = true;
		}
		if (flowerAmount != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " flowerAmount = " + flowerAmount;
			added = true;
		}
		if (vaseType != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " vaseType = '" + vaseType + "'";
			added = true;
		}
		if (arrangement != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " arrangement = '" + arrangement + "'";
			added = true;
		}
		if (stuffedAnimal != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " stuffedAnimal = '" + stuffedAnimal + "'";
			added = true;
		}
		if (chocolate != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " chocolate = '" + chocolate + "'";
			added = true;
		}
		if (message != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " message = '" + message + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating floral request");
			return 0;
		}
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

		boolean added = false;
		String query = "Update medDelivery Set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";

			added = true;
		}
		if (medicineName != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " medicineName = '" + medicineName + "'";
			added = true;
		}
		if (quantity != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " quantity = " + quantity;
			added = true;
		}
		if (dosage != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " dosage = '" + dosage + "'";
			added = true;
		}
		if (specialInstructions != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + " specialInstructions = '" + specialInstructions + "'";
			added = true;
		}
		if (assigneeID != 0) {
			if (added) {
				query = query + ", ";
			}
			query = query + " assigneeID = '" + assigneeID + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating medicine request");
			return 0;
		}
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
		boolean added = false;
		String query = "Update securityServ Set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";

			added = true;
		}
		if (level != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "level = '" + level + "'";
			added = true;
		}
		if (urgency != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "urgency = '" + urgency + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating security request");
			return 0;
		}
	}

	/**
	 * Can change the assigneeID or the request status to any request
	 * @param requestID     is the generated ID of the request
	 * @param assigneeID    is the assignee's ID that you want to change it to
	 * @param requestStatus is the status that you want to change it to
	 * @return a 1 if one line changed successfully, and 0 or other numbers for failure
	 */
	public static int editRequests(int requestID, int assigneeID, String requestStatus) {

		boolean added = false;
		String query = "Update requests ";

		if (assigneeID != 0) {
			query = query + "Set assigneeID = '" + assigneeID + "'";

			added = true;
		}
		if (requestStatus != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "Set requestStatus = '" + requestStatus + "'";

		}

		query = query + " Where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating request table");
			return 0;
		}
	}


	/**
	 * @param requestID    is the generated ID of the request
	 * @param roomID       the new node/room/location the user is assigning this request to
	 * @param languageType is the new language type being requested by the user
	 * @param description  is an edited detailed description
	 * @return 1 if the update was successful, 0 if it failed
	 */
	public static int editLanguageRequest(int requestID, String roomID, String languageType, String description) {
		boolean added = false;
		String query = "Update languageRequest Set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";
			added = true;
		}
		if (languageType != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "languageType = '" + languageType + "'";
			added = true;
		}
		if (description != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + description + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating languageRequest");
			return 0;
		}

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
		boolean added = false;
		String query = "Update laundryRequest Set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";
			added = true;
		}
		if (washLoadAmount != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "washLoadAmount = '" + washLoadAmount + "'";
			added = true;
		}
		if (dryLoadAmount != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "dryLoadAmount = '" + dryLoadAmount + "'";
			added = true;
		}
		if (description != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + description + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating laundryRequest");
			return 0;
		}
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
		boolean added = false;
		String query = "Update maintenanceRequest Set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";
			added = true;
		}
		if (type != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "type = '" + type + "'";
			added = true;
		}
		if (severity != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "severity = '" + severity + "'";
			added = true;
		}
		if (ETA != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "ETA = '" + ETA + "'";
			added = true;
		}
		if (description != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + description + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating maintenanceRequest");
			return 0;
		}
	}

	//TODO: ASHLEY review this please (i didn't take your Food table into account)

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
		boolean added = false;
		String query = "Update foodDeliveryRequest Set ";

		if (roomID != null) {
			query = query + " roomID = '" + roomID + "'";
			added = true;
		}
		if (dietRestrictions != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "dietRestrictions = '" + dietRestrictions + "'";
			added = true;
		}
		if (allergies != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "allergies = '" + allergies + "'";
			added = true;
		}
		if (food != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "food = '" + food + "'";
			added = true;
		}
		if (beverage != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "beverage = '" + beverage + "'";
			added = true;
		}
		if (description != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + description + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating foodDeliveryRequest");
			return 0;
		}
	}

	public static int editInternalPatientRequest(int requestID, String pickUpLocation, String dropOffLocation, int patientID, String department, String severity, String description) {
		boolean added = false;
		String query = "Update internalPatientRequest Set ";

		if (pickUpLocation != null) {
			query = query + " pickUpLocation = '" + pickUpLocation + "'";
			added = true;
		}
		if (dropOffLocation != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "dropOffLocation = '" + dropOffLocation + "'";
			added = true;
		}
		Integer patientIDObj = patientID;
		if (patientIDObj != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "patientID = " + patientID;
			added = true;
		}
		if (department != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "department = '" + department + "'";
			added = true;
		}
		if (severity != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "severity = '" + severity + "'";
			added = true;
		}
		if (description != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + description + "'";
			added = true;
		}

		query = query + " where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating editInternalPatientRequest");
			return 0;
		}
	}

	/**
	 * edit a religious request
	 * @param roomID       where the request takes place
	 * @param religionType the kind of the religion that request is requesting
	 * @param description  some text to further describe the request
	 */
	public static int editReligiousRequest(int requestID, String roomID, String religionType, String description) {
		boolean added = false;
		String query = "Update religiousRequest Set ";

		if (roomID != null) {
			query = query + "roomID = '" + roomID + "'";
			added = true;
		}
		if (religionType != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "religionType = '" + religionType + "'";
			added = true;
		}
		if (description != null) {
			if (added) {
				query = query + ", ";
			}
			query = query + "description = '" + description + "'";
		}

		query = query + " where requestID = " + requestID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating religiousRequest");
			return 0;
		}
	}


// QUERYING TABLES::::
// QUERYING TABLES::::
// QUERYING TABLES::::
// QUERYING TABLES::::
// QUERYING TABLES::::
// QUERYING TABLES::::
// QUERYING TABLES::::

	/**
	 * Gets a list of all the "assigneeIDs", "requestIDs", or "requestStatus" from the requests with the given type done by the given userID
	 * Use "AssigneeID" to get the full name of the assignee, use "assigneeID" to get the ID of the assignee
	 * @param tableName this is the name of the table that we are getting the info from
	 * @param userID    this is the ID of the user who made the request
	 * @param infoType  this is the type of information that is being retrieved
	 * @return an ArrayList<String> with the desired info
	 */
	public static ArrayList<String> getMyCreatedRequestInfo(String tableName, int userID, String infoType) {

		ArrayList<String> listOfInfo = new ArrayList<>();

		String query;
		if (!DB.getUserType(userID).equals("admin")) {
			query = "Select " + infoType + " From requests Join " + tableName + " Using (requestID) Where creatorID = " + userID;
		} else {
			query = "Select " + infoType + " From requests Join " + tableName + " Using (requestID)";
		}

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();

			while (rset.next()) {
				if (infoType.equals("assigneeID") || infoType.equals("surveyResult") || infoType.equals("decision")) {
					int theInt = rset.getInt(infoType);
					listOfInfo.add(String.valueOf(theInt));
				} else if (infoType.equals("AssigneeID")) {
					int ID = rset.getInt(infoType);
					listOfInfo.add(UserAccountDB.getUserName(ID));
				} else {
					String ID = rset.getString(infoType); // potential issue // -TO-DO-: won't work with int AssigneeIDs? Fixed by translating IDs to String, should it return a pair of Assignee ID and name?
					listOfInfo.add(ID);
				}
			}
			rset.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("getRequestInfo() got a SQLException");
		}
		return listOfInfo;
	}

	/**
	 * Gets a list of all the "creatorIDs", "requestIDs", or "requestStatus" from the requests with the given type assigned to the given userID
	 * @param tableName this is the name of the table that we are getting the info from
	 * @param userID    this is the ID of the user who made the request
	 * @param infoType  this is the type of information that is being retrieved
	 * @return an ArrayList<String> with the desired info
	 */
	public static ArrayList<String> getMyAssignedRequestInfo(String tableName, int userID, String infoType) {

		ArrayList<String> listOfInfo = new ArrayList<>();

		String query;
		if (!DB.getUserType(userID).equals("admin")) {
			query = "Select " + infoType + " From requests Join " + tableName + " Using (requestID) Where assigneeID = " + userID;
		} else {
			query = "Select " + infoType + " From requests Join " + tableName + " Using (requestID)";
		}

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();

			while (rset.next()) {
				String ID = rset.getString(infoType); //potential issue
				listOfInfo.add(ID);
			}
			rset.close();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("getRequestInfo() got a SQLException");
		}
		return listOfInfo;
	}

	/**
	 * Gets a list of all the longNames for the location from the given tableName
	 * @param tableType this is the name of the table that we are getting the requestIDs from
	 * @return a list of all longNames for the location for all of the requests
	 */
	public static ArrayList<String> getRequestLocations(String tableType, int userID) {
		ArrayList<String> listOfLongNames = new ArrayList<>();

		String tableName = "";
		switch (tableType) {
			case "floralRequests":
				tableName = "floralRequests";
				break;
			case "medDelivery":
				tableName = "medDelivery";
				break;
			case "sanitationRequest":
				tableName = "sanitationRequest";
				break;
			case "securityServ":
				tableName = "securityServ";
				break;
			case "extTransport":
				tableName = "extTransport";
				break;
		}

		String query;

		if (!DB.getUserType(userID).equals("admin")) {
			query = "Select longName from node, (Select roomID From " + tableName + ", (Select requestID from requests Where requestType = '" + tableType + "' and creatorID = " + userID + ") correctType where correctType.requestID = " + tableName + ".requestID) correctStuff where correctStuff.roomID = node.nodeID";
		} else {
			query = "Select requestid, longname From node,(Select requestid, roomid From " + tableName + ") correctTable Where node.nodeid = correctTable.roomid Order By requestid";
		}
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();
			while (rset.next()) {
				String status = rset.getString("longName");
				listOfLongNames.add(status);
			}
			rset.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("getRequestLocations() got a SQLException");
		}
		return listOfLongNames;
	}

	/**
	 * gets a list of available assignees based on the userType given
	 * @param givenUserType is type of user (i.e. "nurse", "EMT", "doctor", etc.)
	 * @return a HashMap of possible assignees for the specific request type
	 */
	public static HashMap<Integer, String> getSelectableAssignees(String givenUserType) {
		HashMap<Integer, String> listOfAssignees = new HashMap<>();

		String query = "Select firstName, lastName, userID From userAccount Where userType = '" + givenUserType + "'";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();
			while (rset.next()) {
				String firstName = rset.getString("firstName");
				String lastName = rset.getString("lastName");
				int assigneeID = rset.getInt("userID");
				String fullName = firstName + " " + lastName;
				listOfAssignees.put(assigneeID, fullName);
			}
			rset.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("getAvailableAssignees() got a SQLException");
		}
		return listOfAssignees;
	}

	/**
	 * Gets a lits of all the menu items from aubon pain
	 * @return list of AubonPainItem that are in the aubonPainMenu database table
	 */
	public static ArrayList<AubonPainItem> getAubonPanItems() {
		String query = "Select * From aubonPainMenu";

		ArrayList<AubonPainItem> menuItems = new ArrayList<>();

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();
			while (rset.next()) {
				String foodImage = rset.getString("foodImage");
				String foodItem = rset.getString("foodItem");
				String foodPrice = rset.getString("foodPrice");
				String foodCalories = rset.getString("foodCalories");
				String foodDescription = rset.getString("foodDescription");

				AubonPainItem item = new AubonPainItem(foodImage, foodItem, foodPrice, foodCalories, foodDescription);
				menuItems.add(item);
			}
			rset.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("getAvailableAssignees() got a SQLException");
		}

		return menuItems;
	}

	/**
	 * Used to get a list of info from a given column name in the aubonPainMenu table
	 * @param column this is the name of the column the information is extracted from
	 * @return a list of the given information
	 */
	public static ArrayList<String> getAubonPainFeild(String column) {

		String query = "Select " + column + " From aubonPainMenu";

		ArrayList<String> foodItems = new ArrayList<>();

		try (PreparedStatement prepStat = connection.prepareStatement(query)) {

			ResultSet rset = prepStat.executeQuery();
			while (rset.next()) {
				String foodImage = rset.getString(column);

				foodItems.add(foodImage);
			}
			rset.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("getAubonPainFeild() got a SQLException");
		}

		return foodItems;
	}

	public static ObservableList<String> getAssigneeNames(String givenUserType) {
		ObservableList<String> listOfAssignees = FXCollections.observableArrayList();

		String query = "Select firstName, lastName From userAccount Where userType = '" + givenUserType + "'";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();
			while (rset.next()) {
				String firstName = rset.getString("firstName");
				String lastName = rset.getString("lastName");
				String fullName = firstName + " " + lastName;
				listOfAssignees.add(fullName);
			}
			rset.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("getAssigneeNames() got a SQLException");
		}
		return listOfAssignees;

	}

	public static ArrayList<Integer> getAssigneeIDs(String givenUserType) {
		ArrayList<Integer> listOfAssigneesIDs = new ArrayList<Integer>();

		String query = "Select userID From userAccount Where userType = '" + givenUserType + "'";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();
			while (rset.next()) {
				int assigneeID = rset.getInt("userID");
				listOfAssigneesIDs.add(assigneeID);
			}
			rset.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("getAssigneeIDs() got a SQLException");
		}
		return listOfAssigneesIDs;
	}

	public static ArrayList<CovidSurveyObj> getCovidSurveys() {

	ArrayList<CovidSurveyObj> covidSurveys = new ArrayList<>();

	String query = "Select * From entryRequest";

	try (PreparedStatement prepStat = connection.prepareStatement(query)) {

		ResultSet rset = prepStat.executeQuery();

		while (rset.next()) {

			int requestID = rset.getInt("entryRequestID");
			boolean positiveTest = rset.getBoolean("positiveTest");
			boolean symptoms = rset.getBoolean("symptoms");
			boolean closeContact = rset.getBoolean("closeContact");
			boolean quarantine = rset.getBoolean("quarantine");
			boolean noSymptoms = rset.getBoolean("noSymptoms");
			String status = rset.getString("status");
			covidSurveys.add(new CovidSurveyObj(App.userID, requestID, positiveTest, symptoms, closeContact, quarantine, noSymptoms, status));
		}

		rset.close();

	} catch (SQLException e) {
		System.err.println("getCovidSurveys Error : " + e);
	}
		return covidSurveys;

	}

	public static int markAsCovidSafe(int formNumber) {
		String query = "Update entryRequest set positiveTest = false, symptoms = false, closeContact = false, quarantine = false, noSymptoms = true, status = 'Safe' where entryRequestID = " + formNumber;

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating markAsCovidSafe");
			return 0;
		}



	}

	public static int updateUserAccountCovidStatus(int userID, String status) {
		String updateUserAccount = "Update userAccount set covidStatus = '" + status + "' where userID = " + userID;

		try (PreparedStatement prepState = connection.prepareStatement(updateUserAccount)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating updateUserAccountCovidStatus");
			return 0;
		}
	}

	public static int markAsCovidRisk(int formNumber) {
		String query = "Update entryRequest set positiveTest = true, symptoms = true, closeContact = true, quarantine = true, noSymptoms = false, status = 'Unsafe' where entryRequestID = " + formNumber;


		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error in updating markAsCovidRisk");
			return 0;
		}


	}

	public static String getEmail(int userID) {
		String query = "Select email From userAccount Where userID = " + userID;

		String email = "";
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();
			while (rset.next()) {
				email = rset.getString("email");
			}
			rset.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("getEmail() got a SQLException");
		}
		return email;
	}


}
