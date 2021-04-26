package edu.wpi.TeamE.views.serviceRequestObjects;

import edu.wpi.TeamE.views.serviceRequestControllers.Maintenance;

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

}
