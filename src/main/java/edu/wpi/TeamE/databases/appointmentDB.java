package edu.wpi.TeamE.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class appointmentDB {

	static Connection connection = makeConnection.makeConnection().getConnection();

	public static void createAppointmentTable() {

		String query = "Create Table appointment( " +
				"patientID Int References userAccount (userID), " +
				"startTime timeStamp, " +
				"endTime timestamp, " +
				"surveyLink varchar(300), " +
				"Constraint appointmentPK Primary Key(patientID, startTime, endTime))";


		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("error creating appointment table");
		}
	}



	public static void createDoctorExaminesAdmissionTable() {

		String query = "Create Table doctorExaminesAdmission(\n" +
				"    patientID Int ,\n" +
				"    doctorID Int References userAccount(userID),\n" +
				"    startTime timestamp,\n" +
				"    endTime timestamp,\n" +
				"    constraint startEndPK Primary Key(patientID, doctorID,startTime, endTime),\n" +
				"    foreign key (patientID, startTime,endTime) References appointment(patientID,startTime,endTime))";


		try (PreparedStatement prepState = connection.prepareStatement(query)) {

			prepState.execute();

		} catch (SQLException e) {
//			e.printStackTrace();
			System.err.println("error creating doctorExaminesAdmission table");
		}
	}




}
