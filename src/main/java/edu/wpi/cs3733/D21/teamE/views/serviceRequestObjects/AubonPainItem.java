package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

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

	public AubonPainItem(String foodItem, String foodPrice, String foodCalories, String foodDescription){

		this.imageURL = null;
		this.foodItem = foodItem;
		this.foodPrice = foodPrice;
		this.foodCalories = foodCalories;
		this.foodDescription = foodDescription;

	}


	public AubonPainItem(String foodItem, String foodPrice, String foodCalories){
		this.imageURL = null;
		this.foodItem = foodItem;
		this.foodPrice = foodPrice;
		this.foodCalories = foodCalories;
		this.foodDescription = null;
	}

	public AubonPainItem(String foodItem){
		this.imageURL = null;
		this.foodItem = foodItem;
		this.foodPrice = null;
		this.foodCalories = null;
		this.foodDescription = null;
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
