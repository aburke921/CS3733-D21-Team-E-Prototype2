package edu.wpi.TeamE.databases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Edb {
	public static void main(String[] args) {
		System.out.println("Starting Edb V1.0");
		int i = 0;
		int firstArg = 0;

		if (args.length > 0) {
			try {
				firstArg = Integer.parseInt(args[0]);

			} catch (NumberFormatException e) {
				System.err.println("Argument" + args[0] + " must be an integer from 1-5.");
				System.exit(1);
			}
			switch (firstArg) {
				case 1: {
					System.out.println("1 - Node Information\n");
					Connection connection = makeConnection();
					try {

						assert connection != null;
						Statement stmt = connection.createStatement();
						String query = "select * from node";
						ResultSet rset = stmt.executeQuery(query);

						while (rset.next()) {
							String NodeID = rset.getString("NodeID");
							int xCoord = rset.getInt("xCoord");
							int yCoord = rset.getInt("yCoord");
							String floor = rset.getString("floor");
							String building = rset.getString("building");
							String nodeType = rset.getString("nodeType");
							String longName = rset.getString("longName");
							String shortName = rset.getString("shortName");

							System.out.println("NodeID: " + NodeID);
							System.out.println("xCoord: " + xCoord);
							System.out.println("yCoord: " + yCoord);
							System.out.println("floor: " + floor);
							System.out.println("building: " + building);
							System.out.println("nodeType: " + nodeType);
							System.out.println("longName: " + longName);
							System.out.println("shortName: " + shortName);

							System.out.println();
						}

						rset.close();
						stmt.close();

					} catch (SQLException e) {
						// e.printStackTrace();
					}

					break;
				}

				case 2: {
					System.out.println("2 - Update Node Coordinates\n");
					Scanner input = new Scanner(System.in);
					System.out.println("Which node are you trying to update on?\n");
					String nodeID = input.nextLine();
					System.out.println(
							"What new coordinates would you update the node "
									+ nodeID
									+ " to?\n"
									+ "Separate with one space between two numbers");
					int xCoord = Integer.parseInt(input.next());
					int yCoord = Integer.parseInt(input.next());
					Connection connection = makeConnection();
					try {
						assert connection != null;
						Statement stmt = connection.createStatement();

						int rset =
								stmt.executeUpdate(
										"update node set xCoord = "
												+ xCoord
												+ " , yCoord = "
												+ yCoord
												+ " where nodeID = '"
												+ nodeID
												+ "'");
						stmt.close();
						System.out.println("updated the coordinates");
					} catch (SQLException e) {
						// e.printStackTrace();
					}

					break;
				}

				case 3: {
					System.out.println("3 - Update Node Location Long Name\n");
					Scanner input = new Scanner(System.in);
					System.out.println("Which node are you trying to update on?\n");
					String nodeID = input.nextLine();
					System.out.println("What new long name would you update the node " + nodeID + " to?\n");
					String longName = input.nextLine();
					Connection connection = makeConnection();
					try {
						assert connection != null;
						Statement stmt = connection.createStatement();
						int rset =
								stmt.executeUpdate(
										"update node set longName = '"
												+ longName
												+ "' where nodeID = '"
												+ nodeID
												+ "'");

						stmt.close();
					} catch (SQLException e) {
						// e.printStackTrace();
					}
					break;
				}

				case 4: {
					System.out.println("4 - Edge Information\n");
					Connection connection = makeConnection();
					try {
						assert connection != null;
						Statement stmt = connection.createStatement();

						String query = "select * from hasEdge";
						ResultSet rset = stmt.executeQuery(query);

						while (rset.next()) {

							String EdgeID = rset.getString("edgeID");
							String StartNode = rset.getString("startNode");
							String EndNode = rset.getString("endNode");

							System.out.println("EdgeID: " + EdgeID);
							System.out.println("Start Node: " + StartNode);
							System.out.println("End Node: " + EndNode);

							System.out.println();
						}

						rset.close();
						stmt.close();

					} catch (SQLException e) {
						// e.printStackTrace();
					}
					break;
				}

				case 5: {
					System.out.println("5 - Exit Program\n");
					System.exit(0);
					break;
				}

				default:
					System.out.println(
							"1 - Node Information\n"
									+ "2 - Update Node Coordinates\n"
									+ "3 - Update Node Location Long Name\n"
									+ "4 - Edge Information\n"
									+ "5 - Exit Program\n");
					System.exit(0);
			}
		} else {
			System.out.println(
					"1 - Node Information\n"
							+ "2 - Update Node Coordinates\n"
							+ "3 - Update Node Location Long Name\n"
							+ "4 - Edge Information\n"
							+ "5 - Exit Program\n");

			System.exit(0);
		}
	}

	public static Connection makeConnection() {

		// Initialize DB
		System.out.println("Starting connection to Apache Derby\n");
		Connection connection;
		try {

			Properties props = new Properties();
			props.put("user", "admin");
			props.put("password", "admin");

			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

			// remove :memory to have persistent database
			try {
				connection = DriverManager.getConnection("jdbc:derby:ourDatabase;create=true", props);

				refactorTables(connection);

				// deleteAllTables(connection);

				return connection;

			} catch (SQLException e) {
				// e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void createTables(Connection connection) {

		try {
			Statement stmt = connection.createStatement();
			stmt.execute(
					"create table node"
							+ "("
							+ "    nodeID    varchar(31) primary key,"
							+ "    xCoord    int not null,"
							+ "    yCoord    int not null,"
							+ "    floor     varchar(5) not null,"
							+ "    building  varchar(20),"
							+ "    nodeType  varchar(10),"
							+ "    longName  varchar(50),"
							+ "    shortName varchar(35),"
							+ "    unique (xCoord, yCoord, floor)"
							+ ")");

		} catch (SQLException e) {
			// e.printStackTrace();
		}

		try {

			Statement stmt = connection.createStatement();
			stmt.execute(
					"create table hasEdge"
							+ "("
							+ "    edgeID    varchar(63) primary key,"
							+ "    startNode varchar(31) not null references node (nodeID),"
							+ "    endNode   varchar(31) not null references node (nodeID),"
							+ "    unique (startNode, endNode)"
							+ ")");

			// Needs a way to calculate edgeID, either in Java or by a sql trigger
			// Probably in Java since it's a PK

		} catch (SQLException e) {
			// e.printStackTrace();
		}
	}

	public static void deleteAllTables(Connection connection) {

		try {
			Statement stmt = connection.createStatement();
			stmt.execute("drop table hasEdge");
			stmt.execute("drop table node");
		} catch (SQLException e) {
			// e.printStackTrace();
		}
	}

	public static void populateNodes(Connection connection) {
		ArrayList<String[]> dbRows = new ArrayList<>();

		try {

			// creates a file with the file name we are looking to read
			File file = new File("L1Nodes.csv");
			// used to read data from a file
			FileReader fr = new FileReader(file);

			// used to read the text from a character-based input stream.
			BufferedReader br = new BufferedReader(fr);

			String line;
			String[] tempArr;

			// reads first line (this is the header of each file and we don't need it)
			br.readLine();

			// if there is something in the file (after line 1)
			while ((line = br.readLine()) != null) {

				// adds arguments into the array separated by the commas ("," is when it knows the next
				// index)
				tempArr = line.split(",");

				// each line is a row in our database
				dbRows.add(tempArr);
			}

			// closes the BufferedReader
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// we've read in the CSV file and now we want to update each column in the DB

		// this fnc is passed the array of string[] which contain all info we need
		// should go through the function and populate each row with correct info
		for (String[] line : dbRows) {
			try {
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(
						"insert into node values ('"
								+ line[0]
								+ "',"
								+ Integer.valueOf(line[1])
								+ ","
								+ Integer.valueOf(line[2])
								+ ",'"
								+ line[3]
								+ "','"
								+ line[4]
								+ "','"
								+ line[5]
								+ "','"
								+ line[6]
								+ "','"
								+ line[7]
								+ "')");

			} catch (SQLException e) {

				// e.printStackTrace();
			}
		}
	}

	public static void populateHasEdge(Connection connection) {

		ArrayList<String[]> dbEdges = new ArrayList<>();

		try {

			// creates a file with the file name we are looking to read
			File file = new File("L1Edges.csv");
			// used to read data from a file
			FileReader fr = new FileReader(file);

			// used to read the text from a character-based input stream.
			BufferedReader br = new BufferedReader(fr);

			String line;
			String[] tempArr;

			// reads first line (this is the header of each file and we don't need it)
			br.readLine();

			// if there is something in the file (after line 1)
			while ((line = br.readLine()) != null) {

				// adds arguments into the array separated by the commas ("," is when it knows the next
				// index)
				tempArr = line.split(",");

				// inserts the line of data read from the csv file into the hasEdge table
				dbEdges.add(tempArr);
			}

			// System.out.println("edges file read");

			// closes the BufferedReader
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		for (String[] edge : dbEdges) {
			try {
				Statement stmt = connection.createStatement();
				stmt.execute(
						"insert into hasEdge values ('" + edge[0] + "', '" + edge[1] + "', '" + edge[2] + "')");
			} catch (SQLException e) {
				// e.printStackTrace();
			}
		}
	}

	public static void populateEdgeLength(Connection connection) {

		ArrayList<String[]> dbEdges = new ArrayList<>();

		try {

			// creates a file with the file name we are looking to read
			File file = new File("L1Edges.csv");
			// used to read data from a file
			FileReader fr = new FileReader(file);

			// used to read the text from a character-based input stream.
			BufferedReader br = new BufferedReader(fr);

			String line;
			String[] tempArr;

			// reads first line (this is the header of each file and we don't need it)
			br.readLine();

			// if there is something in the file (after line 1)
			while ((line = br.readLine()) != null) {

				// adds arguments into the array separated by the commas ("," is when it knows the next
				// index)
				tempArr = line.split(",");

				// inserts the line of data read from the csv file into the hasEdge table
				dbEdges.add(tempArr);
			}

			// System.out.println("edges file read");

			// closes the BufferedReader
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		for (String[] edge : dbEdges) {
			try {
				Statement stmt = connection.createStatement();
        /*stmt.execute(
                "insert into edgeLength values ('" + edge[1] + "', '" + edge[2] + "', '" +
                        (Math.sqrt(Math.pow(edge[1].getX() - edge[2].getX(), 2) + Math.pow(edge[1].getY() - edge[2].getY(), 2))) + "')"); // WIP*/
			} catch (SQLException e) {
				// e.printStackTrace();
			}
		}
	}

  /*

  first time this program is run, the tables will be created and populated
     - no errors are thrown

  everytime the program is run after the running the first time
  - error is caught since the program is trying to recreate and repopulate tables which are already present
    - the tables will be deleted
    - the tables will be recreated and repopulated

   */

	public static void refactorTables(Connection connection) {
		try {
			createTables(connection);
			populateNodes(connection);
			populateHasEdge(connection);
			populateEdgeLength(connection);
			throw new SQLException();
		} catch (SQLException e) {
			deleteAllTables(connection);
			createTables(connection);
			populateNodes(connection);
			populateHasEdge(connection);
			populateEdgeLength(connection);
		}
	}
}
