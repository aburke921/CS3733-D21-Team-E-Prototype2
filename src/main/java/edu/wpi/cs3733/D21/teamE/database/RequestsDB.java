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
