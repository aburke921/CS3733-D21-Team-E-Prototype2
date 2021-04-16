package edu.wpi.TeamE.views.forms;

public class ExternalPatientForm {

    private String hospital;
    private String type;
    private String severity;
    private String patientID;
    private String description;
    private String eta;
    private boolean status;

    public ExternalPatientForm(String hospital, String type, String severity,
                               String patientID, String description, String eta) {
        this.hospital = hospital;
        this.type = type;
        this.severity = severity;
        this.patientID = patientID;
        this.description = description;
        this.eta = eta;
        this.status = false;
    }

    public String getHospital() {
        return this.hospital;
    }

    public String getType() {
        return this.type;
    }

    public String getSeverity() {
        return this.severity;
    }

    public String getPatientID() {
        return this.patientID;
    }

    public String getDescription() {
        return this.description;
    }

    public String getEta() {
        return this.eta;
    }
}
