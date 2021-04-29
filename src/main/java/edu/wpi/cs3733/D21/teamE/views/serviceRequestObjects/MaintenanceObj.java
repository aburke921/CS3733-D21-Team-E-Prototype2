package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class MaintenanceObj extends ServiceRequestObjs {

    private String request;
    private String severity;
    private String author;
    private String eta;
    private String description;

    public MaintenanceObj(String nodeID, int userID, int assigneeID, String request, String severity, String author, String eta, String description) {

        super.nodeID = nodeID;
        super.userID = userID;
        super.assigneeID = assigneeID;
        this.request = request;
        this.severity = severity;
        this.author = author;
        this.eta = eta;
        this.description = description;
        super.status = "In Progress";

    }

    public String getRequest() {
        return this.request;
    }

    public String getSeverity() {
        return this.severity;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getEta() {
        return this.eta;
    }

    public String getDescription() {
        return this.description;
    }


}
