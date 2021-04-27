package edu.wpi.TeamE.views.serviceRequestObjects;

public abstract class ServiceRequestObjs {

    protected String nodeID;
    protected int userID;
    protected int assigneeID;
    protected String status;

    public String getNodeID() {
        return this.nodeID;
    }

    public int getUserID() {
        return this.userID;
    }

    public int getAssigneeID() {
        return this.assigneeID;
    }

    public String getStatus() {
        return this.status;
    }

}
