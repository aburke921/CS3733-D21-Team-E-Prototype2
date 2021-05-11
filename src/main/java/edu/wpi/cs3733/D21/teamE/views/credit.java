package edu.wpi.cs3733.D21.teamE.views;

import edu.wpi.cs3733.D21.teamE.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class credit {
    @FXML
    private void exit(ActionEvent actionEvent){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
