package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.TeamE.App;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class Login {

    @FXML private JFXTextField emailInput;
    @FXML private JFXTextField passwordInput;

	public void submitLogin() {
		int userID = 0;
		makeConnection connection = makeConnection.makeConnection();
		if (emailInput != null && passwordInput != null) {
			userID = connection.userLogin(emailInput.getText(), passwordInput.getText());
			App.userID = userID; // app will be logged in as guest if userID = 0
		}
		if (userID != 0) {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
				App.getPrimaryStage().getScene().setRoot(root);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			System.out.println("User not in System");
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



