/**
 * Sample Skeleton for 'FoodDelivery.fxml' Controller Class
 */

package edu.wpi.TeamE.views.serviceRequestControllers;

import java.io.IOException;

import javafx.collections.FXCollections;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.lang.String;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import edu.wpi.TeamE.databases.NodeDB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;






public class FoodDelivery extends ServiceRequestFormComponents {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="fullscreen"
	private Rectangle fullscreen; // Value injected by FXMLLoader

	@FXML // fx:id="hide"
	private Circle hide; // Value injected by FXMLLoader

	@FXML // fx:id="exit"
	private Polygon exit; // Value injected by FXMLLoader

	@FXML // fx:id="locationInput"
	private JFXComboBox<String> locationInput; // Value injected by FXMLLoader

	@FXML // fx:id="dietaryRestrictionsInput"
	private JFXComboBox<String> dietaryRestrictionsInput; // Value injected by FXMLLoader

	@FXML // fx:id="allergysInput"
	private JFXComboBox<String> allergysInput; // Value injected by FXMLLoader

	@FXML // fx:id="assigneeInput"
	private JFXComboBox<String> assigneeInput; // Value injected by FXMLLoader

	@FXML // fx:id="foodInput"
	private JFXComboBox<String> foodInput; // Value injected by FXMLLoader

	@FXML // fx:id="beveragesInput"
	private JFXComboBox<String> beveragesInput; // Value injected by FXMLLoader

	@FXML // fx:id="descriptionInput"
	private JFXTextArea descriptionInput; // Value injected by FXMLLoader

	@FXML // fx:id="ETAInput"
	private JFXTextField ETAInput; // Value injected by FXMLLoader

	@FXML // fx:id="cancel"
	private JFXButton cancel; // Value injected by FXMLLoader

	@FXML // fx:id="submit"
	private JFXButton submit; // Value injected by FXMLLoader

	@FXML
	void handleButtonCancel(ActionEvent event) {
		super.handleButtonCancel(event);
	}

	@FXML
	void saveData(ActionEvent event) {
		super.handleButtonSubmit(event);
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		ObservableList<String> locations  = NodeDB.getAllNodeLongNames();
		locationInput.setItems(locations);

		ArrayList<ArrayList<String>> menuStuff  = getMenuItems();
		ArrayList<String> foods = menuStuff.get(0);
		ArrayList<String> drinks = menuStuff.get(1);

		// Can use later:
		ArrayList<String> prices = menuStuff.get(2);
		ArrayList<String> calories = menuStuff.get(3);
		ArrayList<String> descriptions = menuStuff.get(4);

		ObservableList<String> observableFoods = FXCollections.observableList(foods);
		foodInput.setItems(observableFoods);

		ObservableList<String> observableBeverages = FXCollections.observableList(drinks);
		beveragesInput.setItems(observableBeverages);

		assert fullscreen != null : "fx:id=\"fullscreen\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert hide != null : "fx:id=\"hide\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert dietaryRestrictionsInput != null : "fx:id=\"dietaryRestrictionsInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert allergysInput != null : "fx:id=\"allergysInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert assigneeInput != null : "fx:id=\"assigneeInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert foodInput != null : "fx:id=\"foodInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert beveragesInput != null : "fx:id=\"beveragesInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert ETAInput != null : "fx:id=\"ETAInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'FoodDelivery.fxml'.";

	}

	public static ArrayList<ArrayList<String>> getMenuItems(){
		ArrayList<ArrayList<String>> menuItems = new ArrayList<ArrayList<String>>();

		try {
			Document doc = Jsoup.connect("https://order.aubonpain.com/menu/brigham-womens-hospital").get();
			Elements media = doc.select("[src]"); // --> image urls
			Elements foods = doc.getElementsByClass("product-name product__name");
			Elements prices = doc.getElementsByClass("product__attribute product__attribute--price");
			Elements calories = doc.getElementsByClass("product__attribute product__attribute--calorie-label");
			Elements descriptions = doc.getElementsByClass("product__description");


			ArrayList<String> listOfFoods = new ArrayList<String>();
			ArrayList<String> listOfDrinks = new ArrayList<String>();

			boolean drinkItem = false;
			boolean foodItem = true;
			for (Element item : foods) {
				if(item.text().equals("Hot Coffee & Hot Tea")){
					drinkItem = true;
					foodItem = false;
				}
				if(item.text().equals("Chips & Salty Snacks")){
					drinkItem = false;
					foodItem = false;
				}
				if(item.text().equals("Bottled Spring Water (20 oz)")){
					drinkItem = true;
					foodItem = false;
				}
				if(item.text().equals("Cape Cod Lightly Salted Chips")){
					drinkItem = false;
					foodItem = true;
				}

				if(drinkItem){
					listOfDrinks.add(item.text());
				}
				if(foodItem){
					listOfFoods.add(item.text());
				}
			}

			menuItems.add(listOfFoods);
			menuItems.add(listOfDrinks);

			ArrayList<String> listOfPrices = new ArrayList<>();
			for(Element price : prices){
				listOfPrices.add(price.text());
			}
			menuItems.add(listOfPrices);

			ArrayList<String> listOfCalories = new ArrayList<>();
			for(Element calory : calories){
				listOfCalories.add(calory.text());
			}
			menuItems.add(listOfPrices);


			ArrayList<String> listOfDescriptions = new ArrayList<>();
			for(Element description : descriptions){
				listOfDescriptions.add(description.text());

			}
			menuItems.add(listOfDescriptions);

			return menuItems;
		}catch (IOException e){
			System.err.println("Error connecting to the website");
			return null;
		}
	}
}
