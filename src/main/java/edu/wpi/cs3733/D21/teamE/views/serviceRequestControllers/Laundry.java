/**
 * Sample Skeleton for 'Laundry.fxml' Controller Class
 */

package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.lang.String;
import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Laundry extends ServiceRequestFormComponents {

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

    @FXML // fx:id="washLoadAmountInput"
    private JFXComboBox<String> washLoadAmountInput; // Value injected by FXMLLoader

    @FXML // fx:id="dryLoadAmountInput"
    private JFXComboBox<String> dryLoadAmountInput; // Value injected by FXMLLoader

    @FXML // fx:id="assignedPersonnel"
    private JFXTextField assignedPersonnel; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionInput"
    private JFXTextArea descriptionInput; // Value injected by FXMLLoader

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

        assert fullscreen != null : "fx:id=\"fullscreen\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert hide != null : "fx:id=\"hide\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file 'Laundry.fxml'.";
        ObservableList<String> locations = DB.getAllNodeLongNames();
        locationInput.setItems(locations);
        assert washLoadAmountInput != null : "fx:id=\"washLoadAmountInput\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert dryLoadAmountInput != null : "fx:id=\"dryLoadAmountInput\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert assignedPersonnel != null : "fx:id=\"assignedPersonnel\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'Laundry.fxml'.";
        assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'Laundry.fxml'.";

    }
}
