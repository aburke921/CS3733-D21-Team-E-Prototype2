package edu.wpi.TeamE;

import edu.wpi.TeamE.databases.makeConnection;
import javafx.util.Pair;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import edu.wpi.TeamE.algorithms.pathfinding.Node;

import edu.wpi.TeamE.algorithms.pathfinding.Edge;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class DatabaseTests {

    makeConnection connection;

    @BeforeAll
    public void setConnection(){
        connection = makeConnection.makeConnection();
    }

    @BeforeEach
    public void setupTables(){
        try {
            connection.deleteAllTables();
            connection.createTables();
            System.out.println("Tables were reset");
        } catch (Exception e) {
            try {
                connection.createTables();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            System.out.println("Tables were created");
        }
    }

    @Test
    @DisplayName("testGetNodeInfo")
    public void testGetNodeInfo(){
        connection.addNode("test123", 12, 13, "floor", "building", "nodeType", "longName", "shortName");

        Node testNode1 = new Node("test123", 12, 13, "floor", "building", "nodeType", "longName", "shortName");

        Node testNode2 = connection.getNodeInfo("test123");

        assertTrue(testNode1.equals(testNode2));
    }

    @Test
    @DisplayName("testGetNodeLite")
    public void testGetNodeLite(){
        connection.addNode("test233", 22, 33, "floor", null, null, null, null);

        Node testNode1 = new Node("test233", 22, 33, "floor", null, null, null, null);

        Node testNode2 = connection.getNodeLite("test233");

        assertTrue(testNode1.equals(testNode2));
    }

    @Test
    @DisplayName("testGetEdgeInfo")
    public void testGetEdgeInfo(){
        connection.addNode("testEdge1", 121, 122, "h1", "String", "String", "String", "String");
        connection.addNode("testEdge2", 12, 15, "h1", "String", "String", "String", "String");
        connection.addNode("testEdge3", 122, 123, "h1", "String", "String", "String", "String");
        connection.addNode("testEdge4", 124, 153, "h1", "String", "String", "String", "String");

        connection.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");
        connection.addEdge("testEdge3_testEdge4", "testEdge3", "testEdge4");
        connection.addEdge("testEdge2_testEdge4", "testEdge2", "testEdge4");

        ArrayList<Pair<Float, String>> listofEdgeInfo = new ArrayList<Pair<Float, String>>();
        ArrayList<Pair<Float, String>> resultEdgeInfo = new ArrayList<Pair<Float, String>>();

        double length3 = Math.pow((122-124),2);
        double length4 = Math.pow((123-153),2);

        double length34 = Math.sqrt((length3+length4));

        double length1 = Math.pow((12-124),2);
        double length2 = Math.pow((15-153),2);

        double length24 = Math.sqrt((length1+length2));

        listofEdgeInfo.add(new Pair<Float, String>((float)length34,"testEdge3"));
        listofEdgeInfo.add(new Pair<Float, String>((float)length24,"testEdge2"));

        resultEdgeInfo = connection.getEdgeInfo("testEdge4");

        assertTrue(resultEdgeInfo.equals(listofEdgeInfo));

    }


    @Test
    @DisplayName("testGetAllNodes")
    public void testGetAllNodes(){


        connection.addNode("testEdge1", 121, 122, "h1", "String", "String", "String", "String");
        connection.addNode("testEdge2", 12, 15, "h1", "String", "String", "String", "String");
        connection.addNode("testEdge3", 122, 123, "h1", "String", "String", "String", "String");
        connection.addNode("testEdge4", 124, 153, "h1", "String", "String", "String", "String");


        Node n1 = new Node("testEdge1", 121, 122, "h1", "String", "String", "String", "String");
        Node n2 = new Node("testEdge2", 12, 15, "h1", "String", "String", "String", "String");
        Node n3 = new Node("testEdge3", 122, 123, "h1", "String", "String", "String", "String");
        Node n4 = new Node("testEdge4", 124, 153, "h1", "String", "String", "String", "String");

        ArrayList<Node> testNodeArray = new ArrayList<>();
        testNodeArray.add(n1);
        testNodeArray.add(n2);
        testNodeArray.add(n3);
        testNodeArray.add(n4);

        ArrayList<Node> nodeArray = connection.getAllNodes();

        boolean allCorrect = true;
        boolean nodeID = false;
        boolean xCoord = false;
        boolean yCoord = false;
        boolean floor = false;
        boolean building = false;
        boolean nodeType = false;
        boolean longName = false;
        boolean shortName = false;

        if(testNodeArray.size() == nodeArray.size()){
            for(int node = 0; node < nodeArray.size(); node++){
                Node returnedNode = nodeArray.get(node);
                Node correctNode = testNodeArray.get(node);
                if(returnedNode.get("id").equals(correctNode.get("id"))){
                    nodeID = true;
                }
                if(returnedNode.getX() == correctNode.getX()){
                    xCoord = true;
                }
                if(returnedNode.getY() == correctNode.getY()){
                    yCoord = true;
                }
                if(returnedNode.get("floor").equals(correctNode.get("floor"))){
                    floor = true;
                }
                if(returnedNode.get("building").equals(correctNode.get("building"))){
                    building = true;
                }
                if(returnedNode.get("type").equals(correctNode.get("type"))){
                    nodeType = true;
                }
                if(returnedNode.get("longName").equals(correctNode.get("longName"))){
                    longName = true;
                }
                if(returnedNode.get("shortName").equals(correctNode.get("shortName"))){
                    shortName = true;
                }
                if(nodeID && xCoord && yCoord && floor && building && nodeType && longName && shortName == false){
                    allCorrect = false;
                }
            }
        }
        else{
            allCorrect = false;
        }

        assertTrue(allCorrect);

    }


    @Test
    @DisplayName("testGetAllEdges")
    public void testGetAllEdges(){
        connection.addNode("test1", 0, 0,"test", "test", "test", "test", "test");
        connection.addNode("test2", 2, 2,"test", "test", "test", "test", "test");
        connection.addEdge("test1_test2", "test1", "test2");

        ArrayList<Edge> listofEdges = new ArrayList<>();


        double length1 = Math.pow((0-2),2);
        double length2 = Math.pow((0-2),2);

        double length12 = Math.sqrt((length1+length2));

        Edge edge1 = new Edge("test1_test2", "test1", "test2", length12);

        listofEdges.add(edge1);

        ArrayList<Edge> resultListofEdges = connection.getAllEdges();

        boolean allCorrect = true;
        boolean edgeID = false;
        boolean length = false;
        if(listofEdges.size() == resultListofEdges.size()){
            for(int edge = 0; edge < resultListofEdges.size(); edge++){
                Edge returnedEdge = resultListofEdges.get(edge);
                Edge correctEdge = listofEdges.get(edge);
                if(returnedEdge.getLength() == correctEdge.getLength()){
                    length = true;
                }
                if(returnedEdge.getId().equals(correctEdge.getId())){
                    edgeID = true;
                }
                if(length && edgeID == false){
                    allCorrect = false;
                }
            }
        }
        else{
            allCorrect = false;
        }
        assertTrue(allCorrect);
    }

    @Test
    @DisplayName("testAddNode")
    public void testAddNode(){
        // set result to 0
        int testResult = 0;

        // if this works, testResult should be 1
        testResult = connection.addNode("testNode", 111, 222, "h1", "String", "String", "String", "String");

        assertTrue(testResult == 1);
    }

    @Test
    @DisplayName("testAddEdge")
    public void testAddEdge(){

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

        // set result to 0
        int testResult = 0;

        connection.addNode("originalNode", 121, 122, "h1", "String", "String", "String", "String");

        testResult = connection.modifyNode("originalNode", 100, null, null, null, null, null, null);


        assertTrue(testResult == 1);
    }

    @Test
    @DisplayName("testDeleteEdge")
    public void testDeleteEdge(){

        // set result to 0
        int testResult = 0;

        connection.addNode("testEdge1", 121, 122, "h1", "String", "String", "String", "String");
        connection.addNode("testEdge2", 12, 15, "h1", "String", "String", "String", "String");

        connection.addEdge("testEdge1_testEdge2", "testEdge1", "testEdge2");

        testResult = connection.deleteEdge("testEdge1", "testEdge2");

        System.out.println(testResult);

        assertTrue(testResult == 0);
    }

    @Test
    @DisplayName("testDeleteNode")
    public void testDeleteNode(){

        // set result to 0
        int testResult = 0;

        connection.addNode("testEdge1", 121, 122, "h1", "String", "String", "String", "String");
        testResult = connection.deleteNode("testEdge1");

        assertTrue(testResult == 1);
    }

    @Test
    @DisplayName("testGetListofNodeIDS")
    public void testGetListofNodeIDS(){

        //File nodes = new File("src/main/resources/edu/wpi/TeamE/csv/bwEnodes.csv");
        //File edges = new File("src/main/resources/edu/wpi/TeamE/csv/bwEedges.csv");

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

    //TODO: haven't done yet
    @Test
    @DisplayName("testGetNewCSVFile")
    public void testGetNewCSVFile(){
        connection.addNode("test1", 0, 0,"f1", "b1", "test", "long", "asd");
        connection.addNode("test2", 2, 2,"f2", "b2", "test", "name", "test");
        connection.addNode("test3", 3, 3,"f3", "b3", "test", "test", "hert");
        connection.addNode("test4", 4, 4,"f4", "b4", "test", "fun", "test");

        connection.getNewCSVFile("node");

        File testFile = new File("src/test/resources/edu/wpi/TeamE/outputNodeTest.csv");
        File nodeFile = new File("src/test/resources/edu/wpi/TeamE/outputNodeDB.csv");
        ArrayList<String> testArray = new ArrayList<String>();
        ArrayList<String> nodeArray = new ArrayList<String>();

        try {
            FileReader fr = new FileReader(testFile);
            BufferedReader br = new BufferedReader(fr);

            String line;

            while ((line = br.readLine()) != null) {
                testArray.add(line);
            }

            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("populateTable() outer try/catch error");
        }

        try {
            FileReader fr = new FileReader(nodeFile);
            BufferedReader br = new BufferedReader(fr);

            String line;

            while ((line = br.readLine()) != null) {
                nodeArray.add(line);
            }

            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.err.println("populateTable() outer try/catch error");
        }

        assertEquals(nodeArray, testArray);
    }



}
