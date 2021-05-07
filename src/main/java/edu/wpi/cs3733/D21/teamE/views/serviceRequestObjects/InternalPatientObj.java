package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class InternalPatientObj extends ServiceRequestObjs {

    private String severity;
    private String dropOffNodeID;
    private int patientID;
    private String department;
    private String description;

    public InternalPatientObj(int requestID, int userID, String nodeID, String dropOffNodeID, int assigneeID, int patientID, String department, String severity, String description){

        super.requestID = requestID;
        super.userID = userID;
        super.nodeID = nodeID; //pickup location
        this.dropOffNodeID = dropOffNodeID;
        this.assigneeID = assigneeID;
        this.patientID = patientID;
        this.department = department;
        this.severity = severity;
        this.description = description;

    }

    public String getSeverity(){
        return this.severity;
    }

    public String getDropOffNodeID(){
        return this.dropOffNodeID;
    }

    public String getDepartment(){
        return this.department;
    }

    public int getPatientID(){
        return this.patientID;
    }

    public String getDescription(){
        return this.description;
    }
}
