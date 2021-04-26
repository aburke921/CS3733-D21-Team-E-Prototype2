package edu.wpi.TeamE.views.serviceRequestObjects;

public class LanguageInterpreter extends ServiceRequestObjs {

    private String language;
    private String description;

    public LanguageInterpreter(String nodeID, int assigneeID, int userID, String language, String description) {

        super.nodeID = nodeID;
        super.assigneeID = assigneeID;
        super.userID = userID;
        this.language = language;
        this.description = description;

    }


}
