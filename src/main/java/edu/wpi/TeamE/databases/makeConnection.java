package edu.wpi.TeamE.databases;

import edu.wpi.TeamE.algorithms.Edge;
import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.views.MapEditor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.io.*;
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

	public Connection getConnection(){
		return this.connection;
	}






	/**
	 * Deletes node,hasEdge, userAccount, requests, floralRequests, sanitationRequest and extTransport tables.
	 * Also deletes adminAccount, doctorAccount, patientAccount, visitorAccount views
	 * try/catch phrase set up in case the tables all ready do not exist
	 */
	public void deleteAllTables() {

		try {
			Statement stmt = this.connection.createStatement();
			stmt.execute("Drop Table securityServ");
			stmt.execute("Drop Table medDelivery");
			stmt.execute("Drop Table extTransport");
			stmt.execute("Drop Table sanitationRequest");
			stmt.execute("Drop Table floralRequests");
			stmt.execute("Drop Table requests");
			stmt.execute("Drop View visitorAccount");
			stmt.execute("Drop View patientAccount");
			stmt.execute("Drop View doctorAccount");
			stmt.execute("Drop View adminAccount");
			stmt.execute("Drop Table userAccount");
			stmt.execute("Drop Table hasEdge");
			stmt.execute("Drop Table node");
			stmt.close();
		} catch (SQLException e) {
//			 e.printStackTrace();
			System.err.println("deleteAllTables() not working");
		}
	}



	public boolean allTablesThere(){ //todo will probably cause a merge conflict at somepoint, accept DB version
		ArrayList<String> tablesInDB = new ArrayList<String>();
		String[] names = { "TABLE" };
		ResultSet result;
		DatabaseMetaData metadata = null;
		try {
			metadata = connection.getMetaData();
			result = metadata.getTables(null, null, null, names);
			while((result.next())) {
				System.out.println(result.getString("TABLE_NAME"));
				tablesInDB.add(result.getString("TABLE_NAME"));
			}
		} catch(java.sql.SQLException e) {
			e.printStackTrace();
		}
		if (tablesInDB.size() != 0){
			System.out.println("Tables already there");
			return true;
		}
		else return false;
	}


	/**
	 * Calls all of the functions that creates each individual table
	 * Tables Created: node, hasEdge, userAccount, requests, floralRequests, sanitationRequest, extTransport, medDelivery, securityServ
	 * Views Created (which are like tables): visitorAccount, patientAccount, doctorAccount, adminAccount
	 */


	public void addDataForPresentation(){
		//Visitors:
		// - have access to floral Delivery
		UserAccountDB.addUserAccount("bellag@gmail.com", "visitor1", "Bella", "Graham");
		UserAccountDB.addUserAccount("terry_reilly123@yahoo.com", "visitor2", "Terry", "Reilly");
		UserAccountDB.addUserAccount("smiddle@outlook.com", "visitor3", "Sharon", "Middleton");
		UserAccountDB.addUserAccount("catherinehop12@gmail.com", "visitor4", "Catherine", "Hopkins");
		UserAccountDB.addUserAccount("mbernard@wpi.edu", "visitor5", "Michelle", "Bernard");
		UserAccountDB.addUserAccount("mccoy.meghan@hotmail.com", "visitor6", "Meghan", "Mccoy");
		UserAccountDB.addUserAccount("harry89owens@gmail.com", "visitor7", "Harry", "Owens");
		UserAccountDB.addUserAccount("hugowh@gmail.com", "visitor8", "Hugo", "Whitehouse");
		UserAccountDB.addUserAccount("spenrodg@yahoo.com", "visitor9", "Spencer", "Rodgers");
		UserAccountDB.addUserAccount("thomasemail@gmail.com", "visitor10", "Thomas", "Mendez");
		UserAccountDB.addUserAccount("claytonmurray@gmail.com", "visitor11", "Clayton", "Murray");
		UserAccountDB.addUserAccount("lawrencekhalid@yahoo.com", "visitor12", "Khalid", "Lawrence");

		//Patients:
		//13 - 19
		UserAccountDB.addSpecialUserType("adamj@gmail.com","patient1","patient","Adam", "Jenkins");
		UserAccountDB.addSpecialUserType("abbym@yahoo.com","patient2","patient","Abby", "Mohamed");
		UserAccountDB.addSpecialUserType("wesleya@gmail.com","patient3","patient","Wesley", "Armstrong");
		UserAccountDB.addSpecialUserType("travisc@yahoo.com","patient4","patient","Travis", "Cook");
		UserAccountDB.addSpecialUserType("gabriellar@gmail.com","patient5","patient","Gabriella", "Reyes");
		UserAccountDB.addSpecialUserType("troyo@yahoo.com","patient6","patient","Troy", "Olson");
		UserAccountDB.addSpecialUserType("anat@gmail.com","patient7","patient","Ana", "Turner");

		//Doctors:
		//20-27
		UserAccountDB.addSpecialUserType("billb@gmail.com","doctor01","doctor","Bill", "Byrd");
		UserAccountDB.addSpecialUserType("ameliak@yahoo.com","doctor02","doctor","Amelia", "Knight");
		UserAccountDB.addSpecialUserType("simond@gmail.com","doctor03","doctor","Simon", "Daniel");
		UserAccountDB.addSpecialUserType("victoriae@yahoo.com","doctor04","doctor","Victoria", "Erickson");
		UserAccountDB.addSpecialUserType("taylorr@gmail.com","doctor05","doctor","Taylor", "Ramos");
		UserAccountDB.addSpecialUserType("rosas@yahoo.com","doctor06","doctor","Rosa", "Smith");
		UserAccountDB.addSpecialUserType("declanp@gmail.com","doctor07","doctor","Declan", "Patel");
		UserAccountDB.addSpecialUserType("laurenb@yahoo.com","doctor08","doctor","Lauren", "Bolton");

		//Admin:
		//28 - 30
		UserAccountDB.addSpecialUserType("abbyw@gmail.com","admin001","admin","Abby", "Williams");
		UserAccountDB.addSpecialUserType("andrewg@yahoo.com","admin002","admin","Andrew", "Guerrero");
		UserAccountDB.addSpecialUserType("aleshah@gmail.com","admin003","admin","Alesha", "Harris");


		//Floral Requests: //RequestID: 1-9
		RequestsDB.addFloralRequest(13,"Amy Castaneda", "ADEPT00101", "Adam", "Roses", 1, "None", "Hi Adam, I am so sorry to hear about your accident. Please get better soon!");
		RequestsDB.addFloralRequest(13,"Elsa Figueroa", "ADEPT00102", "Abraham", "Tulips", 6, "Round", "Dear Abraham, hope these flowers help you feel better. The team really misses you and hope you will be ready to go by the championship");
		RequestsDB.addFloralRequest(14,"Caroline Sutton", "ADEPT00102", "Xavier", "Carnations", 12, "Square", "Get well soon");
		RequestsDB.addFloralRequest(15,"Miles Long", "ADEPT00301", "Nikki", "Assortment", 1, "None", "");
		RequestsDB.addFloralRequest(15,"Hasan Perry", "ADEPT00101", "Monica", "Roses", 6, "Tall", "Love and miss you!!");
		RequestsDB.addFloralRequest(17,"Caroline Richardson", "DDEPT00102", "Amy", "Tulips", 12, "Square", "Enjoy the flowers");
		RequestsDB.addFloralRequest(17,"Miles Carroll", "ADEPT00102", "Alfred", "Carnations", 1, "Tall", "Miss you!");
		RequestsDB.addFloralRequest(19,"Seth Warner", "ADEPT00101", "Caroline", "Assortment", 6, "Round", "Sorry I forgot to warn you about the slippery stairs, I hope these flowers can make you feel better!");
		RequestsDB.addFloralRequest(19,"Darren Rossi", "ADEPT00301", "Carrie", "Assortment", 12, "Round", "");


		RequestsDB.changeRequestStatus(1, "canceled");
		RequestsDB.changeRequestStatus(4, "canceled");
		RequestsDB.changeRequestStatus(5, "complete");
		RequestsDB.changeRequestStatus(6, "canceled");
		RequestsDB.changeRequestStatus(7, "complete");


		//Sanitation Requests: //RequestID: 10 - 18
		RequestsDB.addSanitationRequest(20,"Crystal Harvey", "AREST00101", "Urine Cleanup", "Restroom with urine on the floor", "Medium", "Bill Byrd");
		RequestsDB.addSanitationRequest(20,"Minnie Newman", "AREST00103", "Urine Cleanup", "Restroom with urine on the toilet seet", "Medium", "Bill Byrd");
		RequestsDB.addSanitationRequest(24,"Ayla Black", "AREST00103", "Feces Cleanup", "Feces smeared on toilet seats", "High", "Taylor Ramos");
		RequestsDB.addSanitationRequest(25,"Lenard Jacobs", "ARETL00101", "Trash Removal", "Trash can full, starting to smell", "Medium", "Rosa Smith");
		RequestsDB.addSanitationRequest(28,"Juan Williams", "IREST00103", "Feces Cleanup", "Just outside of the bathroom there is a pile of feces. Someone did not make it in time.", "Critical", "Abby Williams");
		RequestsDB.addSanitationRequest(30,"May Jimenez", "IREST00203", "Trash Removal", "Trash can smells bad", "Medium", "Alesha Harris");
		RequestsDB.addSanitationRequest(29,"Herman Bull", "IREST00303", "Trash Removal", "Trash can full. Another one is available so don't rush.", "Low", "Andrew Guerrero");
		RequestsDB.addSanitationRequest(22,"Umar Rojas", "HRETL00102", "Urine Cleanup", "Liquid on the floor. Unclear if it is urine. Not a whole lot of it.", "Low", "Simon Daniel");
		RequestsDB.addSanitationRequest(23,"Reuben", "IREST00403", "Trash Removal", "", "Low", "Victoria Erickson");


		RequestsDB.changeRequestStatus(11, "canceled");
		RequestsDB.changeRequestStatus(14, "canceled");
		RequestsDB.changeRequestStatus(15, "complete");
		RequestsDB.changeRequestStatus(16, "canceled");
		RequestsDB.changeRequestStatus(17, "complete");

		//Medicine Delivery Request //RequestID: 19 - 30
		RequestsDB.addMedicineRequest(20, "Clara Bryan", "BLABS00102", "Atorvastatin", 30, "20mg", "Once a day by mouth", "Bill Byrd");
		RequestsDB.addMedicineRequest(20, "Jennifer Cunningham", "BLABS00202", "Lisinopril", 90, "20mg", "Once a day by mouth", "Bill Byrd");
		RequestsDB.addMedicineRequest(21, "Jak Bishop", "IDEPT00103", "Levothyroxine", 90, "12.5mcg", "Once a day my bouth", "Amelia Knight");
		RequestsDB.addMedicineRequest(24, "Ben Coles", "BLABS00102", "Metformin", 30, "850mg", "Twice a day by mouth", "Taylor Ramos");
		RequestsDB.addMedicineRequest(27, "Gloria Webster", "IDEPT00803", "Amlodipine", 30, "5mg", "Once a day by mouth", "Lauren Bolton");
		RequestsDB.addMedicineRequest(26, "Robbie Turner", "IDEPT00603", "Metoprolol", 90, "400mg", "Once a day by mouth", "Declan Patel");
		RequestsDB.addMedicineRequest(23, "Lucas Whittaker", "IDEPT00403", "Omeprazole", 90, "40mg", "Three times a day by mouth before a meal", "Victoria Erickson");
		RequestsDB.addMedicineRequest(24, "Alec Rees", "IDEPT00703", "Simvastatin", 30, "10mg", "Once a day by mouth", "Taylor Ramos");
		RequestsDB.addMedicineRequest(27, "Francesca Ferguson", "IDEPT00903", "Losartan", 90, "100mg", "Once daily by mouth", "Lauren Bolton");
		RequestsDB.addMedicineRequest(21, "Josie Pittman", "IDEPT00203", "Albuterol", 30, "0.63mg", "3 times a day via nebulizer. 4 times a day if needed.", "Amelia Knight");
		RequestsDB.addMedicineRequest(20, "Will Ford", "BLABS00202", "Metformin", 30, "8.5mL", "Once daily with meals.", "Bill Byrd");
		RequestsDB.addMedicineRequest(23, "Billy Gomez", "BLABS00102", "Metformin", 30, "5mL", "Twice a day with meals.", "Victoria Erickson");


		RequestsDB.changeRequestStatus(20, "canceled");
		RequestsDB.changeRequestStatus(24, "canceled");
		RequestsDB.changeRequestStatus(25, "complete");
		RequestsDB.changeRequestStatus(26, "canceled");
		RequestsDB.changeRequestStatus(27, "complete");


		//Security Requests: //RequestID: 31 - 38
		RequestsDB.addSecurityRequest(20, "James O'Moore","HDEPT00203", "Low", "Low");
		RequestsDB.addSecurityRequest(22, "Russell Armstrong","WELEV00E01", "Medium", "Medium");
		RequestsDB.addSecurityRequest(30, "Lillian Peters","HDEPT00203", "Low", "Low");
		RequestsDB.addSecurityRequest(27, "Clara Dixon","ePARK00101", "Medium", "High");
		RequestsDB.addSecurityRequest(24, "Herbert Ortega","BDEPT00402", "Medium", "Medium");
		RequestsDB.addSecurityRequest(20, "Caleb Carr","BDEPT00302", "Low", "Low");
		RequestsDB.addSecurityRequest(25, "Jasper Miller","CCONF002L1", "High", "Critical");
		RequestsDB.addSecurityRequest(29, "Jennifer Brewer","eWALK00701", "Medium", "Medium");


		RequestsDB.changeRequestStatus(31, "canceled");
		RequestsDB.changeRequestStatus(34, "canceled");
		RequestsDB.changeRequestStatus(35, "complete");
		RequestsDB.changeRequestStatus(36, "canceled");
		RequestsDB.changeRequestStatus(37, "complete");


		//RequestID: 39 - 47
		RequestsDB.addExternalPatientRequest(27,"Ciaran Goodwin","EEXIT00101", "Ambulance", "High Severity", "12334567", "5 minutes", "Patient dropped down into a state of unconsciousness randomly at the store. Patient is still unconscious and unresponsive but has a pulse. No friends or family around during the incident. ");
		RequestsDB.addExternalPatientRequest(30,"Lola Bond","EEXIT00101", "Ambulance","Low Severity", "4093380", "20 minutes", "Patient coming in with cut on right hand. Needs stitches. Bleeding is stable.");
		RequestsDB.addExternalPatientRequest(22,"Samantha Russell","FDEPT00501", "Helicopter","High Severity", "92017693", "10 minutes", "Car crash on the highway. 7 year old child in the backseat with no seatbelt on in critical condition. Blood pressure is low and has major trauma to the head.");
		RequestsDB.addExternalPatientRequest(20,"Caleb Chapman","FDEPT00501", "Helicopter","High Severity", "93754789", "20 minutes", "Skier hit tree and lost consciousness. Has been unconscious for 30 minutes. Still has a pulse.");
		RequestsDB.addExternalPatientRequest(24,"Dale Coates","EEXIT00101", "Ambulance","Medium Severity", "417592", "10 minutes", "Smoke inhalation due to a fire. No burns but difficult time breathing.");
		RequestsDB.addExternalPatientRequest(28,"Jerry Myers","FDEPT00501", "Helicopter", "High Severity", "44888936", "15 minutes", "Major car crash on highway. Middle aged woman ejected from the passenger's seat. Awake and unresponsive and in critical condition");
		RequestsDB.addExternalPatientRequest(24,"Betty Warren","EEXIT00101", "Ambulance","Medium Severity", "33337861", "7 minutes", "Patient passed out for 30 seconds. Is responsive and aware of their surroundings. Has no history of passing out.");
		RequestsDB.addExternalPatientRequest(27,"Maxim Rawlings","EEXIT00101", "Ambulance","Low Severity", "40003829", "10 minutes", "Relocating a patient with lung cancer from Mt.Auburn Hospital.");
		RequestsDB.addExternalPatientRequest(24,"Alan Singh","FDEPT00501", "Plane","High Severity", "38739983", "12 hours", "Heart transplant organ in route");


		RequestsDB.changeRequestStatus(40, "complete");
		RequestsDB.changeRequestStatus(42, "complete");
		RequestsDB.changeRequestStatus(44, "complete");
		RequestsDB.changeRequestStatus(45, "complete");
		RequestsDB.changeRequestStatus(47, "complete");


		//SuperAdmin:
		String insertUser = "Insert Into userAccount Values (-1, 'superAdmin', 'superAdmin999', 'admin', 'Super', 'Admin', CURRENT TIMESTAMP )";
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(insertUser);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


}
