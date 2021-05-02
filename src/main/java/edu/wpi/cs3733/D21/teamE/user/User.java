package edu.wpi.cs3733.D21.teamE.user;

import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.map.Path;
import edu.wpi.cs3733.D21.teamE.Time;
import edu.wpi.cs3733.D21.teamE.scheduler.Appointment;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.ServiceRequestObjs;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class User {
    private String userType;
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private List<Appointment> schedule;

    public User(String _userType, int _userID, String _firstName, String _lastName, String _email) {

        this.userType = _userType;
        this.userID = _userID;
        this.firstName = _firstName;
        this.lastName = _lastName;
        this.email = _email;

        this.schedule = new LinkedList<>();
    }

    public StringProperty getProperty(String field) {
        String fieldData;
        if(field.equalsIgnoreCase("userType")){
            fieldData = userType;
        } else if(field.equalsIgnoreCase("firstName")){
            fieldData = firstName;
        } else if(field.equalsIgnoreCase("lastName")){
            fieldData = lastName;
        } else if(field.equalsIgnoreCase("email")){
            fieldData = email;
        } else {
            return null;
        }
        return new SimpleStringProperty(fieldData);
    }

    public IntegerProperty getIdProperty(){
        return new SimpleIntegerProperty(userID);
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}