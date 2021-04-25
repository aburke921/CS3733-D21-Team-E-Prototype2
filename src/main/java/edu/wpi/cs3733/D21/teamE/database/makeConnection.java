package edu.wpi.cs3733.D21.teamE.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class makeConnection {

	private static makeConnection con;

	private makeConnection() {
	}

	public static makeConnection getCon() {
		return SingletonHelper.con;
	}

	private static class SingletonHelper {
		private static final makeConnection con = new makeConnection();
	}

}
