package edu.wpi.TeamE.views.serviceRequestObjects;

public class SanitationServiceObj extends ServiceRequestObjs {

    private String signature;
    private String detail;
    private String service;
    private String severity;

    public SanitationServiceObj(String nodeID, int assigneeID, int userID, String  signature, String detail, String service, String severity) {
        super.nodeID = nodeID;
        super.assigneeID = assigneeID;
        super.userID = userID;
        this.signature = signature;
        this.detail = detail;
        this.service = service;
        this.severity = severity;
        super.status = "In Progress";
    }
}
