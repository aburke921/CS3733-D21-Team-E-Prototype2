
package edu.wpi.cs3733.D21.teamE.email;
import com.sun.mail.smtp.SMTPTransport;


import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;



public class sendEmail {

//	public static void main(String args[]) throws MessagingException {
//
//
////		String email = DB.getEmail(App.userID);
//		//sendAppointmentRemind("aburke@wpi.edu", "May 5th 2021");
//
//
//	}


	public static void sendAppointmentConfirmation(String email, String time, String fullName) throws MessagingException {

		String subject = "BWH Appointment Confirmation!!";
		String emailMessage = "Hello " + fullName + ", \n\n"
				+ "We are confirming your appointment scheduled with us at "
				+ time + " at Brigham & Women's hospital. If you have any issues with your "
				+ "appointment please log into the application and edit your appointment information. \n\n"
				+ "We look forward to seeing you soon, \n"
				+ "Emerald Emus BWH";

		sendEmail(email, subject, emailMessage);

	}


	public static void sendRequestConfirmation(String email, String body) throws MessagingException {

		String subject = "BWH Request Confirmation!!";
		String emailMessage = body;

		sendEmail(email, subject, emailMessage);

	}

	// call this when user edits an appointment
	public static void sendUpdatedAppointment(String email, String time) throws MessagingException {

		String subject = "BWH Appointment Changed!!";
		String emailMessage = "Hello " + email + ", \n\n"
				+ "Your appointment details at Brigham & Women's hospital have been updated. If you have any issues with your "
				+ "appointment please log into the application and edit your appointment information. \n\n"
				+ "We look forward to seeing you soon, \n"
				+ "Emerald Emus BWH";

		sendEmail(email, subject, emailMessage);

	}


	public static void sendEmail(String email, String subject, String emailBody) throws MessagingException {
		//Sets up getting permission to send email
		String password;


		if (System.getProperty("os.name").substring(0, 3).equals("Mac")) {
			password = "olpqmnwwpkhihwjs";
		} else {
			password = "bwpeledauxsuxqja";
		}

		Properties props = System.getProperties();
		props.put("mail.smtps.host", "smtp.gmail.com");
		props.put("mail.smtps.auth", "true");
		Session session = Session.getInstance(props, null);

		//Email information
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("engineeringsoftware3733@gmail.com"));
		;
		msg.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email, false));
		msg.setSubject(subject);
		msg.setText(emailBody);
		msg.setSentDate(new Date());


		SMTPTransport t = (SMTPTransport) session.getTransport("smtps");

		t.connect("smtp.gmail.com", "engineeringsoftware3733@gmail.com", "SoftEngCS3733");
		t.sendMessage(msg, msg.getAllRecipients());

		System.out.println("Response: " + t.getLastServerResponse());
		t.close();
	}

}







