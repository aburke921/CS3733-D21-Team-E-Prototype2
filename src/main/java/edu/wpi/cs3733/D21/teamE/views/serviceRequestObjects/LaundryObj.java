package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

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
        super.status = "In Progress";

    }

    public String getWashLoadAmount() {
        return  this.washLoadAmount;
    }

    public String getDryLoadAmount() {
        return this.dryLoadAmount;
    }

    public String getDescription() {
        return this.description;
    }


}
