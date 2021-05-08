package edu.wpi.cs3733.D21.teamE.scheduler;

import edu.wpi.cs3733.D21.teamE.Date;
import edu.wpi.cs3733.D21.teamE.Time;
import edu.wpi.cs3733.D21.teamE.map.Node;


public class ToDo implements Comparable<ToDo>{
    private final int todoID;
    private final int userID;
    private final String title;
    private final int status;
    private final int priority;

    //possibly null
    private Date scheduledDate;
    private Time scheduledTime;
    private Node location;
    private String detail;
    private Time duration;
    private Date notificationDate;
    private Time notificationTime;

    public ToDo(int _todoID, int _userID, String _title, int _status, int _priority){
        todoID = _todoID;
        userID = _userID;
        title = _title;
        status = _status;
        priority = _priority;
    }

    public ToDo(int _todoID, int _userID, String _title, int _status, int _priority, Date _scheduleDate, Time _scheduleTime, Node _location, String _detail, Time _duration, Date _notificationDate, Time _notificationTime){
        this(_todoID, _userID, _title, _status, _priority);
        scheduledDate = _scheduleDate;
        scheduledTime = _scheduleTime;
        location = _location;
        detail = _detail;
        duration = _duration;
        notificationDate = _notificationDate;
        notificationTime = _notificationTime;
    }

    public ToDo(int _todoID, int _userID, String _title, int _status, int _priority, String _scheduleDate, String _scheduleTime, Node _location, String _detail, String _duration, String _notificationDate, String _notificationTime){
        this(_todoID, _userID, _title, _status, _priority, Date.parseString(_scheduleDate), Time.parseString(_scheduleTime), _location, _detail, Time.parseString(_duration), Date.parseString(_notificationDate), Time.parseString(_notificationTime));
    }

    public int getTodoID() {
        return todoID;
    }

    public int getUserID() {
        return userID;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }

    public Time getNotificationTime() {
        return notificationTime;
    }

    public Time getScheduledTime() {
        return scheduledTime;
    }

    public Time getDuration() {
        return duration;
    }

    public Time getEndTime(){
        return scheduledTime.add(duration);
    }

    public Node getLocation() {
        return location;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public String getDetail() {
        return detail;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    @Override
    public int compareTo(ToDo t) {
        return scheduledTime.compareTo(t.scheduledTime);
    }

    public boolean isConflict(ToDo t) {
        return (t.scheduledTime.compareTo(scheduledTime) > 0 && t.scheduledTime.compareTo(getEndTime()) < 0) ||
                (scheduledTime.compareTo(t.scheduledTime) > 0 && scheduledTime.compareTo(t.getEndTime()) < 0);
    }
}
