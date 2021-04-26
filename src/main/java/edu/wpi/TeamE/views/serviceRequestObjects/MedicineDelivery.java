package edu.wpi.TeamE.views.serviceRequestObjects;

public class MedicineDelivery extends ServiceRequestObjs {

    private String severity;
    private String author;
    private String description;
    private String eta;
    private String type;

    public MedicineDelivery(String nodeID, int assigneeID, int userID, String type, String severity, String author, String description, String eta) {

        super.assigneeID = assigneeID;
        super.nodeID = nodeID;
        super.userID = userID;
        this.type = type;
        this.severity = severity;
        this.author = author;
        this.description = description;
        this.eta = eta;

    }
}
