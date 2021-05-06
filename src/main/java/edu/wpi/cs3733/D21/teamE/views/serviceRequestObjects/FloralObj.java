package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class FloralObj extends ServiceRequestObjs {

    private String flower;
    private int count;
    private String vase;
    private String recipient;
    private String message;
    private String arrangement;
    private String stuffedAnimal;
    private String chocolate;


    public FloralObj(int requestID, int userID, int assigneeID, String nodeID, String recipient, String flower, int count, String vase, String arrangement, String stuffedAnimal, String chocolate, String message) {

        super.requestID = requestID;
        super.nodeID = nodeID;
        super.assigneeID = assigneeID;
        super.userID = userID;
        this.flower = flower;
        this.count = count;
        this.vase = vase;
        this.recipient = recipient;
        this.message = message;
        super.status = "In Progress";
        this.arrangement = arrangement;
        this.stuffedAnimal = stuffedAnimal;
        this.chocolate = chocolate;

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

    public String getChocolate(){
        return this.chocolate;
    }

    public String getStuffedAnimal(){
        return this.stuffedAnimal;
    }

    public String getArrangement(){
        return this.arrangement;
    }

}
