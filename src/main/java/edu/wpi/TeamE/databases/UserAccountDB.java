package edu.wpi.TeamE.databases;

import java.sql.*;
import java.util.ArrayList;

public class UserAccountDB {

	static Connection connection = makeConnection.makeConnection().getConnection();

	/**
	 * Uses executes the SQL statements required to create the userAccount table.
	 * This table has the attributes:
	 * - userID: used to identify the individual. Every account must have a unique userID and account must have one.
	 * - email: the email must be under 31 characters long and must be unique.
	 * - password: the password must be under 31 characters long.
	 * - userType: is the type of account the user is enrolled with. The valid options are: "visitor", "patient", "doctor", "admin".
	 * - firstName: the user's first name.
	 * - lastName: the user's last name.
	 * - creationTime: a time stamp that is added to the table when an account is created
	 */
	public static void createUserAccountTable() {

		String query = "Create Table userAccount (" +
				"userID    Int Primary Key," +
				"email     Varchar(31) Unique Not Null," +
				"password  Varchar(31)        Not Null," +
				"userType  Varchar(31)," +
				"firstName Varchar(31)," +
				"lastName  Varchar(31)," +
				"creationTime Timestamp, " +
				"Constraint userIDLimit Check ( userID != 0 )," +
				"Constraint passwordLimit Check (Length(password) >= 8 )," +
				"Constraint userTypeLimit Check (userType In ('visitor', 'patient', 'doctor', 'admin', 'nurse', 'EMT', 'floralPerson', 'pharmacist', 'security', 'electrician', 'custodian')))";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

			createUserAccountTypeViews();

		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("error creating userAccount table");
		}
	}

	/**
	 * Uses executes the SQL statements required to create views for different types of users. The views created
	 * are: visitorAccount, patientAccount, doctorAccount, adminAccount.
	 */
	public static void createUserAccountTypeViews() {
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View visitorAccount As " +
					"Select * " +
					"From useraccount " +
					"Where usertype = 'visitor'";
			stmt.execute(sqlQuery);

		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating visitorAccount view");
		}
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View patientAccount As " +
					"Select * " +
					"From useraccount " +
					"Where usertype = 'patient'";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating patientAccount view");
		}
		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View doctorAccount As " +
					"Select * " +
					"From useraccount " +
					"Where usertype = 'doctor'";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating doctorAccount view");
		}

		try {
			Statement stmt = connection.createStatement();
			String sqlQuery = "Create View adminAccount As " +
					"Select * " +
					"From useraccount " +
					"Where usertype = 'admin'";
			stmt.execute(sqlQuery);
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("error creating adminAccount view");
		}

	}

	/**
	 * This is allows a visitor to create a user account giving them more access to the certain requests available
	 * @param email     this is the user's email that is connected to the account the are trying to create
	 * @param password  this is a password that the user will use to log into the account
	 * @param firstName this is the user's first name that is associated with the account
	 * @param lastName  this is the user's last name that is associated with the account
	 */
	public static void addUserAccount(String email, String password, String firstName, String lastName) {
		String insertUser = "Insert Into useraccount Values ((Select Count(*) From useraccount) + 1, ?, ?, 'visitor', ?, ?, Current Timestamp)";
		try (PreparedStatement prepState = connection.prepareStatement(insertUser)) {
			prepState.setString(1, email);
			prepState.setString(2, password);
			prepState.setString(3, firstName);
			prepState.setString(4, lastName);
			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			//mapEditor.errorPopup("This email all ready has an account");
			System.err.println("Error inserting into userAccount inside function insertUserAccount()");
		}
	}

	/**
	 * This is allows an administrator or someone with access to the database to be able to create a user account
	 * with more permissions giving them more access to the certain requests available. This is function should not
	 * be used while the app is being run.
	 * @param email     this is the user's email that is connected to the account the are trying to create
	 * @param password  this is a password that the user will use to log into the account
	 * @param userType  this is the type of account that the individual is being assigned to
	 * @param firstName this is the user's first name that is associated with the account
	 * @param lastName  this is the user's last name that is associated with the account
	 */
	public static void addSpecialUserType(String email, String password, String userType, String firstName, String lastName) {
		String insertUser = "Insert Into useraccount Values ((Select Count(*) From useraccount) + 1, ?, ?, ?, ?, ?, Current Timestamp)";
		try (PreparedStatement prepState = connection.prepareStatement(insertUser)) {
			prepState.setString(1, email);
			prepState.setString(2, password);
			prepState.setString(3, userType);
			prepState.setString(4, firstName);
			prepState.setString(5, lastName);
			prepState.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			//showError("This email all ready has an account");
			System.err.println("Error inserting into userAccount inside function insertUserAccount()");
		}
	}


	/**
	 * Function for logging a user in with their unique email and password
	 * @param email    is the user's entered email
	 * @param password is the user's entered password
	 * @return 0 when the credentials does not match with any user in the database, and returns the userID otherwise
	 */
	public static int userLogin(String email, String password) {
		String userLoginS1 = "Select Count(*) As verification From userAccount Where email = ? And password = ?";
		try (PreparedStatement userLoginPS1 = connection.prepareStatement(userLoginS1)) {
			userLoginPS1.setString(1, email);
			userLoginPS1.setString(2, password);
			ResultSet rset = userLoginPS1.executeQuery();
			int verification = 0;
			if (rset.next()) {
				verification = rset.getInt("verification");
			}
			rset.close();
			if(verification == 0){
				return verification;
			} else {
				String userLoginS2 = "Select userId From userAccount Where email = ? And password = ?";
				try (PreparedStatement userLoginPS2 = connection.prepareStatement(userLoginS2)) {
					userLoginPS2.setString(1, email);
					userLoginPS2.setString(2, password);
					rset = userLoginPS2.executeQuery();

					int userID = 0;
					if (rset.next()) {
						userID = rset.getInt("userID");
					}
					rset.close();
					return userID;
				}
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("userLogin() error");
		}
		return 0;
	}










}
