package edu.wpi.cs3733.D21.teamE.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.email.SheetsAndJava;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// where do we change the embedded driver to the client driver
/**
 * This is a Guice Module config class. This module forces guice to provide an instance of the
 * DatabaseService as a singleton.
 */
public class DatabaseServiceProvider extends AbstractModule {

    private final String realDbUrl = "jdbc:derby://localhost:1527/BWDB";

    @Override
    protected void configure() {}

    /**
     * Provide single connection for database access.
     *
     * @throws SQLException if connection cannot be made
     */
    @Provides
    @Singleton
    public Connection provideConnection() throws SQLException {
        System.out.println("...Connected to the DB");

        Connection connection = DriverManager.getConnection(realDbUrl);

        return connection;
    }

    public static void setUpDB(Connection connection){

//
//
//        int[] sheetIDs = {0, 2040772276, 1678365078, 129696308, 1518069362};
//        File nodes = new File("CSVs/MapEAllnodes.csv");
//        File edges = new File("CSVs/MapEAlledges.csv");
//        boolean tablesExist = allTablesThere();
//
//        if(!tablesExist){
//            System.out.print("...DB missing, repopulating...");
//            try {
//                DB.createAllTables();
//                DB.populateTable("node", nodes);
//                DB.populateTable("hasEdge", edges);
//                addDataForPresentation();
//                DB.populateAbonPainTable();
//                for(int ID : sheetIDs){
//                    SheetsAndJava.deleteSheetData(ID);
//                }
//                System.out.println("Done");
//            } catch (Exception e) {
//                System.out.println("...Tables already there");
//            }
//        }
//        System.out.println("App Initialized.");
//
    }

    public static boolean allTablesThere(){
        return false;
    }









}
