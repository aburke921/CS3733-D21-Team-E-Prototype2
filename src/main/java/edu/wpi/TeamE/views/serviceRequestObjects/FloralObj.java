package edu.wpi.TeamE.views.serviceRequestObjects;

public class FloralObj extends ServiceRequestObjs {

    private String flower;
    private String count;
    private String vase;
    private String recipient;
    private String message;

    public FloralObj(String nodeID, int assigneeID, int userID, String flower, String count, String vase, String recipient, String message) {

        super.nodeID = nodeID;
        super.assigneeID = assigneeID;
        super.userID = userID;
        this.flower = flower;
        this.count = count;
        this.vase = vase;
        this.recipient = recipient;
        this.message = message;
        super.status = "In Progress";

    }



}
