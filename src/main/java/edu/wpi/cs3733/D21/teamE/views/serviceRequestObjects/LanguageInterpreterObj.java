package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class LanguageInterpreterObj extends ServiceRequestObjs {

    private String language;
    private String description;

    public LanguageInterpreterObj(String nodeID, int assigneeID, int userID, String language, String description) {

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
