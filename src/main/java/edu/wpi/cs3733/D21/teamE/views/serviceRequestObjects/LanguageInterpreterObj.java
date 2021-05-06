package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class LanguageInterpreterObj extends ServiceRequestObjs {

    private String language;
    private String description;



    public LanguageInterpreterObj(int requestID, int userID, int assigneeID, String nodeID, String language, String description) {

        super.requestID = requestID;
        super.nodeID = nodeID;
        super.assigneeID = assigneeID;
        super.userID = userID;
        this.language = language;
        this.description = description;
        super.status = "In Progress";

    }

    public String getLanguage() {
        return this.language;
    }

    public String getDescription() {
        return this.description;
    }


}
