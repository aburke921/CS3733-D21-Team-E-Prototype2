package edu.wpi.TeamE.views.serviceRequestObjects;

import edu.wpi.TeamE.views.serviceRequestControllers.ServiceRequestFormComponents;

public class ReligiousRequestObj extends ServiceRequestObjs {

    private String religion;
    private String description;

    public ReligiousRequestObj(String nodeID, int userID, int assigneeID, String religion, String description) {

        super.nodeID = nodeID;
        super.userID = userID;
        super.assigneeID = assigneeID;
        this.religion = religion;
        this.description = description;
        super.status = "In Progress";

    }

}
