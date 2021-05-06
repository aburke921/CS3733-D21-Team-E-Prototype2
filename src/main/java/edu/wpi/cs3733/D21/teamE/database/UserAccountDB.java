package edu.wpi.cs3733.D21.teamE.database;

import edu.wpi.cs3733.D21.teamE.views.CovidSurveyObj;
import edu.wpi.cs3733.D21.teamE.views.UserManagement.User;

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
	public static int createUserAccountTable() {

		String query = "Create Table userAccount " +
				"( " +
				"userID              Int Primary Key, " +
				"email               Varchar(100) Unique Not Null, " +
				"password            Varchar(31)        Not Null, " +
				"userType            Varchar(31)        Not Null, " +
				"firstName           Varchar(31)        Not Null, " +
				"lastName            Varchar(31)        Not Null, " +
				"creationTime        Timestamp          Not Null, " +
				"covidStatus     varchar(31), " +
				"lastCovidSurveyDate Date, " +
				"lastParkedNodeID    Varchar(31) References node, " +
				"Constraint userIDLimit Check ( userID != 0 ), " +
				"Constraint passwordLimit Check ( Length(password) >= 5 ) " +
				")";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

			//createUserAccountTypeViews();

		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("|--- Failed to create userAccount table");
			return 0;
		}
		return 1;
	}

//	/**
//	 * Uses executes the SQL statements required to create views for different types of users. The views created
//	 * are: visitorAccount, patientAccount, doctorAccount, adminAccount.
//	 */
//	public static void createUserAccountTypeViews() {
//		try {
//			Statement stmt = connection.createStatement();
//			String sqlQuery = "Create View visitorAccount As " +
//					"Select * " +
//					"From useraccount " +
//					"Where usertype = 'visitor'";
//			stmt.execute(sqlQuery);
//
//		} catch (SQLException e) {
//			// e.printStackTrace();
//			System.err.println("error creating visitorAccount view");
//		}
//		try {
//			Statement stmt = connection.createStatement();
//			String sqlQuery = "Create View patientAccount As " +
//					"Select * " +
//					"From useraccount " +
//					"Where usertype = 'patient'";
//			stmt.execute(sqlQuery);
//		} catch (SQLException e) {
//			// e.printStackTrace();
//			System.err.println("error creating patientAccount view");
//		}
//		try {
//			Statement stmt = connection.createStatement();
//			String sqlQuery = "Create View doctorAccount As " +
//					"Select * " +
//					"From useraccount " +
//					"Where usertype = 'doctor'";
//			stmt.execute(sqlQuery);
//		} catch (SQLException e) {
//			// e.printStackTrace();
//			System.err.println("error creating doctorAccount view");
//		}
//
//		try {
//			Statement stmt = connection.createStatement();
//			String sqlQuery = "Create View adminAccount As " +
//					"Select * " +
//					"From useraccount " +
//					"Where usertype = 'admin'";
//			stmt.execute(sqlQuery);
//		} catch (SQLException e) {
//			// e.printStackTrace();
//			System.err.println("error creating adminAccount view");
//		}
//
//	}

	/**
	 * This is allows a visitor to create a user account giving them more access to the certain requests available
	 * @param email     this is the user's email that is connected to the account the are trying to create
	 * @param password  this is a password that the user will use to log into the account
	 * @param firstName this is the user's first name that is associated with the account
	 * @param lastName  this is the user's last name that is associated with the account
	 */
	public static void addUserAccount(String email, String password, String firstName, String lastName) {
		String insertUser = "Insert Into useraccount Values ((Select Count(*) From useraccount) + 1, ?, ?, 'visitor', ?, ?, Current Timestamp, '', Null, Null)";
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
	 * be used while the app is being run. This function can not be used to add a working admin account.
	 * @param email     this is the user's email that is connected to the account the are trying to create
	 * @param password  this is a password that the user will use to log into the account
	 * @param userType  this is the type of account that the individual is being assigned to
	 * @param firstName this is the user's first name that is associated with the account
	 * @param lastName  this is the user's last name that is associated with the account
	 */
	public static void addSpecialUserType(String email, String password, String userType, String firstName, String lastName) {
		String insertUser = "Insert Into useraccount Values ((Select Count(*) From useraccount) + 1, ?, ?, ?, ?, ?, Current Timestamp, '', Null, Null)";
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
	 * This is allows an administrator or someone with access to the database to be able to edit a user account
	 * with more permissions giving them more access to the certain requests available. This is function should not
	 * be used while the app is being run.
	 * @param email     this is the user's email that is connected to the account the are trying to edit
	 * @param password  this is a password that the user will use to log into the account
	 * @param userType  this is the type of account that the individual is being assigned to
	 * @param firstName this is the user's first name that is associated with the account
	 * @param lastName  this is the user's last name that is associated with the account
	 * @// TODO: 4/27/21 JavaDoc return explanation, also, consider bool return?
	 */
	public static int editUserAccount(int userID, String email, String password, String userType, String firstName, String lastName) {
		boolean added = false;
		String query = "Update userAccount Set ";

		if (email != null && !email.equals("")) {
			query = query + " email = '" + email + "'";

			added = true;
		}
		if (password != null && !password.equals("")) {
			if (added) {
				query = query + ", ";
			}
			query = query + "password = '" + password + "'";
			added = true;
		}
		if (userType != null && !userType.equals("")) {
			if (added) {
				query = query + ", ";
			}
			query = query + "userType = '" + userType + "'";
			added = true;
		}
		if (firstName != null && !firstName.equals("")) {
			if (added) {
				query = query + ", ";
			}
			query = query + " firstName = '" + firstName + "'";

			added = true;
		}
		if (lastName != null && !lastName.equals("")) {
			if (added) {
				query = query + ", ";
			}
			query = query + "lastName = '" + lastName + "'";
		}

		query = query + " where userID = " + userID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error in updating userAccount table");
			return 0;
		}
	}

	/**
	 * This is allows an administrator or someone with access to the database to be able to edit a user account
	 * with more permissions giving them more access to the certain requests available. This is function should not
	 * be used while the app is being run.
	 * @param userID this is the ID of the user the admin is trying to delete
	 */
	public static int deleteUserAccount(int userID) {
		String insertDeleteQuery = "Delete From useraccount Where userid = ?";

		try (PreparedStatement prepState = connection.prepareStatement(insertDeleteQuery)) {
			prepState.setInt(1, userID);
			prepState.execute();
			return 1;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.err.println("Error deleting in function deleteUserAccount()");
			return 0;
		}

	}

	/**
	 * Function for logging a user in with their unique email and password
	 * @param email    is the user's entered email
	 * @param password is the user's entered password
	 * @return 0 when the credentials does not match with any user in the database, and returns the userID otherwise
	 */
	public static int userLogin(String email, String password) {
		String userLoginS1 = "Select Count(*) As verification From useraccount Where email = ? And password = ?";
		try (PreparedStatement userLoginPS1 = connection.prepareStatement(userLoginS1)) {
			userLoginPS1.setString(1, email);
			userLoginPS1.setString(2, password);
			ResultSet rset = userLoginPS1.executeQuery();
			int verification = 0;
			if (rset.next()) {
				verification = rset.getInt("verification");
			}
			rset.close();
			if (verification == 0) {
				return verification;
			} else if (verification == 1) {
				String userLoginS2 = "Select userid From useraccount Where email = ? And password = ?";
				try (PreparedStatement userLoginPS2 = connection.prepareStatement(userLoginS2)) {
					userLoginPS2.setString(1, email);
					userLoginPS2.setString(2, password);
					rset = userLoginPS2.executeQuery();

					int userID = 0;
					if (rset.next()) {
						userID = rset.getInt("userID");
						if (rset.next()) {
							System.err.println("Fatal Error: Entered User Account credentials exist twice in database (should not happen)");
						}
					}
					rset.close();
					return userID;
				}
			} else {
				System.err.println("Fatal Error: Entered User Account credentials exist twice in database (should not happen)");
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			System.err.println("userLogin() error");
		}
		return 0;
	}

	public static String getUserType(int userID) {
		if (userID == 0) {
			return "guest";
		} else {
			String insertUser = "Select usertype From userAccount Where userID = ?";
			try (PreparedStatement prepState = connection.prepareStatement(insertUser)) {
				prepState.setInt(1, userID);

				ResultSet rset = prepState.executeQuery();

				String userType = null;
				if (rset.next()) {
					userType = rset.getString("userType");
				}

				return userType;
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("error in getUserType()");
			}
		}

		return "guest";
	}

	public static ArrayList<User> getAllUsers() {
		ArrayList<User> listOfUsers = new ArrayList<>();

		String query = "Select * From userAccount";

		try (PreparedStatement prepStat = connection.prepareStatement(query)) {

			ResultSet rset = prepStat.executeQuery();

			while (rset.next()) {
				int userID = rset.getInt("userID");
				String email = rset.getString("email");
				String userType = rset.getString("userType");
				String firstName = rset.getString("firstName");
				String lastName = rset.getString("lastName");

				listOfUsers.add(new User(userType, userID, firstName, lastName, email));
			}

			rset.close();

		} catch (SQLException e) {
			System.err.println("getAllUsers Error : " + e);
		}
		return listOfUsers;
	}


	/**
	 *
	 * @param covidSurveyObj is the covidSurveyObject
	 * @param userID  is the user's ID that we are submitting
	 * @return
	 */
	public static boolean submitCovidSurvey(CovidSurveyObj covidSurveyObj, int userID) {
		String status = covidSurveyObj.getStatus();

		String query = "Update userAccount Set covidStatus = ?, lastCovidSurveyDate = Current Date Where userID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, userID);
			if (preparedStatement.executeUpdate() == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		//System.err.println("Error in submitCovidSurvey() from UserAccountDB");
		return false;
	}

	/**
	 * Checks if a user have a unsafe Covid survey
	 * @param userID is the user's ID that we are checking
	 * @return true if user has a safe survey, false if user has a dangerous survey
	 */
	public static boolean isUserCovidSafe(int userID) {


		String query = "Select covidStatus From userAccount Where userID = ? ";

		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userID);
			ResultSet rset = preparedStatement.executeQuery();
			if (rset.next()) {
				System.out.println(rset.getString("covidStatus").equals("Safe"));
			}
			return rset.getString("covidStatus").equals("Safe");
		} catch (SQLException e) {
			System.err.println("Error in isUserCovidSafe() from UserAccountDB");
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isUserCovidRisk(int userID) {

		String query = "Select covidStatus From userAccount Where userID = ? ";

		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userID);
			ResultSet rset = preparedStatement.executeQuery();
			if (rset.next()) {
				System.out.println(rset.getString("covidStatus").equals("Unsafe"));
			}
			return rset.getString("covidStatus").equals("Unsafe");
		} catch (SQLException e) {
			System.err.println("Error in isUserCovidSafe() from UserAccountDB");
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isUserCovidUnmarked(int userID) {


		String query = "Select covidStatus From userAccount Where userID = ? ";

		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userID);
			ResultSet rset = preparedStatement.executeQuery();
			if (rset.next()) {
				//System.out.println(rset.getString("covidStatus").equals("Needs to Be Reviewed"));
			}
			return rset.getString("covidStatus").equals("Needs to Be Reviewed");
		} catch (SQLException e) {
			System.err.println("Error in isUserCovidSafe() from UserAccountDB");
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Checks if a user have filled their COVID survey today
	 * @param userID is the user's ID that we are checking
	 * @return true if user has filled a survey today, false if user did not fill a survey today
	 */
	public static boolean filledCovidSurveyToday(int userID) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"Select Count(lastCovidSurveyDate) As filledToday " +
						"From userAccount " +
						"Where userID = ? " +
						"  And lastCovidSurveyDate = current date")) {
			preparedStatement.setInt(1, userID);
			ResultSet rset = preparedStatement.executeQuery();
			if (rset.next()) {
				return rset.getInt("filledToday") != 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.err.println("Error in filledCovidSurveyToday() from UserAccountDB");
		return false;
	}

	/**
	 * Submits a Parking Slot to the server, the nodeID will have to be of type PARK or lastParkedNodeID will be replaced with null
	 * @param nodeID is the result nodeID that we are submitting
	 * @param userID is the user's ID that we are submitting
	 * @return true if successfully changed one row, false otherwise
	 */
	public static boolean submitParkingSlot(String nodeID, int userID) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"Update userAccount " +
						"Set lastParkedNodeID = (Select nodeID " +
						"From node " +
						"Where nodeID = ? " +
						"And nodeType = 'PARK') " +
						"Where userID = ?")) {
			preparedStatement.setString(1, nodeID);
			preparedStatement.setInt(2, userID);
			if (preparedStatement.executeUpdate() == 1) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.err.println("Error in submitParkingSlot() from UserAccountDB");
		return false;
	}

	/**
	 * Get where the user parked
	 * @param userID is the user's ID that we are checking
	 * @return the node where the user parked, null if not exist
	 */
	public static String whereDidIPark(int userID) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"Select lastParkedNodeID " +
						"From userAccount " +
						"Where userID = ? ")) {
			preparedStatement.setInt(1, userID);
			ResultSet rset = preparedStatement.executeQuery();
			if (rset.next()) {
				return rset.getString("lastParkedNodeID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.err.println("Error in isUserCovidSafe() from UserAccountDB");
		return null;
	}

	public static String getUserName(int userID) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"Select firstName, lastName " +
				"From userAccount " +
				"Where userID = ?")) {
			preparedStatement.setInt(1, userID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				String firstNameSpace = resultSet.getString("firstName") + " ";
				return firstNameSpace + resultSet.getString("lastName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.err.println("Error in getUserName() from UserAccountDB");
		return null;
	}

}
