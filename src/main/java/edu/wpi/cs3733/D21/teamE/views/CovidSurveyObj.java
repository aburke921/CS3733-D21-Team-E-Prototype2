package edu.wpi.cs3733.D21.teamE.views;

//import com.sun.org.apache.xml.internal.security.algorithms.implementations.IntegrityHmac;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class CovidSurveyObj extends RecursiveTreeObject<CovidSurveyObj> {

    private Integer user;
    private String userDisplay;
    private Integer formNumber;
    private String formNumberDisplay;
    private Boolean positiveTest;
    private String positiveTestDisplay;
    private Boolean symptoms;
    private String symptomsDisplay;
    private Boolean closeContact;
    private String closeContactDisplay;
    private Boolean quarantine;
    private String quarantineDisplay;
    private Boolean noSymptoms;
    private String noSymptomsDisplay;
    private String status;

    public CovidSurveyObj(Integer user, Integer formNumber, Boolean positiveTest, Boolean symptoms, Boolean closeContact, Boolean quarantine, Boolean noSymptoms, String status) {
        this.user = user;
        this.userDisplay = user.toString();
        this.formNumber = formNumber;
        this.formNumberDisplay = formNumber.toString();
        this.positiveTest = positiveTest;
        this.positiveTestDisplay = positiveTest.toString();
        this.symptoms = symptoms;
        this.symptomsDisplay = symptoms.toString();
        this.closeContact = closeContact;
        this.closeContactDisplay = closeContact.toString();
        this.quarantine = quarantine;
        this.quarantineDisplay = quarantine.toString();
        this.noSymptoms = noSymptoms;
        this.noSymptomsDisplay = noSymptoms.toString();
        this.status = status;
    }

    public CovidSurveyObj(String userDisplay) {
        this.user = null;
        this.userDisplay = userDisplay;
        this.formNumber = null;
        this.formNumberDisplay = null;
        this.positiveTest = null;
        this.positiveTestDisplay = null;
        this.symptoms = null;
        this.symptomsDisplay = null;
        this.closeContact = null;
        this.closeContactDisplay = null;
        this.quarantine = null;
        this.quarantineDisplay = null;
        this.noSymptoms = null;
        this.noSymptomsDisplay = null;
        this.status = null;
    }

    public Integer getUser() {
        return this.user;
    }

    public String getUserDisplay() {
        return this.userDisplay;
    }

    public Integer getFormNumber() {
        return this.formNumber;
    }

    public String getFormNumberDisplay() {
        return this.formNumberDisplay;
    }

    public boolean getPositiveTest() {
        return this.positiveTest;
    }

    public String getPositiveTestDisplay() {
        return this.positiveTestDisplay;
    }

    public boolean getSymptoms() {
        return this.symptoms;
    }

    public String getSymptomsDisplay() {
        return this.symptomsDisplay;
    }

    public boolean getCloseContact() {
        return this.closeContact;
    }

    public String getCloseContactDisplay() {
        return this.closeContactDisplay;
    }

    public boolean getQuarantine() {
        return this.quarantine;
    }

    public String getQuarantineDisplay() {
        return this.quarantineDisplay;
    }

    public boolean getNoSymptoms() {
        return this.noSymptoms;
    }

    public String getNoSymptomsDisplay() {
        return this.noSymptomsDisplay;
    }

    public String getStatus() {
        return this.status;
    }
}
