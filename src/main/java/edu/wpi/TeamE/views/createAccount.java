package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamE.App;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class createAccount {

    @FXML
    private JFXTextField firstName;
    @FXML
    private JFXTextField lastName;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField password;


    public void createAccountButton() {
        boolean bool = false;
        makeConnection connection = makeConnection.makeConnection();
        if (firstName != null && lastName != null && email != null && password != null) {
            connection.addUserAccount(email.getText(), password.getText(), firstName.getText(), lastName.getText());
            //bool = connection.userLogin(email.getText(), password.getText());
        }
        //if (bool == true) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //} else {
        // System.out.println("Error creating user");
    }


    @FXML
    public void getHelpDefault(ActionEvent actionEvent) {
    }


    /**
     * Terminate application
     * @param e
     */
    @FXML
    private void shutdown(ActionEvent e) {
        App app = new App();
        app.stop();
    }
}
