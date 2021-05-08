package edu.wpi.cs3733.D21.teamE.scheduler;

import edu.wpi.cs3733.D21.teamE.Time;
import edu.wpi.cs3733.D21.teamE.map.Node;


public class ToDo implements Comparable<ToDo>{
    private int todoID;
    private int userID;
    private String title;
    private int status;
    private int priority;

    //possibly null
    private Time alertTime;
    private Time startTime;
    private Time duration;
    private Node location;

    public ToDo(int _todoID, int _userID, String _title, int _status, int _priority){
        todoID = _todoID;
        userID = _userID;
        title = _title;
        status = _status;
        priority = _priority;
    }

    public ToDo(int _todoID, int _userID, String _title, int _status, int _priority, Time _alertTime, Time _startTime, Time _duration, Node _location){
        this(_todoID, _userID, _title, _status, _priority);
        alertTime = _alertTime;
        startTime = _startTime;
        duration = _duration;
        location = _location;
    }

    public ToDo(int _todoID, int _userID, String _title, int _status, int _priority, String _alertTime, String _startTime, String _duration, Node _location){
        this(_todoID, _userID, _title, _status, _priority, Time.parseString(_alertTime), Time.parseString(_startTime), Time.parseString(_duration), _location);
    }

    public int getTodoID() {
        return todoID;
    }

    public void setTodoID(int todoID) {
        this.todoID = todoID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Time getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(Time alertTime) {
        this.alertTime = alertTime;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public Time getEndTime(){
        return startTime.add(duration);
    }

    public Node getLocation() {
        return location;
    }

    public void setLocation(Node location) {
        this.location = location;
    }

    @Override
    public int compareTo(ToDo t) {
        return startTime.compareTo(t.startTime);
    }

    public boolean isConflict(ToDo t) {
        return (t.startTime.compareTo(startTime) > 0 && t.startTime.compareTo(getEndTime()) < 0) ||
                (startTime.compareTo(t.startTime) > 0 && startTime.compareTo(t.getEndTime()) < 0);
    }
}
