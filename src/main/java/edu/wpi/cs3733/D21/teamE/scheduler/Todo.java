package edu.wpi.cs3733.D21.teamE.scheduler;

import edu.wpi.cs3733.D21.teamE.Time;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.ServiceRequestObjs;

public class Todo {
    ServiceRequestObjs service;
    Node location;
    Time startTime;
    Time duration;

    public Todo(){

    }

    public ServiceRequestObjs getService() {
        return service;
    }

    public void setService(ServiceRequestObjs service) {
        this.service = service;
    }

    public Node getLocation() {
        return location;
    }

    public void setLocation(Node location) {
        this.location = location;
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

}
