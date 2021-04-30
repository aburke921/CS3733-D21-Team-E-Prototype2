package edu.wpi.cs3733.D21.teamE;

import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.map.Path;
import edu.wpi.cs3733.D21.teamE.Time;
import edu.wpi.cs3733.D21.teamE.pathfinding.SearchContext;
import static org.junit.jupiter.api.Assertions.*;


import edu.wpi.cs3733.D21.teamE.views.mapEditorControllers.MapEditor;
import edu.wpi.cs3733.D21.teamE.database.appointmentDB;
import edu.wpi.cs3733.D21.teamE.database.csvDB;

import edu.wpi.cs3733.D21.teamE.database.makeConnection;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class PathFindingTests {
    static SearchContext search;

    @BeforeAll
    public static void setupExpected() {
        System.out.println("STARTING UP!!!");
        makeConnection con = makeConnection.makeConnection();
        File nodes = new File("CSVs/MapEAllnodes.csv");
        File edges = new File("CSVs/MapEAlledges.csv");
        try {
            DB.createAllTables();
            appointmentDB.createAppointmentTable();
            csvDB.populateTable("node", nodes);
            csvDB.populateTable("hasEdge", edges);
            System.out.println("Tables were created");
        } catch (Exception e) {
            DB.createAllTables();
            appointmentDB.createAppointmentTable();
        }

        search = new SearchContext();
    }

    @Test
    public void getNodeTest() {
        Node Test1 = new Node("FEXIT00301", 2099, 1357, "1", "Tower", "EXIT", "Emergency Department Entrance", "Emergency Entrance");
        assertTrue(Test1.equals(search.getNode("FEXIT00301")));

        Node Test2 = new Node("AHALL00603", 1580, 2858, "3", "BTM", "HALL", "HallNode", "HallNode");
        assertTrue(Test2.equals(search.getNode("AHALL00603")));

        Node Test3 = new Node("GHALL02901", 1262, 1850, "1", "Shapiro", "HALL", "Hallway MapNode 29 Floor 1", "Hall");
        assertTrue(Test3.equals(search.getNode("GHALL02901")));

        Node Test4 = new Node("GHALL008L2", 1577, 2195, "L2", "Shapiro", "HALL", "Hallway MapNode 8 Floor L2", "Hall");
        assertTrue(Test4.equals(search.getNode("GHALL008L2")));

    }

    @Test
    public void nodeInfoGettersTest() {

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
    public void testLobbyToParking() {
        Pair<String, String> terminalNodes = new Pair<>("FEXIT00201", "ePARK00101");

        Node a1 = search.getNode("FEXIT00201");
        Node a2 = search.getNode("eWALK01001");
        Node a3 = search.getNode("eWALK00701");
        Node a4 = search.getNode("eWALK00601");
        Node a5 = search.getNode("eWALK00501");
        Node a6 = search.getNode("eWALK00401");
        Node a7 = search.getNode("eWALK00301");
        Node a8 = search.getNode("eWALK00201");
        Node a9 = search.getNode("eWALK00101");
        Node a10 = search.getNode("ePARK00101");

        Path exp1 = new Path(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);

        Path out = search.search(terminalNodes.getKey(), terminalNodes.getValue());
        assertTrue(exp1.equals(out));
    }

    @Test
    public void testParkingToER() {
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
    public void testERToLobby() {
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
    public void testStartEndSame() {
        String nodeId = "FEXIT00301";
        Node node = search.getNode(nodeId);
        Path exp4 = new Path(node);
        Path out = search.search(nodeId, nodeId);

        assertTrue(exp4.equals(out));
    }

    @Test
    public void testBendLeft() {   /*
            test bend left
        */
        Node node1 = new Node("1001", 5, 10, "L2", "building1", "type1", "name 1", "name 1");
        Node node2 = new Node("1002", 8, 12, "L2", "building1", "type1", "name 1", "name 1");
        Node node3 = new Node("1003", 10, 11, "L2", "building1", "type1", "name 1", "name 1");

        Path path = new Path(node1, node2, node3);
        List<String> directions = path.makeDirections();

        assertTrue(directions.size() == 1 && directions.get(0).contains("Bend left"));
    }

    @Test
    public void testBendRight() {   /*
            test bend right
        */
        Node node1 = new Node("1001", 5, 10, "L2", "building1", "type1", "name 1", "name 1");
        Node node2 = new Node("1002", 8, 12, "L2", "building1", "type1", "name 1", "name 1");
        Node node3 = new Node("1003", 7, 15, "L2", "building1", "type1", "name 1", "name 1");

        Path path = new Path(node1, node2, node3);
        List<String> directions = path.makeDirections();

        assertTrue(directions.size() == 1 && directions.get(0).contains("Bend right"));
    }

    @Test
    public void testStraightAhead() {   /*
            test straight ahead
        */
        Node node1 = new Node("1001", 5, 10, "L2", "building1", "type1", "name 1", "name 1");
        Node node2 = new Node("1002", 8, 12, "L2", "building1", "type1", "name 1", "name 1");
        Node node3 = new Node("1003", 11, 14, "L2", "building1", "type1", "name 1", "name 1");

        Path path = new Path(node1, node2, node3);

        List<String> directions = path.makeDirections();
        assertTrue(directions.size() == 1 && directions.get(0).contains("Straight ahead"));
    }

    @Test
    public void testBendLeft90Degree() {   /*
            test bend left, 90 degrees
        */
        Node node1 = new Node("1001", 5, 5, "L2", "building1", "type1", "name 1", "name 1");
        Node node2 = new Node("1002", 7, 7, "L2", "building1", "type1", "name 1", "name 1");
        Node node3 = new Node("1003", 10, 4, "L2", "building1", "type1", "name 1", "name 1");

        Path path = new Path(node1, node2, node3);

        List<String> directions = path.makeDirections();
        assertTrue(directions.size() == 1 && directions.get(0).contains("Bend left, by angle 90.0"));
    }

    @Test
    public void testBendLeftMoreThan90Degree() {   /*
            test bend right, 90 degrees
        */
        Node node1 = new Node("1001", 5, 5, "L2", "building1", "type1", "name 1", "name 1");
        Node node2 = new Node("1002", 7, 7, "L2", "building1", "type1", "name 1", "name 1");
        Node node3 = new Node("1003", 8, 4, "L2", "building1", "type1", "name 1", "name 1");

        Path path = new Path(node1, node2, node3);

        List<String> directions = path.makeDirections();
        assertTrue(directions.size() == 1 && directions.get(0).contains("Bend left, by angle"));

        String[] words = directions.get(0).split(" ");
        double angle = Double.parseDouble(words[words.length - 1]);
        assertEquals(116.565, angle, 0.01);

    }

    @Test
    public void testBendLeftLessThan90Degree() {   /*
            test bend right, 90 degrees
        */
        Node node1 = new Node("1001", 5, 5, "L2", "building1", "type1", "name 1", "name 1");
        Node node2 = new Node("1002", 7, 7, "L2", "building1", "type1", "name 1", "name 1");
        Node node3 = new Node("1003", 10, 7, "L2", "building1", "type1", "name 1", "name 1");

        Path path = new Path(node1, node2, node3);

        List<String> directions = path.makeDirections();
        assertTrue(directions.size() == 1 && directions.get(0).contains("Bend left, by angle"));

        String[] words = directions.get(0).split(" ");
        double angle = Double.parseDouble(words[words.length - 1]);
        assertEquals(45.00, angle, 0.01);

    }

    @Test
    public void testBendRight90Degree() {   /*
            test bend right, 90 degrees
        */
        Node node1 = new Node("1001", 5, 5, "L2", "building1", "type1", "name 1", "name 1");
        Node node2 = new Node("1002", 7, 7, "L2", "building1", "type1", "name 1", "name 1");
        Node node3 = new Node("1003", 3, 11, "L2", "building1", "type1", "name 1", "name 1");

        Path path = new Path(node1, node2, node3);

        List<String> directions = path.makeDirections();
        assertTrue(directions.size() == 1 && directions.get(0).contains("Bend right, by angle 90.0"));
    }

    @Test
    public void testBendRightMoreThan90Degree() {   /*
            test bend right, 90 degrees
        */
        Node node1 = new Node("1001", 5, 5, "L2", "building1", "type1", "name 1", "name 1");
        Node node2 = new Node("1002", 7, 7, "L2", "building1", "type1", "name 1", "name 1");
        Node node3 = new Node("1003", 1, 10, "L2", "building1", "type1", "name 1", "name 1");

        Path path = new Path(node1, node2, node3);

        List<String> directions = path.makeDirections();
        assertTrue(directions.size() == 1 && directions.get(0).contains("Bend right, by angle"));

        String[] words = directions.get(0).split(" ");
        double angle = Double.parseDouble(words[words.length - 1]);
        assertEquals(108.435, angle, 0.01);

    }

    @Test
    public void testBendRightLessThan90Degree() {   /*
            test bend right, less 90 degrees
        */
        Node node1 = new Node("1001", 5, 5, "L2", "building1", "type1", "name 1", "name 1");
        Node node2 = new Node("1002", 7, 7, "L2", "building1", "type1", "name 1", "name 1");
        Node node3 = new Node("1003", 6, 10, "L2", "building1", "type1", "name 1", "name 1");

        Path path = new Path(node1, node2, node3);

        List<String> directions = path.makeDirections();
        assertTrue(directions.size() == 1 && directions.get(0).contains("Bend right, by angle"));

        String[] words = directions.get(0).split(" ");
        double angle = Double.parseDouble(words[words.length - 1]);
        assertEquals(63.4349, angle, 0.01);

    }

    @Test
    public void testGetPathLength() {
        Path path1 = search.search("eWALK01101", "eWALK01001");
        assertEquals(325.0, path1.getPathLength(), 0.1);
        assertEquals(105.63, path1.getPathLengthFeet(), 0.1);

        Path path2 = search.search("ePARK00101", "ePARK02501");
        assertEquals(3460.57, path2.getPathLength(), 0.1);
        assertEquals(1124.68, path2.getPathLengthFeet(), 0.1);

        Path path3 = search.search("CRETL001L1", "BREST00102");
        assertEquals(325.01, path3.getPathLength(), 0.1);
        assertEquals(105.63, path3.getPathLengthFeet(), 0.1);

        Path path4 = search.search("ARETL00101", "ADEPT00102");
        assertEquals(575.26, path4.getPathLength(), 0.1);
        assertEquals(186.96, path4.getPathLengthFeet(), 0.1);
    }

    @Test
    public void testETA() {
        Path path1 = search.search("eWALK01101", "eWALK01001");
        assertTrue(path1.getETA().equals(new Time(27)));

        Path path2 = search.search("ePARK00101", "ePARK02501");
        assertTrue(path2.getETA().equals(new Time(282)));

        Path path3 = search.search("CRETL001L1", "BREST00102");
        assertTrue(path3.getETA().equals(new Time(27)));

        Path path4 = search.search("ARETL00101", "ADEPT00102");
        assertTrue(path4.getETA().equals(new Time(47)));
    }

    @Test
    public void testConstraints() {
        //Same nodes, different constraints, forcing different paths
        Path stairs1 = search.search("ARETL00101", "ADEPT00102");
        Path stairs2 = search.search("GHALL00803", "GHALL00601");
        search.setConstraint("HANDICAP");
        Path noStairs1 = search.search("ARETL00101", "ADEPT00102");
        Path noStairs2 = search.search("GHALL00803", "GHALL00601");

        assertFalse(stairs1.equals(noStairs1));
        assertFalse(stairs2.equals(noStairs2));


        search.setConstraint("VANILLA");
        Path ER1 = search.search("ePARK01201", "ELABS00101");
        Path ER2 = search.search("ePARK02501", "FHALL02701");
        search.setConstraint("SAFE");
        Path noER1 = search.search("ePARK01201", "ELABS00101");
        Path noER2 = search.search("ePARK02501", "FHALL02701");

        assertFalse(ER1.equals(noER1));
        assertFalse(ER2.equals(noER2));
    }

    @Test
    public void testAutoGenIDs() {
        MapEditor ed = new MapEditor();
        assertEquals("eELEV00A01", ed.genNodeID("ELEV","1", "Elevator A Floor 1"));
        assertEquals("ePARK02601", ed.genNodeID("PARK","1", "New Parking Sport Floor 1"));
        assertEquals("eDEPT00102", ed.genNodeID("DEPT","2", "New Department Floor 2"));
        assertEquals("eSERV001GG", ed.genNodeID("SERV","G", "New Service Floor G"));
        assertEquals("eSERV001GG", ed.genNodeID("SERV","GG", "New Service Floor GG")); //Just in case of error on entry
        assertEquals("eRETL001L2", ed.genNodeID("RETL","L2", "New Retail Floor L2"));
        assertEquals("eLABS001L1", ed.genNodeID("LABS","L1", "New Labs Floor L1"));
        assertEquals("eWALK00103", ed.genNodeID("WALK","3", "New Walkway Floor 3"));
    }
    /**
     * Manual test - useful for UI, please do not delete w/o notice.
     */
//    public void foo(){
//        Searcher aStar = new AStarSearch();
//        Path foundPath = aStar.search("ACONF00102", "eWALK01901");
//        Iterator<Node> nodeIteratorThisFloorOnly = foundPath.iterator();
//        System.out.println("contents of path:");
//        for (Iterator<Node> it = nodeIteratorThisFloorOnly; it.hasNext(); ) {
//            Node node = it.next();
//            System.out.println("Name: " + node.get("longName") + ", Floor: " + node.get("floor") + ", ID: " + node.get("id"));
//        }
//    }

}
