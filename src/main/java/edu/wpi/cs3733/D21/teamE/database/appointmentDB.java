package edu.wpi.cs3733.D21.teamE.database;

import java.sql.*;

public class appointmentDB {

	static Connection connection = makeConnection.makeConnection().connection;

	public static void createAppointmentTable() {
		//TODO: before deleting any users, save their information from userAccount into CSV
		String query = "Create Table appointment( " +
				"    appointmentID Int Primary Key, " +
				"    patientID Int References useraccount (userid) On Delete Cascade , " +
				"    doctorID Int References useraccount (userid) On Delete Cascade, " +
				"    startTime varchar(31), " +
				"    Constraint appointmentUnique Unique(patientID, startTime) " +
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
	 * @param doctorID  is the doctor assigned to the appointment
	 * @return an int (0 if add fails, 1 if add succeeded)
	 */
	public static int addAppointment(int patientID, String startTime, Integer doctorID) {

		String insertAddApt = "insert into appointment values(" + (getMaxAppointmentID() + 1) + ",?, ?, ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertAddApt)) {
			prepState.setInt(1, patientID);
			prepState.setInt(2, doctorID);
			prepState.setString(3, startTime);

			prepState.executeUpdate();
			prepState.close();

			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into appointment table inside function addAppointment()");
			return 0;
		}

	}

	public static int getMaxAppointmentID() {

		int maxID = 0;

		String query = "Select Max(appointmentid) As maxAppointmentID From appointment";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();

			if (rset.next()) {
				maxID = rset.getInt("maxAppointmentID");
			}

			prepState.close();
			return maxID;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("Error in getMaxAppointmentID()");
			return 0;
		}

	}

	/**
	 * edits an appointment
	 * @param appointmentID is the ID of the appointment
	 * @param newStartTime  is the new start time of the appointment
	 * @param newDoctorID   is the new doctor assigned
	 * @return an int (0 if add fails, 1 if add succeeded)
	 */
	public static int editAppointment(int appointmentID, String newStartTime, Integer newDoctorID) {

		boolean added = false;

		String query = "update appointment set ";


		if (newStartTime != null) {
			query = query + "startTime = '" + newStartTime + "'";

			added = true;
		}
		if (newDoctorID != null) {
			if (added == true) {
				query = query + ", ";
			}
			query = query + "doctorID = " + newDoctorID;
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

		String insertCancelQuery = "Delete From appointment Where appointmentid = ?";

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

