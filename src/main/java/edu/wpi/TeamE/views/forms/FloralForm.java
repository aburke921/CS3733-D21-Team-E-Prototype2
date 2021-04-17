package edu.wpi.TeamE.views.forms;

public class FloralForm extends ServiceRequestForm{
    private String patient;
    private String flowerType;
    private String flowerAmount;
    private String vaseType;
    private String message;


    public FloralForm(String patient, String room, String flowerType, String flowerAmount,
                      String vaseType, String message) {

        this.patient = patient;
        super.location = room;
        this.flowerType = flowerType;
        this.flowerAmount = flowerAmount;
        this.vaseType = vaseType;
        this.message = message;
        super.status = false;

    }

    public String getPatient() {
        return this.patient;
    }

    public String getRoom() {
        return this.location;
    }

    public String getFlowerType() {
        return this.flowerType;
    }

    public String getFlowerAmount() {
        return this.flowerAmount;
    }

    public String getVaseType() {
        return this.vaseType;
    }

    public String getMessage() {
        return this.message;
    }
}
