package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class SanitationServiceObj extends ServiceRequestObjs {

    private String detail;
    private String service;
    private String severity;

    public SanitationServiceObj(int requestID, int userID, int assigneeID, String nodeID, String service, String detail, String severity) {
        super.requestID = requestID;
        super.nodeID = nodeID;
        super.assigneeID = assigneeID;
        super.userID = userID;
        this.detail = detail;
        this.service = service;
        this.severity = severity;
        super.status = "In Progress";
    }

    public String getDetail() {
        return this.detail;
    }

    public String getService() {
        return this.service;
    }

    public String getSeverity() {
        return this.severity;
    }
}
