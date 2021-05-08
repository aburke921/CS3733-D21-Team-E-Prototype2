package edu.wpi.cs3733.D21.teamE.database;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;


import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;

public class DatabaseService {

    static Connection connection = makeConnection.makeConnection("jdbc:derby:BWDB;create=true").getConnection();

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static void terminateConnection() {


        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }catch (SQLException e){
            e.printStackTrace();
        }
//        if(driverString.contains("localhost")){
//            try {
//                DriverManager.getConnection("jdbc:derby://localhost:1527/bw;shutdown=true");
//            }catch (SQLException e){
//                e.printStackTrace();
//                System.err.println("Error terminating localhost connection");
//            }
//        }
//        else{
//            try{
//                DriverManager.getConnection("jdbc:derby:BWDB;shutdown=true");
//            }catch (SQLException e){
//                e.printStackTrace();
//                System.err.println("Error terminating embedded connection");
//            }
//        }
    }

}
