package edu.wpi.TeamE.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class appointmentDB {

	static Connection connection = makeConnection.makeConnection().getConnection();

	public static void createAppointmentTable() {
		//TODO:
		String query = "Create Table appointment( " +
				"    appointmentID Int Primary Key, " +
				"    patientID Int References userAccount (userID) on delete cascade , " +
				"    doctorID Int References userAccount (userID) on delete cascade, " +
				"    startTime timeStamp, " +
				"    endTime timestamp, " +
				"    Constraint appointmentUnique Unique(patientID, startTime, endTime) " +
				")";


		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("error creating appointment table");
		}
	}

//	public static void createDoctorExaminesAdmissionTable() {
//
//		String query = "Create Table doctorExaminesAdmission(\n" +
//				"    appointmentID Int References appointment(appointmentID) on delete cascade,\n" +
//				"    doctorID Int References userAccount(userID) on delete cascade,\n" +
//				"    constraint examinesPK Primary Key(appointmentID,doctorID)\n" +
//				")";
//
//
//		try (PreparedStatement prepState = connection.prepareStatement(query)) {
//
//			prepState.execute();
//
//		} catch (SQLException e) {
////			e.printStackTrace();
//			System.err.println("error creating doctorExaminesAdmission table");
//		}
//	}

	/**
	 * creates an appointment and adds to the appointmentDB table
	 * @param patientID is the ID of the patient making the appointment
	 * @param startTime is when the appointment starts
	 * @param endTime is when the appointment ends
	 * @param doctorID is the doctor assigned to the appointment
	 * @return an int (0 if add fails, 1 if add succeeded)
	 */
	public static int addAppointment(int patientID, String startTime, String endTime, int doctorID) {
		// calls addDoctorExaminesAdmission()
		return 0;
	}

	/**
	 * edits an appointment
	 * @param appointmentID is the ID of the appointment
	 * @param newStartTime is the new start time of the appointment
	 * @param newEndTime is the new end time of the appointment
	 * @param newDoctorID is the new doctor assigned
	 * @return an int (0 if add fails, 1 if add succeeded)
	 */
	public static int editAppointment(int appointmentID, String newStartTime, String newEndTime, int newDoctorID) {
		// calls editDoctorExaminesAdmission()
		return 0;
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
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error deleting from cancelAppointment inside function addSecurityRequest()");
		}

		return 0;
	}
//
//	/**
//	 * adds a doctor to the given appointment
//	 * @param appointmentID is the ID of the appointment
//	 * @param doctorID is the ID of the doctor
//	 * @return an int (0 if add fails, 1 if add succeeded)
//	 */
//	public static int addDoctorExaminesAdmission(int appointmentID, int doctorID) {
//
//		String insertAppointment = "insert into doctorExaminesAdmission where appointmentID = ?, doctorID = ?";
//
//		try (PreparedStatement prepState = connection.prepareStatement(insertAppointment)) {
//			prepState.setInt(1, appointmentID);
//			prepState.setInt(2, doctorID);
//			prepState.execute();
//		}
//		catch(SQLException e) {
//			e.printStackTrace();
//			System.err.println("Error adding into doctorExaminesAdmission inside function addDoctorExaminesAdmission()");
//		}
//		return 0;
//	}
//
//	/**
//	 * edits a doctorExaminesAdmission
//	 * @param newAppointmentID
//	 * @param newDoctorID
//	 * @return an int (0 if add fails, 1 if add succeeded)
//	 */
//	public static int editDoctorExaminesAdmission(int newAppointmentID, int newDoctorID) {
//
//		String insertAppointment = "update doctorExaminesAdmission ";
//
//		boolean added = false;
//		String query = "update securityServ set ";
//
//		if (newAppointmentID != null) {
//			query = query + " appointmentID = '" + newAppointmentID + "'";
//
//			added = true;
//		}
//		if (newDoctorID != null) {
//			if (added == true) {
//				query = query + ", ";
//			}
//			query = query + "doctorID = '" + newDoctorID + "'";
//			added = true;
//		}
//
//		query = query + " where appointmentID = " + newAppointmentID;
//		try (PreparedStatement prepState = connection.prepareStatement(query)) {
//			prepState.executeUpdate();
//			prepState.close();
//			return 1;
//		} catch (SQLException e) {
////			e.printStackTrace();
//			System.err.println("Error in editing editDoctorExaminesAdmission request");
//			return 0;
//		}
//
//		return 0;
//	}





}
