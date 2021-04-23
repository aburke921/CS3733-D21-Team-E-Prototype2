package edu.wpi.TeamE.algorithms.scheduler;

import edu.wpi.TeamE.algorithms.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DailySchedule implements Iterable<Event>{

    private List<Event> agenda;
    private boolean[] availability;

    public DailySchedule(){
        //the number of 5 minute intervals in a day (24 hours)
        int numIntervals = 24 * 60 / 5;
        agenda = new ArrayList<>(numIntervals);
        availability = new boolean[numIntervals];
    }

    //false if couldn't add
    //true if successfully added
    public boolean add(Event agenda){
        return false;
    }

    public void remove(Event appointment) {

    }

    public List<Event> getAgenda(){
        return agenda;
    }

    public List<Node> getLocations(){
        List<Node> nodes = new ArrayList<>(agenda.size());

        return nodes;
    }

    public Event nextEvent(int currentTime){
        return null;
    }

    public int size(){
        return agenda.size();
    }

    @Override
    public Iterator<Event> iterator() {
        return agenda.iterator();
    }


}
