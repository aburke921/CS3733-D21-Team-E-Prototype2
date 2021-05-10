package edu.wpi.cs3733.D21.teamE.views;

import com.google.maps.model.TravelMode;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamE.App;
import javafx.beans.binding.DoubleBinding;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

/**
 * Class to display directions to/from BWH using the Google Maps API
 */
public class DirectionsController {

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
    private JFXButton currentlySelected; // Currently selected travel selection button

    @FXML // fix:id="backButton"
    public Button backButton; // Value injected by FXMLLoader
    @FXML // fx:id="lowerAnchorPane"
    public AnchorPane lowerAnchorPane;

    @FXML // fx:id="toBWH"
    public JFXButton toBWH; // Get Directions to BWH button
    @FXML // fx:id="awayBWH"
    public JFXButton awayBWH; // Get Directions from BWH button

    @FXML // fx:id="heading"
    public Label heading;
    @FXML // fx:id="info"
    public Label info;
    @FXML // fx:id="listing"
    public Pane listing;
    @FXML // fx:id="topper"
    public VBox topper;

    Stage primaryStage;

    /**
     * Singleton object
     */
    DirectionsEntity directionsEntity = null; // DirectionsEntity, Maps API interface

    /**
     * JavaFX Init method
     */
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

        primaryStage = App.getPrimaryStage();

        

        leftAnchorPane.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(primaryStage.widthProperty());
            }

            @Override
            protected double computeValue() {
                return primaryStage.widthProperty().getValue() * 2 / 5;
            }
        });

        imageStackPane.prefWidthProperty().bind(new DoubleBinding() {
            {
                super.bind(primaryStage.widthProperty());
            }

            @Override
            protected double computeValue() {
                return primaryStage.widthProperty().getValue() * 3 / 5;
            }
        });

        imageStackPane.maxHeightProperty().bind(primaryStage.heightProperty().subtract(appBarAnchorPane.heightProperty()));
        leftAnchorPane.maxHeightProperty().bind(primaryStage.heightProperty().subtract(appBarAnchorPane.heightProperty()));

        directionsEntity = DirectionsEntity.getInstance();

        currentlySelected = car;

        toBWH.disableProperty().bind(address.textProperty().isEmpty());
        awayBWH.disableProperty().bind(address.textProperty().isEmpty());

        Image hospital = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
        hospitalImageView.setImage(hospital);
        hospitalImageView.setPreserveRatio(true);

        imageAnchorPane.setCenterShape(true);

        imageAnchorPane.minHeightProperty().bind(imageStackPane.heightProperty());
        imageAnchorPane.prefHeightProperty().bind(imageStackPane.heightProperty());
        hospitalImageView.fitHeightProperty().bind(imageAnchorPane.heightProperty());
        heading.setMaxWidth(370);
        heading.setPrefWidth(370);
        heading.setMinWidth(370);
        heading.setWrapText(true);

        primaryStage.setWidth(primaryStage.getWidth() + 0.00001);

    }

    /**
     * Gets directions to/from BWH
     * Displays them in a dialog
     * Reads text fields
     *
     * @param toBWH did the user request directions to or from BWH
     */
    @FXML
    private void getDirections(Boolean toBWH) {
        StringBuilder origin = new StringBuilder();
        origin.append(address.textProperty().get()).append(" ");
        origin.append(city.textProperty().get()).append(", ");
        origin.append(state.textProperty().get()).append(" ");
        origin.append(zip.textProperty().get());

        List<String> directions = directionsEntity.getDirections(origin.toString(), toBWH);
        if (directions == null) {
            System.err.println("No Directions Found");
            return;
        }
        String URL = directions.remove(0);
        String header = directions.remove(0);
        String details = header.substring(header.indexOf("\n")+2);
        header = header.split("\n")[0];
        heading.setText(header);
        info.setText(details);

        JFXListView<String> dirList = new JFXListView();

        dirList.getItems().addAll(directions);
        dirList.setSelectionModel(new NoSelectionModel<String>());

        dirList.maxHeightProperty().bind(listing.heightProperty());
        dirList.prefWidthProperty().bind(listing.widthProperty());

        dirList.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);

                } else {

                    setMaxWidth(listing.getWidth()- 30 );
                    setPrefWidth(listing.getWidth() - 30);
                    setMinWidth(listing.getWidth() - 30);

                    // allow wrapping
                    setWrapText(true);

                    setText(item.toString());


                }
            }
        });

        listing.getChildren().removeAll();
        listing.getChildren().add(dirList);
        listing.setVisible(true);
        topper.setVisible(true);

        Double width = imageStackPane.widthProperty().getValue() - 135;
        Double height = 395.0;

        WebView webView = new WebView();
        webView.getEngine().loadContent("<iframe width='" + width.toString() + "' height='" + height.toString()  + "' src=" + URL + " />");
        webView.setStyle("-fx-border-width: 0;" +
                "-fx-border-insets: 0;" +
                "-fx-border-radius: 0;" +
                "-fx-border-color: TRANSPARENT;");

        webView.getStyleClass().add("scroll-bar");

        JFXDialogLayout popup = new JFXDialogLayout();
        Text heading = new Text("Map Directions");
        heading.setFont(Font.font(null, FontWeight.BOLD, 18));
        popup.setHeading(heading);
        popup.setBody(webView);
        popup.setAlignment(Pos.CENTER);
        popup.setPrefHeight(USE_COMPUTED_SIZE);
        popup.getStyleClass().add("jfx-dialog");
        JFXDialog dialog = new JFXDialog(imageStackPane, popup, JFXDialog.DialogTransition.CENTER);
        dialog.getStyleClass().add("jfx-dialog");

        dialog.prefWidthProperty().bind(imageStackPane.widthProperty().subtract(75));
        popup.prefWidthProperty().bind(imageStackPane.widthProperty().subtract(75));
        dialog.setMaxHeight(550);
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

    /**
     * Button handler for directions to BWH
     *
     * @param actionEvent Button click action
     */
    public void toBWH(ActionEvent actionEvent) {
        getDirections(true);
    }

    /**
     * Button handler for directions from BWH
     *
     * @param actionEvent Button click action
     */
    public void awayBWH(ActionEvent actionEvent) {
        getDirections(false);
    }

    /**
     * Switches which travel method button is being focused
     *
     * @param button The button to change the highlight to
     */
    private void switchFocusButton(JFXButton button) {
        currentlySelected.getStyleClass().remove("transit-button-selected");
        currentlySelected.getStyleClass().add("transit-button-unselected");
        currentlySelected = button;
        currentlySelected.getStyleClass().remove("transit-button-unselected");
        currentlySelected.getStyleClass().add("transit-button-selected");
    }

    /**
     * Switches the travel mode
     *
     * @param e Button click action
     */
    @FXML
    private void chooseMode(ActionEvent e) {
        String mode = ((Button) e.getSource()).getId();
        switch (mode) {
            case "walking":
                directionsEntity.setMode(TravelMode.WALKING);
                switchFocusButton(walking);
                break;

            case "bike":
                directionsEntity.setMode(TravelMode.BICYCLING);
                switchFocusButton(bike);
                break;

            case "transit":
                directionsEntity.setMode(TravelMode.TRANSIT);
                switchFocusButton(transit);
                break;

            default:
                directionsEntity.setMode(TravelMode.DRIVING);
                switchFocusButton(car);
                break;
        }
    }

    /**
     * Switches scene back to default page
     *
     * @param event Button click event
     */
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
