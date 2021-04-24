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
	private JFXComboBox<?> locationInput; // Value injected by FXMLLoader

	@FXML // fx:id="ambulance1"
	private String ambulance1; // Value injected by FXMLLoader

	@FXML // fx:id="helicopter1"
	private String helicopter1; // Value injected by FXMLLoader

	@FXML // fx:id="plane1"
	private String plane1; // Value injected by FXMLLoader

	@FXML // fx:id="dietaryRestrictionsInput"
	private JFXComboBox<?> dietaryRestrictionsInput; // Value injected by FXMLLoader

	@FXML // fx:id="ambulance"
	private String ambulance; // Value injected by FXMLLoader

	@FXML // fx:id="helicopter"
	private String helicopter; // Value injected by FXMLLoader

	@FXML // fx:id="plane"
	private String plane; // Value injected by FXMLLoader

	@FXML // fx:id="allergysInput"
	private JFXComboBox<?> allergysInput; // Value injected by FXMLLoader

	@FXML // fx:id="high_severity"
	private String high_severity; // Value injected by FXMLLoader

	@FXML // fx:id="medium_severity"
	private String medium_severity; // Value injected by FXMLLoader

	@FXML // fx:id="low_severity"
	private String low_severity; // Value injected by FXMLLoader

	@FXML // fx:id="assigneeInput"
	private JFXComboBox<?> assigneeInput; // Value injected by FXMLLoader

	@FXML // fx:id="ambulance11"
	private String ambulance11; // Value injected by FXMLLoader

	@FXML // fx:id="helicopter11"
	private String helicopter11; // Value injected by FXMLLoader

	@FXML // fx:id="plane11"
	private String plane11; // Value injected by FXMLLoader

	@FXML // fx:id="foodInput"
	private JFXComboBox<?> foodInput; // Value injected by FXMLLoader

	@FXML // fx:id="ambulance111"
	private String ambulance111; // Value injected by FXMLLoader

	@FXML // fx:id="helicopter111"
	private String helicopter111; // Value injected by FXMLLoader

	@FXML // fx:id="plane111"
	private String plane111; // Value injected by FXMLLoader

	@FXML // fx:id="beveragesInput"
	private JFXComboBox<?> beveragesInput; // Value injected by FXMLLoader

	@FXML // fx:id="ambulance1111"
	private String ambulance1111; // Value injected by FXMLLoader

	@FXML // fx:id="helicopter1111"
	private String helicopter1111; // Value injected by FXMLLoader

	@FXML // fx:id="plane1111"
	private String plane1111; // Value injected by FXMLLoader

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
		assert ambulance1 != null : "fx:id=\"ambulance1\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert helicopter1 != null : "fx:id=\"helicopter1\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert plane1 != null : "fx:id=\"plane1\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert dietaryRestrictionsInput != null : "fx:id=\"dietaryRestrictionsInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert ambulance != null : "fx:id=\"ambulance\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert helicopter != null : "fx:id=\"helicopter\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert plane != null : "fx:id=\"plane\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert allergysInput != null : "fx:id=\"allergysInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert high_severity != null : "fx:id=\"high_severity\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert medium_severity != null : "fx:id=\"medium_severity\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert low_severity != null : "fx:id=\"low_severity\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert assigneeInput != null : "fx:id=\"assigneeInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert ambulance11 != null : "fx:id=\"ambulance11\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert helicopter11 != null : "fx:id=\"helicopter11\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert plane11 != null : "fx:id=\"plane11\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert foodInput != null : "fx:id=\"foodInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert ambulance111 != null : "fx:id=\"ambulance111\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert helicopter111 != null : "fx:id=\"helicopter111\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert plane111 != null : "fx:id=\"plane111\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert beveragesInput != null : "fx:id=\"beveragesInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert ambulance1111 != null : "fx:id=\"ambulance1111\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert helicopter1111 != null : "fx:id=\"helicopter1111\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert plane1111 != null : "fx:id=\"plane1111\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert ETAInput != null : "fx:id=\"ETAInput\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'FoodDelivery.fxml'.";
		assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'FoodDelivery.fxml'.";

	}
}
