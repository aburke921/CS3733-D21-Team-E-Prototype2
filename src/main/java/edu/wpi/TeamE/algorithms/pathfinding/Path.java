package edu.wpi.TeamE.algorithms.pathfinding;

import java.util.Iterator;

/**
 * Path Class
 * Is effectively a LinkedList
 */
public class Path {
    //pathHead is a sentinel node
    private final Node pathHead;

    //path end is a pointer to the end of the list
    private Node pathEnd;

    /**
     * construct an empty list
     */
    public Path(){
        pathHead = new Node();
        pathEnd = pathHead;
    }

    /**
     * @return the first node in list
     */
    public Node peek(){
        return pathHead.getNext();
    }

    /**
     * Add node and all 'next' nodes to
     * @param n Node to add to path
     */
    public void add(Node n){
        //set the end of the path to point at n
        pathEnd.setNext(n);
        //move pathEnd to
        for(Iterator<Node> itr = n.iterator(); itr.hasNext(); pathEnd = itr.next());
    }

    /**
     * Appends a Path onto the end of this
     * @param p Path to append, if path is empty nothing happens
     */
    public void add(Path p) {
        add(p.peek());
    }

    /**
     * @return an iterator to loop through the path
     *         if it is not empty it will return will return with the iterator
     *         pointing at the first node, so when you call next the first time
     *         you will receive the first node in the list
     *         otherwise the iterator will point at the sentinel node
     */
    public Iterator<Node> iterator(){
        Iterator<Node> itr = pathHead.iterator();
        if(itr.hasNext()){
            itr.next();
        }
        return itr;
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
}