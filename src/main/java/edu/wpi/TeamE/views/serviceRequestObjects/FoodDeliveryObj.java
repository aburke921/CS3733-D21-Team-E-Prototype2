package edu.wpi.TeamE.views.serviceRequestObjects;

public class FoodDeliveryObj extends ServiceRequestObjs {

    private String dietaryRestrictions;
    private String allergies;
    private String food;
    private String beverage;
    private String description;

    public FoodDeliveryObj(String nodeID, int userID, int assigneeID, String dietaryRestrictions, String allergies, String food, String beverage, String description) {

        super.nodeID = nodeID;
        super.userID = userID;
        super.assigneeID = assigneeID;
        this.dietaryRestrictions = dietaryRestrictions;
        this.allergies = allergies;
        this.food = food;
        this.beverage = beverage;
        this.description = description;

    }
}
