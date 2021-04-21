package edu.wpi.TeamE.algorithms;

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



    /**
     * @return an iterator to loop through the path
     *         if it is not empty it will return will return with the iterator
     *         pointing at the first node, so when you call next the first time
     *         you will receive the first node in the list
     *         otherwise the iterator will point at the sentinel node
     */
    @Override
    public Iterator<Node> iterator() {
        return iterator("ALL");
    }
    public Iterator<Node> iterator(String floorNum){
        return new NodeIterator(getStart(), floorNum);
    }

    private class NodeIterator implements Iterator<Node> {
        Node cursor;
        String floorNum;
        private NodeIterator(Node _cursor, String _floorNum){
            cursor = _cursor;
            floorNum = _floorNum;
        }

        @Override
        public boolean hasNext() {
            if(floorNum.equalsIgnoreCase("ALL")){
                return cursor != null;
            } else {
                return cursor != null && cursor.get("floor").equalsIgnoreCase(floorNum);
            }
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