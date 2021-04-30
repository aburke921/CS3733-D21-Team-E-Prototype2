
package edu.wpi.cs3733.D21.teamE.email;

import com.sun.mail.smtp.SMTPTransport;
import org.simplejavamail.api.email.CalendarMethod;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.config.ConfigLoader;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.util.Date;
//import java.util.Properties;


public class email{

	public static void main(String args[]) throws MessagingException {

		EmailBuilder("ashleyburke921", "nupi.shukla@gmail.com");
		String password;

		System.out.println(System.getProperty("os.name"));
		if(System.getProperty("os.name").equals("Mac OS X")){
			password = "olpqmnwwpkhihwjs";
		}
		else{
			password = "bwpeledauxsuxqja";
		}

		Properties props = System.getProperties();
		props.put("mail.smtps.host","smtp.gmail.com");
		props.put("mail.smtps.auth","true");
		Session session = Session.getInstance(props, null);
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("engineeringsoftware3733@gmail.com"));;
		msg.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("nupi.shukla@gmail.com", false));
		msg.setSubject("Test "+System.currentTimeMillis());
		msg.setText("Hello World!");
		msg.setHeader("X-Mailer", "Tov Are's program");
		msg.setSentDate(new Date());
		SMTPTransport t =
				(SMTPTransport)session.getTransport("smtps");
		t.connect("smtp.gmail.com", "engineeringsoftware3733@gmail.com", password);
		t.sendMessage(msg, msg.getAllRecipients());
		System.out.println("Response: " + t.getLastServerResponse());
		t.close();

//		// Recipient's email ID needs to be mentioned.
//		String to = "nupi.shukla@gmail.com";
//
//		// Sender's email ID needs to be mentioned
//		String from = "ashleyburke921@gmail.com";
//
//		// Assuming you are sending email from localhost
//		String host = "localhost";
//
//		// Get system properties
//		Properties properties = System.getProperties();
//
//		// Setup mail server
//		properties.setProperty("mail.smtp.host", host);
//
//		// Get the default Session object.
//		Session session = Session.getDefaultInstance(properties);
//
//		try {
//			// Create a default MimeMessage object.
//			MimeMessage message = new MimeMessage(session);
//
//			// Set From: header field of the header.
//			message.setFrom(new InternetAddress(from));
//
//			// Set To: header field of the header.
//			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//			// Set Subject: header field
//			message.setSubject("This is the Subject Line!");
//
//			// Now set the actual message
//			message.setText("This is actual message");
//
//			// Send message
//			Transport.send(message);
//			System.out.println("Sent message successfully....");
//		} catch (MessagingException mex) {
//			mex.printStackTrace();
//		}
	}



	public static void EmailBuilder(String username, String recipientEmail) throws MessagingException {


//		Session session = Session.getDefaultInstance(new Properties());
//
//		Message message = new MimeMessage(session);
//
//		message.setFrom(new InternetAddress(username + "@gmail.com"));
//		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
//		message.setSentDate(new Date());
//		SMTPTransport t =
//				(SMTPTransport)session.getTransport("smtps");
//		t.connect("smtp.gmail.com");
//		t.sendMessage(message, message.getAllRecipients());
	}
}


