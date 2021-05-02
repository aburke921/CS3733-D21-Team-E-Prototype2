package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

/**
 * Sample Skeleton for 'Appointment.fxml' Controller Class
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Appointment {

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

	@FXML // fx:id="DoctorInput"
	private JFXComboBox<?> DoctorInput; // Value injected by FXMLLoader

	@FXML // fx:id="descriptionInput"
	private JFXTextArea descriptionInput; // Value injected by FXMLLoader

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
		assert fullscreen != null : "fx:id=\"fullscreen\" was not injected: check your FXML file 'Appointment.fxml'.";
		assert hide != null : "fx:id=\"hide\" was not injected: check your FXML file 'Appointment.fxml'.";
		assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'Appointment.fxml'.";
		assert DoctorInput != null : "fx:id=\"DoctorInput\" was not injected: check your FXML file 'Appointment.fxml'.";
		assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'Appointment.fxml'.";
		assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'Appointment.fxml'.";
		assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'Appointment.fxml'.";

	}
}
