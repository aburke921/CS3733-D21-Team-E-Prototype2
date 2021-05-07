package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class ExternalPatientObj extends ServiceRequestObjs {

    private String severity;

    private String requestType;

    private String patientID;

    private String bloodPressure;

    private String temperature;

    private String oxygenLevel;

    private String details;

    public ExternalPatientObj(int requestID, int userID, int assigneeID, String nodeID, String severity, String requestType, String patientID, String bloodPressure, String temperature, String oxygenLevel, String details) {

        super.requestID = requestID;
        super.assigneeID = assigneeID;
        super.userID = userID;
        super.nodeID = nodeID;
        this.severity = severity;
        this.requestType = requestType;
        this.patientID = patientID;
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
        this.details = details;
        super.status = "In Progress";

    }

    public String getSeverity() {
        return  this.severity;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public String getPatientID() {
        return this.patientID;
    }

    public String getBloodPressure() {
        return this.bloodPressure;
    }
    public String getTemperature() {
        return this.temperature;
    }

    public String getOxygenLevel() {
        return this.oxygenLevel;
    }

    public String getDetails() {
        return this.details;
    }





}
