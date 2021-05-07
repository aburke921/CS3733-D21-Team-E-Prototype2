package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class SecurityServiceObj extends ServiceRequestObjs {

    private String securityLevel;
    private String urgencyLevel;
    private String reason;



    public SecurityServiceObj(int requestID, int userID, int assigneeID, String nodeID, String securityLevel, String urgencyLevel, String reason) {

        super.requestID = requestID;
        super.nodeID = nodeID;
        super.assigneeID = assigneeID;
        super.userID = userID;
        this.securityLevel = securityLevel;
        this.urgencyLevel = urgencyLevel;
        this.reason = reason;
        super.status = "In Progress";

    }

    public String getSecurityLevel() {
        return this.securityLevel;
    }

    public String getUrgencyLevel() {
        return this.urgencyLevel;
    }

    public String getReason() {
        return this.reason;
    }

}
