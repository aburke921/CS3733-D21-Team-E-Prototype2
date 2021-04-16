package edu.wpi.TeamE.views.forms;

public class FloralForm {
    private String patient;
    private String room;
    private String flowerType;
    private String flowerAmount;
    private String vaseType;
    private String message;
    private boolean status;

    public FloralForm(String patient, String room, String flowerType, String flowerAmount,
                      String vaseType, String message) {

        this.patient = patient;
        this.room = room;
        this.flowerType = flowerType;
        this.flowerAmount = flowerAmount;
        this.vaseType = vaseType;
        this.message = message;
        this.status = false;

    }

    public String getPatient() {
        return this.patient;
    }

    public String getRoom() {
        return this.room;
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
