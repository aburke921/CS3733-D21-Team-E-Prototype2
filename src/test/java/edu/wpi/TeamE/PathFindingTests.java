package edu.wpi.TeamE;

import edu.wpi.TeamE.algorithms.pathfinding.AStarSearch;
import edu.wpi.TeamE.algorithms.pathfinding.Node;
import edu.wpi.TeamE.algorithms.pathfinding.Path;
import edu.wpi.TeamE.algorithms.pathfinding.Searcher;
import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.TeamE.databases.makeConnection;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

public class PathFindingTests {
    static Searcher search;
    static ArrayList<Path> expectedList;
    static ArrayList<Pair<String, String>> testList;
    static int index;

    @BeforeAll
    public static void setupExpected(){
        System.out.println("STARTING UP!!!");
        makeConnection con = makeConnection.makeConnection();
        File nodes = new File("src/main/resources/edu/wpi/TeamE/csv/bwEnodes.csv");
        File edges = new File("src/main/resources/edu/wpi/TeamE/csv/bwEedges.csv");
        try {
            con.deleteAllTables();
            System.out.println("Deleted Tables");
            throw new Exception();
            /*
            con.createTables();
            System.out.println("Created Tables");
            con.populateTable("node", nodes);
            System.out.println("Populated nodes");
            con.populateTable("hasEdge", edges);
            System.out.println("Populated edges");
            System.out.println("Tables were reset");
             */
        } catch (Exception e) {
            con.createTables();
            System.out.println("Created Tables");
            con.populateTable("node", nodes);
            System.out.println("Populated nodes");
            con.populateTable("hasEdge", edges);
            System.out.println("Populated edges");
            System.out.println("Tables were created and populated");
        }


        search = new AStarSearch();
        expectedList = new ArrayList<>();
        testList = new ArrayList<>();

        index = 0;
    }

    @Test
    public void getNodeTest(){
        Node Test1 = new Node("FEXIT00301", 2099, 1357, "1", "Tower", "EXIT", "Emergency Department Entrance", "Emergency Entrance");
        assertTrue(Test1.equals(search.getNode("FEXIT00301")));

        Node Test2 = new Node("AHALL00603",1580,2858,"3", "BTM", "HALL", "HallNode" , "HallNode");
        assertTrue(Test2.equals(search.getNode("AHALL00603")));

        Node Test3 = new Node("GHALL02901",1262,1850,"1","Shapiro","HALL","Hallway MapNode 29 Floor 1","Hall");
        assertTrue(Test3.equals(search.getNode("GHALL02901")));

        Node Test4 = new Node("GHALL008L2",1577,2195,"L2","Shapiro","HALL","Hallway MapNode 8 Floor L2","Hall");
        assertTrue(Test4.equals(search.getNode("GHALL008L2")));

    }

    @Test
    public void nodeInfoGettersTest(){

        Node exit = search.getNode("FEXIT00301");
        Node hall = search.getNode("GHALL008L2");
        Node elev = search.getNode("GELEV00N02");
        Node rest = search.getNode("CREST001L2");
        Node dept = search.getNode("GDEPT02402");
        Node stai = search.getNode("FSTAI00301");
        Node serv = search.getNode("CSERV001L1");
        Node labs = search.getNode("GLABS003L2");

        assertEquals("FEXIT00301", exit.get("id"));
        assertEquals(1577, hall.getX());
        assertEquals(1931, elev.getY());
        assertEquals("L2", rest.get("floor"));
        assertEquals("Shapiro", dept.get("building"));
        assertEquals("STAI", stai.get("type"));
        assertEquals("Volunteers Floor L1", serv.get("longName"));
        assertEquals("Cardiovascular Imaging Center", labs.get("shortName"));

    }

    @Test
    public void testLobbyToParking(){
        Pair<String, String> terminalNodes = new Pair<>("FEXIT00201", "ePARK00101");

        Node a1 = search.getNode("FEXIT00201");
        Node a2 = search.getNode("eWALK01001");
        Node a3 = search.getNode("eWALK00701");
        Node a4 = search.getNode("eWALK00601");
        Node a5 = search.getNode("eWALK00401");
        Node a6 = search.getNode("eWALK00301");
        Node a7 = search.getNode("eWALK00201");
        Node a8 = search.getNode("eWALK00101");
        Node a9 = search.getNode("ePARK00101");

        Path exp1 = new Path(a1, a2, a3, a4, a5, a6, a7, a8, a9);

        Path out = search.search(terminalNodes.getKey(), terminalNodes.getValue());
        assertTrue(exp1.equals(out));
    }

    @Test
    public void testParkingToER(){
        Pair<String, String> terminalNodes = new Pair<>("ePARK01701", "FEXIT00301");

        Node b1 = search.getNode("ePARK01701");
        Node b2 = search.getNode("eWALK01601");
        Node b3 = search.getNode("eWALK01301");
        Node b4 = search.getNode("eWALK01201");
        Node b5 = search.getNode("eWALK00801");
        Node b6 = search.getNode("eWALK00901");
        Node b7 = search.getNode("eWALK01101");
        Node b8 = search.getNode("FEXIT00301");

        Path exp2 = new Path(b1, b2, b3, b4, b5, b6, b7, b8);
        Path out = search.search(terminalNodes.getKey(), terminalNodes.getValue());

        assertTrue(exp2.equals(out));
    }

    @Test
    public void testERToLobby(){
        Pair<String, String> terminalNodes = new Pair<>("FEXIT00301", "FEXIT00201");

        Node c1 = search.getNode("FEXIT00301");
        Node c2 = search.getNode("eWALK01101");
        Node c3 = search.getNode("eWALK01001");
        Node c4 = search.getNode("FEXIT00201");

        Path exp3 = new Path(c1, c2, c3, c4);
        Path out = search.search(terminalNodes.getKey(), terminalNodes.getValue());

        assertTrue(exp3.equals(out));
    }

    @Test
    public void testStartEndSame(){
        String nodeId = "FEXIT00301";
        Node node = search.getNode(nodeId);
        Path exp4 = new Path(node);
        Path out = search.search(nodeId, nodeId);

        assertTrue(exp4.equals(out));
    }

}
