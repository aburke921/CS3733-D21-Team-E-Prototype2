package edu.wpi.TeamE;

import edu.wpi.TeamE.databases.makeConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import edu.wpi.TeamE.algorithms.pathfinding.Node;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

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

        try {
            connection.deleteAllTables();
            connection.createTables();
            System.out.println("Tables were reset");
        } catch (Exception e) {
            connection.createTables();
            System.out.println("Tables were created");
        }

        connection.addNode("test123", 12, 13, "floor", "building", "nodeType", "longName", "shortName");

        Node testNode1 = new Node("test123", 12, 13, "floor", "building", "nodeType", "longName", "shortName");

        Node testNode2 = connection.getNodeInfo("test123");

        assertTrue(testNode1.equals(testNode2));
    }

    @Test
    @DisplayName("testGetNodeLite")
    public void testGetNodeLite(){
        try {
            connection.deleteAllTables();
            connection.createTables();
            System.out.println("Tables were reset");
        } catch (Exception e) {
            connection.createTables();
            System.out.println("Tables were created");
        }

        connection.addNode("test233", 22, 33, "floor", null, null, null, null);

        Node testNode1 = new Node("test233", 22, 33, "floor", null, null, null, null);

        Node testNode2 = connection.getNodeLite("test233");

        assertTrue(testNode1.equals(testNode2));
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
        try {
            connection.deleteAllTables();
            connection.createTables();
            System.out.println("Tables were reset");
        } catch (Exception e) {
            connection.createTables();
            System.out.println("Tables were created");
        }

        // set result to 0
        int testResult = 0;

        // if this works, testResult should be 1
        testResult = connection.addNode("testNode", 111, 222, "h1", "String", "String", "String", "String");

        assertTrue(testResult == 1);
    }

    @Test
    @DisplayName("testAddEdge")
    public void testAddEdge(){
        try {
            connection.deleteAllTables();
            connection.createTables();
            System.out.println("Tables were reset");
        } catch (Exception e) {
            connection.createTables();
            System.out.println("Tables were created");
        }

        // set result to 0
        int testResult = 0;

        connection.addNode("testEdge1", 121, 122, "h1", "String", "String", "String", "String");
        connection.addNode("testEdge2", 12, 15, "h1", "String", "String", "String", "String");

        // if this works, testResult should be 1
        testResult = connection.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");

        assertTrue(testResult == 1);

    }

    @Test
    @DisplayName("testModifyNode")
    public void testModifyNode(){
        try {
            connection.deleteAllTables();
            connection.createTables();
            System.out.println("Tables were reset");
        } catch (Exception e) {
            connection.createTables();
            System.out.println("Tables were created");
        }

        // set result to 0
        int testResult = 0;

        connection.addNode("originalNode", 121, 122, "h1", "String", "String", "String", "String");

        testResult = connection.modifyNode("originalNode", 100, null, null, null, null, null, null);


        assertTrue(testResult == 1);
    }

    @Test
    @DisplayName("testDeleteEdge")
    public void testDeleteEdge(){

        try {
            connection.deleteAllTables();
            connection.createTables();
            System.out.println("Tables were reset");
        } catch (Exception e) {
            connection.createTables();
            System.out.println("Tables were created");
        }

        // set result to 0
        int testResult = 0;

        connection.addNode("testEdge1", 121, 122, "h1", "String", "String", "String", "String");
        connection.addNode("testEdge2", 12, 15, "h1", "String", "String", "String", "String");

        connection.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");

        testResult = connection.deleteEdge("testEdge1", "testEdge2");

        assertTrue(testResult == 1);
    }

    @Test
    @DisplayName("testDeleteNode")
    public void testDeleteNode(){
        try {
            connection.deleteAllTables();
            connection.createTables();
            System.out.println("Tables were reset");
        } catch (Exception e) {
            connection.createTables();
            System.out.println("Tables were created");
        }

        // set result to 0
        int testResult = 0;

        connection.addNode("testEdge1", 121, 122, "h1", "String", "String", "String", "String");
        testResult = connection.deleteNode("testEdge1");

        assertTrue(testResult == 1);
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
