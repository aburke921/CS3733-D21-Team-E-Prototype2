package edu.wpi.TeamE;

import edu.wpi.TeamE.databases.makeConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;


public class DatabaseTests {

    makeConnection connection;

    @BeforeAll
    public void setConnection(){
        connection = makeConnection.makeConnection();
    }


    @Test
    @DisplayName("testCreateTables")
    public void testCreateTables(){

    }

    @Test
    @DisplayName("testDeleteAllTables")
    public void testDeleteAllTables(){

    }

    @Test
    @DisplayName("testPopulateTable")
    public void testPopulateTable(){

    }

    @Test
    @DisplayName("testAddLength")
    public void testAddLength(){

    }

    @Test
    @DisplayName("testGetNodeInfo")
    public void testGetNodeInfo(){

    }

    @Test
    @DisplayName("testGetNodeLite")
    public void testGetNodeLite(){

    }

    @Test
    @DisplayName("testGetEdgeInfo")
    public void testGetEdgeInfo(){

    }


    @Test
    @DisplayName("testGetAllNodes")
    public void testGetAllNodes(){

    }

    @Test
    @DisplayName("testGetAllEdges")
    public void testGetAllEdges(){

    }

    @Test
    @DisplayName("testAddNode")
    public void testAddNode(){

    }

    @Test
    @DisplayName("testAddEdge")
    public void testAddEdge(){

    }

    @Test
    @DisplayName("testModifyNode")
    public void testModifyNode(){

    }

    @Test
    @DisplayName("testDeleteEdge")
    public void testDeleteEdge(){

    }

    @Test
    @DisplayName("testDeleteNode")
    public void testDeleteNode(){

    }

    @Test
    @DisplayName("testGetListofNodeIDS")
    public void testGetListofNodeIDS(){

        File nodes = new File("src/main/resources/edu/wpi/TeamE/csv/bwEnodes.csv");
        File edges = new File("src/main/resources/edu/wpi/TeamE/csv/bwEedges.csv");

        try {
            connection.deleteAllTables();
            connection.createTables();
            System.out.println("Tables were reset");
        } catch (Exception e) {
            connection.createTables();
            System.out.println("Tables were created");
        }

        connection.addNode("test1", 0, 0,"test", "test", "test", "test", "test");
        connection.addNode("test2", 2, 2,"test", "test", "test", "test", "test");
        connection.addNode("test3", 3, 3,"test", "test", "test", "test", "test");
        connection.addNode("test4", 4, 4,"test", "test", "test", "test", "test");

        ArrayList<String> listOfNodeIDs = new ArrayList<>();

        listOfNodeIDs.add("test1");
        listOfNodeIDs.add("test2");
        listOfNodeIDs.add("test3");
        listOfNodeIDs.add("test4");

        assertTrue(listOfNodeIDs.equals(connection.getListofNodeIDS()));
    }


    @Test
    @DisplayName("testGetNewCSVFile")
    public void testGetNewCSVFile(){

    }



}
