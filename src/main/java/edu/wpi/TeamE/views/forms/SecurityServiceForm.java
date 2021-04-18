package edu.wpi.TeamE.views.forms;

import edu.wpi.TeamE.views.SecurityService;

public class SecurityServiceForm extends ServiceRequestForm{

    private String location;
    private String levelOfSecurity;
    private String levelOfUrgency;
    private String assignee;

    public SecurityServiceForm(String location, String levelOfSecurity, String levelOfUrgency, String assignee) {
        super.location = location;
        this.levelOfSecurity = levelOfSecurity;
        this.levelOfUrgency = levelOfUrgency;
        this.assignee = assignee;
        super.status = false;
    }

    public String getLocation() {
        return this.location;
    }

    public String getLevelOfSecurity() {
        return this.levelOfSecurity;
    }

    public String getLevelOfUrgency() {
        return this.levelOfUrgency;
    }

    public String getAssignee() {
        return this.assignee;
    }

}
