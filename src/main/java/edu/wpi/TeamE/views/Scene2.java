package edu.wpi.TeamE.views;

import edu.wpi.TeamE.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Scene2 {

  @FXML
  private void advanceScene(ActionEvent e) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Scene3.fxml"));
      App.getPrimaryStage().getScene().setRoot(root);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
