package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class MaintenanceObj extends ServiceRequestObjs {

    private String request;
    private String severity;
    private String description;

    public MaintenanceObj(int requestID, int userID, String nodeID, int assigneeID, String request, String severity, String description) {

        super.requestID = requestID;
        super.nodeID = nodeID;
        super.userID = userID;
        super.assigneeID = assigneeID;
        this.request = request;
        this.severity = severity;
        this.description = description;
        super.status = "In Progress";

    }

    public String getRequest() {
        return this.request;
    }

    public String getSeverity() {
        return this.severity;
    }

    public String getDescription() {
        return this.description;
    }


}
