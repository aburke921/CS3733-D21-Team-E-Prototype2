package edu.wpi.cs3733.D21.teamE.database;

import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.ExternalPatientObj;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.FloralObj;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.MedicineDeliveryObj;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.SanitationServiceObj;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.SecurityServiceObj;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class makeConnection {

	// static variable singleInstance of type SingleConnection
	public static makeConnection singleInstance = null;
	public Connection connection;
	//private MapEditor mapEditor = new MapEditor();

	// private constructor restricted to this class itself
	public makeConnection() {
		// Initialize DB
		System.out.println("Starting connection to Apache Derby\n");
		try {

			//Makes it so a username and password (hardcoded) is needed to access the database data
			Properties props = new Properties();
			props.put("user", "admin");
			props.put("password", "admin");

			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

			try {
				/*
				 * Before making this connectin make sure you're database tab in Intellij
				 * Is not connected to the database! This will cause the DriverManager to
				 * Throw an SQLException and goof a bunch of stuff up!
				 */
				this.connection = DriverManager.getConnection("jdbc:derby:BWDB;create=true", props);
				// this.connection.setAutoCommit(false);
			} catch (SQLException e) {
				// e.printStackTrace();
				System.err.println("error with the DriverManager, check if you have connected to database in IntelliJ");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("error with the EmbeddedDriver class.forName thing");
		}
	}

	// static method to create instance of Singleton class
	public static makeConnection makeConnection() {
		// To ensure only one instance is created
		if (singleInstance == null) {
			singleInstance = new makeConnection();
		}
		return singleInstance;
	}

	public Connection getConnection() {
		return this.connection;
	}


	/**
	 * Deletes node,hasEdge, userAccount, requests, floralRequests, sanitationRequest and extTransport tables.
//	 * Also deletes adminAccount, doctorAccount, patientAccount, visitorAccount views
	 * try/catch phrase set up in case the tables all ready do not exist
	 */
	public void deleteAllTables() {

		try {
			Statement stmt = this.connection.createStatement();
			stmt.execute("Drop Table entryRequest");
			stmt.execute("Drop Table religiousRequest");
			stmt.execute("Drop Table aubonPainMenu");
			stmt.execute("Drop Table internalPatientRequest");
			stmt.execute("Drop Table foodDelivery");
			stmt.execute("Drop Table maintenanceRequest");
			stmt.execute("Drop Table laundryRequest");
			stmt.execute("Drop Table languageRequest");
			stmt.execute("Drop Table appointment");
			stmt.execute("Drop Table securityserv");
			stmt.execute("Drop Table meddelivery");
			stmt.execute("Drop Table exttransport");
			stmt.execute("Drop Table sanitationrequest");
			stmt.execute("Drop Table floralrequests");
			stmt.execute("Drop Table requests");
//			stmt.execute("Drop View visitoraccount");
//			stmt.execute("Drop View patientaccount");
//			stmt.execute("Drop View doctoraccount");
//			stmt.execute("Drop View adminaccount");
			stmt.execute("Drop Table useraccount");
			stmt.execute("Drop Table hasedge");
			stmt.execute("Drop Table node");
			stmt.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("deleteAllTables() not working");
		}
	}


	public boolean allTablesThere() {
		ArrayList<String> tablesInDB = new ArrayList<>();
		String[] names = {"TABLE"};
		ResultSet result;
		DatabaseMetaData metadata;
		try {
			metadata = connection.getMetaData();
			result = metadata.getTables(null, null, null, names);
			while ((result.next())) {
				System.out.println(result.getString("TABLE_NAME"));
				tablesInDB.add(result.getString("TABLE_NAME"));
			}
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
		if (tablesInDB.size() != 0) {
			System.out.println("Tables already there");
			return true;
		} else return false;
	}


	/**
	 * Calls all of the functions that creates each individual table
	 * Tables Created: node, hasEdge, userAccount, requests, floralRequests, sanitationRequest, extTransport, medDelivery, securityServ
//	 * Views Created (which are like tables): visitorAccount, patientAccount, doctorAccount, adminAccount
	 */


	public void addDataForPresentation() {
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

		//Admins:
		//28 - 30
		DB.addSpecialUserType("abbyw@gmail.com", "admin001", "admin", "Abby", "Williams");
		DB.addSpecialUserType("andrewg@yahoo.com", "admin002", "admin", "Andrew", "Guerrero");
		DB.addSpecialUserType("aleshah@gmail.com", "admin003", "admin", "Alesha", "Harris");

		//floralPerson:
		//31-39
		DB.addSpecialUserType("amyw@gmail.com", "floralPerson1", "floralPerson", "Amy", "Castaneda");
		DB.addSpecialUserType("elsaf@gmail.com", "floralPerson2", "floralPerson", "Elsa", "Figueroa");
		DB.addSpecialUserType("carolines@gmail.com", "floralPerson3", "floralPerson", "Caroline", "Sutton");
		DB.addSpecialUserType("milesl@gmail.com", "floralPerson4", "floralPerson", "Miles", "Long");
		DB.addSpecialUserType("hasanp@gmail.com", "floralPerson5", "floralPerson", "Hasan", "Perry");
		DB.addSpecialUserType("caroliner@gmail.com", "floralPerson6", "floralPerson", "Caroline", "Richardson");
		DB.addSpecialUserType("milesc@gmail.com", "floralPerson7", "floralPerson", "Miles", "Carroll");
		DB.addSpecialUserType("sethw@gmail.com", "floralPerson8", "floralPerson", "Seth", "Warner");
		DB.addSpecialUserType("darrenr@gmail.com", "floralPerson9", "floralPerson", "Darren", "Rossi");


		//Floral Requests: //RequestID: 1-9
		FloralObj floralRequest1 = new FloralObj(0,13, 31, "ADEPT00101", "Adam", "Roses", 1, "None", "do not Include arrangement", "do not Include stuffed Animal", "Include Chocolate", "Hi Adam, I am so sorry to hear about your accident. Please get better soon!");
		FloralObj floralRequest2 = new FloralObj(0,13, 32, "ADEPT00102", "Abraham", "Tulips", 6, "Round", "Include arrangement", "Include stuffed Animal", "do not Include Chocolate","Dear Abraham, hope these flowers help you feel better. The team really misses you and hope you will be ready to go by the championship");
		FloralObj floralRequest3 = new FloralObj(0,14, 33, "ADEPT00102", "Xavier", "Carnations", 12, "Square", "Include arrangement", "do not Include stuffed Animal", "Include Chocolate","Get well soon");
		FloralObj floralRequest4 = new FloralObj(0,15, 34, "ADEPT00301", "Nikki", "Assortment", 1, "None", "do not Include arrangement", "Include stuffed Animal", "do not Include Chocolate","Feel Better!");
		FloralObj floralRequest5 = new FloralObj(0,15, 35, "ADEPT00101", "Monica", "Roses", 6, "Tall", "Include arrangement", "do not Include stuffed Animal", "Include Chocolate","Love and miss you!!");
		FloralObj floralRequest6 = new FloralObj(0,17, 36, "DDEPT00102", "Amy", "Tulips", 12, "Square", "do not Include arrangement", "Include stuffed Animal", "do not Include Chocolate","Enjoy the flowers");
		FloralObj floralRequest7 = new FloralObj(0,17, 37, "ADEPT00102", "Alfred", "Carnations", 1, "Tall", "Include arrangement", "Include stuffed Animal", "Include Chocolate","Miss you!");
		FloralObj floralRequest8 = new FloralObj(0,19, 38, "ADEPT00101", "Caroline", "Assortment", 6, "Round", "do not Include arrangement", "do not Include stuffed Animal", "do not Include Chocolate","Sorry I forgot to warn you about the slippery stairs, I hope these flowers can make you feel better!");
		FloralObj floralRequest9 = new FloralObj(0,19, 39, "ADEPT00301", "Carrie", "Assortment", 12, "Round", "Include arrangement", "Include stuffed Animal", "Include Chocolate","Miss you!");



		DB.addFloralRequest(floralRequest1);
		DB.addFloralRequest(floralRequest2);
		DB.addFloralRequest(floralRequest3);
		DB.addFloralRequest(floralRequest4);
		DB.addFloralRequest(floralRequest5);
		DB.addFloralRequest(floralRequest6);
		DB.addFloralRequest(floralRequest7);
		DB.addFloralRequest(floralRequest8);
		DB.addFloralRequest(floralRequest9);


		//custodian:
		//40 - 48
		DB.addSpecialUserType("crystalh@gmail.com", "custodian1", "custodian", "Crystal", "Harvey");
		DB.addSpecialUserType("minnien@gmail.com", "custodian2", "custodian", "Minnie", "Newman");
		DB.addSpecialUserType("aylab@gmail.com", "custodian3", "custodian", "Ayla", "Black");
		DB.addSpecialUserType("lenardj@gmail.com", "custodian4", "custodian", "Lenard", "Jacobs");
		DB.addSpecialUserType("juanw@gmail.com", "custodian5", "custodian", "Juan", "Williams");
		DB.addSpecialUserType("mayj@gmail.com", "custodian6", "custodian", "May", "Jimenez");
		DB.addSpecialUserType("hermanb@gmail.com", "custodian7", "custodian", "Herman", "Bull");
		DB.addSpecialUserType("umarr@gmail.com", "custodian8", "custodian", "Umar", "Rojas");
		DB.addSpecialUserType("reubenf@gmail.com", "custodian9", "custodian", "Reuben", "Francolin");

		//pharmacist:
		//49 - 60
		DB.addSpecialUserType("clarab@gmail.com", "pharmacist1", "pharmacist", "Clara", "Bryan");
		DB.addSpecialUserType("jenniferc@gmail.com", "pharmacist2", "pharmacist", "Jennifer", "Cunningham");
		DB.addSpecialUserType("jakb@gmail.com", "pharmacist3", "pharmacist", "Jak", "Bishop");
		DB.addSpecialUserType("benc@gmail.com", "pharmacist4", "pharmacist", "Ben", "Coles");
		DB.addSpecialUserType("gloriaw@gmail.com", "pharmacist5", "pharmacist", "Gloria", "Webster");
		DB.addSpecialUserType("robbiet@gmail.com", "pharmacist6", "pharmacist", "Robbie", "Turner");
		DB.addSpecialUserType("lucasw@gmail.com", "pharmacist7", "pharmacist", "Lucas", "Whittaker");
		DB.addSpecialUserType("alecr@gmail.com", "pharmacist8", "pharmacist", "Alec", "Rees");
		DB.addSpecialUserType("francescaf@gmail.com", "pharmacist9", "pharmacist", "Francesca", "Ferguson");
		DB.addSpecialUserType("josiep@gmail.com", "pharmacist10", "pharmacist", "Josie", "Pittman");
		DB.addSpecialUserType("willf@gmail.com", "pharmacist11", "pharmacist", "Will", "Ford");
		DB.addSpecialUserType("billyg@gmail.com", "pharmacist12", "pharmacist", "Billy", "Gomez");


		//security
		//61-68
		DB.addSpecialUserType("jameso@gmail.com", "security1", "security", "James", "O'Moore");
		DB.addSpecialUserType("russella@gmail.com", "security2", "security", "Russell", "Armstrong");
		DB.addSpecialUserType("lillianp@gmail.com", "security3", "security", "Lillian", "Peters");
		DB.addSpecialUserType("clarad@gmail.com", "security4", "security", "Clara", "Dixon");
		DB.addSpecialUserType("herberto@gmail.com", "security5", "security", "Herbert", "Ortega");
		DB.addSpecialUserType("calebc@gmail.com", "security6", "security", "Caleb", "Carr");
		DB.addSpecialUserType("jasperm@gmail.com", "security7", "security", "Jasper", "Miller");
		DB.addSpecialUserType("jenniferb@gmail.com", "security8", "security", "Jennifer", "Brewer");


		//EMT:
		//69 - 77
		DB.addSpecialUserType("ciarang@gmail.com", "EMT000001", "EMT", "Ciaran", "Goodwin");
		DB.addSpecialUserType("lolab@gmail.com", "EMT000002", "EMT", "Lola", "Bond");
		DB.addSpecialUserType("samanthar@gmail.com", "EMT000003", "EMT", "Samantha", "Russell");
		DB.addSpecialUserType("calebm@gmail.com", "EMT000004", "EMT", "Caleb", "Myers");
		DB.addSpecialUserType("dalec@gmail.com", "EMT000005", "EMT", "Dale", "Coates");
		DB.addSpecialUserType("jerrym@gmail.com", "EMT000006", "EMT", "Jerry", "Myers");
		DB.addSpecialUserType("bettyw@gmail.com", "EMT000007", "EMT", "Betty", "Warren");
		DB.addSpecialUserType("maximr@gmail.com", "EMT000008", "EMT", "Maxim", "Rawlings");
		DB.addSpecialUserType("alans@gmail.com", "EMT000009", "EMT", "Alan", "Singh");


		DB.editRequests(1, 0, "canceled");
		DB.editRequests(4, 0, "canceled");
		DB.editRequests(5, 0, "complete");
		DB.editRequests(6, 0, "canceled");
		DB.editRequests(7, 0, "complete");


		//Sanitation Requests: //RequestID: 10 - 18
		DB.addSanitationRequest(new SanitationServiceObj(10, 20, 40, "AREST00101", "Urine Cleanup", "Restroom with urine on the floor", "Medium"));
		DB.addSanitationRequest(new SanitationServiceObj(11, 20, 41, "AREST00103", "Urine Cleanup", "Restroom with urine on the toilet seet", "Medium"));
		DB.addSanitationRequest(new SanitationServiceObj(12, 24, 42, "AREST00103", "Feces Cleanup", "Feces smeared on toilet seats", "High"));
		DB.addSanitationRequest(new SanitationServiceObj(13, 25, 43, "ARETL00101", "Trash Removal", "Trash can full, starting to smell", "Medium"));
		DB.addSanitationRequest(new SanitationServiceObj(14, 28, 44, "IREST00103", "Feces Cleanup", "Just outside of the bathroom there is a pile of feces. Someone did not make it in time.", "Critical"));
		DB.addSanitationRequest(new SanitationServiceObj(15, 30, 45, "IREST00203", "Trash Removal", "Trash can smells bad", "Medium"));
		DB.addSanitationRequest(new SanitationServiceObj(16, 29, 46, "IREST00303", "Trash Removal", "Trash can full. Another one is available so do not rush.", "Low"));
		DB.addSanitationRequest(new SanitationServiceObj(17, 22, 47, "HRETL00102", "Urine Cleanup", "Liquid on the floor. Unclear if it is urine. Not a whole lot of it.", "Low"));
		DB.addSanitationRequest(new SanitationServiceObj(18, 23, 48, "IREST00403", "Trash Removal", "", "Low"));


		DB.editRequests(11, 0, "canceled");
		DB.editRequests(14, 0, "canceled");
		DB.editRequests(15, 0, "complete");
		DB.editRequests(16, 0, "canceled");
		DB.editRequests(17, 0, "complete");


		//Medicine Delivery Request //RequestID: 19 - 30
		DB.addMedicineRequest(new MedicineDeliveryObj(19,20, 49, "BLABS00102", "Atorvastatin", 30, 20, "Once a day by mouth", "Bill Byrd"));
		DB.addMedicineRequest(new MedicineDeliveryObj(20, 20, 50, "BLABS00202", "Lisinopril", 90, 20, "Once a day by mouth", "Bill Byrd"));
		DB.addMedicineRequest(new MedicineDeliveryObj(21, 21, 51, "IDEPT00103", "Levothyroxine", 90, 125, "Once a day my bouth", "Amelia Knight"));
		DB.addMedicineRequest(new MedicineDeliveryObj(22, 24, 52, "BLABS00102", "Metformin", 30, 850, "Twice a day by mouth", "Taylor Ramos"));
		DB.addMedicineRequest(new MedicineDeliveryObj(23, 27, 53, "IDEPT00803", "Amlodipine", 30, 5, "Once a day by mouth", "Lauren Bolton"));
		DB.addMedicineRequest(new MedicineDeliveryObj(24, 26, 54, "IDEPT00603", "Metoprolol", 90, 400, "Once a day by mouth", "Declan Patel"));
		DB.addMedicineRequest(new MedicineDeliveryObj(25, 23, 55, "IDEPT00403", "Omeprazole", 90, 40, "Three times a day by mouth before a meal", "Victoria Erickson"));
		DB.addMedicineRequest(new MedicineDeliveryObj(26, 24, 56, "IDEPT00703", "Simvastatin", 30, 10, "Once a day by mouth", "Taylor Ramos"));
		DB.addMedicineRequest(new MedicineDeliveryObj(27, 27, 57, "IDEPT00903", "Losartan", 90, 100, "Once daily by mouth", "Lauren Bolton"));
		DB.addMedicineRequest(new MedicineDeliveryObj(28, 21, 58, "IDEPT00203", "Albuterol", 30, 63, "3 times a day via nebulizer. 4 times a day if needed.", "Amelia Knight"));
		DB.addMedicineRequest(new MedicineDeliveryObj(29, 20, 59, "BLABS00202", "Metformin", 30, 85, "Once daily with meals.", "Bill Byrd"));
		DB.addMedicineRequest(new MedicineDeliveryObj(30, 23, 60, "BLABS00102", "Metformin", 30, 5, "Twice a day with meals.", "Victoria Erickson"));


		DB.editRequests(20, 0, "canceled");
		DB.editRequests(24, 0, "canceled");
		DB.editRequests(25, 0, "complete");
		DB.editRequests(26, 0, "canceled");
		DB.editRequests(27, 0, "complete");


		//Security Requests: //RequestID: 31 - 38
		DB.addSecurityRequest(new SecurityServiceObj(31, 20, 61, "HDEPT00203", "Low", "Low", "Someone is hallucinating and not being safe"));
		DB.addSecurityRequest(new SecurityServiceObj(32, 22, 62, "WELEV00E01", "Medium", "Medium", "A patient is being violent"));
		DB.addSecurityRequest(new SecurityServiceObj(33, 30, 63, "HDEPT00203", "Low", "Low", "A women who was told to leave the hospital is not doing so"));
		DB.addSecurityRequest(new SecurityServiceObj(34, 27, 64, "ePARK00101", "Medium", "High", "A parent whose child is in the hospital is threatening a doctor"));
		DB.addSecurityRequest(new SecurityServiceObj(35, 24, 65, "BDEPT00402", "Medium", "Medium", null));
		DB.addSecurityRequest(new SecurityServiceObj(36, 20, 66, "BDEPT00302", "Low", "Low", null));
		DB.addSecurityRequest(new SecurityServiceObj(37, 25, 67, "CCONF002L1", "High", "Critical", "There is a suspicious person in the waiting room"));
		DB.addSecurityRequest(new SecurityServiceObj(38, 29, 68, "eWALK00701", "Medium", "Medium", null));


		DB.editRequests(31, 0, "canceled");
		DB.editRequests(34, 0, "canceled");
		DB.editRequests(35, 0, "complete");
		DB.editRequests(36, 0, "canceled");
		DB.editRequests(37, 0, "complete");


		//RequestID: 39 - 47
		DB.addExternalPatientRequest(new ExternalPatientObj(39, 27, 70, "EEXIT00101", "High Severity", "Ambulance", "12334567", "High", "Low", "Low", "Patient dropped down into a state of unconsciousness randomly at the store. Patient is still unconscious and unresponsive but has a pulse. No friends or family around during the incident."));
		DB.addExternalPatientRequest(new ExternalPatientObj(40, 30, 71, "EEXIT00101", "High Severity", "Ambulance", "4093380", "Low", "High", "High","Patient coming in with cut on right hand. Needs stitches. Bleeding is stable."));
		DB.addExternalPatientRequest(new ExternalPatientObj(41, 22, 72, "FDEPT00501", "High Severity", "Helicopter", "92017693", "High", "High", "Low","Car crash on the highway. 7 year old child in the backseat with no seatbelt on in critical condition. Blood pressure is low and has major trauma to the head."));
		DB.addExternalPatientRequest(new ExternalPatientObj(42, 20, 73, "FDEPT00501", "High Severity", "Helicopter", "93754789", "High", "High", "Low","Skier hit tree and lost consciousness. Has been unconscious for 30 minutes. Still has a pulse."));
		DB.addExternalPatientRequest(new ExternalPatientObj(43, 24, 74, "EEXIT00101", "High Severity", "Ambulance", "417592",  "Low", "Low", "High","Smoke inhalation due to a fire. No burns but difficult time breathing."));
		DB.addExternalPatientRequest(new ExternalPatientObj(44, 28, 75, "FDEPT00501", "High Severity", "Helicopter", "44888936",  "High", "High", "Low","Major car crash on highway. Middle aged woman ejected from the passenger's seat. Awake and unresponsive and in critical condition"));
		DB.addExternalPatientRequest(new ExternalPatientObj(45,24, 76, "EEXIT00101", "High Severity", "Ambulance", "33337861",  "High", "Low", "High","Patient passed out for 30 seconds. Is responsive and aware of their surroundings. Has no history of passing out."));
		DB.addExternalPatientRequest(new ExternalPatientObj(46,27, 77, "EEXIT00101", "High Severity", "Ambulance", "40003829",  "Low", "High", "Low","Relocating a patient with lung cancer from Mt.Auburn Hospital."));
		DB.addExternalPatientRequest(new ExternalPatientObj(47,24, 77, "FDEPT00501", "High Severity", "Plane", "38739983",  "Low", "Low", "High","Heart transplant organ in route"));



		//RELIGIOUS:
		//78 - 86
		DB.addSpecialUserType("achen@gmail.com", "R000001", "religious", "Amelie", "Chen");
		DB.addSpecialUserType("aburke@gmail.com", "R000002", "religious", "Ashley", "Bolt");
		DB.addSpecialUserType("samanthat@gmail.com", "R000003", "religious", "Samantha", "Tussell");
		DB.addSpecialUserType("calebh@gmail.com", "R000004", "religious", "Caleb", "Hello");
		DB.addSpecialUserType("dalet@gmail.com", "R000005", "religious", "Dale", "Test");
		DB.addSpecialUserType("jerryh@gmail.com", "R000006", "religious", "Jerry", "Hello");
		DB.addSpecialUserType("bettyy@gmail.com", "R000007", "religious", "Betty", "Yay");
		DB.addSpecialUserType("maximn@gmail.com", "R000008", "religious", "Maxim", "No");
		DB.addSpecialUserType("alanp@gmail.com", "R000009", "religious", "Alan", "Pri");


		//INTERPRETER:
		//87 - 95
		DB.addSpecialUserType("chenA@gmail.com", "I000001", "interpreter", "Chen", "A");
		DB.addSpecialUserType("burkeA@gmail.com", "I000002", "interpreter", "Bolt", "A");
		DB.addSpecialUserType("tS@gmail.com", "I000003", "interpreter", "Tussell", "S");
		DB.addSpecialUserType("hC@gmail.com", "I000004", "interpreter", "Hello", "C");
		DB.addSpecialUserType("tD@gmail.com", "I000005", "interpreter", "Test", "Dale");
		DB.addSpecialUserType("hJ@gmail.com", "I000006", "interpreter", "Hello", "Jerry");
		DB.addSpecialUserType("bY@gmail.com", "I000007", "interpreter", "Yay", "Betty");
		DB.addSpecialUserType("mY@gmail.com", "I000008", "interpreter", "No", "Maxim");
		DB.addSpecialUserType("pA@gmail.com", "I000009", "interpreter", "Pri", "Alan");



		//NURSE:
		//96 - 104
		DB.addSpecialUserType("asmith123@gmail.com", "N000001", "nurse", "Andrew", "Smith");
		DB.addSpecialUserType("sanders123@gmail.com", "N000002", "nurse", "Smith", "Andrews");
		DB.addSpecialUserType("js123@gmail.com", "N000003", "nurse", "Jessica", "Kayman");
		DB.addSpecialUserType("nicolette234@gmail.com", "N000004", "nurse", "Cayman", "Nicolette");
		DB.addSpecialUserType("Cayman123@gmail.com", "N000005", "nurse", "Nicolette", "Cayman");
		DB.addSpecialUserType("Blossom12@gmail.com", "N000006", "nurse", "Jill", "Blossom");
		DB.addSpecialUserType("James344@gmail.com", "N000007", "nurse", "Chris", "James");
		DB.addSpecialUserType("Chris322@gmail.com", "N000008", "nurse", "James", "Chris");
		DB.addSpecialUserType("fwe@gmail.com", "N000009", "nurse", "Alan", "Alan");



		DB.editRequests(40, 0, "complete");
		DB.editRequests(42, 0, "complete");
		DB.editRequests(44, 0, "complete");
		DB.editRequests(45, 0, "complete");
		DB.editRequests(47, 0, "complete");






		//Special Accounts:
		String insertUser1 = "Insert Into useraccount Values (-1, 'engineeringsoftware3733@gmail.com', 'admin', 'admin', 'Super', 'Admin', Current Timestamp, '', Null, Null)";
		String insertUser2 = "Insert Into useraccount Values (-99, 'wwong2@wpi.edu', 'admin', 'admin', 'Admin', 'Wong', Current Timestamp, '', Null, Null)";
		String insertUser3 = "Insert Into useraccount Values (-9999, 'admin', 'admin', 'admin', 'Admin', 'Wong', Current Timestamp, '', Null, Null)";
		String insertUser4 = "Insert Into useraccount Values (30000, 'staff', 'staff', 'doctor', 'Staff', 'Wong', Current Timestamp, '', Null, Null)";
		String insertUser5 = "Insert Into useraccount Values (20000, 'patient', 'patient', 'patient', 'Patient', 'Dude', Current Timestamp, '', Null, Null)";
		String insertUser6 = "Insert Into useraccount Values (10000, 'visitor', 'visitor', 'visitor', 'Visitor', 'Wong', Current Timestamp, '', Null, Null)";
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(insertUser1);
			stmt.executeUpdate(insertUser2);
			stmt.executeUpdate(insertUser3);
			stmt.executeUpdate(insertUser4);
			stmt.executeUpdate(insertUser5);
			stmt.executeUpdate(insertUser6);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


}
