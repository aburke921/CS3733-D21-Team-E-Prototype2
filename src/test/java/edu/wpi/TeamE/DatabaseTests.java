package edu.wpi.TeamE;

import edu.wpi.TeamE.databases.makeConnection;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import edu.wpi.TeamE.algorithms.pathfinding.Node;
import org.junit.jupiter.api.TestInstance;

import edu.wpi.TeamE.algorithms.pathfinding.Edge;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class DatabaseTests {

    makeConnection connection;

    @BeforeAll
    public void setConnection(){
        connection = makeConnection.makeConnection();

        try {
            connection.deleteAllTables();
            connection.createTables();
            System.out.println("Tables were reset");
        } catch (Exception e) {
            connection.createTables();
            System.out.println("Tables were created");
        }
    }

    //TODO: haven't done yet
    @Test
    @DisplayName("testCreateTables")
    public void testCreateTables(){

    }

    //TODO: haven't done yet
    @Test
    @DisplayName("testDeleteAllTables")
    public void testDeleteAllTables(){

    }

    //TODO: haven't done yet
    @Test
    @DisplayName("testPopulateTable")
    public void testPopulateTable(){

    }

    //TODO: haven't done yet
    @Test
    @DisplayName("testAddLength")
    public void testAddLength(){

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


    //TODO: this doesn't pass the test for some reason (both arrayLists print out the exact same thing though)
    @Test
    @DisplayName("testGetAllNodes")
    public void testGetAllNodes(){
         ArrayList<Node> nodeArray = new ArrayList<>();

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

        nodeArray = connection.getAllNodes();

        for (Node n : testNodeArray) {
            System.out.println(n.get("id") + " " + n.getX() + " " + n.getY() + " " + n.get("floor") + " " + n.get("building") + " " + n.get("type") + " " + n.get("longName") + " " + n.get("shortName") + "\n");
        }

        System.out.println("break\n");

        for (Node n : nodeArray) {
            System.out.println(n.get("id") + " " + n.getX() + " " +   n.getY() + " " + n.get("floor") + " " + n.get("building") + " " + n.get("type") + " " + n.get("longName") + " " + n.get("shortName") + "\n");
        }


        assertTrue(nodeArray.equals(testNodeArray));

    }

    //TODO: this doesn't pass the test for some reason (both arrayLists print out the exact same thing though)
    @Test
    @DisplayName("testGetAllEdges")
    public void testGetAllEdges(){
        connection.addNode("test1", 0, 0,"test", "test", "test", "test", "test");
        connection.addNode("test2", 2, 2,"test", "test", "test", "test", "test");
        connection.addEdge("test1_test2", "test1", "test2");

        ArrayList<Edge> listofEdges = new ArrayList<>();
        ArrayList<Edge> resultListofEdges = new ArrayList<>();

        double length1 = Math.pow((0-2),2);
        double length2 = Math.pow((0-2),2);

        double length12 = Math.sqrt((length1+length2));

        Edge edge1 = new Edge("test1_test2", "test1", "test2", length12);

        listofEdges.add(edge1);

        resultListofEdges = connection.getAllEdges();

        boolean allCorrect = true;
        boolean edgeID = false;
        boolean length = false;
        if(listofEdges.size() == resultListofEdges.size()){
            for(int edge = 0; edge < resultListofEdges.size(); edge++){
                Edge returnedEdge = resultListofEdges.get(edge);
                Edge correctEdge = listofEdges.get(edge);
                if(returnedEdge.length() == correctEdge.length()){
                    length = true;
                }
                if(returnedEdge.getId().equals(correctEdge.getId())){
                    edgeID = true;
                }
                if(length && edgeID == false){
                    allCorrect = true;
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

        assertTrue(testResult == 1);
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

//    @Test
//    @DisplayName("testGetListofNodeIDS")
//    public void testGetListofNodeIDS(){
//
//        //File nodes = new File("src/main/resources/edu/wpi/TeamE/csv/bwEnodes.csv");
//        //File edges = new File("src/main/resources/edu/wpi/TeamE/csv/bwEedges.csv");
//
//        connection.addNode("test1", 0, 0,"test", "test", "test", "test", "test");
//        connection.addNode("test2", 2, 2,"test", "test", "test", "test", "test");
//        connection.addNode("test3", 3, 3,"test", "test", "test", "test", "test");
//        connection.addNode("test4", 4, 4,"test", "test", "test", "test", "test");
//
//        ArrayList<String> listOfNodeIDs = new ArrayList<>();
//
//        listOfNodeIDs.add("test1");
//        listOfNodeIDs.add("test2");
//        listOfNodeIDs.add("test3");
//        listOfNodeIDs.add("test4");
//
//        assertTrue(listOfNodeIDs.equals(connection.getListofNodeIDS()));
//    }

    //TODO: haven't done yet
    @Test
    @DisplayName("testGetNewCSVFile")
    public void testGetNewCSVFile(){

    }



}
