package edu.wpi.cs3733.D21.teamE.views;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TravelMode;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamE.App;
import javafx.beans.binding.DoubleBinding;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.*;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static org.apache.commons.lang3.text.WordUtils.wrap;


public class Directions {

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane;
    @FXML // fx:id="stackPane"
    private StackPane stackPane; //main stack pane used for JFXDialog popups
    @FXML // fx:id="leftAnchorPane"
    private AnchorPane leftAnchorPane;

    @FXML // fx:id="imageView"
    private ImageView hospitalImageView;
    @FXML // fx:id="imageAnchorPane"
    private AnchorPane imageAnchorPane;
    @FXML // fx:id="imageStackPane"
    private StackPane imageStackPane;

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

    DirectionsController directControl = null; // Directions Controller, Maps API interface


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

        Stage primaryStage = App.getPrimaryStage();

        leftAnchorPane.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind( primaryStage.widthProperty() );
            }
            @Override
            protected double computeValue() {
                return primaryStage.widthProperty().getValue() * 2 / 5;
            }
        });

        imageStackPane.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind( primaryStage.widthProperty() );
            }
            @Override
            protected double computeValue() {
                return primaryStage.widthProperty().getValue() * 3 / 5;
            }
        });

        directControl = DirectionsController.getInstance();

        car.setStyle("-fx-background-color: -fx--primary");
        bike.setStyle("-fx-background-color: -fx--primary-light");
        walking.setStyle("-fx-background-color: -fx--primary-light");
        transit.setStyle("-fx-background-color: -fx--primary-light");
        currentlySelected = car;


        Image hospital = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
        hospitalImageView.setImage(hospital);
        hospitalImageView.setPreserveRatio(true);

        imageAnchorPane.setCenterShape(true);

        imageAnchorPane.prefHeightProperty().bind(primaryStage.heightProperty());
        hospitalImageView.fitHeightProperty().bind(imageAnchorPane.heightProperty());
    }

    @FXML
    private void getDirections(Boolean toBWH) {
        StringBuilder origin = new StringBuilder();
        origin.append(address.textProperty().get()).append(" ");
        origin.append(city.textProperty().get()).append(", ");
        origin.append(state.textProperty().get()).append(" ");
        origin.append(zip.textProperty().get());

        List<String> directions = directControl.getDirections(origin.toString(), toBWH);
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

        listView.setCellFactory(param -> new ListCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item==null) {
                    setGraphic(null);
                    setText(null);

                }else{

                    // set the width's
                    setMinWidth(param.getWidth() - 25);
                    setMaxWidth(param.getWidth() - 25);
                    setPrefWidth(param.getWidth() - 25);

                    // allow wrapping
                    setWrapText(true);

                    setText(item.toString());


                }
            }
        });

        JFXDialogLayout popup = new JFXDialogLayout();
        popup.setHeading(new Text(header));
        popup.setBody(listView);
        popup.setPrefHeight(USE_COMPUTED_SIZE);
        popup.getStyleClass().add("jfx-dialog-overlay-pane");
        JFXDialog dialog = new JFXDialog(imageStackPane, popup, JFXDialog.DialogTransition.CENTER);
        dialog.getStyleClass().add("jfx-dialog-overlay-pane");

        dialog.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(imageStackPane.widthProperty());
            }
            @Override
            protected double computeValue() {
                return imageStackPane.widthProperty().getValue() - 150;
            }
        });
        popup.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(imageStackPane.widthProperty());
            }
            @Override
            protected double computeValue() {
                return imageStackPane.widthProperty().getValue() - 150;
            }
        });

        int fullSize = listView.getItems().size() * 45 + 120;
        if (fullSize > 600) {
            dialog.setMaxHeight(600);
        } else {
            dialog.setMaxHeight(fullSize);
        }
        JFXButton okay = new JFXButton("Close");
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
        currentlySelected.setStyle("-fx-background-color: -fx--primary-light");
        currentlySelected = button;
        currentlySelected.setStyle("-fx-background-color: -fx--primary");
    }

    @FXML
    private void chooseMode(ActionEvent e) {
        String mode = ((Button) e.getSource()).getId();
        switch (mode) {
            case "walking":
                directControl.setMode(TravelMode.WALKING);
                switchFocusButton(walking);
                break;

            case "bike":
                directControl.setMode(TravelMode.BICYCLING);
                switchFocusButton(bike);
                break;

            case "transit":
                directControl.setMode(TravelMode.TRANSIT);
                switchFocusButton(transit);
                break;

            default:
                directControl.setMode(TravelMode.DRIVING);
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
