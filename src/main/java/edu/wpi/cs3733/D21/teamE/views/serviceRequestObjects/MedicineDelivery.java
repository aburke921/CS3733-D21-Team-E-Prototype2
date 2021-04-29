package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class MedicineDelivery extends ServiceRequestObjs {

    private String medicineName;
    private String doseQuantity;
    private String doseMeasure;
    private String specialInstructions;
    private String signature;

    public MedicineDelivery(String nodeID, int assigneeID, int userID, String medicineName, String doseQuantity, String doseMeasure, String specialInstructions, String signature) {

        super.assigneeID = assigneeID;
        super.nodeID = nodeID;
        super.userID = userID;
        this.medicineName = medicineName;
        this.doseQuantity = doseQuantity;
        this.doseMeasure = doseMeasure;
        this.specialInstructions = specialInstructions;
        this.signature = signature;
        super.status = "In Progress";

    }

    public String getMedicineName() {
        return this.medicineName;
    }

    public String getDoseQuantity() {
        return this.doseQuantity;
    }

    public String getDoseMeasure() {
        return this.doseMeasure;
    }

    public String getSpecialInstructions() {
        return this.specialInstructions;
    }

    public String getSignature() {
        return this.signature;
    }
}
