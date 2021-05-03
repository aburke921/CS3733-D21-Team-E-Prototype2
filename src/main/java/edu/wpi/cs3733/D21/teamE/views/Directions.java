package edu.wpi.cs3733.D21.teamE.views;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamE.App;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.apache.commons.lang3.text.WordUtils.wrap;


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
    private JFXButton currentlySelected;
    
    @FXML // fix:id="backButton"
    public Button backButton; // Value injected by FXMLLoader


    public void initialize() {
        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false);
            App.setShowLogin(true);
            App.setStackPane(stackPane);
            App.setPageTitle("Directions to BWH");
            App.setHelpText(""); //todo help text
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentlySelected = car;
        currentlySelected.getStyleClass().remove("transit-button-unselected");
        currentlySelected.getStyleClass().add("transit-button-selected");
        DirectionsController.init();
    }

    @FXML
    private void getDirections(Boolean toBWH) {
        StringBuilder origin = new StringBuilder();
        origin.append(address.textProperty().get()).append(" ");
        origin.append(city.textProperty().get()).append(", ");
        origin.append(state.textProperty().get()).append(" ");
        origin.append(zip.textProperty().get());

        //System.out.println("\nEnter Desired Time of Arrival");
        //String arrivalTime = io.nextLine();
        // TODO: Figure out time scheduler

        List<String> directions = DirectionsController.getDirections(origin.toString(), toBWH);
        if (directions == null) {
            System.err.println("No Directions Found");
            return;
        }

        JFXListView<String> listView = new JFXListView<>();
        String header = directions.remove(0);
        listView.getItems().addAll(directions);
        listView.setPrefHeight(USE_COMPUTED_SIZE);
        listView.setSelectionModel(new NoSelectionModel<String>());
        listView.getStyleClass().add("directions");

        JFXDialogLayout popup = new JFXDialogLayout();
        popup.setHeading(new Text(header));
        popup.setBody(listView);
        popup.setPrefHeight(USE_COMPUTED_SIZE);
        JFXDialog dialog = new JFXDialog(stackPane, popup, JFXDialog.DialogTransition.CENTER);
        dialog.getStyleClass().add("directionsDialog");

        dialog.setMaxWidth(800);
        dialog.setPrefWidth(800);
        popup.setMaxWidth(800);
        popup.setPrefWidth(800);

        int fullSize = listView.getItems().size() * 45 + 120;
        if (fullSize > 500) {
            dialog.setMaxHeight(500);
        } else {
            dialog.setMaxHeight(fullSize);
        }
        JFXButton okay = new JFXButton("Done");
        okay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();

            }
        });
        popup.setActions(okay);
        dialog.show();

    }

    public void toBWH(ActionEvent actionEvent) {
        getDirections(true);
    }

    public void awayBWH(ActionEvent actionEvent) {
        getDirections(false);
    }

    private void switchFocusButton(JFXButton button) {
        currentlySelected.getStyleClass().remove("transit-button-selected");
        currentlySelected.getStyleClass().add("transit-button-unselected");
        currentlySelected = button;
        currentlySelected.getStyleClass().remove("transit-button-unselected");
        currentlySelected.getStyleClass().add("transit-button-selected");
    }

    @FXML
    private void chooseMode(ActionEvent e) {
        String mode = ((Button) e.getSource()).getId();
        switch (mode) {
            case "walking":
                DirectionsController.setMode(TravelMode.WALKING);
                switchFocusButton(walking);
                break;

            case "bike":
                DirectionsController.setMode(TravelMode.BICYCLING);
                switchFocusButton(bike);
                break;

            case "transit":
                DirectionsController.setMode(TravelMode.TRANSIT);
                switchFocusButton(transit);
                break;

            default:
                DirectionsController.setMode(TravelMode.DRIVING);
                switchFocusButton(car);
                break;
        }
    }

    @FXML
    private void toDefault(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
