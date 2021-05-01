
package edu.wpi.cs3733.D21.teamE.email;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import com.sun.mail.smtp.SMTPTransport;

import java.sql.Timestamp;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;



public class email{

	//Timestamp startTime = new Timestamp(121, 3, 30, 21, 18, 30, 0);

	public static void main(String args[]) throws MessagingException {
		Date startTime = new Date();
		startTime.setTime(startTime.getTime() + 60000);

//		String email = DB.getEmail(App.userID);
		//sendAppointmentRemind("aburke@wpi.edu", "May 5th 2021");

		confirmationEmail(startTime);

	}


	public static void sendAppointmentRemind(String email, String time) throws MessagingException {

		String subject = "BWH Appointment Reminder!!";
		String emailMessage = "Hello " + email + ", \n\n"
				+ "This is a reminder that you have an appointment scheduled for "
				+ time + " at Brigham & Women's hospital. If you have any issues with your "
				+ "appointment please log into the application and edit your appointment information. \n\n"
				+ "We look forward to seeing you soon, \n"
				+ "Emerald Emus BWH";

		sendEmail(email, subject, emailMessage);

	}


	public static void sendEmail(String email, String subject, String emailBody) throws MessagingException{
		//Sets up getting permission to send email
		String password;


		if(System.getProperty("os.name").substring(0,3).equals("Mac")){
			password = "olpqmnwwpkhihwjs";
		}
		else{
			password = "bwpeledauxsuxqja";
		}

		Properties props = System.getProperties();
		props.put("mail.smtps.host","smtp.gmail.com");
		props.put("mail.smtps.auth","true");
		Session session = Session.getInstance(props, null);

		//Email information
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("engineeringsoftware3733@gmail.com"));;
		msg.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email, false));
		msg.setSubject(subject);
		msg.setText(emailBody);
		msg.setSentDate(new Date());



		SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

		t.connect("smtp.gmail.com", "engineeringsoftware3733@gmail.com", password);
		t.sendMessage(msg, msg.getAllRecipients());

		System.out.println("Response: " + t.getLastServerResponse());
		t.close();
	}

	public static void confirmationEmail(Date startTime) {

		try {
			JobDetail job = JobBuilder.newJob(AppointmentJob.class)
					.withIdentity("AppointmentJob", "group1").build();

			SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
					.withIdentity("trigger1", "group1")
					.startAt(startTime)
					.forJob("AppointmentJob", "group1")
					.build();


			Scheduler scheduler1 = new StdSchedulerFactory().getScheduler();
			scheduler1.start();
			scheduler1.scheduleJob(job, trigger);

			Thread.sleep(100000);

			scheduler1.shutdown();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}







