
package edu.wpi.cs3733.D21.teamE.email;

import com.sun.mail.smtp.SMTPTransport;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;



public class email{

	public static void main(String args[]) throws MessagingException {

		String email = DB.getEmail(App.userID);
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
				InternetAddress.parse("email", false));
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

	}

}


