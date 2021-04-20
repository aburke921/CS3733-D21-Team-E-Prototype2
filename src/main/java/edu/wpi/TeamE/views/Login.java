package edu.wpi.TeamE.views;

import com.jfoenix.controls.*;
import edu.wpi.TeamE.App;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import javax.swing.*;
import java.io.IOException;

public class Login {

    public Button submitLoginButton;
    @FXML private JFXTextField emailInput;
    @FXML private JFXPasswordField passwordInput;
    @FXML private StackPane stackPane;

    @FXML // fx:id="exit"
    private Polygon exit;

    public void initialize() {
        //If exit button is clicked, exit app
        exit.setOnMouseClicked(event -> {
            App app = new App();
            app.stop();
        });

        //set submit as default (will submit on an "ENTER" keypress)
        submitLoginButton.setDefaultButton(true);
    }

    public void submitLogin() {
        boolean bool = false;
        makeConnection connection = makeConnection.makeConnection();
        if(emailInput.getText().isEmpty()) {
            errorPopup("Must input an email");
            return;
        }
        if(passwordInput.getText().isEmpty()) {
            errorPopup("Must input password");
            return;
        }
        if(emailInput != null && passwordInput != null) {
            bool = connection.userLogin(emailInput.getText(), passwordInput.getText());
        } if(bool == true) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            errorPopup("Incorrect Email or Password");
            return;
    }

}

    public void toNewAccount(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/createAccount.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void getHelpDefault(ActionEvent actionEvent) {
    }

    @FXML
    public void guestLogin(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void errorPopup(String errorMessage) {
        JFXDialogLayout error = new JFXDialogLayout();
        error.setHeading(new Text("Error!"));
        error.setBody(new Text(errorMessage));
        JFXDialog dialog = new JFXDialog(stackPane, error, JFXDialog.DialogTransition.CENTER);
        JFXButton okay = new JFXButton("Okay");
        okay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();

            }
        });
        error.setActions(okay);
        dialog.show();
    }
}



