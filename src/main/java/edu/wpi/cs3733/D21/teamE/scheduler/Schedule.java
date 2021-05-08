package edu.wpi.cs3733.D21.teamE.scheduler;

import edu.wpi.cs3733.D21.teamE.map.Node;
import java.util.*;

public class Schedule {
    private final List<Todo> schedule;

    public Schedule(List<Todo> todoList){
        schedule = new ArrayList<Todo>(todoList.size()){
            @Override
            public boolean add(Todo o) {
                if(isAvailable(o) && super.add(o)){
                    Collections.sort(schedule);
                    return true;
                } else {
                    return false;
                }
            }
        };
        for(Todo todo : todoList){
            //noinspection UseBulkOperation
            schedule.add(todo);
        }
    }

    public boolean add(Todo newTodo){
        return schedule.add(newTodo);
    }

    public boolean isAvailable(Todo t) {
        boolean isAvailable = true;
        Iterator<Todo> itr = schedule.iterator();
        while(isAvailable && itr.hasNext()){
            isAvailable &= !(t.isConflict(itr.next()));
        }
        return isAvailable;
    }

    public List<Node> getLocations(){
        List<Node> locations = new LinkedList<>();
        for(Todo event : schedule){
            locations.add(event.getLocation());
        }
        return locations;
    }

    public List<Todo> getSchedule(){
        return schedule;
    }

    public Todo get(int index){
        return schedule.get(index);
    }

}
