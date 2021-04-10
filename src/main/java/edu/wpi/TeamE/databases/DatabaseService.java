package edu.wpi.TeamE.databases;

import com.google.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseService {
  /*
   Database service class. This class will be loaded as a Singleton by Guice.
  */

  ArrayList<String[]> dbRows = new ArrayList<>();

  private final Connection connection;

  @Inject
  DatabaseService(Connection connection) {
    this.connection = connection;
  }

  int rows = 0;

  public void insertData(ArrayList<String[]> arrayList) {
    for (String[] line : arrayList) {
      try {
        Statement stmt = connection.createStatement();
        String query =
            "INSERT into node(nodeID, xCoord, yCoord, floor, building, nodeType, longName, shortName)"
                + " VALUES("
                + "'"
                + line[0]
                + "',"
                + Integer.valueOf(line[1])
                + ","
                + Integer.valueOf(line[2])
                + ","
                + "'"
                + line[3]
                + "',"
                + "'"
                + line[4]
                + "',"
                + "'"
                + line[5]
                + "',"
                + "'"
                + line[6]
                + "',"
                + "'"
                + line[7]
                + "'"
                + ")";
        rows = stmt.executeUpdate(query);
        rows++;
      } catch (SQLException e) {
        log.error(e.getMessage());
      }
    }
    log.info("Rows Impacted: " + rows);
  }

  public void createTables() {

    try {
      Statement stmt = connection.createStatement();
      String query =
          "CREATE TABLE node ("
              + "nodeID VARCHAR(31) PRIMARY KEY, "
              + "xCoord INTEGER NOT NULL, "
              + "yCoord INTEGER NOT NULL, "
              + "floor VARCHAR(5) NOT NULL, "
              + "building VARCHAR(20), "
              + "nodeType VARCHAR(10), "
              + "longName VARCHAR(50), "
              + "shortName VARCHAR(35), "
              + "CONSTRAINT coordinateFloorUNQ UNIQUE (xCoord, yCoord, floor))";

      stmt.execute(query);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {

      Statement stmt = connection.createStatement();
      String query =
          "CREATE TABLE hasEdge ("
              + "edgeID VARCHAR(63) PRIMARY KEY, "
              + "startNode VARCHAR(31) NOT NULL, "
              + "endNode VARCHAR(31) NOT NULL, "
              + "CONSTRAINT startEndUNQ UNIQUE (startNode, endNode), "
              + "CONSTRAINT startNodeFK FOREIGN KEY (startNode) REFERENCES node(nodeID), "
              + "CONSTRAINT endNodeFK FOREIGN KEY (endNOde) REFERENCES node(nodeID))";

      stmt.execute(query);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void populateDB() {

    try {

      // creates a file with the file name we are looking to read
      File file = new File("/Users/ashley/Desktop/Project B/L1Nodes.csv");
      // used to read data from a file
      FileReader fr = new FileReader(file);

      // used to read the text from a character-based input stream.
      BufferedReader br = new BufferedReader(fr);

      String line = "";
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
    insertData(dbRows);
  }

  public void populateHasEdge() {
    try {

      // creates a file with the file name we are looking to read
      File file = new File("/Users/ashley/Desktop/Project B/L1Edges.csv");
      // used to read data from a file
      FileReader fr = new FileReader(file);

      // used to read the text from a character-based input stream.
      BufferedReader br = new BufferedReader(fr);

      String line = "";
      String[] tempArr;

      // reads first line (this is the header of each file and we don't need it)
      br.readLine();

      // if there is something in the file (after line 1)
      while ((line = br.readLine()) != null) {

        // adds arguments into the array separated by the commas ("," is when it knows the next
        // index)
        tempArr = line.split(",");

        // inserts the line of data read from the csv file into the hasEdge table
        try {
          Statement stmt = connection.createStatement();
          String query =
              "INSERT INTO hasEdge(edgeID, startNode, endNode) VALUES( "
                  + "'"
                  + tempArr[0]
                  + "', "
                  + "'"
                  + tempArr[1]
                  + "', "
                  + "'"
                  + tempArr[2]
                  + "')";

          stmt.execute(query);

        } catch (SQLException e) {
          e.printStackTrace();
        }
      }

      // closes the BufferedReader
      br.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
