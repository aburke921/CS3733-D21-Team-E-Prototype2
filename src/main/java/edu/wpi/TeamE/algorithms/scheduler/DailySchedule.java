package edu.wpi.TeamE.algorithms.scheduler;

import edu.wpi.TeamE.algorithms.Node;

import java.util.*;

public class DailySchedule implements Iterable<Event>{

    private final List<Event> agenda;
    private final boolean[] availability;

    public DailySchedule(){
        //the number of 5 minute intervals in a day (24 hours)
        int numIntervals = 24 * 60 / 5;
        availability = new boolean[numIntervals];
        agenda = new ArrayList<>(numIntervals){
            @Override
            public boolean add(Event event) {
                if(super.add(event)){
                    Collections.sort(agenda);
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    //false if couldn't add
    //true if successfully added
    public boolean add(Event event){
        int arrivalTime = event.getArrivalTime();
        int endTime = event.getEndTime();
        if(isAvailable(arrivalTime, endTime)){
            agenda.add(event);
            setAvailability(false, arrivalTime, endTime);
            return true;
        } else {
            return false;
        }
    }

    public boolean isAvailable(int startTime, int endTime){
        boolean available = true;
        for(int i = indexOf(startTime); i < indexOf(endTime); i++){
            available &= availability[i];
        }
        return available;
    }

    public void setAvailability(boolean available, int startTime, int endTime){
        for(int i = indexOf(startTime); i < indexOf(endTime); i++){
            availability[i] = available;
        }
    }

    private int indexOf(int arrivalTime) {
        return arrivalTime/300;
    }

    public void remove(Event appointment) {
        int arrivalTime = appointment.getArrivalTime();
        int endTime = appointment.getEndTime();
        agenda.remove(appointment);
        setAvailability(true, arrivalTime, endTime);
    }

    public List<Event> getAgenda(){
        return agenda;
    }

    public Event nextEvent(int currentTime){
        for(int i = 0; i < agenda.size() - 1; i++){
            Event event = agenda.get(i);
            if(event.getArrivalTime() < currentTime && event.getEndTime() < currentTime){
                return agenda.get(i + 1);
            }
        }
        return null;
    }

    public Event getEvent(int currentTime){
        for(int i = 0; i < agenda.size() - 1; i++){
            Event event = agenda.get(i);
            if(event.getArrivalTime() < currentTime && event.getEndTime() < currentTime){
                return agenda.get(i);
            }
        }
        return null;
    }

    public int size(){
        return agenda.size();
    }

    @Override
    public Iterator<Event> iterator() {
        int midnight1 = 0;
        int midnight2 = 86400;
        return iterator(midnight1, midnight2);
    }

    public Iterator<Event> iterator(int startTime, int endTime){
        return new EventIterator(agenda, startTime, endTime);
    }

    private class EventIterator implements Iterator<Event>{
        private List<Event> eventList;
        private int index;
        private int startTime;
        private int endTime;


        EventIterator(List<Event> _eventList, int _startTime, int _endTime){
            eventList = _eventList;
            index = 0;
            startTime = _startTime;
            endTime = _endTime;
        }



        @Override
        public boolean hasNext() {
            if(index < eventList.size()){
                Event current = eventList.get(index);
                int arrivalTime = current.getArrivalTime();
                return arrivalTime > startTime && arrivalTime < endTime;
            } else {
                return false;
            }
        }

        @Override
        public Event next() {
            return eventList.get(index++);
        }
    }

}
