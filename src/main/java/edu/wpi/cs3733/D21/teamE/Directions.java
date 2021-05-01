package edu.wpi.cs3733.D21.teamE;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Scanner;


public class Directions {

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane;
    @FXML // fx:id="stackPane"
    private StackPane stackPane; //main stack pane used for JFXDialog popups

    @FXML // fx:id="address"
    private JFXTextField address;
    @FXML // fx:id="city"
    private JFXTextField city;
    @FXML // fx:id="state"
    private JFXTextField state;
    @FXML // fx:id="zip"
    private JFXTextField zip;

    @FXML // fx:id="car"
    private JFXButton car;
    @FXML // fx:id="transit"
    private JFXButton transit;
    @FXML // fx:id="bike"
    private JFXButton bike;
    @FXML // fx:id="walking"
    private JFXButton walking;

    // TODO: make `modes` a toggle group

    @FXML // fx:id="getDir"
    private JFXButton getDir;

    @FXML // fix:id="backButton"
    public Button backButton; // Value injected by FXMLLoader


    public void initialize() {
        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false);
            App.setShowLogin(true);
            App.setStackPane(stackPane);
            App.setPageTitle("Home");
            App.setHelpText(""); //todo help text
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }
        DirectionsController.init();
    }

    @FXML
    private void getDirections(ActionEvent e) {
        StringBuilder origin = new StringBuilder();
        origin.append(address.textProperty().get()).append(" ");
        origin.append(city.textProperty().get()).append(", ");
        origin.append(state.textProperty().get()).append(" ");
        origin.append(zip.textProperty().get());

        //System.out.println("\nEnter Desired Time of Arrival");
        //String arrivalTime = io.nextLine();
        // TODO: Figure out time scheduler

        try {
            DirectionsResult result = DirectionsController.getDirections(origin.toString());
            DirectionsLeg trip = result.routes[0].legs[0];
            System.out.println("\nFrom: " + trip.startAddress + " to " + trip.endAddress);
            System.out.println("Distance: " + trip.distance + "\tDuration: " + trip.duration + "\tDuration with Traffic: " + trip.durationInTraffic);
            // TODO: Figure out directions listing
            System.out.println();
            for (DirectionsStep step: trip.steps) {
                System.out.println(step.toString());
            }
        } catch (IOException exception) {
            System.err.println("IO Exception: " + exception.getMessage());
        } catch (InterruptedException exception) {
            System.err.println("Interrupted Exception: " + exception.getMessage());
        } catch (ApiException exception) {
            System.err.println("API Exception: " + exception.getMessage());
        }
    }

    @FXML
    private void chooseMode(ActionEvent e) {
        String mode = ((Button) e.getSource()).getId();
        switch (mode) {
            case "walking":
                DirectionsController.setMode(TravelMode.WALKING);
                break;

            case "bike":
                DirectionsController.setMode(TravelMode.BICYCLING);
                break;

            case "transit":
                DirectionsController.setMode(TravelMode.TRANSIT);
                break;

            default:
                DirectionsController.setMode(TravelMode.DRIVING);
                break;
        }
    }

    @FXML
    private void toDefault(ActionEvent event) {
        DirectionsController.close();
        // TODO: Fix exit and reload
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
