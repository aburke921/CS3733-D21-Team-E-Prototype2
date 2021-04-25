/**
 * Sample Skeleton for 'FoodDelivery.fxml' Controller Class
 */

package edu.wpi.TeamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.lang.String;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class FoodDelivery {

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
	private JFXComboBox<?> allergysInput; // Value injected by FXMLLoader

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

	}

	@FXML
	void saveData(ActionEvent event) {

	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
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
}
