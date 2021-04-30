package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class FloralObj extends ServiceRequestObjs {

    private String flower;
    private int count;
    private String vase;
    private String recipient;
    private String message;

    public FloralObj(String nodeID, int assigneeID, int userID, String flower, int count, String vase, String recipient, String message) {

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


    public String getFlower() {
        return this.flower;
    }

    public int getCount() {
        return this.count;
    }

    public String getVase() {
        return this.vase;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public String getMessage() {
        return this.message;
    }

}
