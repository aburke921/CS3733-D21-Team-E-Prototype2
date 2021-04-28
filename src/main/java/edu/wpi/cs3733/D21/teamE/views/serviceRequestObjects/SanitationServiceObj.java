package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

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
