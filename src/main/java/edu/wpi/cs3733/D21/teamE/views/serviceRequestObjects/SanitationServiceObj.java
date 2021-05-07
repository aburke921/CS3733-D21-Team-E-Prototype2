package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class SanitationServiceObj extends ServiceRequestObjs {

    private String signature;
    private String detail;
    private String service;
    private String severity;

    public SanitationServiceObj(int requestID, int userID, int assigneeID, String nodeID, String service, String detail, String severity, String signature) {
        super.requestID = requestID;
        super.nodeID = nodeID;
        super.assigneeID = assigneeID;
        super.userID = userID;
        this.signature = signature;
        this.detail = detail;
        this.service = service;
        this.severity = severity;
        super.status = "In Progress";
    }

    public String getSignature() {
        return this.signature;
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
