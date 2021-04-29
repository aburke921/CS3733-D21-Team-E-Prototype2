package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

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
        super.status = "In Progress";

    }

    public String getDietaryRestrictions() {
        return this.dietaryRestrictions;
    }

    public String getAllergies() {
        return this.allergies;
    }

    public String getFood() {
        return this.food;
    }

    public String getBeverage() {
        return this.beverage;
    }

    public String getDescription() {
        return this.description;
    }
}
