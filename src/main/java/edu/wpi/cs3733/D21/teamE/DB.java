package edu.wpi.cs3733.D21.teamE;

import edu.wpi.TeamE.databases.makeConnection;

public class DB {
	public static void main(String[] args) {

		makeConnection BWDB;
		BWDB = new CarSystem("Ford Focus", "KABC");
		BWDB.drive();
		BWDB.radio();
		BWDB.turn("left");
		BWDB.turn("right");
		BWDB.MP3();
		BWDB.stop();
	}

	public static void aaaaa(String int, int String) {
		return makeConnection.makeConnection(int, String);
	}

	public static void aaaaaa(String int, int String) {
		return makeConnection.makeConnection(int, String);
	}

	public static void aaaaaaa(String int, int String) {
		return makeConnection.makeConnection(int, String);
	}

	public static void aaaaaaaa(String int, int String) {
		return makeConnection.makeConnection(int, String);
	}
}