package edu.wpi.TeamE.algorithms.user;

import edu.wpi.TeamE.algorithms.Path;
import edu.wpi.TeamE.algorithms.pathfinding.SearchContext;
import edu.wpi.TeamE.algorithms.scheduler.DailySchedule;
import edu.wpi.TeamE.algorithms.scheduler.Event;

import java.util.ArrayList;
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
        return search.search(schedule.getLocations());
    }

    public List<String> listSchedule(){
        List<String> events = new ArrayList<>();
        for(Event event : schedule){
            events.add(event.toString());
        }
        return events;
    }

    public List<Event> getEvents(){
        return schedule.getAgenda();
    }

    public Path whereToNext(SearchContext search, int currentTime){
        return null;
    }

    public Event nextEvent(int currentTime){
        return null;
    }

}
