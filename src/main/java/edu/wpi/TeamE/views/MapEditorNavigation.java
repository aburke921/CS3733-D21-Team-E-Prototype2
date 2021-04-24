package edu.wpi.TeamE.views;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.shape.Polygon;

import java.io.IOException;

public class MapEditorNavigation {

    @FXML // fx:id="exit"
    private Polygon exit;

    public void initialize() {
        //If exit button is clicked, exit app
        exit.setOnMouseClicked(event -> {
            App app = new App();
            app.stop();
        });
    }

    @FXML
    private void toDefault(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void toNodeMapEditor(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/MapEditor.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void toNodeFileUpload(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/MapFileUpload.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void toEdgeMapEditor(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/EdgeMapEditor.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void toEdgeFileUpload(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/EdgeFileUpload.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void getHelpDefault(ActionEvent actionEvent) {
    }
}

