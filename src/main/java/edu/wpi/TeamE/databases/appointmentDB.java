package edu.wpi.TeamE.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class appointmentDB {

	static Connection connection = makeConnection.makeConnection().getConnection();

	public static void createAppointmentTable() {
		//TODO: before deleting any users, save their information from userAccount into CSV
		String query = "Create Table appointment( " +
				"    appointmentID Int Primary Key, " +
				"    patientID Int References userAccount (userID) on delete cascade , " +
				"    doctorID Int References userAccount (userID) on delete cascade, " +
				" 	 nodeID varchar(31) References node (nodeID) on delete cascade,  " +
				"    startTime timeStamp, " +
				"    endTime timestamp, " +
				"    Constraint appointmentUnique Unique(patientID, startTime, endTime) " +
				")";


		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
 		e.printStackTrace();
			System.err.println("error creating appointment table");
		}
	}

	/**
	 * creates an appointment and adds to the appointmentDB table
	 * @param patientID is the ID of the patient making the appointment
	 * @param startTime is when the appointment starts
	 * @param endTime is when the appointment ends
	 * @param doctorID is the doctor assigned to the appointment
	 * @return an int (0 if add fails, 1 if add succeeded)
	 */
	public static int addAppointment(int patientID, String startTime, String endTime, int doctorID) {

		String insertAddApt = "insert into appointment where patientID = ?, startTime = ?, endTime = ?, doctor = ?";

		try (PreparedStatement prepState = connection.prepareStatement(insertAddApt)) {
			prepState.setInt(1, patientID);
			prepState.setString(2, startTime);
			prepState.setString(3, endTime);
			prepState.setInt(4, doctorID);

			prepState.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into appointment table inside function addAppointment()");
			return 0;
		}
	}

	/**
	 * edits an appointment
	 * @param appointmentID is the ID of the appointment
	 * @param newStartTime is the new start time of the appointment
	 * @param newEndTime is the new end time of the appointment
	 * @param newDoctorID is the new doctor assigned
	 * @return an int (0 if add fails, 1 if add succeeded)
	 */
	public static int editAppointment(int appointmentID, String newStartTime, String newEndTime, Integer newDoctorID) {

		boolean added = false;

		String query = "update appointment set ";

		if (newStartTime!= null) {
			query = query + " startTime = '" + newStartTime + "'";

			added = true;
		}
		if (newEndTime != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + "endTime = '" + newEndTime + "'";
			added = true;
		}
		if (newDoctorID != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + "doctorID = '" + newDoctorID + "'";
			added = true;
		}

		query = query + " where appointmentID = " + appointmentID;
		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			prepState.executeUpdate();
			prepState.close();
			return 1;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error in updating appointment table");
			return 0;
		}

	}

	/**
	 * cancels an appointment given the appointmentID
	 * @param appointmentID is the ID of the appointment
	 * @return an int (0 if add fails, 1 if add succeeded)
	 */
	public static int cancelAppointment(int appointmentID) {

		String insertCancelQuery = "delete from appointment where appointmentID = ?";

		try (PreparedStatement prepState = connection.prepareStatement(insertCancelQuery)) {
			prepState.setInt(1, appointmentID);
			prepState.execute();
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error deleting from cancelAppointment inside function cancelAppointment()");
			return 0;
		}

	}

}

