package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class ServiceRequestForm {

    private String id;
    private String location;
    private String assignee;
    private String status;

    public ServiceRequestForm(String id, String location, String assignee, String status) {
        this.id = id;
        this.location = location;
        this.assignee = assignee;
        this.status = status;
    }

    public ServiceRequestForm(String id) {
        this.id = id;
        this.location = "";
        this.assignee = "";
        this.status = "";
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getStatus() {
        return status;
    }
}
