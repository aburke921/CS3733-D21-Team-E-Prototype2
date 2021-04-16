package edu.wpi.TeamE.views.forms;

public class MedicineDeliveryForm {

    private String roomNumber;
    private String department;
    private String medicineName;
    private String quantity;
    private String dosage;
    private String specialInstructions;
    private boolean status;

    public MedicineDeliveryForm(String roomNumber, String department, String medicineName,
                                String quantity, String dosage, String specialInstructions) {

        this.roomNumber = roomNumber;
        this.department = department;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.dosage = dosage;
        this.specialInstructions = specialInstructions;

        this.status = false;

    }

    public String getRoomNumber() {
        return this.roomNumber;
    }

    public String getDepartment() {
        return this.department;
    }

    public String getMedicineName() {
        return this.medicineName;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public String getDosage() {
        return this.dosage;
    }

    public String getSpecialInstructions() {
        return this.specialInstructions;
    }


}
