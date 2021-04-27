package edu.wpi.TeamE.views.serviceRequestObjects;

public class AubonPainItem {

	String imageURL;
	String foodItem;
	String foodPrice;
	String foodCalories;
	String foodDescription;

	public AubonPainItem(String imageURL, String foodItem, String foodPrice, String foodCalories, String foodDescription){

		this.imageURL = imageURL;
		this.foodItem = foodItem;
		this.foodPrice = foodPrice;
		this.foodCalories = foodCalories;
		this.foodDescription = foodDescription;

	}

	public String getImageURL(){
		return this.imageURL;
	}

	public String getFoodItem(){
		return this.foodItem;
	}

	public String getFoodPrice(){
		return this.foodPrice;
	}

	public String getFoodCalories(){
		return this.foodCalories;
	}

	public String getFoodDescription(){
		return this.foodDescription;
	}

}
