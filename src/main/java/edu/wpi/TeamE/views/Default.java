package edu.wpi.TeamE.views;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class Default {

    @FXML // fx:id="imageView"
    private ImageView imageView;

    @FXML // fx:id="exit"
    private Polygon exit;

    @FXML // fx:id="imageView"
    private Circle hide;

    @FXML // fx:id="imageView"
    private Rectangle fullscreen;

    /**
     * Move to Service Request page
     * @param e
     */
    @FXML
    private void toServiceRequests(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/ServiceRequests.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Move to Map Editor page
     * @param e
     */
    @FXML
    private void toMapEditor(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/newMapEditor.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Move to Path Finder page
     * @param e
     */
    @FXML
    private void toPathFinder(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/PathFinder.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void toServiceRequestStatus(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/ServiceRequestStatus.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void toCovidSurvey(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/CovidSurvey.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void getHelpDefault(ActionEvent actionEvent) {
    }

    public void initialize() {
        Image image = new Image("edu/wpi/TeamE/logo.png");
        imageView.setImage(image);

        //If exit button is clicked, exit app
        exit.setOnMouseClicked(event -> {
            App app = new App();
            app.stop();
        });
    }

}

