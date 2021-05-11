package edu.wpi.cs3733.D21.teamE.scheduler;

import edu.wpi.cs3733.D21.teamE.Time;
import edu.wpi.cs3733.D21.teamE.map.Node;
import java.util.*;

public class Schedule implements Iterable<ToDo>{
    private final List<ToDo> schedule;

    public Schedule(List<ToDo> todoList){
        schedule = new ArrayList<>(todoList.size());
        for(ToDo todo : todoList){
            add(todo);
        }
    }

    public boolean add(ToDo newToDo){
        if(canAdd(newToDo) && schedule.add(newToDo)){
            Collections.sort(schedule);
            return true;
        } else {
            System.out.println("Cannot add todo to schedule");
            return false;
        }
    }
    public boolean canAdd(ToDo newToDo){
        return canAdd(newToDo.getStartTime(), newToDo.getEndTime());
    }

    public boolean canAdd(Time startTime, Time endTime){
        boolean isAvailable = true;
        Iterator<ToDo> itr = schedule.iterator();

        ToDo curr = null;
        while(isAvailable && itr.hasNext()){
            curr = itr.next();
            isAvailable &= !(curr.isConflict(startTime, endTime));
        }
        if(!isAvailable) {
            System.out.println("Cannot add new ToDo between " + startTime + " and " + endTime);
            System.out.println("Conflicts with ToDo " + ((curr != null) ? curr.getTitle() + ": " + curr.getStartTime() + " and " + curr.getEndTime() : ""));
        }
        return isAvailable;
    }

    public boolean canChange(ToDo existing, Time newStartTime, Time newEndTime){
        boolean isAvailable = true;
        Iterator<ToDo> itr = schedule.iterator();

        ToDo curr = null;
        while(isAvailable && itr.hasNext()){
            curr = itr.next();
            if(!curr.equals(existing)){
                isAvailable &= !(curr.isConflict(newStartTime, newEndTime));
            }
        }
        if(!isAvailable) {
            System.out.println("Cannot add new ToDo between " + newStartTime + " and " + newEndTime);
            System.out.println("Conflicts with ToDo " + ((curr != null) ? curr.getTitle() + ": " + curr.getStartTime() + " and " + curr.getEndTime() : ""));
        }
        return isAvailable;
    }



    public List<Node> getLocations(){
        List<Node> locations = new LinkedList<>();
        for(ToDo event : schedule){
            if(event.getLocation() != null) {
                locations.add(event.getLocation());
            }
        }
        return locations;
    }

    public List<ToDo> getTodoList(){
        return schedule;
    }

    public ToDo get(int index){
        return schedule.get(index);
    }

    public void print(){
        System.out.println("Printing Schedule");
        for(ToDo todo : schedule){
            System.out.println(todo.getTitle() + " , " + todo.getLocation());
        }
    }


    @Override
    public Iterator<ToDo> iterator() {
        return schedule.iterator();
    }
}
