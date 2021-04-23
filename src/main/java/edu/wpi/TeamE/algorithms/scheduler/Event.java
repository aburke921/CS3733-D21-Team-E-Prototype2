package edu.wpi.TeamE.algorithms.scheduler;

import edu.wpi.TeamE.algorithms.Node;

public class Event implements Comparable<Event>{
    Node location;
    String description;
    int arrivalTime;
    int endTime;

    public Event(Node _location, String _description, int _arrivalTime, int _endTime){
        location = _location;
        description = _description;
        arrivalTime = _arrivalTime;
        endTime = _endTime;
    }

    public Node getLocation(){
        return location;
    }
    public int getArrivalTime(){
        return arrivalTime;
    }
    public int getEndTime(){
        return endTime;
    }
    public String getDescription(){
        return description;
    }

    @Override
    public String toString() {
        return description + " at " + arrivalTime + " o'clock in room " + location;
    }

    @Override
    public int compareTo(Event e) {
        return Integer.compare(arrivalTime, e.arrivalTime);
    }
}
