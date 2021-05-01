package edu.wpi.cs3733.D21.teamE.email;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


import javax.mail.MessagingException;

public class AppointmentJob implements Job{

        public void execute(JobExecutionContext context) throws JobExecutionException {

            String emailMessage = "Hello, \n\n" + "Your appointment with us has been confirmed. We look forward to seeing you soon!\n";
            System.out.println("Job1 --->>> Time is " + new Date());
            try {
                email.sendEmail("nupi.shukla@gmail.com","Brigham & Women's Appointment Confirmation", emailMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

}
