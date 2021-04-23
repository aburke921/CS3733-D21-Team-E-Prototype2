package edu.wpi.TeamE.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class appointmentDB {

	static Connection connection = makeConnection.makeConnection().getConnection();

	public static void createAppointmentTable() {

		String query = "Create Table appointment(\n" +
				"    appointmentID Int Primary Key,\n" +
				"    patientID Int References userAccount (userID) on delete cascade,\n" +
				"    startTime timeStamp,\n" +
				"    endTime timestamp,\n" +
				"    Constraint appointmentUnique Unique(patientID, startTime, endTime)\n" +
				")";


		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("error creating appointment table");
		}
	}



	public static void createDoctorExaminesAdmissionTable() {

		String query = "Create Table doctorExaminesAdmission(\n" +
				"    appointmentID Int References appointment(appointmentID) on delete cascade,\n" +
				"    doctorID Int References userAccount(userID) on delete cascade,\n" +
				"    constraint examinesPK Primary Key(appointmentID,doctorID)\n" +
				")";


		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("error creating doctorExaminesAdmission table");
		}
	}




}
