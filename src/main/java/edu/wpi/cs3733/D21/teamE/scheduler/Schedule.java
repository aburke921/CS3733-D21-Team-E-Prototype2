package edu.wpi.cs3733.D21.teamE.scheduler;

import edu.wpi.cs3733.D21.teamE.map.Node;
import java.util.*;

public class Schedule {
    private final List<ToDo> schedule;

    public Schedule(List<ToDo> todoList){
        schedule = new ArrayList<>(todoList.size());
        for(ToDo todo : todoList){
            add(todo);
        }
    }

    public boolean add(ToDo newToDo){
        if(isAvailable(newToDo) && schedule.add(newToDo)){
            Collections.sort(schedule);
            return true;
        } else {
            return false;
        }
    }

    public boolean isAvailable(ToDo t) {
        boolean isAvailable = true;
        Iterator<ToDo> itr = schedule.iterator();
        while(isAvailable && itr.hasNext()){
            isAvailable &= !(t.isConflict(itr.next()));
        }
        return isAvailable;
    }

    public List<Node> getLocations(){
        List<Node> locations = new LinkedList<>();
        for(ToDo event : schedule){
            locations.add(event.getLocation());
        }
        return locations;
    }

    public List<ToDo> getSchedule(){
        return schedule;
    }

    public ToDo get(int index){
        return schedule.get(index);
    }

}
