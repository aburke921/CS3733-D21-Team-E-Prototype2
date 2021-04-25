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
			stmt.execute("Drop Table appointment");
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

		//floralPerson:
		//31-39
		UserAccountDB.addSpecialUserType("amyw@gmail.com","floralPerson1","floralPerson","Amy", "Castaneda");
		UserAccountDB.addSpecialUserType("elsaf@gmail.com","floralPerson2","floralPerson","Elsa", "Figueroa");
		UserAccountDB.addSpecialUserType("carolines@gmail.com","floralPerson3","floralPerson","Caroline", "Sutton");
		UserAccountDB.addSpecialUserType("milesl@gmail.com","floralPerson4","floralPerson","Miles", "Long");
		UserAccountDB.addSpecialUserType("hasanp@gmail.com","floralPerson5","floralPerson","Hasan", "Perry");
		UserAccountDB.addSpecialUserType("caroliner@gmail.com","floralPerson6","floralPerson","Caroline", "Richardson");
		UserAccountDB.addSpecialUserType("milesc@gmail.com","floralPerson7","floralPerson","Miles", "Carroll");
		UserAccountDB.addSpecialUserType("sethw@gmail.com","floralPerson8","floralPerson","Seth", "Warner");
		UserAccountDB.addSpecialUserType("darrenr@gmail.com","floralPerson9","floralPerson","Darren", "Rossi");

		//Floral Requests: //RequestID: 1-9
		RequestsDB.addFloralRequest(13,31, "ADEPT00101", "Adam", "Roses", 1, "None", "Hi Adam, I am so sorry to hear about your accident. Please get better soon!");
		RequestsDB.addFloralRequest(13,32, "ADEPT00102", "Abraham", "Tulips", 6, "Round", "Dear Abraham, hope these flowers help you feel better. The team really misses you and hope you will be ready to go by the championship");
		RequestsDB.addFloralRequest(14,33, "ADEPT00102", "Xavier", "Carnations", 12, "Square", "Get well soon");
		RequestsDB.addFloralRequest(15,34, "ADEPT00301", "Nikki", "Assortment", 1, "None", "");
		RequestsDB.addFloralRequest(15,35, "ADEPT00101", "Monica", "Roses", 6, "Tall", "Love and miss you!!");
		RequestsDB.addFloralRequest(17,36, "DDEPT00102", "Amy", "Tulips", 12, "Square", "Enjoy the flowers");
		RequestsDB.addFloralRequest(17,37, "ADEPT00102", "Alfred", "Carnations", 1, "Tall", "Miss you!");
		RequestsDB.addFloralRequest(19,38, "ADEPT00101", "Caroline", "Assortment", 6, "Round", "Sorry I forgot to warn you about the slippery stairs, I hope these flowers can make you feel better!");
		RequestsDB.addFloralRequest(19,39, "ADEPT00301", "Carrie", "Assortment", 12, "Round", "");

		//custodian:
		//40 - 48
		UserAccountDB.addSpecialUserType("crystalh@gmail.com","custodian1","custodian","Crystal", "Harvey");
		UserAccountDB.addSpecialUserType("minnien@gmail.com","custodian2","custodian","Minnie", "Newman");
		UserAccountDB.addSpecialUserType("aylab@gmail.com","custodian3","custodian","Ayla", "Black");
		UserAccountDB.addSpecialUserType("lenardj@gmail.com","custodian4","custodian","Lenard", "Jacobs");
		UserAccountDB.addSpecialUserType("juanw@gmail.com","custodian5","custodian","Juan", "Williams");
		UserAccountDB.addSpecialUserType("mayj@gmail.com","custodian6","custodian","May", "Jimenez");
		UserAccountDB.addSpecialUserType("hermanb@gmail.com","custodian7","custodian","Herman", "Bull");
		UserAccountDB.addSpecialUserType("umarr@gmail.com","custodian8","custodian","Umar", "Rojas");
		UserAccountDB.addSpecialUserType("reubenf@gmail.com","custodian9","custodian","Reuben", "Francolin");

		//pharmacist:
		//49 - 60
		UserAccountDB.addSpecialUserType("clarab@gmail.com","pharmacist1","pharmacist","Clara", "Bryan");
		UserAccountDB.addSpecialUserType("jenniferc@gmail.com","pharmacist2","pharmacist","Jennifer", "Cunningham");
		UserAccountDB.addSpecialUserType("jakb@gmail.com","pharmacist3","pharmacist","Jak", "Bishop");
		UserAccountDB.addSpecialUserType("benc@gmail.com","pharmacist4","pharmacist","Ben", "Coles");
		UserAccountDB.addSpecialUserType("gloriaw@gmail.com","pharmacist5","pharmacist","Gloria", "Webster");
		UserAccountDB.addSpecialUserType("robbiet@gmail.com","pharmacist6","pharmacist","Robbie", "Turner");
		UserAccountDB.addSpecialUserType("lucasw@gmail.com","pharmacist7","pharmacist","Lucas", "Whittaker");
		UserAccountDB.addSpecialUserType("alecr@gmail.com","pharmacist8","pharmacist","Alec", "Rees");
		UserAccountDB.addSpecialUserType("francescaf@gmail.com","pharmacist9","pharmacist","Francesca", "Ferguson");
		UserAccountDB.addSpecialUserType("josiep@gmail.com","pharmacist10","pharmacist","Josie", "Pittman");
		UserAccountDB.addSpecialUserType("willf@gmail.com","pharmacist11","pharmacist","Will", "Ford");
		UserAccountDB.addSpecialUserType("billyg@gmail.com","pharmacist12","pharmacist","Billy", "Gomez");


		//security
		//61-69
		UserAccountDB.addSpecialUserType("jameso@gmail.com","security1","security","James", "O'Moore");
		UserAccountDB.addSpecialUserType("russella@gmail.com","security2","security","Russell", "Armstrong");
		UserAccountDB.addSpecialUserType("lillianp@gmail.com","security3","security","Lillian", "Peters");
		UserAccountDB.addSpecialUserType("clarad@gmail.com","security4","security","Clara", "Dixon");
		UserAccountDB.addSpecialUserType("herberto@gmail.com","security5","security","Herbert", "Ortega");
		UserAccountDB.addSpecialUserType("calebc@gmail.com","security6","security","Caleb", "Carr");
		UserAccountDB.addSpecialUserType("jasperm@gmail.com","security7","security","Jasper", "Miller");
		UserAccountDB.addSpecialUserType("jenniferb@gmail.com","security8","security","Jennifer", "Brewer");


		//EMT:
		//70 - 78
		UserAccountDB.addSpecialUserType("ciarang@gmail.com","EMT000001","EMT","Ciaran", "Goodwin");
		UserAccountDB.addSpecialUserType("lolab@gmail.com","EMT000002","EMT","Lola", "Bond");
		UserAccountDB.addSpecialUserType("samanthar@gmail.com","EMT000003","EMT","Samantha", "Russell");
		UserAccountDB.addSpecialUserType("calebm@gmail.com","EMT000004","EMT","Caleb", "Myers");
		UserAccountDB.addSpecialUserType("dalec@gmail.com","EMT000005","EMT","Dale", "Coates");
		UserAccountDB.addSpecialUserType("jerrym@gmail.com","EMT000006","EMT","Jerry", "Myers");
		UserAccountDB.addSpecialUserType("bettyw@gmail.com","EMT000007","EMT","Betty", "Warren");
		UserAccountDB.addSpecialUserType("maximr@gmail.com","EMT000008","EMT","Maxim", "Rawlings");
		UserAccountDB.addSpecialUserType("alans@gmail.com","EMT000009","EMT","Alan", "Singh");



		RequestsDB.editRequests(1, 0, "canceled");
		RequestsDB.editRequests(4, 0, "canceled");
		RequestsDB.editRequests(5, 0, "complete");
		RequestsDB.editRequests(6, 0, "canceled");
		RequestsDB.editRequests(7, 0, "complete");


		//Sanitation Requests: //RequestID: 10 - 18
		RequestsDB.addSanitationRequest(20,40, "AREST00101", "Urine Cleanup", "Restroom with urine on the floor", "Medium", "Bill Byrd");
		RequestsDB.addSanitationRequest(20,41, "AREST00103", "Urine Cleanup", "Restroom with urine on the toilet seet", "Medium", "Bill Byrd");
		RequestsDB.addSanitationRequest(24,42, "AREST00103", "Feces Cleanup", "Feces smeared on toilet seats", "High", "Taylor Ramos");
		RequestsDB.addSanitationRequest(25,43, "ARETL00101", "Trash Removal", "Trash can full, starting to smell", "Medium", "Rosa Smith");
		RequestsDB.addSanitationRequest(28,44, "IREST00103", "Feces Cleanup", "Just outside of the bathroom there is a pile of feces. Someone did not make it in time.", "Critical", "Abby Williams");
		RequestsDB.addSanitationRequest(30,45, "IREST00203", "Trash Removal", "Trash can smells bad", "Medium", "Alesha Harris");
		RequestsDB.addSanitationRequest(29,46, "IREST00303", "Trash Removal", "Trash can full. Another one is available so don't rush.", "Low", "Andrew Guerrero");
		RequestsDB.addSanitationRequest(22,47, "HRETL00102", "Urine Cleanup", "Liquid on the floor. Unclear if it is urine. Not a whole lot of it.", "Low", "Simon Daniel");
		RequestsDB.addSanitationRequest(23,48, "IREST00403", "Trash Removal", "", "Low", "Victoria Erickson");


		RequestsDB.editRequests(11, 0, "canceled");
		RequestsDB.editRequests(14, 0, "canceled");
		RequestsDB.editRequests(15, 0, "complete");
		RequestsDB.editRequests(16, 0, "canceled");
		RequestsDB.editRequests(17, 0, "complete");



		//Medicine Delivery Request //RequestID: 19 - 30
		RequestsDB.addMedicineRequest(20, 49, "BLABS00102", "Atorvastatin", 30, "20mg", "Once a day by mouth", "Bill Byrd");
		RequestsDB.addMedicineRequest(20, 50, "BLABS00202", "Lisinopril", 90, "20mg", "Once a day by mouth", "Bill Byrd");
		RequestsDB.addMedicineRequest(21, 51, "IDEPT00103", "Levothyroxine", 90, "12.5mcg", "Once a day my bouth", "Amelia Knight");
		RequestsDB.addMedicineRequest(24, 52, "BLABS00102", "Metformin", 30, "850mg", "Twice a day by mouth", "Taylor Ramos");
		RequestsDB.addMedicineRequest(27, 53, "IDEPT00803", "Amlodipine", 30, "5mg", "Once a day by mouth", "Lauren Bolton");
		RequestsDB.addMedicineRequest(26, 54, "IDEPT00603", "Metoprolol", 90, "400mg", "Once a day by mouth", "Declan Patel");
		RequestsDB.addMedicineRequest(23, 55, "IDEPT00403", "Omeprazole", 90, "40mg", "Three times a day by mouth before a meal", "Victoria Erickson");
		RequestsDB.addMedicineRequest(24, 56, "IDEPT00703", "Simvastatin", 30, "10mg", "Once a day by mouth", "Taylor Ramos");
		RequestsDB.addMedicineRequest(27, 57, "IDEPT00903", "Losartan", 90, "100mg", "Once daily by mouth", "Lauren Bolton");
		RequestsDB.addMedicineRequest(21, 58, "IDEPT00203", "Albuterol", 30, "0.63mg", "3 times a day via nebulizer. 4 times a day if needed.", "Amelia Knight");
		RequestsDB.addMedicineRequest(20, 59, "BLABS00202", "Metformin", 30, "8.5mL", "Once daily with meals.", "Bill Byrd");
		RequestsDB.addMedicineRequest(23, 60, "BLABS00102", "Metformin", 30, "5mL", "Twice a day with meals.", "Victoria Erickson");


		RequestsDB.editRequests(20, 0, "canceled");
		RequestsDB.editRequests(24, 0, "canceled");
		RequestsDB.editRequests(25, 0, "complete");
		RequestsDB.editRequests(26, 0, "canceled");
		RequestsDB.editRequests(27, 0, "complete");



		//Security Requests: //RequestID: 31 - 38
		RequestsDB.addSecurityRequest(20, 61,"HDEPT00203", "Low", "Low");
		RequestsDB.addSecurityRequest(22, 62,"WELEV00E01", "Medium", "Medium");
		RequestsDB.addSecurityRequest(30, 63,"HDEPT00203", "Low", "Low");
		RequestsDB.addSecurityRequest(27, 64,"ePARK00101", "Medium", "High");
		RequestsDB.addSecurityRequest(24, 65,"BDEPT00402", "Medium", "Medium");
		RequestsDB.addSecurityRequest(20, 66,"BDEPT00302", "Low", "Low");
		RequestsDB.addSecurityRequest(25, 67,"CCONF002L1", "High", "Critical");
		RequestsDB.addSecurityRequest(29, 68,"eWALK00701", "Medium", "Medium");


		RequestsDB.editRequests(31, 0, "canceled");
		RequestsDB.editRequests(34, 0, "canceled");
		RequestsDB.editRequests(35, 0, "complete");
		RequestsDB.editRequests(36, 0, "canceled");
		RequestsDB.editRequests(37, 0, "complete");


		//RequestID: 39 - 47
		RequestsDB.addExternalPatientRequest(27,70,"EEXIT00101", "Ambulance", "High Severity", "12334567", "5 minutes", "Patient dropped down into a state of unconsciousness randomly at the store. Patient is still unconscious and unresponsive but has a pulse. No friends or family around during the incident. ");
		RequestsDB.addExternalPatientRequest(30,71,"EEXIT00101", "Ambulance","Low Severity", "4093380", "20 minutes", "Patient coming in with cut on right hand. Needs stitches. Bleeding is stable.");
		RequestsDB.addExternalPatientRequest(22,72,"FDEPT00501", "Helicopter","High Severity", "92017693", "10 minutes", "Car crash on the highway. 7 year old child in the backseat with no seatbelt on in critical condition. Blood pressure is low and has major trauma to the head.");
		RequestsDB.addExternalPatientRequest(20,73,"FDEPT00501", "Helicopter","High Severity", "93754789", "20 minutes", "Skier hit tree and lost consciousness. Has been unconscious for 30 minutes. Still has a pulse.");
		RequestsDB.addExternalPatientRequest(24,74,"EEXIT00101", "Ambulance","Medium Severity", "417592", "10 minutes", "Smoke inhalation due to a fire. No burns but difficult time breathing.");
		RequestsDB.addExternalPatientRequest(28,75,"FDEPT00501", "Helicopter", "High Severity", "44888936", "15 minutes", "Major car crash on highway. Middle aged woman ejected from the passenger's seat. Awake and unresponsive and in critical condition");
		RequestsDB.addExternalPatientRequest(24,76,"EEXIT00101", "Ambulance","Medium Severity", "33337861", "7 minutes", "Patient passed out for 30 seconds. Is responsive and aware of their surroundings. Has no history of passing out.");
		RequestsDB.addExternalPatientRequest(27,77,"EEXIT00101", "Ambulance","Low Severity", "40003829", "10 minutes", "Relocating a patient with lung cancer from Mt.Auburn Hospital.");
		RequestsDB.addExternalPatientRequest(24,78,"FDEPT00501", "Plane","High Severity", "38739983", "12 hours", "Heart transplant organ in route");


		RequestsDB.editRequests(40, 0, "complete");
		RequestsDB.editRequests(42, 0, "complete");
		RequestsDB.editRequests(44, 0, "complete");
		RequestsDB.editRequests(45, 0, "complete");
		RequestsDB.editRequests(47, 0, "complete");


		//SuperAdmin:
		String insertUser1 = "Insert Into userAccount Values (-1, 'superAdmin', 'superAdmin999', 'admin', 'Super', 'Admin', CURRENT TIMESTAMP)";
		String insertUser2 = "Insert Into userAccount Values (-99, 'admin', 'admin', 'admin', 'admin', 'admin', CURRENT TIMESTAMP)";
		String insertUser3 = "Insert Into userAccount Values (99, 'staff', 'staff', 'doctor', 'staff', 'staff', CURRENT TIMESTAMP)";
		String insertUser4 = "Insert Into userAccount Values (100, 'guest', 'guest', 'patient', 'guest', 'visitor', CURRENT TIMESTAMP)";
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(insertUser1);
			stmt.executeUpdate(insertUser2);
			stmt.executeUpdate(insertUser3);
			stmt.executeUpdate(insertUser4);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


}
