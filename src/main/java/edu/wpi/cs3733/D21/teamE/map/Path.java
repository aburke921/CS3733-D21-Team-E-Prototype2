package edu.wpi.cs3733.D21.teamE.map;

import edu.wpi.cs3733.D21.teamE.Time;
import java.util.Iterator;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Path Class
 * Is effectively a LinkedList
 */
public class Path implements Comparable<Path>, Iterable<Node>{
    //pathHead is a sentinel node
    private final Node pathHead;

    //path end is a pointer to the end of the list
    private Node pathEnd;

    private double length = 0;

    //Scale from pixel to foot
    public final double SCALE = 0.325;
    //Average walking speed for someone in their 60s, a large portion of patient demographic
    public final int SPEED = 4;

    /**
     * construct an empty list
     */
    public Path(){
        pathHead = new Node();
        pathEnd = pathHead;

    }

    public Path(Node... nodes){
        this();
        for(Node node : nodes){
            add(node);
        }
    }

    public boolean isEmpty(){
        return pathHead == pathEnd;
    }

    /**
     * @return the first node in list
     */
    public Node getStart(){
        return pathHead.getNext();
    }
    public Node getEnd(){
        return pathEnd;
    }

    /**
     * Adds a copy of a singular node to end of list
     * @param _n Node to add to path
     */
    public void add(Node _n){
        Node n = _n.copy();

        if (!isEmpty()) { //If it's empty, then there is no distance to calculate
            length += pathEnd.dist(n);
        }
        pathEnd.setNext(n);
        pathEnd = n;
    }

    public Node pop(){
        Node first = getStart();
        pathHead.setNext(first.getNext());
        first.setNext(null);
        return first;
    }

    /**
     * Appends a Path onto the end of this
     * @param p Path to append, if path is empty nothing happens
     */
    public void add(Path p) {
        if(!p.isEmpty()) {
            length += p.getPathLength() + pathEnd.dist(p.getStart());
            pathEnd.setNext(p.getStart());
            pathEnd = p.pathEnd;
        }
    }

    public List<Path> splitByFloor(){
        List<Path> paths = new LinkedList<>();
        Iterator<Node> itr = iterator();
        Path leg = new Path();
        String prevFloor = getStart().get("floor");
        while(itr.hasNext()){
            Node node = itr.next();
            if(node.get("floor").equals(prevFloor)){
                leg.add(node);
            } else {
                prevFloor = node.get("floor");
                paths.add(leg);
                leg = new Path(node);
            }
        }
        paths.add(leg);
        return paths;
    }


    /**
     * @return an iterator to loop through the path
     *         if it is not empty it will return will return with the iterator
     *         pointing at the first node, so when you call next the first time
     *         you will receive the first node in the list
     *         otherwise the iterator will point at the sentinel node
     */
    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator(getStart());
    }

    private class NodeIterator implements Iterator<Node> {
        Node cursor;
        private NodeIterator(Node _cursor){
            cursor = _cursor;
        }

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public Node next() {
            Node tmp = cursor;
            cursor = cursor.getNext();
            return tmp;
        }
    }

    /**
     * A method(no parameters) in our path class that returns a collection of
     * strings(array, array list), iterate through its list and
     * figure out how to turn the list nodes into a list of strings that describe it.
     * Figure out current line(coordinates), next line, and then make an angle.
     * Bend left, turn left, by angle (small or large) decide direction after.
     *
     * @return the list of strings as directions
     */
    public List<String> makeDirections(){

        List<String> directions = new ArrayList<>();

        //iterate the list
        Iterator<Node> itr = iterator();


        /*
        node 1
        \
         \
         _\|----->  node 3
          node 2

        =>bend left

        node 1
        \
         \
         _\|
         / node 2
        /
      |/_
      node 3
        =>bend right

         */

        if(itr.hasNext()){

            //node 1
            Node node1 = itr.next();

            if(itr.hasNext()){

                //node 2
                Node node2 = itr.next();

                while (itr.hasNext()){

                    //node 3
                    Node node3 = itr.next();

                    //p3 - p1
                    Point p3_1 = new Point(node3.getX() - node1.getX(), node3.getY() - node1.getY());

                    //p2 - p1
                    Point p2_1 = new Point(node2.getX() - node1.getX(), node2.getY() - node1.getY());

                    //calculate the cross product
                    double crossProduct = p3_1.getX() * p2_1.getY() - p2_1.getX() * p3_1.getY();

                    //find angle
                    double angle = 0;
                    //vectors
                    Point p1p2 = new Point(node2.getX() - node1.getX(), node2.getY() - node1.getY());
                    Point p2p3 = new Point(node3.getX() - node2.getX(), node3.getY() - node2.getY());
                    //dot product p1p2 x p2p3
                    double dotProduct = p1p2.getX() * p2p3.getX() + p1p2.getY() * p2p3.getY();
                    //length of vector p1p2
                    double p1p2Length = Math.sqrt( p1p2.getX() *  p1p2.getX() + p1p2.getY() *  p1p2.getY());
                    //length of vector p2p3
                    double p2p3Length = Math.sqrt( p2p3.getX() *  p2p3.getX() + p2p3.getY() *  p2p3.getY());

                    angle = Math.acos(dotProduct / (p1p2Length * p2p3Length));
                    angle = 180 * angle / Math.PI;//convert radian to angle

                    if (crossProduct < 0){
                        directions.add("Bend right, by angle " + Math.abs(angle));
                    }else if (crossProduct > 0){
                        directions.add("Bend left, by angle " + Math.abs(angle));
                    }else{
                        directions.add("Straight ahead");
                    }

                    //continue for next node
                    node1 = node2;
                    node2 = node3;
                }
            }
        }

        return directions;

    }

    /**

     * A method(no parameters) in our path class that returns a collection of
     * strings(array, array list), iterate through its list and
     * figures out how to turn the list nodes into a list of strings that describe it.
     * Figures out current line(coordinates), next line, and then make an angle.
     * Bend left, turn left, by angle (small or large) decide direction after.
     *
     * @return the list of strings as directions
     */
    public List<String> makeDirectionsWithDist(){

        List<String> directions = new ArrayList<>();

        //iterate the list
        Iterator<Node> itr = this.iterator();

        if(itr.hasNext()){

            //node 1
            Node node1 = itr.next();

            if(itr.hasNext()){

                //node 2
                Node node2 = itr.next();
                double len = node1.dist(node2);
                int dist = (int) (Math.round((len * SCALE) / 10) * 10);
                if (dist == 0) {
                    dist = 5;
                }
                if(node2.get("type").equalsIgnoreCase("ELEV") && node1.get("type").equalsIgnoreCase("ELEV")) {
                    if (!itr.hasNext()) {
                        directions.add("Take Elevator " + node1.get("longName").charAt(9) + " to Floor " + node2.get("floor"));
                    }
                } else if (node2.get("type").equalsIgnoreCase("STAI") && node1.get("type").equalsIgnoreCase("STAI")) {
                    if (!itr.hasNext()) {
                        if (Node.calculateZ(node1.get("floor")) > Node.calculateZ(node2.get("floor"))) {
                            directions.add("Take the Stairs down to Floor " + node2.get("floor"));
                        } else {
                            directions.add("Take the Stairs up to Floor " + node2.get("floor"));
                        }
                    }
                } else {
                    directions.add("Go straight for " + dist + " feet");
                }

                int floorChangeState = 0;
                // 0 = normal
                // 1 = elev in 2 and 3
                // 2 = elev in 1 and 2
                // 3 = stairs in 2 and 3
                // 4 = stairs in 1 and 2

                while (itr.hasNext()){
                    floorChangeState = 0;
                    //node 3
                    Node node3 = itr.next();

                    if(node2.get("type").equalsIgnoreCase("ELEV")) {
                        if (node3.get("type").equalsIgnoreCase("ELEV")) {
                            floorChangeState = 1;
                        } else if (node1.get("type").equalsIgnoreCase("ELEV")) {
                            floorChangeState = 2;
                        }
                    } else if (node2.get("type").equalsIgnoreCase("STAI")) {
                        if (node3.get("type").equalsIgnoreCase("STAI")) {
                            floorChangeState = 3;
                        } else if (node1.get("type").equalsIgnoreCase("STAI")) {
                            floorChangeState = 4;
                        }
                    }

                    switch (floorChangeState){
                        case 1:
                            break;
                        case 2:
                            len = node3.dist(node2);
                            dist = (int) (Math.round((len * SCALE) / 10) * 10);
                            if (dist == 0) {
                                dist = 5;
                            }
                            directions.add("Take Elevator " + node1.get("longName").charAt(9) + " to Floor " + node2.get("floor"));
                            directions.add("Exit the Elevator and go straight for " + dist + " feet");
                            break;
                        case 3:
                            String floor = "";
                            while(node3.get("type").equalsIgnoreCase("STAI")) {
                                floor = node3.get("floor");
                                if (itr.hasNext()) {
                                    node2 = node3;
                                    node3 = itr.next();
                                } else {
                                    System.out.println("Last Stairs Found");
                                    break;
                                }
                            }
                            if (Node.calculateZ(node1.get("floor")) > Node.calculateZ(node2.get("floor"))) {
                                directions.add("Take the Stairs down to Floor " + floor);
                            } else {
                                directions.add("Take the Stairs up to Floor " + floor);
                            }
                        case 4:
                            if (!node3.get("type").equalsIgnoreCase("STAI")) {
                                len = node3.dist(node2);
                                dist = (int) (Math.round((len * SCALE) / 5) * 5);
                                if (dist == 0) {
                                    dist = 5;
                                }
                                directions.add("Exit the Stairwell and go straight for " + dist + " feet");
                            }
                            break;
                        default:

                            double len1_2 = node1.dist(node2);
                            double len2_3 = node2.dist(node3);

                            //vectors
                            //p3 defined by p1m (p1-p3 vector components)
                            Point p3p1 = new Point(node3.getX() - node1.getX(), node3.getY() - node1.getY());
                            //p2 defined by p1 (p1-p2 vector components)
                            Point p1p2 = new Point(node2.getX() - node1.getX(), node2.getY() - node1.getY());
                            //p3 defined by p2 (p2-p3 vector components)
                            Point p2p3 = new Point(node3.getX() - node2.getX(), node3.getY() - node2.getY());

                            //calculate the cross product
                            double crossProduct = p3p1.getX() * p1p2.getY() - p1p2.getX() * p3p1.getY();
                            //find angle
                            double angle = 0;
                            //dot product p1p2 x p2p3
                            double dotProduct = p1p2.getX() * p2p3.getX() + p1p2.getY() * p2p3.getY();

                            angle = Math.acos(dotProduct / (len1_2 * len2_3));
                            angle = 180 * angle / Math.PI;//convert radian to degree

                            String turn;
                            int angleComp = (int) Math.abs(angle);

                            if (angleComp < 35) {
                                turn = "Bend to the";
                            } else if (angleComp < 65) {
                                turn = "Take a shallow turn to the";
                            } else if (angleComp < 115) {
                                turn = "Turn to the";
                            } else {
                                turn = "Take a sharp turn to the";
                            }

                            dist = (int) (Math.round((len2_3 * SCALE) / 10) * 10);

                            if (dist == 0) {
                                dist = 5;
                            }

                            // straight "tolerance"
                            if (angleComp > 10) {
                                if (crossProduct < 0 ){
                                    directions.add(turn + " right");
                                    directions.add("Go straight for " + dist + " feet");
                                } else {
                                    directions.add(turn + " left");
                                    directions.add("Go straight for " + dist + " feet");
                                }
                            } else {
                                int index = directions.size() - 1;
                                String lastDir = directions.get(index);
                                if (lastDir.contains("straight")){
                                    directions.remove(index);
                                    String clean = lastDir.replaceAll("\\D+","");
                                    dist += Integer.parseInt(clean);
                                    String result = lastDir.split("straight")[0];
                                    directions.add(result + "straight for " + dist + " feet");
                                } else {
                                    directions.add("Go straight for " + dist + " feet");
                                }

                            }
                            break;
                    }

                    //continue for next node
                    node1 = node2;
                    node2 = node3;
                }
            }
        }

        return directions;

    }

    /**
     * Prints a string rep. of the Path
     * @param labels a list of the labels you want to print, everything but xCoord and yCoord
     */
    public void print(String... labels){
        System.out.print("Path: \n\t");
        Iterator<Node> itr = iterator();
        while(itr.hasNext()){
            Node next = itr.next();
            for(String label : labels){
                System.out.printf("%s : %s, ", label, next.get(label));
            }
        }
        System.out.println();
    }
    public void print(){
        print("id", "floor", "building", "type", "longName", "shortName");
    }

    /**
     * @param p Path obj with which to compare
     * @return boolean value rep. if 'p' and 'this' are same
     */
    public boolean equals(Path p){
        Iterator<Node> aItr = iterator();
        Iterator<Node> bItr = p.iterator();
        boolean nodeSame = true;
        boolean lengthSame = equals(aItr.hasNext(), bItr.hasNext());
        while(aItr.hasNext() && bItr.hasNext()){
            Node a = aItr.next();
            Node b = bItr.next();
            nodeSame &= a.equals(b) && b.equals(a);
            lengthSame &= equals(aItr.hasNext(), bItr.hasNext());
        }
        return lengthSame && nodeSame;
    }

    /**
     * @param a,b booleans to compare
     * @return true if a,b are equal
     */
    private boolean equals(boolean a, boolean b){
        //double implication
        return (!a||b)&&(a||!b);
    }

    /**
     * Gets the length of the path for Time Estimates
     * @return The total length of the path
     */
    public double getPathLength(){
        return length;
    }

    /**
     * Gets the length of the path in feet for Time Estimates
     * @return The total length of the path in feet
     */
    public double getPathLengthFeet(){
        return length * SCALE;
    }

    public Time getETA() {
        int seconds = (int) Math.round(this.getPathLengthFeet() / SPEED + 0.5); //force round up
        return new Time(seconds);
    }

    /**
     * convenience method
     * but is an unnecessary O(n) computation
     * when iterators are pretty easy to use
     * @return the path as a List
     */
    public List<Node> toList(){
        List<Node> nodes = new LinkedList<>();
        for(Iterator<Node> itr = iterator(); itr.hasNext(); nodes.add(itr.next()));
        return nodes;
    }

    @Override
    public int compareTo(Path p) {
        return Double.compare(length, p.length);
    }


}
