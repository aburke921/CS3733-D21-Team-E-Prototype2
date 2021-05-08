package edu.wpi.cs3733.D21.teamE;


import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

	//define logger
	static Logger logger = Logger.getLogger("BWH");


	public static void main(String[] args) {

		try {
			//define handler
			FileHandler handler = new FileHandler("BWHApplication.log", true);
			SimpleFormatter formatter = new SimpleFormatter();
			handler.setFormatter(formatter);
			handler.setLevel(Level.ALL);

			//add handler to logger
			logger.addHandler(handler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.setLevel(Level.ALL);


		logger.info("Starting Application");
		App.launch(App.class, args);
	}
}
