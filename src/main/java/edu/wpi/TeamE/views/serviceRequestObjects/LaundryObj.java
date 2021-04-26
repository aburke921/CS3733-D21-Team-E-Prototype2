package edu.wpi.TeamE.views.serviceRequestObjects;

public class LaundryObj extends ServiceRequestObjs {

    private String washLoadAmount;
    private String dryLoadAmount;
    private String description;

    public LaundryObj(String nodeID, int assigneeID, int userID, String washLoadAmount, String dryLoadAmount, String description) {

        super.assigneeID = assigneeID;
        super.nodeID = nodeID;
        super.userID = userID;
        this.washLoadAmount = washLoadAmount;
        this.dryLoadAmount = dryLoadAmount;
        this.description = description;

    }


}
