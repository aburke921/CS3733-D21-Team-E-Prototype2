package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class MedicineDeliveryObj extends ServiceRequestObjs {

    private String medicineName;
    private Integer doseQuantity;
    private Integer doseMeasure;
    private String specialInstructions;
    private String signature;

    public MedicineDeliveryObj(int requestID, int userID, int assigneeID, String nodeID, String medicineName, Integer doseQuantity, Integer doseMeasure, String specialInstructions, String signature) {
        super.requestID = requestID;
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

    public Integer getDoseQuantity() {
        return this.doseQuantity;
    }

    public Integer getDoseMeasure() {
        return this.doseMeasure;
    }

    public String getSpecialInstructions() {
        return this.specialInstructions;
    }

    public String getSignature() {
        return this.signature;
    }
}
