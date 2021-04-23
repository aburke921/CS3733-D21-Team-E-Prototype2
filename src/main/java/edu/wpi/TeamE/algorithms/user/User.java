package edu.wpi.TeamE.algorithms.user;

import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.TeamE.algorithms.Path;
import edu.wpi.TeamE.algorithms.Time;
import edu.wpi.TeamE.algorithms.pathfinding.SearchContext;
import edu.wpi.TeamE.algorithms.scheduler.DailySchedule;
import edu.wpi.TeamE.algorithms.scheduler.Event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class User {

    private DailySchedule schedule;
    private String id;
    private String type;

    public User(){
        schedule = new DailySchedule();
    }

    public User(String _id, String _type){
        this();
        id = _id;
        type = _type;
    }

    public boolean bookAppointment(Event newAppointment) {
        return schedule.add(newAppointment);
    }

    public void removeAppointment(Event appointment){
        schedule.remove(appointment);
    }

    public Path drawSchedule(SearchContext search){
        return search.search(getLocations());
    }

    public Path drawSchedule(SearchContext search, int startTime, int endTime){
        return search.search(getLocations(startTime, endTime));
    }

    public List<String> listSchedule(int startTime, int endTime){
        List<String> nodes = new ArrayList<>(schedule.size());
        Iterator<Event> itr = schedule.iterator(startTime, endTime);
        while(itr.hasNext()){
            nodes.add(itr.next().toString());
        }
        return nodes;
    }

    public List<String> listSchedule(){
        return listSchedule(Time.DAY_START, Time.DAY_END);
    }

    public List<Node> getLocations(int startTime, int endTime){
        List<Node> nodes = new ArrayList<>(schedule.size());
        Iterator<Event> itr = schedule.iterator(startTime, endTime);
        while(itr.hasNext()){
            nodes.add(itr.next().getLocation());
        }
        return nodes;
    }

    public List<Node> getLocations(){
        return getLocations(Time.DAY_START, Time.DAY_END);
    }

    public List<Time> getArrivalTimes(int startTime, int endTime){
        List<Time> nodes = new ArrayList<>(schedule.size());
        Iterator<Event> itr = schedule.iterator(startTime, endTime);
        while(itr.hasNext()){
            nodes.add(new Time(itr.next().getArrivalTime()));
        }
        return nodes;
    }

    public List<Time> getArrivalTimes(){
        return getArrivalTimes(Time.DAY_START, Time.DAY_END);
    }

    public List<Event> getEvents(){
        return schedule.getAgenda();
    }

    public Path whereToNext(SearchContext search, int currentTime){
        Iterator<Event> itr = schedule.iterator();
        while(itr.hasNext()){
            Event event = itr.next();
            if(itr.hasNext() && event.getArrivalTime() < currentTime && event.getEndTime() < currentTime){
                return search.search(event.getLocation(), itr.next().getLocation());
            }
        }
        return null;
    }
}
