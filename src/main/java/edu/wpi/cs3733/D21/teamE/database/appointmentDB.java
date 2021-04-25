package edu.wpi.cs3733.D21.teamE.database;


import java.sql.*;

public class appointmentDB {

	static Connection connection = makeConnection.makeConnection().getConnection();

	/**
	 * creates appointment table in BWDB
	 */
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
	public static int addAppointment(int patientID, long startTime, long endTime, int doctorID) {
		Timestamp sTime = new Timestamp(startTime);
		Timestamp eTime = new Timestamp(endTime);
		String insertAddApt = "insert into appointment values(" + (getMaxAppointmentID() + 1) + ",?, ?, null, ? , ?)";

		try (PreparedStatement prepState = connection.prepareStatement(insertAddApt)) {
			prepState.setInt(1, patientID);
			prepState.setInt(2, doctorID);
			prepState.setTimestamp(3, sTime);
			prepState.setTimestamp(4, eTime);


			prepState.executeUpdate();
			prepState.close();

			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error inserting into appointment table inside function addAppointment()");
			return 0;
		}

	}

	/**
	 * increments appointmentID by 1 each time an appointment is made
	 * @return 0 if ID cannot be incremented, 1 if ID is incremented correctly
	 */
	public static int getMaxAppointmentID(){

		int maxID = 0;

		String query = "Select Max(appointmentID) as maxAppointmentID From appointment";

		try (PreparedStatement prepState = connection.prepareStatement(query)) {
			ResultSet rset = prepState.executeQuery();

			if(rset.next()){
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
	 * @param newStartTime is the new start time of the appointment
	 * @param newEndTime is the new end time of the appointment
	 * @param newDoctorID is the new doctor assigned
	 * @return an int (0 if add fails, 1 if add succeeded)
	 */
	public static int editAppointment(int appointmentID, int newStartTime, int newEndTime, Integer newDoctorID) {

		boolean added = false;

		String query = "update appointment set ";

		Integer newStartTimeI = newStartTime;
		Integer newEndTimeI = newStartTime;

		if (newStartTimeI!= null) {

			long newStartTimelong = (long) newStartTime;
			Long newStartTimeL = newStartTimelong;
			Timestamp sTime = new Timestamp(newStartTimeL);

			query = query + "startTime = '" + sTime + "'";

			added = true;
		}
		if (newEndTimeI != null) {

			long newEndTimelong = (long) newEndTime;
			Long newEndTimeL = newEndTimelong;
			Timestamp eTime = new Timestamp(newEndTimeL);

			if (added == true) {
				query = query + ", ";
			}
			query = query + "endTime = '" + eTime + "'";
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

